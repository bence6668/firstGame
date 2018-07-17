package firstgame;

public class Player {

    //<editor-fold defaultstate="collapsed" desc="Variables">
    private static Player instance;
    private int balance;
    private String username;
//</editor-fold>
    //Constructor
    private Player() {

    }
    
    //<editor-fold defaultstate="collapsed" desc="setter-methods">
    public void setProperties(int balance, String username) {
        this.balance = balance;
        this.username = username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setBalance(int balance) {
        this.balance = balance;
    }
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getter-methods">
    public static Player getInstance() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
    }
    
    public int getBalance() {
        return balance;
    }
    
    public String getUsername() {
        return username;
    }
//</editor-fold>
}
