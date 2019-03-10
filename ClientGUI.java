import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientGUI extends Application {

    private static Socket socket;

    private static BufferedReader socketIn;

    private static PrintWriter socketOut;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ClientGUI.fxml"));
        primaryStage.setTitle("Chat Client");
        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest(e -> shutdown());
        primaryStage.show();
    }

    public void shutdown() {
        try {
            socketOut.println("0~null");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    public static Socket getSocket() {
        return socket;
    }

    public static BufferedReader getSocketIn() {
        return socketIn;
    }

    public static PrintWriter getSocketOut() {
        return socketOut;
    }

    public static void begin(Socket clientSocket, BufferedReader in, PrintWriter out) {
        socket = clientSocket;
        socketIn = in;
        socketOut = out;
        launch();
    }
}
