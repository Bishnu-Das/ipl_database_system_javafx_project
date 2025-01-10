package ComponentController;

import java.io.IOException;
import java.util.Optional;

import Main.Main;
import Main.myClubController;
import Network.Client;
import database.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PlayerListItemControllerMyClubBuy {
    Main main;
    Client client;

    private Player player;

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

    public void setPlayer(Player player) throws IOException {
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

        System.out.println("Player clicked");
        boolean isConfirmed = showPrompt("Are you sure you want to buy "+player.getName()+" ?");
        if (!isConfirmed) {
            System.out.println("Player purchase canceled by the user.");
            return;
        }
        this.player.setClub(main.myClub.getName());
        main.myClub.addPlayer(player);

        if (main.client != null) {
            System.out.println("client is not null trying to send player data to server...");
            main.buyablePlayer.remove(player);
            main.client.RemoveFromBuyableList(player, main.myClub.getName());
        }

        if (main != null) {
            main.sceneCache.remove("player");
            main.controllerCache.remove("player");
            main.sceneCache.remove("club");
            main.controllerCache.remove("club");
            myClubController clubController = main.getMyClubController();
            if (clubController != null) {
                clubController.loadClubPlayer();
            } else {
            }
            buyPlayerController buyPlayerController = main.getBuyPlayerController();
            if (buyPlayerController != null) {
                buyPlayerController.loadClubPlayer();
            } else {
            }

        } else {

        }
    }

    public boolean showPrompt(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setClient(Client client2) {
        this.client = client2;
    }
}
