package com.chessview.graph;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.chessview.data.DataRetrieval;
import com.chessview.data.Requester;
import com.chessview.graph.layout.GridLayoutManager;
import com.chessview.region.ROI;
import com.chessview.screen.ChessViewScreen;

public abstract class GraphSquare implements Requester{
	public static float minSize = 15;
	
	/// True iff this GraphSquare has requested its children be generated
	private boolean request_made_;
	
	/// The children on this node from 0+
	private volatile List<GraphSquareChild> children_;
	protected DataRetrieval data_retreiver;
	
	
	public GraphSquare(DataRetrieval data_retreiver) {
		this.data_retreiver = data_retreiver;
		
		this.children_ = null;
		this.request_made_ = false;
	}
	
	protected abstract void render_node(Rectangle drawable_region);
	protected abstract void render_node(ROI region);
	
	
	// called by main thread
	public GraphSquareChild render(ROI region) {
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
				child.graph.render(bounding_box, 0);
			}
		}
	
		
		this.render_node(region);
		
		if(last_rendered == null) {
			region.ZoomInLimit();
		} else if(last_rendered.virtual_position.contains(region.region_of_interest_)) {
			return last_rendered;
		}
		
		return null;
		
	}
	
	//called by parent
	private void render(Rectangle bounding_box, int depth) {
		
		// Check inside drawable region
		if(!bounding_box.overlaps(ChessViewScreen.kDrawableRegion)) {
			return;
		}
		
		// Check large enough to draw children
		if(bounding_box.width > minSize) {
			
			if(children_ == null) {
				if(!request_made_) {
					if(GenerateChildren()) {
						request_made_ = true;
					}
				}
				return;
			}	
			
			for(GraphSquareChild child : children_) {	
				child.graph.render(GetBoundingBox(bounding_box, child.virtual_position), depth+1);
			}
		}
		
		this.render_node(bounding_box);
	}
	
	
	
	
	
	protected Rectangle GetBoundingBox(Rectangle original, Rectangle virtual_position) {
		return new Rectangle(original.x + virtual_position.x*original.width,
							 original.y + virtual_position.y*original.height,
							 original.width*virtual_position.width,
							 original.height*virtual_position.height);
	}
	
	protected Rectangle GetBoundingBox(Rectangle original, Rectangle virtual_position, Rectangle region) {
		if(!region.overlaps(virtual_position)) {
			return null;
		}
		
		//region represents 1
		return new Rectangle(original.x + original.width*((virtual_position.x - region.x)/region.width),
							 original.y + original.height*((virtual_position.y - region.y)/region.height),
							 original.width*(virtual_position.width/region.width),
							 original.height*(virtual_position.height/region.height));
		
	}
	
	
	public void AddData(ArrayList<Object> children_data) {
		if(this.children_ != null) {
			return;
		}
		
		ArrayList<GraphSquareChild> new_children = new ArrayList<GraphSquareChild>();
		
		GridLayoutManager layout_manager = new GridLayoutManager(children_data.size());
		
		for(int i = 0; i < children_data.size(); ++i) {
			new_children.add(make_child(children_data.get(i), layout_manager.GetNext()));
		}
		
		this.children_ = new_children;
		
		
	}

	protected abstract boolean GenerateChildren();
		//;
	protected abstract GraphSquareChild make_child(Object data, Rectangle virtual_position);

	public void CullGrandChildrenExcept(Rectangle avoid) {
		if(children_ == null) {
			return;
		}
		for(GraphSquareChild gsc : children_) {
			if(!gsc.virtual_position.equals(avoid)) {
				gsc.graph.children_ = null;
				gsc.graph.request_made_ = false;
			}
		}
	}
	
}
