package com.chessview.region;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class ROI {
	
	public static final Rectangle kDefaultVirtualCanvas = new Rectangle(0,0,1,1);
	private static final float MIN_ROI_SIZE = 0.001f;
	private static final float MAX_ROI_SIZE = 1.2f;
	
	// higher is less sensitive
	public static final float sensitivity = 50;
	
	public final Rectangle kBoundingBox;
	public Rectangle regionOfInterest;
	
	private boolean zoomMax;
	private boolean zoomMin;
	
	public ROI(Rectangle bounding_box) {
		this.kBoundingBox = bounding_box;
		this.regionOfInterest = new Rectangle(kDefaultVirtualCanvas);
	
		this.zoomMin = false;
		this.zoomMax = true;
	}
	
	public void Render(float delta, ShapeRenderer sr) {
		sr.setColor(Color.RED);
		sr.rect(kBoundingBox.x + regionOfInterest.x*kBoundingBox.width, 
										   kBoundingBox.y + regionOfInterest.y*kBoundingBox.height,
										   regionOfInterest.width*kBoundingBox.width, 
										   regionOfInterest.height*kBoundingBox.height);
		sr.setColor(Color.WHITE);
	}
	
	public Rectangle GetBoundingBox() {
		return this.kBoundingBox;
	}
	
	/**
	 * Method to handle zoom events. Zooming in requires a negative amount. Zooming out 
	 * requires a positive amount. Will unlock the zoom from the maximum if zoomed in and
	 * vice versa.
	 * 
	 * Amount is the % of the zoom. Zoom is relative to the current region size. 
	 * 
	 * E.g a zoom of amount -10 will be a 10% zoom in.
	 * 
	 * Zoom effect is similar to that found on Google Maps etc. I.e. the locus of the zoom 
	 * given by x and y should remain the same before and after the zoom takes place.
	 * 
	 * @param x is the x value of the zoom locus.
	 * @param y is the y value of the zoom locus.
	 * @param amount is the amount to zoom.
	 * @return true iff a zoom was completed.
	 */
	
	public boolean Zoom(float x, float y, float amount) {
		
		if(!this.kBoundingBox.contains(x,y)) {
			return false;
		}
		
		// Make zoom equal to amount% of current region size.
		amount *= regionOfInterest.width * 0.01f;
		
		// Constrain and update zoom locking mechanisms.
		amount = constrainZoom(amount);
		
		if(amount == 0) {
			return false;
		}
		
		// Get left and right bias of zoom locus.
		float bottomWeight = ((y-kBoundingBox.y)/kBoundingBox.height);
		float leftWeight = ((x-kBoundingBox.x)/kBoundingBox.width);
		
		// perform initial transformation of ROI size. (ROI is always square)
		regionOfInterest.width += amount;		
		regionOfInterest.height = regionOfInterest.width;
		
		// adjust x and y bias to ensure locus remains in the same position.
		regionOfInterest.x += -amount*leftWeight;
		regionOfInterest.y += -amount*bottomWeight;
		
		return true;
	}
	
	/**
	 * Handles the zoom in and zoom amount constraints. Returns a restricted value for
	 * zoom amount that prevents the ROI from leaving the given bounds. Also sets and
	 * releases the zoom locks. 
	 * @param amount is the requested zoom amount (negative = zoom in | positive = zoom out)
	 * @return constrained value for amount;
	 */

	private float constrainZoom(float amount) {
		if(amount < 0) {
			
			// constrain zoom in operation
			
			if (this.zoomMin) {
				return 0f;
			}
			this.zoomMax = false;
			
			if(regionOfInterest.width + amount < ROI.MIN_ROI_SIZE) {
				this.zoomMin = true;
				return regionOfInterest.width - ROI.MIN_ROI_SIZE;
			}
			
			return amount;
			
		} else {
			
			// constrain zoom out operation
			
			if  (this.zoomMax) {
				return 0f;
			}
			this.zoomMin = false;
			
			if(regionOfInterest.width + amount > ROI.MAX_ROI_SIZE) {
				this.zoomMax = true;
				return ROI.MAX_ROI_SIZE - regionOfInterest.width;
			}
			
			return amount;
		}
	}
	
	// The given virtual coordinates become the absolute coords
	public void Reconstrain(Rectangle virtual_coords) {
		
		float x_scale = 1/virtual_coords.width;
		regionOfInterest.x -= virtual_coords.x;
		regionOfInterest.x *= x_scale;
		regionOfInterest.width *= x_scale;
		
		float y_scale = 1/virtual_coords.height;
		regionOfInterest.y -= virtual_coords.y;
		regionOfInterest.y *= y_scale;
		regionOfInterest.height *= y_scale;
	}
	
	
	public void Reset() {
		this.regionOfInterest = new Rectangle(0,0,1,1);	
	}
	
	public void ZoomInLimit() {
		this.zoomMin = true;
	}
	
	public void ZoomInEnable() {
		this.zoomMin = false;
	}
	 
	public void ZoomOutLimit() {
		this.zoomMax = true;
	}

	public void Pan(float deltaX, float deltaY) {
		regionOfInterest.x -= deltaX / ((1.0f/regionOfInterest.width) * 1000f);
		regionOfInterest.y += deltaY / ((1.0f/regionOfInterest.height) * 1000f);
		
	}

	public boolean ZoomOutRequired() {
		return !ROI.kDefaultVirtualCanvas.contains(this.regionOfInterest);
	}

	public void Deconstrain(Rectangle virtual_position) {
		regionOfInterest.x = virtual_position.x + virtual_position.width * regionOfInterest.x;
		regionOfInterest.y = virtual_position.y + virtual_position.height * regionOfInterest.y;
		regionOfInterest.width = regionOfInterest.width * virtual_position.width;
		regionOfInterest.height = virtual_position.height * regionOfInterest.height;	
	
	}

}
