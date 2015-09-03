package com.generator.albertoruibal.bitboard.movegen;

import com.generator.albertoruibal.bitboard.Board;
import com.generator.albertoruibal.bitboard.Move;
/**
 * Based on the class from the Open Source engine Carballo
 * https://github.com/albertoruibal/carballo
 * originally written by Alberto Alonso Ruibal.
 * 
 * Modified for use in ChessViewer by James Neale 2015
 */
public class LegalMoveGenerator extends MagicMoveGenerator {

	/**
	 * Get only LEGAL moves testing with doMove
	 * The moves are returned with the check flag set
	 */
	@Override
	public int generateMoves(Board board, int moves[], int index) {
		int lastIndex = super.generateMoves(board, moves, index);
		int j = index;
		for (int i = 0; i < lastIndex; i++) {
			if (board.doMove(moves[i], true, false)) {
				moves[j++] = board.getCheck() ? moves[i] | Move.CHECK_MASK : moves[i];
				board.undoMove();
			}
		}
		return j;
	}
}