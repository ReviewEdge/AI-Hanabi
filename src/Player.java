import java.util.ArrayList;
import java.util.Scanner;

/**
 * This is the only class you should edit.
 * @author You
 *
 */
public class Player {
	// Add whatever variables you want. You MAY NOT use static variables, or otherwise allow direct communication between
	// different instances of this class by any means; doing so will result in a score of 0.
	
	// Delete this once you actually write your own version of the class.
	private static Scanner scn = new Scanner(System.in);

	private PartialHand myHand;
	private PartialHand whatMyPartnerKnows;
	private Hand partnerHand;
	private ArrayList<Card> discardTracker;
	private Board latestBoard;
	
	/**
	 * This default constructor should be the only constructor you supply.
	 */
	public Player() {
		this.myHand = new PartialHand();
		this.partnerHand = new Hand();
		this.whatMyPartnerKnows = new PartialHand();
		this.discardTracker = new ArrayList<Card>();
		this.latestBoard = new Board();
	}

	public void firstTimeSeeingPartnerHand(Hand firstPH) throws Exception {
		this.partnerHand = new Hand(firstPH);

		// mark all of partner's cards as used
		for (int i=0; i<firstPH.size(); i++) {
			myHand.thisCardHasBeenUsed(new KBCard(firstPH.get(i)));
		}
	}

	public void recordHintToPartner(int hint, boolean isColor) throws Exception {
		ArrayList<Integer> applyingPartHint = new ArrayList<>();
		for (int sl2=0; sl2 < 5; sl2++) {
			if (isColor) {
				if (partnerHand.get(sl2).color == hint) {
					applyingPartHint.add(sl2);
				}
			} else {
				if (partnerHand.get(sl2).value == hint) {
					applyingPartHint.add(sl2);
				}
			}

		}
		// ^^ vvv  update my KB about what my partner knows
		if (isColor) {
			whatMyPartnerKnows.takeAColorHint(hint, applyingPartHint);
		} else {
			whatMyPartnerKnows.takeANumHint(hint, applyingPartHint);
		}
	}



	public void recordHintToPartner(int hint, boolean isColor, Hand ph) throws Exception {
		ArrayList<Integer> applyingPartHint = new ArrayList<>();
		for (int sl2=0; sl2 < 5; sl2++) {
			if (isColor) {
				if (ph.get(sl2).color == hint) {
					applyingPartHint.add(sl2);
				}
			} else {
				if (ph.get(sl2).value == hint) {
					applyingPartHint.add(sl2);
				}
			}

		}
		// ^^ vvv  update my KB about what my partner knows
		if (isColor) {
			whatMyPartnerKnows.takeAColorHint(hint, applyingPartHint);
		} else {
			whatMyPartnerKnows.takeANumHint(hint, applyingPartHint);
		}
	}
	
	/**
	 * This method runs whenever your partner discards a card.
	 * @param startHand The hand your partner started with before discarding.
	 * @param discard The card he discarded.
	 * @param disIndex The index from which he discarded it.
	 * @param draw The card he drew to replace it; null, if the deck is empty.
	 * @param drawIndex The index to which he drew it.
	 * @param finalHand The hand your partner ended with after redrawing.
	 * @param boardState The state of the board after play.
	 */
	public void tellPartnerDiscard(Hand startHand, Card discard, int disIndex, Card draw, int drawIndex, 
			Hand finalHand, Board boardState) throws Exception {
		if(partnerHand.size() == 0){
			firstTimeSeeingPartnerHand(startHand);
		}

		// update knowledge base
		if (draw != null) {
			myHand.thisCardHasBeenUsed(new KBCard(draw));
		}

		partnerHand = new Hand(finalHand);
		whatMyPartnerKnows.slots.get(disIndex).lostHints();

		discardTracker.add(discard);

	}
	
	/**
	 * This method runs whenever you discard a card, to let you know what you discarded.
	 * @param discard The card you discarded.
	 * @param boardState The state of the board after play.
	 */
	public void tellYourDiscard(Card discard, Board boardState) {
		// update knowledge base
		myHand.thisCardHasBeenUsed(new KBCard(discard));
		discardTracker.add(discard);

	}
	
