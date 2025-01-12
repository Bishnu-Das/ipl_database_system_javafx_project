package Main;

import java.io.IOException;

import Network.Client;
import database.Club;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class loginController {

    private Main main;
    Client client;

    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    void loginButtonClicked(ActionEvent event) throws Exception {
        String password = passwordField.getText();
        String username = usernameField.getText();
        if (main != null) {
            String ip = "127.0.0.1";
            int port = 44444;

            client = new Client(ip, port, main);
            if (client != null) {
                main.setClient(client);
            } else {
            }
            Club clubOb = client.sendLoginRequest(username, password);
            if (clubOb != null && clubOb instanceof Club) {
                main.showHomeScreenScene();
                main.setMyClub(clubOb);
                return;
            } else if (clubOb == null) {
                showPrompt("Invalid username or password..");
            }

        }
    }
    @FXML
    void continueAsGuestButtoClicked(ActionEvent event) throws IOException{
        if(main!=null){
            String ip = "127.0.0.1";
            int port = 44444;
            client = new Client(ip, port, main);
            if(client!=null){
                main.setClient(client);
            }
            boolean status =  client.sendLoginRequestAsGuest();
            if(status){
                main.showHomeScreenScene();
            }
        }
    }

    public void showPrompt(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);

        alert.setTitle("Prompt");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    

    @FXML
    void exitButtonClicked(ActionEvent event) {
        System.exit(0);
    }

}
