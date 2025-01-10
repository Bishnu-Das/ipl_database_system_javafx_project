package ComponentController;

import Main.Main;
import database.Club;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ClubListItemController {
    private Club club;
    @FXML
    private Label clubName;
    @FXML
    private ImageView clubImage;

    public void setClubName() {
        this.clubName.setText(club.getName());
    }

    public void setClubImage(String clubName) {
        try {
            String url = "/images/clubimage/" + clubName.trim().toLowerCase() + ".png";
            if (getClass().getResource(url) == null) {
                url = "/images/clubimage/default.png";
            }
            Image image2 = new Image(getClass().getResource(url).toString());
            this.clubImage.setImage(image2);
        } catch (Exception e) {
            System.out.println("Image not loading");
            e.printStackTrace();
        }
    }

    public void setClub(Club club) {
        this.club = club;
    }

    @FXML
    private void handleClubClick() throws Exception {
        System.out.println("player clicked");
        Main.showClubDetailScene(this.club);
    }

}
