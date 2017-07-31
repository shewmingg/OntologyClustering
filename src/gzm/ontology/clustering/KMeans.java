package gzm.ontology.clustering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KMeans extends Clustering{
	int __k;//k centers
	ArrayList<List<Double>> __Sim;
	ArrayList<Cluster> __clusters;
	public KMeans(int k){
		__k = k;	
			
		__clusters = new ArrayList<Cluster>();
	}
	
	@Override
	public
	void buildClusters( ArrayList<List<Double>> Sim) {
		__Sim = Sim;
		//ArrayList<List<Double>> means = GenerateKMeans(__k);
		ArrayList<List<Double>> means = GenerateKMeans(__k, __Sim);
		
		ArrayList<List<Integer>> ids = new ArrayList<List<Integer>>(); // store each k's nodes.
		//init
		for(int i=0; i<__k;i++){
			ids.add(new ArrayList<Integer>());
		}
		
		GenerateClusters(means, ids);// first time clustering
		
	//	boolean changed = true;
		while(RecalcMeans(means, ids)){
			//changed = RecalcMeans(means, ids);
			GenerateClusters(means, ids);
		}
		for(int i =0;i<ids.size();i++){
			//delete empty clusters
			if(ids.get(i).size()!=0){
				__clusters.add(new Cluster(i,(ArrayList<Integer>) ids.get(i)));  
			}
		}	
		System.out.println("KMeans clusters generated");
	}
	private void GenerateClusters(ArrayList<List<Double>> means, ArrayList<List<Integer>> ids){
		//clear the ids, for re generate clusters
		for(int i=0;i<ids.size();i++){
			ids.get(i).clear();
		}
		for(int i=0;i<__Sim.size();i++){
			List<Double> dists = new ArrayList<Double>();
			for(int j=0;j<means.size();j++){
				dists.add(CalcDistance(__Sim.get(i), means.get(j)));
			}
			ids.get(FindSmallestIndex(dists)).add(i); //push the index of concept into the corresponding array of that mean
		}
	}

	//parameter means changed to the new mean of each cluster, return true if some means changed
	private boolean RecalcMeans(ArrayList<List<Double>> means, ArrayList<List<Integer>> ids){
		List<Double> tmp;
		boolean flag = false;
		for(int i=0;i<means.size();i++){
			//only if the cluster has some nodes
			if(ids.get(i).size()!=0){
				tmp = new ArrayList<Double>(means.get(i));
				means.get(i).clear();//reset means
				for(int j=0;j<__Sim.size();j++){
					double total = 0;//sum of number in jth index.
					for(int k=0;k<ids.get(i).size();k++){
						total+=__Sim.get(ids.get(i).get(k)).get(j);
					}
					means.get(i).add(total/ids.get(i).size());
				}
				if(flag == false && !tmp.equals(means.get(i))){
					
					flag = true;
				}
			}
		}
		return flag;
	}
	private int FindSmallestIndex(List<Double> a){
		Double sm = Double.MAX_VALUE;
		int index = -1;
		for(int i=0;i<a.size();i++){
			if(sm> a.get(i)){
				sm = a.get(i);
				index = i;
			}
		}
		return index;
	}
	
	private double CalcDistance(List<Double> a, List<Double> b){
		double result=0;
		for(int i = 0;i<a.size();i++){
			result += Math.pow(a.get(i)-b.get(i),2);
		}
		return (double) Math.sqrt(result);
	}
	private ArrayList<List<Double>> GenerateKMeans(int k){
		Random rand = new Random();
		ArrayList<List<Double>> means = new ArrayList<List<Double>>();
		for(int i=0;i<k;i++){
			ArrayList<Double> mean = new ArrayList<Double>();
			for(int j=0;j<__Sim.size();j++){
				mean.add(rand.nextDouble());
			}
			means.add(mean);
		}
		return means;
	}
	private ArrayList<List<Double>> GenerateKMeans(int k, ArrayList<List<Double>> Sim){
		ArrayList<List<Double>> means = new ArrayList<List<Double>>();
		ArrayList<Integer> visited = new ArrayList<Integer>(Collections.nCopies(Sim.size(), 0));
		Random rand = new Random();
		
		int selected=-1;
		int count =0;
		while(count<k){
			selected = rand.nextInt(Sim.size());
			if(visited.get(selected) == 0){
				visited.set(selected, 1);
				means.add(new ArrayList<Double>(Sim.get(selected)));
				count ++;
			}
			
		}

		return means;
	}
	
	@Override
	public ArrayList<Cluster> getClusters() {
		
		return __clusters;
	}
	
}
