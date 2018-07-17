package firstgame;

public class LanguagePack {

    //Variables
    private String language;
    private static LanguagePack instance;

    //Constructor
    private LanguagePack() {

    }

//<editor-fold defaultstate="collapsed" desc="getter-methods">
    public String getLanguage() {
        return language;
    }

    public static LanguagePack getInstance() {
        if (instance == null) {
            instance = new LanguagePack();
        }
        return instance;
    }
//</editor-fold>
    //setter-method

    public void setLanguage(String language) {
        this.language = language;
    }
}
