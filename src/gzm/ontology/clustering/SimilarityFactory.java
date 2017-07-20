package gzm.ontology.clustering;

public class SimilarityFactory {
	public Similarity GetSimilarity(String similarityType){
		if(similarityType == null){
			return null;
		}else if(similarityType.equalsIgnoreCase("WU")){
			return new WuSimilarity();
		}
	    return null;
	}
}
