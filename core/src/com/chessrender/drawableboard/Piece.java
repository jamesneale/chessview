package com.chessrender.drawableboard;


public class Piece {
	public static final byte PAWN = 0;
	public static final byte ROOK = 1;
	public static final byte KNIGHT = 2;
	public static final byte BISHOP = 3;
	public static final byte QUEEN = 4;
	public static final byte KING = 5;
	
	public static final byte BLACK_OFFSET = 10;
	
	public byte row, col, name;
	public boolean white;
	
	@Override
	public String toString() {
		String n;
		switch(name) {
		case PAWN:
			n = "Pawn";
			break;
		case ROOK:
			n = "Rook";
			break;
		case KING:
			n = "King";
			break;
		case QUEEN:
			n = "Queen";
			break;
		case BISHOP:
			n = "Bishop";
			break;
		case KNIGHT:
			n = "Knight";
			break;
		default:
			n = "";
		}
		return (white?"White ":"Black ") + n + " " + row + " " + col;
	}
}
