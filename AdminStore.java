import java.util.ArrayList;

class AdminStore {

    /**
     * Stores a list of admins that have access to kick and message server
     * functions.
     */
    private static ArrayList<String> admins;

    /**
     * Constructor.
     */
    AdminStore() {
        admins = new ArrayList<>();
    }

    /**
     * Adds a client to the store.
     *
     * @param name
     *      The client to add.
     */
    synchronized void add(String name) {
        admins.add(name);
    }

    /**
     * Removes a client from the store.
     *
     * @param name
     *      The client to remove.
     *
     * @return
     *      Whether the client was removed successfully.
     */
    synchronized boolean remove(String name) {
        return admins.remove(name);
    }

    /**
     * Finds a client in the store.
     *
     * @param name
     *      The client to be found.
     *
     * @return
     *      Whether the client was found.
     */
    synchronized boolean contains(String name) {
        return admins.contains(name);
    }
}
