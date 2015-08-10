package com.chessview.data;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import com.generator.Position;

public class DataRetrieval implements Runnable{
	
	public ArrayBlockingQueue<DataRequest> data_requests_;
	
	public DataRetrieval() {
		this.data_requests_ = new ArrayBlockingQueue<DataRequest>(2000);
	}
	

	private static ArrayList<String> GenerateNodes(String node_data) {
		return Position.GenerateMovesFrom(node_data.split(":")[0]);
	}


	@Override
	public void run() {
		while(true) {

			try {
				DataRequest new_request = data_requests_.take();
				ArrayList<String> children = GenerateNodes(new_request.node_data);
				new_request.requester.AddChildren(children);

			
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
	}
	
}
