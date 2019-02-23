public interface MsgCommands {

    void sendToAll();

    void sendTo(String name);

    void exit();

    String getClientsList(); // Should this change to void?


}
