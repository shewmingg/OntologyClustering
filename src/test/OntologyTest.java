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
import gzm.ontology.clustering.TextExtraction;
import gzm.ontology.clustering.util;

public class OntologyTest {
	public static void main(String[] args) {
		TextExtraction te = new TextExtraction();
		ArrayList<String> a = new ArrayList<String>();
		a.add("Round_Ligament_of_the_Feet");
		System.out.print(te.formalize(a));

	}
}
