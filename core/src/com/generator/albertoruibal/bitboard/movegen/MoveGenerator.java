package com.generator.albertoruibal.bitboard.movegen;

import com.generator.albertoruibal.bitboard.Board;

public interface MoveGenerator {

	int generateMoves(Board board, int moves[], int index);

}