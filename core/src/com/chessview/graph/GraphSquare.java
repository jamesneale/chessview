package com.chessview.graph;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.chessview.data.DataRequest;
import com.chessview.data.DataRetrieval;
import com.chessview.region.ROI;
import com.chessview.screen.ChessViewScreen;

public class GraphSquare {
	
	public static final float kMinBounding = 10;
	
	public final String kNodeData;
	
	private boolean request_made_;
	private ArrayList<GraphSquareChild> children_;
	
	private DataRetrieval data_retreiver;
	
	public GraphSquare(String node_data, DataRetrieval data_retreiver) {
		kNodeData = node_data;
		this.data_retreiver = data_retreiver;
		
		this.children_ = null;
		this.request_made_ = false;
	}
	
	
	// called by main thread
	public GraphSquareChild render(float delta, ShapeRenderer shape_renderer, ROI region) {
		if(children_ == null) {
			if(!request_made_) {
				if(GenerateChildren()) {
					request_made_ = true;
				}
			}
			return null;
		}			
		
		
		GraphSquareChild last_rendered = null;
		
		for(GraphSquareChild child : children_) {
			Rectangle bounding_box = GetBoundingBox(region.kBoundingBox, child.virtual_position, region.region_of_interest_);
			
			if(bounding_box != null) {
				last_rendered = child;
				child.graph.render(delta, shape_renderer, bounding_box, 0);
			}
			
		}
	
		if(last_rendered == null) {
			// stop zoom
		} else if(last_rendered.virtual_position.contains(region.region_of_interest_)) {
			return last_rendered;
		}
		return null;
		
	}
	
	//called by parent
	private void render(float delta, ShapeRenderer shape_renderer, Rectangle bounding_box, int depth) {
		
		// Check inside drawable region
		if(!bounding_box.overlaps(ChessViewScreen.kDrawableRegion)) {
			return;
		}
		
		// Check large enough to draw children
		if(bounding_box.width > kMinBounding) {
			
			if(children_ == null) {
				if(!request_made_) {
					if(GenerateChildren()) {
						request_made_ = true;
					}
				}
				return;
			}	
			
			for(GraphSquareChild child : children_) {	
				child.graph.render(delta, shape_renderer, GetBoundingBox(bounding_box, child.virtual_position), depth+1);
			}
		}
		
		// draw ourselves
		float color = Math.max(0.2f, 1 - depth*0.2f);
		shape_renderer.setColor(color, color, color, 1);
		shape_renderer.rect(bounding_box.x, bounding_box.y, bounding_box.width, bounding_box.height);
		
	}
	
	
	
	
	
	private Rectangle GetBoundingBox(Rectangle original, Rectangle virtual_position) {
		return new Rectangle(original.x + virtual_position.x*original.width,
							 original.y + virtual_position.y*original.height,
							 original.width*virtual_position.width,
							 original.height*virtual_position.height);
	}
	
	private Rectangle GetBoundingBox(Rectangle original, Rectangle virtual_position, Rectangle region) {
		if(!region.overlaps(virtual_position)) {
			return null;
		}
		
		//region represents 1
		return new Rectangle(original.x + original.width*((virtual_position.x - region.x)/region.width),
							 original.y + original.height*((virtual_position.y - region.y)/region.height),
							 original.width*(virtual_position.width/region.width),
							 original.height*(virtual_position.height/region.height));
		
	}
	private final static int kMaxNodes = 25;
	
	private boolean GenerateChildren() {
		return this.data_retreiver.data_requests_.offer(new DataRequest(this, this.kNodeData));
	}


	public synchronized void AddChildren(ArrayList<String> children_data) {
		if(this.children_ != null) {
			return;
		}
		children_ = new ArrayList<GraphSquareChild>();
		
		float size = 0.2f;
		// float area_per_child = 1 / (children_data.size()*2f);	
		//float size = (float) Math.sqrt((double)area_per_child);
		
		for(int i = 0; i < children_data.size() && i < kMaxNodes; ++i) {
			Rectangle virtual_position = new Rectangle((float)Math.random()*(1f-size), (float)Math.random()*(1f-size), size, size);
			children_.add(new GraphSquareChild(new GraphSquare(children_data.get(i), this.data_retreiver), virtual_position));
		}
	}
}
