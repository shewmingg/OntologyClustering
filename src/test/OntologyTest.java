package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gzm.ontology.clustering.Alignment;
import gzm.ontology.clustering.Chameleon;
import gzm.ontology.clustering.Cluster;
import gzm.ontology.clustering.Clustering;
import gzm.ontology.clustering.Evaluation;
import gzm.ontology.clustering.OntologyClusterGenerator;
import gzm.ontology.clustering.util;

public class OntologyTest {
	public static void main(String[] args) {
		String folder1 = "./university/";
		
		String similarityType = "JACCARD";
		//knn k;
		int k=1;
		//metric
		double minMetric = 0.1;
		Clustering ca1 = new Chameleon(k, minMetric);
		Clustering ca2 = new Chameleon(k, minMetric);
		
//		int k = 500;
//		Clustering ca1 = new KMeans(k);
//		Clustering ca2 = new KMeans(k);
		
		double mergeMetric = 1.0;

		OntologyClusterGenerator ocgm = new OntologyClusterGenerator("university.owl",folder1,"university"
				,"file:///Users/gaozhiming/Documents/eclipseworkspace/OntologySearchSpaceReduction/university/university.owl");
		
	//	ocgm.Generate(similarityType, ca1, mergeMetric);
		

	}
}
