package gzm.ontology.clustering;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.morph.WordnetStemmer;
import gzm.ontology.birch.Birch;
import gzm.ontology.clustering.util.SimilarityType;

public class Main {
	public static void main(String[] args) {
		String folder1 = "./mouse/";
		//String folder1 = "./university/";
		
		String folder2 = "./human/";
		
//		String similarityType = "JACCARD";
//		String similarityType = "WU";
//		String similarityType = "SB";
		SimilarityType similarityType= SimilarityType.SB;
		
		SimilarityFactory sf = new SimilarityFactory();
		Similarity similarity = sf.GetSimilarity(similarityType);
		
//		//knn k;
//		int k=1;
//		//metric
//		double minMetric = 0.1;
//		Clustering ca1 = new Chameleon(k, minMetric);
//		Clustering ca2 = new Chameleon(k, minMetric);
		
		//int k = 500;
		Clustering ca1 = new KMeans(65);
		Clustering ca2 = new KMeans(93);
		
//		Clustering ca1 = new Birch();
//		Clustering ca2 = new Birch();
		double mergeMetric = 1.0;
		OntologyClusterGenerator ocgm = new OntologyClusterGenerator("mouse.owl",folder1,"mouse"
				,"file:///Users/gaozhiming/Documents/eclipseworkspace/OntologySearchSpaceReduction/mouse/mouse.owl");
	
//		OntologyClusterGenerator ocgm = new OntologyClusterGenerator("university.owl",folder1,"university"
//				,"file:///Users/gaozhiming/Documents/eclipseworkspace/OntologySearchSpaceReduction/university/university.owl");
		
		ocgm.Generate(similarity, ca1, mergeMetric);
		OntologyClusterGenerator ocgh = new OntologyClusterGenerator("human.owl",folder2,"human",
				"file:///Users/gaozhiming/Documents/eclipseworkspace/OntologySearchSpaceReduction/human/human.owl");
		ocgh.Generate(similarity, ca2, mergeMetric);
		
		
		//read clusters, clusters' concept label, clusters' concept id of two ontologies
		ArrayList<Cluster> c1 = util.readCluster("Clusters", folder1);
		ArrayList<Cluster> c2 = util.readCluster("Clusters", folder2);	
//		ArrayList<Cluster> c1 = new ArrayList<Cluster>();
//		for(int i=0;i<2743;i++){
//			c1.add(new Cluster(i,new ArrayList<>(Arrays.asList(i))));
//		}
//		ArrayList<Cluster> c2 = new ArrayList<Cluster>();
//		for(int i=0;i<3304;i++){
//			c2.add(new Cluster(i,new ArrayList<>(Arrays.asList(i))));
//		}
//		
		
		ArrayList<String> l1 = util.readArrayList("ConceptLabel",folder1);
		ArrayList<String> l2 = util.readArrayList("ConceptLabel",folder2);
		ArrayList<String> conceptIds1 = util.readArrayList("Concept", folder1);
		ArrayList<String> conceptIds2 = util.readArrayList("Concept", folder2);
		
		//align the clusters in two ontology, yield the matching proposal of each concept in first ontology
		Alignment alm = new Alignment();
		double alignMetric = 0.05;
		Map<String,List<String>> m = alm.AlignOntologies(c1, c2, l1, l2, conceptIds1, conceptIds2, alignMetric);
		
		//evaluation
		Evaluation eva = new Evaluation();
		List<List<String>> ref = eva.ReadRdf("reference.rdf"); // file must be in the same folder of this program
		
		List<List<String>> sambo = eva.ReadRdf("SAMBO.rdf");
		
		eva.ComputePrecisionRecallFscore(ref, m);
		eva.ComputePrecisionRecallFscore(ref, eva.CommonExtraction(sambo,m));
		
		
		//second way of r p f computation
		eva.ComputePrecisionRecallFscore(eva.CommonExtraction(ref, m), eva.CommonExtraction(sambo, m));
	}
}
