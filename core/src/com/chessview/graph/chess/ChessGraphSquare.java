package com.chessview.graph.chess;

import chessrender.ChessRenderer;

import com.badlogic.gdx.math.Rectangle;
import com.chessview.data.DataRetrieval;
import com.chessview.graph.GraphSquare;
import com.chessview.graph.GraphSquareChild;
import com.chessview.region.ROI;

public class ChessGraphSquare extends GraphSquare {

	private ChessRenderer cr;
	
	public ChessGraphSquare(DataRetrieval data_retreiver, String node_data, ChessRenderer cr) {
		super(data_retreiver, node_data);
		this.cr = cr;
	}

	@Override
	protected void render_node(Rectangle drawable_region) {
		cr.RenderChessBoard(this.kNodeData, drawable_region);
	}

	@Override
	protected GraphSquareChild make_child(String node_data, Rectangle virtual_position) {
		return new GraphSquareChild(new ChessGraphSquare(this.data_retreiver, node_data, cr), virtual_position);
	}

	@Override
	protected void render_node(ROI region) {
		// TODO Auto-generated method stub
		
	}

}
