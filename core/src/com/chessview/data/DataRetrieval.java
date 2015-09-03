package com.chessview.data;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import com.generator.albertoruibal.bitboard.Board;
import com.generator.perft.Perft;

public abstract class DataRetrieval implements Runnable{
	
	private long node_count;
	public ArrayBlockingQueue<DataRequest> data_requests_;
	
	public DataRetrieval() {
		this.data_requests_ = new ArrayBlockingQueue<DataRequest>(2000);
	}

	protected abstract ArrayList<Object> GenerateNodes(Object node_data);

	@Override
	public void run() {
		while(true) {

			try {
				DataRequest new_request = data_requests_.take();
				ArrayList<Object> children = GenerateNodes(new_request.node_data);
				new_request.requester.AddData(children);
				this.node_count += children.size();
				
			
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
	}


	public String getNodeCount() {
		return this.node_count + "";
	}
	
}
