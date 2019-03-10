import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.ServerSocket;

public class ServerGUIController {
    public Label portNumber;
    public TextArea chatBox;
    public TextArea infoBox;
    public TextField inputBox;
    private ServerThread serverThread;

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

        ServerSocket server = ServerGUI.getServer();

        serverThread = new ServerThread(server, out);

        portNumber.setText(Integer.toString(server.getLocalPort()));
    }

    private void sendInput() {
        String input = inputBox.getCharacters().toString();
        inputBox.setText("");

        if (serverThread.endThread()) {
            serverThread.exit();

        } else {
            serverThread.processMsg(input);
        }
    }

    public void onPress(ActionEvent actionEvent) {
        sendInput();
    }

    public void onEnter(ActionEvent actionEvent) {
        sendInput();
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
