export interface MessageAck {
    id: string;
    received: string;
    ack: string
}


export interface Message {
    id?: string;
    payload: string;
    delay: number;
    responseCode: number;
}