import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * ClientReceive Object
 * <p>
 * Handles receiving messages from the server.
 */
class ClientReceive extends MsgControl {

    /**
     * The GUI controller object.
     */
    private ClientGUIController GUI;

    /**
     * Stores whether the GUI is being used.
     */
    private boolean usingGUI = false;

    /**
     * Constructor.
     *
     * @param socket
     *      The socket that the client is connected on.
     * @param in
     *      The BufferedReader object that receives messages from the server.
     */
    ClientReceive(Socket socket, BufferedReader in) {
            this.socket = socket;
            this.in = in;
            out = new PrintWriter(System.out, true);
    }

    /**
     * Constructor. Used by GUI, sets the input stream and output stream.
     *
     * @param socket
     *      The socket that the client is connected on.
     *
     * @param in
     *      The socket input stream.
     *
     * @param out
     *      The socket output stream.
     *
     * @param GUI
     *      The GUI controller object.
     */
    ClientReceive(Socket socket, BufferedReader in, PrintWriter out, ClientGUIController GUI) {
        usingGUI = true;
        this.GUI = GUI;
        this.socket = socket;
        this.in = in;
        this.out = out;
    }

    /**
     * Sets the name of the client.
     */
    @Override
    void setName() {
        String message;
        try {
            while (true) {
                 message = in.readLine();

                 if (message.startsWith("READY")) {
                     String[] split = message.split(":");
                     name = split[1];
                     if (usingGUI) {
                         // Updates the user's name displayed in the GUI
                         UpdateGUI.setName(GUI, name);
                     }
                     out.println("Entering chat.\n");
                     break;
                 }
                 out.println(message);
            }
        } catch (IOException e) {
            System.err.println("Connection to server lost.");
            exit();
        }
    }

    /**
     * @return
     *      Whether to end the thread.
     */
    @Override
    boolean endThread() {
        return socket.isClosed();
    }

    /**
     * Processes messages received from the server.
     *
     * @param message
     *      The message to be processed.
     */
    @Override
    void processMsg(String message) {
        String[] parts = message.split("~", 3);
        String identifier;
        if (parts.length == 3) {
            identifier = parts[0];

        } else {
            identifier = "-1";
        }

        switch (identifier) {
            case "-1":
                brokenMsg();
                break;

            case "0":
                exit();
                break;

            case "1":
                msgServer(parts[2]);
                break;

            case "2":
                msgAll(parts[1], parts[2]);
                break;

            case "3":
                msgDirect(parts[1], parts[2]);
                break;

            case "4":
                clientsList(parts[2]);
                break;

            case "5":
                kick(parts[1], parts[2]);
                break;

            default:
                brokenMsg();
        }
    }

    /**
     * Closes the socket and exits the program.
     */
    @Override
    void exit() {
        try {
            socket.close();
            System.out.println("Connection closed.");
            System.exit(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Informs the user if the last message they received could not be
     * processed and on the fourth failed message the connection to the server
     * is terminated due to a weak connection.
     */
    @Override
    void brokenMsg() {
        if (++brokenMsgCount > 3) {
            System.err.println("There is an issue with connecting to the server.");
            exit();

        } else {
            out.println("The last message received could not be processed.");
        }
    }

    /**
     * Prints a message received from the server.
     *
     * @param message
     *      The message received from the server.
     */
    @Override
    void msgServer(String message) {
        out.println(message);
    }

    /**
     * Prints a global message.
     *
     * @param name
     *      The user that sent the global message.
     *
     * @param message
     *      The global message.
     */
    @Override
    void msgAll(String name, String message) {
        String msgOut = name + " > " + message;
        out.println(msgOut);
    }

    /**
     * Prints a direct message.
     *
     * @param name
     *      The client who sent the message.
     *
     * @param message
     *      The direct message received.
     */
    @Override
    void msgDirect(String name, String message) {
        String msgOut = name + "(direct) > " + message;
        out.println(msgOut);
    }

    /**
     * Unused.
     */
    @Override
    void getClientsList() {
    }

    /**
     * Informs the user they have been kicked and then calls the exit method.
     *
     * @param kickedBy
     *      The client that removed the other client.
     *
     * @param toKick
     *      The client being kicked.
     */
    @Override
    void kick(String kickedBy, String toKick) {
        out.println("You have been kicked from the chat.");
        exit();
    }

    /**
     * Prints a list of clients.
     *
     * @param list
     *      The list of clients on the server.
     */
    private void clientsList(String list) {
        out.println(list);
    }
}
