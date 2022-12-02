package model;

import util.EmailSenderService;

import javax.mail.MessagingException;

public class IntercomSystem {
    private String serverIP;
    private static IntercomSystem instance;
    private Resident resident1;
    private Resident resident2;
    private Concierge concierge;

    public static String chat = "";
    public static int emergency = 0;
    public static String incomingVisitor = "";
    public static String visitDecision = "";

    private IntercomSystem() {
        resident1 = new Resident("A01", 8880, 8881);
        resident2 = new Resident( "A02", 8882, 8883);
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

    public Resident getResident1() {
        return resident1;
    }

    public void setResident1(Resident resident1) {
        this.resident1 = resident1;
    }

    public Resident getResident2() {
        return resident2;
    }

    public void setResident2(Resident resident2) {
        this.resident2 = resident2;
    }

    public Concierge getConcierge() {
        return concierge;
    }

    public void setConcierge(Concierge concierge) {
        this.concierge = concierge;
    }

    public void sendText(String text, Resident r) {
        r.sendText(text, MessageType.CHAT);
        chat = chat + "\n" + r.getName() + ": " + text;
    }

    public void sendEmergencyEmail(String name, String contact, Resident r) throws MessagingException {
        r.sendText("Emergencia", MessageType.EMERGENCY);
        EmailSenderService emailSenderService = new EmailSenderService();
        emailSenderService.sendEmail("EMERGENCIA", "Ha ocurrido una emergencia en el apartamento " + name + ". \nY usted aparece como contacto de emergencia", contact);
    }
}
