package com.chessview.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.chessview.ChessView;
import com.chessview.data.DataRetrieval;
import com.chessview.graph.GraphSquare;
import com.chessview.region.ROI;

public class ChessViewScreen extends AbstractScreen implements InputProcessor {

	private final GraphSquare kRootNode;
	
	private GraphSquare current_node_;
	
	public static final Rectangle kDrawableRegion = new Rectangle(-AbstractScreen.kVirtualWidth/2+2, -AbstractScreen.kVirtualHeight/2+2, 
			  AbstractScreen.kVirtualWidth-4, AbstractScreen.kVirtualHeight-4 );
	
	private ROI region_of_interest_;
	
	private DataRetrieval data_retreiver;
	
	
	public ChessViewScreen(ChessView kApplication) {
		super(kApplication);
		

		this.data_retreiver = new DataRetrieval();
		(new Thread(this.data_retreiver)).start();
		
		kRootNode = new GraphSquare("123", data_retreiver);
		region_of_interest_ = new ROI(ChessViewScreen.kDrawableRegion);

		this.current_node_ = kRootNode;
		
		Gdx.input.setInputProcessor(this);
		
	
	}

	@Override
	public void show() {
		 super.show();
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);		
		
		kApplication.shape_renderer().begin(ShapeType.Line); {
					
			GraphSquare next_node = kRootNode.render(delta, 
					kApplication.shape_renderer(), region_of_interest_);
			
			if(next_node != null) {
				this.current_node_ = next_node;
			}
		
		} kApplication.shape_renderer().end();
				
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
		// TODO Auto-generated method stub
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
		return region_of_interest_.Zoom(mouse_pos.x, mouse_pos.y, amount);
	}

}
