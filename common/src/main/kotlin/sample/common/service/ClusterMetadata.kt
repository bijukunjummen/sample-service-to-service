package sample.common.service

data class ClusterMetadata(
    val clusterName: String,
    val clusterLocation: String,
    val hostName: String = "",
    val ipAddress: String = "",
    val zone: String = "",
    val region: String = ""
)