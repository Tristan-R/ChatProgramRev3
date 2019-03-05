import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;

/**
 * ServerThread Object
 * <p>
 * Handles user input on the server side.
 */
class ServerThread extends MsgControl {

    /**
     * Stores the socket that the server is running on.
     */
    private ServerSocket server;

    /**
     * Constructor. Creates a new user input stream and an output stream.
     *
     * @param server
     *      The socket that the server is connected on.
     */
    ServerThread(ServerSocket server) {
        this.server = server;
        in = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(System.out, true);
        serverWriter = out;
    }

    /**
     * Sets the name of the server.
     */
    @Override
    void setName() {
        name = "server";
        admins.add(name);
    }

    /**
     * @return
     *      Whether to end the thread.
     */
    @Override
    boolean endThread() {
        return server.isClosed();
    }

    /**
     * Processes user input.
     *
     * @param message
     *      The user input message to process. All commands start with a /
     *      except for EXIT.
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

                case "/promote":
                    if (parts.length == 2) {
                        promote(parts[1]);

                    } else {
                        brokenMsg();
                    }
                    break;

                case "/demote":
                    if (parts.length == 2) {
                        demote(parts[1]);

                    } else {
                        brokenMsg();
                    }
                    break;

                case "/broadcast":
                    if (parts.length == 2) {
                        msgAll(name, parts[1]);

                    } else {
                        brokenMsg();
                    }
                    break;

                default:
                    brokenMsg();
            }
        } else if (message.equals("EXIT")) {
            exit();

        } else {
            brokenMsg();
        }
    }

    /**
     * Attempts to close all clients, closes the ServerSocket then exits the
     * program.
     */
    @Override
    void exit() {
        try {
            msgAll("server", "Server shutting down.");
            Thread.sleep(1000);
            for (String client : clients.keySet()) {
                PrintWriter clientOut = clients.remove(client);
                admins.remove(client);
                String removeMsg = msgBuilder(0, "server", "null");
                clientOut.println(removeMsg);
                kickModify("Add", client);
            }
            server.close();
            out.println("Server disconnected.");
            Thread.sleep(2000); // Pause to allow all client threads to end.

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();

        } finally {
            System.exit(0);
        }

    }

    /**
     * Informs the user if the command they entered could not be processed.
     */
    @Override
    void brokenMsg() {
        out.println("Your last message could not be processed.");
    }

    /**
     * Prints a message from the user.
     *
     * @param message
     *      The message to print.
     */
    @Override
    void msgServer(String message) {
        out.println(message);
    }

    /**
     * Sends a message to all clients.
     *
     * @param from
     *      The server's name.
     *
     * @param message
     *      The message to send to all clients.
     */
    @Override
    void msgAll(String from, String message) {
        String messageOut = msgBuilder(2, name, message);

        for (String client : clients.keySet()) {
            clients.get(client).println(messageOut);
        }
    }

    /**
     * Sends a message to a single client.
     *
     * @param name
     *      The server's name.
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
                out.println("Could not find this user.");
            }
        } else {
            brokenMsg();
        }
    }

    /**
     * Prints a list of all clients connected to the server and specifies admin
     * if that client is an admin.
     */
    @Override
    void getClientsList() {
        String list = "";
        int i = 0;
        for (String client : clients.keySet()) {
            list = list.concat("     " + client);

            if (admins.contains(client)) {
                list = list.concat("(admin)");
            }

            if (++i % 4 == 0) {
                list = list.concat("\n");
            }
        }

        out.println(list);
    }

    /**
     * Removes a client from the server.
     *
     * @param kickedBy
     *      The server's name.
     *
     * @param toKick
     *      The client to be removed from the server.
     */
    @Override
    void kick(String kickedBy, String toKick) {
        if (clients.containsKey(toKick)) {
            PrintWriter kickClient = clients.remove(toKick);
            admins.remove(toKick);
            String removeMsg = msgBuilder(5, "server", "null");
            kickClient.println(removeMsg);
            kickModify("Add", toKick);

        } else {
            out.println("Could not find this user.");
        }
    }

    /**
     * Promotes a client to admin status.
     *
     * @param client
     *      The client to promote.
     */
    private void promote(String client) {
        if (clients.containsKey(client)) {
            if (admins.contains(client)) {
                out.println("This user is already an admin.");

            } else {
                admins.add(client);
            }
        } else {
            out.println("Could not find this user.");
        }
    }

    /**
     * Demotes a client from admin status.
     *
     * @param client
     *      The client to demote.
     */
    private void demote(String client) {
        if (clients.containsKey(client)) {
            if (admins.contains(client)) {
                admins.remove(client);

            } else {
                out.println("This user is not an admin.");
            }
        } else {
            out.println("Could not find this user.");
        }
    }
}
