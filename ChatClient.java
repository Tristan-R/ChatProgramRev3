public class ChatClient { // Need to send '/' commands first
    /*
    Sending Commands:
        0 - EXIT
        1 - Send to server // Needs to be an authorisation on server end
        2 - Send to all
        3 - Direct message // message will have format (3)~(fromUser)~(toUser)>(message)
        4 - See current clients
        5 - Kicked from chat
        6 - Reconnect to server? // Does the server need to know it's a reconnect? Or just send EXIT?
        7 -

    Receiving Commands: // need one for name change
        0 - EXIT
        1 - Received from server // Direct messages from server should be handled server end
                                 // and the server should put (direct) before the message.
        2 - Received global message
        3 - Received direct message
        4 - List of clients
        5 - Kick command
        6 - Try reconnecting
        7 -
     */
}
