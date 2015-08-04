package chessrender;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class ChessRenderer {
	
	private SpriteBatch sprite_batch_;
	
	private TextureAtlas atlas_;
	
	private TextureRegion chessBoard_;
	private TextureRegion[] blackPieces_;
	private TextureRegion[] whitePieces_;
	
	public ChessRenderer(TextureAtlas atlas, SpriteBatch sprite_batch) {
		this.atlas_ = atlas;
		this.sprite_batch_ = sprite_batch;
		
		this.chessBoard_ = atlas_.findRegion("chessboard");
	}
	
	
	public void RenderChessBoard(String fen, Rectangle inside) {
		
		sprite_batch_.draw(chessBoard_, inside.x+inside.width/4, inside.y+inside.height/4, inside.width/2, inside.height/2);
	}
	
	public void begin() {
		this.sprite_batch_.begin();
	}
	public void end() {
		this.sprite_batch_.end();
	}
	
	public void RenderChessBoard(String fen, Vector2 position, float scale) {
		
		sprite_batch_.draw(chessBoard_, position.x, position.y, 0, 0, chessBoard_.getRegionWidth(), 
				chessBoard_.getRegionHeight(), scale, scale, 0);
		
		int boardIndex = 0;
		/*
		for(int i = 0; i < fen.length(); ++i) {
			if(fen.charAt(i) == ' ') {
				return;
			} else if(fen.charAt(i) == '/') {
			} else if(Character.isDigit(fen.charAt(i))) {
				boardIndex += fen.charAt(i) - '0';
			} else {
				
			}
		}*/
	}
}
