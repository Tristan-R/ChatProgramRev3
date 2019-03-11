import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentHashMap;


/**
 * MsgControl abstract class
 * <p>
 * Abstract class for receiving input from a BufferedReader, processing that
 * message and then printing the output to a PrintWriter.
 */
public abstract class MsgControl implements Runnable {

    /**
     * Stores the name of the client that is using this object.
     */
    String name;

    /**
     * Stores the socket that the client is connected on.
     */
    Socket socket;

    /**
     * The BufferedReader object to read messages in from.
     */
    BufferedReader in;

    /**
     * The PrintWriter object to print the processed message to.
     */
    PrintWriter out;

    /**
     * Stores a list of client names and their output streams.
     */
    static ConcurrentHashMap<String, PrintWriter> clients = new ConcurrentHashMap<>();

    /**
     * Stores the server's output stream.
     */
    static PrintWriter serverWriter;

    /**
     * An object for storing admins that have access to kick and message server
     * functions.
     */
    static AdminStore admins = new AdminStore();

    /**
     * An object for storing clients to be kicked.
     */
    static RemoveClientStore removeClients = new RemoveClientStore();

    /**
     * The number of messages received from the input that could not be
     * processed.
     */
    int brokenMsgCount = 0;

    /**
     * Sets the client's name.
     */
    abstract void setName();

    /**
     * Checks if the thread needs to be closed.
     *
     * @return
     *      If the thread needs to be closed.
     */
    abstract boolean endThread();

    /**
     * Takes an input message and processes it.
     *
     * @param message
     *      The input message to process.
     */
    abstract void processMsg(String message);

    /**
     * Closes the socket and cleanly exits the program.
     */
    abstract void exit();

    /**
     * Processes a broken message; one that doesn't have a valid identifier or
     * doesn't have the correct number of sections.
     */
    abstract void brokenMsg();

    /**
     * Receives a message from or send a message to the server.
     *
     * @param message
     *      The message to send to the server or the message received from the
     *      server.
     */
    abstract void msgServer(String message);

    /**
     * Receives a global message or sends a global message.
     *
     * @param from
     *      The client who sent the message.
     *
     * @param message
     *      The message that has been sent or received.
     */
    abstract void msgAll(String from, String message);

    /**
     * Receives a message from or sends a message to a single client.
     *
     * @param name
     *      The client who sent the message.
     *
     * @param message
     *      The client to send the message to and the message to be sent to the
     *      client, or the message received.
     */
    abstract void msgDirect(String name, String message);

    /**
     * Retrieves a list of clients currently on the server.
     */
    abstract void getClientsList();

    /**
     * Removes a client from the server.
     *
     * @param kickedBy
     *      The client that removed the other client.
     *
     * @param toKick
     *      The client that got removed from the server.
     */
    abstract void kick(String kickedBy, String toKick);

    /**
     * Default constructor.
     */
    MsgControl() {}

    /**
     * Constructor. Sets up an input and output stream from a socket.
     *
     * @param socket
     *      The socket to retrieve the input and output stream from.
     */
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

    /**
     * Builds a message in the correct format to send to a client.
     *
     * @param msgType
     *      The message type that is sent. A list of these can be found in
     *      README.txt.
     *
     * @param from
     *      The client that sent the message (can also come from server).
     *
     * @param message
     *      The message to send to the client.
     *
     * @return
     *      The built message that is sent to the client.
     */
    String msgBuilder(int msgType, String from, String message) {
        return msgType + "~" + from + "~" + message;
    }

    /**
     * Builds a message in the correct format to be sent to the server.
     *
     * @param msgType
     *      The message type that is sent. A list of these can be found in
     *      README.txt.
     *
     * @param message
     *      The message to send to the server.
     *
     * @return
     *      The built message that is sent to the server.
     */
    String msgBuilder(int msgType, String message) {
        return msgType + "~" + message;
    }

    /**
     * Starts by getting a name and then continuously waits for input, calls
     * processMsg on it, and repeats until the socket is closed or the client
     * is kicked.
     */
    public void run() {
        setName();

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
            if (!endThread()) {
                System.err.println("Connection to socket lost.");
            }
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            exit();
        }
    }
}
