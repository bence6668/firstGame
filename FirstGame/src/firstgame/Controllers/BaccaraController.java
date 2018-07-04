/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firstgame.Controllers;

//<editor-fold defaultstate="collapsed" desc="imports">
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
//</editor-fold>

/**
 * FXML Controller class
 *
 * @author Bence Urszin
 */
public class BaccaraController implements Initializable {

    //<editor-fold defaultstate="collapsed" desc="Variablen">
    private ArrayList<Image> images;
    private String benutzername;
    private boolean isBetLost;
    private boolean isNewBankerCardNeeded;
    private boolean isNewPlayerCardNeeded;
    private boolean isfirstClick = true;
    private boolean isInfoVisible;
    private int guthaben;
    private int betrag;
    private Locale locale;
    private ResourceBundle resBoundle;
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Klassenobjekte">
    private Player player;
    private DatabaseConnection db;
    private BaccaraModel model;
    private LanguagePack langPack;
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="FXML Elemente">
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
//</editor-fold>
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

    /**
     * Beim Start des Spiels wird Benutzername und Kontostand aus der Klasse
     * Player abgeftragt und angezeigt und der Kartendeck aus der KLasse Deck
     * abgefragt.
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = DatabaseConnection.getInstance();
        player = Player.getInstance();
        model = new BaccaraModel();

        benutzername = player.getBenutzername();
        guthaben = player.getGuthaben();
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

        nameLbl.setText(benutzername);

        if (guthaben == 0) {
            alertLbl.setText(resBoundle.getString("nocredit"));
            betTxtField.setMouseTransparent(true);
            betTxtField.setFocusTraversable(false);
            setzenBtn.setMouseTransparent(true);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="FXML Buttons">
    /**
     * Nachdem der Spieler TIE, BANKER oder PLAYER ausgewählt hat, und die
     * Validierung richtig ist, wird das Betrag gesetzt, aus dem Konto abgezogen
     * und die chips werden angezeigt.
     *
     */
    @FXML
    private void betragSetzen(ActionEvent event) throws IOException {

        if (model.getIsPlayerBet() == false && model.getIsBankerBet() == false && model.getIsTieBet() == false) {
            alertLbl.setText(resBoundle.getString("alert1"));
        } else {
            try {
                betrag = Integer.parseInt(betTxtField.getText());
                model.setBetrag(betrag);
                if (betrag > guthaben) {
                    alertLbl.setText(resBoundle.getString("alert2"));
                } else if (betrag < 0) {
                    alertLbl.setText(resBoundle.getString("alert3"));
                } else {
                    player.setGuthaben(guthaben - betrag);
                    guthabenLbl.setText(resBoundle.getString("money") + " " + String.valueOf(player.getGuthaben()));
                    db.updateGuthaben(player.getGuthaben(), benutzername);
                    betTxtField.setMouseTransparent(true);
                    setzenBtn.setMouseTransparent(true);
                    displayBet();
                    betBankerBtn.setDisable(true);
                    betTieBtn.setDisable(true);
                    betPlayerBtn.setDisable(true);
                    alertLbl.setText(resBoundle.getString("alert4"));
                    isfirstClick = false;

                }
            } catch (NumberFormatException ex) {
                alertLbl.setText(resBoundle.getString("alert5"));
            }
        }
    }

    @FXML
    private void newCard(ActionEvent event) throws IOException {
        displaySecondCard(1);
        isNewBankerCardNeeded = model.isnewBankerCard();
        if (isNewBankerCardNeeded == true) {
            alertLbl.setText(resBoundle.getString("cardb"));
            newBankerCardBtn.setDisable(false);

        } else {
            finishGame();
        }

    }

