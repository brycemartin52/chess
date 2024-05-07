package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private int r;
    private int c;

    public ChessPosition(int row, int col) {
        this.r = row;
        this.c = col;
    }

    public boolean inBounds(){
        return 1 <= r && r <= 8 && 1 <= c && c <= 8;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {return r;}

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {return c;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPosition that = (ChessPosition) o;
        return r == that.r && c == that.c;
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, c);
    }

    @Override
    public String toString() {
        return "ChessPosition{" +
                "row=" + r +
                ", column=" + c +
                '}';
    }
}
