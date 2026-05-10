package sample.common.service;

public record ClusterMetadata(
        String clusterName,
        String clusterLocation,
        String hostName,
        String ipAddress,
        String zone,
        String region
) {
    public ClusterMetadata(String clusterName, String clusterLocation) {
        this(clusterName, clusterLocation, "", "", "", "");
    }

    public ClusterMetadata(String clusterName, String clusterLocation, String hostName) {
        this(clusterName, clusterLocation, hostName, "", "", "");
    }
}
