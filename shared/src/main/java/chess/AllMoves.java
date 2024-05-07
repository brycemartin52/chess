package chess;

import java.util.Collection;
import java.util.Objects;
import java.util.ArrayList;

public class AllMoves {


    private static void straight(ChessBoard board, int row, int col, Collection<ChessMove> moves){
        int og_row = row;
        int og_col = col;
        ChessPosition pos = new ChessPosition(row, col);
        ChessGame.TeamColor team = board.getPiece(new ChessPosition(row, col)).getTeamColor();

        while(row < 8){
            row += 1;
            ChessPosition new_pos = new ChessPosition(row, col);
            ChessPiece other_piece = board.getPiece(new_pos);
            if(other_piece == null){
                moves.add(new ChessMove(pos, new_pos));
            }
            else{
                ChessGame.TeamColor other_team = other_piece.getTeamColor();
                if(team != other_team){
                    moves.add(new ChessMove(pos, new_pos));
                }
                break;
            }
        }

        row = og_row;
        while(row > 1){
            row -= 1;
            ChessPosition new_pos = new ChessPosition(row, col);
            ChessPiece other_piece = board.getPiece(new_pos);
            if(other_piece == null){
                moves.add(new ChessMove(pos, new_pos));
            }
            else{
                ChessGame.TeamColor other_team = other_piece.getTeamColor();
                if(team != other_team){
                    moves.add(new ChessMove(pos, new_pos));
                }
                break;
            }
        }
        row = og_row;

        while(col < 8){
            col += 1;
            ChessPosition new_pos = new ChessPosition(row, col);
            ChessPiece other_piece = board.getPiece(new_pos);
            if(other_piece == null){
                moves.add(new ChessMove(pos, new_pos));
            }
            else{
                ChessGame.TeamColor other_team = other_piece.getTeamColor();
                if(team != other_team){
                    moves.add(new ChessMove(pos, new_pos));
                }
                break;
            }
        }
        col = og_col;

        while(col > 1){
            col -= 1;
            ChessPosition new_pos = new ChessPosition(row, col);
            ChessPiece other_piece = board.getPiece(new_pos);
            if(other_piece == null){
                moves.add(new ChessMove(pos, new_pos));
            }
            else{
                ChessGame.TeamColor other_team = other_piece.getTeamColor();
                if(team != other_team){
                    moves.add(new ChessMove(pos, new_pos));
                }
                break;
            }
        }
    }

    private static void diagonal(ChessBoard board, int og_row, int og_col, Collection<ChessMove> moves){
        int row = og_row;
        int col = og_col;
        ChessPosition pos = new ChessPosition(row, col);
        ChessGame.TeamColor team = board.getPiece(new ChessPosition(row, col)).getTeamColor();

        while(row < 8 && col < 8){
            row += 1;
            col += 1;
            ChessPosition new_pos = new ChessPosition(row, col);
            ChessPiece other_piece = board.getPiece(new_pos);
            if(other_piece == null){
                moves.add(new ChessMove(pos, new_pos));
            }
            else{
                ChessGame.TeamColor other_team = other_piece.getTeamColor();
                if(team != other_team){
                    moves.add(new ChessMove(pos, new_pos));
                }
                break;
            }
        }

        row = og_row;
        col = og_col;
        while(row > 1 && col < 8){
            row -= 1;
            col += 1;
            ChessPosition new_pos = new ChessPosition(row, col);
            ChessPiece other_piece = board.getPiece(new_pos);
            if(other_piece == null){
                moves.add(new ChessMove(pos, new_pos));
            }
            else{
                ChessGame.TeamColor other_team = other_piece.getTeamColor();
                if(team != other_team){
                    moves.add(new ChessMove(pos, new_pos));
                }
                break;
            }
        }
        row = og_row;
        col = og_col;

        while(row < 8 && col > 1){
            row += 1;
            col -= 1;
            ChessPosition new_pos = new ChessPosition(row, col);
            ChessPiece other_piece = board.getPiece(new_pos);
            if(other_piece == null){
                moves.add(new ChessMove(pos, new_pos));
            }
            else{
                ChessGame.TeamColor other_team = other_piece.getTeamColor();
                if(team != other_team){
                    moves.add(new ChessMove(pos, new_pos));
                }
                break;
            }
        }
        row = og_row;
        col = og_col;

        while(row > 1 && col > 1){
            row -= 1;
            col -= 1;
            ChessPosition new_pos = new ChessPosition(row, col);
            ChessPiece other_piece = board.getPiece(new_pos);
            if(other_piece == null){
                moves.add(new ChessMove(pos, new_pos));
            }
            else{
                ChessGame.TeamColor other_team = other_piece.getTeamColor();
                if(team != other_team){
                    moves.add(new ChessMove(pos, new_pos));
                }
                break;
            }
        }
    }

