package com.chessview.data;

import com.chessview.graph.GraphSquare;

public class DataRequest {
	public GraphSquare requester;
	public String node_data;
	
	public DataRequest(GraphSquare requester, String node_data) {
		this.requester = requester;
		this.node_data = node_data;
	}
}
