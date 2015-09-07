package com.chessview.screen;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.chessview.ChessView;

public class SplashScreen extends AbstractScreen {
	Sprite splash;	
	
	boolean fadeOut;
	float alpha;
	
	public SplashScreen(ChessView kApplication) {
		super(kApplication);
	}
	
	@Override
	public void show() {
		super.show();

		splash = kApplication.atlas().createSprite("splash");
		splash.setPosition(-kVirtualWidth/2, -kVirtualHeight/2);
		
		fadeOut = false;
	}
	
	@Override
	public void render(float delta) {

		super.render(delta);
		
		if(fadeOut) {
			if(alpha < -0.5f) {
				kApplication.setScreen(new ChessViewScreen(kApplication));
				return;
			} 
			alpha -= 0.01f;
		} else {
			if(alpha > 1.5f) {
				fadeOut = true;
			} else {
				alpha += 0.01f;
			}
		}
		
		splash.setAlpha(Math.max(0, Math.min(1f,alpha)));
		
		kApplication.spriteBatch().begin();
		
		splash.draw(kApplication.spriteBatch());
		
		kApplication.spriteBatch().end();
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

}
