package com.generator;

public class GameState {
	public boolean white_turn;
	public int full_move;
	public int half_move;
	public boolean[] castling;
	public boolean en_passant;
	public int en_passant_row;
	public int en_passant_col;
	
	public GameState() {
		
	}
	public GameState(String[] split_fen) {
		System.out.println(split_fen[1]);
		white_turn = split_fen[1].equals("w");
		
		this.castling = new boolean[4];
	}
	
	public GameState clone() {
		GameState clone = new GameState();
		clone.white_turn = this.white_turn;
		clone.full_move = this.full_move;
		clone.half_move = this.half_move;
		
		clone.castling = new boolean[this.castling.length];
		for(int i = 0; i < this.castling.length; ++i) {
			clone.castling[i] = this.castling[i];
		}
		
		clone.en_passant = this.en_passant;
		clone.en_passant_row = this.en_passant_row;
		clone.en_passant_col = this.en_passant_row; 
		
		return clone;
	}
}
