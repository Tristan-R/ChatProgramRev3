import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * <h1>Launcher for Server GUI</h1>
 * This launcher will start the server GUI when called.
 */
public class ServerGUI extends Application {

    /**
     * Stores the socket that the server is connected on.
     */
    private static ServerSocket server;

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
        Parent root = FXMLLoader.load(getClass().getResource("ServerGUI.fxml"));
        primaryStage.setTitle("Chat Server");
        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest(e -> shutdown());
        primaryStage.show();
    }

    /**
     * The procedure to follow when the window is closed.
     */
    private void shutdown() {
        try {
            server.close();

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            System.exit(0);
        }
    }

    /**
     * @return
     *      The socket that the server is connected on.
     */
    public static ServerSocket getServer() {
        return server;
    }

    /**
     * Launches the GUI application.
     *
     * @param serverSocket
     *      The socket that the server is connected on.
     */
    static void begin(ServerSocket serverSocket) {
        server = serverSocket;
        launch();
    }
}
