import java.io.IOException;
import java.net.ServerSocket;

/**
 * <h1>Launcher for Chat Server</h1>
 * This launcher will start the chat server and will allow clients to connect.
 * <p>
 * The server will can also commands entered on the command line.
 * See README.txt for full instructions.
 */
public class ChatServer {

    /**
     * Stores the socket that the server is running on.
     */
    private ServerSocket server;

    /**
     * Constructor.
     *
     * @param port
     *      The port to start the server on.
     */
    private ChatServer(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("Server started on port " + server.getLocalPort());

        } catch (IOException e) {
            System.err.println("Unable to establish server.");
            System.exit(-1);
        }
    }

    /**
     * Starts a new thread for accepting clients and a new thread for receiving
     * user commands.
     */
    private void begin() {
        new Thread(new AcceptClients(server)).start();

        new Thread(new ServerThread(server)).start();
    }

    /**
     * This is the main method that checks for a new port number if specified
     * on the command line at launch.
     *
     * @param args
     *      Used to change the port number if -csp flag is entered.
     */
    public static void main(String[] args) {
        int portNumber = 14001;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-csp")) {
                if (i + 1 < args.length) {
                    try {
                        portNumber = Integer.parseInt(args[i + 1]);

                    } catch (NumberFormatException e) {
                        System.err.println("Error in port number.");
                        System.exit(-1);
                    }
                } else {
                    System.err.println("No port number given.");
                    System.exit(-1);
                }
            }
        }
        new ChatServer(portNumber).begin();
    }
}
