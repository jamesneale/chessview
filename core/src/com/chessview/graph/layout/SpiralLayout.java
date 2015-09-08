package com.chessview.graph.layout;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class SpiralLayout extends Layout {
	
		private static final Rectangle DATA_POSITION = new Rectangle(0.675f, 0.45f, 0.3f, 0.45f);
		
		private static Rectangle[] recs;
		
		private static float A = 0.020f;
		private static float B = 0.003f;
		
		
		public SpiralLayout(TextureAtlas atlas) {
			super(atlas.findRegion("gridLayout"));
		
			recs = new Rectangle[20];
			generateRecs();
			
		}
		private void generateRecs() {
		
			float size = 0.01f;
			int offset = 80;
			for(int i = 0; i < recs.length; ++i) {
				int theta = i*offset; 
				if(offset > 55) offset -= 2;
				if(i == 13) {
					theta += 10;
				}
				
				recs[i] = new Rectangle(0.48f - size + (float)(A * Math.cos(MathUtils.degreesToRadians * theta) * Math.pow(Math.E, B * theta)),
										0.6f - size/2 + (float)(1.2 * A * Math.sin(MathUtils.degreesToRadians * theta) * Math.pow(Math.E, B * theta)),
						                size,
						                size);
				size *= 1.2f;

			}
			recs[17].x += 0.05f;
			recs[12].x += 0.02f;
		}
		
		@Override
		public Rectangle getVirtualPosition(int total, int index) {
			if(index < recs.length) {
				return recs[recs.length-1-index];
			}
			return null;
		}
		
		@Override
		public final Rectangle getChessBoardPosition(int i) {
			return SpiralLayout.DATA_POSITION;
		}
}
