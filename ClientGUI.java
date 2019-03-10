import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * <h1>Launcher for Client GUI</h1>
 * This launcher will start the client GUI when called.
 */
public class ClientGUI extends Application {

    /**
     * Stores the socket that the client is connected on.
     */
    private static Socket socket;

    /**
     * Stores the socket input stream.
     */
    private static BufferedReader socketIn;

    /**
     * Stores the socket output stream.
     */
    private static PrintWriter socketOut;

    /**
     * Builds and launches the GUI.
     *
     * @param primaryStage
     *      The primary stage for this application, onto which
     *      the application scene can be set.
     *
     * @throws Exception
     *      If an error occurs.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ClientGUI.fxml"));
        primaryStage.setTitle("Chat Client");
        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest(e -> shutdown());
        primaryStage.show();
    }

    /**
     * The procedure to follow when the window is closed.
     */
    private void shutdown() {
        try {
            socketOut.println("0~null");
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            System.exit(0);
        }
    }

    /**
     * @return
     *      The socket that the client is connected on.
     */
    public static Socket getSocket() {
        return socket;
    }

    /**
     * @return
     *      The socket input stream.
     */
    static BufferedReader getSocketIn() {
        return socketIn;
    }

    /**
     * @return
     *      The socket output stream.
     */
    static PrintWriter getSocketOut() {
        return socketOut;
    }

    /**
     * Launches the GUI application.
     *
     * @param clientSocket
     *      The socket the client is connected on.
     *
     * @param in
     *      The socket input stream.
     *
     * @param out
     *      The socket output stream.
     */
    static void begin(Socket clientSocket, BufferedReader in, PrintWriter out) {
        socket = clientSocket;
        socketIn = in;
        socketOut = out;
        launch();
    }
}
