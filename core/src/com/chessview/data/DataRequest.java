package com.chessview.data;

public class DataRequest {
	public Requester requester;
	public String node_data;
	
	public DataRequest(Requester requester, String node_data) {
		this.requester = requester;
		this.node_data = node_data;
	}
}
