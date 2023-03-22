import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;


public class PartialHand {

    private HashMap<KBCard, Integer> allPotentialCards;
    public ArrayList<Slot> slots;


    public PartialHand() {

        allPotentialCards = getNewDeck();
        slots = new ArrayList<Slot>(5);

        for (int slot = 0; slot < 5; slot++) {
            slots.add(new Slot(this, getNewDeck()));
        }
    }

    public ArrayList<KBCard> getMyCertainCards() {
        ArrayList<KBCard> certainCards = new ArrayList<KBCard>(5);
        for (Slot sl : slots) {
            sl.calcBothCertainty();      //TODO: probably a wasteful call
            KBCard get_certain = sl.getCertainCard();
            certainCards.add(get_certain);
        }
        return certainCards;
    }

    public HashMap<KBCard, Integer> getAllPotentialCards() {
        return allPotentialCards;
    }

//    blacklist a card
    public void thisCardHasBeenUsed(KBCard card) {
        allPotentialCards.put(card, allPotentialCards.get(card)-1);
        // remove one occurrence of card from each slot's possible card
        for (Slot sl : slots) {
            sl.removeCardFromContention(card);
        }
    }

    public void takeAColorHint(int color, ArrayList<Integer> indices) {
        for (Integer sl : indices) {
            slots.get(sl).setColorHint(color);
        }
    }

    public void takeANumHint(int num, ArrayList<Integer> indices) {
        for (Integer sl : indices) {
            slots.get(sl).setNumHint(num);
        }
    }

    private HashMap<KBCard, Integer> getNewDeck() {
        HashMap<KBCard, Integer> newDeck = new HashMap<KBCard, Integer>();
        // Loads deck with three of each 1, two of each 2-3-4, and one of each 5.
        for (int i = 0; i < 5; i++) {
            newDeck.put(new KBCard(i, 1), 3);
            newDeck.put(new KBCard(i, 2), 2);
            newDeck.put(new KBCard(i, 3), 2);
            newDeck.put(new KBCard(i, 4), 2);
            newDeck.put(new KBCard(i, 5), 1);
        }
        return newDeck;
    }

    @Override
    public String toString() {
        String out = "";
        for (int slot = 0; slot < 5; slot++) {
            out = out.concat("Slot " + slot + slots.get(slot).toString() + "\n");
        }
        return out;
    }
}
