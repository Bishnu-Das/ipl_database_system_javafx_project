package Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ComponentController.CountryListItemController;
import ComponentController.PlayerListItemController;
import database.Player;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PlayerController {

    private Main main;
    List<Player> players;

    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    private VBox playerList;

    @FXML
    private HBox searchByNameBar;
    @FXML
    private HBox searchByClubBar;
    @FXML
    private HBox searchByPositionBar;
    @FXML
    private HBox searchBySalaryBar;

    @FXML
    private TextField searchByNameText;
    @FXML
    private TextField searchByPositionText;
    @FXML
    private TextField searchByCountryClubCountryText;
    @FXML
    private TextField searchByCountryClubClubText;

    @FXML
    private TextField searchBySalaryMinValText;
    @FXML
    private TextField searchBySalaryMaxValText;

    @FXML
    private void hideSearchBar() {
        searchByNameBar.setVisible(false);
        searchByClubBar.setVisible(false);
        searchByPositionBar.setVisible(false);
        searchBySalaryBar.setVisible(false);
    }

    public void playerButtonClicked(ActionEvent actionEvent) throws Exception {
        if (main != null) {
            main.showPlayerScene();
        }
        hideSearchBar();
    }

    @FXML
    private void clubsButtonClicked(ActionEvent event) throws Exception {
        if (main != null) {
            main.showClubScene();
        }
        hideSearchBar();
    }

    @FXML
    private void addPlayerButtonClicked(ActionEvent event) throws Exception {
        if (main != null) {
            main.showAddPlayerScene();
        }
        hideSearchBar();
    }

    @FXML
    private void homeButtonClicked(ActionEvent event) throws Exception {
        if (main != null) {
            main.showHomeScreenScene();
        }
        hideSearchBar();
    }

    @FXML
    public void myClubButtonClicked(ActionEvent event) throws IOException {
        if (main != null) {
            main.showMyClubScene();
        } else {
        }
    }

    // search by name

    @FXML
    private void nameButtonClicked(ActionEvent event) throws Exception {
        clearSearchBarText();
        hideSearchBar();
        searchByNameBar.setVisible(true);
    }

    @FXML
    private void searchButtonClickedName(ActionEvent event) throws Exception {
        searchPlayers(searchByNameText.getText());
    }

    // search by club and country
    @FXML
    private void clubButtonClickedSearch(ActionEvent event) throws Exception {
        clearSearchBarText();
        hideSearchBar();
        searchByClubBar.setVisible(true);
    }

    @FXML
    private void searchButtonClickedClub(ActionEvent event) throws Exception {
        searchClub(searchByCountryClubCountryText.getText(), searchByCountryClubClubText.getText());
    }

    // search by position
    @FXML
    private void positionButtonClickedSearchSideBar(ActionEvent event) throws Exception {
        clearSearchBarText();
        hideSearchBar();
        searchByPositionBar.setVisible(true);
    }

    @FXML
    private void searchButtonClickedPosition(ActionEvent event) throws Exception {
        searchPosition2(searchByPositionText.getText());
    }

    // search by salary
    @FXML
    private void salaryRangeButtonClicked(ActionEvent action) throws Exception {
        clearSearchBarText();
        hideSearchBar();
        searchBySalaryBar.setVisible(true);
    }

    @FXML
    private void searchButtonClickedSalary(ActionEvent action) throws Exception {
        searchSlaryRange(searchBySalaryMinValText.getText(), searchBySalaryMaxValText.getText());
    }

    // countrywise count
    @FXML
    private void CountryWisePlayerCount(ActionEvent action) throws Exception {
        clearSearchBarText();
        hideSearchBar();
        countryWisePlayerCount();
    }

    // all players button clicked
    @FXML
    void AllPlayersClicked(ActionEvent event) {
        loadPlayer();
    }

    private void clearSearchBarText() {
        searchByNameText.clear();
        searchByPositionText.clear();
        searchByCountryClubCountryText.clear();
        searchByCountryClubClubText.clear();
        searchBySalaryMinValText.clear();
        searchBySalaryMaxValText.clear();
    }

    public void searchPlayers(String playerName) throws IOException {
        playerList.getChildren().clear();
        Player currentPlayer = main.client.sendSearchByNameRequest(playerName);
        List<Player> players = new ArrayList<>();
        players.add(currentPlayer);

        if (currentPlayer == null) {
        } else {
            showPlayerInVbox(players, playerList);
        }
    }

    public void searchClub(String Country, String Club) throws IOException {
        playerList.getChildren().clear();
        List<Player> players;
        players = main.client.sendSearchByClubAndCountryRequest(Club, Country);
        showPlayerInVbox(players, playerList);
    }

    public void searchPosition2(String position) throws IOException {

        playerList.getChildren().clear();
        List<Player> players;
        players = main.client.sendSearchByPositionRequest(position);
       showPlayerInVbox(players, playerList);
    }

    public void searchSlaryRange(String minSalary, String maxSalary) throws NumberFormatException, IOException {
        playerList.getChildren().clear();
        List<Player> players;
        players = main.client.sendSearchBySalaryRequest(Integer.parseInt(minSalary), Integer.parseInt(maxSalary));

        showPlayerInVbox(players, playerList);
    }

    public void countryWisePlayerCount() throws IOException {
        playerList.getChildren().clear();
        HashMap<String, Integer> countries;
        countries = main.client.sendCountryWiseCountRequest();

        for (Map.Entry<String, Integer> entry : countries.entrySet()) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/Components/CountryListItem.fxml"));
            HBox listItem = fxmlLoader.load();
            CountryListItemController countryListItemController = fxmlLoader.getController();
            countryListItemController.setCountryName(entry.getKey());
            countryListItemController.setPlayerNo((entry.getValue().toString()));
            countryListItemController.setCountryImage(entry.getKey().toString());
            playerList.getChildren().add(listItem);
        }
    }

    public void loadPlayer() {
        playerList.getChildren().clear();
        players = main.getAllPlayers();
        if (players == null) {
            System.out.println("players null in main...");
            return;
        }
        showPlayerInVbox(players, playerList);
    }
    
    @FXML
    private Button addPlayerButton;

    @FXML
    private Button myClubButton;
     @FXML
    private ImageView myClubImage;
    @FXML
    private ImageView addPlayerImage;

    void setInisibleMyClubAndAddPlayer(){
        addPlayerButton.setVisible(false);
        myClubButton.setVisible(false);
        myClubImage.setVisible(false);
        addPlayerImage.setVisible(false);
    }

    void showPlayerInVbox(List<Player> players, VBox playerList){
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
                // ("not loading player");
                e.printStackTrace();
            }
        }
    }

}
