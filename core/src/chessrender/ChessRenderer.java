package chessrender;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class ChessRenderer {
	
	private SpriteBatch sprite_batch_;
	
	private TextureAtlas atlas_;
	
	private TextureRegion chessBoard_;
	private TextureRegion[] black_pieces_;
	private TextureRegion[] white_pieces_;
	
	private HashMap<String, Pixmap> pixmaps;
	
	public ChessRenderer(TextureAtlas atlas, SpriteBatch sprite_batch) {
		this.atlas_ = atlas;
		this.sprite_batch_ = sprite_batch;
		
		this.chessBoard_ = atlas_.findRegion("chessboard");
		
		black_pieces_ = new TextureRegion[6];
		white_pieces_ = new TextureRegion[6];
		
		black_pieces_[0] = atlas.findRegion("black_pawn");
		black_pieces_[1] = atlas.findRegion("black_rook");
		black_pieces_[2] = atlas.findRegion("black_knight");
		black_pieces_[3] = atlas.findRegion("black_bishop");
		black_pieces_[4] = atlas.findRegion("black_king");
		black_pieces_[5] = atlas.findRegion("black_queen");
		
		white_pieces_[0] = atlas.findRegion("white_pawn");
		white_pieces_[1] = atlas.findRegion("white_rook");
		white_pieces_[2] = atlas.findRegion("white_knight");
		white_pieces_[3] = atlas.findRegion("white_bishop");
		white_pieces_[4] = atlas.findRegion("white_king");
		white_pieces_[5] = atlas.findRegion("white_queen");
		
		pixmaps = new HashMap<String, Pixmap>();
	}
	
	
	public void RenderChessBoard(String fen, Rectangle inside) {		
		sprite_batch_.draw(chessBoard_, inside.x, inside.y, inside.width, inside.height);
	}
	
	public void begin() {
		this.sprite_batch_.begin();
	}
	public void end() {
		this.sprite_batch_.end();
	}
	
}
