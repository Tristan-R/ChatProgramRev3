import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Controller class for client GUI.
 */
public class ClientGUIController {
    public TextArea chatBox;
    public TextArea infoBox;
    public TextField inputBox;
    public Label name;
    private ClientSend clientSend;

    /**
     * Called on startup of GUI, sets up output to text areas and starts a
     * thread for receiving messages.
     */
    public void initialize() {
        Console chatConsole = new Console(chatBox);
        PrintStream chatStream = new PrintStream(chatConsole, true);
        PrintWriter out = new PrintWriter(chatStream, true);
        chatBox.setEditable(false);

        Console infoConsole = new Console(infoBox);
        PrintStream infoStream = new PrintStream(infoConsole, true);
        System.setOut(infoStream);
        System.setErr(infoStream);
        infoBox.setDisable(true);

        Socket socket = ClientGUI.getSocket();

        clientSend = new ClientSend(socket, ClientGUI.getSocketOut(), out);

        new Thread(new ClientReceive(socket, ClientGUI.getSocketIn(), out, this)).start();
    }

    /**
     * Sends input to clientSend object for processing.
     */
    private void sendInput() {
        String input = inputBox.getCharacters().toString();
        inputBox.setText("");

        if (clientSend.endThread()) {
            clientSend.exit();

        } else {
            clientSend.processMsg(input);
        }

        if (clientSend.endThread()) {
            clientSend.exit();
        }
    }

    /**
     * Called on Send button press.
     */
    public void onPress() {
        sendInput();
    }

    /**
     * Called on Enter key.
     */
    public void onEnter() {
        sendInput();
    }

    /**
     * Sets the display name of the user.
     *
     * @param name
     *      The name of the user.
     */
    public void setName(String name) {
        this.name.setText(name);
    }

    /**
     * Console class for printing system output to a text area.
     */
    public static class Console extends OutputStream {

        /**
         * The text area to print to.
         */
        private TextArea output;

        /**
         * Constructor.
         *
         * @param ta
         *      The text area to print to.
         */
        Console(TextArea ta) {
            this.output = ta;
        }

        /**
         * Writes the specified byte to the text area.
         *
         * @param i
         *      The byte.
         *
         * @throws IOException
         *      If an I/O error occurs.
         */
        @Override
        public void write(int i) throws IOException {
            output.appendText(String.valueOf((char) i));
        }
    }
}
