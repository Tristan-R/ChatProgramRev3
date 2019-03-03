import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient { // Need to send '/' commands first
    /*
    Sending Commands:
        0 - EXIT
        1 - Send to server // Needs to be an authorisation on server end
        2 - Send to all
        3 - Direct message // message will have format (3)~(fromUser)~(toUser)>(message)
        4 - See current clients
        5 - Kicked from chat
        6 - Reconnect to server? // Does the server need to know it's a reconnect? Or just send EXIT?
        7 -

    Receiving Commands: // need one for name change
        0 - EXIT
        1 - Received from server // Direct messages from server should be handled server end
                                 // and the server should put (direct) before the message.
        2 - Received global message
        3 - Received direct message
        4 - List of clients
        5 - Kick command
        6 - Try reconnecting
        7 -
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
        new Thread(new ClientSend(socket)).start();

        new Thread(new ClientReceive(socket)).start();
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
