import java.util.Objects;

public class KBCard extends Card {

    public KBCard(int color, int value) {
        super(color, value);
    }

    public KBCard(Card c) {
        super(c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, value);
    }
}
