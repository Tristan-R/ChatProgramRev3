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

    protected BufferedReader in;

    protected PrintWriter out;

    //Should I move server out and into own variable?
    protected static HashMap<String, PrintWriter> clients = new HashMap<>();

    // Used to add admins that can message the server (and maybe other privileges)
    protected static ArrayList<String> admins = new ArrayList<>();

    protected static ArrayList<String> removeClients = new ArrayList<>();

    protected int brokenMsgCount = 0;

    abstract boolean endThread();

    abstract String convertMsg(String message);

    abstract void exit();

    abstract void brokenMsg();

    abstract void msgServer(String message);

    abstract void msgAll(String message);

    abstract void msgDirect(String name, String message);

    abstract void getClientsList();

    abstract void kick(String kickedBy, String toKick);

    abstract void unknownCommand(int identifier, String from, String message);

    MsgControl() {}

    MsgControl(BufferedReader in, PrintWriter out) {
        this.in = in;
        this.out = out;
    }

    MsgControl(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            /* Needs to go in run statement */
            // This should be handled by the client and should be the first message sent
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

    protected String msgBuilder(int msgType, String from, String message) {
        return msgType + "~" + from + "~" + message;
    }

    public void run() {
        try {
            String input;

            while (!endThread()) {
                input = in.readLine();

                if (removeClients.remove(name)) {
                    return;

                } else {
                    /* This else statement should just be a call to a processMsg command and the switch should be
                     * in the child classes.
                     */
                    String message = convertMsg(input);
                    System.out.println(message);
                    String[] parts = message.split("~", 3);
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
                            getClientsList();
                            break;

                        case 5 :
                            kick(parts[1], parts[2]);
                            break;

                        default :
                            unknownCommand(identifier, parts[1], parts[2]);
                    }
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
