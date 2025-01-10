package ComponentController;

import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CountryListItemController {
    @FXML
    private Label countryName;
    @FXML
    private Label noOfPlayer;
    @FXML
    private ImageView countryImage;

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

    public void setCountryName(String playerName) {
        this.countryName.setText(playerName);
    }

    public void setPlayerNo(String playerNo) {
        this.noOfPlayer.setText(playerNo);
    }
}
