package main;

import model.Concierge;
import model.DiscoveryThread;
import model.Resident;

public class Main {
    public static void main(String[] args) {
        new Thread(new DiscoveryThread()).start();
        Resident r1 = new Resident(false, "A01", 8887);
        r1.connectWithConcierge();

        Concierge c = new Concierge();

        new Thread(c).start();
        new Thread(r1).start();

    }
}
