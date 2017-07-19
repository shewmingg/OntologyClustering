package gzm.ontology.clustering;

import java.util.ArrayList;
import java.util.List;

public class OntologyClusterGenerator {
	String __fileName;
	String __folder;
	String __ontoName;
	String __absolutePath;
	ArrayList<Cluster> __clusters;
	
	public OntologyClusterGenerator(String fileName, String folder, String ontoName,String absolutePath){
		
		__fileName = fileName;
		__folder = folder;
		__ontoName = ontoName;
		__absolutePath = absolutePath;
	}
	public void Generate(){	
		OntologyIterator oiter = new OntologyIterator(__fileName,__folder,__ontoName,__absolutePath);
		//first time generate
//		oiter.Iterate();
//		ArrayList<String> conceptLabel = oiter.getConceptLabel();
//		ArrayList<List<Float>> Sim = oiter.getSim();


		
		// second time read from files		
		ArrayList<List<Float>> Sim = util.readDoubleArrayList("Similarity",__folder);
		ArrayList<String> conceptLabel = util.readArrayList("ConceptLabel",__folder);
		System.out.println("read similarity, conceptlabel file completed");
		
//		//knn k;
//		int k=1;
//		//metric
//		double minMetric = 0.1;
//		Clustering c = new Chameleon(k, minMetric, Sim,conceptLabel);
		
		int k = 67;
		Clustering c = new KMeans(k,Sim);
		
		c.buildClusters();
		
		
		
		ArrayList<Cluster> clusters = c.getClusters();
		
		List<List<String>> clusterText = new ArrayList<List<String>>();
		TextExtraction te = new TextExtraction();
		for(int i=0;i<clusters.size();i++){
			clusterText.add(te.formalize(clusters.get(i).getLabels(conceptLabel)));
		}
		List<List<String>> words = new ArrayList<List<String>>();// words in each cluster to be saved
		List<List<Double>> tfidf = new ArrayList<List<Double>>();// tfidf to be saved
		te.calculateTfIdf((ArrayList<List<String>>)clusterText, (ArrayList<List<String>>)words, (ArrayList<List<Double>>)tfidf);
		
		util.writeDoubleArrayList("tfidf",(ArrayList<List<Double>>)tfidf,__folder);
		
		//VSM cosine similarity
		ArrayList<List<Double>> cos = te.calculateCosSim((ArrayList<List<Double>>)tfidf);
		
		util.writeDoubleArrayList("cos",(ArrayList<List<Double>>)cos,__folder);
		te.mergeClustersWithCos(0.5, cos, clusters);
		//c.saveClusters("fc", clusters, conceptLabel,__folder);
		util.writeCluster("Clusters", clusters, __folder);
		__clusters = clusters;
	}
	
	public ArrayList<Cluster> GetClusters(){
		return __clusters;
	}
}
