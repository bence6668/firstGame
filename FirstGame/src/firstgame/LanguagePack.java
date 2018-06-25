package firstgame;

public class LanguagePack {

    private String language;
    private static LanguagePack instance;

    private LanguagePack() {

    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public static LanguagePack getInstance() {
        if (instance == null) {
            instance = new LanguagePack();
        }
        return instance;
    }

}
