import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientReceive extends MsgControl {

    ClientReceive(Socket socket) {
        try {
            this.socket = socket;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(System.out, true);

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
                msgAll(parts[2]);
                break;

            case "3":
                msgDirect(parts[1], parts[2]);
                break;

            case "4":
                getClientsList();
                break;

            case "5":
                kick(parts[1], parts[2]);
                break;

            default:
                brokenMsg();
        }
    }

    @Override
    void exit() {

    }

    @Override
    void brokenMsg() {

    }

    @Override
    void msgServer(String message) {

    }

    @Override
    void msgAll(String message) {

    }

    @Override
    void msgDirect(String name, String message) {

    }

    @Override
    void getClientsList() {

    }

    @Override
    void kick(String kickedBy, String toKick) {

    }
}
