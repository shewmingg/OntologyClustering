package test;
import java.util.ArrayList;
import java.util.Arrays;

import gzm.ontology.clustering.*;
public class QuickSortTest {
	public static void main(String[] args) {
		QuickSort qs = new QuickSort();
		ArrayList<Double> test = new ArrayList<Double>(Arrays.asList(0.7,2.1,0.3,9.8,12.32,3.23));
		ArrayList<Integer> idx = qs.sort(test);
		for(int i=0;i<idx.size();i++){
			System.out.println(idx.get(i)+" ");
		}
	}
}
