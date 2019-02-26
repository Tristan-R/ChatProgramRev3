import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class AcceptClients implements Runnable {

    private ServerSocket server;

    public AcceptClients(ServerSocket server) {
        this.server = server;
    }

    public void run() {
        try {
            while (true) {
                Socket s = server.accept();
                System.out.println("Server accepted connection on " + server.getLocalPort() + " ; " + s.getPort());
                ClientThread client = new ClientThread(s);
                Thread thread = new Thread(client);
                thread.start();
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
