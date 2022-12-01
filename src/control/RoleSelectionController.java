package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import main.Main;
import model.IntercomSystem;
import model.Resident;

import java.io.IOException;

public class RoleSelectionController {
    @FXML
    private Button res1BTN;

    @FXML
    private Button conBTN;

    @FXML
    private Button res2BTN;

    private final IntercomSystem intercomSystem;

    public RoleSelectionController() throws IOException {
        intercomSystem = IntercomSystem.getInstance();
    }

    @FXML
    void launchWindow(ActionEvent event) throws IOException {
        // Gets the information of the button pressed to determine the role
        Button b = (Button) event.getSource();
        String s = b.getText();

        // Launches different window depending on selection
        if (s.equalsIgnoreCase("Residente 1")) {
            Main.openWindow("../ui/Apartment.fxml", new ApartmentController(intercomSystem.getResident1()));
        } else if (s.equalsIgnoreCase("Residente 2")){
            Main.openWindow("../ui/Apartment.fxml", new ApartmentController(intercomSystem.getResident2()));
        } else {
            Main.openWindow("../ui/Concierge.fxml", new ConciergeController(intercomSystem.getConcierge()));
        }
    }
}
