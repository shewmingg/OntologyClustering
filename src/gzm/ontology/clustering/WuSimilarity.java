package gzm.ontology.clustering;

import java.util.ArrayList;
import java.util.List;

public class WuSimilarity extends Similarity {

	@Override
	ArrayList<List<Float>> GetSimilarity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	void GenerateSimilarity() {
		// TODO Auto-generated method stub
		
	}
	public void GenerateSimilarity(ArrayList<List<Float>> dist, ArrayList<Integer> depth, ArrayList<List<Float>> sim){
		float min=Float.MAX_VALUE;
		int index = -1;
		for(int i=0;i<dist.size();i++){
			for(int j=0;j<dist.size();j++){
				for(int k=0;k<dist.get(i).size();k++){
					if(((dist.get(i).get(k)!=0 && dist.get(j).get(k)!=0) ||
							(dist.get(i).get(k)!=0 && j==k) ||
							(dist.get(j).get(k)!=0 &&i==k)) && i!=j){
						if(min>dist.get(i).get(k)){
							min = dist.get(i).get(k);
							index = k;
						}
					}						
				}
				if(index!=-1){
					sim.get(i).set(j, 2*depth.get(index)/(dist.get(i).get(index)+
						dist.get(j).get(index)+2*depth.get(index)));
				}else{
					sim.get(i).set(j, (float)0);
				}
				index = -1;
				min = Float.MAX_VALUE;
			}

		}
	}

}
