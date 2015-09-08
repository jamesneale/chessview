package com.chessview.graph;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.chessview.data.DataRetrieval;
import com.chessview.data.Requester;
import com.chessview.graph.layout.Layout;
import com.chessview.graph.layout.LayoutManager;
import com.chessview.region.ROI;
import com.chessview.screen.ChessViewScreen;

public abstract class GraphSquare implements Requester{
	public static float minSize = 15;
	
	/// True iff this GraphSquare has requested its children be generated
	private boolean request_made_;
	
	/// The children on this node from 0+
	protected volatile List<GraphSquare> children_;
	
	
	public GraphSquare() {		
		this.children_ = null;
		this.request_made_ = false;
	}
	
	protected abstract void renderNode(Rectangle drawable_region);
	protected abstract void renderNodeDataOnly(Rectangle drawable_region);
	protected abstract void renderNode(ROI region);
	
	
	// called by main thread
	public int render(ROI region) {
		if(children_ == null) {
			if(!request_made_) {
				if(GenerateChildren()) {
					request_made_ = true;
				}
			}
			return -1;
		}			
				
		int last_rendered = -1;
		
		Layout layout = LayoutManager.getLayoutInstance();
		
		// render children data
		for(int i = 0; i < children_.size(); ++i) {
			
			Rectangle vp = layout.getVirtualPosition(children_.size(), i);
			if(vp == null) {
				continue;
			}
			
			Rectangle bounding_box = GetBoundingBox(region.kBoundingBox, 
					layout.getVirtualPosition(children_.size(), i),
					region.regionOfInterest);
			
			if(bounding_box != null) {
				last_rendered = i;
				children_.get(i).render(bounding_box, 0);
			}
		}
		
		// render this node's data
		this.renderNode(region);
		
		if(last_rendered == -1) {
			// no children rendered so constrain zoom in
			region.ZoomInLimit();
		} else if(layout.getVirtualPosition(children_.size(), last_rendered).contains(region.regionOfInterest)) {
			region.ZoomInEnable();
			return last_rendered;
		} else {
			region.ZoomInEnable();
		}
		
		return -1;
		
	}
	
	//called by parent
	private void render(Rectangle bounding_box, int depth) {
		
		// Check inside drawable region
		if(!bounding_box.overlaps(ChessViewScreen.kDrawableRegion)) {
			return;
		}
		
		if(LayoutManager.dataOnly() && bounding_box.width < ChessViewScreen.kDrawableRegion.width/2) {
			this.renderNodeDataOnly(bounding_box);
			return;
		}
		
		Layout layout = LayoutManager.getLayoutInstance();
		
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
			
			
			
			for(int i = 0; i < children_.size(); ++i) {
				Rectangle vp = layout.getVirtualPosition(children_.size(), i);
				if(vp == null) {
					continue;
				}
				children_.get(i).render(GetBoundingBox(bounding_box, vp), depth+1);
			}
			
		}
		
		this.renderNode(bounding_box);
	}
	
	public Rectangle getChildVirtualPosition(int index) {
		return LayoutManager.getLayoutInstance().getVirtualPosition(children_.size(), index);
	}
	
	public GraphSquare getChildNode(int index) {
		return children_.get(index);
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
		
		ArrayList<GraphSquare> new_children = new ArrayList<GraphSquare>();
		
		for(int i = 0; i < children_data.size(); ++i) {
			new_children.add(makeChild(children_data.get(i)));
		}
		
		this.children_ = new_children;
		
		
	}

	protected abstract boolean GenerateChildren();
		//;
	protected abstract GraphSquare makeChild(Object data);

	public void CullGrandChildrenExcept(GraphSquare avoid) {
		if(children_ == null) {
			return;
		}
		for(GraphSquare gsc : children_) {
			if(gsc != avoid) {
				//TODO remove direct modification - little bit hacky
				gsc.children_ = null;
				gsc.request_made_ = false;
			}
		}
	}

	public int getChildIndex(GraphSquare previous) {
		for(int i = 0; i < children_.size();++i) {
			if(previous == children_.get(i)) {
				return i;
			}
		}
		return -1;
	}
	
	public boolean touchDown(float x, float y, int button, ROI region) {
		
		x = region.regionOfInterest.x + region.regionOfInterest.width*((x-region.kBoundingBox.x)/region.kBoundingBox.width);
		y = region.regionOfInterest.y + region.regionOfInterest.height*((y-region.kBoundingBox.y)/region.kBoundingBox.height);
		
		
		
		Layout layout = LayoutManager.getLayoutInstance();
		Rectangle data = layout.getChessBoardPosition(0);
		
		
		if(data.contains(x,y)) {
			this.interactData((x-data.x)/data.width,(y-data.y)/data.height);
			return true;
		}

		if(children_ == null) {
			return false;
		}
		
		Rectangle vp = new Rectangle();
		for(int i = 0; i < children_.size(); ++i) {
			if(layout.getVirtualPosition(children_.size(), i) == null) {
				continue;
			}
			vp.set(layout.getVirtualPosition(children_.size(), i));
			vp.x += data.x * vp.width;
			vp.y += data.y * vp.height;
			vp.width *= data.width;
			vp.height *= data.height;
			
			if(vp.contains(x,y)) {
				children_.get(i).interactData((x-vp.x)/vp.width, (y-vp.y)/vp.height);
			}
			
		}
		
		return false;
	}
	
	protected abstract void interactData(float x, float y);
	public abstract Object getData();
	
}
