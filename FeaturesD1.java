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


public class FeaturesD1 {


	/**
	 * This class finds the parameter values NSIM, NPNF AND NDL for all persons and saves them as individual lists
	 * @param args
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	
	public void KLdiv() throws Exception
	{
		//Code to write KL Divergence values for all documents to disk and 	
	File fileF=new File("KLDivList.txt"); //Change the file name based on topic model being considered
		BufferedWriter writer1 = new BufferedWriter(new FileWriter(fileF));
        File toRead4=new File("DocTopicProbListD2");
        FileInputStream fis4=new FileInputStream(toRead4);
        ObjectInputStream ois4=new ObjectInputStream(fis4);
        HashMap<String,ArrayList<Double>> TopicProbMap=(HashMap<String,ArrayList<Double>>)ois4.readObject();
        ois4.close();
        fis4.close();
        Double j=0.0;
        int i=0;
		numTopics=50;
		numDocs=TopicProbMap.size()
        HashMap<String,Integer> BigMap= new HashMap<String,Integer>(numDocs);
       
        
        for(Map.Entry<String,ArrayList<Double>> entry1 :TopicProbMap.entrySet()){
//            ArrayList <Double> BigList=new ArrayList<Double>(14020);

        	String filename=entry1.getKey();
        	System.out.println("processing file:"+i++);
        	//writer1.write(filename);
        	Double[] d1=entry1.getValue().toArray(new Double[numTopics]);
   		 int Nsim=0;

        	 for(Map.Entry<String,ArrayList<Double>> entry2 :TopicProbMap.entrySet()){
        		 Double[] d2=entry2.getValue().toArray(new Double[numTopics]);
        	   	   double prob=	DocumentSimilarity.klDivergence(d1,d2);
        	   	 if(prob<2.81)
        	   	   Nsim++;
        	   	  // BigList.add(prob);
        	   	  // j=j+prob;
        	
        	 }
        	 
         	//writer1.write(BigList.toString());
        	//writer1.write("\n");
        	 BigMap.put(filename,Nsim);
        	 System.out.println(filename + " : "+Nsim);
        }
        
        
        
        //System.out.println("total: "+j + " avg: "+j/numDocs*numDocs);
//total=5.529775026380085E8
        File fileO=new File("NSimMap");
  	    FileOutputStream fos0=new FileOutputStream(fileO);
  	        ObjectOutputStream oos0=new ObjectOutputStream(fos0);
  	        oos0.writeObject(BigMap);
  	        //System.out.println(BigMap);
  	        oos0.flush();
  	        oos0.close();
  	        fos0.close();
  	        writer1.close();
  	    
  	   
	}
       
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		
//Already calculated KL Divergence and generated NSimMap. Uncomment to create the NSimMap from Topic Probabilities:	
//FeaturesD1 o=new FeaturesD1();
	//	o.KLdiv();
		// TODO Auto-generated method stub
		
		  DocumentSimilarity ob=new DocumentSimilarity();
		     
			//Reading the map of person names and the list of news articles in which they occur
					File toRead=new File("PersonDocListNew");
			        FileInputStream fis=new FileInputStream(toRead);
			        ObjectInputStream ois=new ObjectInputStream(fis);

			        HashMap<String,List<String>> mapInFile=(HashMap<String,List<String>>)ois.readObject();
			        System.out.println(mapInFile.size());
			        ois.close();
			        fis.close();
			
			        //Reading the Term Frequency Map     
			        File toRead4=new File("PersonTFValuesCombined");
			        FileInputStream fis4=new FileInputStream(toRead4);
			        ObjectInputStream ois4=new ObjectInputStream(fis4);

			        HashMap<String,HashMap<String,Integer>> TFMap2=(HashMap<String,HashMap<String,Integer>>)ois4.readObject();
			        ois4.close();
			        fis4.close(); 
			       //System.out.println(TFMap2.get("mrs kimball"));
			        
			   //Reading the KL Divergence Map     
			        File toRead3=new File("NSimMap");
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
			        
	                
	                for(Map.Entry<String,List<String>> entry1 :mapInFile.entrySet()){
			            
			        	
			        	
			        	List1.clear();
			            	List2.clear();
			            	List3.clear();
			            	DocTF.clear();
			            	persondocs.clear();
			            	
			              	String person=entry1.getKey();
			              	
			       		   persondocs.addAll(entry1.getValue());
			        		System.out.println(++pno+" "+entry1.getKey()+ " "+persondocs.size());
			      	String doc1;
			      	String text1;
			      	String path="C:/Users/AAYUSHEE/Documents/final/"; //Read articles repository
			      	int length;
			       //  int totallength=0;
			         DocTF=TFMap2.get(person);
			        System.out.println(DocTF);
			        for(int i=0;i<persondocs.size();i++)
			        {


			     	   doc1=path+persondocs.get(i);
			     	   //System.out.println(doc1);
			     	   int Nsim=NSimMap.get(doc1);
			     	   text1=ob.getText(doc1);
			     	 String ab=persondocs.get(i);

			      	Double tf=(double)DocTF.get(ab)/70;
			     	 length=ob.Tokenize(doc1, text1);
			      double normlength=(double)length/18053; 
			     List1.add(tf);
			     List2.add(normlength);
			     List3.add((double)Nsim/14020);

			        }
			        Collections.sort(List1);
			        Collections.sort(List2);
			        Collections.sort(List3);
			        RankNTF.put(person, List1.get(List1.size()-1));
			        RankNSim.put(person, List3.get(List3.size()-1));
			        RankNDL.put(person, List2.get(List2.size()-1));

			        
			        }
			      
	               // System.out.println(RankNTF);
			        //NERDemo.RankScores(RankNTF);
			        
	                
	                File f1=new File("rankedNSIM.csv");
			        BufferedWriter bw1=new BufferedWriter(new FileWriter(f1));
			        File f2=new File("rankedNDL.csv");
			        BufferedWriter bw2=new BufferedWriter(new FileWriter(f2));
			        File f3=new File("rankedNTF.csv");
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
