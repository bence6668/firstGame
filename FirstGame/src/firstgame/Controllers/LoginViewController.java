package firstgame.Controllers;

import firstgame.LanguagePack;
import firstgame.MainApp;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginViewController implements Initializable {

    @FXML
    private TextField txtUname;
    @FXML
    private TextField txtPassw;
    @FXML
    private Label lblName;
    @FXML
    private Label lblPass;
    @FXML
    private Button logBtn;
    @FXML
    private Button regBtn;
    @FXML
    private ComboBox<String> langBox;

    private ResourceBundle resBoundle;
    private Locale locale;
    private final ObservableList<String> languages = FXCollections.observableArrayList("ENG", "HUN", "DEU");
    private final LanguagePack language = LanguagePack.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadLanguage("en");
        langBox.setItems(languages);
        langBox.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {

            switch (langBox.getSelectionModel().getSelectedItem()) {
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
                    throw new AssertionError();
            }
        });
        langBox.setButtonCell(new ListCell() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("-fx-text-fill: derive(-fx-control-inner-background,-0%)");
                } else {
                    setStyle("-fx-text-fill: -fx-text-inner-color");
                    setText(item.toString());
                }
            }

        });
    }

    private void loadLanguage(String lang) {
        locale = new Locale(lang);
        resBoundle = ResourceBundle.getBundle("resources.properties.language", locale);
        lblName.setText(resBoundle.getString("username"));
        lblPass.setText(resBoundle.getString("password"));
        logBtn.setText(resBoundle.getString("log"));
        regBtn.setText(resBoundle.getString("reg"));
    }

    @FXML
    private void openRegWindow(ActionEvent event) throws IOException {

        if (langBox.getSelectionModel().getSelectedItem() != null) {
            language.setLanguage(langBox.getSelectionModel().getSelectedItem());
        } else {
            language.setLanguage("en");
        }
        Stage stage = MainApp.getStage();
        Parent root = FXMLLoader.load(getClass().getResource("/firstgame/Views/RegisterView.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void login(ActionEvent event) throws IOException {

    }

}
