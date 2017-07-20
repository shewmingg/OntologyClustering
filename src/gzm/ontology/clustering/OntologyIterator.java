package gzm.ontology.clustering;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
	private ArrayList<String> __Concept = new ArrayList<String>();
	private List<List<Float>> __SimMat = new ArrayList<List<Float>>(); //Similarity matrix
	private List<List<Float>> __DisMat = new ArrayList<List<Float>>(); //Distance Matrix
	private ArrayList<Integer> __DepMat = new ArrayList<Integer>(); //Depth in the tree architecture
	private ArrayList<String> __ConceptLabel = new ArrayList<String>();
	private OntModel __m;
	private String __filePath;
	public OntologyIterator(String fileName, String filePath, String ontoName,String absolutePath){
		__fileName = fileName;
		__filePath = filePath;
		__ontoName = ontoName;
		__absolutePath = absolutePath;
	}
	public void Iterate(){
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
		
		Iterator<OntClass> i = __m.listHierarchyRootClasses();
		while (i.hasNext()) {
			IterateClass(-1, i.next());
		}
		autoSetSim();
		//writeToFile("Sim",(ArrayList<List<Float>>)__SimMat);
		//writeToFile("Dis",(ArrayList<List<Float>>)__DisMat);
		util.writeDoubleArrayList("Similarity",(ArrayList<List<Float>>)__SimMat,__filePath);
		util.writeArrayList("Concept",__Concept,__filePath);
		util.writeArrayList("ConceptLabel",__ConceptLabel,__filePath);
	}
	
	public void IterateClass(int ParIdx, OntClass cls, String SimilarityType){
		//check the concept's validity
		if(LegalCheck(cls)){	
			//concept hasn't been recorded
			if(!__Concept.contains(cls.getLocalName())){
				
				//add concept id and label to the arrays respectively
				__Concept.add(cls.getLocalName());
				__ConceptLabel.add(cls.getLabel(new String()));
				
				//increase the capacity of Similarity and Distance matrix
				enlarge2dArray((ArrayList<List<Float>>) __SimMat);
				enlarge2dArray((ArrayList<List<Float>>) __DisMat);
				
				//increase the depth array
				__DepMat.add(0);
				
				//set distance and depth of the concept
				autoSetDistAndDep(ParIdx, __Concept.indexOf(cls.getLocalName()));
			
			//concept already appeared before
			}else{
				if(ParIdx!=-1){
					for(int i=0;i<__DisMat.size();i++){
						if(__DisMat.get(ParIdx).get(i)!=0)
							__DisMat.get(__Concept.indexOf(cls.getLocalName())).set(i, __DisMat.get(ParIdx).get(i)+1);
					}
					__DisMat.get(__Concept.indexOf(cls.getLocalName())).set(ParIdx, (float)1);
					if(__DepMat.get(ParIdx)+1>__DepMat.get(__Concept.indexOf(cls.getLocalName()))){
						__DepMat.set(__Concept.indexOf(cls.getLocalName()), __DepMat.get(ParIdx)+1);
					}
				}
			}
		}
		//depth first iterate every leaves of it.
		if (cls.canAs(OntClass.class)) {
			for (Iterator<OntClass> i = cls.listSubClasses(true); i.hasNext();) {
				OntClass sub = i.next();
				IterateClass(__Concept.indexOf(cls.getLocalName()), sub);			
			}
		}
		
	}
	//given the parent index, set the child concept's distance and depth.
	public void autoSetDistAndDep(int ParIdx, int CldIdx){
		if(ParIdx!=-1){
			for(int i=0;i<__DisMat.size();i++){
				if(__DisMat.get(ParIdx).get(i)!=0)
					__DisMat.get(CldIdx).set(i, __DisMat.get(ParIdx).get(i)+1);
			}		
			__DisMat.get(CldIdx).set(ParIdx, (float)1);
			__DepMat.set(CldIdx, __DepMat.get(ParIdx)+1);
		}else{
			__DepMat.set(CldIdx, 1);
		}
		
	}
	public void autoSetSim(){
		float min=Float.MAX_VALUE;
		int index = -1;
		for(int i=0;i<__DisMat.size();i++){
			for(int j=0;j<__DisMat.size();j++){
				for(int k=0;k<__DisMat.get(i).size();k++){
					if(((__DisMat.get(i).get(k)!=0 && __DisMat.get(j).get(k)!=0) ||
							(__DisMat.get(i).get(k)!=0 && j==k) ||
							(__DisMat.get(j).get(k)!=0 &&i==k)) && i!=j){
						if(min>__DisMat.get(i).get(k)){
							min = __DisMat.get(i).get(k);
							index = k;
						}
					}						
				}
				if(index!=-1){
					__SimMat.get(i).set(j, 2*__DepMat.get(index)/(__DisMat.get(i).get(index)+
						__DisMat.get(j).get(index)+2*__DepMat.get(index)));
				}else{
					__SimMat.get(i).set(j, (float)0);
				}
				index = -1;
				min = Float.MAX_VALUE;
			}

		}
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
	
	
	protected void enlarge2dArray(ArrayList<List<Float>> a){
		for(List<Float> temp: a){
			temp.add((float)0);
		}	
		ArrayList<Float> temp  = new ArrayList<Float>();
		while(temp.size() < a.size()+1) temp.add((float) 0);
		a.add(temp);
		
	}

	
	// generate Similarity
	public void GenSim(OntClass cls){
		for (Iterator<OntClass> i = cls.listSubClasses(true); i.hasNext();) {
			OntClass sub = i.next();
			GenSim(sub);	
		}
	}
	
	
	public ArrayList<List<Float>> getSim(){
		return (ArrayList<List<Float>>)__SimMat;
	}
	

	
	
	public ArrayList<String> getConceptLabel(){
		return __ConceptLabel;
	}
	
	
}
