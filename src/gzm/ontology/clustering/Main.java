package gzm.ontology.clustering;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.morph.WordnetStemmer;

public class Main {
	public static void main(String[] args) {
		String folder1 = "./mouse/";
		String folder2 = "./human/";
		
		OntologyClusterGenerator ocgm = new OntologyClusterGenerator("mouse.owl",folder1,"mouse"
				,"file:///Users/gaozhiming/Documents/eclipseworkspace/OntologySearchSpaceReduction/mouse/mouse.owl");
		ocgm.Generate();
		OntologyClusterGenerator ocgh = new OntologyClusterGenerator("human.owl",folder2,"human",
				"file:///Users/gaozhiming/Documents/eclipseworkspace/OntologySearchSpaceReduction/human/human.owl");
		ocgh.Generate();
		
		//read clusters, clusters' concept label, clusters' concept id of two ontologies
		ArrayList<Cluster> c1 = util.readCluster("Clusters", folder1);
		ArrayList<Cluster> c2 = util.readCluster("Clusters", folder2);
		ArrayList<String> l1 = util.readArrayList("ConceptLabel",folder1);
		ArrayList<String> l2 = util.readArrayList("ConceptLabel",folder2);
		ArrayList<String> conceptIds1 = util.readArrayList("Concept", folder1);
		ArrayList<String> conceptIds2 = util.readArrayList("Concept", folder2);
		
		//align the clusters in two ontology, yield the matching proposal of each concept in first ontology
		Alignment alm = new Alignment();
		Map<String,List<String>> m = alm.AlignOntologies(c1, c2, l1, l2, conceptIds1, conceptIds2);
		
		//evaluation
		Evaluation eva = new Evaluation();
		ArrayList<String> ref = eva.ReadRDF("reference.rdf"); // file must be in the same folder of this program
		eva.ComputePrecisionRecallFscore(ref, m);
	}
}
