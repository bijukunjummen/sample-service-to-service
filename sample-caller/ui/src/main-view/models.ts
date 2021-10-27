export interface MessageAck {
    id: string;
    received: string;
    callerHeaders: Map<string, string[]>;
    producerHeaders: Map<string, string[]>;
    statusCode: number;
    roundTripTimeMillis: number;
}


export interface Message {
    id?: string;
    payload: string;
    delay: number;
    responseCode: number;
}