## Steps:
1. Assuming that a GCP project is available, run the script sample-anthos-multi-ingress.sh, which does the following:
   * Create two GKE clusters, one in us-west1-a, another in europe-north1-a
   * Installs anthos components into the two clusters and registers them with the fleet in the project
   * Installs service mesh
   * Enables Ingress
   * Enables "istio-apps" namespace to be eligible for istio injection
2. Set some initial environment variables:
    ```shell
    export PROJECT=$(gcloud config get-value project)
    export PROJECT_1=${PROJECT}
    export LOCATION_1=us-west1-a
    export CLUSTER_1=cluster1
    export CTX_1="gke_${PROJECT_1}_${LOCATION_1}_${CLUSTER_1}"
    
    export PROJECT_2=${PROJECT}
    export LOCATION_2=europe-north1-a
    export CLUSTER_2=cluster2
    export CTX_2="gke_${PROJECT_2}_${LOCATION_2}_${CLUSTER_2}"    
    ```
3. Using kubectl commands install "caller" components in us-west1-a
    ```shell
    kubectl --context=$CTX_1 -n istio-apps apply -f sample-caller/kube 
    kubectl --context=$CTX_1 -n istio-apps apply -f sample-caller/kube-anthos 
   ```
   
4. Install "caller" & "producer" components in europe-north1-a
    ```shell
    kubectl --context=$CTX_1 -n istio-apps apply -f sample-caller/kube 
    kubectl --context=$CTX_2 -n istio-apps apply -f sample-producer/kube 
   ```