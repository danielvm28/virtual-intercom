package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Concierge {

    private ServerSocket serverSocketReceive1;
    private ServerSocket serverSocketReceive2;
    private ServerSocket serverSocketSend1;
    private ServerSocket serverSocketSend2;
    private Socket clientSocketReceive1;
    private Socket clientSocketReceive2;
    private Socket clientSocketSend1;
    private Socket clientSocketSend2;
    private PrintWriter out;
    private BufferedReader in;
    private PrintWriter out2;
    private BufferedReader in2;

    /**
     * Initializes all the server and client sockets to start the threads to await responses
     */
    public void ini() {
        try {
            serverSocketReceive1 = new ServerSocket(8880);
            serverSocketSend1 = new ServerSocket(8881);
            clientSocketReceive1 = serverSocketReceive1.accept();
            clientSocketSend1 = serverSocketSend1.accept();

            serverSocketReceive2 = new ServerSocket(8882);
            serverSocketSend2 = new ServerSocket(8883);
            clientSocketReceive2 = serverSocketReceive2.accept();
            clientSocketSend2 = serverSocketSend2.accept();

            out = new PrintWriter(clientSocketSend1.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocketReceive1.getInputStream()));

            out2 = new PrintWriter(clientSocketSend2.getOutputStream(), true);
            in2 = new BufferedReader(new InputStreamReader(clientSocketReceive2.getInputStream()));

            // Start a thread to await responses for resident 1
            new Thread(this::hostResponsesR1).start();
            // Start a thread to await responses for resident 2
            new Thread(this::hostResponsesR2).start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Hosts all the responses directed to the resident 1, who opened sockets in the ports 8880 and 8881 for receiving and sending packets respectively
     */
    public void hostResponsesR1() {
        try {
            String inputLine;

            while (true) {
                inputLine = in.readLine();
                char firstChar = inputLine.charAt(0);

                if (firstChar == 'A') {
                    out2.println(inputLine);
                } else if (firstChar == 'V') {
                    IntercomSystem.visitDecision = inputLine.substring(4);
                } else {
                    IntercomSystem.emergency = 1;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Hosts all the responses directed to the resident 2, who opened sockets in the ports 8882 and 8883 for receiving and sending packets respectively
     */
    public void hostResponsesR2() {
        try {
            String inputLine;

            while (true) {
                inputLine = in2.readLine();
                char firstChar = inputLine.charAt(0);

                if (firstChar == 'A') {
                    out.println(inputLine);
                } else if (firstChar == 'V') {
                    IntercomSystem.visitDecision = inputLine.substring(4);
                } else {
                    IntercomSystem.emergency = 2;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends an alert of new visitor to the respective apartment with the name of the visitor
     * @param apartment
     * @param visitor
     */
    public void sendVisitAlert(String apartment, String visitor) {
        if (apartment.equals("A01")) {
            out.println("V - " + visitor);
        } else {
            out2.println("V - " + visitor);
        }
    }
}
