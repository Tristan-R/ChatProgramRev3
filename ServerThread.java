import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;

class ServerThread extends MsgControl {

    private ServerSocket server;

    ServerThread(ServerSocket server) {
        this.server = server;
        in = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(System.out, true);
        serverWriter = out;
    }

    @Override
    void setName() {
        name = "server";
        admins.add(name);
    }

    @Override
    boolean endThread() {
        return server.isClosed();
    }

    @Override
    void processMsg(String message) {
        if (message.startsWith("/")) {
            String[] parts = message.split(" ", 2);

            switch (parts[0]) {
                case "/server":
                    if (parts.length == 2) {
                        msgServer(parts[1]);

                    } else {
                        brokenMsg();
                    }
                    break;

                case "/direct":
                    if (parts.length == 2) {
                        msgDirect(name, parts[1]);

                    } else {
                        brokenMsg();
                    }
                    break;

                case "/clients":
                    getClientsList();
                    break;

                case "/kick":
                    if (parts.length == 2) {
                        kick(name, parts[1]);

                    } else {
                        brokenMsg();
                    }
                    break;

                case "/promote":
                    if (parts.length == 2) {
                        promote(parts[1]);

                    } else {
                        brokenMsg();
                    }
                    break;

                case "/demote":
                    if (parts.length == 2) {
                        demote(parts[1]);

                    } else {
                        brokenMsg();
                    }
                    break;

                default:
                    brokenMsg();
            }
        } else if (message.equals("EXIT")) {
            exit();

        } else {
            msgAll(name, message);
        }
    }

    @Override
    void exit() {
        try {
            for (String client : clients.keySet()) {
                PrintWriter clientOut = clients.remove(client);
                admins.remove(client);
                String removeMsg = msgBuilder(0, "server", "null");
                clientOut.println(removeMsg);
                kickModify("Add", client);
            }
            Thread.sleep(5000);
            server.close();
            out.println("Server disconnected.");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();

        } finally {
            System.exit(0);
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
    void msgAll(String from, String message) {
        String messageOut = msgBuilder(2, name, message);

        for (String client : clients.keySet()) {
            clients.get(client).println(messageOut);
        }
    }

    @Override
    void msgDirect(String name, String message) { //Should direct go on the name or message part?
        String[] parts = message.split(":", 2);

        if (clients.containsKey(parts[0])) {
            String messageOut = msgBuilder(1, name, parts[1]);
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
            PrintWriter kickClient = clients.remove(toKick);
            admins.remove(toKick);
            String removeMsg = msgBuilder(5, "server", "null");
            kickClient.println(removeMsg);
            kickModify("Add", toKick);

        } else {
            out.println("Could not find this user.");
        }
    }

    private void promote(String client) {
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

    private void demote(String client) {
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
}
