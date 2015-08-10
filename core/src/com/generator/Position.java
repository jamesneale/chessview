package com.generator;

import java.util.ArrayList;

public class Position {
	
	private char[][] pieces;
	
	private GameState gs;
	
	private GameState previous_gs;
	private Move previous_move;
	
	public Position(String FEN) {
		String[] FEN_fields = FEN.split(" ");	
		pieces = GetPieces(FEN_fields[0]);
		
		gs = new GameState(FEN_fields);
		
		previous_gs = null;
		previous_move = null;
	}
	
	public static ArrayList<String> GenerateMovesFrom(String FEN) {
		Position position = new Position(FEN);
		
		ArrayList<String> validated_moves = new ArrayList<String>();
		
		ArrayList<Move> PLmoves = position.generatePLMoves();
		
		for(Move move : PLmoves) {
			if(position.attemptMove(move)) {
				validated_moves.add(position.toFEN() + ":" + move.toString());
				position.undoLastMove();
			}
		}
		
		return validated_moves;
	}
	
	
	public ArrayList<Move> generatePLMoves() {
		
		ArrayList<Move> moves = new ArrayList<Move>();
		
		for(int i = 0; i < pieces.length; ++i) {
			for(int j = 0; j < pieces[i].length; ++j) {
				if(Character.isAlphabetic(pieces[i][j])) {
					if(gs.white_turn) {
						if(Character.isUpperCase(pieces[i][j])) {
							getMovesForPiece(moves, i, j);
						}
					} else if(Character.isLowerCase(pieces[i][j])) {
						getMovesForPiece(moves, i, j);
					}
				}
			}
		}
		
		return moves;
	}
	
	private void getMovesForPiece(ArrayList<Move> moves, int i, int j) {
		char piece = Character.toLowerCase(pieces[i][j]);
		switch(piece) {
		case 'p' :
			getMovesForPawn(moves, i, j);
			break;
		case 'n':
			getMovesForKnight(moves, i, j);
			break;
		case 'b':
			getMovesForBishop(moves, i, j);
			break;
		case 'r':
			getMovesForRook(moves, i, j);
			break;
		case 'q':
			getMovesForQueen(moves, i, j);
			break;
		case 'k':
			getMovesForKing(moves, i, j);
			break;
		}
	}
	
	private void getMovesForPawn(ArrayList<Move> moves, int i, int j) {
		if(Character.isUpperCase(pieces[i][j])) {
			if(testAndAddQuietMove(moves, i, j, i-1, j) && i == 6) {
				testAndAddQuietMove(moves, i, j, i-2, j);
			}
			testAndAddLoudMove(moves, i, j, i-1, j-1);
			testAndAddLoudMove(moves, i, j, i-1, j+1);
		} else {	
			if(testAndAddQuietMove(moves, i, j, i+1, j) && i == 1) {
				testAndAddQuietMove(moves, i , j, i + 2, j);
			}
			testAndAddLoudMove(moves, i, j, i+1, j-1);
			testAndAddLoudMove(moves, i, j, i+1, j+1);
		}
	}
		
	private void getMovesForKing(ArrayList<Move> moves, int i, int j) {
			testAndAddMove(moves, i, j, i-1, j);
			testAndAddMove(moves, i, j, i, j-1);
			testAndAddMove(moves, i, j, i+1, j);
			testAndAddMove(moves, i, j, i, j+1);
	}
	
	
	private void getMovesForQueen(ArrayList<Move> moves, int i, int j) {
		getMovesForRook(moves, i, j);
		getMovesForBishop(moves, i, j);
	}
	
	private void getMovesForKnight(ArrayList<Move> moves, int i, int j) {
		testAndAddMove(moves, i, j, i-2, j-1);
		testAndAddMove(moves, i, j, i+2, j-1);
		testAndAddMove(moves, i, j, i-1, j-2);
		testAndAddMove(moves, i, j, i+1, j-2);
		testAndAddMove(moves, i, j, i-2, j+1);
		testAndAddMove(moves, i, j, i+2, j+1);
		testAndAddMove(moves, i, j, i-1, j+2);
		testAndAddMove(moves, i, j, i+1, j+2);
	}
	
	private void getMovesForBishop(ArrayList<Move> moves, int i, int j) {
		testAndAddRay(moves, i, j,  1,  1);
		testAndAddRay(moves, i, j, -1,  1);
		testAndAddRay(moves, i, j,  1, -1);
		testAndAddRay(moves, i, j, -1, -1);
	}
	