	/**
	 * This method runs whenever your partner played a card
	 * @param startHand The hand your partner started with before playing.
	 * @param play The card she played.
	 * @param playIndex The index from which she played it.
	 * @param draw The card she drew to replace it; null, if the deck was empty.
	 * @param drawIndex The index to which she drew the new card.
	 * @param finalHand The hand your partner ended with after playing.
	 * @param wasLegalPLay Whether the play was legal or not.
	 * @param boardState The state of the board after play.
	 */
	public void tellPartnerPlay(Hand startHand, Card play, int playIndex, Card draw, int drawIndex,
			Hand finalHand, boolean wasLegalPlay, Board boardState) throws Exception {
		if(partnerHand.size() == 0){
			firstTimeSeeingPartnerHand(startHand);
		}

		// update knowledge base
		if (draw != null) {
			myHand.thisCardHasBeenUsed(new KBCard(draw));
		}
		partnerHand = new Hand(finalHand);
		whatMyPartnerKnows.slots.get(playIndex).lostHints();
	}
	
	/**
	 * This method runs whenever you play a card, to let you know what you played.
	 * @param play The card you played.
	 * @param wasLegalPlay Whether the play was legal or not.
	 * @param boardState The state of the board after play.
	 */
	public void tellYourPlay(Card play, boolean wasLegalPlay, Board boardState) {
		// update knowledge base
		myHand.thisCardHasBeenUsed(new KBCard(play));
	}
	
	/**
	 * This method runs whenever your partner gives you a hint as to the color of your cards.
	 * @param color The color hinted, from Colors.java: RED, YELLOW, BLUE, GREEN, or WHITE.
	 * @param indices The indices (from 0-4) in your hand with that color.
	 * @param partnerHand Your partner's current hand.
	 * @param boardState The state of the board after the hint.
	 */
	public void tellColorHint(int color, ArrayList<Integer> indices, Hand partnerHand, Board boardState) throws Exception {
		if(partnerHand.size() == 0){
			firstTimeSeeingPartnerHand(partnerHand);
		}

		myHand.takeAColorHint(color, indices);
	}
	
	/**
	 * This method runs whenever your partner gives you a hint as to the numbers on your cards.
	 * @param number The number hinted, from 1-5.
	 * @param indices The indices (from 0-4) in your hand with that number.
	 * @param partnerHand Your partner's current hand.
	 * @param boardState The state of the board after the hint.
	 */
	public void tellNumberHint(int number, ArrayList<Integer> indices, Hand partnerHand, Board boardState) throws Exception {
		if(partnerHand.size() == 0){
			firstTimeSeeingPartnerHand(partnerHand);
		}

		myHand.takeANumHint(number, indices);
	}
	
