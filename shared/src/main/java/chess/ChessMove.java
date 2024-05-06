package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    ChessPosition startPosition;
    ChessPosition endPosition;
    ChessPiece.PieceType piece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition) {
        this.piece = null;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.piece = promotionPiece;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {return startPosition;}

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {return endPosition;}

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {return piece;}
}
