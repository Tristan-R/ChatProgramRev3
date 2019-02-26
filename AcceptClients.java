import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class AcceptClients implements Runnable {

    private ServerSocket in;

    public AcceptClients(ServerSocket in) {
        this.in = in;
    }

    public void run() {
        try {
            while (true) {
                Socket s = in.accept();
                System.out.println("Server accepted connection on " + in.getLocalPort() + " ; " + s.getPort());
                MsgControl client = new ClientThread(s);
                Thread thread = new Thread(client);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
