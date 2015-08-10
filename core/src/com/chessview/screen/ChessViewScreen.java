package com.chessview.screen;

import java.util.ArrayDeque;

import chessrender.ChessRenderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.chessview.ChessView;
import com.chessview.data.DataRetrieval;
import com.chessview.graph.GraphSquare;
import com.chessview.graph.GraphSquareChild;
import com.chessview.graph.chess.ChessGraphSquare;
import com.chessview.region.ROI;

public class ChessViewScreen extends AbstractScreen implements InputProcessor {

	
	/*
	 * Game tree graph variables
	 */
	
	/// Root node of the graph. Initial board state.
	private final GraphSquare kRootNode;
	
	/// The full move list taken in the current path - used as a stack
	private ArrayDeque<GraphSquareChild> game_path_;
	
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
	
	/// UI overlay
	private TextureRegion overlay;
	private Sprite background;
	
	
	/*
	 * Data generation
	 */
	/// Handles all data retrieval. Run as a separate thread.
	private Thread data_retrieval_thread;
	private DataRetrieval data_retreiver;
	
	/// FEN representation of the starting board
	private final String kInitialFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
	
	public ChessViewScreen(ChessView kApplication) {
		super(kApplication);
		
		this.data_retreiver = new DataRetrieval();

		this.chessboard_renderer_ = new ChessRenderer(kApplication.atlas(), kApplication.sprite_back());
		kRootNode = new ChessGraphSquare(data_retreiver, kInitialFen, this.chessboard_renderer_);
	//	kRootNode = new LineGraphSquare(data_retreiver, kInitialFen, kApplication.shape_renderer());
	}

	@Override
	public void show() {
		super.show();

		// Set up data retrieval.
		this.data_retrieval_thread = new Thread(this.data_retreiver);
		this.data_retrieval_thread.start();

		// Set up game tree
		this.game_path_ = new ArrayDeque<GraphSquareChild>();
		this.game_path_.addFirst(new GraphSquareChild(kRootNode, new Rectangle()));

		// Set up UI
		this.background = kApplication.atlas().createSprite("background");
		this.background.setPosition(-kVirtualWidth/2, -kVirtualHeight/2);
		this.overlay = kApplication.atlas().findRegion("overlay");
		region_of_interest_ = new ROI(ChessViewScreen.kDrawableRegion);
		Gdx.input.setInputProcessor(this);
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);		
		
		GraphSquareChild next_node = null;
		
		kApplication.sprite_back().begin(); {
			this.background.draw(kApplication.sprite_back());
		}
		kApplication.sprite_back().end();
	
		this.chessboard_renderer_.begin(); {		
	//	kApplication.shape_renderer().begin(ShapeType.Line); {
			next_node = game_path_.peek().graph.render(region_of_interest_);	
		} 
		//kApplication.shape_renderer().end();
		this.chessboard_renderer_.end();
		
		kApplication.sprite_back().begin(); {
			kApplication.sprite_back().draw(overlay, -AbstractScreen.kVirtualWidth/2, -AbstractScreen.kVirtualHeight/2);
		}
		kApplication.sprite_back().end();
		
		
		if(next_node != null) {
			game_path_.addFirst(next_node);
			this.region_of_interest_.Reconstrain(next_node.virtual_position);
		} else if(game_path_.size() > 1 && this.region_of_interest_.ZoomOutRequired()) {
			next_node = game_path_.pop();
			this.region_of_interest_.Deconstrain(next_node.virtual_position);
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
	public boolean keyDown(int keycode) {
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		region_of_interest_.Pan(Gdx.input.getDeltaX(), Gdx.input.getDeltaY());
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {	
		Vector3 mouse_pos = this.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		return region_of_interest_.Zoom(mouse_pos.x, mouse_pos.y, amount*4);
	}

}
