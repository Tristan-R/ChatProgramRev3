import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

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

    MsgControl(BufferedReader in, PrintWriter out) {
        this.in = in;
        this.out = out;
    }

    MsgControl(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            while (true) {
                out.println("Please enter a username (max 15 characters):");
                name = in.readLine();
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

    void startThreads() { // Needs revising
        send = new Send(clients, messagesOut, name);
        receive = new Receive(in, newMessages);

        sendThread = new Thread(send);
        receiveThread = new Thread(receive);

        sendThread.start();
        receiveThread.start();
    }

    public void send() {
        notifyAll();

    }

    public void run() {
        startThreads();


    }

}
