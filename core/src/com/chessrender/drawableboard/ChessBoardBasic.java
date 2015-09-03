package com.chessrender.drawableboard;

import java.util.ArrayList;

import com.chessrender.ChessRenderer;

public class ChessBoardBasic extends DrawableChessBoard{
	ArrayList<Piece> pieces;
	
	public ChessBoardBasic(String FEN) {
		
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

	@Override
	public void render(ChessRenderer cr, float x, float y, float square_size) {
		for(Piece p : this.pieces) {
			cr.getSpriteBatch().draw(p.white?cr.white_pieces_[p.name]:cr.black_pieces_[p.name], 
					x + square_size * p.col, y + square_size * p.row, square_size, square_size);
		}
		
	}



	
}
