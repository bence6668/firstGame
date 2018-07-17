package firstgame.Controllers;

import firstgame.BaccaraModel;
import firstgame.DatabaseConnection;
import firstgame.LanguagePack;
import firstgame.MainApp;
import firstgame.Player;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class BaccaraController implements Initializable {

    //<editor-fold defaultstate="collapsed" desc="Variables">
    private ArrayList<Image> images;
    private String username;
    private boolean isBetLost;
    private boolean isNewBankerCardNeeded;
    private boolean isNewPlayerCardNeeded;
    private boolean isfirstClick = true;
    private boolean isInfoVisible;
    private int balance;
    private int bet;
    private Locale locale;
    private ResourceBundle resBundle;
    private Player player;
    private DatabaseConnection db;
    private BaccaraModel model;
    private LanguagePack langPack;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="FXML-items">
    @FXML
    private AnchorPane alertPane;
    @FXML
    private Label nameLbl, guthabenLbl;
    @FXML
    private Label alertLbl, infoLbl1, infoLbl2, infoLbl3;
    @FXML
    private Label bankerbetragLbl, playerbetragLbl, tiebetragLbl;
    @FXML
    private Label playerPointsLbl, bankerPointsLbl;
    @FXML
    private TextField betTxtField;
    @FXML
    private ImageView card1_playerImg, card2_playerImg, card3_playerImg;
    @FXML
    private ImageView card1_bankImg, card2_bankImg, card3_bankImg;
    @FXML
    private ImageView bankerChipImg, playerChipImg, tieChipImg;
    @FXML
    private ImageView infoImg1, infoImg2, infoImg3, infoImg4, infoImg5;
    @FXML
    private Button setzenBtn, zieheBtn, newPlayerCardBtn, newBankerCardBtn, newGameBtn, betBankerBtn, betTieBtn, betPlayerBtn;
    @FXML
    private Button zuruck;
    @FXML
    private Label setBetLbl;
    @FXML
    private Label alertLbl2;
    @FXML
    private Label alertLbl3;
    @FXML
    private Button jaButton;
    @FXML
    private Button neinButton;
//</editor-fold>

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = DatabaseConnection.getInstance();
        player = Player.getInstance();
        model = new BaccaraModel();

        username = player.getUsername();
        balance = player.getBalance();
        langPack = LanguagePack.getInstance();
        switch (langPack.getLanguage()) {
            case "ENG":
                loadLanguage("en");
                break;
            case "DEU":
                loadLanguage("de");
                break;
            case "HUN":
                loadLanguage("hu");
                break;
            default:
                loadLanguage("en");
        }

        nameLbl.setText(username);

        if (balance == 0) {
            alertLbl.setText(resBundle.getString("nocredit"));
            betTxtField.setMouseTransparent(true);
            betTxtField.setFocusTraversable(false);
            setzenBtn.setMouseTransparent(true);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="FXML Buttons">
    private void setBet(ActionEvent event) throws IOException {

        if (model.getIsPlayerBet() == false && model.getIsBankerBet() == false && model.getIsTieBet() == false) {
            alertLbl.setText(resBundle.getString("alert1"));
        } else {
            try {
                bet = Integer.parseInt(betTxtField.getText());
                model.setBet(bet);
                if (bet > balance) {
                    alertLbl.setText(resBundle.getString("alert2"));
                } else if (bet < 0) {
                    alertLbl.setText(resBundle.getString("alert3"));
                } else {
                    player.setBalance(balance - bet);
                    guthabenLbl.setText(resBundle.getString("money") + " " + String.valueOf(player.getBalance()));
                    db.updateBalance(player.getBalance(), username);
                    betTxtField.setMouseTransparent(true);
                    setzenBtn.setMouseTransparent(true);
                    displayBet();
                    betBankerBtn.setDisable(true);
                    betTieBtn.setDisable(true);
                    betPlayerBtn.setDisable(true);
                    alertLbl.setText(resBundle.getString("alert4"));
                    isfirstClick = false;

                }
            } catch (NumberFormatException ex) {
                alertLbl.setText(resBundle.getString("alert5"));
            }
        }
    }

    @FXML
    private void newCard(ActionEvent event) throws IOException {
        displaySecondCard(1);
        isNewBankerCardNeeded = model.isnewBankerCard();
        if (isNewBankerCardNeeded == true) {
            alertLbl.setText(resBundle.getString("cardb"));
            newBankerCardBtn.setDisable(false);

        } else {
            finishGame();
        }

    }

    @FXML
    private void drawCard(ActionEvent event) throws IOException {
        if (isfirstClick == true) {
            alertLbl.setText(resBundle.getString("takebet"));
        } else {

            for (int i = 0; i < 4; i++) {
                model.drawCard(i);
            }
            model.calculateCardSum();
            playerPointsLbl.setText(String.valueOf(model.getSumOfPlayerCards()));
            bankerPointsLbl.setText(String.valueOf(model.getSumOfBankerCards()));

            images = model.getImages();
            card1_playerImg.setImage(images.get(0));
            card2_playerImg.setImage(images.get(1));
            card1_bankImg.setImage(images.get(2));
            card2_bankImg.setImage(images.get(3));
            zieheBtn.setDisable(true);
            isNewPlayerCardNeeded = model.isNewPlayerCard();

            if (isNewPlayerCardNeeded == false) {
                if (model.getSumOfBankerCards() > 2) {
                    finishGame();
                } else {
                    alertLbl.setText(resBundle.getString("cardb"));
                    newBankerCardBtn.setDisable(false);
                }

            } else {
                alertLbl.setText(resBundle.getString("cardp"));
                newPlayerCardBtn.setDisable(false);
            }
        }

    }

    @FXML
    private void newBankerCard(ActionEvent event) throws IOException {
        displaySecondCard(2);
        finishGame();
    }

    @FXML
    private void betPlayer(ActionEvent event) throws IOException {
        takeBet(true, false, false);
    }

    @FXML
    private void betTie(ActionEvent event) throws IOException {
        takeBet(false, true, false);
    }

    @FXML
    private void betBanker(ActionEvent event) throws IOException {
        takeBet(false, false, true);
    }

    @FXML
    private void zuruck(ActionEvent event) throws IOException {
        if (bet != 0) {
            alertLbl2.setText(resBundle.getString("alert6"));
            alertLbl3.setText(resBundle.getString("alert7"));
            jaButton.setText(resBundle.getString("apply"));
            neinButton.setText(resBundle.getString("cancel"));
            alertPane.setVisible(true);
        } else {
            zuruckZumStart();
        }
    }

    @FXML
    private void ja(ActionEvent event) throws IOException {
        zuruckZumStart();
    }

    @FXML
    private void nein(ActionEvent event) throws IOException {
        alertPane.setVisible(false);
    }

    @FXML
    private void newGame(ActionEvent event) throws IOException {
        Stage stage = MainApp.getStage();
        Parent root = FXMLLoader.load(getClass().getResource("/firstgame/Views/Baccara.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();;
    }

    @FXML
    private void openInformation(MouseEvent event) {
        if (isInfoVisible == false) {
            switchInfoVisibility(true);
        } else {
            switchInfoVisibility(false);
        }

    }

    private void zuruckZumStart() throws IOException {
        Stage stage = MainApp.getStage();

        Parent root = FXMLLoader.load(getClass().getResource("/firstgame/Views/LoginView.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
//</editor-fold>

    private void setWinnerAlert() {
        switch (model.getWinner()) {
            case 1:
                alertLbl.setText(resBundle.getString("wonp"));
                break;
            case 2:
                alertLbl.setText(resBundle.getString("wont"));
                break;
            case 3:
                alertLbl.setText(resBundle.getString("wonb"));
                break;
            default:
        }
    }

    private void takeBet(boolean player, boolean tie, boolean banker) {
        model.setIsPlayerBet(player);
        model.setIsTieBet(tie);
        model.setIsBankerBet(banker);
        playerChipImg.setVisible(player);
        tieChipImg.setVisible(tie);
        bankerChipImg.setVisible(banker);

    }

    private void updateGuthaben() {
        bet = model.calculateBet();
        if (model.getIsTieBet() == true) {
            bet *= 9;
        }
        player.setBalance(player.getBalance() + bet);
        guthabenLbl.setText(resBundle.getString("money") + " " + String.valueOf(player.getBalance()));
        displayBet();
        db.updateBalance(player.getBalance(), player.getUsername());

    }

    private void finishGame() {
        model.whoIsWinner();
        setWinnerAlert();
        isBetLost = model.winOrLoose();
        if (isBetLost == true) {
            updateGuthaben();
        } else {
            db.updateBalance(player.getBalance(), player.getUsername());
        }

        newGameBtn.setDisable(false);
        newGameBtn.setVisible(true);
    }

    public void switchInfoVisibility(boolean visibility) {
        infoImg1.setVisible(visibility);
        infoImg2.setVisible(visibility);
        infoImg3.setVisible(visibility);
        infoImg4.setVisible(visibility);
        infoImg5.setVisible(visibility);
        infoLbl1.setVisible(visibility);
        infoLbl2.setVisible(visibility);
        infoLbl3.setVisible(visibility);
        isInfoVisible = visibility;
    }

    private void displaySecondCard(int player) {
        model.getImages().clear();
        model.getCardValues().clear();
        model.drawCard(0);
        if (player == 1) {
            model.addPlayerToSum(0);
            playerPointsLbl.setText(String.valueOf(model.getSumOfPlayerCards()));
            images = model.getImages();
            card3_playerImg.setImage(images.get(0));
            newPlayerCardBtn.setDisable(true);
        } else {
            model.addBankerToSum(0);
            bankerPointsLbl.setText(String.valueOf(model.getSumOfBankerCards()));
            images = model.getImages();
            card3_bankImg.setImage(images.get(0));
            newBankerCardBtn.setDisable(true);
        }
    }

    private void displayBet() {
        if (model.getIsBankerBet() == true) {
            bankerbetragLbl.setText(String.valueOf(bet));
            bankerbetragLbl.setVisible(true);
        }
        if (model.getIsPlayerBet() == true) {
            playerbetragLbl.setText(String.valueOf(bet));
            playerbetragLbl.setVisible(true);
        }
        if (model.getIsTieBet() == true) {
            tiebetragLbl.setText(String.valueOf(bet));
            tiebetragLbl.setVisible(true);
        }
    }

    private void loadLanguage(String lang) {
        locale = new Locale(lang);
        resBundle = ResourceBundle.getBundle("resources.properties.language", locale);
        guthabenLbl.setText(resBundle.getString("money") + " " + player.getBalance());
        setBetLbl.setText(resBundle.getString("bet2"));
        setzenBtn.setText(resBundle.getString("bet3"));
        infoLbl1.setText(resBundle.getString("bet1"));
        infoLbl2.setText(resBundle.getString("jeton"));
        infoLbl3.setText(resBundle.getString("card"));
        zuruck.setText(resBundle.getString("logout"));
        newGameBtn.setText(resBundle.getString("new"));
    }

}
