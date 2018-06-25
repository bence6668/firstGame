package firstgame.Controllers;

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

    LanguagePack langPack;
    private ResourceBundle resBoundle;
    private Locale locale;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
    }

}
