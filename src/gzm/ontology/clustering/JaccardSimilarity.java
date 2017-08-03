package gzm.ontology.clustering;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import com.hp.hpl.jena.ontology.OntClass;

public class JaccardSimilarity extends Similarity{

	@Override
	void GenerateSimilarity(OntologyIterator context) {
		// TODO Auto-generated method stub
		int up = 0;
		int bot = 0;
		ArrayList<BitSet> bss = context.TransToBitset((ArrayList<List<Integer>>) context.getDistance());
		
		for(int i=0;i<bss.size();i++){
			for(int j=i;j<bss.size();j++){
				BitSet tmp = (BitSet) bss.get(i).clone();
				
				tmp.and(bss.get(j));
				up = tmp.cardinality();
				tmp = (BitSet) bss.get(i).clone();
				tmp.or(bss.get(j));
				bot = tmp.cardinality();
				context.getSim().get(i).set(j, 1.0*up/bot);
				context.getSim().get(j).set(i, 1.0*up/bot);
			}
			up = 0;
			bot = 0;
		}	
	}

	@Override
	void SetParametersFirstOccur(OntologyIterator context, int ParIdx, OntClass cls) {
		// TODO Auto-generated method stub
		if(ParIdx!=-1){
			context.getDistance().get(ParIdx).set(context.getConceptId().indexOf(cls.getLocalName()),1);
			context.getDistance().get(context.getConceptId().indexOf(cls.getLocalName())).set(ParIdx,1);
			
		}
		//distance to itself
		context.getDistance().get(context.getConceptId().indexOf(cls.getLocalName())).set(context.getConceptId().indexOf(cls.getLocalName()),1);
	}

	@Override
	void SetParametersNonFirstOccur(OntologyIterator context, int ParIdx, OntClass cls) {
		// TODO Auto-generated method stub
		if(ParIdx!=-1){
			context.getDistance().get(ParIdx).set(context.getConceptId().indexOf(cls.getLocalName()),1);
			context.getDistance().get(context.getConceptId().indexOf(cls.getLocalName())).set(ParIdx,1);
			
		}
	}

}
