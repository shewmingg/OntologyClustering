package gzm.ontology.clustering;

import gzm.ontology.clustering.util.SimilarityType;

public class SimilarityFactory {
	public Similarity GetSimilarity(SimilarityType similarityType){
		if(similarityType == null){
			return null;
		}else if(similarityType.equals(SimilarityType.WU)){
			return new WuSimilarity();
		}else if(similarityType.equals(SimilarityType.JACCARD)){
			return new JaccardSimilarity();
		}else if(similarityType.equals(SimilarityType.SB)){
			return new SbSimilarity();
		}else if(similarityType.equals(SimilarityType.Dennai)){
			return new DennaiSimilarity();
		}
	    return null;
	}
}
