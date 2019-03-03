import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSend extends MsgControl {

    ClientSend(Socket socket) {
        try {

            this.socket = socket;
            in = new BufferedReader(new InputStreamReader(System.in));
            out = new PrintWriter(socket.getOutputStream(), true);

        } catch (IOException e) {
            System.err.println("There was an issue establishing a connection.");
            System.err.println("Closing application.");

        } finally {
            try {
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    boolean endThread() {
        return socket.isClosed();
    }

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

                default:
                    brokenMsg();
            }
        } else if (message.equals("EXIT")) {
            exit();

        } else {
            msgAll(message);
        }
    }

    @Override
    void exit() {
        String msgOut = msgBuilder(0, "null");
        out.println(msgOut);

        try {
            socket.close();
            System.out.println("Disconnected from server.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    void brokenMsg() {
        System.out.println("Your last message could not be processed.");
    }

    @Override
    void msgServer(String message) {
        String msgOut = msgBuilder(1, message);
        out.println(msgOut);
    }

    @Override
    void msgAll(String message) {
        String msgOut = msgBuilder(2, message);
        out.println(msgOut);
    }

    @Override
    void msgDirect(String name, String message) {
        String newMessage = name + ">" + message;
        String msgOut = msgBuilder(3, newMessage);
        out.println(msgOut);
    }

    @Override
    void getClientsList() {
        String msgOut = msgBuilder(4, "null");
        out.println(msgOut);
    }

    @Override
    void kick(String kickedBy, String toKick) {
        String msgOut = msgBuilder(5, toKick);
        out.println(msgOut);
    }
}
