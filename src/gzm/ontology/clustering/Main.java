package gzm.ontology.clustering;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.morph.WordnetStemmer;
import gzm.ontology.birch.Birch;

public class Main {
	public enum SimilarityType {
	    WU,SB,JACCARD,Dennai
	};
	public static void main(String[] args) {
		ArrayList<String> para = new ArrayList<String>();
		
		 TimeWatch watch = TimeWatch.start();
	        
	   	String folder1 = "./mouse/";
		//String folder1 = "./university/";
		
		String folder2 = "./human/";
		
//		String similarityType = "JACCARD";
//		String similarityType = "WU";
//		String similarityType = "SB";
		SimilarityType similarityType= SimilarityType.SB;
		para.add(similarityType.toString());
		SimilarityFactory sf = new SimilarityFactory();
		Similarity similarity = sf.GetSimilarity(similarityType);
		
		//knn k;
		int k=1;
		//metric
		double minMetric = 0.1;
		Clustering ca1 = new Chameleon(k, minMetric);
		Clustering ca2 = new Chameleon(k, minMetric);
//		para.add("Chameleon");
//		para.add("k:"+k.toString());
//		para.add("minMetric:"+minMetric.toString());
		
		
//		int k1 = 65;
//		int k2 = 93;
//		int k1 = 2000;
//		int k2 = 2000;
//		Clustering ca1 = new KMeans(k1);
//		Clustering ca2 = new KMeans(k2);
//		para.add("KMeans");
//		para.add("k1:"+ k1);
//		para.add("k2:" + k2);
		
		
//		Clustering ca1 = new Birch();
//		Clustering ca2 = new Birch();
//		para.add("Birch");
		
		double mergeMetric = 1.0;
		para.add("mergeMetric: "+ mergeMetric);
		
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
		double alignMetric = 0.4;
		para.add("alignmentMetric: "+alignMetric);
		Map<String,List<String>> m = alm.AlignOntologies(c1, c2, l1, l2, conceptIds1, conceptIds2, alignMetric);
		
		
        System.out.println("Elapsed Time in seconds: " + watch.time(TimeUnit.SECONDS));
        System.out.println("Elapsed Time in nano seconds: " + watch.time());
		
		//evaluation
		Evaluation eva = new Evaluation();
		List<List<String>> ref = eva.ReadRdf("reference.rdf"); // file must be in the same folder of this program
		
		List<List<String>> sambo = eva.ReadRdf("SAMBO.rdf");
		
		para.addAll(eva.ComputePrecisionRecallFscore(ref, m));
		para.addAll(eva.ComputePrecisionRecallFscore(ref, eva.CommonExtraction(sambo,m)));
		
		//second way of r p f computation
		para.addAll(eva.ComputePrecisionRecallFscore(eva.CommonExtraction(ref, m), eva.CommonExtraction(sambo, m)));
		
		util.WriteLog("experiment_result",para);
		
		
	}
}
