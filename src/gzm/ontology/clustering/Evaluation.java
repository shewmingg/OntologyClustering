package gzm.ontology.clustering;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.FileManager;

public class Evaluation {
	//return the reference matching, each two is a set
	public ArrayList<String> ReadRDF(String fileName){
		 // create an empty model
		 Model model = ModelFactory.createDefaultModel();

		 // use the FileManager to find the input file
		 InputStream in = FileManager.get().open( fileName );
		if (in == null) {
		    throw new IllegalArgumentException(
		                                 "File: " + fileName + " not found");
		}

		// read the RDF/XML file
		model.read(in, null);

		StmtIterator iter = model.listStatements();
		
		
		ArrayList<String> a = new ArrayList<String>();
		// print out the predicate, subject and object of each statement
		while (iter.hasNext()) {
		    Statement stmt      = iter.nextStatement();  // get next statement
		//    Resource  subject   = stmt.getSubject();     // get the subject
		    Property  predicate = stmt.getPredicate();   // get the predicate
		    RDFNode   object    = stmt.getObject();      // get the object

		    
		    if(predicate.toString().equals("http://knowledgeweb.semanticweb.org/heterogeneity/alignmententity2") ||
		    		predicate.toString().equals("http://knowledgeweb.semanticweb.org/heterogeneity/alignmententity1")){
		    //	System.out.println(object.toString().split("#")[1]+"\n");
		    	a.add(object.toString().split("#")[1]);
		    }
		} 
		return a;
	}

	//generate suggestion based on clusters in two ontology
	public void GenerateSuggestion(){
		
	}
	
	public void ComputePrecisionRecallFscore(ArrayList<String> reference, Map<String,List<String>> suggested){
		int matched = 0;// count how many matching in reference get suggested.
		for(int i=1; i<reference.size();i=i+2){
			if(suggested.containsKey(reference.get(i))){
				List<String> val = suggested.get(reference.get(i));
				if(val.contains(reference.get(i-1))){
					matched++;
				}
			}
		}
		double recall = matched*2.0/reference.size();
		int suggestionCount = CountSuggestion(suggested);
		double precision = matched*1.0/suggestionCount;
		double fscore = 2* precision * recall / (precision + recall);
		
		NumberFormat formatter = new DecimalFormat("#0.00");     
		
		System.out.println("suggestion quantity: "+ suggestionCount);
		System.out.println("recall: "+ formatter.format(recall*100) + "%");	
		System.out.println("precision: "+ formatter.format(precision*100) + "%");
		System.out.println("f-score: " + formatter.format(fscore*100) + "%");
		
		
	}
	public int CountSuggestion(Map<String,List<String>> suggested){
		int count=0;
		for (Map.Entry<String, List<String>> entry : suggested.entrySet())
		{
		    count+=entry.getValue().size();
			
		}
		return count;
	}
}
