package gzm.ontology.clustering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Alignment {
	
	public Map<String, List<String>> AlignOntologies(ArrayList<Cluster> c1, ArrayList<Cluster> c2,
			ArrayList<String> conceptLabel1, ArrayList<String> conceptLabel2,
			ArrayList<String> conceptIds1, ArrayList<String> conceptIds2, double metric, int topk
			) {
		// c1 and c2 vsm
		List<List<String>> clusterText = new ArrayList<List<String>>();
		TextExtraction te = new TextExtraction();
		
		te.formalizeClusters(c1, conceptLabel1,clusterText);
		te.formalizeClusters(c2, conceptLabel2,clusterText);
		
		
		List<List<String>> words = new ArrayList<List<String>>();// words in each cluster to be saved
		List<List<Double>> tfidf = new ArrayList<List<Double>>();// tfidf to be saved
		te.calculateTfIdf((ArrayList<List<String>>) clusterText, (ArrayList<List<String>>) words,
				(ArrayList<List<Double>>) tfidf);
		// VSM cosine similarity
		List<List<Double>> cos = te.calculateCosSim((ArrayList<List<Double>>) tfidf);


		// get mapped cluster

		// suggested map of every cluster's concepts in first ontology
		List<List<Integer>> cids = new ArrayList<List<Integer>>();
		for (int i = 0; i < c1.size(); i++) {
			ArrayList<Integer> tmp = new ArrayList<Integer>();
			for (int j = c1.size(); j < c1.size() + c2.size(); j++) {
				if (cos.get(i).get(j) > metric) {
					tmp.add(j - c1.size());
				}
			}
			
			cids.add(tmp);
		}


		Map<String, List<String>> m = new HashMap<String, List<String>>();

		for (int i = 0; i < cids.size(); i++) {
			Cluster csm1 = c1.get(i); // suggested mapping cluster in 1st ontology
			List<String> ids1 = util.GetClusterIds(csm1, conceptIds1);// 1st ontology cluster's ids.
			ArrayList<String> clustersConcept = new ArrayList<String>();
			for (int j = 0; j < cids.get(i).size(); j++) {
				// cluster in 2nd ontology
				Cluster csm2 = c2.get(cids.get(i).get(j));
				List<String> ids2 = util.GetClusterIds(csm2, conceptIds2);
				clustersConcept.addAll(ids2);
			}
			for (int k = 0; k < ids1.size(); k++) {
				m.put(ids1.get(k), clustersConcept);
			}
		}
		return m;
	}
	public void GenerateTfIdf(){
		
	}
}
