import java.io.IOException;
import java.net.Socket;

public class ClientThread extends MsgControl {

    ClientThread(Socket socket) {
        super(socket);
    }

    @Override
    void exit() {
        try {
            receive.interrupt();
            send.interrupt();
            clients.remove(name);
            socket.close();
            clients.get("server").println("Client has disconnected on " + socket.getLocalPort() + " : " + socket.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    void brokenMsg() {
        if (++brokenMsgCount > 3) {
            String returnMsg = msgBuilder(1, "server", "We could not process your last message.");
            out.println(returnMsg);
        } else {
            String returnMsg = msgBuilder(6, "server", "Weak connection to server, attempting to reconnect.");
            out.println(returnMsg);
            exit();
        }
    }

    @Override
    void msgServer(String message) {
        if (admins.contains(name)) {
            clients.get("server").println(message);

        } else {
            String returnMsg = msgBuilder(1, "server", "You do not have permission to do that.");
            out.println(returnMsg);
        }
    }

    @Override
    void msgAll(String message) {
        String messageOut = msgBuilder(2, name, message);
        messagesOut.add(messageOut);
        notifyAll();
    }

    // message will have format (toUser)>(message)
    @Override
    void msgDirect(String name, String message) {
        String[] parts = message.split(">", 1);

        if (clients.containsKey(parts[0])) {
            String messageOut = msgBuilder(3, name, parts[1]);
            clients.get(parts[0]).println(messageOut);

        } else {
            String messageOut = msgBuilder(1, "server", "There is not a user with this name.");
            out.println(messageOut);
        }
    }

    @Override
    void getClientsList() {
        String list = "";
        int i = 1;
        for (String client : clients.keySet()) {
            list = list.concat("\t" + client);

            if (++i % 4 == 0) {
                list = list.concat("\n");
            }
        }

        out.println(list);
    }

    @Override
    void kick(String kickedBy, String toKick) {
        if (admins.contains(name)) {
            if (clients.containsKey(toKick)) {
                clients.remove(toKick);

            } else {
                String messageOut = msgBuilder(1, "server", "User does not exist.");
                out.println(messageOut);
            }
        } else {
            String messageOut = msgBuilder(1, "server", "You do not have permission to do that.");
            out.println(messageOut);
        }
    }

    @Override
    void startThreads() {

    }
}
