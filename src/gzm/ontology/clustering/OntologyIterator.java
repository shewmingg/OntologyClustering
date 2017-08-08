package gzm.ontology.clustering;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;


public class OntologyIterator {
	private String __fileName; 
	private String __ontoName;
	private String __absolutePath;
	private ArrayList<String> __conceptId = new ArrayList<String>();
	private List<List<Double>> __sim = new ArrayList<List<Double>>(); //Similarity matrix
	private List<List<Integer>> __dist = new ArrayList<List<Integer>>(); //Distance Matrix
	private ArrayList<Integer> __depth = new ArrayList<Integer>(); //Depth in the tree architecture
	private ArrayList<String> __conceptLabel = new ArrayList<String>();
	private OntModel __m;
	private String __filePath;
	public OntologyIterator(String fileName, String filePath, String ontoName,String absolutePath){
		__fileName = fileName;
		__filePath = filePath;
		__ontoName = ontoName;
		__absolutePath = absolutePath;
	}
	public void Iterate(Similarity similarity){
		__m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, null);
		try {
			__m.read(__absolutePath);
		}
		catch (com.hp.hpl.jena.shared.WrappedIOException e) {
			if (e.getCause() instanceof java.io.FileNotFoundException) {
				System.err.println("A java.io.FileNotFoundException caught: " 
						+ e.getCause().getMessage());
				System.err.println("You must alter the path passed to " +
						"OntModel.read() so it finds your ontology");
			}
		}
		catch (Throwable t) {
			System.err.println("Caught exception, message: " + t.getMessage());
		}
		
		Iterator<OntClass> iter = __m.listHierarchyRootClasses();
		while (iter.hasNext()) {
			IterateClass(-1, iter.next(), similarity);
		}
		//init similarity
		for(int i=0;i<__conceptId.size();i++){
			__sim.add(new ArrayList<Double>(Collections.nCopies(__conceptId.size(), (double)0)));
		}
		similarity.GenerateSimilarity(this);
		//GenerateSimilarity(similarity);

		util.writeDoubleArrayList("Similarity",(ArrayList<List<Double>>)__sim,__filePath);
		util.writeArrayList("Concept",__conceptId,__filePath);
		util.writeArrayList("ConceptLabel",__conceptLabel,__filePath);
	}
	
	public void IterateClass(int ParIdx, OntClass cls, Similarity similarity){
		//check the concept's validity
		if(LegalCheck(cls)){	
			//concept hasn't been recorded
			if(!__conceptId.contains(cls.getLocalName())){
				
				//add concept id and label to the arrays respectively
				__conceptId.add(cls.getLocalName());
				__conceptLabel.add(cls.getLabel(new String()));
				
				//increase the capacity of  Distance matrix
				enlarge2dArray((ArrayList<List<Integer>>) __dist);
				
				similarity.SetParametersFirstOccur(this, ParIdx, cls);
				
			//concept already appeared before
			}else{
				similarity.SetParametersNonFirstOccur(this, ParIdx, cls);
				
			}
		}
		//depth first iterate every leaves of it.
		if (cls.canAs(OntClass.class)) {
			for (Iterator<OntClass> i = cls.listSubClasses(true); i.hasNext();) {
				OntClass sub = i.next();
				IterateClass(__conceptId.indexOf(cls.getLocalName()), sub, similarity);			
			}
		}
		
	}
	//given the parent index, set the child concept's distance and depth.
	public void autoSetDistAndDep(int ParIdx, int CldIdx){
		if(ParIdx!=-1){
			for(int i=0;i<__dist.size();i++){
				//parent concept's connections
				if(__dist.get(ParIdx).get(i)!=0)
					//select the longest path as the distance
					if(__dist.get(CldIdx).get(i)<__dist.get(ParIdx).get(i)+1){
						__dist.get(CldIdx).set(i, __dist.get(ParIdx).get(i)+1);
					}
			}		
			__dist.get(CldIdx).set(ParIdx, 1);
			//select the longest path as depth
			if(getDepth().get(CldIdx)<getDepth().get(ParIdx)+1){
				getDepth().set(CldIdx, getDepth().get(ParIdx)+1);
			}
		}else{
			//select the longest path as depth
			if(getDepth().get(CldIdx)<1){
				getDepth().set(CldIdx, 1);
			}
		}
		
	}
	public ArrayList<BitSet> TransToBitset(ArrayList<List<Integer>> a){
		ArrayList<BitSet> bss = new ArrayList<BitSet>();
		for(int i=0;i<a.size();i++){
			BitSet bs = new BitSet();
			for(int j=0;j<a.get(i).size();j++){
				if(a.get(i).get(j) == 1){
					bs.set(j);
				}
			}
			bss.add(bs);
		}
		
		return bss;
	}
	public ArrayList<List<Integer>> FindLeastCommonParent(ArrayList<Integer> depth, ArrayList<List<Integer>> dist, int size){
		ArrayList<List<Integer>> cpidx = new ArrayList<List<Integer>>();
		for(int i=0;i<size;i++){
			cpidx.add(new ArrayList<Integer>(Collections.nCopies(size, 0)));
		}
		for(int i=0;i<size;i++){
			for(int j=i;j<size;j++){
				//same
				if(i==j){
					cpidx.get(i).set(j, i);
				}else{
					int index = -1;//least common parent index
					int max = 0; // to find the deepest common parent node
					for(int k=0;k<size;k++){
						//if a common parent is found
						//
						if((dist.get(i).get(k)!=0 && dist.get(j).get(k)!=0)||(dist.get(i).get(k)!=0 &&j==k)||(dist.get(j).get(k)!=0 && i==k)){
							if(depth.get(k)>max){
								index = k;
								max = depth.get(k);
							}
						}
					}
					//if no common parent, index still be -1
						cpidx.get(i).set(j, index);
						cpidx.get(j).set(i, index);
				}
				
			}
		}
		return cpidx;
	}
	// return max element in a
	public int findMax(ArrayList<Integer> a){
		int max = -Integer.MAX_VALUE;
		for(int t:a){
			if(t>max){
				max = t;
			}
		}
		return max;
	}
	public boolean LegalCheck(OntClass c){
		if (!c.isRestriction() && !c.isAnon()  
			&& c.getLocalName() != null && !c.getLocalName().isEmpty()){
			return true;
		}
		return false;
	}
	
	
	protected void enlarge2dArray(ArrayList<List<Integer>> a){
		for(List<Integer> temp: a){
			temp.add(0);
		}	
		ArrayList<Integer> temp  = new ArrayList<Integer>();
		while(temp.size() < a.size()+1) temp.add(0);
		a.add(temp);
	}

	
	// generate Similarity
//	public void GenSim(OntClass cls){
//		for (Iterator<OntClass> i = cls.listSubClasses(true); i.hasNext();) {
//			OntClass sub = i.next();
//			GenSim(sub);	
//		}
//	}
//	
	
	public ArrayList<List<Double>> getSim(){
		return (ArrayList<List<Double>>)__sim;
	}
	

	
	
	public ArrayList<String> getConceptLabel(){
		return __conceptLabel;
	}
	public ArrayList<Integer> getDepth() {
		return __depth;
	}
	public ArrayList<String> getConceptId(){
		return __conceptId;
	}
	public void set__depth(ArrayList<Integer> __depth) {
		this.__depth = __depth;
	}
	public List<List<Integer>> getDistance(){
		return __dist;
	}
	
}
