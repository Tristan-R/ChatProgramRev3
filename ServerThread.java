import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;

public class ServerThread extends MsgControl {

    private ServerSocket server;

    ServerThread(ServerSocket server) {
        this.server = server;
        name = "server";
        in = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(System.out, true);
        clients.put(name, out);
        admins.add(name);
    }

    @Override
    boolean endThread() {
        return server.isClosed();
    }

    @Override
    String convertMsg(String message) {
        if (message.startsWith("/")) {
            String[] parts = message.split(" ", 2);

            switch (parts[0]) {
                case "/server" :
                    return msgBuilder(1, name, parts[1]);

                case "/direct" :
                    return msgBuilder(3, name, parts[1]);

                case "/clients" : //Need to fix as the lack of space breaks it
                    return msgBuilder(4, name, parts[1]);

                case "/kick" :
                    return msgBuilder(5, name, parts[1]);

                case "/promote" :
                    return msgBuilder(100, name, parts[1]);

                case "/demote" :
                    return msgBuilder(101, name, parts[1]);

                default :
                    return msgBuilder(-1, name, "null");
            }


        } else if (message.equals("EXIT")) {
            return msgBuilder(0, name, "null");

        } else {
            return msgBuilder(2, name, message);
        }
    }

    @Override
    void exit() {
        try {
            server.close();
            out.println("Server disconnected.");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    void brokenMsg() {
        out.println("Your last message could not be processed.");
    }

    @Override
    void msgServer(String message) {
        out.println(message);
    }

    @Override
    void msgAll(String message) {
        String messageOut = msgBuilder(1, name, message);

        for (String client : clients.keySet()) {
            if (!client.equals(name)) {
                clients.get(client).println(messageOut);
            }
        }
    }

    @Override
    void msgDirect(String name, String message) { //Should direct go on the name or message part?
        String[] parts = message.split(":", 2);

        if (clients.containsKey(parts[0])) {
            String messageOut = msgBuilder(1, name + "(direct)", parts[1]);
            clients.get(parts[0]).println(messageOut);

        } else {
            out.println("Could not find this user.");
        }
    }

    @Override
    void getClientsList() {
        String list = "";
        int i = 1;
        for (String client : clients.keySet()) {
            list = list.concat("\t" + client);

            if (admins.contains(client)) {
                list = list.concat("(admin)");
            }

            if (++i % 4 == 0) {
                list = list.concat("\n");
            }
        }

        out.println(list);
    }

    @Override
    void kick(String kickedBy, String toKick) {
        if (clients.containsKey(toKick)) {
            clients.remove(toKick);
            removeClients.add(toKick);

        } else {
            out.println("Could not find this user.");
        }
    }

    void promote(String client) {
        if (clients.containsKey(client)) {
            if (admins.contains(client)) {
                out.println("This user is already an admin.");

            } else {
                admins.add(client);
            }
        } else {
            out.println("Could not find this user.");
        }
    }

    void demote(String client) {
        if (clients.containsKey(client)) {
            if (admins.contains(client)) {
                admins.remove(client);

            } else {
                out.println("This user is not an admin.");
            }
        } else {
            out.println("Could not find this user.");
        }
    }

    @Override
    void unknownCommand(int identifier, String from, String message) {
        switch (identifier) {
            case 100 :
                promote(message);
                break;

            case 101 :
                demote(message);
                break;

            default :
                brokenMsg();
        }
    }
}
