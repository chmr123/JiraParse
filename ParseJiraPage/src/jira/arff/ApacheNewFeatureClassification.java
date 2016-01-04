package jira.arff;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.opencsv.CSVReader;


public class ApacheNewFeatureClassification {
	static Stemmer stemmer = new Stemmer();
	public static void main(String[] args) throws IOException {
		
		String filename = args[0];
		Set<String> allterms = new HashSet<String>();
		ArrayList<String> dic;
		Map<String, String> data = new LinkedHashMap<String, String>();
		Map<int[], String> instances = new LinkedHashMap<int[], String>();
		CSVReader reader = new CSVReader(new FileReader(filename));
	    String [] nextLine;
	    while ((nextLine = reader.readNext()) != null) {
	        // nextLine[] is an array of values from the line
	       String summary = nextLine[0].replaceAll("[^A-Za-z0-9 ]", "").toLowerCase();
	       String desc = nextLine[1].replaceAll("[^A-Za-z0-9 ]", "").toLowerCase();
	       String category = nextLine[2];
	       
	       if(category.equals("Won't Fix")) 
	    	   category = "-";
	       else 
	    	   category = "+";
	       
	       String all = summary + " " + desc;
	       String[] split = all.split("\\s+");
	       for(String s : split){
	    	   allterms.add(stem(s));
	       }  
	       data.put(all, category);
	    }
	    
	    dic =  new ArrayList<String>(allterms);
	    for(String key : data.keySet()){
	    	String[] split = key.split("\\s+");
	    	List<String> termlist = Arrays.asList(split);
	    	int[] vector = new int[allterms.size()];
	    	for(int i = 0; i < dic.size(); i++){
	    		if(termlist.contains(dic.get(i))){
	    			vector[i] = 1;
	    		}
	    	}
	    	
	    	instances.put(vector, data.get(key));
	    }
	    writearff(instances,filename,dic);
	    

	}
	
	private static String stem(String term){
		char[] chars = term.toCharArray();
		for (char c : chars)
			stemmer.add(c);
		stemmer.stem();
		String stemmed = stemmer.toString();
		return stemmed;
	}
	
	private static void writearff(Map<int[], String> instances, String filename, ArrayList<String> dic) throws IOException{
		FileWriter fw = new FileWriter(filename.replace(".csv", ".arff"));
		fw.write("@RELATION APACHE\n");
		for(String s : dic){
			fw.write("@ATTRIBUTE " + s + " NUMERIC\n");
		}
		//fw.write("@ATTRIBUTE class" + " {Fix, Won't-Fix}\n");
		fw.write("@ATTRIBUTE FixOption {'+','-'}");
		fw.write("\n@DATA\n");
		for(int[] vector : instances.keySet()){
			for(int v : vector){
				fw.write(v + ",");
			}
			fw.write(instances.get(vector) + "\n");
		}
		
		fw.flush();
		fw.close();
	}
	
	
}
