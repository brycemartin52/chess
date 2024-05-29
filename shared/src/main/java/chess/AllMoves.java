package chess;

import java.util.ArrayList;
import java.util.Collection;

public class AllMoves {

    public static void straight(ChessBoard board, ChessPosition ogPos, Collection<ChessMove> moves){
        ChessPiece piece = board.getPiece(ogPos);
        ChessGame.TeamColor team = piece.getTeamColor();
        int ogR = ogPos.getRow();
        int ogC = ogPos.getColumn();

        int row = ogR;
        int col = ogC;
        while(row < 8){
            row++;
            ChessPosition otherPos = new ChessPosition(row, col);
            ChessPiece otherPiece = board.getPiece(otherPos);
            if(otherPiece == null){
                moves.add(new ChessMove(ogPos, otherPos));
            }
            else{
                if(otherPiece.getTeamColor() != team){
                    moves.add(new ChessMove(ogPos, otherPos));
                }
                break;
            }
        }
        row = ogR;

        while(row > 1){
            row--;
            ChessPosition otherPos = new ChessPosition(row, col);
            ChessPiece otherPiece = board.getPiece(otherPos);
            if(otherPiece == null){
                moves.add(new ChessMove(ogPos, otherPos));
            }
            else{
                if(otherPiece.getTeamColor() != team){
                    moves.add(new ChessMove(ogPos, otherPos));
                }
                break;
            }
        }
        row = ogR;

        while(col < 8){
            col++;
            ChessPosition otherPos = new ChessPosition(row, col);
            ChessPiece otherPiece = board.getPiece(otherPos);
            if(otherPiece == null){
                moves.add(new ChessMove(ogPos, otherPos));
            }
            else{
                if(otherPiece.getTeamColor() != team){
                    moves.add(new ChessMove(ogPos, otherPos));
                }
                break;
            }
        }
        col = ogC;

        while(col > 1){
            col--;
            ChessPosition otherPos = new ChessPosition(row, col);
            ChessPiece otherPiece = board.getPiece(otherPos);
            if(otherPiece == null){
                moves.add(new ChessMove(ogPos, otherPos));
            }
            else{
                if(otherPiece.getTeamColor() != team){
                    moves.add(new ChessMove(ogPos, otherPos));
                }
                break;
            }
        }
    }

    public static Collection<ChessMove> collectDiagonals(ChessBoard board, ChessPosition ogPos, Collection<ChessMove> moves, int rowAdd, int colAdd){
        int ogR = ogPos.getRow();
        int ogC = ogPos.getColumn();

        int row = ogR;
        int col = ogC;
        while(row < 8 && col < 8){
            row += rowAdd;
            col += colAdd;
            ChessPosition otherPos = new ChessPosition(row, col);
            ChessPiece otherPiece = board.getPiece(otherPos);
            if(otherPiece == null){
                moves.add(new ChessMove(ogPos, otherPos));
            }
            else{
                if(otherPiece.getTeamColor() != board.getPiece(ogPos).getTeamColor()){
                    moves.add(new ChessMove(ogPos, otherPos));
                }
                break;
            }
        }
        row = ogR;
        col = ogC;

        return moves;
    }

    public static void diagonal(ChessBoard board, ChessPosition ogPos, Collection<ChessMove> moves){
        ChessPiece piece = board.getPiece(ogPos);
        ChessGame.TeamColor team = piece.getTeamColor();
        int ogR = ogPos.getRow();
        int ogC = ogPos.getColumn();

        int row = ogR;
        int col = ogC;
        while(row < 8 && col < 8){
            row++;
            col++;
            ChessPosition otherPos = new ChessPosition(row, col);
            ChessPiece otherPiece = board.getPiece(otherPos);
            if(otherPiece == null){
                moves.add(new ChessMove(ogPos, otherPos));
            }
            else{
                if(otherPiece.getTeamColor() != team){
                    moves.add(new ChessMove(ogPos, otherPos));
                }
                break;
            }
        }
        row = ogR;
        col = ogC;

        while(row > 1 && col < 8){
            row--;
            col++;
            ChessPosition otherPos = new ChessPosition(row, col);
            ChessPiece otherPiece = board.getPiece(otherPos);
            if(otherPiece == null){
                moves.add(new ChessMove(ogPos, otherPos));
            }
            else{
                if(otherPiece.getTeamColor() != team){
                    moves.add(new ChessMove(ogPos, otherPos));
                }
                break;
            }
        }
        row = ogR;
        col = ogC;

        while(row < 8 && col > 1){
            row++;
            col--;
            ChessPosition otherPos = new ChessPosition(row, col);
            ChessPiece otherPiece = board.getPiece(otherPos);
            if(otherPiece == null){
                moves.add(new ChessMove(ogPos, otherPos));
            }
            else{
                if(otherPiece.getTeamColor() != team){
                    moves.add(new ChessMove(ogPos, otherPos));
                }
                break;
            }
        }
        row = ogR;
        col = ogC;

        while(row > 1 && col > 1){
            row--;
            col--;
            ChessPosition otherPos = new ChessPosition(row, col);
            ChessPiece otherPiece = board.getPiece(otherPos);
            if(otherPiece == null){
                moves.add(new ChessMove(ogPos, otherPos));
            }
            else{
                if(otherPiece.getTeamColor() != team){
                    moves.add(new ChessMove(ogPos, otherPos));
                }
                break;
            }
        }
    }

