package com.chessview.graph.layout;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class LayoutManager {
	
	private static Layout[] layouts;
	
	private static int activeLayout = 0;
	private static boolean chessBoardOnly = false;
	
	
	public static Layout getLayoutInstance() {
		return layouts[activeLayout];
	}
	
	public static void setUpLayouts(TextureAtlas atlas) {
		layouts = new Layout[] {
				new GridLayout(atlas),
				new SpiralLayout(atlas),
				new PerfectSquareLayout(atlas)
		};
	}
	
	public TextureRegion getLayoutButton() {
		return layouts[activeLayout].getLayoutButton();
	}
	
	public static void nextLayout() {
		/*
		layouts[activeLayout].chessBoardOnly = false;
		activeLayout = (activeLayout+1)%layouts.length;*/
	}
	
	public static void toggleDataOnly() {
		chessBoardOnly = !chessBoardOnly;
	}

	public static void disableDataOnly() {
		chessBoardOnly = false;
	}
	
	public static void enableDataOnly() {
		chessBoardOnly = true;
	}

	public static boolean dataOnly() {
		return chessBoardOnly;
	}

}
