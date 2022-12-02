package control;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import model.IntercomSystem;
import model.Resident;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ApartmentController implements Initializable {
    private  IntercomSystem intercomSystem;

    private  Resident rs;

    public ApartmentController(Resident rs) throws IOException {
        intercomSystem = IntercomSystem.getInstance();
        this.rs = rs;

        // Begins connecting the resident with the concierge
        rs.connectWithConcierge();
    }

    @FXML
    private Button buttonAPT;

    @FXML
    private Button buttonCambiarContacto;

    @FXML
    private Button buttonEnviar;

    @FXML
    private Button buttonPanico;

    @FXML
    private Label labelCorreo;

    @FXML
    private TextArea textAreaChat;

    @FXML
    private TextField textFeildCorreo;

    @FXML
    private TextField textFieldMensajeEnviar;

    private String actChat;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        actChat = "";

        textFieldMensajeEnviar.setOnKeyPressed(event -> {
            if (event.getCode()== KeyCode.ENTER){
                buttonEnviar.fire();
            }
        });

        // Start the window differently depending on the apartment
        if (rs.getName().equals("A01")){
            buttonAPT.setText("A01");
        } else{
            buttonAPT.setText("A02");
        }

        new Thread(() -> {
            rs.awaitResponse();
        }).start();

        // Update the chat consistently
        updateChat();

        // Method to check indefinitely if a new visitor is coming
        visitor();
    }

    public void visitor(){
        new Thread(() -> {
            Platform.runLater(() ->{
                String x = ""+IntercomSystem.incomingVisitor;
                IntercomSystem.incomingVisitor="";

                if (!x.equals("")){
                    Alert alert;
                    alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Visitante");
                    alert.setHeaderText("");
                    alert.setContentText("La persona "+x+" desea entrar, si desea que la persona ingrese presione Aceptar");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK){
                        intercomSystem.determineVisitorDecision(true, rs);
                    } else {
                        intercomSystem.determineVisitorDecision(false, rs);
                    }
                }
            });
            visitor();
        }).start();
    }

    public void updateChat() {
        new Thread(()-> {

            // Update the chat in the main thread
            Platform.runLater(() -> {
                if (actChat.length() != IntercomSystem.chat.length()) {
                    actChat = IntercomSystem.chat;
                    textAreaChat.setText(actChat);
                }
            });

            updateChat();

        }).start();
    }

    @FXML
    void enviarMensaje(ActionEvent event) {
        Alert alert;

        // Updates the new message to the controller in order for the resident to get it and send it via TCP
        if (textFieldMensajeEnviar.getText().trim().equals("")){
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No se ha escrito ninguno mensaje");
            alert.setHeaderText("Mensaje");
            alert.setContentText("Tiene que escribir un mensaje para continuar");
            alert.show();
        }else {
            intercomSystem.sendText(textFieldMensajeEnviar.getText(), rs);
            textAreaChat.setText(IntercomSystem.chat);
            textFieldMensajeEnviar.clear();
        }
    }

    @FXML
    void panico(ActionEvent event) throws MessagingException {

        // Triggers an alert to the concierge and also sends an email to the address marked as emergency contact
        Alert alert;
        if(rs.getEmergencyContact().trim().equals("")){
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Sin contacto de emergencia");
            alert.setHeaderText("Correo");
            alert.setContentText("No tenemos registrado ningun correo de emergencia");
            alert.show();

        } else{
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Boton de panico");
            alert.setHeaderText("Correo");
            alert.setContentText("Se envio un correo de emergencia a esta direccion: "+rs.getEmergencyContact());
            alert.show();
            intercomSystem.sendEmergencyEmail(rs.getName(), rs.getEmergencyContact(), rs);
        }

    }

    @FXML
    void CambiarContacto(ActionEvent event) {

        // Sets the emergency contact for the resident
        Alert alert;
        if(textFeildCorreo.getText().trim().equals("")){
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Sin contacto de emergencia");
            alert.setHeaderText("Correo");
            alert.setContentText("No se escribio ningun contacto de emergencia");
        } else {
            rs.setEmergencyContact(textFeildCorreo.getText());
            labelCorreo.setText(rs.getEmergencyContact());
            textFeildCorreo.setText("");
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cambio contacto de emergencia");
            alert.setHeaderText("Contacto de emergencia");
            alert.setContentText("Se cambio el contacto de emergencia a: "+rs.getEmergencyContact());
        }
        alert.show();
    }
}
