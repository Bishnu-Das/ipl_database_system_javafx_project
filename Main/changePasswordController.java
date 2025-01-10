package Main;

import java.io.IOException;

import database.Club;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;

public class changePasswordController {
    Main main;
    Club club;

    @FXML
    private PasswordField newPassField;

    @FXML
    private PasswordField newPassFieldConfirm;

    @FXML
    private PasswordField prevPassField;

    @FXML
    void backButtonClicked(ActionEvent event) throws IOException {
        main.showMyClubScene();
    }

    @FXML
    void changePasswordButtonClicked(ActionEvent event) {
        String oldPassword = prevPassField.getText();
        String newPassword = newPassField.getText();
        String newPasswordConfirm = newPassFieldConfirm.getText();
        System.out.println(oldPassword + " " + newPassword + " " + newPasswordConfirm);
        if (!newPassword.equals(newPasswordConfirm)) {
            showPrompt("password not matching.");
            return;
        }
        boolean status = main.client.sendResetClubPassRequest(club.getName(), oldPassword, newPassword);
        if (status) {
            showPrompt("password changed successfully");
            prevPassField.clear();
            newPassField.clear();
            newPassFieldConfirm.clear();
        } else {
            System.out.println("invalid passwords");
            showPrompt("invalid password. try again.");
        }

    }

    public void showPrompt(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);

        alert.setTitle("Prompt");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setClub(Club myClub) {
        this.club = myClub;
    }

}
