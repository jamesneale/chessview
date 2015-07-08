package com.chessview;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.chessview.screen.AbstractScreen;
import com.chessview.screen.ChessViewScreen;

public class ChessView extends Game {
	
	private ShapeRenderer shape_renderer_;
	
	@Override
	public void create () {
		this.shape_renderer_ = new ShapeRenderer();
		
		this.setScreen(new ChessViewScreen(this));
	}

	public ShapeRenderer shape_renderer() {
		return this.shape_renderer_;
	}
	
	public void unproject(Vector3 screen_coord) {
		
	}
}
