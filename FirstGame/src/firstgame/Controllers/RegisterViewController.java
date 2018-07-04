package firstgame.Controllers;

import firstgame.DatabaseConnection;
import firstgame.LanguagePack;
import firstgame.MainApp;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class RegisterViewController implements Initializable {

    @FXML
    private TextField txtUname;
    @FXML
    private TextField txtPass1;
    @FXML
    private Label lblUname;
    @FXML
    private Label lblPass;
    @FXML
    private Button logBtn;
    @FXML
    private Button regBtn;
    @FXML
    private Label lblAccount;
    @FXML
    private TextField txtName2;
    @FXML
    private TextField txtName1;
    @FXML
    private TextField txtMail;
    @FXML
    private Label lblName1;
    @FXML
    private Label lblName2;
    @FXML
    private Label lblName211;
    @FXML
    private TextField txtPass2;
    @FXML
    private Label lblPass2;
    @FXML
    private Label lblAlert;

    private LanguagePack langPack;
    private ResourceBundle resBoundle;
    private Locale locale;
    private DatabaseConnection db;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        langPack = LanguagePack.getInstance();
        db = DatabaseConnection.getInstance();
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

    }

    @FXML
    private void openLogWindow(ActionEvent event) throws IOException {
        Stage stage = MainApp.getStage();
        Parent root = FXMLLoader.load(getClass().getResource("/firstgame/Views/LoginView.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    private void loadLanguage(String lang) {
        locale = new Locale(lang);
        resBoundle = ResourceBundle.getBundle("resources.properties.language", locale);
        lblAccount.setText(resBoundle.getString("account"));
        lblName1.setText(resBoundle.getString("name1"));
        lblName2.setText(resBoundle.getString("name2"));
        lblUname.setText(resBoundle.getString("username"));
        lblPass.setText(resBoundle.getString("password"));
        lblPass2.setText(resBoundle.getString("password2"));
        regBtn.setText(resBoundle.getString("reg"));
        logBtn.setText(resBoundle.getString("log"));

    }

    @FXML
    private void register(ActionEvent event) {
        if (txtName1.getText().equals("") || txtName2.getText().equals("") || txtMail.getText().equals("") || txtUname.getText().equals("") || txtPass1.getText().equals("")) {
            lblAlert.setText(resBoundle.getString("fieldcheck"));
        } else if (!txtPass1.getText().equals(txtPass2.getText())) {
            lblAlert.setText(resBoundle.getString("passmatch"));
        } else {
            db.addUser(txtName1.getText(), txtName2.getText(), txtMail.getText(), txtUname.getText(), txtPass1.getText());
            lblAlert.setText(resBoundle.getString("successfull"));
            freezeWindow();
        }

    }
    
    private void freezeWindow(){
        txtName1.setMouseTransparent(true);
        txtName2.setMouseTransparent(true);
        txtPass1.setMouseTransparent(true);
        txtPass2.setMouseTransparent(true);
        txtMail.setMouseTransparent(true);
        txtUname.setMouseTransparent(true);
        regBtn.setMouseTransparent(true);
    }

    @FXML
    private void checkStrength(MouseEvent event) {
        if (txtPass1.getText().equals("")) {

        } else {
            if (txtPass1.getLength() < 8) {
                lblAlert.setText(resBoundle.getString("strength"));
            } else {
                lblAlert.setText("");
            }
        }

    }

    @FXML
    private void checkEmail(MouseEvent event) {
        if (txtMail.getText().equals("")) {

        } else {
            if (!txtMail.getText().contains("@") || !txtMail.getText().contains(".")) {
                lblAlert.setText(resBoundle.getString("mailcheck"));
            } else {
                lblAlert.setText("");
            }
        }

    }

}
