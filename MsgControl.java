import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;


// Messages will have the format: (identifier)~(name)~(message)
public abstract class MsgControl implements Runnable {

    String name;

    Socket socket;

    BufferedReader in;

    PrintWriter out;

    //Should I move server out and into own variable?
    static HashMap<String, PrintWriter> clients = new HashMap<>();

    // Used to add admins that can message the server (and maybe other privileges)
    static ArrayList<String> admins = new ArrayList<>();

    static ArrayList<String> removeClients = new ArrayList<>();

    int brokenMsgCount = 0;

    abstract boolean endThread();

    abstract void processMsg(String message);

    abstract void exit();

    abstract void brokenMsg();

    abstract void msgServer(String message);

    abstract void msgAll(String message);

    abstract void msgDirect(String name, String message);

    abstract void getClientsList();

    abstract void kick(String kickedBy, String toKick);

    MsgControl() {}

    MsgControl(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            /* Needs to go in run statement */
            // Cannot include ~, <, >, : or ;
            while (true) {
                out.println("Please enter a username (max 15 characters):");
                name = in.readLine();
                // Need to improve for when the user enters a name already in use
                // Should it allow spaces
                if (name.length() <= 15) {
                    clients.put(name, out);
                    break;
                }
            }
        } catch (SocketException e) {
            System.err.println("Socket connection error.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String msgBuilder(int msgType, String from, String message) {
        return msgType + "~" + from + "~" + message;
    }

    public void run() {
        try {
            String input;

            while (!endThread()) {
                input = in.readLine();

                if (removeClients.remove(name)) {
                    return;

                } else if (!endThread()) {
                    processMsg(input);
                }
            }
        } catch (SocketException e) {
            System.err.println("Connection error.");

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            exit();
        }
    }
}
