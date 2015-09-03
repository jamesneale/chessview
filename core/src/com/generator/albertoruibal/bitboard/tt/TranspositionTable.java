package com.generator.albertoruibal.bitboard.tt;

import java.util.Arrays;

import com.generator.albertoruibal.bitboard.Board;
import com.generator.albertoruibal.bitboard.Move;
import com.generator.albertoruibal.bitboard.bb.BitboardUtils;
/**
 * Based on the class from the Open Source engine Carballo
 * https://github.com/albertoruibal/carballo
 * originally written by Alberto Alonso Ruibal.
 * 
 * Modified for use in ChessViewer by James Neale 2015
 * 
 * Transposition table using two keys and multiprobe
 * <p/>
 * Uses part of the board's zobrist key (shifted) as the index
 *
 */
public class TranspositionTable {

	public final static int DEPTH_QS_CHECKS = 1;
	public final static int DEPTH_QS_NO_CHECKS = 0;

	public final static int TYPE_EVAL = 0;
	public final static int TYPE_EXACT_SCORE = 1;
	public final static int TYPE_FAIL_LOW = 2;
	public final static int TYPE_FAIL_HIGH = 3;

	private final static int MAX_PROBES = 4;

	public long[] keys;
	public long[] infos;
	public short[] evals;

	private int size;
	private long info;
	private short eval;
	private byte generation;
	private int entriesOccupied;

	private int score;
	private int sizeBits;

	public TranspositionTable(int sizeMb) {
		sizeBits = BitboardUtils.square2Index(sizeMb) + 16;
		size = 1 << sizeBits;
		keys = new long[size];
		infos = new long[size];
		evals = new short[size];
		entriesOccupied = 0;

		generation = 0;
	}

	public void clear() {
		entriesOccupied = 0;
		Arrays.fill(keys, 0);
	}

	public int getBestMove() {
		return (int) (info & 0x1fffff);
	}

	public int getNodeType() {
		return (int) ((info >>> 21) & 0xf);
	}

	public byte getGeneration() {
		return (byte) ((info >>> 32) & 0xff);
	}

	public byte getDepthAnalyzed() {
		return (byte) ((info >>> 40) & 0xff);
	}

	public int getScore() {
		return score;
	}

	public int getEval() {
		return eval;
	}

	public void newGeneration() {
		generation++;
	}

	public boolean isMyGeneration() {
		return getGeneration() == generation;
	}

	public void set(Board board, int nodeType, int bestMove, int score, int depthAnalyzed, int eval, boolean exclusion) {
		long key2 = board.getKey2();
		int startIndex = (int) ((exclusion ? board.getExclusionKey() : board.getKey()) >>> (64 - sizeBits));
		int replaceIndex = startIndex;
		int replaceImportance = Integer.MAX_VALUE; // A higher value, so the first entry will be the default

		for (int i = startIndex; i < startIndex + MAX_PROBES && i < size; i++) {
			info = infos[i];

			if (keys[i] == 0) { // Replace an empty TT position
				entriesOccupied++;
				replaceIndex = i;
				break;
			} else if (keys[i] == key2) { // Replace the same position
				replaceIndex = i;
				if (bestMove == Move.NONE) { // Keep previous best move
					bestMove = getBestMove();
				}
				break;
			}

			// Calculates a value with this TT entry importance
			int entryImportance = (getNodeType() == TYPE_EXACT_SCORE ? 10 : 0) + // Bonus for the PV entries
					255 - getGenerationDelta() + // The older the generation, the less importance
					getDepthAnalyzed(); // The more depth, the more importance

			// We will replace the less important entry
			if (entryImportance < replaceImportance) {
				replaceImportance = entryImportance;
				replaceIndex = i;
			}
		}

		keys[replaceIndex] = key2;
		info = (bestMove & 0x1fffff) | ((nodeType & 0xf) << 21) | (((long) (generation & 0xff)) << 32) | (((long) (depthAnalyzed & 0xff)) << 40)
				| (((long) (score & 0xffff)) << 48);

		infos[replaceIndex] = info;
		evals[replaceIndex] = (short) eval;
	}

	/**
	 * Returns the difference between the current generation and the entry generation (max 255)
	 */
	private int getGenerationDelta() {
		byte entryGeneration = (byte) ((info >>> 32) & 0xff);
		return (generation >= entryGeneration ? generation - entryGeneration : 256 + generation - entryGeneration);
	}

	public int getHashFull() {
		return (int) (1000L * entriesOccupied / size);
	}
}