	private void getMovesForRook(ArrayList<Move> moves, int i, int j) {
		testAndAddRay(moves, i, j,  1,  0);
		testAndAddRay(moves, i, j, -1,  0);
		testAndAddRay(moves, i, j,  0, -1);
		testAndAddRay(moves, i, j,  0,  1);
	}
	
	private void testAndAddRay(ArrayList<Move> moves, int sr, int sc, int row_dir, int col_dir) {
		int tr = sr + row_dir;
		int tc = sc + col_dir;
		while(testAndAddMove(moves, sr, sc, tr, tc)){
			tr += row_dir;
			tc += col_dir;
		}
	}
	
	private boolean testAndAddMove(ArrayList<Move> moves, int sr, int sc, int tr, int tc) {
		if(tr < 0 || tr > 7 || tc < 0 || tc > 7) {
			return false;
		}
		if(Character.isAlphabetic(pieces[tr][tc])) {
			if(Character.isUpperCase(pieces[tr][tc])) {
				if(Character.isUpperCase(pieces[sr][sc])) {
					return false;
				}
			} else {
				if(Character.isLowerCase(pieces[sr][sc])) {
					return false;
				}
			}
		}
		moves.add(new Move(sr, sc, tr, tc, pieces[sr][sc], pieces[tr][tc]));
		return true;
	}
	
	private boolean testAndAddQuietMove(ArrayList<Move> moves, int sr, int sc, int tr, int tc) {
		if(tr < 0 || tr > 7 || tc < 0 || tc > 7) {
			return false;
		}
		if(Character.isAlphabetic(pieces[tr][tc])) {
			return false;
		}
			
		moves.add(new Move(sr, sc, tr, tc, pieces[sr][sc], pieces[tr][tc]));
		return true;
	}
	
	private boolean testAndAddLoudMove(ArrayList<Move> moves, int sr, int sc, int tr, int tc) {
		if(tr < 0 || tr > 7 || tc < 0 || tc > 7) {
			return false;
		}
		if(!Character.isAlphabetic(pieces[tr][tc])) {
			return false;
		}
		if(Character.isUpperCase(pieces[tr][tc])) {
			if(Character.isUpperCase(pieces[sr][sc])) {
				return false;
			}
		} else {
			if(Character.isLowerCase(pieces[sr][sc])) {
				return false;
			}
		}
		
		moves.add(new Move(sr, sc, tr, tc, pieces[sr][sc], pieces[tr][tc]));
		return true;
	}
	
	
	public boolean attemptMove(Move move) {
		
		this.previous_gs = gs.clone();
		this.previous_move = move;
		
		move.makeMove(pieces);
		move.updateGameState(gs);
		
		return true;
	}
	
	public void undoLastMove() {
		if(this.previous_gs == null || this.previous_move == null) {
			return;
		}
		else {
			previous_move.undoMove(pieces);
			previous_move = null;
			
			this.gs = previous_gs;
			previous_gs = null;
		}
	}
	
	public String toFEN() {
		String FEN = "";
		int empty_space = 0;
		for(int i = 0;  i < pieces.length; ++i) {
			for(int j = 0; j < pieces[i].length; ++j) {
				if(!Character.isAlphabetic(pieces[i][j])) {
					++empty_space;
				} else {
					if(empty_space > 0) {
						FEN += empty_space + "";
						empty_space = 0;
					}
					FEN += pieces[i][j];
				}
			}
			if(empty_space > 0) {
				FEN += empty_space + "";
				empty_space = 0;
			}
			if(pieces.length - i > 1) FEN += '/';
		}
		
		FEN += " " + (gs.white_turn?'w':'b');
		return FEN;
	}
	
	private static char[][] GetPieces(String FEN_pieces) {
		int row = 0;
		int col = 0;
		
		char[][] pieces = new char[8][8];
		
		for(int i = 0; i < FEN_pieces.length();++i) {
			if(FEN_pieces.charAt(i) == '/') {
				++row;
				col = 0;
			} else if(Character.isDigit(FEN_pieces.charAt(i))) {
				col += FEN_pieces.charAt(i) - '0';
			} else {
				pieces[row][col] = FEN_pieces.charAt(i);
				++col;
			}
		}
		return pieces;
	}
	
	public static void main(String[] args) {
		Position pos = new Position("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2");
		
		System.out.println(pos.toFEN());
	}
}
