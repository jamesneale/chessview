package com.chessview.graph.chess;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.chessrender.ChessBoard;
import com.chessrender.ChessRenderer;
import com.chessview.data.DataRetrieval;
import com.chessview.graph.GraphSquare;
import com.chessview.graph.GraphSquareChild;
import com.chessview.region.ROI;

public class ChessGraphSquare extends GraphSquare {

	private ChessRenderer cr;
	private ChessBoard board;
	
	public static final Rectangle chessboard_region = new Rectangle(0.4f, 0.35f, 0.2f, 0.3f);
	
	public ChessGraphSquare(DataRetrieval data_retreiver, String node_data, ChessRenderer cr) {
		super(data_retreiver, node_data);
		board = new ChessBoard(node_data.split(":")[0]);
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
	protected GraphSquareChild make_child(String node_data, Rectangle virtual_position) {
		return new GraphSquareChild(new ChessGraphSquare(this.data_retreiver, node_data, cr), virtual_position);
	}

	@Override
	protected void render_node(ROI region) {
		
		Rectangle chessboard = GetBoundingBox(region.kBoundingBox, chessboard_region, region.region_of_interest_);
		if(chessboard != null) {
			cr.RenderSelectedChessBoard(this.board, chessboard);
		}
	}

	
}
