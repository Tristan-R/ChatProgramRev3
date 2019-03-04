import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;


// Messages will have the format: (identifier)~(name)~(message)
public abstract class MsgControl implements Runnable {

    String name;

    Socket socket;

    BufferedReader in;

    PrintWriter out;

    static ConcurrentHashMap<String, PrintWriter> clients = new ConcurrentHashMap<>();

    static PrintWriter serverWriter;

    // Used to add admins that can message the server (and maybe other privileges)
    static ArrayList<String> admins = new ArrayList<>();

    private static ArrayList<String> removeClients = new ArrayList<>();

    int brokenMsgCount = 0;

    synchronized void kickModify(String operation, String name) {
        if (operation.equals("Add")) {
            removeClients.add(name);
        } else if (operation.equals("Remove")) {
            removeClients.remove(name);
        }
    }

    private synchronized boolean kickSearch(String name) {
        return removeClients.contains(name);
    }

    abstract void setName();

    abstract boolean endThread();

    abstract void processMsg(String message);

    abstract void exit();

    abstract void brokenMsg();

    abstract void msgServer(String message);

    abstract void msgAll(String from, String message);

    abstract void msgDirect(String name, String message);

    abstract void getClientsList();

    abstract void kick(String kickedBy, String toKick);

    MsgControl() {}

    MsgControl(Socket socket) {
        try {
            this.socket = socket;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

        } catch (SocketException e) {
            System.err.println("Socket connection error.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String msgBuilder(int msgType, String from, String message) {
        return msgType + "~" + from + "~" + message;
    }

    String msgBuilder(int msgType, String message) {
        return msgType + "~" + message;
    }

    public void run() {
        setName();

        try {
            String input;

            while (!endThread()) {
                input = in.readLine();

                if (kickSearch(name)) {
                    kickModify("Remove", name);
                    return;

                } else if (!endThread()) {
                    processMsg(input);
                }
            }
        } catch (SocketException e) {
            if (!endThread()) {
                System.err.println("Connection error.");
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            exit();
        }
    }
}
