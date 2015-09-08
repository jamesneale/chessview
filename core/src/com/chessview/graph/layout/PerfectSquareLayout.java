package com.chessview.graph.layout;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;

public class PerfectSquareLayout extends Layout {

	//private static final Rectangle DATA_POSITION = new Rectangle(0f, 0.5f, 0.5f, 0.5f);
	private static final Rectangle DATA_POSITION = new Rectangle(0.1f, 0.525f, 0.3f, 0.45f);
	
	private static final Rectangle[] THREE = new Rectangle[]{
		new Rectangle(  0f,   0f, 0.5f, 0.5f),
		new Rectangle(0.5f, 0.5f, 0.5f, 0.5f),
		new Rectangle(0.5f,   0f, 0.5f, 0.5f)
	};
	
	private static Rectangle[][] recs;
	
	
	public PerfectSquareLayout(TextureAtlas atlas) {
		super(atlas.findRegion("gridLayout"));
	
		recs = new Rectangle[20][];
		recs[0] = splitLast(THREE, 3);
		
		for(int i = 1; i < 20; ++i) {
			recs[i] = splitLast(recs[i-1], 4);
		}
		
	}
	
	@Override
	public Rectangle getVirtualPosition(int total, int index) {
		if(total <= 3) {
			return THREE[index];
		}
		total = (total-1)/12;
		if(index < recs[total].length) {
			return recs[total][index];
		}
		return null;
	}

	private Rectangle[] splitLast(Rectangle[] list, int count) {
		Rectangle[] nextList = new Rectangle[list.length + count *3];
		int unsplitRectangles = list.length - count;
		
		for(int i = 0; i < unsplitRectangles; ++i) {
			nextList[i] = list[i];
		}
		int j = unsplitRectangles;
		for(int i = unsplitRectangles;i<list.length;++i) {
			float size = list[i].height/2;
			float realSize = size * 0.9f;
			float offset = size * 0.05f;
			nextList[j++] = new Rectangle(list[i].x + offset, list[i].y + size + offset, realSize, realSize);
			nextList[j++] = new Rectangle(list[i].x + offset, list[i].y + offset, realSize, realSize);
			nextList[j++] = new Rectangle(list[i].x + size + offset, list[i].y + size + offset, realSize, realSize);
			nextList[j++] = new Rectangle(list[i].x + size + offset, list[i].y + offset, realSize, realSize);
		}
		
		return nextList;
		
	}
	
	@Override
	public final Rectangle getChessBoardPosition(int i) {
		return PerfectSquareLayout.DATA_POSITION;
	}
}
