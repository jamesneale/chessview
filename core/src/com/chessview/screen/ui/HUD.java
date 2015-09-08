package com.chessview.screen.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.chessview.data.DataRetrieval;
import com.chessview.graph.layout.LayoutManager;

public class HUD {
	private Vector2 position;
	private TextureRegion background;
	
	private MoveList moveList;
	
	private long previousNodeCount;
	private GlyphLayout nodeCountLayout;
	
	private BitmapFont font;
	
	private static final Rectangle layoutButton = new Rectangle(22,28,245,65);
	private static final Rectangle boardOnlyButton = new Rectangle(22, 980, 245, 80);
	
	
	public HUD(float x, float y, TextureAtlas atlas, BitmapFont font) {
		position = new Vector2(x,y);
		this.background = atlas.findRegion("overlay");
		
		this.font = font;
		
		this.nodeCountLayout = new GlyphLayout();
		this.previousNodeCount = 0;
		
		// TODO make this relative
		moveList = new MoveList(new Rectangle(-910, -250, 250, 620), this.font);
	}
	
	public boolean contains(float x, float y) {
		return  x > position.x && y > position.y && 
			   (x - position.x < background.getRegionWidth()) &&
			   (y - position.y < background.getRegionHeight());				
	}
	
	public void render(SpriteBatch batch, float delta) {
		
		// Prepare text
		if(this.previousNodeCount != DataRetrieval.getNodeCount()) {
			previousNodeCount = DataRetrieval.getNodeCount();
			nodeCountLayout.setText(font, previousNodeCount + "");
		}
		
		batch.draw(background, position.x, position.y);
		
		//TODO make this relative also store is somewhere
		this.font.draw(batch, this.nodeCountLayout, -720-this.nodeCountLayout.width, -365);
		
		this.moveList.render(batch);
	}
	
	public void pushMove(String move) {
		this.moveList.push(move);
	}
	
	public void popMove() {
		this.moveList.pop();
	}
	
	public boolean touchUp() {
		LayoutManager.disableDataOnly();
		return false;
	}
	
	public boolean touchDown(float x, float y, int button) {
		if(!this.contains(x, y)) {
			return false;
		}
		
		x -= this.position.x;
		y -= this.position.y;
		
		if(HUD.layoutButton.contains(x, y)) {
			LayoutManager.nextLayout();
		} else if(HUD.boardOnlyButton.contains(x, y)) {
			LayoutManager.enableDataOnly();
		}
		
		return true;
	}
	
	

}
