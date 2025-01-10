package Main;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class homeController {
    private Main main;

    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    public void playerButtonClicked(ActionEvent actionEvent) throws Exception {
        if (main != null) {
            main.showPlayerScene();

        } else {
            System.out.println("main not initialized!");
        }
    }

    @FXML
    private void clubsButtonClicked(ActionEvent event) throws Exception {
        if (main != null) {
            main.showClubScene();
        } else {
            System.out.println("something wrong");
        }
    }

    @FXML
    private void addPlayerButtonClicked(ActionEvent event) throws Exception {
        if (main != null) {
            main.showAddPlayerScene();
        } else {
            System.out.println("Something wrong at add player");
        }
    }

    @FXML
    private void homeButtonClicked(ActionEvent event) throws Exception {
        if (main != null) {
            main.showHomeScreenScene();
        } else {
            System.out.println("something wrong");
        }
    }

    @FXML
    public void myClubButtonClicked(ActionEvent event) throws IOException {
        if (main != null) {
            main.showMyClubScene();
        } else {
            System.out.println("my club not working");
        }
    }

    @FXML
    void logoutButtonClicked(ActionEvent event) throws IOException {
        if (main.allPlayers != null) {
            System.out.println("sending and exiting....");
        } else {
            System.out.println("main not initialized!");
        }
        main.showLoginScene();
    }

}