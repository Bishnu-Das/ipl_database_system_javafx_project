package ComponentController;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;

public class PlayerDetailController {
    @FXML
    private ImageView playerImage;

    @FXML
    private Label nameLabel;

    @FXML
    private Label positionLabel;

    @FXML
    private Label heightLabel;

    @FXML
    private Label jerseyNoLabel;

    @FXML
    private Label ageLabel;

    @FXML
    private Label salaryLabel;

    @FXML
    private ImageView countryImage;

    @FXML
    private Label countryLabel;

    @FXML
    private ImageView clubImageLabel;

    @FXML
    private Label clubNameLabel;

    public void setPlayerImage(String playerName) {
        try {
            String url = "/images/player/" + playerName.trim().toLowerCase() + ".png";
            Image image2 = new Image(getClass().getResource(url).toString());
            this.playerImage.setImage(image2);
        } catch (Exception e) {
            String url = "/images/player/default.png";
            Image image2 = new Image(getClass().getResource(url).toString());
            this.playerImage.setImage(image2);
        }
    }

    public void setPlayerName(String name) {
        this.nameLabel.setText(name);
    }

    public void setPlayerPosition(String position) {
        this.positionLabel.setText(position);
    }

    public void setPlayerHeight(String height) {
        this.heightLabel.setText("Height :" + height);
    }

    public void setPlayerJerseyNumber(String jerseyNumber) {
        this.jerseyNoLabel.setText("Jersey Number: " + jerseyNumber);
    }

    public void setPlayerAge(String age) {
        this.ageLabel.setText("Age: " + age);
    }

    public void setPlayerSalary(String salary) {
        this.salaryLabel.setText("Salary: " + salary);
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

    public void setPlayerCountry(String country) {
        this.countryLabel.setText(country);
    }

    public void setClubImage(String clubName) {

        try {
            String url = "/images/clubimage/" + clubName.trim().toLowerCase() + ".png";
            if (getClass().getResource(url) == null) {
                return;
            }
            Image image2 = new Image(getClass().getResource(url).toString());
            this.clubImageLabel.setImage(image2);
        } catch (Exception e) {
            System.out.println("Image not loading");
            e.printStackTrace();
        }
    }

    public void setPlayerClubName(String clubName) {
        this.clubNameLabel.setText(clubName);
    }
}
