package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static ui.EscapeSequences.*;

public class ChessBoard {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 1;
    private static final int LINE_WIDTH_IN_CHARS = 1;
    private static final String EMPTY = EscapeSequences.EMPTY;
    private static final String PAWN = BLACK_PAWN;
    private static final String BISHOP = BLACK_BISHOP;
    private static final String KNIGHT = BLACK_KNIGHT;
    private static final String ROOK = BLACK_ROOK;
    private static final String QUEEN = BLACK_QUEEN;
    private static final String KING = BLACK_KING;
    private static chess.ChessBoard board = new chess.ChessBoard();
    private static ChessGame.TeamColor perspective;
    private static ArrayList<ChessPosition> hiCoordinates;


    public static void main(String[] args) {
        ChessGame game = new ChessGame();
        printBoard(game, null);
    }

    public static String printBoard(ChessGame game, ArrayList <ChessPosition> highlightCoordinates){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        board = game.getBoard();
        hiCoordinates = highlightCoordinates;

        out.print(ERASE_SCREEN);

        if(game.getTeamTurn() == ChessGame.TeamColor.WHITE){
            perspective = ChessGame.TeamColor.WHITE;
        }
        else{
            perspective = ChessGame.TeamColor.BLACK;
        }

        drawHeaders(out);
        drawTicTacToeBoard(out);
        drawHeaders(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
        return "Printed";
    }

    private static void drawHeaders(PrintStream out) {
        String[] headers;
        if(perspective == ChessGame.TeamColor.BLACK){
            headers = new String[]{" \u2003\u2003" + "H", "G", "F", "E", "D", "C", "B", "A"};
        }
        else{
            headers = new String[]{" \u2003\u2003" + "A", "B", "C", "D", "E", "F", "G", "H"};
        }

        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeaderText(out, headers[boardCol]);
            out.print(" \u2003".repeat(LINE_WIDTH_IN_CHARS));
        }
        out.print("\u2003");

        setBlack(out);
        out.println();
    }

    private static void drawHeaderText(PrintStream out, String headerText) {
        setHeader(out);
        out.print(headerText);
    }

    private static void drawTicTacToeBoard(PrintStream out) {
        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
            drawRowOfSquares(out, boardRow);
        }
    }

    private static void drawRowOfSquares(PrintStream out, int row) {

        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_CHARS; ++squareRow) {
            setHeader(out);
            if(perspective == ChessGame.TeamColor.BLACK){
                out.print(" " + (row + 1) + " ");
            }
            else{
                out.print(" " + (8 - row) + " ");
            }

            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; boardCol++) {
                ChessPosition pos = switch (perspective){
                    case ChessGame.TeamColor.BLACK -> new ChessPosition(row + 1, 8 - boardCol);
                    case ChessGame.TeamColor.WHITE -> new ChessPosition(8 - row, boardCol + 1);
                };

                if ((boardCol + row)%2 == 0){
                    if(hiCoordinates != null && hiCoordinates.contains(pos)){
                        if(hiCoordinates.getFirst().equals(pos)){setYellow(out);}
                        else{setHighlightedWhite(out);}
                    }
                    else{setWhite(out);}

                }
                else{
                    if(hiCoordinates != null && hiCoordinates.contains(pos)){
                        if(hiCoordinates.getFirst().equals(pos)){setYellow(out);}
                        else{setHighlightedBlack(out);}
                    }
                    else{setBlack(out);}
                }
                printPlayer(out, board.getPiece(pos));

            }
            setHeader(out);
            out.print(" " + (row + 1) + " ");

            setBlack(out);
            out.println();
        }
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setHighlightedWhite(PrintStream out) {
        out.print(SET_BG_COLOR_GREEN);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setYellow(PrintStream out) {
        out.print(SET_BG_COLOR_YELLOW);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setHighlightedBlack(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREEN);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setHeader(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_GREEN);
    }

    private static void printPlayer(PrintStream out, ChessPiece piece) {

        if(piece == null){
            out.print(EMPTY);
        }
        else{
            ChessPiece.PieceType type = piece.getPieceType();
            ChessGame.TeamColor team = piece.getTeamColor();
            if(team == ChessGame.TeamColor.WHITE){out.print(SET_TEXT_COLOR_DARK_BLUE);}
            else{out.print(SET_TEXT_COLOR_RED);}

            String pieceType = switch(type){
                case ChessPiece.PieceType.QUEEN -> QUEEN;
                case ChessPiece.PieceType.BISHOP -> BISHOP;
                case ChessPiece.PieceType.KNIGHT -> KNIGHT;
                case ChessPiece.PieceType.ROOK -> ROOK;
                case ChessPiece.PieceType.PAWN -> PAWN;
                case ChessPiece.PieceType.KING -> KING;
            };
            out.print(pieceType);
        }
    }
}
