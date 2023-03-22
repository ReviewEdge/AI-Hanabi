import java.awt.*;
import java.util.HashMap;

public class Slot {
    private Integer numHint;
    private Integer colorHint;
    private HashMap<KBCard, Integer> slotPotentialCards;
    private PartialHand parent;

    private Integer certainColor;
    private Integer certainValue;
    private KBCard certainCard;


    public Slot(PartialHand parent, HashMap<KBCard, Integer> slotPotentialCards) {
        this.parent = parent;
        numHint = null;
        colorHint = null;
        this.slotPotentialCards = slotPotentialCards;

        certainColor = null;
        certainValue = null;
        certainCard = null;

    }


    public void calcBothCertainty() {
        checkForCertainValue();
        checkForCertainColor();
    }


    public Integer checkForCertainColor() {
        Integer checkingCertain = null;

        for (HashMap.Entry<KBCard, Integer> cardType : slotPotentialCards.entrySet()) {
            if (checkingCertain == null) {
                if (cardType.getValue() > 0) {
                    checkingCertain = Integer.valueOf(cardType.getKey().color);
                }
            }

            else if ( (cardType.getValue() > 0) && (!Integer.valueOf(cardType.getKey().color).equals(checkingCertain)) ) {
                return null;
            }
        }

        this.certainColor = checkingCertain;

        if ((certainColor != null) && (certainValue != null)) {
            certainCard = new KBCard(certainColor, certainValue);
        }

        return checkingCertain;
    }


    public Integer checkForCertainValue() {
        Integer checkingCertain = null;

        for (HashMap.Entry<KBCard, Integer> cardType : slotPotentialCards.entrySet()) {
            if (checkingCertain == null) {
                if (cardType.getValue() > 0) {
                    checkingCertain = Integer.valueOf(cardType.getKey().value);
                }
            }

            else if ( (cardType.getValue() > 0) && (!Integer.valueOf(cardType.getKey().value).equals(checkingCertain)) ) {
                return null;
            }
        }

        this.certainValue = checkingCertain;

        if ((certainColor != null) && (certainValue != null)) {
            certainCard = new KBCard(certainColor, certainValue);
        }

        return checkingCertain;
    }

    public Integer getCertainColor() {
        return certainColor;
    }

    public Integer getCertainValue() {
        return certainValue;
    }

    public KBCard getCertainCard() {
        return certainCard;
    }

    public HashMap<KBCard, Integer> getSlotPotentialCards() {
        return slotPotentialCards;
    }

    public void setNumHint(int hint) {
        numHint = Integer.valueOf(hint);

        certainValue = numHint;

        if ((certainColor != null) && (certainValue != null)) {
            certainCard = new KBCard(certainColor, certainValue);
        }

        // remove cards from pos not matching hint
        for (KBCard card : slotPotentialCards.keySet()) {
            if (card.value != numHint.intValue()) {
                // remove any occurrences of card with wrong value
                slotPotentialCards.put(card, 0);
            }
        }

    }

    public void setColorHint(int hint) {
        colorHint = Integer.valueOf(hint);

        certainColor = colorHint;

        if ((certainColor != null) && (certainValue != null)) {
            certainCard = new KBCard(certainColor, certainValue);
        }

        // remove cards from possible, not matching hint
        for (KBCard card : slotPotentialCards.keySet()) {
            if (card.color != colorHint.intValue()) {
                // remove any occurrences of card with wrong color
                slotPotentialCards.put(card, 0);
            }
        }
    }

    public void lostHints() {
        colorHint = null;
        numHint = null;

        certainValue = null;
        certainColor = null;
        certainCard = null;

        // possibleCards is reset to all potentialCards
        resetSlotPotentialCards();
    }

    // possibleCards is reset to all potentialCards
    public void resetSlotPotentialCards() {
        for (HashMap.Entry<KBCard, Integer> cardType : parent.getAllPotentialCards().entrySet()) {
            slotPotentialCards.put(new KBCard(cardType.getKey()), Integer.valueOf(cardType.getValue().intValue()));
        }

        checkForCertainColor();
        checkForCertainValue();
    }

    public void removeCardFromContention(KBCard card) {


        //should i run certainty checks here???
        checkForCertainColor();
        checkForCertainValue();

        int new_tot = slotPotentialCards.get(card)-1;
        if (new_tot < 1) {
            new_tot = 0;
        }

        slotPotentialCards.put(card, new_tot);
    }

    @Override
    public String toString() {
        String out = "";

        String strCol = null;
        if(certainColor != null) {
            strCol = Colors.suitColor(certainColor);
        }
        out+="\nCertain: card - " + certainCard + ", color - " + strCol + ", value - " + certainValue + " \n";

        for (int color=0; color<5; color++) {
            if (color == 1) {
                out+=Colors.suitColor(color) + " |\t";
            } else { out+=Colors.suitColor(color) + " |\t\t"; }
            for (int val=1; val<=5; val++) {
                out+= val + ": " + slotPotentialCards.get(new KBCard(color, val)) + "\t";
            }
            out+="\n";
        }
        return out;
    }

    public Integer getNumHint() {
        return numHint;
    }

    public Integer getColorHint() {
        return colorHint;
    }
}
