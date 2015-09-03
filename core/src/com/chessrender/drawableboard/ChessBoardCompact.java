package com.chessrender.drawableboard;

import com.chessrender.ChessRenderer;

public class ChessBoardCompact extends DrawableChessBoard {
	byte[] piece_offset;
	byte[] piece_type;
	
	public ChessBoardCompact(String FEN) {
		
		piece_offset = new byte[32];
		piece_type = new byte[32];
		
		for(int i = 0; i < 32; ++i) {
			piece_offset[i] = -1;
		}
		
		byte row = 7;
		byte col = 0;
		int cur_piece = 0;
		for(int i = 0; i < FEN.length();++i) {
			if(FEN.charAt(i) == ' ') {
				break;
			} else if(FEN.charAt(i) == '/') {
				--row;
				col = 0;
			} else if(Character.isDigit(FEN.charAt(i))) {
				col += FEN.charAt(i) - '0';
			} else {
				piece_offset[cur_piece] = (byte)(row*8 + col);
				char c = FEN.charAt(i);
				
				if(Character.isLowerCase(c)) {
					piece_type[cur_piece] = 10;
				} else {
					piece_type[cur_piece] = 0;
				}
				c = Character.toUpperCase(c);
				switch(c) {
				case 'P': 
					piece_type[cur_piece] += Piece.PAWN;
					break;
				case 'R': 
					piece_type[cur_piece] += Piece.ROOK;
					break;
				case 'B': 
					piece_type[cur_piece] += Piece.BISHOP;
					break;
				case 'N': 
					piece_type[cur_piece] += Piece.KNIGHT;
					break;
				case 'K': 
					piece_type[cur_piece] += Piece.KING;
					break;
				case 'Q': 
					piece_type[cur_piece] += Piece.QUEEN;
					break;
				}
				++cur_piece;
			}	
		}
	}
	
	public byte getPieceName(int i) {
		if(piece_type[i] > 9) {
			return (byte)(piece_type[i] - 10);
		}
		return piece_type[i];
	}
	
	private boolean isWhite(int i) {
		return piece_type[i] < 10;
	}
	
	private int getRow(int i) {
		return piece_offset[i]/8;
	}
	
	private int getCol(int i) {
		return piece_offset[i]%8;
	}
	
	private boolean hasPiece(int i) {
		return piece_offset[i] > -1;
	}
	@Override
	public void render(ChessRenderer cr, float x, float y, float square_size) {
		
		int i = 0;
		while(i < 32) {
			if(!this.hasPiece(i)) {
				return;
			}
			if(this.isWhite(i)) {
				this.render_piece(cr.getSpriteBatch(), x, y, square_size, cr.white_pieces_, piece_offset[i], this.getPieceName(i));
			} else {
				this.render_piece(cr.getSpriteBatch(), x, y, square_size, cr.black_pieces_, piece_offset[i], this.getPieceName(i));
			}
			++i;
		}
		
	}

	

	
	
}
