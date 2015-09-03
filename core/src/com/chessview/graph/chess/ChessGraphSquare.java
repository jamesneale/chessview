package com.chessview.graph.chess;

import com.badlogic.gdx.math.Rectangle;
import com.chessrender.ChessRenderer;
import com.chessrender.drawableboard.ChessBoardBitBasic;
import com.chessview.data.BoardDataRetrieval;
import com.chessview.data.DataRequest;
import com.chessview.graph.GraphSquare;
import com.chessview.graph.GraphSquareChild;
import com.chessview.region.ROI;

public class ChessGraphSquare extends GraphSquare {

	private ChessRenderer cr;
	private ChessBoardBitBasic board;
	
	public static final Rectangle chessboard_region = new Rectangle(0.4f, 0.35f, 0.2f, 0.3f);
	
	public ChessGraphSquare(BoardDataRetrieval data_retreiver, ChessBoardBitBasic node_data, ChessRenderer cr) {
		super(data_retreiver);
		board = node_data;
		this.cr = cr;
	}

	@Override
	protected void render_node(Rectangle drawable_region) {
		Rectangle chessboard = new Rectangle(drawable_region.x + drawable_region.width * chessboard_region.x, 
											 drawable_region.y + drawable_region.height * chessboard_region.y, 
											 drawable_region.width * chessboard_region.width, 
											 drawable_region.height * chessboard_region.height);
		
		cr.RenderChessBoard(this.board, chessboard);
	}

	@Override
	protected GraphSquareChild make_child(Object node_data, Rectangle virtual_position) {
		return new GraphSquareChild(new ChessGraphSquare((BoardDataRetrieval)this.data_retreiver, (ChessBoardBitBasic)node_data, cr), virtual_position);
	}
	
	@Override
	protected boolean GenerateChildren() {
		return this.data_retreiver.data_requests_.offer(new DataRequest(this, board));
	}

	@Override
	protected void render_node(ROI region) {
		
		Rectangle chessboard = GetBoundingBox(region.kBoundingBox, chessboard_region, region.region_of_interest_);
		if(chessboard != null) {
			cr.RenderChessBoardSelected(this.board, chessboard);
		}
	}
	
	
	@Override
	public String toString() {
		return board.getMove();
	}

	

	
}
