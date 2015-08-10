package chessrender;

import java.util.ArrayList;

public class ChessBoard {
	ArrayList<Piece> pieces;
	
	public ChessBoard(String FEN) {
		
		pieces = new ArrayList<Piece>();
		
		byte row = 7;
		byte col = 0;
		for(int i = 0; i < FEN.length();++i) {
			if(FEN.charAt(i) == ' ') {
				break;
			} else if(FEN.charAt(i) == '/') {
				--row;
				col = 0;
			} else if(Character.isDigit(FEN.charAt(i))) {
				col += FEN.charAt(i) - '0';
			} else {
				Piece p = new Piece();
				p.row = row;
				p.col = col;
				char c = FEN.charAt(i);
				if(Character.isLowerCase(c)) {
					p.white = false;
				} else {
					p.white = true;
				}
				c = Character.toUpperCase(c);
				switch(c) {
				case 'P': 
					p.name = Piece.PAWN;
					break;
				case 'R': 
					p.name = Piece.ROOK;
					break;
				case 'B': 
					p.name = Piece.BISHOP;
					break;
				case 'N': 
					p.name = Piece.KNIGHT;
					break;
				case 'K': 
					p.name = Piece.KING;
					break;
				case 'Q': 
					p.name = Piece.QUEEN;
					break;
					
				}
			//	System.out.println(p.toString());
				pieces.add(p);
				++col;
			}
			
			
		}
	}
	
	public static void main(String[] args) {
		String FEN = "R3bD/K ";
		ChessBoard cb = new ChessBoard(FEN);
		for(Piece p : cb.pieces) {
			System.out.println(p.toString());
		}
	}
}
