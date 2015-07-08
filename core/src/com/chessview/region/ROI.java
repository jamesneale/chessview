package com.chessview.region;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class ROI {
	
	// higher is less sensitive
	public static final float sensitivity = 50;
	
	public final Rectangle kBoundingBox;
	public Rectangle region_of_interest_;
	
	public ROI(Rectangle bounding_box) {
		this.kBoundingBox = bounding_box;
		this.region_of_interest_ = new Rectangle(0,0,1,1);
	}
	
	public void Render(float delta, ShapeRenderer sr) {
		sr.setColor(Color.RED);
		sr.rect(kBoundingBox.x + region_of_interest_.x*kBoundingBox.width, 
										   kBoundingBox.y + region_of_interest_.y*kBoundingBox.height,
										   region_of_interest_.width*kBoundingBox.width, 
										   region_of_interest_.height*kBoundingBox.height);
		sr.setColor(Color.WHITE);
	}
	
	public Rectangle GetBoundingBox() {
		return this.kBoundingBox;
	}
	
	public boolean Zoom(float x, float y, int amount) {
		
		if(!this.kBoundingBox.contains(x,y)) {
			return false;
		}
		
		float amountf = amount / sensitivity;
		
		float bottom_weight = ((y-kBoundingBox.y)/kBoundingBox.height);
		float left_weight = ((x-kBoundingBox.x)/kBoundingBox.width);
		

		region_of_interest_.x += -amountf*left_weight;
		region_of_interest_.width += amountf;
		
		region_of_interest_.y += -amountf*bottom_weight;
		region_of_interest_.height += amountf;
		
		return true;
	}

}
