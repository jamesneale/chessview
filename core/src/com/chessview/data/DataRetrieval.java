package com.chessview.data;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DataRetrieval implements Runnable{
	
	public ArrayBlockingQueue<DataRequest> data_requests_;
	
	public DataRetrieval() {
		this.data_requests_ = new ArrayBlockingQueue<DataRequest>(2000);
	}
	

	private static ArrayList<String> GenerateNodes(String node_data) {
		ArrayList<String> nodes = new ArrayList<String>();
		
		int max = 25;
		for(int i = 0; i < max; ++i) {
			nodes.add("Hello" + i);
		}
		
		return nodes;
	}


	@Override
	public void run() {
		while(true) {

			try {
				DataRequest new_request = data_requests_.take();
				ArrayList<String> children = GenerateNodes(new_request.node_data);
				new_request.requester.AddChildren(children);

			
			} catch (Exception e) {

			}
		}
		
	}
	
}