    public static Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition pos){
        Collection<ChessMove> moves = new ArrayList<>();
        int cur_row = pos.getRow();
        int cur_col = pos.getColumn();
        ChessGame.TeamColor team = board.getPiece(pos).getTeamColor();
        int forward = switch(team){
            case WHITE -> 1;
            case BLACK -> -1;
        };

        int start = switch(team){
            case WHITE -> 2;
            case BLACK -> 7;
        };

        // Checks if in front is empty, adds if it's the case
        ChessPosition new_pos = new ChessPosition(cur_row + forward, cur_col);
        ChessPiece other_piece = board.getPiece(new_pos);

        if(other_piece == null){
            if(cur_row == start + 5 * forward){
                moves.add(new ChessMove(pos, new_pos, ChessPiece.PieceType.QUEEN));
                moves.add(new ChessMove(pos, new_pos, ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(pos, new_pos, ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(pos, new_pos, ChessPiece.PieceType.ROOK));
            }

            else{
                if(cur_row == start && board.getPiece(new ChessPosition(cur_row + 2 * forward, cur_col)) == null) {
                    moves.add(new ChessMove(pos, new ChessPosition(cur_row + 2 * forward, cur_col)));
                }
                moves.add(new ChessMove(pos, new_pos));
            }
        }

        if(cur_col > 1){
            ChessPosition lef_pos = new ChessPosition(cur_row + forward, cur_col - 1);
            ChessPiece lef_piece = board.getPiece(lef_pos);

            if(lef_piece != null && lef_piece.getTeamColor() != team){
                if(cur_row == start + 5 * forward){
                    moves.add(new ChessMove(pos, lef_pos, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(pos, lef_pos, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(pos, lef_pos, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(pos, lef_pos, ChessPiece.PieceType.ROOK));
                }
                else{
                    moves.add(new ChessMove(pos, lef_pos));
                }
            }
        }

        if(cur_col < 8){
            ChessPosition rig_pos = new ChessPosition(cur_row + forward, cur_col + 1);
            ChessPiece rig_piece = board.getPiece(rig_pos);

            if(rig_piece != null && rig_piece.getTeamColor() != team){
                if(cur_row == start + 5 * forward){
                    moves.add(new ChessMove(pos, rig_pos, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(pos, rig_pos, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(pos, rig_pos, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(pos, rig_pos, ChessPiece.PieceType.ROOK));
                }
                else{
                    moves.add(new ChessMove(pos, rig_pos));
                }

            }
        }
        return moves;
    }

    public static Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition pos){
        Collection<ChessMove> moves = new ArrayList<>();
        int og_row = pos.getRow();
        int og_col = pos.getColumn();
        ChessGame.TeamColor team = board.getPiece(new ChessPosition(og_row, og_col)).getTeamColor();

        int[][] pos_moves = {
                {1, 2}, {2, 1}, {1, -2}, {2, -1},
                {-1, 2}, {-2, 1}, {-1, -2}, {-2, -1}
        };

        for(int i = 0; i <= 7; i++){
            int row = pos_moves[i][0] + og_row;
            int col = pos_moves[i][1] + og_col;
            if(1 <= row && row <= 8 && 1 <= col && col <= 8){
                ChessPosition new_pos = new ChessPosition(row, col);
                ChessPiece other_piece = board.getPiece(new_pos);
                if(other_piece == null){
                    moves.add(new ChessMove(pos, new_pos));
                }
                else{
                    ChessGame.TeamColor other_team = other_piece.getTeamColor();
                    if(team != other_team){
                        moves.add(new ChessMove(pos, new_pos));
                    }
                }
            }
        }
        return moves;
    }

    public static Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition pos){
        Collection<ChessMove> moves = new ArrayList<>();
        int cur_row = pos.getRow();
        int cur_col = pos.getColumn();
        diagonal(board, cur_row, cur_col, moves);
        return moves;
    }

    public static Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition pos){
        Collection<ChessMove> moves = new ArrayList<>();
        int cur_row = pos.getRow();
        int cur_col = pos.getColumn();
        straight(board, cur_row, cur_col, moves);
        return moves;
    }

    public static Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition pos){
        Collection<ChessMove> moves = new ArrayList<>();
        int cur_row = pos.getRow();
        int cur_col = pos.getColumn();
        straight(board, cur_row, cur_col, moves);
        diagonal(board, cur_row, cur_col, moves);
        return moves;
    }

    public static Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition pos){
        Collection<ChessMove> moves = new ArrayList<>();
        int og_row = pos.getRow();
        int og_col = pos.getColumn();
        ChessGame.TeamColor team = board.getPiece(new ChessPosition(og_row, og_col)).getTeamColor();

        int[][] pos_moves = {
                {1, 1}, {1, 0}, {1, -1}, {0, 1},
                {0, -1}, {-1,1}, {-1,0}, {-1,-1}
        };

        for(int i = 0; i <= 7; i++){
            int row = pos_moves[i][0] + og_row;
            int col = pos_moves[i][1] + og_col;
            if(1 <= row && row <= 8 && 1 <= col && col <= 8){
                ChessPosition new_pos = new ChessPosition(row, col);
                ChessPiece other_piece = board.getPiece(new_pos);
                if(other_piece == null){
                    moves.add(new ChessMove(pos, new_pos));
                }
                else{
                    ChessGame.TeamColor other_team = other_piece.getTeamColor();
                    if(team != other_team){
                        moves.add(new ChessMove(pos, new_pos));
                    }
                }
            }
        }
        return moves;
    }
}
