package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;

import gzm.ontology.clustering.QuickSort;

public class WhateverTest {
	public static void main(String[] args) {
		BitSet bs1 = new BitSet();
		BitSet bs2 = new BitSet();
		bs1.set(0);
		bs1.set(4);
		bs2.set(0);
		bs2.set(3);
		bs1.and(bs2);
		System.out.println(bs1);
		
	}
}
