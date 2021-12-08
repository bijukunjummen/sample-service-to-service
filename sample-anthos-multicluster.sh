export PROJECT=$(gcloud config get-value project)
export PROJECT_1=${PROJECT}
export LOCATION_1=us-west1-a
export CLUSTER_1=cluster1
export CTX_1="gke_${PROJECT_1}_${LOCATION_1}_${CLUSTER_1}"

export PROJECT_2=${PROJECT}
export LOCATION_2=us-central1-a
export CLUSTER_2=cluster2
export CTX_2="gke_${PROJECT_2}_${LOCATION_2}_${CLUSTER_2}"
gcloud container clusters create ${CLUSTER_1} \
    --project=${PROJECT_1} \
    --zone=${LOCATION_1} \
    --enable-ip-alias \
    --machine-type=e2-standard-4 \
    --num-nodes=2 \
    --workload-pool=${PROJECT_1}.svc.id.goog

gcloud container clusters create ${CLUSTER_2} \
    --project=${PROJECT_2} \
    --zone=${LOCATION_2} \
    --enable-ip-alias \
    --machine-type=e2-standard-4 \
    --num-nodes=2 \
    --workload-pool=${PROJECT_2}.svc.id.goog
## Download ASMCLI
curl https://storage.googleapis.com/csm-artifacts/asm/asmcli_1.11 > asmcli

## Install Anthos service mesh on both clusters..
./asmcli install \
  --project_id ${PROJECT_1} \
  --cluster_name ${CLUSTER_1} \
  --cluster_location ${LOCATION_1} \
  --fleet_id ${PROJECT} \
  --enable_all  \
  --ca mesh_ca

./asmcli install \
  --project_id ${PROJECT_2} \
  --cluster_name ${CLUSTER_2} \
  --cluster_location ${LOCATION_2} \
  --fleet_id ${PROJECT} \
  --enable_all  \
  --ca mesh_ca

## Adds Firewall rules
function join_by { local IFS="$1"; shift; echo "$*"; }

ALL_CLUSTER_CIDRS=$(gcloud container clusters list --project $PROJECT_1 --format='value(clusterIpv4Cidr)' | sort | uniq)
ALL_CLUSTER_CIDRS=$(join_by , $(echo "${ALL_CLUSTER_CIDRS}"))
ALL_CLUSTER_NETTAGS=$(gcloud compute instances list --project $PROJECT_1 --format='value(tags.items.[0])' | sort | uniq)
ALL_CLUSTER_NETTAGS=$(join_by , $(echo "${ALL_CLUSTER_NETTAGS}"))

gcloud compute firewall-rules create anthos-multicluster-pods \
    --allow=tcp,udp,icmp,esp,ah,sctp \
    --direction=INGRESS \
    --priority=900 \
    --source-ranges="${ALL_CLUSTER_CIDRS}" \
    --target-tags="${ALL_CLUSTER_NETTAGS}" --quiet


## Configure endpoint discovery:
./asmcli create-mesh \
    ${PROJECT} \
    ${PROJECT_1}/${LOCATION_1}/${CLUSTER_1} \
    ${PROJECT_2}/${LOCATION_2}/${CLUSTER_2}


# Find the right tag to apply
# Determine the ASM revision
ASM_REVISION=$(kubectl get deploy -n istio-system -l app=istiod -o jsonpath={.items[*].metadata.labels.'istio\.io\/rev'}'{"\n"}')

# Tag the namespaces - Cluster 1
kubectx gke_${PROJECT_1}_${LOCATION_1}_${CLUSTER_1}
kubectl create namespace istio-apps
kubectl label namespace istio-apps istio-injection- istio.io/rev=${ASM_REVISION} --overwrite

kubectl create namespace gw-namespace
kubectl label namespace gw-namespace \
  istio.io/rev=${ASM_REVISION} --overwrite

# Install Ingress Gateway
git clone https://github.com/GoogleCloudPlatform/anthos-service-mesh-packages.git

kubectl apply -n gw-namespace \
  -f anthos-service-mesh-packages/samples/gateways/istio-ingressgateway

# Tag the namespaces - Cluster 2
kubectx gke_${PROJECT_2}_${LOCATION_2}_${CLUSTER_2}
kubectl create namespace istio-apps
kubectl label namespace istio-apps istio-injection- istio.io/rev=${ASM_REVISION} --overwrite