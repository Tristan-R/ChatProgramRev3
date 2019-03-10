import java.util.ArrayList;

class RemoveClientStore {

    /**
     * A list of clients that have been kicked before their sockets are closed.
     */
    private static ArrayList<String> removeClients;

    /**
     * Constructor.
     */
    RemoveClientStore() {
        removeClients = new ArrayList<>();
    }

    /**
     * Synchronised method for adding or removing a client from the list of
     * removed clients.
     *
     * @param operation
     *      Identifies whether a client is to be added or removed.
     *
     * @param name
     *      The name of the client to be added or removed.
     */
    synchronized void kickModify(String operation, String name) {
        if (operation.equals("Add")) {
            removeClients.add(name);
        } else if (operation.equals("Remove")) {
            removeClients.remove(name);
        }
    }

    /**
     * Synchronised method to check if a client needs to be kicked.
     * @param name
     *      The client name to search for.
     *
     * @return
     *      Whether the client is in the list of kicked clients.
     */
    synchronized boolean kickSearch(String name) {
        return removeClients.contains(name);
    }
}
