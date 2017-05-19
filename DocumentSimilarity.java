//import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

//import org.json.simple.JSONObject;
//import org.json.simple.JSONValue;



class DocumentSimilarity
{

	
	
	
	public static <T> List<T> mode(List<? extends T> coll) {
        Map<T, Integer> seen = new HashMap<T, Integer>();
        int max = 0;
        List<T> maxElems = new ArrayList<T>();
        for (T value : coll) {
            if (seen.containsKey(value))
                seen.put(value, seen.get(value) + 1);
            else
                seen.put(value, 1);
            if (seen.get(value) > max) {
                max = seen.get(value);
                maxElems.clear();
                maxElems.add(value);
            } else if (seen.get(value) == max) {
                maxElems.add(value);
            }
        }
        return maxElems;
    }
	
	
	
	
	public int AvgLength() throws Exception{
		ArrayList<String> Documents2=new ArrayList<String>(); 
		File folder=new File("C:/Users/himanshu/Documents/CorrectedP");
		NERDemo obj=new NERDemo();
		Documents2=obj.traverseFiles(folder, Documents2);
		System.out.println(Documents2.size());
		int Tokens=0;
		DocumentSimilarity ob=new DocumentSimilarity();
		for(String doc: Documents2)
		{
			
			String text=ob.getText(doc);
			int DocLen=ob.Tokenize(doc, text);
			Tokens=Tokens+DocLen;
		}
		
		return Tokens;
	}
	
	
	public void AvgLineLen() throws Exception{

		
		ArrayList<String> Documents2=new ArrayList<String>(); 
		File folder=new File("C:/Users/himanshu/git/Thesis/Thesis/Corrected");
		NERDemo obj=new NERDemo();
		Documents2=obj.traverseFiles(folder, Documents2);
		
		DocumentSimilarity ob=new DocumentSimilarity();
		float avglinelength=0;
		for(String doc: Documents2)
		{
		File f=new File(doc);
		//File f=new File(doc);
		BufferedReader reader=new BufferedReader(new FileReader(f));
		String line;
		int total=0;
		int count=0;
		while((line=reader.readLine())!=null)
		{
			count++;
			total=total+(ob.Tokenize(doc, line));
			
		}
		reader.close();
		float length=(float)total/count;
		//System.out.println(total+" "+count);
		System.out.println("Avg line length for doc "+doc+ " is : "+length);
		avglinelength=avglinelength+length;
	  
		}
	
			System.out.println(avglinelength/50);
	}
	
	/*
	public Double SimScore(String text1, String text2) throws Exception{
	  
	//  System.out.println(text1+"\n"+text2);
		 String urlToCall = "http://www.scurtu.it/apis/documentSimilarity";
	    String content = "doc1=" + URLEncoder.encode(text1, "UTF-8") +
			      "&doc2=" + URLEncoder.encode(text2,"UTF-8");
	    
	    
	    
	    URL url = new URL(urlToCall); 
	    HttpURLConnection connection = (HttpURLConnection) url.openConnection();                       
	    connection.setDoOutput(true); 
	    connection.setDoInput (true);
	    connection.setUseCaches (false);        
	    connection.setInstanceFollowRedirects(false); 
	    connection.setRequestMethod("POST"); 
	    connection.setRequestProperty("Content-Type", "text/plain"); 
	    connection.setRequestProperty("charset", "utf-8");
	    connection.connect();
	    
	    
	    DataOutputStream output = new DataOutputStream(connection.getOutputStream());
	    output.writeBytes(content);
	    output.flush();
	    output.close();
	    
	    StringBuilder strBuffer = new StringBuilder();
	    String str = null;
	    BufferedReader input = new BufferedReader (new InputStreamReader(connection.getInputStream()));
	            while (null != ((str = input.readLine()))) {
	                strBuffer.append(str);
	      }
	      
	    Object obj=JSONValue.parse(strBuffer.toString());
	    JSONObject jsonObj = (JSONObject)obj;    
	   // System.out.println(jsonObj);
	    Double score = (Double) jsonObj.get("result");
		//System.out.println(score);
	    return score;
	    }*/
	
	public String getText(String doc) throws IOException{
		File f=new File(doc);
		//File f=new File(doc);
		BufferedReader reader=new BufferedReader(new FileReader(f));
		String line;
		
		StringBuilder  sb = new StringBuilder();
		while((line=reader.readLine())!=null)
		{sb.append(line);
		sb.append(" ");
		}
		reader.close();
		
		return sb.toString();
		
	}
	
	public int Tokenize(String Document,String DocumentText) throws Exception{
		 
		//Map<String,Integer> WordCountMap = new HashMap<String,Integer>();	
		File file1=new File(Document);
		ArrayList <String> TokenList=new ArrayList <String>();
		StringTokenizer st=new StringTokenizer(DocumentText," &|^,=:\"//.()'[]-?;+*<>");
	    Pattern p = Pattern.compile("[a-zA-z0-9]");
	
		while(st.hasMoreTokens())
		{
		
		 String token = ((String) st.nextToken()).toLowerCase();
		 Matcher m = p.matcher(token);
		 if(m.find())
		 {
			 TokenList.add(token);
			 }
		}
		return TokenList.size();
	}
	

