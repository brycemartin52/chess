package chess;

import java.util.Collection;
import java.util.Objects;
import java.util.ArrayList;

public class AllMoves {

    private void straight(ChessPosition pos){
        ChessPosition new_pos = new ChessPosition(pos.getRow(), pos.getColumn());
        //Change new_position one at a time and validate the move,
        //by checking if it's empty or has and opponents piece (in
        //which case it adds it as valid but stops iterating).
    }

    private void diagonal(ChessPosition pos){
        //Same process as diagonal
    }

    public static Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition pos){
        Collection<ChessMove> moves = new ArrayList<>();
        int cur_row = pos.getRow();
        int cur_col = pos.getRow();
        // Checks if in front is empty, adds if it's the case
        // Checks if either diagonal has an opponent piece, add them
        // For both conditions, check if on last row. If so, add promotion
        // Checks if on starting row and if 1 and 2 more spaces are valid, then add jump
        return moves;
    }

    public static Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition pos){
        Collection<ChessMove> moves = new ArrayList<>();
        int cur_row = pos.getRow();
        int cur_col = pos.getRow();
        // checks all L forms and adds valid moves
        return moves;
    }

    public static Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition pos){
        Collection<ChessMove> moves = new ArrayList<>();
        int cur_row = pos.getRow();
        int cur_col = pos.getRow();
        // Runs diagonal to check and add all valid moves
        return moves;
    }

    public static Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition pos){
        Collection<ChessMove> moves = new ArrayList<>();
        int cur_row = pos.getRow();
        int cur_col = pos.getRow();
        // Runs straight to check and add all valid moves
        return moves;
    }

    public static Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition pos){
        Collection<ChessMove> moves = new ArrayList<>();
        int cur_row = pos.getRow();
        int cur_col = pos.getRow();
        // Runs straight to check and add all valid moves
        // Runs diagonal to check and add all valid moves
        return moves;
    }

    public static Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition pos){
        Collection<ChessMove> moves = new ArrayList<>();
        int cur_row = pos.getRow();
        int cur_col = pos.getRow();
        // Checks 1 around king to add valid moves
        return moves;
    }
}
