SERVER
--------
The server can be launched from the ChatServer file after compiling. To change the port number that
the server will run on, add the flag -csp followed by a port number to the command line when
running the file.

If the port you have chosen is free then the server will start accepting clients on this port, if
the port is not free then the program will error and exit.

To close all connection to the server and exit the program, enter EXIT on the server's command
line.

The server also allows user input on the server side. All commands given to the server start with a
/ character, this is to avoid accidentally broadcasting a global message.

Here is a list of all the available commands:

    COMMAND                     DESCRIPTION
    /broadcast (message)        Sends a global message to all clients.

    /server (message)           Sends a message to the server. This essentially bounces the message
                                back.

    /direct (client):(message)  Sends a message to a single client. NOTE there is no space between
                                the client's name, the colon and the start of the message.

    /clients                    Returns a list of all clients connected to the server along with
                                their admin status.

    /kick (client)              Removes a client from the server.

    /promote (client)           Promotes a client to admin status.

    /demote (client)            Demotes a client from admin status.