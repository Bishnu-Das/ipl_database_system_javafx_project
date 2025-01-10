package ComponentController;

import java.io.IOException;

import Main.Main;
import Network.Client;
import database.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PlayerListItemController {
    Main main;
    private Player player;
    Client client;

    @FXML
    private Label playerNumber;

    @FXML
    private Label playerName;

    @FXML
    private Label playerPosition;

    @FXML
    private Label playerFlag;

    @FXML
    private ImageView playerClub;
    @FXML
    private ImageView countryImage;

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setCountryImage(String countryName) {
        try {
            String url = "/images/countryimage/" + countryName.trim().toLowerCase() + ".png";
            if (getClass().getResource(url) == null) {
                url = "/images/countryimage/default.png";
            }
            Image image2 = new Image(getClass().getResource(url).toString());
            this.countryImage.setImage(image2);
        } catch (Exception e) {
            System.out.println("Image not loading");
            e.printStackTrace();
        }
    }

    public void setPlayerName(String playerName) {
        this.playerName.setText(playerName);
    }

    public void setPlayerNumber(int playerNumber) {
        if (playerNumber == -1) {
            this.playerNumber.setText("NA");
        } else {
            this.playerNumber.setText(String.valueOf(playerNumber));
        }
    }

    public void setPlayerPosition(String playerPosition) {
        this.playerPosition.setText(playerPosition);
    }

    public void handlePlayerClick() throws IOException {
        System.out.println("player clicked");
        Main.showPlayerDetailScene(this.player);
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}
