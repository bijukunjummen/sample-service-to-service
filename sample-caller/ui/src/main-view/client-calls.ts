import { MessageAck, Message } from './models';

export function makePassthroughCall(message: Message): Promise<MessageAck> {
    const url: string = "/caller/messages"
    return fetch(url, {
        headers: { "Content-Type": "application/json" },
        method: "POST",
        body: JSON.stringify(message)
    })
    .then((response: Response) =>  response.json() as unknown)
    .then((data) => data as MessageAck)
}