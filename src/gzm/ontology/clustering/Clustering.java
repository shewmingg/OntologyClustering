package gzm.ontology.clustering;

import java.util.ArrayList;
import java.util.List;

public abstract class Clustering {
	abstract void buildClusters(ArrayList<List<Float>> Sim);
	abstract ArrayList<Cluster> getClusters();
	
}
