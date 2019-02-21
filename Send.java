import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class Send implements Runnable {

    HashMap<String, PrintWriter> clients;
    ArrayList<String> messages;
    String name;

    Send(HashMap<String, PrintWriter> clients, ArrayList<String> messages, String name) {
        this.clients = clients;
        this.messages = messages;
        this.name = name;
    }

    public void run() {
        try {
            if (messages.size() > 0) {
                String message = messages.remove(0);
                for (String client : clients.keySet()) {
                    if (!client.equals(name)) {
                        clients.get(client).println(message);
                    }
                }
            } else {
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();

        } finally {
            // Send close request to MsgControl
        }
    }
}
