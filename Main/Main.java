package Main;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ComponentController.ClubDetailController;
import ComponentController.PlayerDetailController;
import ComponentController.sellPlayerController;
import Network.Client;
import ComponentController.buyPlayerController;
import database.Club;
import database.Player;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    //all the best
    private Stage mainStage;
    private static Stage PlayerDetail;
    private static Stage ClubDetail;
    private static Stage BuyPlayer;
    private static Stage SellPlayer;
    public Map<String, Scene> sceneCache = new HashMap<>();
    public Map<String, Object> controllerCache = new HashMap<>();

    public List<Player> allPlayers;
    public Client client;
    public Club myClub;
    public List<Club> allClubs;
    public List<Player> buyablePlayer;

    // all controller here
    private sellPlayerController sellPlayerController;
    private myClubController myClubController;
    private buyPlayerController buyPlayerController;
    private PlayerController playerController;
    private clubController clubController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.mainStage = primaryStage;
        showLoginScene();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void showLoginScene() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();
        loginController controller = loader.getController();
        controller.setMain(this);
        mainStage.setTitle("Main Screen");
        mainStage.setScene(new Scene(root, 1000, 600));
        mainStage.setResizable(false);
        mainStage.show();
    }

    public void showChangePasswordScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("changePassword.fxml"));
        Parent root = loader.load();
        changePasswordController controller = loader.getController();
        controller.setMain(this);
        controller.setClub(myClub);
        mainStage.setTitle("Change Password Screen");
        mainStage.setScene(new Scene(root, 1000, 600));
        mainStage.setResizable(false);
        mainStage.show();
    }

    public void showPlayerScene() throws Exception {
        if (!sceneCache.containsKey("player")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("player.fxml"));
            Parent root = loader.load();
            PlayerController controller = loader.getController();
            if(myClub==null){
                Platform.runLater(()->controller.setInvisibleMyClubAndAddPlayer());
            }
            controller.setMain(this);
            controller.loadPlayer();
            Scene scene = new Scene(root, 1000, 600);
            sceneCache.put("player", scene);
            controllerCache.put("player", controller);
        }
        mainStage.setScene(sceneCache.get("player"));
        mainStage.show();
    }

    public void showClubScene() throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("club.fxml"));
        Parent root = loader.load();
        clubController controller = loader.getController();
        controller.setMain(this);
        if(myClub==null){
            Platform.runLater(()->controller.setInvisibleMyClubAndAddPlayer());
        }

        controller.loadClub();
        Scene scene = new Scene(root, 1000, 600);

        mainStage.setScene(scene);
        mainStage.show();

    }

    public void showHomeScreenScene() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("home.fxml"));
        Parent root = loader.load();
        homeController controller = loader.getController();
        if(myClub==null){
            Platform.runLater(()->controller.setInvisibleMyClubAndAddPlayer());
        }
        controller.setMain(this);
        mainStage.setScene(new Scene(root, 1000, 600));
        mainStage.show();
    }

    public void showAddPlayerScene() throws Exception {
        if (!sceneCache.containsKey("addPlayer")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addplayer.fxml"));
            Parent root = loader.load();
            addPlayerController controller = loader.getController();
            controller.setMain(this);
            Scene scene = new Scene(root, 1000, 600);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            sceneCache.put("addPlayer", scene);
            controllerCache.put("addPlayer", controller);
        }

        mainStage.setScene(sceneCache.get("addPlayer"));
        mainStage.show();
    }

    public void showMyClubScene() throws IOException {
        if (!sceneCache.containsKey("myClub")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("myClub.fxml"));
            Parent root = loader.load();
            myClubController controller = loader.getController();
            controller.setMain(this);
            controller.setClubName(myClub.getName());
            controller.loadClubPlayer();
            this.myClubController = controller;
            Scene scene = new Scene(root, 1000, 600);
            mainStage.setScene(scene);
            mainStage.show();
        } else {
            this.myClubController = (myClubController) controllerCache.get("myClub");
        }
    }

    public myClubController getMyClubController() {
        return myClubController;
    }

    public static void showPlayerDetailScene(Player player) throws IOException {
        PlayerDetail = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/Components/PlayerDetail.fxml"));
        Parent root = loader.load();
        PlayerDetailController controller = loader.getController();
        
        controller.setPlayerName(player.getName());
        controller.setPlayerPosition(player.getPosition());
        controller.setPlayerHeight(String.valueOf(player.getHeight()));
        controller.setPlayerJerseyNumber(String.valueOf(player.getNumber()));
        controller.setPlayerAge(String.valueOf(player.getAge()));
        controller.setPlayerSalary(String.format("$%,.2f", player.getWeeklySalary()));
        controller.setPlayerCountry(player.getCountry());
        controller.setClubImage(player.getClub().toLowerCase());
        controller.setPlayerClubName(player.getClub());
        controller.setCountryImage(player.getCountry().toLowerCase());
        controller.setPlayerImage(player.getName());
        PlayerDetail.setScene(new Scene(root));
        PlayerDetail.show();

    }

    public static void showClubDetailScene(Club club) throws IOException {
        ClubDetail = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/Components/ClubDetail.fxml"));
        Parent root = loader.load();
        ClubDetailController controller = loader.getController();
        controller.setClubName(club.getName());
        controller.setClubImage(club.getName());
        controller.setTotalAmount(String.valueOf((club.getTotalSalary() / 1000000)) + " M");
        controller.setClub(club);
        controller.loadClubPlayer();
        ClubDetail.setScene(new Scene(root));
        ClubDetail.show();
    }

    public void showSellPlayerScene() throws IOException {
        SellPlayer = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/Components/sellPlayer.fxml"));
        Parent root = loader.load();
        sellPlayerController controller = loader.getController();
        controller.setClient(client);
        controller.setMain(this);
        this.sellPlayerController = controller;
        controller.loadClubPlayer();
        if (client != null) {
            System.out.println("client is not null in main..");
            controller.setClient(client);
            System.out.println("client set in sellplayer controller..");
        } else {
            System.out.println("client is null in main....");
        }
        this.sellPlayerController = controller;
        SellPlayer.setScene(new Scene(root));
        SellPlayer.show();
    }

    public void showBuyPlayerScene() throws IOException {
        BuyPlayer = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/Components/buyPlayer.fxml"));
        Parent root = loader.load();
        buyPlayerController controller = loader.getController();
        controller.setClient(client);
        controller.setMain(this);
        this.buyPlayerController = controller;
        controller.loadClubPlayer();
        if (client != null) {
            System.out.println("client is not null in main");
            controller.setClient(client);
            System.out.println("client set in buy player controller");
        } else {
            System.out.println("client is null in main...");
        }
        this.buyPlayerController = controller;
        BuyPlayer.setScene(new Scene(root));
        BuyPlayer.show();
    }

    public sellPlayerController getSellPlayerController() {
        return sellPlayerController;
    }

    public buyPlayerController getBuyPlayerController() {
        return buyPlayerController;
    }

    public void setClient(Client client2) throws IOException {
        this.client = client2;
    }

    public void setAllPlayerList(List<Player> players) {
        this.allPlayers = players;
    }

    public void setMyClub(Club club2) throws IOException {
        this.myClub = club2;
    }

    public List<Player> getAllPlayers() {
        return allPlayers;
    }

    public List<Club> getAllClubs() {
        return allClubs;
    }

    public Club getMyClub() {
        return myClub;
    }

    public void updateAllItem() {
        Platform.runLater(() -> {
            if (playerController != null) {
                playerController.loadPlayer();
            }
            if (sellPlayerController != null) {
                sellPlayerController.loadClubPlayer();
            }
            if (buyPlayerController != null) {
                buyPlayerController.loadClubPlayer();
            }
            if (clubController != null) {
                clubController.loadClub();
            }
            if (myClubController != null) {
                myClubController.loadClubPlayer();
            }
        });
    }

}