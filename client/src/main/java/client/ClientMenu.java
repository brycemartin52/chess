package client;

import chess.ChessGame;

public class ClientMenu {

    public static void displayMenu(boolean loggedIn, boolean inGame){
        if(!loggedIn){
            System.out.println("(R)egister <username> <password> <email>");
            System.out.println("(L)ogin <username> <password>");
            System.out.println("(H)elp");
            System.out.println("(Q)uit");
        }

        else if(inGame){
            System.out.println("(R)edraw");
            System.out.println("(M)ove");
            System.out.println("(Hi)ghlight <piecePos>");
            System.out.println("(L)eave");
            System.out.println("Resign/(Q)");
            System.out.println("(H)elp");
        }

        else{
            System.out.println("(N)ew <Game Name>");
            System.out.println("(A)ll");
            System.out.println("(P)lay <WHITE/BLACK> <Game ID>");
            System.out.println("(W)atch <Game ID>");
            System.out.println("(H)elp");
            System.out.println("Log(O)ut");
        }
    }

    public static String displayHelpMenu(boolean loggedIn, boolean inGame, ChessGame.TeamColor team) {
        if (!loggedIn) {
            return """
                Type...
                    'Register' or 'R': Sign up to play chess
                    'Login' or 'L': Login to view your games
                    'Help' or 'H': Show possible actions
                    'Quit' or 'Q': Exit application
                """;
        }
        else if(inGame){
            if(team != null){
                return """
                Type...
                    'Redraw' or 'R': Redraws the board
                    'Move' or 'M': type a starting and ending position
                        where you'd like to move a piece
                    'Highlight' or 'Hi': type a position where you'd
                        like to see the possible moves for a piece
                    'Leave' or 'L': Leave the game (you can rejoin later,
                        or someone else can take your place
                    'Resign' or 'Q': Resign the game. Better luck next time!
                    'Help' or 'H': Show possible actions
                """;
            }

            return """
                Type...
                    'Redraw' or 'R': Redraws the board
                    'Highlight' or 'Hi': type a position where you'd
                        like to see the possible moves for a piece
                    'Leave' or 'L': Leave the game (you can rejoin later,
                        or someone else can take your place
                    'Help' or 'H': Show possible actions
                """;

        }
        return """
                Type...
                    'New' or 'N': Time to start a fresh game
                    'All' or 'A': What are my options?
                    'Play' or 'P': It's about to get crazy
                    'Watch' or 'W': Watch an epic showdown
                    'Help' or 'H': What can I even do?
                    'Logout' or 'L': Back to main menu
                """;
    }
}
