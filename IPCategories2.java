import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;


public class IPCategories2 
{
	/**
	 * This class finds the parameter values NSIM, NPNF AND NDL for all persons and saves them as individual lists
	 * @param args
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	
	//Set the number of topics to be used
	public static int numTopics=10;
	//Set the number of documents in the repository
	// It could also be 14020
	public static int numDocs=14020; 
	
	//A method to invert the map
	public static <V, K> Map<V, K> invert(Map<K, V> map) 
	{
		Map<V, K> inv = new HashMap<V, K>();
		for (Map.Entry<K, V> entry : map.entrySet())
	        inv.put(entry.getValue(), entry.getKey());
		return inv;
	}

	public void KLdiv() throws Exception
	{
		
		//Code to write KL Divergence values for all documents to disk and 	
		//Change the file name based on topic model being considered
		File fileF=new File("/Users/Haimonti/Research/IIITD/Aayushee/Code/Source-Code/Thesis/src/ReputedPersonDetection/data/TopicFinalOutput/KLDivList.txt"); 
		BufferedWriter writer1 = new BufferedWriter(new FileWriter(fileF));
		//Read the document topic probability distribution
        File toRead4=new File("/Users/Haimonti/Research/IIITD/Aayushee/Code/Source-Code/Thesis/src/ReputedPersonDetection/data/TopicFinalOutput/DocTopicPrbDist");
        FileInputStream fis4=new FileInputStream(toRead4);
        ObjectInputStream ois4=new ObjectInputStream(fis4);
        HashMap<String,double[]> TopicProbMap=(HashMap<String,double[]>)ois4.readObject();
        ois4.close();
        fis4.close();
        //Read the document ID list
        File docID=new File("/Users/Haimonti/Research/IIITD/Aayushee/Code/Source-Code/Thesis/src/ReputedPersonDetection/data/TopicFinalOutput/DocIDList");
        FileInputStream fisDocID=new FileInputStream(docID);
        ObjectInputStream oisDocID=new ObjectInputStream(fisDocID);
        HashMap<String,Integer> docIDMap=(HashMap<String,Integer>)oisDocID.readObject();
        oisDocID.close();
        fisDocID.close();
        //Now invert the doc ID mapping
        Map<Integer, String> invDocID = invert(docIDMap);
        Double j=0.0;
        int i=0;
        HashMap<String,Integer> BigMap= new HashMap<String,Integer>(numDocs);
       
        
        for(Map.Entry<String,double[]> entry1 :TopicProbMap.entrySet())
        {
        	//ArrayList <Double> BigList=new ArrayList<Double>(14020);
        	// Filename is not an actual name but just a document number
        	// Change this code some time later.
        	//String filename= invDocID.get(i);
        	//Increment i
        	i=i+1;
        	//System.out.println("processing file: "+filename);
        	//writer1.write(filename);
        	double[] d1=entry1.getValue(); //.toArray(new Double[numTopics]);
   		 		int Nsim=0;

        	 for(Map.Entry<String,double[]> entry2 :TopicProbMap.entrySet())
        	 {
        		 double[] d2=entry2.getValue();
        	   	   double prob=	DocumentSimilarity.klDivergence(d1,d2);
        	   	 //Setting a threshold of some kind!  
        	   	 if(prob<2.81)
        	   	   Nsim++;
        	   	  //BigList.add(prob);
        	   	  //j=j+prob;
        	
        	 }
        	 
         		//writer1.write(BigList.toString());
         		//writer1.write("\n");
         		//BigMap.put(filename,Nsim);
        	 //System.out.println(filename + " : "+Nsim);
        }
        //System.out.println("total: "+j + " avg: "+j/14020*14020);
        //total=5.529775026380085E8
        File fileO=new File("/Users/Haimonti/Research/IIITD/Aayushee/Code/Source-Code/Thesis/src/ReputedPersonDetection/data/TopicFinalOutput/NSimMap");
  	    FileOutputStream fos0=new FileOutputStream(fileO);
  	        ObjectOutputStream oos0=new ObjectOutputStream(fos0);
  	        oos0.writeObject(BigMap);
  	        //System.out.println(BigMap);
  	        oos0.flush();
  	        oos0.close();
  	        fos0.close();
  	        writer1.close();
  	    
  	   
	}
        //System.out.println(TopicProbMap);
    /*public void NSimCal()   
	{
    	
    	
    	File toRead=new File("FinalOutput/PersonDocList");
        FileInputStream fis=new FileInputStream(toRead);
        ObjectInputStream ois=new ObjectInputStream(fis);

        HashMap<String,List<String>> mapInFile=(HashMap<String,List<String>>)ois.readObject();
        System.out.println(mapInFile.size());
        ois.close();
        fis.close();
    	//Reading the KL Divergence Map     
        File toRead3=new File("FinalOutput/KLDiv");
        FileInputStream fis3=new FileInputStream(toRead3);
        ObjectInputStream ois3=new ObjectInputStream(fis3);

        HashMap<String,ArrayList<Double>> KLDivMap=(HashMap<String,ArrayList<Double>>)ois3.readObject();
        ois3.close();
        fis3.close();
	
        ArrayList<Double> KList=new ArrayList<Double>();
        List<String> persondocs=new ArrayList<String>();
        for(Map.Entry<String,List<String>> entry1 :mapInFile.entrySet()){	            	
        			KList.clear();
	            	persondocs.clear();
	        		System.out.println(entry1.getKey());
	              	String person=entry1.getKey();
	       		   persondocs.addAll(entry1.getValue());
	       		//   System.out.println(persondocs);
	      	String doc1;
	      	String path="C:\\Users\\AAYUSHEE\\Documents\\final\\"; //Read articles repository
	      	int Nsim=0;
	        for(int i=0;i<persondocs.size();i++)
	        {
	     	 
	     	   
	     	   doc1=path+persondocs.get(i);
	     	
	     	   KList=KLDivMap.get(doc1);
	     	   for(Double d:KList)
	     	   {
	     		   if(d<2.81)
	     			   Nsim++;
	     	   }
	     	   
	}*/
	
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception 
	{
			
		// TODO Auto-generated method stub
		
		  			DocumentSimilarity ob=new DocumentSimilarity();
		     
		  			//Reading the map of person names and the list of news articles in which they occur
					File toRead=new File("/Users/Haimonti/Research/IIITD/Aayushee/Code/Source-Code/Thesis/src/ReputedPersonDetection/data/TopicFinalOutput/PersonDocList");
			        FileInputStream fis=new FileInputStream(toRead);
			        ObjectInputStream ois=new ObjectInputStream(fis);

			        HashMap<String,List<String>> mapInFile=(HashMap<String,List<String>>)ois.readObject();
			        System.out.println("Person List of News Articles "+mapInFile.size());
			        ois.close();
			        fis.close();
			
			        //Reading the Term Frequency Map 
			        //This map should be after Coref Res
			        File toRead4=new File("/Users/Haimonti/Research/IIITD/Aayushee/Code/Source-Code/Thesis/src/ReputedPersonDetection/data/TopicFinalOutput/PersonTFValuesNER");
			        FileInputStream fis4=new FileInputStream(toRead4);
			        ObjectInputStream ois4=new ObjectInputStream(fis4);

			        HashMap<String,HashMap<String,Integer>> TFMap2=(HashMap<String,HashMap<String,Integer>>)ois4.readObject();
			        ois4.close();
			        fis4.close(); 
			       //System.out.println(TFMap2.get("mrs kimball"));
			        
			        //Call the code to generate KL divergence
			        IPCategories2 o=new IPCategories2();
					o.KLdiv();
					
			        //Reading the KL Divergence Map     
			        File toRead3=new File("/Users/Haimonti/Research/IIITD/Aayushee/Code/Source-Code/Thesis/src/ReputedPersonDetection/data/TopicFinalOutput/NSimMap");
			        FileInputStream fis3=new FileInputStream(toRead3);
			        ObjectInputStream ois3=new ObjectInputStream(fis3);

			        HashMap<String,Integer> NSimMap=(HashMap<String,Integer>)ois3.readObject();
			        ois3.close();
			        fis3.close(); 
			        
			        HashMap<String,Double> RankNDL=new HashMap<String,Double>();
			        HashMap<String,Double> RankNSim=new HashMap<String,Double>();
			        HashMap<String,Double> RankNTF=new HashMap<String,Double>();

			        ArrayList<Double> List1=new ArrayList<Double>();
			        ArrayList<Double> List2=new ArrayList<Double>();
			        ArrayList<Double> List3=new ArrayList<Double>();
	                HashMap<String, Integer>DocTF=new HashMap<String,Integer>();
	                List<String> persondocs=new ArrayList<String>();
	                int pno=0;
			        
	                for(Map.Entry<String,List<String>> entPer :mapInFile.entrySet())
	                {
			            	List1.clear();
			            	List2.clear();
			            	List3.clear();
			            	DocTF.clear();
			            	persondocs.clear();
			            	
			              	String person=entPer.getKey();
			              	
			       		   persondocs.addAll(entPer.getValue());
			        		System.out.println(++pno+" "+entPer.getKey()+ " "+persondocs.size());
					      	String doc1;
					      	String text1;
					      	//Read articles repository
					      	String path="/Users/Haimonti/Research/IIITD/Aayushee/Code/Source-Code/Thesis/src/ReputedPersonDetection/data/final/"; 
					      	int length;
					       //  int totallength=0;
					         DocTF=TFMap2.get(person);
			       //  System.out.println(DocTF);
			        for(int i=0;i<persondocs.size();i++)
			        {
			        	doc1=path+persondocs.get(i);
			     	    //System.out.println("Doc1 is "+doc1);
			     	   int Nsim=NSimMap.get(persondocs.get(i));
			     	   text1=ob.getText(doc1);
			     	   String ab=persondocs.get(i);
			     	   // Not sure why we are dividing with this number!
			     	   Double tf=(double)DocTF.get(ab)/70;
			     	   length=ob.Tokenize(doc1, text1);
			     	   // Check this number also!
			     	   double normlength=(double)length/18053; 
			     	   //Estimating Normalized Person Name Frequency
			     	   List1.add(tf);
			     	   //Estimating Normalized Document Length
			     	   List2.add(normlength);
			     	   //Estimating Normalized Similarity 
			     	   List3.add((double)Nsim/numDocs);

			        }
			        Collections.sort(List1);
			        Collections.sort(List2);
			        Collections.sort(List3);
			        RankNTF.put(person, List1.get(List1.size()-1));
			        RankNSim.put(person, List3.get(List3.size()-1));
			        RankNDL.put(person, List2.get(List2.size()-1));
	                }
			        
			        //NERDemo.RankScores(RankNTF);
			        File f1=new File("/Users/Haimonti/Research/IIITD/Aayushee/Code/Source-Code/Thesis/src/ReputedPersonDetection/data/TopicFinalOutput/rankedNSIM.csv");
			        BufferedWriter bw1=new BufferedWriter(new FileWriter(f1));
			        File f2=new File("/Users/Haimonti/Research/IIITD/Aayushee/Code/Source-Code/Thesis/src/ReputedPersonDetection/data/TopicFinalOutput/rankedNDL.csv");
			        BufferedWriter bw2=new BufferedWriter(new FileWriter(f2));
			        File f3=new File("/Users/Haimonti/Research/IIITD/Aayushee/Code/Source-Code/Thesis/src/ReputedPersonDetection/data/TopicFinalOutput/rankedNTF.csv");
			        BufferedWriter bw3=new BufferedWriter(new FileWriter(f3));
			        
			        for(Map.Entry<String,Double> m1:RankNSim.entrySet()){
			        bw1.write(m1.getKey()+","+m1.getValue());
			        bw1.write("\n");
			        }
			        
			        for(Map.Entry<String,Double> m2:RankNDL.entrySet()){
				        bw2.write(m2.getKey()+","+m2.getValue());
				        bw2.write("\n");
			        }
			        
			        for(Map.Entry<String,Double> m3:RankNTF.entrySet()){
				        bw3.write(m3.getKey()+","+m3.getValue());
				        bw3.write("\n");
			        }
			        
			       bw1.close();
			       bw2.close();
			       bw3.close();	
		
		}
}
