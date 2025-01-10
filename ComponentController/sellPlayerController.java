package ComponentController;

import java.io.IOException;
import java.util.List;

import Main.Main;
import Network.Client;
import database.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class sellPlayerController {
    Main main;
    Client client;

    @FXML
    private VBox plyerList;

    public void loadClubPlayer() {
        List<Player> players = main.myClub.getPlayers();
        plyerList.getChildren().clear();
        for (int i = 0; i < players.size(); i++) {
            Player currPlayer = players.get(i);
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/Components/PlayerListItemMyClubSell.fxml"));

                HBox listItem = fxmlLoader.load();
                PlayerListItemControllerMyClubSell playerListItemControllermyClub = fxmlLoader.getController();
                playerListItemControllermyClub.setMain(main);
                playerListItemControllermyClub.setPlayer(currPlayer);
                playerListItemControllermyClub.setPlayerName(currPlayer.getName());
                playerListItemControllermyClub.setPlayerNumber(currPlayer.getNumber());
                playerListItemControllermyClub.setPlayerPosition(currPlayer.getPosition());
                playerListItemControllermyClub.setCountryImage(currPlayer.getCountry());
                if (main == null) {
                    System.out.println("main is null in sell player controller");
                }

                if (client != null) {
                    playerListItemControllermyClub.setClient(client);
                } else {
                }

                plyerList.getChildren().add(listItem);
            } catch (IOException e) {
                System.out.println("not loading player");
                e.printStackTrace();
            }
        }
    }

    @FXML
    void VboxClickedForDelete(MouseEvent event) {

    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}
