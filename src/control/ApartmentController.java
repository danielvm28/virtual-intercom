package control;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.IntercomSystem;
import model.Resident;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ApartmentController implements Initializable {
    private  IntercomSystem intercomSystem;

    private  Resident rs;

    public ApartmentController(Resident rs) throws IOException {
        intercomSystem = IntercomSystem.getInstance();
        this.rs = rs;
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
    private Label labelTurno;

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

        // Start the window differently depending on the apartment
        if (rs.getName().equals("A01")){
            buttonAPT.setText("A02");
            new Thread(() -> {
                rs.awaitResponse();
            }).start();
            buttonEnviar.setDisable(true);
        } else{
            buttonAPT.setText("A01");
        }

        // Update the chat consistently
        updateChat();
    }

    public void updateChat() {
        new Thread(()-> {

            // Update the chat
            Platform.runLater(() -> {
                // Set the label text and send button according to the resident's turn
                if (rs.isWriting()) {
                    labelTurno.setText("Escribir");
                    buttonEnviar.setDisable(false);
                } else {
                    labelTurno.setText("Esperar");
                    buttonEnviar.setDisable(true);
                }

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
            new Thread(() -> {
                rs.awaitResponse();
            }).start();
        }
    }

    @FXML
    void panico(ActionEvent event) throws MessagingException {
        Alert alert;
        if(rs.getEmergencyContact().trim().equals("")){
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Sin contacto de emergencia");
            alert.setHeaderText("Correo");
            alert.setContentText("No tenemos registrado ningun correo de emergencia");

        } else{
            intercomSystem.sendEmergencyEmail(rs.getName(), rs.getEmergencyContact());
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Boton de panico");
            alert.setHeaderText("Correo");
            alert.setContentText("Se envio un correo de emergencia a esta direccion: "+rs.getEmergencyContact());
        }
        alert.show();
    }

    @FXML
    void CambiarContacto(ActionEvent event) {
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
