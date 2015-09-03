package com.chessrender.drawableboard;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.chessrender.ChessRenderer;

public abstract class DrawableChessBoard {
	
	protected void render_piece(SpriteBatch sb, float x, float y, float square_size, TextureRegion[] pieces, byte offset, byte name) {
		sb.draw(pieces[name], x + square_size * (offset%8), y + square_size * (offset/8), square_size, square_size);
	}

	public abstract void render(ChessRenderer cr, float x, float y, float square_size);

	public void renderLine(ChessRenderer cr, float x, float y, float square_size){
		
	}
}
