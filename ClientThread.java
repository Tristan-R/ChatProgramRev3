import java.io.IOException;
import java.net.Socket;

public class ClientThread extends MsgControl {

    ClientThread(Socket socket) {
        super(socket);
    }

    @Override
    boolean endThread() {
        return socket.isClosed();
    }

    @Override
    void processMsg(String message) {
        String[] parts = message.split("~", 2);
        String identifier;
        if (parts.length == 2) {
            identifier = parts[0];
        } else {
            identifier = "-1";
        }

        switch (identifier) {
            case "-1":
                brokenMsg();
                break;

            case "0":
                exit();
                break;

            case "1":
                msgServer(parts[1]);
                break;

            case "2":
                msgAll(parts[1]);
                break;

            case "3":
                msgDirect(name, parts[1]);
                break;

            case "4":
                getClientsList();
                break;

            case "5":
                kick(name, parts[1]);
                break;

            default:
                brokenMsg();
        }
    }

    @Override
    void exit() {
        try {
            clients.remove(name);
            socket.close();
            serverWriter.println("Client has disconnected on " + socket.getLocalPort() + " : " + socket.getPort());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    void brokenMsg() {
        if (++brokenMsgCount > 3) {
            String returnMsg = msgBuilder(6, "server", "Weak connection to server, attempting to reconnect.");
            out.println(returnMsg);
            exit();

        } else {
            String returnMsg = msgBuilder(1, "server", "We could not process your last message.");
            out.println(returnMsg);
        }
    }

    @Override
    void msgServer(String message) {
        if (admins.contains(name)) {
            serverWriter.println(name + " > " + message);

        } else {
            String returnMsg = msgBuilder(1, "server", "You do not have permission to do that.");
            out.println(returnMsg);
        }
    }

    @Override
    void msgAll(String message) {
        String messageOut = msgBuilder(2, name, message);

        for (String client : clients.keySet()) {
            if (!client.equals(name)) {
                clients.get(client).println(messageOut);
            }
        }
    }

    // message will have format (toUser)>(message)
    @Override
    void msgDirect(String name, String message) {
        String[] parts = message.split(">", 2);

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
                if (admins.contains(toKick)) {
                    String messageOut = msgBuilder(1, "server", "Only the server can kick admins.");
                    out.println(messageOut);

                } else {
                    clients.remove(toKick);
                    removeClients.add(toKick);
                }
            } else {
                String messageOut = msgBuilder(1, "server", "This user does not exist.");
                out.println(messageOut);
            }
        } else {
            String messageOut = msgBuilder(1, "server", "You do not have permission to do that.");
            out.println(messageOut);
        }
    }
}
