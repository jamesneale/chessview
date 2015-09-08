package com.chessview.graph.chess;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.chessrender.ChessRenderer;
import com.chessrender.drawableboard.ChessBoardBitBasic;
import com.chessview.data.DataManager;
import com.chessview.data.DataRequest;
import com.chessview.graph.GraphSquare;
import com.chessview.graph.layout.Layout;
import com.chessview.graph.layout.LayoutManager;
import com.chessview.region.ROI;
import com.generator.albertoruibal.bitboard.Move;
import com.generator.albertoruibal.bitboard.bb.BitboardUtils;

public class ChessGraphSquare extends GraphSquare {

	private ChessRenderer cr;
	private ChessBoardBitBasic board;
	
	protected List<GraphSquare> allChildren = null;
	
	public ChessGraphSquare(ChessBoardBitBasic node_data, ChessRenderer cr) {
		super();
		board = node_data;
		this.cr = cr;
	}

	@Override
	protected void renderNode(Rectangle drawableRegion) {
		
		Layout curLayout = LayoutManager.getLayoutInstance();
		Rectangle chessboard = new Rectangle(curLayout.getChessBoardPosition(children_!=null?children_.size():0));
		
		chessboard.height *= drawableRegion.height;
		chessboard.width *= drawableRegion.width;
		chessboard.x = drawableRegion.x + drawableRegion.width*chessboard.x;
		chessboard.y = drawableRegion.y + drawableRegion.height*chessboard.y;

		cr.RenderChessBoard(this.board, chessboard);
	}
	
	@Override
	protected void renderNodeDataOnly(Rectangle drawableRegion) {
		
		Rectangle chessboard = new Rectangle(Layout.CHESSBOARD_ONLY_REGION);
		
		chessboard.height *= drawableRegion.height;
		chessboard.width *= drawableRegion.width;
		chessboard.x = drawableRegion.x + drawableRegion.width*chessboard.x;
		chessboard.y = drawableRegion.y + drawableRegion.height*chessboard.y;

		cr.RenderChessBoard(this.board, chessboard);
	}

	@Override
	protected GraphSquare makeChild(Object node_data) {
		return new ChessGraphSquare((ChessBoardBitBasic)node_data, cr);
	}
	
	@Override
	protected boolean GenerateChildren() {
		return DataManager.submitRequest(new DataRequest(this, board));
	}

	@Override
	protected void renderNode(ROI region) {
		
		Rectangle chessboard = GetBoundingBox(region.kBoundingBox, 
				LayoutManager.getLayoutInstance().getChessBoardPosition(children_!=null?children_.size():0), 
				region.regionOfInterest);
		if(chessboard != null) {
			cr.RenderChessBoardSelected(this.board, chessboard);
		}
	}
	
	
	@Override
	public String toString() {
		return board.getMove();
	}

	@Override
	protected void interactData(float x, float y) {
		if(this.children_ == null) {
			return;
		}
		int col = (int)(x/0.125f);
		int row = (int)(y/0.125f);
		if(col < 0 || col > 7 || row < 0 || row > 7) {
			return;
		}
		byte index = (byte)(row*8 + col);
		if(this.allChildren == null) {
			allChildren = this.children_;
		} else if(!board.hasPieceAt(BitboardUtils.index2Square(index))) {
			this.children_ = allChildren;
			return;
		}
		children_ = new ArrayList<GraphSquare>();
		for(GraphSquare child : allChildren) {
			ChessBoardBitBasic childData = (ChessBoardBitBasic)child.getData();
			if(Move.getFromIndex(childData.move) == index) {
				children_.add(child);
			}
		}
	}

	@Override
	public Object getData() {
		return this.board;
	}
	
}
