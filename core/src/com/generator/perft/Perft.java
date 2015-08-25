package com.generator.perft;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.chessview.data.DataRequest;
import com.chessview.data.DataRetrieval;
import com.chessview.data.Requester;
import com.generator.albertoruibal.bitboard.Board;

public class Perft implements Requester{
	
	ArrayList<String> data;
	ArrayList<String> old_data;
	int waiting;
	
	public Perft() {
		data = new ArrayList<String>();
		old_data = null;
		waiting = -1;
	}

	public static ArrayList<String> GenerateMovesFrom(String FEN) {
		Board board = new Board();
		board.setFen(FEN);
		
		ArrayList<String> boards = new ArrayList<String>();
		
		int moves[] = new int[256];
		int move_count = board.getLegalMoves(moves);
		
		for(int i = 0; i < move_count; ++i) {
			board.doMove(moves[i], true, false);
			boards.add(board.getFen());
			board.undoMove();
		}
		
		return boards;
	}
	
	public boolean isWaiting() {
		return waiting > 0;
	}

	public void AddDatum(String datum) {
		data.add(datum);
	}
	
	@Override
	public synchronized void AddData(ArrayList<String> datum) {
		data.addAll(datum);
		--waiting;
	}
	
	public void iterate(DataRetrieval[] dr) {
		old_data = data;
		data = new ArrayList<String>();
		waiting = old_data.size();
		for(int i = 0; i < old_data.size(); ++i) {
			try {
				dr[i%dr.length].data_requests_.put(new DataRequest(this, old_data.get(i)));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void perft(int depth, String FEN, DataRetrieval[] data_retrievers) {
		Perft perft = new Perft();
		perft.AddDatum(FEN);
		
		long startTime = System.nanoTime();
		for(int i = 0; i < depth; ++i) {
			perft.iterate(data_retrievers);
			
			while(perft.isWaiting()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();		
				}
			}
			System.out.println("PERFT " + (i+1) + ": " + perft.data.size() + " in " + ((System.nanoTime()-startTime)/1000000) + "ms");
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		
		int thread_count = 4;
		
		DataRetrieval[] data_retrieval = new DataRetrieval[thread_count];
		for(int i = 0; i < data_retrieval.length; ++i) {
			data_retrieval[i] = new DataRetrieval();
			(new Thread(data_retrieval[i])).start();
		}
		
		ArrayList<String> testFen = new ArrayList<String>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("C:/testfen.txt"));
			String line;
			while((line = br.readLine()) != null) {
				testFen.add(line.split(":")[0]);
			}
			br.close();
		} catch (Exception e) {
			System.out.println("Error opening test fen file");
		}
		
		for(String fen : testFen) {
			Perft.perft(4, fen, data_retrieval);
		}
		
		System.exit(0);
	}
}
