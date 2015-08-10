package com.chessview.region;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class ROI {
	
	public static final Rectangle kDefaultVirtualCanvas = new Rectangle(0,0,1,1);
	private static final float kMinROISize = 0.001f;
	
	
	// higher is less sensitive
	public static final float sensitivity = 50;
	
	
	
	public final Rectangle kBoundingBox;
	public Rectangle region_of_interest_;
	
	private boolean zoom_out_max_;
	private boolean zoom_in_max_;
	
	public ROI(Rectangle bounding_box) {
		this.kBoundingBox = bounding_box;
		this.region_of_interest_ = new Rectangle(kDefaultVirtualCanvas);
	
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
		float amountf = region_of_interest_.width * -0.1f ;
		
		float bottom_weight = ((y-kBoundingBox.y)/kBoundingBox.height);
		float left_weight = ((x-kBoundingBox.x)/kBoundingBox.width);
		

		region_of_interest_.x += -amountf*left_weight;
		region_of_interest_.width = Math.max(region_of_interest_.width + amountf, ROI.kMinROISize);
		
		region_of_interest_.y += -amountf*bottom_weight;
		region_of_interest_.height = region_of_interest_.width;
		
		zoom_out_max_ = false;
	}
	
	// TODO fix this method to constrain zoom out
	private void ZoomOut(float x, float y, int amount) {
		if(this.zoom_out_max_) {
			return;
		}
		float amountf = region_of_interest_.width * 0.1f ;
		
		float bottom_weight = ((y-kBoundingBox.y)/kBoundingBox.height);
		float left_weight = ((x-kBoundingBox.x)/kBoundingBox.width);
		

		region_of_interest_.x += -amountf*left_weight;
		region_of_interest_.width = Math.max(region_of_interest_.width + amountf, ROI.kMinROISize);
		
		region_of_interest_.y += -amountf*bottom_weight;
		region_of_interest_.height = region_of_interest_.width;
		
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

	public void Pan(int deltaX, int deltaY) {
		region_of_interest_.x -= deltaX / ((1.0f/region_of_interest_.width) * 1000f);
		region_of_interest_.y += deltaY / ((1.0f/region_of_interest_.height) * 1000f);
		
	}

	public boolean ZoomOutRequired() {
		return !ROI.kDefaultVirtualCanvas.contains(this.region_of_interest_);
	}

	public void Deconstrain(Rectangle virtual_position) {
		region_of_interest_.x = virtual_position.x + virtual_position.width * region_of_interest_.x;
		region_of_interest_.y = virtual_position.y + virtual_position.height * region_of_interest_.y;
		region_of_interest_.width = region_of_interest_.width * virtual_position.width;
		region_of_interest_.height = virtual_position.height * region_of_interest_.height;	
	
	}

}
