package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Concierge implements Runnable {

    public static void main(String[] args) {
        new Thread(new DiscoveryThread()).start();
    }

    private boolean writing1 = false;

    public Concierge() {
        //writing1 = false;
    }

    @Override
    public void run() {
        try (
                ServerSocket serverSocket = new ServerSocket(8887);
                ServerSocket serverSocket2 = new ServerSocket(8889);
                Socket clientSocket = serverSocket.accept();
                Socket clientSocket2 = serverSocket2.accept();

                PrintWriter out =
                        new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                BufferedReader stdIn =
                        new BufferedReader(
                                new InputStreamReader(System.in));

                PrintWriter out2 = new PrintWriter(clientSocket2.getOutputStream(), true);
                BufferedReader in2 = new BufferedReader(
                        new InputStreamReader(clientSocket2.getInputStream()));
        ) {

            String inputLine;
            boolean closeChat1 = false;
            boolean closeChat2 = false;

            while (true) {
                if (writing1) {
                    inputLine = in.readLine();

                    // Closes the chat if both parts agree
                    if (inputLine.equalsIgnoreCase("close")) {
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
                    if (inputLine.equalsIgnoreCase("close")) {
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
}
