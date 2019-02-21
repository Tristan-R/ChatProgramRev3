import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;

public class ChatServer {

    private ServerSocket in;
    private String name = "server";
    private BufferedReader userIn;
    private PrintWriter serverOut;

    public ChatServer(int port) {
        try {
            in = new ServerSocket(port);
            System.out.println("Server started on port " + in.getLocalPort());
        } catch (IOException e) {
            System.err.println("Unable to establish server.");
            System.exit(-1);
        }
    }

    public void begin() {
        new Thread(new AcceptClients(in)).start();


    }

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
