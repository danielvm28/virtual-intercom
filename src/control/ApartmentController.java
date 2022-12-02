package control;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.IntercomSystem;
import model.Resident;

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
    private Button buttonEnviar;

    @FXML
    private Button buttonPanico;

    @FXML
    private Label labelTurno;

    @FXML
    private TextArea textAreaChat;

    @FXML
    private TextField textFieldMensajeEnviar;

    private String actChat;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        actChat = "";

        // Start the window differently depending on the apartment
        if (rs.getName().equals("A01")){
            buttonAPT.setText("A02");
            buttonEnviar.setDisable(true);
        } else{
            buttonAPT.setText("A01");
        }

        new Thread(() -> {
            rs.awaitResponse();
        }).start();

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
            alert.setTitle("No se ha escrito ninguno nombre de visitante");
            alert.setHeaderText("Visitante");
            alert.setContentText("Recuerde que se debe informar el nombre de la persona que desea entrar");
        }else {
            intercomSystem.sendText(textFieldMensajeEnviar.getText(), rs);
            textAreaChat.setText(IntercomSystem.chat);
            textFieldMensajeEnviar.clear();
        }
    }

    @FXML
    void panico(ActionEvent event) {

    }
}
