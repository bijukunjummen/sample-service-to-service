import { MessageAck, Message } from './models';

export function makePassthroughCall(message: Message): Promise<MessageAck> {
    const url = "/caller/messages"
    return fetch(url, {
        headers: { "Content-Type": "application/json" },
        method: "POST",
        body: JSON.stringify(message)
    })
    .then((response) =>  response.json())
    .then((data) => data as MessageAck)
}
