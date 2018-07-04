/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firstgame;

import java.util.ArrayList;
import javafx.scene.image.Image;

/**
 *
 * @author Bence
 */
public class BaccaraModel {

    //<editor-fold defaultstate="collapsed" desc="Klassenobjekte">
    private Deck deckClass;
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variablen">
    private ArrayList<Card> deck;
    private ArrayList<Image> images = new ArrayList<>();
    private ArrayList<Integer> cardValues = new ArrayList<>();
    private int sumOfPlayerCards;
    private int sumOfBankerCards;
    private int thirdPlayerCard;
    private int betrag = 0;
    private int winner;
    private boolean isPlayerBet;
    private boolean isBankerBet;
    private boolean isTieBet;
//</editor-fold>

    public BaccaraModel() {
        deckClass = new Deck();
        deck = deckClass.getDeck();
    }

    //Die Karten werden gezogen und als Bilder ersetzt und ihre Wert wird gleichzeitig berechnet
    public void zieheKarte(int i) {
        images.add(new Image(getClass().getResourceAsStream("/resources/images/" + deck.get(i).getName() + deck.get(i).getType() + ".png")));
        cardValues.add(deck.get(i).getValue());
        cardValues.set(i, calculaateCardValue(cardValues.get(i)));
        deck.remove(i);
    }

    //Hier wird überprüft ob der BANKER eine neue Karte ziehen muss
    public boolean isnewBankerCard() {
        if (sumOfBankerCards < 3) {
            return true;
        } else if (sumOfPlayerCards == 3 && thirdPlayerCard == 8) {
            return false;
        } else if (sumOfBankerCards == 4 && thirdPlayerCard < 8 && thirdPlayerCard > 2) {
            return true;
        } else if (sumOfBankerCards == 5 && thirdPlayerCard < 8 && thirdPlayerCard > 4) {
            return true;
        } else if (sumOfPlayerCards == 6 && thirdPlayerCard < 8 && thirdPlayerCard > 5) {
            return true;
        } else if (sumOfBankerCards > 6) {
            return false;
        } else {
            return false;
        }
    }

    //HIer wird überprüft ob der Spieler eine neue Karte ziehen muss
    public boolean isNewPlayerCard() {
        if (getSumOfPlayerCards() < 6) {
            return true;
        } else {
            return false;
        }
    }

    //Der Wert der Karten wird berechnet
    private int calculaateCardValue(int value) {
        int newValue = 0;

        if (value > 9) {
            newValue = 0;
        } else {
            newValue = value;
        }
        return newValue;
    }

    //Die Summe der Karten wird berechnet
    public void calculateCardSum() {
        sumOfPlayerCards += cardValues.get(0) + cardValues.get(1);
        sumOfBankerCards += cardValues.get(2) + cardValues.get(3);
        if (sumOfPlayerCards > 9) {
            sumOfPlayerCards -= 10;
        }
        if (sumOfBankerCards > 9) {
            sumOfBankerCards -= 10;
        }

    }

    //Die dritte Karte des Dealers wird zu der Summe der Karten addiert
    public void addBankerToSum(int i) {
        sumOfBankerCards += cardValues.get(i);
        if (sumOfBankerCards > 9) {
            sumOfBankerCards -= 10;
        }
    }

    //Die dritte Karte des Spielers wird zu der Summe der Karten addiert
    public void addPlayerToSum(int i) {
        thirdPlayerCard = cardValues.get(i);
        sumOfPlayerCards += thirdPlayerCard;
        if (sumOfPlayerCards > 9) {
            sumOfPlayerCards -= 10;
        }
    }

    //Bei verschiedener Fälle wird das Betrag berechnet 
    public int calculateBet() {

        if (sumOfPlayerCards == 9) {
            betrag *= 1.5;
        } else {
            betrag *= 2;
        }
        return betrag;
    }

    //Hier wird überprüft ob der PLAYER, BANKER gewonnen hat, oder ist das Spiel UNENTSCHIEDEN
    public void whoIsWinner() {
        if (sumOfPlayerCards > sumOfBankerCards) {
            winner = 1;
        } else if (sumOfPlayerCards == sumOfBankerCards) {
            winner = 2;
        } else {
            winner = 3;
        }
    }

    //Hier wird überprüft, ob der Spielerbetrag auf dem Feld gesetzt wurde, der gewonnen hat 
    public boolean winOrLoose() {
        if (winner == 1 && isPlayerBet == true) {
            return true;
        } else if (winner == 2 && isTieBet == true) {
            return true;
        } else if (winner == 3 && isBankerBet == true) {
            return true;
        } else {
            return false;
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Setter-Methoden">
    public void setBetrag(int betrag) {
        this.betrag = betrag;
    }

    public void setIsPlayerBet(boolean isPlayerBet) {
        this.isPlayerBet = isPlayerBet;
    }

    public void setIsBankerBet(boolean isBankerBet) {
        this.isBankerBet = isBankerBet;
    }

    public void setIsTieBet(boolean isTieBet) {
        this.isTieBet = isTieBet;
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Getter-Methoden">

    public ArrayList<Integer> getCardValues() {
        return cardValues;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public int getSumOfPlayerCards() {
        return sumOfPlayerCards;
    }

    public int getSumOfBankerCards() {
        return sumOfBankerCards;
    }

    public boolean getIsPlayerBet() {
        return isPlayerBet;
    }

    public boolean getIsBankerBet() {
        return isBankerBet;
    }

    public boolean getIsTieBet() {
        return isTieBet;
    }

    public int getWinner() {
        return winner;
    }

    public int getBetrag() {
        return betrag;
    }

//</editor-fold>   
}
