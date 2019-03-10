import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 * ClientThread Object
 * <p>
 * Handles messages from clients on the server side.
 */
class ClientThread extends MsgControl {

    /**
     * Stores whether the thread should end.
     */
    private boolean shutdown = false;

    /**
     * Constructor.
     *
     * @param socket
     *      The socket that the client is connected on.
     */
    ClientThread(Socket socket) {
        super(socket);
    }

    /**
     * Asks the client for a name and sets the name. Names must not be taken by
     * another client and they must not exceed 15 characters or contain any of
     * the characters: ~ < > : ;
     */
    @Override
    void setName() {
        try {
            String testName;
            String message;
            while (true) {
                out.println("Please enter a username (max 15 characters):");
                message = in.readLine();
                String[] parts = message.split("~", 2);

                if (parts.length == 2 && parts[0].equals("2")) {
                    testName = parts[1];

                    if (testName.length() <= 15) {
                        if (testName.matches("(.*)[~<>:;](.*)")) {
                            out.println("Name must not contain ~ < > : ;");

                        } else {
                            if (clients.containsKey(testName)) {
                                out.println("Name already in use.");

                            } else {
                                name = testName;
                                clients.put(name, out);
                                out.println("READY:" + name);
                                break;
                            }
                        }
                    }
                } else if (parts[0].equals("0")) {
                    shutdown = true;
                    break;

                } else {
                    out.println("Invalid name.");
                }
            }
        } catch (SocketException e) {
            if (!endThread()) {
                System.out.println("Connection to socket lost.");
                shutdown = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
     * Processes input from the client.
     *
     * @param message
     *      The message received from the client. The input will be in the
     *      form: (identifier)~(message)
     */
    @Override
    void processMsg(String message) {
        String[] parts = message.split("~", 2);
        String identifier;
        if (parts.length == 2) {
            identifier = parts[0];

        } else {
            identifier = "-1";
        }

        switch (identifier) {
            case "-1":
                brokenMsg();
                break;

            case "0":
                try {
                    socket.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case "1":
                msgServer(parts[1]);
                break;

            case "2":
                msgAll(name, parts[1]);
                break;

            case "3":
                msgDirect(name, parts[1]);
                break;

            case "4":
                getClientsList();
                break;

            case "5":
                kick(name, parts[1]);
                break;

            default:
                brokenMsg();
        }
    }

    /**
     * Closes the socket, removes the client from the clients list and exits
     * the program.
     */
    @Override
    void exit() {
        try {
            if (name != null) {
                clients.remove(name);
            }
            socket.close();
            System.out.println("Client has disconnected on " + socket.getLocalPort() + " : " + socket.getPort());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Informs the client if the message they sent could not be processed.
     * On the fourth failed message the connection will be terminated due to
     * a weak connection.
     */
    @Override
    void brokenMsg() {
        if (++brokenMsgCount > 3) {
            String returnMsg = msgBuilder(0, "server", "Weak connection to server, disconnecting...");
            out.println(returnMsg);
            exit();

        } else {
            String returnMsg = msgBuilder(1, "server", "We could not process your last message.");
            out.println(returnMsg);
        }
    }

    /**
     * Prints a message to serverWriter if the client is an admin.
     *
     * @param message
     *      The message to print to the server.
     */
    @Override
    void msgServer(String message) {
        if (admins.contains(name)) {
            serverWriter.println(name + " > " + message);

        } else {
            String returnMsg = msgBuilder(1, "server", "You do not have permission to do that.");
            out.println(returnMsg);
        }
    }

    /**
     * Sends a message to all clients.
     *
     * @param name
     *      The name of the client sending the message.
     *
     * @param message
     *      The message to be sent.
     */
    @Override
    void msgAll(String name, String message) {
        String messageOut = msgBuilder(2, name, message);

        for (String client : clients.keySet()) {
            if (!client.equals(name)) {
                clients.get(client).println(messageOut);
            }
        }
    }

    /**
     * Sends a message to a single client.
     *
     * @param name
     *      The name of the client who sent the message.
     *
     * @param message
     *      The client to send the message to and the message to be sent to the
     *      client in the format: (client):(message)
     */
    @Override
    void msgDirect(String name, String message) {
        String[] parts = message.split(":", 2);
        if (parts.length == 2) {
            if (clients.containsKey(parts[0])) {
                String messageOut = msgBuilder(3, name, parts[1]);
                clients.get(parts[0]).println(messageOut);

            } else {
                String messageOut = msgBuilder(1, "server", "There is not a user with this name.");
                out.println(messageOut);
            }
        } else {
            brokenMsg();
        }
    }

    /**
     * Returns a list of all clients connected to the server.
     */
    @Override
    void getClientsList() {
        String list = "";
        String messageOut = msgBuilder(4, "null", "CLIENTS:\n");
        int i = 0;
        for (String client : clients.keySet()) {
            list = list.concat("     " + client);

            if (++i % 4 == 0) {
                if (i != 4) {
                    messageOut = messageOut.concat("\n");
                }
                messageOut = messageOut.concat(msgBuilder(4, "null", list));
                list = "";
            }
        }
        if (i % 4 != 0) {
            if (i > 4) {
                messageOut = messageOut.concat("\n");
            }
            messageOut = messageOut.concat(msgBuilder(4, "null", list));
        }
        out.println(messageOut);
    }

    /**
     * Removes a client from the server if the client requesting the action is
     * an admin.
     *
     * @param kickedBy
     *      The client that removed the other client.
     *
     * @param toKick
     *      The client to be removed.
     */
    @Override
    void kick(String kickedBy, String toKick) {
        if (admins.contains(name)) {
            if (clients.containsKey(toKick)) {
                if (admins.contains(toKick)) {
                    String messageOut = msgBuilder(1, "server", "Only the server can kick admins.");
                    out.println(messageOut);

                } else {
                    PrintWriter kickClient = clients.remove(toKick);
                    String removeMsg = msgBuilder(5, "server", "null");
                    kickClient.println(removeMsg);
                    removeClients.kickModify("Add", toKick);
                }
            } else {
                String messageOut = msgBuilder(1, "server", "This user does not exist.");
                out.println(messageOut);
            }
        } else {
            String messageOut = msgBuilder(1, "server", "You do not have permission to do that.");
            out.println(messageOut);
        }
    }
}
