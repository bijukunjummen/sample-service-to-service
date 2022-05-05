package sample.common.service

data class ClusterMetadata(
    val clusterName: String,
    val clusterLocation: String,
    val hostName: String = ""
)