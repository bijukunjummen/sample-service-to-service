module "asm-primary" {
  source           = "terraform-google-modules/kubernetes-engine/google//modules/asm"
  version          = "17.3.0"
  project_id       = data.google_client_config.current.project
  cluster_name     = module.primary-cluster.name
  location         = module.primary-cluster.location
  cluster_endpoint = module.primary-cluster.endpoint
  outdir          = "asm-dir-${module.primary-cluster.name}"
}

module "asm-secondary" {
  source           = "terraform-google-modules/kubernetes-engine/google//modules/asm"
  version          = "17.3.0"
  project_id       = data.google_client_config.current.project
  cluster_name     = module.secondary-cluster.name
  location         = module.secondary-cluster.location
  cluster_endpoint = module.secondary-cluster.endpoint
  outdir = "asm-dir-${module.secondary-cluster.name}"
}