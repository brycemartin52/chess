package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] board;

    public ChessBoard() {
       board  = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        int row = position.getRow();
        int col = position.getColumn();
        board[row][col] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();
        return board[row][col];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
//         for(int i = 0; i < 7; i++){
//             addPiece(ChessPosition(1, i), ChessPiece(WHITE, PAWN));
//         }
//         addPiece(ChessPosition(0, 0), ChessPiece(WHITE, ROOK));
//         addPiece(ChessPosition(0, 7), ChessPiece(WHITE, ROOK));
//         addPiece(ChessPosition(0, 1), ChessPiece(WHITE, KNIGHT));
//         addPiece(ChessPosition(0, 6), ChessPiece(WHITE, KNIGHT));
//         addPiece(ChessPosition(0, 2), ChessPiece(WHITE, BISHOP));
//         addPiece(ChessPosition(0, 5), ChessPiece(WHITE, BISHOP));
//         addPiece(ChessPosition(0, 3), ChessPiece(WHITE, QUEEN));
//         addPiece(ChessPosition(0, 4), ChessPiece(WHITE, KING));
//
//         for(int i = 0; i < 7; i++){
//             addPiece(ChessPosition(6, i), ChessPiece(BLACK, PAWN));
//         }
//         addPiece(ChessPosition(7, 0), ChessPiece(BLACK, ROOK));
//         addPiece(ChessPosition(7, 7), ChessPiece(BLACK, ROOK));
//         addPiece(ChessPosition(7, 1), ChessPiece(BLACK, KNIGHT));
//         addPiece(ChessPosition(7, 6), ChessPiece(BLACK, KNIGHT));
//         addPiece(ChessPosition(7, 2), ChessPiece(BLACK, BISHOP));
//         addPiece(ChessPosition(7, 5), ChessPiece(BLACK, BISHOP));
//         addPiece(ChessPosition(7, 3), ChessPiece(BLACK, QUEEN));
//         addPiece(ChessPosition(7, 4), ChessPiece(BLACK, KING));
        throw new RuntimeException("Not implemented");
    }
}
