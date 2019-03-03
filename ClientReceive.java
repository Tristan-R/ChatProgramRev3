import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientReceive extends MsgControl {

    ClientReceive(Socket socket, BufferedReader in) {
            this.socket = socket;
            this.in = in;
            out = new PrintWriter(System.out, true);
    }

    @Override
    void setName() {
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

    @Override
    void exit() {
        try {
            System.out.println("Server disconnected.");
            socket.close();
            System.exit(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    void brokenMsg() {
        if (++brokenMsgCount > 3) {
            System.err.println("There is an issue with connecting to the server.");
            exit();

        } else {
            out.println("The last message received could not be processed.");
        }
    }

    @Override
    void msgServer(String message) {
        String msgOut = "server(direct) > " + message;
        out.println(msgOut);
    }

    @Override
    void msgAll(String name, String message) {
        String msgOut = name + " > " + message;
        out.println(msgOut);
    }

    @Override
    void msgDirect(String name, String message) {
        String msgOut = name + "(direct) > " + message;
        out.println(msgOut);
    }

    @Override
    void getClientsList() {
    }

    @Override
    void kick(String kickedBy, String toKick) {
        out.println("You have been kicked from the chat.");
        exit();
    }

    private void clientsList(String list) {
        out.println(list);
    }
}
