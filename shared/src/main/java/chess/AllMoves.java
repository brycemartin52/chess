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
            ChessPosition other_pos = new ChessPosition(row, col);
            ChessPiece other_piece = board.getPiece(other_pos);
            if(other_piece == null){
                moves.add(new ChessMove(ogPos, other_pos));
            }
            else{
                if(other_piece.getTeamColor() != team){
                    moves.add(new ChessMove(ogPos, other_pos));
                }
                break;
            }
        }
        row = ogR;
        col = ogC;

        while(row < 8 && col > 1){
            row++;
            col--;
            ChessPosition other_pos = new ChessPosition(row, col);
            ChessPiece other_piece = board.getPiece(other_pos);
            if(other_piece == null){
                moves.add(new ChessMove(ogPos, other_pos));
            }
            else{
                if(other_piece.getTeamColor() != team){
                    moves.add(new ChessMove(ogPos, other_pos));
                }
                break;
            }
        }
        row = ogR;
        col = ogC;

        while(row > 1 && col > 1){
            row--;
            col--;
            ChessPosition other_pos = new ChessPosition(row, col);
            ChessPiece other_piece = board.getPiece(other_pos);
            if(other_piece == null){
                moves.add(new ChessMove(ogPos, other_pos));
            }
            else{
                if(other_piece.getTeamColor() != team){
                    moves.add(new ChessMove(ogPos, other_pos));
                }
                break;
            }
        }
    }

    public static Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition og_pos){
        Collection<ChessMove> moves = new ArrayList<>();
        straight(board, og_pos, moves);
        return moves;
    }
    public static Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition og_pos){
        Collection<ChessMove> moves = new ArrayList<>();
        diagonal(board, og_pos, moves);
        return moves;
    }
    public static Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition og_pos){
        Collection<ChessMove> moves = new ArrayList<>();
        straight(board, og_pos, moves);
        diagonal(board, og_pos, moves);
        return moves;
    }
    public static Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition og_pos){
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor team = board.getPiece(og_pos).getTeamColor();
        int og_r = og_pos.getRow();
        int og_c = og_pos.getColumn();

        int[][] pos_moves = {{1,1}, {1,0}, {1,-1}, {0,1}, {0,-1}, {-1,1}, {-1,0}, {-1,-1}};

        for(int i = 0; i <= 7; i++){
            int row = pos_moves[i][0] + og_r;
            int col = pos_moves[i][1] + og_c;

            if(1 <= row && row <= 8 && 1 <= col && col <= 8){
                ChessPosition other_pos = new ChessPosition(row, col);
                ChessPiece other_piece = board.getPiece(other_pos);
                if(other_piece == null){
                    moves.add(new ChessMove(og_pos, other_pos));
                }
                else{
                    if(other_piece.getTeamColor() != team){
                        moves.add(new ChessMove(og_pos, other_pos));
                    }
                }
            }
        }

        return moves;
    }
    public static Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition og_pos){
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor team = board.getPiece(og_pos).getTeamColor();
        int og_r = og_pos.getRow();
        int og_c = og_pos.getColumn();

        int[][] pos_moves = {{1,2}, {2,1}, {1,-2}, {2, -1}, {-1,2}, {-2,1}, {-1,-2}, {-2, -1}};

        for(int i = 0; i <= 7; i++){
            int row = pos_moves[i][0] + og_r;
            int col = pos_moves[i][1] + og_c;

            if(1 <= row && row <= 8 && 1 <= col && col <= 8){
                ChessPosition other_pos = new ChessPosition(row, col);
                ChessPiece other_piece = board.getPiece(other_pos);
                if(other_piece == null){
                    moves.add(new ChessMove(og_pos, other_pos));
                }
                else{
                    if(other_piece.getTeamColor() != team){
                        moves.add(new ChessMove(og_pos, other_pos));
                    }
                }
            }
        }

        return moves;
    }
    public static Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition og_pos){
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor team = board.getPiece(og_pos).getTeamColor();
        int og_r = og_pos.getRow();
        int og_c = og_pos.getColumn();

        int start = switch(team){
            case WHITE -> 2;
            case BLACK -> 7;
        };
        int forward = switch(team){
            case WHITE -> 1;
            case BLACK -> -1;
        };

        int row = og_r;
        int col = og_c;

        //Forward logic
        ChessPosition other_pos = new ChessPosition(row + forward, col);
        ChessPiece other_piece = board.getPiece(other_pos);
        if(other_piece == null){
            if(row == start + 5*forward){
                moves.add(new ChessMove(og_pos, other_pos, ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(og_pos, other_pos, ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(og_pos, other_pos, ChessPiece.PieceType.QUEEN));
                moves.add(new ChessMove(og_pos, other_pos, ChessPiece.PieceType.KNIGHT));
            }
            else{
                if(row == start){
                    ChessPosition jump_pos = new ChessPosition(row + 2*forward, col);
                    ChessPiece jump_piece = board.getPiece(jump_pos);
                    if(jump_piece == null){
                        moves.add(new ChessMove(og_pos, jump_pos));
                    }
                }
                moves.add(new ChessMove(og_pos, other_pos));
            }
        }

        // Right diagonal
        if(col < 8){
            ChessPosition rig_pos = new ChessPosition(row + forward, col + 1);
            ChessPiece rig_piece = board.getPiece(rig_pos);
            if(rig_piece != null && rig_piece.getTeamColor() != team){
                if(row == start + 5*forward){
                    moves.add(new ChessMove(og_pos, rig_pos, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(og_pos, rig_pos, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(og_pos, rig_pos, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(og_pos, rig_pos, ChessPiece.PieceType.KNIGHT));
                }
                else{
                    moves.add(new ChessMove(og_pos, rig_pos));
                }
            }
        }

        // Left diagonal
        if(col > 1){
            ChessPosition lef_pos = new ChessPosition(row + forward, col - 1);
            ChessPiece lef_piece = board.getPiece(lef_pos);
            if(lef_piece != null && lef_piece.getTeamColor() != team){
                if(row == start + 5*forward){
                    moves.add(new ChessMove(og_pos, lef_pos, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(og_pos, lef_pos, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(og_pos, lef_pos, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(og_pos, lef_pos, ChessPiece.PieceType.KNIGHT));
                }
                else{
                    moves.add(new ChessMove(og_pos, lef_pos));
                }
            }
        }
        return moves;
    }
}
