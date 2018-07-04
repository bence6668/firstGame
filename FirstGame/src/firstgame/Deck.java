/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firstgame;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Bence
 */
public class Deck {

    //<editor-fold defaultstate="collapsed" desc="Variablen">
    private final String TYPE[] = {"H", "D", "S", "C"};
    private final String VALUE[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"};
    private ArrayList<Card> deck = new ArrayList<>();
//</editor-fold>
    //Der Deck wird beim instanzierung erzeugt und gemischt

    public Deck() {
        createCardList();
        Collections.shuffle(deck);
    }

    //Der Deck wird aus 52 Karten erzeugt
    private void createCardList() {
        for (int i = 0; i < TYPE.length; i++) {
            for (int j = 0; j < VALUE.length; j++) {
                deck.add(new Card(j + 1, VALUE[j], TYPE[i]));
            }
        }
    }

    //Getter-Methode
    public ArrayList<Card> getDeck() {
        return deck;
    }
}
