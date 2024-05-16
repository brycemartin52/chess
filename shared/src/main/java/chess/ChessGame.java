package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor teamTurn;

    private ChessPosition whiteKingPos;
    private ChessPosition blackKingPos;

    public ChessGame() {
        this.board = new ChessBoard();
        board.resetBoard();
        teamTurn = TeamColor.WHITE;
//        findKings();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {teamTurn = team;}

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    public void doMove(ChessMove move){
        ChessPosition start_pos = move.getStartPosition();
        ChessPiece piece = board.getPiece(start_pos);
        if(move.getPromotionPiece() != null){
            piece.type = move.getPromotionPiece();
        }
        board.addPiece(start_pos, null);
        board.addPiece(move.getEndPosition(), piece);
    }

    public void findKings(){
        boolean found1 = false;
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                ChessPosition pos = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(pos);
                if(piece != null && piece.getPieceType() == ChessPiece.PieceType.KING){
                    switch(piece.getTeamColor()){
                        case WHITE -> whiteKingPos = pos;
                        case BLACK -> blackKingPos = pos;
                    }
                    if(found1){
                        return;
                    }
                    else{
                        found1 = true;
                    }
                }
            }
        }
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessBoard tmpBoard = board;
        ChessPiece piece = board.getPiece(startPosition);
        TeamColor team = piece.getTeamColor();
        Collection<ChessMove> potentialMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> valMoves = new ArrayList<>();

//        try{
            for(ChessMove move : potentialMoves) {
                board = (ChessBoard) tmpBoard.copy();
                doMove(move);
                findKings();
                if (!isInCheck(team)) {
                    valMoves.add(move);
                }
            }
//        }
//        catch(CloneNotSupportedException e){
//            throw new RuntimeException(e);
//        }


        board = tmpBoard;
        return valMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece == null) {
            throw new InvalidMoveException("Position contains no piece");
        }

        if (piece.getTeamColor() != getTeamTurn()) {
            throw new InvalidMoveException("Incorrect team");
        }

        Collection<ChessMove> posMoves = validMoves(move.getStartPosition());

        if (!posMoves.contains(move)) {
            throw new InvalidMoveException("Position is out of range");
        }
        doMove(move);
        if(piece.getPieceType() == ChessPiece.PieceType.KING){
            findKings();
        }
    }


    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPos = switch(teamColor){
            case WHITE -> whiteKingPos;
            case BLACK -> blackKingPos;
        };

        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                ChessPosition pos = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(pos);
                if(piece != null && piece.getTeamColor() != teamColor){
                    Collection<ChessMove> moves = piece.pieceMoves(board, pos);
                    for(ChessMove move : moves){
                        ChessPosition endPos = move.getEndPosition();
                        if(endPos.equals(kingPos)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(!isInCheck(teamColor)){
            return false;
        }
        else{
            // Copy the board
            // grab all the moves
            // test each individual move
            // for each move, if not still in check, then add to the collection
            // if the collection is empty, return true
            //
            return true;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
        findKings();
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {return board;}
}
