package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Concierge {

    private boolean writing1;
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


    public Concierge() {
        writing1 = false;
    }

    public void hostResponsesR1() {
        try {
            String inputLine;

            while (true) {
                inputLine = in.readLine();
                out2.println(inputLine);
                writing1 = false;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void hostResponsesR2() {
        try {
            String inputLine;

            while (true) {
                inputLine = in2.readLine();
                out.println(inputLine);
                writing1 = false;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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

            new Thread(this::hostResponsesR1).start();
            new Thread(this::hostResponsesR2).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void hostChat() {
        try (
                ServerSocket serverSocket = new ServerSocket(8880);
                ServerSocket serverSocket2 = new ServerSocket(8881);
                Socket clientSocket = serverSocket.accept();
                Socket clientSocket2 = serverSocket2.accept();

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                PrintWriter out2 = new PrintWriter(clientSocket2.getOutputStream(), true);
                BufferedReader in2 = new BufferedReader(new InputStreamReader(clientSocket2.getInputStream()))
        ) {

            String inputLine;
            boolean closeChat1 = false;
            boolean closeChat2 = false;

            while (true) {
                if (writing1) {
                    inputLine = in.readLine();

                    // Closes the chat if both parts agree
                    if (inputLine != null && inputLine.equalsIgnoreCase("close")) {
                        closeChat1 = true;
                        if (closeChat2) {
                            break;
                        }
                    }
                    out2.println(inputLine);
                    writing1 = false;
                } else {
                    inputLine = in2.readLine();

                    // Closes the chat if both parts agree
                    if (inputLine != null && inputLine.equalsIgnoreCase("close")) {
                        closeChat2 = true;
                        if (closeChat1) {
                            break;
                        }
                    }
                    out.println(inputLine);
                    writing1 = true;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendVisitAlert() {

    }
}
