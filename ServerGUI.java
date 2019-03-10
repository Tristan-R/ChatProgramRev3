import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerGUI extends Application {

    private static ServerSocket server;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ServerGUI.fxml"));
        primaryStage.setTitle("Chat Server");
        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest(e -> shutdown());
        primaryStage.show();
    }

    private void shutdown() {
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    public static ServerSocket getServer() {
        return server;
    }

    static void begin(ServerSocket serverSocket) {
        server = serverSocket;
        launch();
    }
}
