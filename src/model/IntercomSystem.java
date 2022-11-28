package model;

public class IntercomSystem {
    private String serverIP;
    private static IntercomSystem instance;

    private IntercomSystem() {

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
}
