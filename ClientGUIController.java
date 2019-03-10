import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientGUIController {
    public TextArea chatBox;
    public TextArea infoBox;
    public TextField inputBox;
    public Label name;
    private ClientSend clientSend;

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

    public void onPress(ActionEvent actionEvent) {
        sendInput();
    }

    public void onEnter(ActionEvent actionEvent) {
        sendInput();
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public static class Console extends OutputStream {

        private TextArea output;

        public Console(TextArea ta) {
            this.output = ta;
        }

        @Override
        public void write(int i) throws IOException {
            output.appendText(String.valueOf((char) i));
        }
    }
}
