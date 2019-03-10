import javafx.application.Platform;

/**
 * UpdateGUI class
 * <p>
 * Used to update ClientGUI from a non-application thread.
 */
class UpdateGUI {

    /**
     * Updates the name label in the GUI.
     *
     * @param GUI
     *      The GUI controller.
     *
     * @param name
     *      The name to set for the label.
     */
    static void setName(ClientGUIController GUI, String name) {
        Platform.runLater(() -> GUI.setName(name));
    }
}
