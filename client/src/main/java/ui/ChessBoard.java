package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

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
    private static Random rand = new Random();
    private static chess.ChessBoard board = new chess.ChessBoard();
    private static ChessGame.TeamColor perspective;


    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        ChessGame game = new ChessGame();
        game.doMove(new ChessMove(new ChessPosition(2, 4), new ChessPosition(4, 4)));
        board = game.getBoard();

        out.print(ERASE_SCREEN);

        perspective = ChessGame.TeamColor.WHITE;
        drawHeaders(out);
        drawTicTacToeBoard(out);
        drawHeaders(out);

        out.print(SET_BG_COLOR_BLACK);
        out.println();

        perspective = ChessGame.TeamColor.BLACK;
        drawHeaders(out);
        drawTicTacToeBoard(out);
        drawHeaders(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
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
                if ((boardCol + row)%2 == 0){
                    setWhite(out);
                }
                else{
                    setBlack(out);
                }
                ChessPosition pos;
                if(perspective == ChessGame.TeamColor.BLACK){
                    pos = new ChessPosition(row + 1, 8 - boardCol);
                }
                else{
                    pos = new ChessPosition(8 - row, boardCol + 1);
                }

                ChessPiece piece = board.getPiece(pos);
                printPlayer(out, piece);

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

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
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