import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * AcceptClients object
 * <p>
 * Accepts connections from clients and adds them to the chat.
 */
public class AcceptClients implements Runnable {

    /**
     * Stores the socket that the server is running on.
     */
    private ServerSocket server;

    /**
     * Constructor.
     *
     * @param server
     *      The socket that the server is running on.
     */
    AcceptClients(ServerSocket server) {
        this.server = server;
    }

    /**
     * Waits for a new client to connect and then it will create a new
     * ClientThread object for it.
     */
    public void run() {
        try {
            while (true) {
                Socket s = server.accept();
                System.out.println("Server accepted connection on " + server.getLocalPort() + " ; " + s.getPort());
                ClientThread client = new ClientThread(s);
                Thread thread = new Thread(client);
                thread.start();
            }
        } catch (SocketException e) {
            if (!server.isClosed()) {
                System.err.println("Fatal connection error.");
            }
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
