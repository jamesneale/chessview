package com.chessview.graph.layout;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class PerfectSquareLayout extends Layout {
	
	public PerfectSquareLayout(TextureAtlas atlas) {
		super(atlas.findRegion("gridLayout"));
	}

}
