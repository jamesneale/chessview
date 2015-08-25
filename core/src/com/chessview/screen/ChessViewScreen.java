package com.chessview.screen;

import java.util.ArrayDeque;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.chessrender.ChessRenderer;
import com.chessview.ChessView;
import com.chessview.data.DataRetrieval;
import com.chessview.graph.GraphSquare;
import com.chessview.graph.GraphSquareChild;
import com.chessview.graph.chess.ChessGraphSquare;
import com.chessview.region.ROI;

public class ChessViewScreen extends AbstractScreen implements GestureListener, InputProcessor {

	
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
	
	// Text
	private GlyphLayout glyph_layout;
	private String node_count;
	
	// Android Zoom
	private float previous_distance;
	private static final float kMinForScroll = 2f;
	
	/*
	 * Data generation
	 */
	/// Handles all data retrieval. Run as a separate thread.
	private DataRetrieval data_retreiver;
	
	/// FEN representation of the starting board
	private final String kInitialFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
	
	public ChessViewScreen(ChessView kApplication) {
		super(kApplication);
		
		this.data_retreiver = new DataRetrieval();

		this.chessboard_renderer_ = new ChessRenderer(kApplication.atlas(), kApplication.sprite_back());
		kRootNode = new ChessGraphSquare(data_retreiver, kInitialFen, this.chessboard_renderer_);
	
		this.glyph_layout = new GlyphLayout();
		this.node_count = "";
	}

	@Override
	public void show() {
		super.show();

		// Set up data retrieval.
		new Thread(this.data_retreiver).start();

		// Set up game tree
		this.game_path_ = new ArrayDeque<GraphSquareChild>();
		this.game_path_.addFirst(new GraphSquareChild(kRootNode, new Rectangle()));

		// Set up UI
		this.background = kApplication.atlas().createSprite("background");
		this.background.setPosition(-kVirtualWidth/2, -kVirtualHeight/2);
		this.overlay = kApplication.atlas().findRegion("overlay");
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
		
		GraphSquareChild next_node = null;
		
		//prepare text
		if(!this.node_count.equals(this.data_retreiver.getNodeCount())) {
			glyph_layout.setText(this.textFont, this.node_count);
			this.node_count = this.data_retreiver.getNodeCount();
		}
		
		
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
			this.textFont.draw(kApplication.sprite_back(), this.glyph_layout, -720-this.glyph_layout.width, -365);
			
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
		return true;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		region_of_interest_.Pan(deltaX, deltaY);
		return true;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		if(distance > this.previous_distance) {
			if(distance - this.previous_distance > kMinForScroll) {
				
			}
		}
		
		System.out.println(initialDistance + " " + distance);
		return true;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		return false;
		/*
		initialPointer1.lerp(initialPointer2, 0.5f);
		Vector3 midpoint = new Vector3(initialPointer1.x, initialPointer2.y, 0);
		
		float distance = pointer1.dst2(pointer2);
		int dir = distance > this.previous_distance?1:-1;
		this.previous_distance = distance;
		
		this.unproject(midpoint);
		System.out.println(midpoint + " " + dir);
		return region_of_interest_.Zoom(initialPointer1.x, initialPointer1.y, dir);
	*/
	}


	@Override
	public boolean scrolled(int amount) {
		Vector3 mouse_pos = this.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		return region_of_interest_.Zoom(mouse_pos.x, mouse_pos.y, amount*4);
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
		System.out.println("touch down");
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		region_of_interest_.Pan(Gdx.input.getDeltaX(), Gdx.input.getDeltaY());
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

}
