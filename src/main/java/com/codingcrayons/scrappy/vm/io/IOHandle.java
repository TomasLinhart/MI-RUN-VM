package com.codingcrayons.scrappy.vm.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

public class IOHandle {

	private final List<BufferedReader> readers;
	private final List<BufferedWriter> writers;

	public IOHandle() {
		readers = new ArrayList<BufferedReader>(10);
		writers = new ArrayList<BufferedWriter>(10);
	}

	public BufferedReader getReader(int index) {
		return readers.get(index);
	}

	public BufferedWriter getWriter(int index) {
		return writers.get(index);
	}

	public int store(BufferedReader br) {
		readers.add(br);
		return readers.size() - 1;
	}

	public int store(BufferedWriter wr) {
		writers.add(wr);
		return writers.size() - 1;
	}

}
