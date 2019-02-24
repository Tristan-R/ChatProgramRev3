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

    private String name;

    private Socket socket;

    private BufferedReader in;

    private PrintWriter out;

    private Thread receiveThread;

    private Thread sendThread;

    static HashMap<String, PrintWriter> clients;

    private ArrayList<String> newMessages = new ArrayList<>();

    private ArrayList<String> messagesOut = new ArrayList<>();

    Receive receive;

    Send send;

    abstract void exit();

    abstract void brokenMsg();

    abstract void msgServer();

    abstract void msgAll();

    abstract void msgDirect(String name);

    abstract void getClientsList();

    abstract void kick();

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

    public void sendToAll() { // Make private after removing interface?
        notifyAll();

    }

    public void run() {
        startThreads();

        try {
            while (!socket.isClosed()) {
                if (newMessages.size() <= 0) {
                    wait();

                } else {
                    String nextMessage = newMessages.remove(0);
                    String[] parts = nextMessage.split("~");
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
                            msgServer();
                            break;

                        case 2 :
                            msgAll();
                            break;

                        case 3 :
                            msgDirect(parts[1]);
                            break;

                        case 4 :
                            getClientsList();
                            break;

                        case 5 :
                            kick();
                            break;

                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
