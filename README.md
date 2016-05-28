--TEAM FOR-TRUE --

We now have a script called "run." 

Usage: Mac and Linux - ./run 2 or ./run 4
       Windows - run 2, run 4 or double click run.bat from file browser

./run with no number argument defaults to 4 servers.

Runs ./gradlew build (Mac & Linux only) and fires up either 2 or 4 servers connected to a client, 
each of which are in their own terminal.

As of right now, it works in the lab. I'm not sure about while SSHd in. 
Going to work on getting it working in a single terminal window for SSHd users. 
Current issue in a single terminal is that during the hello handshake 
some text gets glued together that shouldn't.

Currently, the terminals opened for each server/client are in xterm.
If you would like them to open with the default terminal, change the
xterm -e strings in the run script to gnome-terminal -e. 

If you would like the servers and client to be all in one terminal, 
simply delete the xterm -e strings before the java -cp commands.

If you have any questions, comments, suggestions, or vehement disagreements,
please let me know in a team meeting, by trello or by e-mail.

TODO: get ./run 1 working
