package gzm.ontology.clustering;

import java.util.ArrayList;
import java.util.List;


public class Cluster {
	int __id;
	ArrayList<Integer> __ids;
	double __weightSum = 0;
	public Cluster(int id, ArrayList<Integer> ids){
		__id = id;
		__ids = ids;
	}
	public Cluster(int id){
		__id = id;
		__ids = new ArrayList<Integer>();
	}
	public ArrayList<Integer> getIndexes(){
		return __ids;
	}
	
	public void AddIndexes(ArrayList<Integer> a){
		__ids.addAll(a);
	}
	
	public void AddIndex(int a){
		__ids.add(a);
	}
	public int getId(){
		return __id;
	}
	
	
	
	
	public double calEC(ArrayList<List<Double>> Sim,ArrayList<List<Integer>> Edge) {
		__weightSum = 0;
		for(int i: __ids){
			for(int j: __ids){
				if(i<j && Edge.get(i).get(j) == 1){
					__weightSum += Sim.get(i).get(j);
				}
			}
		}
		return __weightSum;
	}
	
	//calculate n nearestEdge to otherCluster
	public ArrayList<int[]> calNearestEdge(Cluster otherCluster, int n, ArrayList<List<Double>> Sim){
		int count = 0;
		double distance = 0;
		double minDistance = Integer.MAX_VALUE;
		ArrayList<int[]> edgeList = new ArrayList<>();
		
		Cluster c2 = null;

		try {
			c2 = (Cluster) otherCluster.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		int[] tempEdge;
		
		
		ArrayList<Integer> tmpIds1 = new ArrayList<>(__ids);
		ArrayList<Integer> tmpIds2 = new ArrayList<>(c2.getIndexes());
		
		while (count < n) {
			tempEdge = new int[2];
			minDistance = Integer.MAX_VALUE;
			
			for (int ids1 : tmpIds1) {
				for (int ids2 : tmpIds2) {
					
					if (Sim.get(ids1).get(ids2) < minDistance) {
						
						tempEdge[0] = ids1;
						tempEdge[1] = ids2;

						minDistance = Sim.get(ids1).get(ids2);
					}
				}
			}
			if(minDistance!=0){
				
				tmpIds1.remove((Object)tempEdge[0]);
				tmpIds2.remove((Object)tempEdge[1]);
				edgeList.add(tempEdge);
				count++;
			}else{
				break;
			}
			
		}

		return edgeList;
	}
	
	public ArrayList<String> getLabels(ArrayList<String> conceptLabels){
		ArrayList<String> tmp = new ArrayList<String>();
		for(int i=0;i<__ids.size();i++){
			tmp.add(conceptLabels.get(__ids.get(i)));
		}
		return tmp;
	}
	
	
	@Override
	protected Object clone() throws CloneNotSupportedException {

		//deep clone
		ArrayList<Integer> pointList = (ArrayList<Integer>) this.__ids.clone();
		Cluster cluster = new Cluster(__id, pointList);
		
		return cluster;
	}
	

}
