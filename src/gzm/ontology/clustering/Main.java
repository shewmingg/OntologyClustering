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

public class Main {
	public static void main(String[] args) {
		String folder1 = "./mouse/";
		//String folder1 = "./university/";
		
		String folder2 = "./human/";
		
//		String similarityType = "JACCARD";
//		String similarityType = "WU";
		String similarityType = "SB";
		
		
//		//knn k;
//		int k=1;
//		//metric
//		double minMetric = 0.1;
//		Clustering ca1 = new Chameleon(k, minMetric);
//		Clustering ca2 = new Chameleon(k, minMetric);
		
//		int k = 500;
//		Clustering ca1 = new KMeans(k);
//		Clustering ca2 = new KMeans(k);
		
//		Clustering ca1 = new Birch();
//		Clustering ca2 = new Birch();
//		double mergeMetric = 1.0;
//		OntologyClusterGenerator ocgm = new OntologyClusterGenerator("mouse.owl",folder1,"mouse"
//				,"file:///Users/gaozhiming/Documents/eclipseworkspace/OntologySearchSpaceReduction/mouse/mouse.owl");
//	
////		OntologyClusterGenerator ocgm = new OntologyClusterGenerator("university.owl",folder1,"university"
////				,"file:///Users/gaozhiming/Documents/eclipseworkspace/OntologySearchSpaceReduction/university/university.owl");
//		
//		ocgm.Generate(similarityType, ca1, mergeMetric);
//		OntologyClusterGenerator ocgh = new OntologyClusterGenerator("human.owl",folder2,"human",
//				"file:///Users/gaozhiming/Documents/eclipseworkspace/OntologySearchSpaceReduction/human/human.owl");
//		ocgh.Generate(similarityType, ca2, mergeMetric);
		
		
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
		
		eva.ComputePrecisionRecallFscore(ref, m);
		List<List<String>> sambo = eva.ReadRdf("SAMBO.rdf");
		
		eva.ComputePrecisionRecallFscore(ref, sambo);
		
	}
}
