package control;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.IntercomSystem;
import model.Resident;

import java.net.URL;
import java.util.ResourceBundle;

public class ApartamentoController implements Initializable {
    private  IntercomSystem intercomSystem = IntercomSystem.getInstance();

    private  Resident rs;

    public ApartamentoController(Resident rs) {
        this.rs = rs;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (rs.getName().equals("A01")){
            buttonAPT.setText("A02");
        } else{
            buttonAPT.setText("A01");
        }
        new Thread(()-> {
            while (true){
                try {
                    Thread.sleep(500);
                    if (!rs.isWriting){
                        textAreaChat.setText(intercomSystem.getChat());
                        labelTurno.setText(rs.isWriting() ? "Escribiendo" : "Esperando");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @FXML
    void enviarMensaje(ActionEvent event) {
        Alert alert;
        if (textFieldMensajeEnviar.getText().trim().equals("")){
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No se ha escrito ninguno nombre de visitante");
            alert.setHeaderText("Visitante");
            alert.setContentText("Recuerde que se debe informar el nombre de la persona que desea entrar");
        }else {
            intercomSystem.setNewMessage(rs.getName()+": "+textFieldMensajeEnviar.getText());
            textAreaChat.setText(intercomSystem.getChat());
            labelTurno.setText(rs.isWriting() ? "Escribiendo" : "Esperando");
        }
    }

    @FXML
    void panico(ActionEvent event) {

    }
}