    //Falls ein Betrag schon gesetzt wurde, werden die erste vier Karten gezogen, und ein meldung erscheinen ob 
    @FXML
    private void zieheKarte(ActionEvent event) throws IOException {
        if (isfirstClick == true) {
            alertLbl.setText(resBoundle.getString("takebet"));
        } else {

            for (int i = 0; i < 4; i++) {
                model.zieheKarte(i);
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
                    alertLbl.setText(resBoundle.getString("cardb"));
                    newBankerCardBtn.setDisable(false);
                }

            } else {
                alertLbl.setText(resBoundle.getString("cardp"));
                newPlayerCardBtn.setDisable(false);
            }
        }

    }

    @FXML
    private void newBankerCard(ActionEvent event) throws IOException {
        displaySecondCard(2);
        finishGame();
    }

    //Betrag auf Spieler wird gesetzt
    @FXML
    private void betPlayer(ActionEvent event) throws IOException {
        takeBet(true, false, false);
    }

    //Betrag auf unentschieden wird gesetzt
    @FXML
    private void betTie(ActionEvent event) throws IOException {
        takeBet(false, true, false);
    }

    //Betrag auf Dealer wird gesetzt
    @FXML
    private void betBanker(ActionEvent event) throws IOException {
        takeBet(false, false, true);
    }

    //Falls es keine Betrag gesetzt wurde, wird das Spiel abgebrochen, ansonst wird ein Bestätigungsfenetr angezeigt
    @FXML
    private void zuruck(ActionEvent event) throws IOException {
        if (betrag != 0) {
            alertLbl2.setText(resBoundle.getString("alert6"));
            alertLbl3.setText(resBoundle.getString("alert7"));
            jaButton.setText(resBoundle.getString("apply"));
            neinButton.setText(resBoundle.getString("cancel"));
            alertPane.setVisible(true);
        } else {
            zuruckZumStart();
        }
    }

    //Das Spiel wird abgebrochen 
    @FXML
    private void ja(ActionEvent event) throws IOException {
        zuruckZumStart();
    }

    //Das Bestätigungsfenster wird zugemacht
    @FXML
    private void nein(ActionEvent event) throws IOException {
        alertPane.setVisible(false);
    }

    //Mit dem Button wird das Spiel neu gestartet
    @FXML
    private void newGame(ActionEvent event) throws IOException {
        Stage stage = MainApp.getStage();
        Parent root = FXMLLoader.load(getClass().getResource("/firstgame/Views/Baccara.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();;
    }

    //Das INFO wird geöffnet oder zugemacht
    @FXML
    private void openInformation(MouseEvent event) {
        if (isInfoVisible == false) {
            switchInfoVisibility(true);
        } else {
            switchInfoVisibility(false);
        }

    }

    //Mit dem Button kann der Spieler zurück zur Übersichtseite wechseln
    private void zuruckZumStart() throws IOException {
        Stage stage = MainApp.getStage();

        Parent root = FXMLLoader.load(getClass().getResource("/firstgame/Views/LoginView.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
//</editor-fold>

    //Es wird ausgegeben ob der Spieler oder Dealer gewonnen hat, oder ist der Spielstand unentschieden
    private void setWinnerAlert() {
        switch (model.getWinner()) {
            case 1:
                alertLbl.setText(resBoundle.getString("wonp"));
                break;
            case 2:
                alertLbl.setText(resBoundle.getString("wont"));
                break;
            case 3:
                alertLbl.setText(resBoundle.getString("wonb"));
                break;
            default:
        }
    }

    //Auf dem ausgewälten Feld wird ein Chip erscheinen 
    private void takeBet(boolean player, boolean tie, boolean banker) {
        model.setIsPlayerBet(player);
        model.setIsTieBet(tie);
        model.setIsBankerBet(banker);
        playerChipImg.setVisible(player);
        tieChipImg.setVisible(tie);
        bankerChipImg.setVisible(banker);

    }

    //Die aktuelle Guthaben wird angezeigt und im Datenbank gespeichert
    private void updateGuthaben() {
        betrag = model.calculateBet();
        if (model.getIsTieBet() == true) {
            betrag *= 9;
        }
        player.setGuthaben(player.getGuthaben() + betrag);
        guthabenLbl.setText(resBoundle.getString("money") + " " + String.valueOf(player.getGuthaben()));
        displayBet();
        db.updateGuthaben(player.getGuthaben(), player.getBenutzername());

    }
//    Das Spiel wird beendet und der Spieler kann das Spiel neu starten

    private void finishGame() {
        model.whoIsWinner();
        setWinnerAlert();
        isBetLost = model.winOrLoose();
        if (isBetLost == true) {
            updateGuthaben();
        } else {
            db.updateGuthaben(player.getGuthaben(), player.getBenutzername());
        }

        newGameBtn.setDisable(false);
        newGameBtn.setVisible(true);
    }

    //Das INFO wird als sichtbar oder unsichtbar eingestellt
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

    //Die zweite Karte des Spielers oder Dealers wird angezeigt
    private void displaySecondCard(int player) {
        model.getImages().clear();
        model.getCardValues().clear();
        model.zieheKarte(0);
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

    //Das Betrag wird angezeigt
    private void displayBet() {
        if (model.getIsBankerBet() == true) {
            bankerbetragLbl.setText(String.valueOf(betrag));
            bankerbetragLbl.setVisible(true);
        }
        if (model.getIsPlayerBet() == true) {
            playerbetragLbl.setText(String.valueOf(betrag));
            playerbetragLbl.setVisible(true);
        }
        if (model.getIsTieBet() == true) {
            tiebetragLbl.setText(String.valueOf(betrag));
            tiebetragLbl.setVisible(true);
        }
    }

    private void loadLanguage(String lang) {
        locale = new Locale(lang);
        resBoundle = ResourceBundle.getBundle("resources.properties.language", locale);
        guthabenLbl.setText(resBoundle.getString("money") + " " + player.getGuthaben());
        setBetLbl.setText(resBoundle.getString("bet2"));
        setzenBtn.setText(resBoundle.getString("bet3"));
        infoLbl1.setText(resBoundle.getString("bet1"));
        infoLbl2.setText(resBoundle.getString("jeton"));
        infoLbl3.setText(resBoundle.getString("card"));
        zuruck.setText(resBoundle.getString("logout"));
        newGameBtn.setText(resBoundle.getString("new"));
    }
}
