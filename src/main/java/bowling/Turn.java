package bowling;

import java.util.Objects;

public class Turn {
    private final static int LAST_DOUBLE_BALL_TURN = 9;

    private final int value;

    public Turn(int value) {
        this.value = value;
    }

    public boolean isLastNormalTurn() {
        return value == LAST_DOUBLE_BALL_TURN;
    }

    public Turn getNextTurn() {
        return new Turn(value + 1);
    }

    public int getNumber() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Turn turn = (Turn) o;
        return value == turn.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}