package gzm.ontology.clustering;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.FileManager;

public class Evaluation {
	//return the reference matching, each two is a set
	public List<List<String>> ReadRdf(String fileName){
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
		
		List<List<String>> fin = new ArrayList<List<String>>();
		// print out the predicate, subject and object of each statement
		while (iter.hasNext()) {
		    Statement stmt      = iter.nextStatement();  // get next statement
		//    Resource  subject   = stmt.getSubject();     // get the subject
		    Property  predicate = stmt.getPredicate();   // get the predicate
		    RDFNode   object    = stmt.getObject();      // get the object

		    
		    if(predicate.toString().equals("http://knowledgeweb.semanticweb.org/heterogeneity/alignmententity2") ||
		    		predicate.toString().equals("http://knowledgeweb.semanticweb.org/heterogeneity/alignmententity1")){
		//    	System.out.println(object.toString().split("#")[1]+"\n");

		    	a.add(object.toString().split("#")[1]);
		    	
		    }
	    	if(a.size() == 2){
	    		fin.add(new ArrayList<String>(a));
	    		a.clear();
	    	}
		} 
		return fin;
	}

	
	//generate suggestion based on clusters in two ontology
	public void GenerateSuggestion(){
		
	}
	public void ComputePrecisionRecallFscore(List<List<String>> reference, List<List<String>> suggested){
		int matched = 0;// count how many matching in reference get suggested.
		for(int i=0;i<reference.size();i++){
			if(suggested.contains(reference.get(i))){
				matched++;
			}					
		}
		double recall = matched*1.0/reference.size();
		int suggestionCount = suggested.size();
		double precision = matched*1.0/suggestionCount;
		double fscore = 2* precision * recall / (precision + recall);
		
		NumberFormat formatter = new DecimalFormat("#0.00");     
		
		System.out.println("suggestion quantity: "+ suggestionCount);
		System.out.println("recall: "+ formatter.format(recall*100) + "%");	
		System.out.println("precision: "+ formatter.format(precision*100) + "%");
		System.out.println("f-score: " + formatter.format(fscore*100) + "%");	
	}
	public void ComputePrecisionRecallFscore(List<List<String>> reference, Map<String,List<String>> suggested){
		int matched = 0;// count how many matching in reference get suggested.
		for(int i=0;i<reference.size();i++){
			if(suggested.containsKey(reference.get(i).get(0))){
				List<String> val = suggested.get(reference.get(i).get(0));
				if(val.contains(reference.get(i).get(1))){
					matched++;
				}
			}else if(suggested.containsKey(reference.get(i).get(1))){
				List<String> val = suggested.get(reference.get(i).get(1));
				if(val.contains(reference.get(i).get(0))){
					matched++;
				}
			}
		}
		double recall = matched*1.0/reference.size();
		int suggestionCount = CountSuggestion(suggested);
		double precision = matched*1.0/suggestionCount;
		double fscore = 2* precision * recall / (precision + recall);
		
		NumberFormat formatter = new DecimalFormat("#0.00");     
		
		System.out.println("suggestion quantity: "+ suggestionCount);
		System.out.println("recall: "+ formatter.format(recall*100) + "%");	
		System.out.println("precision: "+ formatter.format(precision*100) + "%");
		System.out.println("f-score: " + formatter.format(fscore*100) + "%");	
	}
	// how many suggestions contained in Map<String, List<String>>, being dimen2's count sum
	public int CountSuggestion(Map<String,List<String>> suggested){
		int count=0;
		for (Map.Entry<String, List<String>> entry : suggested.entrySet())
		{
		    count+=entry.getValue().size();
			
		}
		return count;
	}
	//extract sets of reference tuple that also occur in suggested map.
	public List<List<String>> CommonExtraction(List<List<String>> reference,Map<String,List<String>> suggested){
		List<List<String>> fin = new ArrayList<List<String>>();
		for(int i=0;i<reference.size();i++){
			if(suggested.containsKey(reference.get(i).get(0))){
				List<String> val = suggested.get(reference.get(i).get(0));
				if(val.contains(reference.get(i).get(1))){
					fin.add(new ArrayList<>(reference.get(i)));
				}
			}else if(suggested.containsKey(reference.get(i).get(1))){
				List<String> val = suggested.get(reference.get(i).get(1));
				if(val.contains(reference.get(i).get(0))){
					fin.add(new ArrayList<>(reference.get(i)));
				}
			}
		}
		return fin;
	}
}