    public static Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition ogPos){
        Collection<ChessMove> moves = new ArrayList<>();
        straight(board, ogPos, moves);
        return moves;
    }
    public static Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition ogPos){
        Collection<ChessMove> moves = new ArrayList<>();
        diagonal(board, ogPos, moves);
        return moves;
    }
    public static Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition ogPos){
        Collection<ChessMove> moves = new ArrayList<>();
        straight(board, ogPos, moves);
        diagonal(board, ogPos, moves);
        return moves;
    }
    public static Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition ogPos){
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor team = board.getPiece(ogPos).getTeamColor();
        int ogR = ogPos.getRow();
        int ogC = ogPos.getColumn();

        int[][] posMoves = {{1,1}, {1,0}, {1,-1}, {0,1}, {0,-1}, {-1,1}, {-1,0}, {-1,-1}};

        for(int i = 0; i <= 7; i++){
            int row = posMoves[i][0] + ogR;
            int col = posMoves[i][1] + ogC;

            if(1 <= row && row <= 8 && 1 <= col && col <= 8){
                ChessPosition otherPos = new ChessPosition(row, col);
                ChessPiece otherPiece = board.getPiece(otherPos);
                if(otherPiece == null){
                    moves.add(new ChessMove(ogPos, otherPos));
                }
                else{
                    if(otherPiece.getTeamColor() != team){
                        moves.add(new ChessMove(ogPos, otherPos));
                    }
                }
            }
        }

        return moves;
    }
    public static Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition ogPos){
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor team = board.getPiece(ogPos).getTeamColor();
        int ogR = ogPos.getRow();
        int ogC = ogPos.getColumn();

        int[][] posMoves = {{1,2}, {2,1}, {1,-2}, {2, -1}, {-1,2}, {-2,1}, {-1,-2}, {-2, -1}};

        for(int i = 0; i <= 7; i++){
            int row = posMoves[i][0] + ogR;
            int col = posMoves[i][1] + ogC;

            if(1 <= row && row <= 8 && 1 <= col && col <= 8){
                ChessPosition otherPos = new ChessPosition(row, col);
                ChessPiece otherPiece = board.getPiece(otherPos);
                if(otherPiece == null){
                    moves.add(new ChessMove(ogPos, otherPos));
                }
                else{
                    if(otherPiece.getTeamColor() != team){
                        moves.add(new ChessMove(ogPos, otherPos));
                    }
                }
            }
        }

        return moves;
    }
    public static Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition ogPos){
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor team = board.getPiece(ogPos).getTeamColor();
        int row = ogPos.getRow();
        int col = ogPos.getColumn();

        int start = switch(team){
            case WHITE -> 2;
            case BLACK -> 7;
        };
        int forward = switch(team){
            case WHITE -> 1;
            case BLACK -> -1;
        };

        //Forward logic
        ChessPosition otherPos = new ChessPosition(row + forward, col);
        ChessPiece otherPiece = board.getPiece(otherPos);
        if(otherPiece == null){
            if(row == start + 5*forward){
                moves.add(new ChessMove(ogPos, otherPos, ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(ogPos, otherPos, ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(ogPos, otherPos, ChessPiece.PieceType.QUEEN));
                moves.add(new ChessMove(ogPos, otherPos, ChessPiece.PieceType.KNIGHT));
            }
            else{
                if(row == start){
                    ChessPosition jumpPos = new ChessPosition(row + 2*forward, col);
                    ChessPiece jumpPiece = board.getPiece(jumpPos);
                    if(jumpPiece == null){
                        moves.add(new ChessMove(ogPos, jumpPos));
                    }
                }
                moves.add(new ChessMove(ogPos, otherPos));
            }
        }

        // Right diagonal
        if(col < 8){
            ChessPosition rigPos = new ChessPosition(row + forward, col + 1);
            ChessPiece rigPiece = board.getPiece(rigPos);
            if(rigPiece != null && rigPiece.getTeamColor() != team){
                if(row == start + 5*forward){
                    moves.add(new ChessMove(ogPos, rigPos, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(ogPos, rigPos, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(ogPos, rigPos, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(ogPos, rigPos, ChessPiece.PieceType.KNIGHT));
                }
                else{
                    moves.add(new ChessMove(ogPos, rigPos));
                }
            }
        }

        // Left diagonal
        if(col > 1){
            ChessPosition lefPos = new ChessPosition(row + forward, col - 1);
            ChessPiece lefPiece = board.getPiece(lefPos);
            if(lefPiece != null && lefPiece.getTeamColor() != team){
                if(row == start + 5*forward){
                    moves.add(new ChessMove(ogPos, lefPos, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(ogPos, lefPos, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(ogPos, lefPos, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(ogPos, lefPos, ChessPiece.PieceType.KNIGHT));
                }
                else{
                    moves.add(new ChessMove(ogPos, lefPos));
                }
            }
        }
        return moves;
    }
}
