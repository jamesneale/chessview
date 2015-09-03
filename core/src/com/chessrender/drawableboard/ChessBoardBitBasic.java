package com.chessrender.drawableboard;

import com.badlogic.gdx.math.MathUtils;
import com.chessrender.ChessRenderer;
import com.generator.albertoruibal.bitboard.Board;
import com.generator.albertoruibal.bitboard.Move;
import com.generator.albertoruibal.bitboard.bb.BitboardUtils;

public class ChessBoardBitBasic extends DrawableChessBoard{
	public long whites = 0;
	public long blacks = 0;
	public long pawns = 0;
	public long rooks = 0;
	public long queens = 0;
	public long bishops = 0;
	public long knights = 0;
	public long kings = 0;
	public long flags = 0;
	
	public long[] castlingRooks = {0,0,0,0};
	
	int move = 0;
	
	public ChessBoardBitBasic(Board copy) {
		whites = copy.whites;
		blacks = copy.blacks;
		pawns = copy.pawns;
		rooks = copy.rooks;
		queens = copy.queens;
		bishops = copy.bishops;
		knights = copy.knights;
		kings = copy.kings;
		flags = copy.flags;
	}
	
	public ChessBoardBitBasic(Board copy, int move) {
		this(copy);
		this.move = move;
	}
	
	public byte getPieceAt(long square) {
		return ((pawns & square) != 0 ? Piece.PAWN : //
				((knights & square) != 0 ? Piece.KNIGHT : //
						((bishops & square) != 0 ? Piece.BISHOP : //
								((rooks & square) != 0 ? Piece.ROOK : //
										((queens & square) != 0 ? Piece.QUEEN : Piece.KING)))));
	}

	@Override
	public void renderLine(ChessRenderer cr, float x, float y, float square_size) {
		if(move == 0) {
			return;
		}
		
		int thickness = (int)(square_size / 10);
		x += square_size/2;
		y += square_size / 2;
		
		int fromOffset = Move.getFromIndex(move);
		int toOffset = Move.getToIndex(move);
		
		
		
		this.drawLine(cr, x + (fromOffset%8)*square_size, y + (fromOffset/8)*square_size, x + (toOffset%8)*square_size, y + (toOffset/8)*square_size, thickness);
		
	}
	@Override
	public void render(ChessRenderer cr, float x, float y, float square_size) {
		long i = BitboardUtils.A8;
		byte offset = 63;
		while (i != 0) {
			if((i & blacks) != 0) {
				this.render_piece(cr.getSpriteBatch(), x, y, square_size, cr.black_pieces_, offset, this.getPieceAt(i));	
			} else if((i & whites) != 0) {
				this.render_piece(cr.getSpriteBatch(), x, y, square_size, cr.white_pieces_, offset, getPieceAt(i));	
			}
			i >>>= 1;
			--offset;
		}
	}

	
	/*
	 * Credit for this method to: https://github.com/mattdesl/lwjgl-basics/wiki/Batching-Rectangles-and-Lines
	 * with slight modification
	 */
	void drawLine(ChessRenderer cr, float x1, float y1, float x2, float y2, int thickness) {
		float dx = x2-x1;
	    float dy = y2-y1;
	    float dist = (float)Math.sqrt(dx*dx + dy*dy);
	    float deg = (float)Math.atan2(dy, dx) * MathUtils.radDeg;
	    
	    if(dx == 0) {
	    	if(dy > 0) {
	    		x1 += thickness / 2;
	    	} else {
	    		x1 -= thickness / 2;
	    	}
	    }
	    cr.getSpriteBatch().draw(cr.getLineTexture(), x1, y1, 0, 0, 1, 1, dist, thickness, deg, true);
	}

	public String getMove() {
		if(move == 0) {
			return "";
		}
		return Move.toString(move);
	}
	
	
	public static void main(String[] args) {
		Board test = new Board();
		test.setFen(Board.FEN_START_POSITION);
		
		ChessBoardBitBasic test_board = new ChessBoardBitBasic(test);
		
		long i = BitboardUtils.A8;
		byte offset = 63;
		while (i != 0) {
			if((i & test_board.blacks) != 0) {
				System.out.println(offset%8 + " " + offset/8 + " " + test_board.getPieceAt(i));
			} else if((i & test_board.whites) != 0) {
				System.out.println(offset);
			}
			i >>>= 1;
			--offset;
		}
		
	}
}
