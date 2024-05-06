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
       resetBoard();
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
//             addPiece(new ChessPosition(1, i), new ChessPiece(WHITE, PAWN));
//         }
//         addPiece(new ChessPosition(0, 0), new ChessPiece(WHITE, ROOK));
//         addPiece(new ChessPosition(0, 7), new ChessPiece(WHITE, ROOK));
//         addPiece(new ChessPosition(0, 1), new ChessPiece(WHITE, KNIGHT));
//         addPiece(new ChessPosition(0, 6), new ChessPiece(WHITE, KNIGHT));
//         addPiece(new ChessPosition(0, 2), new ChessPiece(WHITE, BISHOP));
//         addPiece(new ChessPosition(0, 5), new ChessPiece(WHITE, BISHOP));
//         addPiece(new ChessPosition(0, 3), new ChessPiece(WHITE, QUEEN));
//         addPiece(new ChessPosition(0, 4), new ChessPiece(WHITE, KING));
//
//         for(int i = 0; i < 7; i++){
//             addPiece(new ChessPosition(6, i), new ChessPiece(BLACK, PAWN));
//         }
//         addPiece(new ChessPosition(7, 0), new ChessPiece(BLACK, ROOK));
//         addPiece(new ChessPosition(7, 7), new ChessPiece(BLACK, ROOK));
//         addPiece(new ChessPosition(7, 1), new ChessPiece(BLACK, KNIGHT));
//         addPiece(new ChessPosition(7, 6), new ChessPiece(BLACK, KNIGHT));
//         addPiece(new ChessPosition(7, 2), new ChessPiece(BLACK, BISHOP));
//         addPiece(new ChessPosition(7, 5), new ChessPiece(BLACK, BISHOP));
//         addPiece(new ChessPosition(7, 3), new ChessPiece(BLACK, QUEEN));
//         addPiece(new ChessPosition(7, 4), new ChessPiece(BLACK, KING));
        throw new RuntimeException("Not implemented");
    }
}
