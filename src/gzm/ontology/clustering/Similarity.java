package gzm.ontology.clustering;

import java.util.ArrayList;
import java.util.List;

public abstract class Similarity {
	ArrayList<List<Float>> similarity = new ArrayList<List<Float>>();
	abstract ArrayList<List<Float>> GetSimilarity();
	abstract void GenerateSimilarity();
}
