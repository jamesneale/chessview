package com.chessview.graph.chess;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.chessview.data.DataRetrieval;
import com.chessview.graph.GraphSquare;
import com.chessview.graph.GraphSquareChild;
import com.chessview.region.ROI;

public class LineGraphSquare extends GraphSquare {

	private ShapeRenderer sr;
	
	public LineGraphSquare(DataRetrieval data_retreiver, String node_data, ShapeRenderer sr) {
		super(data_retreiver, node_data);
		this.sr = sr;
	}

	@Override
	protected void render_node(Rectangle drawable_region) {
		sr.rect(drawable_region.x, drawable_region.y, drawable_region.width, drawable_region.height);
	}

	@Override
	protected GraphSquareChild make_child(String node_data, Rectangle virtual_position) {
		return new GraphSquareChild(new LineGraphSquare(this.data_retreiver, node_data, sr), virtual_position);
	}

	@Override
	protected void render_node(ROI region) {
		// TODO Auto-generated method stub
		
	}

}
