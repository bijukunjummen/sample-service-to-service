export interface MessageAck {
    id: string;
    received: string;
    ack: string;
    statusCode: number;
    roundTripTimeMillis: number;
}


export interface Message {
    id?: string;
    payload: string;
    delay: number;
    responseCode: number;
}