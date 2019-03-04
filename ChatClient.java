import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {
    /*
    Sending Commands:
        0 - EXIT
        1 - Send to server
        2 - Send to all
        3 - Direct message
        4 - See current clients
        5 - Kicked from chat

    Receiving Commands:
        0 - EXIT
        1 - Received from server
        2 - Received global message
        3 - Received direct message
        4 - List of clients
        5 - Kick command
     */

    private Socket socket;

    private ChatClient(String address, int port) {
        try {
            socket = new Socket(address, port);

        } catch (UnknownHostException e) {
            System.err.println("IP address could not be determined.");
            System.exit(-1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void begin() {
        try {
            BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));

            new Thread(new ClientSend(socket, userIn, serverOut)).start();

            new Thread(new ClientReceive(socket, serverIn)).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String address = "localhost";
        int port = 14001;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-cca")) {
                if (i + 1 < args.length) {
                    address = args[i + 1];
                } else {
                    System.err.println("No address given.");
                    System.exit(-1);
                }
            } else if (args[i].equals("-ccp")) {
                if (i + 1 < args.length) {
                    try {
                        port = Integer.parseInt(args[i + 1]);
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
        System.out.println("Connected to server on : " + address + " ; " + port);
        new ChatClient(address, port).begin();
    }
}
