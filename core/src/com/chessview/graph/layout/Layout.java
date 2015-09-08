package com.chessview.graph.layout;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public abstract class Layout {
	
	public static final int MIN_LAYOUTS = 218; 

	public static final Rectangle DEFAULT_CHESSBOARD_REGION = new Rectangle(0.4f, 0.35f, 0.2f, 0.3f);
	public static final Rectangle CHESSBOARD_ONLY_REGION = new Rectangle(0.2f, 0.05f, 0.6f, 0.9f);
	
	private TextureRegion layoutButton;
	
	public Layout(TextureRegion layoutButton) {
		this.layoutButton = layoutButton;
	}

	public TextureRegion getLayoutButton() {
		return layoutButton;
	}
	
	public Rectangle getVirtualPosition(int total, int index) {
		return null;
	}

	public abstract Rectangle getChessBoardPosition(int i);
	
	
}
