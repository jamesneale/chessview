package com.chessview.graph.chess;

import com.badlogic.gdx.math.Rectangle;
import com.chessrender.ChessRenderer;
import com.chessrender.drawableboard.ChessBoardBitBasic;
import com.chessview.data.BoardDataRetrieval;
import com.chessview.data.DataRequest;
import com.chessview.graph.GraphSquare;
import com.chessview.graph.layout.Layout;
import com.chessview.graph.layout.LayoutManager;
import com.chessview.region.ROI;

public class ChessGraphSquare extends GraphSquare {

	private ChessRenderer cr;
	private ChessBoardBitBasic board;
	
	public ChessGraphSquare(BoardDataRetrieval data_retreiver, ChessBoardBitBasic node_data, ChessRenderer cr) {
		super(data_retreiver);
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
		return new ChessGraphSquare((BoardDataRetrieval)this.data_retreiver, (ChessBoardBitBasic)node_data, cr);
	}
	
	@Override
	protected boolean GenerateChildren() {
		return this.data_retreiver.data_requests_.offer(new DataRequest(this, board));
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

	

	
}
