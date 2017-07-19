package gzm.ontology.clustering;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Chameleon extends Clustering {
	
	int __k;
	double __metric;
	List<List<Integer>> __edges = new ArrayList<List<Integer>>(); 
	List<List<Float>> __Sim;
	
	ArrayList<Cluster> __initClusters;
	ArrayList<Cluster> __finalClusters;
	
	public Chameleon(int k, double metric, ArrayList<List<Float>> Sim){
		__k = k;
		__metric = metric;

	}
	
	public void KNN(){
		List<Float> tempSim;
		ArrayList<Integer> idx;
		for(int i=0;i<__Sim.size();i++){
			tempSim =new ArrayList<Float>(__Sim.get(i));
			idx = sortSim(tempSim);
			for(int j=0;j<idx.size();j++){
				if(j<__k){
					__edges.get(i).set(idx.get(j), 1);
					__edges.get(idx.get(j)).set(i, 1);
				}
			}
		}
	}
	// return descending index array of Sim
		private ArrayList<Integer> sortSim(List<Float> array) {
			List<Float> copyArray = new ArrayList<Float>(array);
			//Collections.sort(copyArray);
			ArrayList<Integer> ids = new ArrayList<Integer>();
			int k = 0;
			double maxWeight = 0;
			
			for(int i=0; i<copyArray.size(); i++){
				maxWeight = 0;
				
				for(int j=0; j<copyArray.size(); j++){
					if(copyArray.get(j) > maxWeight){
						 maxWeight = copyArray.get(j);
						 k = j;
					}
				}	
				if(maxWeight>0){
					ids.add(k);			
					copyArray.set(k, (float)0);
				}
				else{
					break;
				}
				
			}
			
			return ids;
		}
		private void searchSmallCluster() {
			int currentId = 0;
			Cluster cluster;
			__initClusters = new ArrayList<>();
			ArrayList<Integer> idxList = null;

			ArrayList<Integer> visitMap = new ArrayList<Integer>(Collections.nCopies(__Sim.size(),0));
			for (int i = 0; i < __Sim.size(); i++) {
				
				if (visitMap.get(i) == 1) {
					continue;
				}

				idxList = new ArrayList<>();
				idxList.add(i);
				recusiveDfsSearch(i, -1, idxList,visitMap);
				
				cluster = new Cluster(currentId, idxList);
				__initClusters.add(cluster);
				
				currentId++;
			}
		}
		private void recusiveDfsSearch(int idx, int parentIdx, ArrayList<Integer> idxList, ArrayList<Integer> visitMap) {

			if (visitMap.get(idx)==1) {
				return;
			}

			visitMap.set(idx, 1);
			for (int j = 0; j < __Sim.size(); j++) {
				

				if (__edges.get(idx).get(j) == 1 && j!=parentIdx) {
					idxList.add(j);
					
					recusiveDfsSearch(j, idx, idxList,visitMap);
				}
			}
		}
		private void printClusters(ArrayList<Cluster> clusterList) {
			int i = 1;

			for (Cluster cluster : clusterList) {
				System.out.print("cluster " + i + ":");
				for (Integer p : cluster.getIndexes()) {
					System.out.print(p + " ");
				}
				System.out.println();
				i++;
			}

		}
		private void combineSubClusters() {
			Cluster cluster = null;

			__finalClusters = new ArrayList<>();

			//if only one cluster remains, exit
			while (__initClusters.size() > 1) {
				cluster = __initClusters.get(0);
				combineAndRemove(cluster, __initClusters);
			}
		}
		private ArrayList<Cluster> combineAndRemove(Cluster cluster,
				ArrayList<Cluster> clusterList) {
			ArrayList<Cluster> remainClusters;
			double metric = 0;
			double maxMetric = -Integer.MAX_VALUE;
			Cluster cluster1 = null;
			Cluster cluster2 = null;

			for (Cluster c2 : clusterList) {
				if(cluster.getId() == c2.getId()){
					continue;
				}
				
				metric = calMetricfunction(cluster, c2, 1);
				
				if (metric > maxMetric) {
					maxMetric = metric;
					cluster1 = cluster;
					cluster2 = c2;
				}
			}
			//if value exceeds metric, merge and keep on finding mergeable cluster
			if (maxMetric > __metric) {
				clusterList.remove(cluster2);
				//connect edges.
				connectClusterToCluster(cluster1, cluster2);
				//connect two clusters, add second's indexes into first cluster
				cluster1.AddIndexes(cluster2.getIndexes());
				remainClusters = combineAndRemove(cluster1, clusterList);
			} else {
				clusterList.remove(cluster);
				remainClusters = clusterList;
				__finalClusters.add(cluster);
			}

			return remainClusters;
		}
		private void connectClusterToCluster(Cluster c1, Cluster c2){
			ArrayList<int[]> connectedEdges;
			
			connectedEdges = c1.calNearestEdge(c2, 2,(ArrayList<List<Float>>)__Sim);
			
			for(int[] array: connectedEdges){
				__edges.get(array[0]).set(array[1], 1);
				__edges.get(array[1]).set(array[0], 1);
			}
		}
		
		private double calMetricfunction(Cluster c1, Cluster c2, int alpha) {
			double metricValue = 0;
			double RI = 0;
			double RC = 0;

			RI = calRI(c1, c2);
			RC = calRC(c1, c2);
			// if alpha larger than 1, more focus on RI, otherwise mofre focus on RC
			metricValue = RI * Math.pow(RC, alpha);

			return metricValue;
		}
		private double calRI(Cluster c1, Cluster c2) {
			double RI = 0;
			double EC1 = 0;
			double EC2 = 0;
			double EC1To2 = 0;

			EC1 = c1.calEC((ArrayList<List<Float>>)__Sim, (ArrayList<List<Integer>>)__edges);
			EC2 = c2.calEC((ArrayList<List<Float>>)__Sim, (ArrayList<List<Integer>>)__edges);
			EC1To2 = calEC(c1, c2);
			if((EC1+EC2)!=0){
				RI = 2 * EC1To2 / (EC1 + EC2);
			}

			return RI;
		}
		private double calEC(Cluster c1, Cluster c2) {
			double resultEC = 0;
			ArrayList<int[]> connectedEdges = null;

			connectedEdges = c1.calNearestEdge(c2, 2,(ArrayList<List<Float>>)__Sim);
			//cal summed weights connecting two clusters c1 c2
			for (int[] array : connectedEdges) {
				resultEC += __Sim.get(array[0]).get(array[1]);
			}

			return resultEC;
		}

		private double calRC(Cluster c1, Cluster c2) {
			double RC = 0;
			double EC1 = 0;
			double EC2 = 0;
			double EC1To2 = 0;
			int pNum1 = c1.getIndexes().size();
			int pNum2 = c2.getIndexes().size();

			EC1 = c1.calEC((ArrayList<List<Float>>)__Sim, (ArrayList<List<Integer>>)__edges);
			EC2 = c2.calEC((ArrayList<List<Float>>)__Sim, (ArrayList<List<Integer>>)__edges);
			EC1To2 = calEC(c1, c2);
			if((pNum2 * EC1 + pNum1 * EC2)!=0){
			RC = EC1To2 * (pNum1 + pNum2) / (pNum2 * EC1 + pNum1 * EC2);
			}
			return RC;
		}
		
		
		public ArrayList<Cluster> getClusters(){
			return __finalClusters;
		}
		
		public void buildClusters(ArrayList<List<Float>> Sim){
			__Sim = Sim;
			for(int i=0;i<Sim.size();i++){
				List<Integer> a = new ArrayList<Integer>(Collections.nCopies(Sim.size(),0));
				__edges.add(a);
			}
			KNN();
			searchSmallCluster();
//			printClusters(__initClusters);
//			saveClusters("initClusters", __initClusters,__concepts);
			combineSubClusters();
//			System.out.println("final clusters:");
//			printClusters(__finalClusters);
//			saveClusters("finalClusters",__finalClusters,__concepts);
		}
}
