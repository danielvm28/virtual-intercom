package control;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import model.Concierge;
import model.IntercomSystem;
import util.DiscoveryThread;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ConciergeController implements Initializable {

    private IntercomSystem intercomSystem;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        checkEmergency();
    }

    public void checkEmergency(){
        new Thread(() -> {
            Platform.runLater(() ->{
                int x = IntercomSystem.emergency;
                if (x==1 || x==2){
                    Alert alert;
                    alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Emergencia");
                    alert.setHeaderText("");
                    alert.setContentText("El apartamento A0"+x+" se encuentra en una emergencia");
                    IntercomSystem.emergency=0;
                    Optional<ButtonType> result = alert.showAndWait();
                }
            });
            checkEmergency();
        }).start();
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
        alert.show();
    }

}
