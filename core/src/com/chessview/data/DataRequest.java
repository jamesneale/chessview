package com.chessview.data;

public class DataRequest {
	public Requester requester;
	public Object node_data;
	
	public DataRequest(Requester requester, Object node_data) {
		this.requester = requester;
		this.node_data = node_data;
	}
}
