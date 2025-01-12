package Main;

import java.io.IOException;
import java.util.List;
import ComponentController.ClubListItemController;
import database.Club;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class clubController {
    private Main main;

    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    private VBox clubListViewer;
    @FXML
    private ScrollPane clubListScrollPan;

    @FXML
    public void playerButtonClicked(ActionEvent actionEvent) throws Exception {
        if (main != null) {
            main.showPlayerScene();
        } else {
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
        } else {
        }
    }

    @FXML
    private void homeButtonClicked(ActionEvent event) throws Exception {
        if (main != null) {
            main.showHomeScreenScene();
        } else {
        }
    }

    @FXML
    public void myClubButtonClicked(ActionEvent event) throws IOException {
        if (main != null) {
            main.showMyClubScene();
        } else {
        }
    }

    public void loadClub() {
        clubListViewer.getChildren().clear();
        List<Club> clubList = main.getAllClubs();
        if (clubList == null) {
            return;
        }
        for (int i = 0; i < clubList.size(); i++) {
            Club currClub = clubList.get(i);
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/Components/ClubListItem.fxml"));
                HBox listItem = fxmlLoader.load();
                ClubListItemController clubListItemController = fxmlLoader.getController();
                clubListItemController.setClub(currClub);
                clubListItemController.setClubName();
                clubListItemController.setClubImage(currClub.getName());
                clubListViewer.getChildren().add(listItem);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    private Button addPlayerButton;

    @FXML
    private Button myClubButton;

    void setInvisibleMyClubAndAddPlayer(){
        addPlayerButton.setVisible(false);
        myClubButton.setVisible(false);
    }

}
