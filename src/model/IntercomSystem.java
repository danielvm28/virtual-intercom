package model;

public class IntercomSystem {
    private String serverIP;
    private static IntercomSystem instance;
    private Resident resident1;
    private Resident resident2;
    private Concierge concierge;

    private String chat;
    private String newMessage;

    private IntercomSystem() {
        resident1 = new Resident(false, "A01", 8887);
        resident2 = new Resident(true, "A02", 8889);
        concierge = new Concierge();
    }

    public static IntercomSystem getInstance() {
        if (instance == null) {
            instance = new IntercomSystem();
        }

        return instance;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
        System.out.println(serverIP);
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(String newMessage) {
        this.newMessage = newMessage;
        chat += "\n " + newMessage;
    }

    public void startChat() {
        resident1.connectWithConcierge();
        new Thread(() -> concierge.hostChat()).start();
        new Thread(() -> resident1.startChat()).start();
    }
}
