package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

import gzm.ontology.clustering.QuickSort;

public class WhateverTest {
	public static void main(String[] args) {
		int up = 0;
		int bot = 0;
		ArrayList<BitSet> bss = new ArrayList<BitSet>();
		
		for(int i=0;i<bss.size();i++){
			for(int j=i+1;j<bss.size();j++){
				BitSet tmp = bss.get(i);
				
				tmp.and(bss.get(j));
				up = tmp.cardinality();
				tmp = bss.get(i);
				tmp.or(bss.get(j));
				bot = tmp.cardinality();
			}
			up = 0;
			bot = 0;
		}
	}
}
