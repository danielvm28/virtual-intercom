package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import model.Concierge;
import util.DiscoveryThread;

import java.util.Optional;

public class ConciergeController {

    @FXML
    private Button buttonA01;

    @FXML
    private Button buttonA02;

    @FXML
    private TextField textFieldNombreVisitante;

    private Button button;

    private Concierge concierge;

    public ConciergeController(Concierge concierge) {
        this.concierge = concierge;
        new Thread(new DiscoveryThread()).start();
        //new Thread(concierge::hostChat).start();
        new Thread(concierge::ini).start();
    }

    @FXML
    void alert(ActionEvent event) {
        Button b = (Button) event.getSource();
        String apt = b.getText();
        Alert alert;
        if (!textFieldNombreVisitante.getText().isEmpty()){
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alerta de ingreso");
            alert.setHeaderText("");
            alert.setContentText("Se enviara una alerta de que"+textFieldNombreVisitante.getText()+"desea ir hacia el apartamento "+apt);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                //mandar alerta de ingreso
                if (apt.equals("A01")){
                    //ENVIAR ALERTA A A01
                } else {
                    //ENVIAR ALERTA A A02
                }
                //Se borra el nombre
                textFieldNombreVisitante.setText("");
            }
        } else {
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No se ha escrito ninguno nombre de visitante");
            alert.setHeaderText("Visitante");
            alert.setContentText("Recuerde que se debe informar el nombre de la persona que desea entrar");
        }
    }

}
