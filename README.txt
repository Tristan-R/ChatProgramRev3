SERVER
--------
The server can be launched from the ChatServer file after compiling. To change the port number that
the server will run on, add the flag -csp followed by a port number to the command line when
running the file. Omitting the flag will connect to the default port of 14001.

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


CLIENT
--------
A client can be launched from the ChatClient file after compiling. To change the port number that
the client will try to connect on, add the flag -ccp followed by a port number to the command line
when running the file. To change the IP address, add the flag -cca followed by an IP address to the
command line when running the program. You can add both, one or none of these flags when launching
the program. Omitting these flags will connect you to localhost on port 14001 if there is a server
running at this location.

If a server can be found at the IP address and port you have chosen then you will be connected to
the server, if there is not then the program will error and exit.

To close the connection to the server and exit the program, enter EXIT on the clients's command
line.

The client allows the user to send a message by typing their message and clicking enter. The client
also allows the user to perform more advanced commands. All commands start with a / character.

Here is a list of all the available commands:

    COMMAND                     DESCRIPTION
    /server (message)           Sends a message to the server. NOTE this command is restricted to
                                admins only.

    /direct (client):(message)  Sends a message to a single client. NOTE there is no space between
                                the client's name, the colon and the start of the message.

    /clients                    Returns a list of all clients connected to the server.

    /kick (client)              Kicks a client from the server. NOTE this command is restricted to
                                admins only. Admins cannot be kicked except by the server.


In the case that the connection to the server is poor and messages are received by the server
broken or from the server broken then the user will receive a broken message notification. On the
fourth broken message, the connection to the server will be terminated. To attempt reconnecting to
the server, relaunch the ChatClient application.