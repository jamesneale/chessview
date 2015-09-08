package com.chessview.graph.layout;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.chessview.graph.chess.ChessGraphSquare;

public class GridLayout extends Layout {
	
	private static final float SPACING = 0.01f;
	
	private ArrayList<Integer> maxStorable;
	private ArrayList<ArrayList<Rectangle>> layouts;
	
	public GridLayout(TextureAtlas atlas) {
		super(atlas.findRegion("gridLayout"));
		this.setUpLayouts();
	}
	
	private void setUpLayouts() {
		layouts = new ArrayList<ArrayList<Rectangle>>();
		maxStorable = new ArrayList<Integer>();
		
		int totalActual = 0;
		int gridCount = 3;
		while(totalActual < Layout.MIN_LAYOUTS) {
			int index = 0;
			totalActual = 0;
			int totalPossible = gridCount * gridCount;
			
			float gridSize = 1.0f/gridCount;
			
			ArrayList<Rectangle> curLayout = new ArrayList<Rectangle>();
			
			while(index < totalPossible) {
				int col = index/gridCount;
				int row = index%gridCount;
				
				row = gridCount/2 + (row%2==0?row/2:-(1 + row/2));
				col = row<(gridCount/2)?col:gridCount-(col+1);
				
				
				Rectangle vp = new Rectangle(
						col * gridSize + SPACING/2, 
						row * gridSize + SPACING/2, 
						gridSize - SPACING, gridSize - SPACING);
				
				if(!vp.overlaps(Layout.DEFAULT_CHESSBOARD_REGION)) {
					++totalActual;
					curLayout.add(vp);
				}
				
				maxStorable.add(totalActual);
				layouts.add(curLayout);
				++index;
			}
			
			
			++gridCount;
		}
		
		
	}
	
	@Override
	public Rectangle getVirtualPosition(int max, int index) {
		
		int layout = 0;
		while(this.maxStorable.get(layout) < max) {
			++layout;
		}
		return this.layouts.get(layout).get(index);
	}

	@Override
	public Rectangle getChessBoardPosition(int i) {
		return Layout.DEFAULT_CHESSBOARD_REGION;
	}
}
