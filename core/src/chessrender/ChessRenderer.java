package chessrender;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;


public class ChessRenderer {
	
	private static final float kMinSquareSize = 2f;
	
	private SpriteBatch sprite_batch_;
	
	private TextureAtlas atlas_;
	
	private TextureRegion chessBoard_;
	private TextureRegion chessBoardSelected_;
	
	private TextureRegion[] black_pieces_;
	private TextureRegion[] white_pieces_;
	
	public ChessRenderer(TextureAtlas atlas, SpriteBatch sprite_batch) {
		this.atlas_ = atlas;
		this.sprite_batch_ = sprite_batch;
		
		this.chessBoard_ = atlas_.findRegion("chessboard");
		this.chessBoardSelected_ = atlas_.findRegion("selected_chessboard");
		
		black_pieces_ = new TextureRegion[6];
		white_pieces_ = new TextureRegion[6];
		
		black_pieces_[Piece.PAWN] = atlas.findRegion("black_pawn");
		black_pieces_[Piece.ROOK] = atlas.findRegion("black_rook");
		black_pieces_[Piece.KNIGHT] = atlas.findRegion("black_knight");
		black_pieces_[Piece.BISHOP] = atlas.findRegion("black_bishop");
		black_pieces_[Piece.KING] = atlas.findRegion("black_king");
		black_pieces_[Piece.QUEEN] = atlas.findRegion("black_queen");
		
		white_pieces_[Piece.PAWN] = atlas.findRegion("white_pawn");
		white_pieces_[Piece.ROOK] = atlas.findRegion("white_rook");
		white_pieces_[Piece.KNIGHT] = atlas.findRegion("white_knight");
		white_pieces_[Piece.BISHOP] = atlas.findRegion("white_bishop");
		white_pieces_[Piece.KING] = atlas.findRegion("white_king");
		white_pieces_[Piece.QUEEN] = atlas.findRegion("white_queen");
		
	}
	
	
	public void RenderChessBoard(ChessBoard board, Rectangle inside) {		
		sprite_batch_.draw(chessBoard_, inside.x, inside.y, inside.width, inside.height);
		this.RenderPieces(board, inside);
	}
	
	public void RenderSelectedChessBoard(ChessBoard board, Rectangle inside) {
		sprite_batch_.draw(chessBoardSelected_, inside.x, inside.y, inside.width, inside.height);
		this.RenderPieces(board, inside);
	}
	
	private void RenderPieces(ChessBoard board, Rectangle inside) {		
		float square_size = inside.width / 8;
		if(square_size < ChessRenderer.kMinSquareSize) {
			return;
		}
		for(Piece p : board.pieces) {
			sprite_batch_.draw(p.white?white_pieces_[p.name]:black_pieces_[p.name], 
					inside.x + square_size * p.col, inside.y + square_size * p.row, square_size, square_size);
		}
	}
	
	public void begin() {
		this.sprite_batch_.begin();
	}
	public void end() {
		this.sprite_batch_.end();
	}
	
}
