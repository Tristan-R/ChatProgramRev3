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

    protected String name;

    protected Socket socket;

    private BufferedReader in;

    protected PrintWriter out;

    protected static HashMap<String, PrintWriter> clients;

    // Used to add admins that can message the server (and maybe other privileges)
    protected static ArrayList<String> admins;

    private ArrayList<String> newMessages = new ArrayList<>();

    // Probably should be unique to ClientThread and ServerOut
    protected ArrayList<String> messagesOut = new ArrayList<>();

    protected Thread receive;

    // Probably should be unique to ClientThread and ServerOut
    protected Thread send;

    protected int brokenMsgCount = 0;

    abstract void exit();

    abstract void brokenMsg();

    abstract void msgServer(String message);

    abstract void msgAll(String message);

    abstract void msgDirect(String name, String message);

    abstract void getClientsList(String list);

    abstract void kick(String kickedBy, String toKick);

    abstract void startThreads();

    MsgControl(BufferedReader in, PrintWriter out) {
        this.in = in;
        this.out = out;
    }

    MsgControl(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            // This should be handled by the client and should be the first message sent
            // Cannot include ~, < or >
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

    protected String msgBuilder(int msgType, String from, String message) {
        return msgType + "~" + from + "~" + message;
    }

    public void run() {
        startThreads();

        try {
            while (!socket.isClosed()) {
                if (!clients.containsKey(name)) {
                    exit();

                } else if (newMessages.size() <= 0) {
                    wait();

                } else {
                    String nextMessage = newMessages.remove(0);
                    String[] parts = nextMessage.split("~", 2);
                    int identifier;
                    if (parts.length == 3) {
                        identifier = Integer.parseInt(parts[0]);
                    } else {
                        identifier = -1;
                    }

                    switch (identifier) {
                        case -1 :
                            brokenMsg();
                            break;

                        case 0 :
                            exit();
                            break;

                        case 1 :
                            msgServer(parts[2]);
                            break;

                        case 2 :
                            msgAll(parts[2]);
                            break;

                        case 3 :
                            msgDirect(parts[1], parts[2]);
                            break;

                        case 4 :
                            getClientsList(parts[2]);
                            break;

                        case 5 :
                            kick(parts[1], parts[2]);
                            break;

                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
