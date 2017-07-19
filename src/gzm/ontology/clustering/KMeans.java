package gzm.ontology.clustering;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KMeans extends Clustering{
	int __k;//k centers
	ArrayList<List<Float>> __Sim;
	ArrayList<Cluster> __clusters;
	public KMeans(int k){
		__k = k;	
			
		__clusters = new ArrayList<Cluster>();
	}
	
	@Override
	void buildClusters( ArrayList<List<Float>> Sim) {
		__Sim = Sim;
		//ArrayList<List<Float>> means = GenerateKMeans(__k);
		ArrayList<List<Float>> means = GenerateKMeans(__k, __Sim);
		
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
	}
	private void GenerateClusters(ArrayList<List<Float>> means, ArrayList<List<Integer>> ids){
		//clear the ids, for re generate clusters
		for(int i=0;i<ids.size();i++){
			ids.get(i).clear();
		}
		for(int i=0;i<__Sim.size();i++){
			List<Float> dists = new ArrayList<Float>();
			for(int j=0;j<means.size();j++){
				dists.add(CalcDistance(__Sim.get(i), means.get(j)));
			}
			ids.get(FindSmallestIndex(dists)).add(i); //push the index of concept into the corresponding array of that mean
		}
	}

	//parameter means changed to the new mean of each cluster, return true if some means changed
	private boolean RecalcMeans(ArrayList<List<Float>> means, ArrayList<List<Integer>> ids){
		List<Float> tmp;
		boolean flag = false;
		for(int i=0;i<means.size();i++){
			//only if the cluster has some nodes
			if(ids.get(i).size()!=0){
				tmp = new ArrayList<Float>(means.get(i));
				means.get(i).clear();//reset means
				for(int j=0;j<__Sim.size();j++){
					float total = 0;//sum of number in jth index.
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
	private int FindSmallestIndex(List<Float> a){
		Float sm = Float.MAX_VALUE;
		int index = -1;
		for(int i=0;i<a.size();i++){
			if(sm> a.get(i)){
				sm = a.get(i);
				index = i;
			}
		}
		return index;
	}
	
	private float CalcDistance(List<Float> a, List<Float> b){
		float result=0;
		for(int i = 0;i<a.size();i++){
			result += Math.pow(a.get(i)-b.get(i),2);
		}
		return (float) Math.sqrt(result);
	}
	private ArrayList<List<Float>> GenerateKMeans(int k){
		Random rand = new Random();
		ArrayList<List<Float>> means = new ArrayList<List<Float>>();
		for(int i=0;i<k;i++){
			ArrayList<Float> mean = new ArrayList<Float>();
			for(int j=0;j<__Sim.size();j++){
				mean.add(rand.nextFloat());
			}
			means.add(mean);
		}
		return means;
	}
	private ArrayList<List<Float>> GenerateKMeans(int k, ArrayList<List<Float>> Sim){
		ArrayList<List<Float>> means = new ArrayList<List<Float>>();
		Random rand = new Random();
		for(int i=0;i<k;i++){
			means.add(new ArrayList<Float>(Sim.get(rand.nextInt(Sim.size()))));
		}
		return means;
	}
	
	@Override
	ArrayList<Cluster> getClusters() {
		
		return __clusters;
	}
	
}
