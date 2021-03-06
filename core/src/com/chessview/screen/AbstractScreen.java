package com.chessview.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.chessview.ChessView;

public abstract class AbstractScreen implements Screen {

	public static final int kVirtualWidth = 1920;
	public static final int kVirtualHeight = 1080;
	private static final float kAspectRatio = ((float)kVirtualWidth)/kVirtualHeight;
	
	protected BitmapFont textFont;
	
	protected Rectangle viewport_;
	
	private OrthographicCamera camera_;
	
	protected final ChessView kApplication;
	
	public AbstractScreen(ChessView kApplication) {
		this.kApplication = kApplication;
		
		this.camera_ = new OrthographicCamera(AbstractScreen.kVirtualWidth, AbstractScreen.kVirtualHeight);
	
		this.initalizeText();
	}
	 
	@Override
	public void resize(int width, int height) {

		System.out.println("gere");
		float aspectRatio = (float)width/(float)height;
		float scale = 1f;
		Vector2 crop = new Vector2(0f, 0f);
		if(aspectRatio > kAspectRatio){
			scale = (float)height/(float)kVirtualHeight;
			crop.x = (width - kVirtualWidth*scale)/2f;
		}
		else if(aspectRatio < kAspectRatio){
			scale = (float)width/(float)kVirtualWidth;
			crop.y = (height - kVirtualHeight*scale)/2f;
		}
		else{
			scale = (float)width/(float)kVirtualWidth;
		}
		
		viewport_ = new Rectangle(crop.x, crop.y, (float)kVirtualWidth*scale, (float)kVirtualHeight*scale);
		
		Gdx.gl.glViewport((int)viewport_.x, (int)viewport_.y, (int)viewport_.width, (int)viewport_.height);
	}
	
	@Override
	public void render(float delta) {		

		camera_.update();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	}
	
	@Override
	public void show() {
		kApplication.spriteBatch().setProjectionMatrix(camera_.combined);
	}
	
	
	protected Vector3 unproject(Vector3 screen_coord) {
		return this.camera_.unproject(screen_coord, viewport_.x, viewport_.y, 
									  viewport_.width, viewport_.height);
	}
	
	private void initalizeText() {
		FileHandle fontFile = Gdx.files.internal("SEGOEUI.TTF");
	    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
	    FreeTypeFontParameter parameter = new FreeTypeFontParameter();
	    parameter.size = 25;
	    parameter.magFilter = TextureFilter.Linear;
	    parameter.minFilter = TextureFilter.Linear;
	    textFont = generator.generateFont(parameter);
	    generator.dispose();
	}

}
