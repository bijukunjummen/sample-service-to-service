import { MessageAck } from './MessageAck';

export function makePassthroughCall(payload: string, delay: number): Promise<MessageAck> {
    const url = "/messages"
    return fetch(url, {
        headers: { "Content-Type": "application/json" },
        method: "POST",
        body: JSON.stringify({ payload: payload, delay: delay })
    })
    .then((response) =>  response.json())
    .then((data) => data as MessageAck)
}
