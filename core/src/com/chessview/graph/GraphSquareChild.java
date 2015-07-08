package com.chessview.graph;

import com.badlogic.gdx.math.Rectangle;

public class GraphSquareChild {
	public GraphSquare graph;
	public Rectangle virtual_position;
	
	public GraphSquareChild(GraphSquare graph, Rectangle virtual_position) {
		this.graph = graph;
		this.virtual_position = virtual_position;
	}
} 
