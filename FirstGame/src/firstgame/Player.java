/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firstgame;

/**
 *
 * @author Laouand Baro
 */
public class Player {

    //Variablen
    private static Player instance;
    private int guthaben;
    private String benutzername;

    //Kontruktor der Klasse Player
    private Player() {

    }

    public static Player getInstance() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
    }

    public void setProperties(int guthaben, String benutzername) {

        this.guthaben = guthaben;
        this.benutzername = benutzername;
    }

    public void setBenutzername(String benutzername) {
        this.benutzername = benutzername;
    }
    
    

    public int getGuthaben() {
        return guthaben;
    }

    public void setGuthaben(int guthaben) {
        this.guthaben = guthaben;
    }

    public String getBenutzername() {
        return benutzername;
    }
}
