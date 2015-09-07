package com.chessview.screen;

import java.util.ArrayDeque;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.chessrender.ChessRenderer;
import com.chessrender.drawableboard.ChessBoardBitBasic;
import com.chessview.ChessView;
import com.chessview.data.BoardDataRetrieval;
import com.chessview.graph.GraphSquare;
import com.chessview.graph.chess.ChessGraphSquare;
import com.chessview.graph.layout.LayoutManager;
import com.chessview.region.ROI;
import com.chessview.screen.ui.HUD;
import com.generator.albertoruibal.bitboard.Board;
import com.movelist.MoveList;

public class ChessViewScreen extends AbstractScreen implements GestureListener, InputProcessor {

	
	/*
	 * Game tree graph variables
	 */
	
	/// Root node of the graph. Initial board state.
	private final GraphSquare kRootNode;
	
	/// The full move list taken in the current path - used as a stack
	private ArrayDeque<GraphSquare> game_path_;
	
	/// Initial board state
	
	/*
	 * UI variables
	 */
	/// Drawable area of the screen	
	public static final Rectangle kDrawableRegion = new Rectangle(-660, -540, 1620, 1080);
	
	/// The region of the current node that is being viewed
	private ROI region_of_interest_;
	
	/// Handles the drawing of chessboards
	private ChessRenderer chessboard_renderer_;
	
	/// HUD
	private HUD hud;
	private boolean disableDrag;
	
	// Background image
	private Sprite background;
	
	int frame_count = 0;
	
	/*
	 * Data generation
	 */
	/// Handles all data retrieval. Run as a separate thread.
	private BoardDataRetrieval data_retreiver;
	
	/// FEN representation of the starting board
	private final String kInitialFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
	
	public ChessViewScreen(ChessView kApplication) {
		super(kApplication);
		
		this.data_retreiver = new BoardDataRetrieval();
		
		Board temp = new Board();
		temp.setFen(kInitialFen);

		this.chessboard_renderer_ = new ChessRenderer(kApplication.atlas(), kApplication.spriteBatch());
		kRootNode = new ChessGraphSquare(data_retreiver, new ChessBoardBitBasic(temp), this.chessboard_renderer_);
	}

	@Override
	public void show() {
		super.show();

		// Set up data retrieval.
		new Thread(this.data_retreiver).start();
		
		// Set up layout manager
		
		LayoutManager.setUpLayouts(kApplication.atlas());

		// Set up game tree
		this.game_path_ = new ArrayDeque<GraphSquare>();
		this.game_path_.addFirst(kRootNode);

		// Set up UI
		this.background = kApplication.atlas().createSprite("background");
		this.background.setPosition(-kVirtualWidth/2, -kVirtualHeight/2);
		
		this.disableDrag = false;
		
		this.hud = new HUD(background.getX(), background.getY(), kApplication.atlas(), this.textFont);
		
		region_of_interest_ = new ROI(ChessViewScreen.kDrawableRegion);
		
		if(Gdx.app.getType() == ApplicationType.Desktop) {
			Gdx.input.setInputProcessor(this);
		} else {
			Gdx.input.setInputProcessor(new GestureDetector(this));
		}
		
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);		
		
		int nextNodeIndex = -1;
		
		//prepare text
		
		
		kApplication.spriteBatch().begin(); {
			this.background.draw(kApplication.spriteBatch());
		}
		kApplication.spriteBatch().end();
	
		this.chessboard_renderer_.begin(); {		
			nextNodeIndex = game_path_.peek().render(region_of_interest_);
		} 
		this.chessboard_renderer_.end();
		
		kApplication.spriteBatch().begin(); {
			hud.render(kApplication.spriteBatch(), delta);
		}
		kApplication.spriteBatch().end();
		
		
		if(nextNodeIndex > -1) {
			if(game_path_.size() > 1) {
				
				// Cull grandchildren to deallocate memory 
				GraphSquare cur_top = game_path_.pop();
				game_path_.peek().CullGrandChildrenExcept(cur_top);
				game_path_.push(cur_top);
			}
			this.region_of_interest_.Reconstrain(game_path_.peek().getChildVirtualPosition(nextNodeIndex));
			game_path_.push(game_path_.peek().getChildNode(nextNodeIndex));
		
			this.hud.pushMove(this.game_path_.peek().toString());
			
		} else if(game_path_.size() > 1 && this.region_of_interest_.ZoomOutRequired()) {
			GraphSquare previous = game_path_.pop();
			region_of_interest_.Deconstrain(game_path_.peek().getChildVirtualPosition(game_path_.peek().getChildIndex(previous)));
			this.hud.popMove();
		}
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public boolean scrolled(int amount) {
		Vector3 mouse_pos = this.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		return region_of_interest_.Zoom(mouse_pos.x, mouse_pos.y, amount*25);
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 mousePos = this.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		if(this.hud.touchDown(mousePos.x, mousePos.y, button)) {
			this.disableDrag = true;
			return true;
		}
		
		if(this.game_path_.peek().touchDown(mousePos.x, mousePos.y, button, this.region_of_interest_)) {
			this.disableDrag = true;
			return true;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Vector3 mousePos = this.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		this.hud.touchUp(mousePos.x, mousePos.y, button);
		
		disableDrag = false;
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(!disableDrag) {
			region_of_interest_.Pan(Gdx.input.getDeltaX(), Gdx.input.getDeltaY());
		}
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	
	
	
	// TOUCH SCREEN HANDLERS
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

	

}
