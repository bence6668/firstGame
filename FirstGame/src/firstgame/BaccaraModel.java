package firstgame;

import java.util.ArrayList;
import javafx.scene.image.Image;

public class BaccaraModel {

    //<editor-fold defaultstate="collapsed" desc="Variablen">
    private ArrayList<Card> deck;
    private ArrayList<Image> images = new ArrayList<>();
    private ArrayList<Integer> cardValues = new ArrayList<>();
    private int sumOfPlayerCards;
    private int sumOfBankerCards;
    private int thirdPlayerCard;
    private int bet = 0;
    private int winner;
    private boolean isPlayerBet;
    private boolean isBankerBet;
    private boolean isTieBet;
    private Deck deckClass;
//</editor-fold>
    //Contructor

    public BaccaraModel() {
        deckClass = new Deck();
        deck = deckClass.getDeck();
    }

    //The cards will be drawn, they will be with images replaced, and the value of the card will be calculated
    public void drawCard(int i) {
        images.add(new Image(getClass().getResourceAsStream("/resources/images/" + deck.get(i).getName() + deck.get(i).getType() + ".png")));
        cardValues.add(deck.get(i).getValue());
        cardValues.set(i, calculaateCardValue(cardValues.get(i)));
        deck.remove(i);
    }

    //Check if the BANKER needs a new card or not
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
    //Check if the PLAYER needs a new card or not

    public boolean isNewPlayerCard() {
        if (getSumOfPlayerCards() < 6) {
            return true;
        } else {
            return false;
        }
    }

    //The value of the card will be calculated 
    private int calculaateCardValue(int value) {
        int newValue = 0;

        if (value > 9) {
            newValue = 0;
        } else {
            newValue = value;
        }
        return newValue;
    }

    //The sum of the cards will be calculated 
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

    //The third card's value of the BANKER will be added to the sum 
    public void addBankerToSum(int i) {
        sumOfBankerCards += cardValues.get(i);
        if (sumOfBankerCards > 9) {
            sumOfBankerCards -= 10;
        }
    }

    //The third card's value of the PLAYER will be added to the sum 
    public void addPlayerToSum(int i) {
        thirdPlayerCard = cardValues.get(i);
        sumOfPlayerCards += thirdPlayerCard;
        if (sumOfPlayerCards > 9) {
            sumOfPlayerCards -= 10;
        }
    }

    public int calculateBet() {

        if (sumOfPlayerCards == 9) {
            bet *= 1.5;
        } else {
            bet *= 2;
        }
        return bet;
    }

    public void whoIsWinner() {
        if (sumOfPlayerCards > sumOfBankerCards) {
            winner = 1;
        } else if (sumOfPlayerCards == sumOfBankerCards) {
            winner = 2;
        } else {
            winner = 3;
        }
    }

    //Check if the bet was placed to the winner field or not
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

    //<editor-fold defaultstate="collapsed" desc="getter-methods">
    public void setBet(int bet) {
        this.bet = bet;
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
    //<editor-fold defaultstate="collapsed" desc="setter-methods">

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

    public int getBet() {
        return bet;
    }

//</editor-fold>   
}
