
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;



class TopicSim
{


//Go through output from mallet and create a document probability distribution map
public void CreateDocTopicMap() throws IOException
	{
		 // HashMap<String,HashMap< Integer,Double>> TopicProbMap = new HashMap<String,HashMap<Integer,Double>>(); //HASHMAP TO CALCULATE PERSON-DOCUMENT TF VALUES 
		  HashMap<String,ArrayList<Double>> TopicProbMap = new HashMap<String,ArrayList<Double>>(); //HASHMAP TO CALCULATE PERSON-DOCUMENT TF VALUES 

		  File f=new File("D:/Coref/lib3.7/mallet-2.0.8/combinedcomposition50.csv");
		
			BufferedReader reader=new BufferedReader(new FileReader(f));
			String line;
			
			while((line=reader.readLine())!=null)
			{
				int i=1;
				String split[]=line.split(",");
		
				  HashMap< Integer,Double> TPMap = new HashMap<Integer,Double>(); //HASHMAP TO CALCULATE PERSON-DOCUMENT TF VALUES 
				  while(i!=51) //?? SURE?
				  {TPMap.put(i, Double.parseDouble(split[i]));
				  i=i+1;
				  }
				  //System.out.println(TPMap);
				  Map<Integer, Double> Sortedmap = new LinkedHashMap<Integer, Double>(TPMap);
		        	ArrayList<Double> ProbScore=new ArrayList<Double>();

				  for(Map.Entry<Integer,Double> entry1 :Sortedmap.entrySet())
				  {  //System.out.println(entry1.getKey());
				      
				          ProbScore.add(entry1.getValue());
				 
				  TopicProbMap.put(split[0], ProbScore);
				  //TopicProbMap.put(split[0], (HashMap<Integer, Double>) Sortedmap);
			}}
			//System.out.println(TopicProbMap);
	reader.close();
			 File fileO=new File("OutputD2/DocTopicProbListD2");
		  	    FileOutputStream fos0=new FileOutputStream(fileO);
		  	        ObjectOutputStream oos0=new ObjectOutputStream(fos0);
		  	        oos0.writeObject(TopicProbMap);
		  	        System.out.println(TopicProbMap);
		  	        oos0.flush();
		  	        oos0.close();
		  	        fos0.close();
	System.out.println("written doc topic prob list");
	
	}
	
	public void KLdiv() throws Exception
	{ //Statistics obj;
	
		//Code to write KL Divergence values for all documents to disk and 	
	File fileF=new File("KLDivList.txt"); //Change the file name based on topic model being considered
	BufferedWriter writer1 = new BufferedWriter(new FileWriter(fileF));
        File toRead4=new File("OutputD2/DocTopicProbListD2");
        FileInputStream fis4=new FileInputStream(toRead4);
        ObjectInputStream ois4=new ObjectInputStream(fis4);
        HashMap<String,ArrayList<Double>> TopicProbMap=(HashMap<String,ArrayList<Double>>)ois4.readObject();
        ois4.close();
        fis4.close();
        int i=0;
		Double total=0.0;
		int numTopics=50;
		int numDocs=TopicProbMap.size();
        HashMap<String,ArrayList<Double>> BigMap= new HashMap<String,ArrayList<Double>>(numDocs);
       
		//ArrayList <Double> BigList1=new ArrayList<Double>(numDocs);

        for(Map.Entry<String,ArrayList<Double>> entry1 :TopicProbMap.entrySet()){
                  ArrayList <Double> BigList2=new ArrayList<Double>(numDocs);
					Double j=0.0;

        	String filename=entry1.getKey();
        	System.out.println("processing file: "+filename + " no. : "+i++);
        	Double[] d1=entry1.getValue().toArray(new Double[numTopics]);
			int Nsim=0;
			writer1.write(filename);
        	 for(Map.Entry<String,ArrayList<Double>> entry2 :TopicProbMap.entrySet()){
        		 Double[] d2=entry2.getValue().toArray(new Double[numTopics]);
        	   	   double prob=	DocumentSimilarity.klDivergence(d1,d2);
				   writer1.write(","+String.valueOf(prob));
					//BigList2.add(prob);
				//if(prob<8.73)
        	   	  // Nsim++;
        	   // j=j+prob;

        	
        	 }
			 writer1.write("\n");
			//BigList1.add(new Statistics(BigList2).getVariance());
    
			//Double avg=j/numDocs;
			//System.out.println(avg);
			//total+=avg;
        	// BigMap.put(filename,BigList2);
        	 //System.out.println(filename + " : "+Nsim);
        }
		       // System.out.println("total: "+total + " avg: "+total/numDocs);
/*
        System.out.println(Collections.max(BigList1)+" "+ Collections.min(BigList1));
		for (Double e: BigList1){
			writer1.write(String.valueOf(e));
			writer1.write("\n");
			writer1.flush();
		}*/
             	
//total=5.529775026380085E8 for D1 
    // total: 537121.7948302091 avg: 8.728578309123263 for D2 
  	       writer1.close();

		/*   File fileO=new File("NSimMap");
  	    FileOutputStream fos0=new FileOutputStream(fileO);
  	        ObjectOutputStream oos0=new ObjectOutputStream(fos0);
  	        oos0.writeObject(BigMap);
  	        //System.out.println(BigMap);
  	        oos0.flush();
  	        oos0.close();
  	        fos0.close();
 	     */
  	   
	}


	
 @SuppressWarnings("unchecked")
public static void main(String[] args) throws Exception {
 
 
	 TopicSim obj=new TopicSim();
	    obj.CreateDocTopicMap();
 
		obj.KLdiv();
 
 
 }
}
