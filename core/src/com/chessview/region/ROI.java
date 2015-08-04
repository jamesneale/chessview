package com.chessview.region;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class ROI {
	
	// higher is less sensitive
	public static final float sensitivity = 50;
	
	public final Rectangle kBoundingBox;
	public Rectangle region_of_interest_;
	
	private boolean zoom_out_max_;
	private boolean zoom_in_max_;
	
	public ROI(Rectangle bounding_box) {
		this.kBoundingBox = bounding_box;
		this.region_of_interest_ = new Rectangle(0,0,1,1);
	
		this.zoom_in_max_ = false;
		this.zoom_out_max_ = true;
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
		
		if(amount > 0) {
			ZoomOut(x,y,amount);
		} else {
			ZoomIn(x,y,amount);
		}
		
		return true;
	}
	
	private void ZoomIn(float x, float y, int amount) {
		if(this.zoom_in_max_) {
			return;
		}
		float amountf = amount / sensitivity;
		
		float bottom_weight = ((y-kBoundingBox.y)/kBoundingBox.height);
		float left_weight = ((x-kBoundingBox.x)/kBoundingBox.width);
		

		region_of_interest_.x += -amountf*left_weight;
		region_of_interest_.width += amountf;
		
		region_of_interest_.y += -amountf*bottom_weight;
		region_of_interest_.height += amountf;
		
		zoom_out_max_ = false;
	}
	
	// TODO fix this method to constrain zoom out
	private void ZoomOut(float x, float y, int amount) {
		if(this.zoom_out_max_) {
			return;
		}
		float amountf = amount / sensitivity;
		
		float bottom_weight = ((y-kBoundingBox.y)/kBoundingBox.height);
		float left_weight = ((x-kBoundingBox.x)/kBoundingBox.width);
		

		region_of_interest_.x += -amountf*left_weight;
		region_of_interest_.width += amountf;
		
		region_of_interest_.y += -amountf*bottom_weight;
		region_of_interest_.height += amountf;
		
		zoom_in_max_ = false;
	}
	
	
	// The given virtual coordinates become the absolute coords
	public void Reconstrain(Rectangle virtual_coords) {
		
		float x_scale = 1/virtual_coords.width;
		region_of_interest_.x -= virtual_coords.x;
		region_of_interest_.x *= x_scale;
		region_of_interest_.width *= x_scale;
		
		float y_scale = 1/virtual_coords.height;
		region_of_interest_.y -= virtual_coords.y;
		region_of_interest_.y *= y_scale;
		region_of_interest_.height *= y_scale;
		
	}
	public void Reset() {
		this.region_of_interest_ = new Rectangle(0,0,1,1);
		
	}
	
	public void ZoomInLimit() {
		this.zoom_in_max_ = true;
	}
	public void ZoomOutLimit() {
		this.zoom_out_max_ = true;
	}

}
