import java.util.ArrayList;

/**
 * RemoveClientStore object
 * <p>
 * Stores clients to be removed from the chat.
 */
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
     * Synchronized method for adding a client to the list of removed clients.
     *
     * @param name
     *      The name of the client to be added.
     */
    synchronized void add(String name) {
        removeClients.add(name);
    }

    /**
     * Synchronized method for removing a client from the list of removed
     * clients.
     *
     * @param name
     *      The name of the client to be removed.
     *
     * @return
     *      Whether the client was successfully removed.
     */
    synchronized boolean remove(String name) {
        return removeClients.remove(name);
    }
}
