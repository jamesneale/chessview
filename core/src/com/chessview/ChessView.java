package com.chessview;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.chessview.screen.ChessViewScreen;
import com.chessview.screen.SplashScreen;

public class ChessView extends Game {
	
	private ShapeRenderer shape_renderer_;
	private SpriteBatch sprite_batch_;
	private TextureAtlas atlas_;
	
	@Override
	public void create() {
		
		
		this.shape_renderer_ = new ShapeRenderer();
		this.sprite_batch_ = new SpriteBatch();
		
		this.atlas_ = new TextureAtlas(Gdx.files.internal("atlas.atlas"));
		
		this.setScreen(new SplashScreen(this));
		
		
	}

	public ShapeRenderer shape_renderer() {
		return this.shape_renderer_;
	}
	
	public SpriteBatch sprite_back() {
		return this.sprite_batch_;
	}
	
	public TextureAtlas atlas() {
		return this.atlas_;
	}
	
}
