package gzm.ontology.clustering;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.ontology.OntClass;

public class WuSimilarity extends Similarity {



	@Override
	void GenerateSimilarity(OntologyIterator context) {
		// TODO Auto-generated method stub
		ArrayList<List<Integer>> cpidx = context.FindLeastCommonParent(context.getDepth(), (ArrayList<List<Integer>>) context.getDistance(), context.getDepth().size());
		for(int i=0;i<context.getSim().size();i++){
			for(int j=i;j<context.getSim().size();j++){
				if(cpidx.get(i).get(j)!=-1){
					context.getSim().get(i).set(j, 2.0*context.getDepth().get(cpidx.get(i).get(j))/(context.getDepth().get(i)+context.getDepth().get(j)));
					context.getSim().get(j).set(i, 2.0*context.getDepth().get(cpidx.get(i).get(j))/(context.getDepth().get(i)+context.getDepth().get(j)));
				}
				
			}
		}
	}

	@Override
	void SetParametersFirstOccur(OntologyIterator context, int ParIdx, OntClass cls) {
		// TODO Auto-generated method stub
		context.getDepth().add(0);
		
		//set distance and depth of the concept
		context.autoSetDistAndDep(ParIdx, context.getConceptId().indexOf(cls.getLocalName()));
	}

	@Override
	void SetParametersNonFirstOccur(OntologyIterator context, int ParIdx, OntClass cls) {
		// TODO Auto-generated method stub
		if(ParIdx!=-1){
			for(int i=0;i<context.getDistance().size();i++){
				if(context.getDistance().get(ParIdx).get(i)!=0)
					context.getDistance().get(context.getConceptId().indexOf(cls.getLocalName())).set(i, context.getDistance().get(ParIdx).get(i)+1);
			}
			context.getDistance().get(context.getConceptId().indexOf(cls.getLocalName())).set(ParIdx, 1);
			if(context.getDepth().get(ParIdx)+1>context.getDepth().get(context.getConceptId().indexOf(cls.getLocalName()))){
				context.getDepth().set(context.getConceptId().indexOf(cls.getLocalName()), context.getDepth().get(ParIdx)+1);
			}
		}
		
	}

}
