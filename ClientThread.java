import java.net.Socket;

public class ClientThread extends MsgControl {

    ClientThread(Socket socket) {
        super(socket);
    }

    @Override
    void exit() {

    }

    @Override
    void brokenMsg() {

    }

    @Override
    void msgServer(String message) {
        if (admins.contains(name)) {
            clients.get("server").println(message);

        } else {
            out.println("You do not have permission to do that.");
        }
    }

    @Override
    void msgAll(String message) {
        String messageOut = name + " > " + message;
        messagesOut.add(messageOut);
        notifyAll();
    }

    // message will have format (client)~(message)
    @Override
    void msgDirect(String name, String message) {
        String[] parts = message.split("~");

        if (clients.containsKey(parts[0])) {
            String messageOut = name + " (direct) > " + message;
            clients.get(parts[0]).println(messageOut);

        } else {
            out.println("There is not a user with this name.");
        }
    }

    @Override
    void getClientsList(String list) {
        out.println(list);
    }

    @Override
    void kick(String kickedBy, String toKick) {
        if (admins.contains(name)) {
            if (clients.containsKey(toKick)) {
                clients.remove(toKick);

            } else {
                out.println("User does not exist.");
            }
        } else {
            out.println("You do not have permission to perform this command.");
        }
    }

    @Override
    void startThreads() {

    }
}
