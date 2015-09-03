package com.movelist;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class MoveList {
	public static final float VERTICAL_SPACING = 5;
	
	private ArrayList<GlyphLayout> moveList;
	private Rectangle location;
	private BitmapFont font;
	
	public MoveList(Rectangle location, BitmapFont font) {
		this.location = location;
		this.font = font;
		
		moveList = new ArrayList<GlyphLayout>();
	}
	
	public void pop(){
		moveList.remove(moveList.size()-1);
	}
	
	public void push(String move) {
		moveList.add(new GlyphLayout(font, ((moveList.size()+1) + ". " + move)));
	}
	
	public void render(SpriteBatch batch) {
		float offset = location.y + location.height;
		for(GlyphLayout move : moveList) {
			font.draw(batch, move, location.x, offset);
			offset -= move.height + VERTICAL_SPACING;
		}
	}
	
}
