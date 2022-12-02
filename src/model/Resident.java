package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Resident {
    private String name;
    private int portSend;
    private int portReceive;
    private String emergencyContact;
    private IntercomSystem intercomSystem;
    private Socket echoSocket;
    private Socket receptionSocket;
    private PrintWriter out;
    private BufferedReader in;

    public Resident(String name, int portSend, int portReceive)  {
        this.portSend = portSend;
        this.portReceive = portReceive;
        this.name = name;
        emergencyContact = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPortSend() {
        return portSend;
    }

    public void setPortSend(int portSend) {
        this.portSend = portSend;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public void connectWithConcierge() {


        // Find the server using UDP broadcast
        try {
            //Open a random port to send the package
            DatagramSocket c = new DatagramSocket();
            c.setBroadcast(true);

            byte[] sendData = "DISCOVER_FUIFSERVER_REQUEST".getBytes();

            //Try the 255.255.255.255 first
            try {
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), 8888);
                c.send(sendPacket);
                System.out.println(getClass().getName() + ">>> Request packet sent to: 255.255.255.255 (DEFAULT)");
            } catch (Exception e) {
            }

            // Broadcast the message over all the network interfaces
            Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();

                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue; // Don't want to broadcast to the loopback interface
                }

                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    InetAddress broadcast = interfaceAddress.getBroadcast();
                    if (broadcast == null) {
                        continue;
                    }

                    // Send the broadcast package!
                    try {
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, 8888);
                        c.send(sendPacket);
                    } catch (Exception e) {
                    }

                    System.out.println(getClass().getName() + ">>> Request packet sent to: " + broadcast.getHostAddress() + "; Interface: " + networkInterface.getDisplayName());
                }
            }

            System.out.println(getClass().getName() + ">>> Done looping over all network interfaces. Now waiting for a reply!");

            //Wait for a response
            byte[] recvBuf = new byte[15000];
            DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
            c.receive(receivePacket);

            //We have a response
            System.out.println(getClass().getName() + ">>> Broadcast response from server: " + receivePacket.getAddress().getHostAddress());

            //Check if the message is correct
            String message = new String(receivePacket.getData()).trim();
            if (message.equals("DISCOVER_FUIFSERVER_RESPONSE")) {
                //DO SOMETHING WITH THE SERVER'S IP (for example, store it in your controller)
                IntercomSystem controller = IntercomSystem.getInstance();
                controller.setServerIP(receivePacket.getAddress().toString().substring(1));
            }

            //Close the port!
            c.close();

            intercomSystem = IntercomSystem.getInstance();

            try {
                echoSocket = new Socket(intercomSystem.getServerIP(), portSend);
                receptionSocket = new Socket(intercomSystem.getServerIP(), portReceive);
                out = new PrintWriter(echoSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(receptionSocket.getInputStream()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException ex) {
            Logger.getLogger(Resident.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void awaitResponse() {
        try {
            String inputLine;
            inputLine = in.readLine();
            char firstChar = inputLine.charAt(0);

            if (firstChar == 'V') {
                IntercomSystem.incomingVisitor = inputLine.substring(4);
            } else {
                IntercomSystem.chat = (IntercomSystem.chat + "\n" + inputLine);
            }

            awaitResponse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendText(String text, MessageType type) {
        switch (type) {
            case CHAT:
                out.println(name + ": " + text);
                break;
            case VISIT:
                out.println("V - " + text);
                break;
            case EMERGENCY:
                out.println(text);
                break;
        }
    }
}
