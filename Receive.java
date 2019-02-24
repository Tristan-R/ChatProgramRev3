import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;

public class Receive implements Runnable {

    BufferedReader in;
    ArrayList<String> messages;

    Receive(BufferedReader in, ArrayList<String> messages) {
        this.in = in;
        this.messages = messages;
    }

    public void run() {
        try {
            String input;

            while(true) {
                input = in.readLine();

                messages.add(input);
                notifyAll();

            }
        } catch (SocketException e) {
            if (!Thread.interrupted()) {
                System.err.println("Connection error.");
            }
            messages.add("0~null~null");

        } catch (IOException e) {
            e.printStackTrace();
            messages.add("0~null~null");

        }
    }
}
