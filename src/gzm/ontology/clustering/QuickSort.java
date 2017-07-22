package gzm.ontology.clustering;

import java.util.ArrayList;

//specialized quicksort for this program. also maintain the index of the values
public class QuickSort  {
    private ArrayList<Double> __numbers;
    private int __size;
    private ArrayList<Integer> __idxes;
    
    //return descending indexes.
    public ArrayList<Integer> sort(ArrayList<Double> values) {
    	__idxes = new ArrayList<Integer>();
    	for(int i=0;i<values.size();i++){
    		__idxes.add(i);
    	}
        // check for empty or null array
        if (values ==null || values.size()==0){
            return __idxes;
        }
        __numbers = values;
        __size = values.size();
        quicksort(0, __size - 1);
        return __idxes;
    }

    private void quicksort(int low, int high) {
        int i = low, j = high;
        // Get the pivot element from the middle of the list
        double pivot = __numbers.get(low + (high-low)/2);

        // Divide into two lists
        while (i <= j) {
            // If the current value from the left list is smaller than the pivot
            // element then get the next element from the left list
            while (__numbers.get(i) > pivot) {
                i++;
            }
            // If the current value from the right list is larger than the pivot
            // element then get the next element from the right list
            while (__numbers.get(j) < pivot) {
                j--;
            }

            // If we have found a value in the left list which is larger than
            // the pivot element and if we have found a value in the right list
            // which is smaller than the pivot element then we exchange the
            // values.
            // As we are done we can increase i and j
            if (i <= j) {
                exchange(i, j);
                i++;
                j--;
            }
        }
        // Recursion
        if (low < j)
            quicksort(low, j);
        if (i < high)
            quicksort(i, high);
    }

    private void exchange(int i, int j) {
        double temp = __numbers.get(i);
        __numbers.set(i,__numbers.get(j));
        __numbers.set(j,temp);
        int tmpidx = __idxes.get(i);
        __idxes.set(i, __idxes.get(j));
        __idxes.set(j, tmpidx);
    }
}