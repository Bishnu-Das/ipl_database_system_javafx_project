package Main;

import java.io.IOException;
import java.util.List;

import ComponentController.PlayerListItemController;
import Network.Client;
import database.Club;
import database.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class myClubController {
    private Main main;
    Client client;
    Club club;
    @FXML
    private ScrollPane clubListScrollPan;

    @FXML
    private Label clubName;

    @FXML
    private VBox playerListViewer;

    @FXML
    void addPlayerButtonClicked(ActionEvent event) throws Exception {
        main.showAddPlayerScene();
    }

    @FXML
    void clubsButtonClicked(ActionEvent event) throws Exception {
        main.showClubScene();
        for (Player player : main.myClub.getPlayers()) {
            System.out.println(player.getName());
            System.out.println(player.getClub());
        }
    }

    @FXML
    void homeButtonClicked(ActionEvent event) throws IOException {
        main.showHomeScreenScene();
    }

    @FXML
    void myClubButtonClicked(ActionEvent event) throws IOException {
        main.showMyClubScene();
    }

    @FXML
    void playerButtonClicked(ActionEvent event) throws Exception {
        main.showPlayerScene();
    }

    @FXML
    void sellButtonClicked(ActionEvent event) throws IOException {
        main.showSellPlayerScene();
    }

    @FXML
    void buyButtonClicked(ActionEvent event) throws IOException {
        main.showBuyPlayerScene();
    }

    @FXML
    void changePasswordButtonClicked(ActionEvent event) throws IOException {
        main.showChangePasswordScene();
    }

    public void setClubName(String name) {
        this.clubName.setText(name);
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void loadClubPlayer() {
        List<Player> players = main.myClub.getPlayers();
        playerListViewer.getChildren().clear();

        for (int i = 0; i < players.size(); i++) {
            Player currPlayer = players.get(i);
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/Components/PlayerListItem.fxml"));
                HBox listItem = fxmlLoader.load();

                PlayerListItemController playerListItemController = fxmlLoader.getController();
                playerListItemController.setClient(client);
                playerListItemController.setPlayer(currPlayer);
                playerListItemController.setPlayerName(currPlayer.getName());
                playerListItemController.setPlayerNumber(currPlayer.getNumber());
                playerListItemController.setPlayerPosition(currPlayer.getPosition());
                playerListItemController.setCountryImage(currPlayer.getCountry());
                playerListItemController.setMain(this.main);

                playerListViewer.getChildren().add(listItem);
            } catch (IOException e) {
                System.out.println("not loading player");
                e.printStackTrace();
            }
        }
    }
}