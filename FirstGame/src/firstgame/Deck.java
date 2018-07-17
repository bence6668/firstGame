package firstgame;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {

    //<editor-fold defaultstate="collapsed" desc="Variables">
    private final String TYPE[] = {"H", "D", "S", "C"};
    private final String VALUE[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"};
    private ArrayList<Card> deck = new ArrayList<>();
//</editor-fold>
    //The deck will be created and mixed
    public Deck() {
        createCardList();
        Collections.shuffle(deck);
    }
    //The 52 cards will be added to the deck
    private void createCardList() {
        for (int i = 0; i < TYPE.length; i++) {
            for (int j = 0; j < VALUE.length; j++) {
                deck.add(new Card(j + 1, VALUE[j], TYPE[i]));
            }
        }
    }
    //getter-methods
    public ArrayList<Card> getDeck() {
        return deck;
    }
}
