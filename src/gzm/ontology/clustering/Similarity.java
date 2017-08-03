package gzm.ontology.clustering;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.ontology.OntClass;

public abstract class Similarity {
	ArrayList<List<Float>> similarity = new ArrayList<List<Float>>();
	ArrayList<List<Float>> GetSimilarity(){
		return similarity;
	}
	abstract void GenerateSimilarity(OntologyIterator context);
	abstract void SetParametersFirstOccur(OntologyIterator context,int ParIdx, OntClass cls);
	abstract void SetParametersNonFirstOccur(OntologyIterator context,int ParIdx, OntClass cls);
}
