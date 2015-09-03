package com.chessview.data;

import java.util.ArrayList;

import com.chessrender.drawableboard.ChessBoardBitBasic;
import com.generator.albertoruibal.bitboard.Board;

public class BoardDataRetrieval extends DataRetrieval{
	private Board temp_board;
	
	public BoardDataRetrieval() {
		this.temp_board = new Board();
	}

	protected ArrayList<Object> GenerateNodes(Object data) {
		
		ChessBoardBitBasic board_data = (ChessBoardBitBasic)data;
		temp_board.setBoard(board_data);
		
		
		
		ArrayList<Object> boards = new ArrayList<Object>();
		boards.addAll(temp_board.getLegalBoards());
		
		return boards;
	}
}
