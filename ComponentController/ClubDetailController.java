package ComponentController;

import java.io.IOException;
import java.util.List;
import database.Club;
import database.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class ClubDetailController {
    Club club;

    @FXML
    VBox playerList;

    @FXML
    private ImageView clubImage;

    @FXML
    private Label clubName;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox playerListVBox;

    @FXML
    private Label totalAmount;

    @FXML
    private Button maxSalaryButton;

    @FXML
    private Button maxHeightButton;

    @FXML
    private Button maxAgeButton;

    public void setClub(Club club) {
        this.club = club;
    }

    public void loadClubPlayer() {

        List<Player> players = club.getPlayers();

        int count = 0;
        for (int i = 0; i < players.size(); i++) {
            Player currPlayer = players.get(i);
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/Components/PlayerListItem.fxml"));
                HBox listItem = fxmlLoader.load();
                PlayerListItemController playerListItemController = fxmlLoader.getController();
                playerListItemController.setPlayer(currPlayer);
                playerListItemController.setPlayerName(currPlayer.getName());
                playerListItemController.setPlayerNumber(currPlayer.getNumber());
                playerListItemController.setPlayerPosition(currPlayer.getPosition());
                playerListItemController.setCountryImage(currPlayer.getCountry());
                playerList.getChildren().add(listItem);
                count++;
            } catch (IOException e) {
                System.out.println("not loading player");
                e.printStackTrace();
            }
        }
        System.out.println("total player: " + count);
    }

    public void setClubImage(String clubName) {
        System.out.println("Club Name: " + clubName.trim().toLowerCase());
        try {
            String url = "/images/clubimage/" + clubName.trim().toLowerCase() + ".png";
            System.out.println("Constructed URL: " + url);

            if (getClass().getResource(url) != null) {
                Image image2 = new Image(getClass().getResource(url).toString());
                this.clubImage.setImage(image2);
                System.out.println("Image loaded successfully.");
            }
            else{
                url = "/images/clubimage/default.png";
                Image image2 = new Image(getClass().getResource(url).toString());
                this.clubImage.setImage(image2);
                System.out.println("Image loaded successfully.");
            }
        } catch (Exception e) {
            System.out.println("Image not loading");
            e.printStackTrace();
        }
    }

    public void setClubName(String name) {
        this.clubName.setText(name);
    }

    public void setTotalAmount(String amount) {
        this.totalAmount.setText(amount);
    }

    @FXML
    private void maxSalaryButtonClicked() {

        playerList.getChildren().clear();
        List<Player> players = club.getMaxSalaryPlayer(club.getName());
        System.out.println(players.size());
        for (int i = 0; i < players.size(); i++) {
            Player currPlayer = players.get(i);
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/Components/PlayerListItem.fxml"));
                HBox listItem = fxmlLoader.load();
                PlayerListItemController playerListItemController = fxmlLoader.getController();
                playerListItemController.setPlayer(currPlayer);
                playerListItemController.setPlayerName(currPlayer.getName());
                playerListItemController.setPlayerNumber(currPlayer.getNumber());
                playerListItemController.setPlayerPosition(currPlayer.getPosition());
                playerListItemController.setCountryImage(currPlayer.getCountry());
                playerList.getChildren().add(listItem);
            } catch (IOException e) {
                System.out.println("not loading player");
                e.printStackTrace();
            }
        }

    }

    @FXML
    private void maxHeightButtonClicked() {
        System.out.println("Max Height Button Clicked");
        playerList.getChildren().clear();
        List<Player> players = club.getMaxHeightPlayer(club.getName());
        for (int i = 0; i < players.size(); i++) {
            Player currPlayer = players.get(i);
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/Components/PlayerListItem.fxml"));
                HBox listItem = fxmlLoader.load();
                PlayerListItemController playerListItemController = fxmlLoader.getController();
                playerListItemController.setPlayer(currPlayer);
                playerListItemController.setPlayerName(currPlayer.getName());
                playerListItemController.setPlayerNumber(currPlayer.getNumber());
                playerListItemController.setPlayerPosition(currPlayer.getPosition());
                playerListItemController.setCountryImage(currPlayer.getCountry());
                playerList.getChildren().add(listItem);
            } catch (IOException e) {
                System.out.println("not loading player");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void maxAgeButtonClicked() {
        System.out.println("Max Age Button Clicked");
        playerList.getChildren().clear();
        List<Player> players = club.getMaxAgePlayer(club.getName());
        for (int i = 0; i < players.size(); i++) {
            Player currPlayer = players.get(i);
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/Components/PlayerListItem.fxml"));
                HBox listItem = fxmlLoader.load();
                PlayerListItemController playerListItemController = fxmlLoader.getController();
                playerListItemController.setPlayer(currPlayer);
                playerListItemController.setPlayerName(currPlayer.getName());
                playerListItemController.setPlayerNumber(currPlayer.getNumber());
                playerListItemController.setPlayerPosition(currPlayer.getPosition());
                playerListItemController.setCountryImage(currPlayer.getCountry());
                playerList.getChildren().add(listItem);
            } catch (IOException e) {
                System.out.println("not loading player");
                e.printStackTrace();
            }
        }
    }
}
