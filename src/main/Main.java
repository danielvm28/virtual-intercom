package main;

import model.Concierge;
import model.DiscoveryThread;
import model.IntercomSystem;
import model.Resident;

public class Main {
    public static void main(String[] args) {
        new Thread(new DiscoveryThread()).start();
        IntercomSystem intercomSystem = IntercomSystem.getInstance();
        intercomSystem.startChat();
    }
}
