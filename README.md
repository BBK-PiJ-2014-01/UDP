# UDP
Client-server application where multiple clients connects to one audio server.
    - The first client to connect sends an audio recording (in chunks) to the server.
    - The server relays the audio stream to all the clients who connect after.
    - Once a client has stopped sending a file, the server chooses the next connecting client as a sending client.

To launch the service:  java AudioServerLauncher
To Launch a client:     java AudioClientLauncher

On first connection, over TCP/IP, clients and central server support the following messaging protocol:
Step 1:
    - Client requests a unique ID
    Protocol message string: "requestUUID"
    - Server responds, sending a type 4 (pseudo randomly generated) universally unique ID
    Format example: "067e6162-3b6f-4ae2-a171-2470b63dff00"
Step 2:
    - Client requests if in first position
    Protocol message aggregated string: "requestROLE" + client UUID
    - Server responds "FIRST" if client is first to connect, or "NOT FIRST" if not.
Step 3:
    - Client requests what communication protocol to use to send or receive the audio file
    Protocol message: "getPROTOCOL"
    - Client responds "UDP"
Step 4:
    - Bother clients and server opens a UDP connection.
    If the client was first to connect, it sends an audio file to the server
    If the client was not first, it receives the audio file relayed by the server, from the first connected client.
Step 5:
    - Client notifies the server that it is disconnecting 
    Protocol message: "closeCONNECTION"
    - Server close the thread handling the client

Screenshots of an example available in the Word document "ScreenShots.docx"

Testing threading proved very difficult.
The current TestThread generates 10 threads.
When test launched, no failure identified so far with transfer files going either way (client <-> server).


