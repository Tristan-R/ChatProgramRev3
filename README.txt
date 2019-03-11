COMPILE AND RUN
-----------------
I have included two folders:

    COMMAND_LINE_VERSION - This contains a version of the program before I added the GUIs. It omits
    all of the files that are not needed for the GUI and also additional code within files that is
    only needed when using the GUI.

    GUI_VERSION - This contains a version of the program after I added the GUI. This involves new
    files needed exclusively for the GUI and also additional code within shared files that is only
    needed for the GUI.

To compile and run the command line version, compile and run as usual.

The JavaFX libraries are required to compile and run the GUI version. The GUI was built using
JavaFX 11 however it does not incorporate features that were added after JavaFX 2.0.

I cannot guarantee it will run using a version of JavaFX prior to JavaFX 11 and I have not had
experience with compiling and/or running previous versions of JavaFX. I will however explain how to
compile and run the GUI with JavaFX 11.

To compile with JavaFX 11 on Linux/Mac or Bash for Windows, compile using:
javac --module-path path/to/javafx-sdk-11.0.2/lib --add-modules=javafx.controls,javafx.fxml *.java

To run the program use:
java --module-path path/to/javafx-sdk-11.0.2/lib --add-modules=javafx.controls,javafx.fxml ChatServer -gui

Replace ChatServer -gui with ChatClient -gui for the client. The -csp, -ccp and -cca can still be
entered before or after the -gui flag too.

For more information on compiling and running using JavaFX 11 including instructions for using the
Windows command line visit: https://openjfx.io/openjfx-docs/#install-javafx

If using Oracle Java 8 - 10 (not OpenJDK) then JavaFX 8 or 9 will be pre-packaged into Java. To run
the GUI just compile and run as if the program was a command line program. If the GUI does not work
then this may be a compatibility issue between JavaFX 9/10 and JavaFX 11. Please try using
JavaFX 11, instruction for getting started can be found at: https://openjfx.io/openjfx-docs/


SERVER
-----------------
The server can be launched from the ChatServer file after compiling. To change the port number that
the server will run on, add the flag -csp followed by a port number to the command line when
running the file. Omitting the flag will connect to the default port of 14001.

If the port you have chosen is free then the server will start accepting clients on this port, if
the port is not free then the program will error and exit.

To close all connection to the server and exit the program, enter EXIT on the server's command
line. The server will wait for 3 seconds before closing to allow clients on slower connections time
to receive the closing command and exit.

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
-----------------
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


COMMAND LINE
-----------------
A command line version is available for both the server and the client. The command line version
for each will launch by default.


GUI
-----------------
A GUI is available for both the server and clients. The GUI for each can be launched by adding the
-gui flag to the command line at launch. The GUI works just like the command line version and all
commands are available. To send messages or commands you can click the Enter key on your keyboard
or the Send button within the GUI.

To exit the GUI and close the connection on both the server and client versions, send the EXIT
command as in the command line version or click the Close button at the top right of the GUI.