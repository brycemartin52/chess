package chess;

import java.util.Collection;
import java.util.Objects;
import java.util.ArrayList;

public class AllMoves {

    private void straight(ChessPosition pos, boolean add){
        ChessPosition new_pos = new ChessPosition(pos.getRow(), pos.getColumn());
    }

    private void diagonal(ChessPosition pos, boolean add){

    }

    public static Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition pos){
        Collection<ChessMove> moves = new ArrayList<>();
        int cur_row = pos.getRow();
        int cur_col = pos.getRow();
        Collection<ChessMove> potential_moves = new ArrayList<>();
        // for move in potential_moves
        // if

        return moves;
    }
}
