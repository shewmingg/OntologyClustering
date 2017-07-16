package gzm.ontology.clustering;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.morph.WordnetStemmer;

public class TextExtraction {
	Dictionary dict;
	ArrayList<POS> pos;
	WordnetStemmer stemmer;
	public TextExtraction(){
		dict = new Dictionary(new File("dict"));
		try {
			dict.open();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		stemmer = new WordnetStemmer(dict);
		pos = new ArrayList<POS>();
		pos.add(POS.ADJECTIVE);
		pos.add(POS.ADVERB);
		pos.add(POS.VERB);
		pos.add(POS.NOUN);
	}
	//input: a: ArrayList of cluster's Strings
	//output: formalized words, tokenized, lowercased, delete common words, change to origin
	public ArrayList<String> formalize(ArrayList<String> a){
		
		
		ArrayList<String> finalString = new ArrayList<String>();
		for(int i=0;i<a.size();i++){
			if(a.get(i)!=null){
				ArrayList<String> tmp = new ArrayList<String>(Arrays.asList(a.get(i).split(" |_")));
				for(int j=0;j<tmp.size();j++){
					tmp.get(j).toLowerCase();
					if(tmp.get(j)=="the" || tmp.get(j)=="a" || tmp.get(j)=="an" || tmp.get(j)=="of"){
						tmp.remove(j);
					}
					else{
						for(int k=0;k<pos.size();k++){
							List<String> stemmed = stemmer.findStems(tmp.get(j), pos.get(k));
							if(stemmed.size()!=0){
								tmp.set(j, stemmed.get(0));
								break;
							}
						}
					}
				}
				finalString.addAll(tmp);
			}
		}

		return finalString;
	}
	

	public void calculateTfIdf(ArrayList<List<String>> a, ArrayList<List<String>> words, ArrayList<List<Double>> tfidf ){
		ArrayList<Integer> allCount = new ArrayList<Integer>();
		ArrayList<String> allWords = new ArrayList<String>();
		List<List<Double>> count = new ArrayList<List<Double>>();
		int clusterCount = 0;
		
		for(int i=0;i<a.size();i++){
			words.add(new ArrayList<String>());
			count.add(new ArrayList<Double>());
			for(int j=0;j<a.get(i).size();j++){	
				if(!a.get(i).get(j).equals("null") && a.get(i).get(j)!= null){
					clusterCount++;
					if(!words.get(i).contains(a.get(i).get(j))){
						words.get(i).add(a.get(i).get(j));
						count.get(i).add(1.0);
					}
					else{
						int index = words.get(i).indexOf(a.get(i).get(j));
						double formalCount = count.get(i).get(index);
						count.get(i).set(index,formalCount+1);
					}
				}
			}
			//get all words and their count, for later idf purpose
			for(int j=0;j<words.get(i).size();j++){				
				count.get(i).set(j, count.get(i).get(j)/clusterCount);// normalize tf
				if(!allWords.contains(words.get(i).get(j))){
					allWords.add(words.get(i).get(j));
					allCount.add(1);
				}else{	
					int index = allWords.indexOf(words.get(i).get(j));
					allCount.set(index,allCount.get(index)+1);
				}
			}
			clusterCount = 0;// reset count for cluster
		}
		
		//only existing words are calculated
//		for(int i=0;i<words.size();i++){
//			tfidf.add(new ArrayList<Double>());
//			for(int j=0;j<words.get(i).size();j++){
//				tfidf.get(i).add(count.get(i).get(j) * (1+Math.log(a.size()/allCount.get(allWords.indexOf(words.get(i).get(j))))));
//			}
//		}	
		//every word is calculated
		for(int i=0;i<words.size();i++){
			tfidf.add(new ArrayList<Double>());
			for(int j=0;j<allWords.size();j++){
				if(words.get(i).contains(allWords.get(j))){
					int index = words.get(i).indexOf(allWords.get(j));
					
					tfidf.get(i).add(count.get(i).get(index) * (1+Math.log(a.size()/allCount.get(j))));
				}
				else{
					tfidf.get(i).add(0.0);
				}
				
			}
		}
	}
	public ArrayList<List<Double>> calculateCosSim(ArrayList<List<Double>> tfidf){
		List<List<Double>> cos = new ArrayList<List<Double>>();
		double dot = 0;
		double sqr1 = 0, sqr2 = 0;
		for(int i=0;i<tfidf.size();i++){
			cos.add(new ArrayList<Double>());
			for(int j=0;j<tfidf.size();j++){
				if(i==j){
					cos.get(i).add(-2.0);
				}else{
					for(int p=0;p<tfidf.get(i).size();p++){
						dot += tfidf.get(i).get(p) * tfidf.get(j).get(p);
						sqr1+=Math.pow(tfidf.get(i).get(p),2);
						sqr2+=Math.pow(tfidf.get(j).get(p),2);
					}
					double divided = (Math.sqrt(sqr1)*Math.sqrt(sqr2));
					if(divided!= 0){
						cos.get(i).add(dot/divided);
//						if(dot/divided>0.7){
//							System.out.println(dot/divided+" "+i+" "+j);
//						}
					}else{
						cos.get(i).add(-1.0);
					}
					dot =0;
					sqr1 = 0;
					sqr2 = 0;
				}
			}
			
		}
		return (ArrayList<List<Double>>)cos;
	}
	
	public void mergeClustersWithCos(double metric, ArrayList<List<Double>> cos, ArrayList<Cluster> clusters){
		ArrayList<Integer> mergeList = new ArrayList<Integer>();
		ArrayList<Integer> visitedMap = new ArrayList<Integer>(Collections.nCopies(cos.size(), 0));
		ArrayList<Cluster> finalClusters = new ArrayList<Cluster>();
		for(int i=0;i<cos.size();i++){
			if(visitedMap.get(i)==0){
				visitedMap.set(i,1);
				for(int j=0;j<cos.get(i).size();j++){
					if(cos.get(i).get(j)>metric && visitedMap.get(j)==0){
						visitedMap.set(j, 1);
						mergeList.add(j);
						recurssiveMerge(metric,cos,j,mergeList,visitedMap);
					}
				}
				for(int j=0;j<mergeList.size();j++){
					clusters.get(i).AddIndexes(clusters.get(mergeList.get(j)).getIndexes());
				}
				finalClusters.add(clusters.get(i));
				mergeList.clear();
			}
		}
		clusters.clear();
		clusters.addAll(finalClusters);
	}
	public void recurssiveMerge(double metric, ArrayList<List<Double>> cos, int clusterIndex, ArrayList<Integer> mergeList, ArrayList<Integer> visitedMap){
		for(int i=0;i<cos.get(clusterIndex).size();i++){
			if(cos.get(clusterIndex).get(i)>metric && visitedMap.get(i)==0){
				visitedMap.set(i, 1);
				mergeList.add(i);
				recurssiveMerge(metric,cos,i,mergeList,visitedMap);
			}
		}
	}
}
