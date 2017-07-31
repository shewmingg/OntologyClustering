package gzm.ontology.clustering;

import java.util.ArrayList;
import java.util.List;

public abstract class Clustering {
	public abstract void buildClusters(ArrayList<List<Double>> Sim);
	public abstract ArrayList<Cluster> getClusters();
	
}
