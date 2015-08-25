package com.generator;

public class Move {
	public int start_row, start_col;
	public int end_row, end_col;
	
	public char moved_piece, taken_piece;
	
	public Move(int sr, int sc, int er, int ec, char mp, char tp) {
		this.start_row = sr;
		this.start_col = sc;
		this.end_row = er;
		this.end_col = ec;
		this.moved_piece = mp;
		this.taken_piece = tp;
	}
	
	public Move clone() {
		return new Move(this.start_row, this.start_col, this.end_row, this.end_col,
				this.moved_piece, this.taken_piece);
	}
	
	public void updateGameState(GameState gs) {
		gs.white_turn = !gs.white_turn;
	}
	
	public void makeMove(char[][] pieces) {
		pieces[start_row][start_col] = ' ';
		pieces[end_row][end_col] = moved_piece;
	}
	
	public void undoMove(char[][] pieces) {
		pieces[start_row][start_col] = moved_piece;
		pieces[end_row][end_col] = taken_piece;
	}
	
	public String toString() {
		return (char)('A'+start_row) + start_col + " " + (char)('A'+end_row) + end_col;
	}
}
