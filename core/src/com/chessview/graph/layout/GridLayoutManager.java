package com.chessview.graph.layout;

import com.badlogic.gdx.math.Rectangle;
import com.chessview.graph.chess.ChessGraphSquare;

public class GridLayoutManager {
	
	public final int kMaxElements;
	
	public final int kGridSize;
	
	public final float kGridWidth;
	public final float kGridHeight;
	
	public final float kSpacing = 0.01f;
	
	private int cur_grid;
	
	public GridLayoutManager(int count) {
		kMaxElements = Math.min(count, 232);
		
		int grid_size = 4;
		while(grid_size * (grid_size-1) < count) {
			grid_size += 2;
		}
	
		kGridSize = grid_size;
		kGridWidth = 1.0f / kGridSize;
		kGridHeight = 1.0f / kGridSize;
		
		cur_grid = 0;
	}
	
	public Rectangle GetNext() {
	/*	if(cur_grid >= kMaxElements) {
			throw new IllegalArgumentException("Request for more than available grid space " + cur_grid + " max: " + kMaxElements);
		} */
		int cur_row = cur_grid / kGridSize;
		int cur_col = cur_grid % kGridSize;
		
		if(cur_col % 2 != 0) {
			cur_row = (kGridSize - 1) - cur_row;
		}
		
		++cur_grid;
		
		Rectangle virtual_position = new Rectangle(cur_col * kGridWidth + kSpacing, cur_row * kGridHeight +kSpacing, kGridWidth - kSpacing, kGridHeight - kSpacing);
		
		if(virtual_position.overlaps(ChessGraphSquare.chessboard_region)) {
			return GetNext(); 
		}
		return virtual_position;
	}
}