	/**
	 * This method runs when the game asks you for your next move.
	 * @param yourHandSize How many cards you have in hand.
	 * @param partnerHand Your partner's current hand.
	 * @param boardState The current state of the board.
	 * @return A string encoding your chosen action. Actions should have one of the following formats; in all cases,
	 *  "x" and "y" are integers.
	 * 	a) "PLAY x y", which instructs the game to play your card at index x and to draw a card back to index y. You
	 *     should supply an index y even if you know the deck to be empty. All indices should be in the range 0-4.
	 *     Illegal plays will consume a fuse; at 0 fuses, the game ends with a score of 0.
	 *  b) "DISCARD x y", which instructs the game to discard the card at index x and to draw a card back to index y.
	 *     You should supply an index y even if you know the deck to be empty. All indices should be in the range 0-4.
	 *     Discarding returns one hint if there are fewer than the maximum number available.
	 *  c) "NUMBERHINT x", where x is a value from 1-5. This command informs your partner which of his cards have a value
	 *     of the chosen number. An error will result if none of his cards have that value, or if no hints remain.
	 *     This command consumes a hint.
	 *  d) "COLORHINT x", where x is one of the RED, YELLOW, BLUE, GREEN, or WHITE constant values in Colors.java.
	 *     This command informs your partner which of his cards have the chosen color. An error will result if none of
	 *     his cards have that color, or if no hints remain. This command consumes a hint.
	 */
	public String ask(int yourHandSize, Hand partnerHand, Board boardState) throws Exception {
		latestBoard = boardState;
		if(partnerHand.size() == 0){
			firstTimeSeeingPartnerHand(partnerHand);
		}

		// DECISION MAKING

		// see what the legal plays are

		Integer safeValue = null;
		boolean hasSafeValue = true;


		ArrayList<Card> legalPlays = new ArrayList<Card>(5);
		for (int color = 0; color<latestBoard.tableau.size(); color++) {
			int play_val = latestBoard.tableau.get(color)+1;
			if (play_val != 6) {
				if (safeValue == null) {
					safeValue = play_val;
				} else if ((hasSafeValue) && (play_val != safeValue)) {
					hasSafeValue = false;
					safeValue = null;
				}

				legalPlays.add(new Card(color, play_val));
			}
		}

		//--------------------- if you can for certain make a legal play, do it ---------------------

		// play at that slot if a certain value is also a safe value, or if a certain card is a legal card
		int slot_counter = 0;
		for (Slot sl : myHand.slots) {

			// plays at card of a value if you know that value is safe
			if (sl.getCertainValue() != null) {
				if ( (hasSafeValue) && (sl.getCertainValue().equals(safeValue)) ) {
					// remove hints for that slot
					myHand.slots.get(slot_counter).lostHints();

					return "PLAY " + slot_counter + " " + slot_counter;
				}
			}

			// plays a card if it is certain and legal
			if (sl.getCertainCard() != null) {
				if ((sl.getCertainCard() != null) && legalPlays.contains(sl.getCertainCard())) {
					// remove hints for that slot
					myHand.slots.get(slot_counter).lostHints();

					return "PLAY " + slot_counter + " " + slot_counter;
				}
			}

			slot_counter++;
		}



		//--------------------- make the best hint, if the hint condition is met ---------------------

		if (latestBoard.numHints > 0) {
			// do they have a card that is currently playable? give them the first or second hint for it
			for (int sl = 0; sl < 5; sl++) {
				if (legalPlays.contains(partnerHand.get(sl))) {
					// if they don't know #, hint it
					if (whatMyPartnerKnows.slots.get(sl).checkForCertainValue() == null) {
						recordHintToPartner(partnerHand.get(sl).value, false, partnerHand);
						return "NUMBERHINT " + partnerHand.get(sl).value;
					}

					// if they don't know color, hint it
					if (whatMyPartnerKnows.slots.get(sl).checkForCertainColor() == null) {
						recordHintToPartner(partnerHand.get(sl).color, true, partnerHand);
						return "COLORHINT " + partnerHand.get(sl).color;
					}

					// if they know both, don't do anything
				}
			}

		} //end hints block



		//--------------------- else, discard ---------------------


		// defaults to dumping first card
		String discard_statement = "DISCARD 0 0";

		// discard certain-cards lower than played lowest of that color
		ArrayList<KBCard> partner_certain = myHand.getMyCertainCards();
		for (int sl = 0; sl < 5; sl++) {
			KBCard certain_card = partner_certain.get(sl);
			if (certain_card != null) {
				// if it's lower than is playable, discard it
				if (certain_card.value <= latestBoard.tableau.get(certain_card.color)) {
					myHand.slots.get(sl).lostHints();
					return "DISCARD " + sl + " " + sl;
				}
			}
		}


//		discard slot with least data (no hints)
		for (int sl = 0; sl < 5; sl++) {
			if((myHand.slots.get(sl).getNumHint() == null) && (myHand.slots.get(sl).getColorHint() == null)) {
				myHand.slots.get(sl).lostHints();
				return "DISCARD " + sl + " " + sl;
			}
		}

		// if you have hints left...
		// Checks for helpful hints again, this time it's more lenient because it has no good cards to discard
		// increases average score by about .3 (hints threshold > 1)

		if (latestBoard.numHints > 1) {

			// increments the legal hint card by 1
			for (int i = 0; i < legalPlays.size(); i++) {
				KBCard oldCard = new KBCard(legalPlays.get(i));
				legalPlays.set(i, new KBCard(oldCard.value+1, oldCard.color));
			}

			// do they have a card that is currently playable? give them the first or second hint for it
			for (int sl = 0; sl < 5; sl++) {
				if (legalPlays.contains(partnerHand.get(sl))) {
					// if they don't know #, hint it
					if (whatMyPartnerKnows.slots.get(sl).checkForCertainValue() == null) {
						recordHintToPartner(partnerHand.get(sl).value, false, partnerHand);
						return "NUMBERHINT " + partnerHand.get(sl).value;
					}

					// if they don't know color, hint it
					if (whatMyPartnerKnows.slots.get(sl).checkForCertainColor() == null) {
						recordHintToPartner(partnerHand.get(sl).color, true, partnerHand);
						return "COLORHINT " + partnerHand.get(sl).color;
					}

					// if they know both, don't do anything
				}
			}

		} // end hints-2 block


		// If you have some fuses to risk, make a guess play
		// (increases average score by about .25)
		if (latestBoard.numFuses > 0) {
			// play best card (any card with a hint)
			for (int sl = 0; sl < 5; sl++) {
				// if you have a card that you know the value, play it
				if(myHand.slots.get(sl).getCertainValue() != null) {
					// remove hints for that slot
					myHand.slots.get(sl).lostHints();
					return "PLAY " + sl + " " + sl;
				}
			}
		}


		// if there are no good moves, dump the first slot
		myHand.slots.get(0).lostHints();
		return discard_statement;
	}


	@Override
	public String toString() {
		return "Me: " + myHand.toString();
	}
}
