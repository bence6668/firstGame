package firstgame;

public class Card {

    //<editor-fold defaultstate="collapsed" desc="Variables">
    private String name;
    private String type;
    private int value;
//</editor-fold>

    public Card(int value, String name, String type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    //<editor-fold defaultstate="collapsed" desc="getter-methods">
    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public String getType() {
        return type;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setter-methods">
    public void setName(String name) {
        this.name = name;
    }

    public void setValue(int value) {
        this.value = value;
    }

//</editor-fold>
}