	//Similarity between articles return nsim
//This method creates a Map of Doc with all their topic probabilities in sorted order and writes it to disk
	public void CreateDocTopicMap() throws IOException
	{
		 // HashMap<String,HashMap< Integer,Double>> TopicProbMap = new HashMap<String,HashMap<Integer,Double>>(); //HASHMAP TO CALCULATE PERSON-DOCUMENT TF VALUES 
		  HashMap<String,ArrayList<Double>> TopicProbMap = new HashMap<String,ArrayList<Double>>(); //HASHMAP TO CALCULATE PERSON-DOCUMENT TF VALUES 

		  File f=new File("G:/mallet/Output.csv");
		
			BufferedReader reader=new BufferedReader(new FileReader(f));
			String line;
			
			while((line=reader.readLine())!=null)
			{
				int i=1;
				String split[]=line.split(",");
		
				  HashMap< Integer,Double> TPMap = new HashMap<Integer,Double>(); //HASHMAP TO CALCULATE PERSON-DOCUMENT TF VALUES 
				  while(i!=61)
				  {TPMap.put(Integer.parseInt(split[i]), Double.parseDouble(split[i+1]));
				  i=i+2;
				  }
				  Map<Integer, Double> Sortedmap = new LinkedHashMap<Integer, Double>(TPMap);
		        	ArrayList<Double> ProbScore=new ArrayList<Double>();

				  for(Map.Entry<Integer,Double> entry1 :Sortedmap.entrySet())
				     // System.out.println(entry1.getKey());
				      
				          ProbScore.add(entry1.getValue());
				 
				  TopicProbMap.put(split[0], ProbScore);
				  //TopicProbMap.put(split[0], (HashMap<Integer, Double>) Sortedmap);
			}
	reader.close();
			 File fileO=new File("FinalOutput/DocTopicProbList2");
		  	    FileOutputStream fos0=new FileOutputStream(fileO);
		  	        ObjectOutputStream oos0=new ObjectOutputStream(fos0);
		  	        oos0.writeObject(TopicProbMap);
		  	        System.out.println(TopicProbMap);
		  	        oos0.flush();
		  	        oos0.close();
		  	        fos0.close();
	
	
	}
	//find topic proportion of article dno. and in every d of list of articles: get 2 vectors
	
	
	//find dist. between 2 vectors, add 1 if similarity more than some threshold
	// 
	//double div=0.0; 
	//div=klDivergence(pr1,pr2);
	//if(div>0.5)
	//Nsim++
	public static final double log2 = Math.log(2);
	
	public static double klDivergence(double[] p1, double[] p2) {
			

	      double klDiv = 0.0;

	      for (int i = 0; i < p1.length; ++i) {
	        if (p1[i] == 0) { continue; }
	        if (p2[i] == 0.0) { continue; } // Limin

	      klDiv += p1[i] * Math.log( p1[i] / p2[i] );
	      }

	      return klDiv / log2; // moved this division out of the loop -DM
	    }
	
	
	
 @SuppressWarnings("unchecked")
public static void main(String[] args) throws Exception {
 //  DocumentSimilarity object=new DocumentSimilarity();
    //	object.AvgLineLen();
		
	
	 //Code to calculate most common topic in each person category
	 
/*	 File toRead=new File("Topic10FinalOutput/PersonDocList");
        FileInputStream fis=new FileInputStream(toRead);
        ObjectInputStream ois=new ObjectInputStream(fis);

        HashMap<String,List<String>> mapInFile=(HashMap<String,List<String>>)ois.readObject();

        ois.close();
        fis.close();
        
        HashMap<String,List<String>> MapSmall= new HashMap<String,List<String>>();
        HashMap<String,List<String>> MapMedium=new HashMap<String,List<String>>();
        HashMap<String,List<String>> MapLarge=new HashMap<String,List<String>>();
        
        //print All data in MAP
        for(Map.Entry<String,List<String>> m :mapInFile.entrySet()){
           // System.out.println(m.getKey()+"  "+m.getValue());
       String person=m.getKey();
       List<String> docs=m.getValue();
       if(docs.size()<4)
    	   MapSmall.put(person, docs);
        if(docs.size()>=4 && docs.size()<16)
        	MapMedium.put(person, docs);
        if(docs.size()>=16)
        	MapLarge.put(person, docs);
        
        }
	
      //  System.out.println(MapSmall.size());
       // System.out.println(MapMedium.size());
        //System.out.println(MapLarge.size());
      
		
		
        File toRead2=new File("Topic10FinalOutput/FinalMap");
        FileInputStream fis2=new FileInputStream(toRead2);
        ObjectInputStream ois2=new ObjectInputStream(fis2);

        Map<String,Map<String,Integer>> FinalMap=( Map<String,Map<String,Integer>>)ois2.readObject();

        ois2.close();
        fis2.close();
        
    //    System.out.println(FinalMap);
        /*	List<Integer>Topics=new ArrayList<Integer>();
        for(Map.Entry<String,List<String>> m2 :MapSmall.entrySet()){
        	String person=m2.getKey();
        	Map <String,Integer>DocTopicMap=FinalMap.get(person);
        	Topics.addAll(DocTopicMap.values());
        }
        System.out.println(mode(Topics));
		
        //small code to find out details about a person from people gazetteer...
       String Pname="capt creeten";
        Map<String, Integer> Pmap=FinalMap.get(Pname);
        System.out.println(Pname+ " "+ Pmap);*/
 
	 DocumentSimilarity obj=new DocumentSimilarity();
	 obj.CreateDocTopicMap();
 
 
 
 
 }
}
