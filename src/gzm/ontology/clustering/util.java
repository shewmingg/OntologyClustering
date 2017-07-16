package gzm.ontology.clustering;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class util { 
	public static ArrayList<List<Float>> readDoubleArrayList(String fileName){
		BufferedReader br = null;
		FileReader fr = null;
		ArrayList<List<Float>> dbArray = new ArrayList<>();
		try {
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);
			String line;
			line = br.readLine();
			String[] numbers = null;
			int size = Integer.parseInt(line);
			for(int j=0;j<size;j++){
				line = br.readLine();
				numbers = line.split(" ");
				ArrayList<Float> tmp = new ArrayList<>();
				for(int i=0;i<numbers.length;i++){
					tmp.add(Float.valueOf(numbers[i].trim()));
				}
				dbArray.add((List<Float>) tmp);
			}	
						
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dbArray;
	}
	public static ArrayList<List<Float>> readDoubleArrayList(String fileName, String folder){
		BufferedReader br = null;
		FileReader fr = null;
		ArrayList<List<Float>> dbArray = new ArrayList<>();
		try {
			fr = new FileReader(folder+fileName);
			br = new BufferedReader(fr);
			String line;
			line = br.readLine();
			String[] numbers = null;
			int size = Integer.parseInt(line);
			for(int j=0;j<size;j++){
				line = br.readLine();
				numbers = line.split(" ");
				ArrayList<Float> tmp = new ArrayList<>();
				for(int i=0;i<numbers.length;i++){
					tmp.add(Float.valueOf(numbers[i].trim()));
				}
				dbArray.add((List<Float>) tmp);
			}	
						
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dbArray;
	}
	public static <T> ArrayList<T> readArrayList(String fileName){
		BufferedReader br = null;
		FileReader fr = null;
		ArrayList<T> array = new ArrayList<T>();
		try {
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);
			String line;
			line = br.readLine();
			int size = Integer.parseInt(line);
			for(int j=0;j<size;j++){
				line = br.readLine();
				array.add((T)line.trim());
			}	
						
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return array;
	}
	public static <T> ArrayList<T> readArrayList(String fileName,String folder){
		BufferedReader br = null;
		FileReader fr = null;
		ArrayList<T> array = new ArrayList<T>();
		try {
			fr = new FileReader(folder + fileName);
			br = new BufferedReader(fr);
			String line;
			line = br.readLine();
			int size = Integer.parseInt(line);
			for(int j=0;j<size;j++){
				line = br.readLine();
				array.add((T)line.trim());
			}	
						
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return array;
	}
	
	public static <T> void writeArrayList(String fileName, ArrayList<T> a){
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {			
			fw = new FileWriter(fileName); // file name is "name"
			bw = new BufferedWriter(fw);
				bw.write(a.size()+"\n");
			for(int i=0;i<a.size();i++){
				
				bw.write(a.get(i)+"\n");
			}
			System.out.println("ArrayList " + fileName +" written to file");
		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {
				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
	}
	public static <T> void writeArrayList(String fileName, ArrayList<T> a,String folder){
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {			
			fw = new FileWriter(folder + fileName); // file name is "name"
			bw = new BufferedWriter(fw);
				bw.write(a.size()+"\n");
			for(int i=0;i<a.size();i++){
				
				bw.write(a.get(i)+"\n");
			}
			System.out.println("ArrayList " + fileName +"written to file");
		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {
				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
	}
	

	public static <T> void writeDoubleArrayList(String fileName, ArrayList<List<T>> a){
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {			
			fw = new FileWriter(fileName);
			bw = new BufferedWriter(fw);
			bw.write(a.size()+"\n");
			for(int i=0;i<a.size();i++){
				for(int j=0;j<a.get(i).size();j++){
					bw.write(a.get(i).get(j)+" ");
				}
				if(i!=a.size()-1)
				bw.write("\n");
			}
			System.out.println(fileName + " written");

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {
				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
	}
	
	public static <T> void writeDoubleArrayList(String fileName, ArrayList<List<T>> a,String folder){
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {			
			fw = new FileWriter(folder + fileName);
			bw = new BufferedWriter(fw);
			bw.write(a.size()+"\n");
			for(int i=0;i<a.size();i++){
				for(int j=0;j<a.get(i).size();j++){
					bw.write(a.get(i).get(j)+" ");
				}
				if(i!=a.size()-1)
				bw.write("\n");
			}
			System.out.println("Double ArrayList "+ fileName + " written");

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {
				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
	}
	
	public static void writeCluster(String fileName,ArrayList<Cluster> c,String folder){
		BufferedWriter bw = null;
		FileWriter fw = null;
		ArrayList<Integer> indexes = null;
		try {			
			fw = new FileWriter(folder + fileName); 
			bw = new BufferedWriter(fw);
				bw.write(c.size()+"\n");
			for(int i=0;i<c.size();i++){
				indexes = c.get(i).getIndexes();
				for(int j=0;j<indexes.size();j++){
					bw.write(indexes.get(j)+" ");
				}
				bw.write("\n");
			}
			System.out.println("cluster " + fileName +" written to file");
		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {
				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
	}
	public static ArrayList<Cluster> readCluster(String fileName,String folder){
		ArrayList<Cluster> c = new ArrayList<Cluster>();
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(folder+ fileName);
			br = new BufferedReader(fr);
			String line;
			line = br.readLine();
			int size = Integer.parseInt(line);
			for(int i=0;i<size;i++){
				line = br.readLine();
				String[] a = line.split(" ");
				c.add(new Cluster(i));
				for(int j=0;j<a.length;j++){
					c.get(i).AddIndex(Integer.valueOf(a[j]));
				}
			}	
						
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}
	public static ArrayList<String> GetClusterIds(Cluster c, ArrayList<String> conceptIds){
		ArrayList<String> a = new ArrayList<String>();
		ArrayList<Integer> indexes = c.getIndexes();
		for(int i=0;i<indexes.size();i++){
			a.add(conceptIds.get(indexes.get(i)));
		}
		return a;
	}
	
}
