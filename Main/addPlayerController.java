package Main;

import java.io.IOException;

import database.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class addPlayerController {
    private Main main;

    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    TextField playerNameField;
    @FXML
    TextField playerCountryField;
    @FXML
    TextField playerAgeField;
    @FXML
    TextField playerHeightField;
    @FXML
    TextField playerClubField;
    @FXML
    TextField playerPositionField;
    @FXML
    TextField playerNoField;
    @FXML
    TextField playerSalaryField;

    @FXML
    public void saveButtonClicked(ActionEvent event) throws Exception {
        try {
            String name = playerNameField.getText();
            if (name == null || name.isEmpty()) {
                showPrompt("Player name cannot be empty!");
                return;
            }
            String country = playerCountryField.getText();
            if (country == null || country.isEmpty()) {
                showPrompt("Player country cannot be empty!");
                return;
            }
            String club = playerClubField.getText();
            if (country == null || club.isEmpty()) {
                showPrompt("Player club cannot be empty!");
                return;
            }
            String position = playerPositionField.getText();
            if (country == null || position.isEmpty()) {
                showPrompt("Player position cannot be empty!");
                return;
            }

            int playerNo;
            String playerNoText = playerNoField.getText();
            if (playerNoText == null || playerNoText.isEmpty()) {
                playerNo = -1;
            } else {
                playerNo = Integer.parseInt(playerNoText);
            }

            String salaryText = playerSalaryField.getText();
            if (salaryText == null || salaryText.isEmpty()) {
                showPrompt("Player salary cannot be empty!");
                return;
            }
            int salary = Integer.parseInt(salaryText);

            String ageText = playerAgeField.getText();
            if (ageText == null || ageText.isEmpty()) {
                showPrompt("Player age cannot be empty!");
                return;
            }
            int age = Integer.parseInt(ageText);

            String heightText = playerHeightField.getText();
            if (heightText == null || heightText.isEmpty()) {
                showPrompt("Player height cannot be empty!");
                return;
            }
            double height = Double.parseDouble(heightText);

            boolean status = main.client
                    .addNewPlayerToServer(new Player(name, country, age, height, club, position, playerNo, salary));

            if (!status) {
                showPrompt("Player with same name detected..");
                return;
            }

            showPrompt("Player Added SuccessFully");
            main.sceneCache.remove("player");
            main.controllerCache.remove("player");
            main.sceneCache.remove("club");
            main.controllerCache.remove("club");
            resetAddPlayerField();
        } catch (NumberFormatException e) {
            showPrompt("Enter valid value");
        }
    }

    public void showPrompt(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Prompt");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    private void resetAddPlayerField() {
        playerNameField.setText(null);
        playerCountryField.setText(null);
        playerClubField.setText(null);
        playerPositionField.setText(null);
        playerNoField.setText(null);
        playerSalaryField.setText(null);
        playerAgeField.setText(null);
        playerHeightField.setText(null);
    }

    @FXML
    public void playerButtonClicked(ActionEvent actionEvent) throws Exception {
        if (main != null) {
            main.showPlayerScene();
        }
    }

    @FXML
    private void clubsButtonClicked(ActionEvent event) throws Exception {
        if (main != null) {
            main.showClubScene();
        }
    }

    @FXML
    private void addPlayerButtonClicked(ActionEvent event) throws Exception {

        if (main != null) {
            main.showAddPlayerScene();
        }
    }

    @FXML
    private void homeButtonClicked(ActionEvent event) throws Exception {
        if (main != null) {
            main.showHomeScreenScene();
        }
    }

    @FXML
    public void myClubButtonClicked(ActionEvent event) throws IOException {

        if (main != null) {
            main.showMyClubScene();
        }
    }
}
