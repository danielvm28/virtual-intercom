# virtual-intercom

Members
- Daniel Valencia
- Juan Camilo Bernal
- Juan Pablo Uribe

## Procedure
The client requires an intercom program for a two-apartment building. This program should have a terminal in the Concierge's lodge where a visitor can be announced by name and the apartment they are visiting. There is also a terminal in each apartment with a chat function among the residents. The residents can either allow or deny entry to an announced visitor and press a panic button that sends an email to a previously chosen person and informs the Concierge.

To simplify the initial connection, a connection is opened on ports 8888 on both the client and server so that the server's IP address can be obtained without manually entering it. This procedure is done using UDP.

To address the aforementioned problem, a program was developed using the Java programming language. It was necessary to identify the classes involved: The Concierge's lodge, which serves as the server with its requested functions. The apartments, which use TCP for the chat functionality, SMTP for the panic button and email. Thirdly, a separate class was assigned to connect the model with the graphical interfaces. Finally, JavaFX was used for the graphical interfaces, requiring (3) screens: one for selecting whether it is apartment "A01," "A02," or "Concierge's Lodge," another for each apartment (sharing the same interface), and one for controlling the Concierge's Lodge.

## Challenges
Difficulty was encountered in the chat functionality. This was caused by the TCP code where the same port is used for both listening and sending, preventing correct operation. Another challenging area was sending a visitor alert to the apartments. This problem arose from how the chat between apartments was handled, where ports were alternately used for listening or writing, causing the order to change and errors in communication.

## Solutions
For the chat between apartments, it was necessary to modify the total number of ports used in the TCP code. This allowed the chat to transition from turn-based to more realistic. Having one port for listening and another for sending for each apartment (a total of four ports) allows one of the interlocutors to send more than one message instead of waiting for a response. This same action also resolved the problem of the visitor alert. Now there is a port that is constantly receiving messages, which is used to communicate the presence of a visitor to the resident. It is identified by the first letter of the message, indicating an alert ("V" for visitor, distinguishing it from "A" for apartment and "P" for panic). This approach is also used to send a panic alert from an apartment to the Concierge's Lodge.
