import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * ClientSend Object
 * <p>
 * Handles sending messages to the server.
 */
class ClientSend extends MsgControl {

    /**
     * Stores whether the thread should end.
     */
    private boolean shutdown = false;

    /**
     * Constructor.
     *
     * @param socket
     *      The socket that the client is connected on.
     *
     * @param in
     *      The BufferedReader receiving user input.
     *
     * @param out
     *      The PrintWriter object sending messages to the server.
     */
    ClientSend(Socket socket, BufferedReader in, PrintWriter out) {
            this.socket = socket;
            this.in = in;
            this.out = out;
    }

    /**
     * Unused.
     */
    @Override
    void setName() {
    }

    /**
     * @return
     *      Whether to end the thread.
     */
    @Override
    boolean endThread() {
        return socket.isClosed() || shutdown;
    }

    /**
     * Processes user input.
     *
     * @param message
     *      The user input message to process. All commands start with a /
     *      except for EXIT and global messages that start don't start with
     *      any prefix.
     */
    @Override
    void processMsg(String message) {
        if (message.startsWith("/")) {
            String[] parts = message.split(" ", 2);

            switch (parts[0]) {
                case "/server":
                    if (parts.length == 2) {
                        msgServer(parts[1]);

                    } else {
                        brokenMsg();
                    }
                    break;

                case "/direct":
                    if (parts.length == 2) {
                        msgDirect(name, parts[1]);

                    } else {
                        brokenMsg();
                    }
                    break;

                case "/clients":
                    getClientsList();
                    break;

                case "/kick":
                    if (parts.length == 2) {
                        kick(name, parts[1]);

                    } else {
                        brokenMsg();
                    }
                    break;

                default:
                    brokenMsg();
            }
        } else if (message.equals("EXIT")) {
            shutdown = true;

        } else {
            msgAll("null", message);
        }
    }

    /**
     * Sends a closing message to the server and then closes the socket and
     * exits the program.
     */
    @Override
    void exit() {
        String msgOut = msgBuilder(0, "null");
        out.println(msgOut);

        try {
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Informs the user if the command they entered could not be processed.
     */
    @Override
    void brokenMsg() {
        System.out.println("Your last message could not be processed.");
    }

    /**
     * Sends a message for the server to the server.
     *
     * @param message
     *      The message to send to the server.
     */
    @Override
    void msgServer(String message) {
        String msgOut = msgBuilder(1, message);
        out.println(msgOut);
    }

    /**
     * Sends a message to all clients.
     *
     * @param name
     *      The name of the client sending the message.
     *
     * @param message
     *      The message to send.
     */
    @Override
    void msgAll(String name, String message) {
        String msgOut = msgBuilder(2, message);
        out.println(msgOut);
    }

    /**
     * Sends a message to a single client.
     *
     * @param name
     *      The client who sent the message.
     *
     * @param message
     *      The client to send the message to and the message to be sent to the
     *      client in the format: (client):(message)
     */
    @Override
    void msgDirect(String name, String message) {
        String msgOut = msgBuilder(3, message);
        out.println(msgOut);
    }

    /**
     * Requests a list of all currently connected clients.
     */
    @Override
    void getClientsList() {
        String msgOut = msgBuilder(4, "null");
        out.println(msgOut);
    }

    /**
     * Sends a request to remove a client from the server.
     *
     * @param kickedBy
     *      The client that removed the other client.
     *
     * @param toKick
     *      The client to be removed from the server.
     */
    @Override
    void kick(String kickedBy, String toKick) {
        String msgOut = msgBuilder(5, toKick);
        out.println(msgOut);
    }
}
