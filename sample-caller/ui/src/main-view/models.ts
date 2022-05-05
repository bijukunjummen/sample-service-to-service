export interface MessageAck {
    id: string;
    received: string;
    callerHeaders: Map<string, string[]>;
    producerHeaders: Map<string, string[]>;
    callerMetadata: ClusterMetadata;
    producerMetadata: ClusterMetadata;
    statusCode: number;
    roundTripTimeMillis: number;
}

export interface Message {
    id?: string;
    payload: string;
    delay: number;
    responseCode: number;
}

export interface ClusterMetadata {
    clusterName: string;
    clusterLocation: string;
    hostName: string;
}