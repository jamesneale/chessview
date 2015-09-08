package com.chessview.data;

public class DataManager {

	private static DataRetrieval[] dataRetrievers;
	private static int nextIndex = -1;
	
	public static void setUpBoardDataRetrieval(int threads) {
		
		threads = Math.max(0, Math.min(threads, 8));
		
		dataRetrievers = new BoardDataRetrieval[threads];
		
		for(int i = 0; i < dataRetrievers.length; ++i) {
			dataRetrievers[i] = new BoardDataRetrieval();
			(new Thread(dataRetrievers[i])).start();
		}
	}

	public static boolean submitRequest(DataRequest dataRequest) {
		nextIndex = (nextIndex+1)%dataRetrievers.length;
		return dataRetrievers[nextIndex].data_requests_.offer(dataRequest);
	}
}
