package com.chessview.graph.layout;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class SpiralLayout extends Layout {
	public SpiralLayout(TextureAtlas atlas) {
		super(atlas.findRegion("gridLayout"));
	}
}
