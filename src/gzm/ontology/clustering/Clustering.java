package gzm.ontology.clustering;

import java.util.ArrayList;
import java.util.List;

public abstract class Clustering {
	abstract void buildClusters(ArrayList<List<Double>> Sim);
	abstract ArrayList<Cluster> getClusters();
	
}
