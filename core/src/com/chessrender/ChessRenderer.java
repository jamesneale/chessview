package com.chessrender;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.chessrender.drawableboard.DrawableChessBoard;
import com.chessrender.drawableboard.Piece;


public class ChessRenderer {
	
	static final float kMinSquareSize = 4f;
	
	private SpriteBatch sprite_batch_;
	
	private TextureAtlas atlas_;
	
	private TextureRegion chessBoard_;
	private TextureRegion chessBoardSelected_;
	
	public TextureRegion[] black_pieces_;
	public TextureRegion[] white_pieces_;
	
	private TextureRegion orangeSquare_;
	
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
		
		orangeSquare_ = atlas.findRegion("orangesquare");
	}
	
	public void RenderChessBoard(DrawableChessBoard board, Rectangle inside) {
		this.RenderChessBoard(chessBoard_, board, inside);
	}
	
	public void RenderChessBoardSelected(DrawableChessBoard board, Rectangle inside) {
		this.RenderChessBoard(chessBoardSelected_, board, inside);
	}
	
	private void RenderChessBoard(TextureRegion board_img, DrawableChessBoard board, Rectangle inside) {
		sprite_batch_.draw(board_img, inside.x, inside.y, inside.width, inside.height);
		
		float square_size = inside.width / 8;
		
		if(square_size < ChessRenderer.kMinSquareSize) {
			return;
		}

		board.renderLine(this, inside.x, inside.y, square_size);
		board.render(this, inside.x, inside.y, square_size);
	}
	
	public void begin() {
		this.sprite_batch_.begin();
	}
	public void end() {
		this.sprite_batch_.end();
	}
	public SpriteBatch getSpriteBatch() {
		return this.sprite_batch_;
	}

	public TextureRegion getLineTexture() {
		return this.orangeSquare_;
	}
	
}
