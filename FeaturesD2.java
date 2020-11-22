import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

public class FeaturesD2 {


	/**
	 * This class finds the parameter values NSIM, NPNF AND NDL for all persons and saves them as individual lists
	 * @param args
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	
	public void CreateNSIM() throws Exception
	{
		
	File fileF=new File("NSIM.csv"); 
	
        HashMap<String,Integer> BigMap= new HashMap<String,Integer>();
			BufferedReader br = new BufferedReader(new FileReader(fileF));       
        	 
			 String line;

		while((line=br.readLine())!=null){
		       String[] arr = line.trim().split(",");
				
			
		
		BigMap.put(arr[0],Integer.valueOf(arr[1]));
		System.out.println(arr[0] + " : "+arr[1]);
		}
     
        
        
        
        //System.out.println("total: "+j + " avg: "+j/numDocs*numDocs);
//total=5.529775026380085E8
        File fileO=new File("OutputD2/NSimMap");
  	    FileOutputStream fos0=new FileOutputStream(fileO);
  	        ObjectOutputStream oos0=new ObjectOutputStream(fos0);
  	        oos0.writeObject(BigMap);
  	        //System.out.println(BigMap);
  	        oos0.flush();
  	        oos0.close();
  	        fos0.close();
  	    
  	   
	}
       
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
	FeaturesD2 o =new FeaturesD2();
	//If NSimMap does not exist , then uncomment the following to create NSimMap:
	//o.CreateNSIM();
		// TODO Auto-generated method stub
		
		  DocumentSimilarity ob=new DocumentSimilarity();
		     
			//Reading the map of person names and the list of news articles in which they occur
					File toRead=new File("OutputD2/PersonDocListFinal");
			        FileInputStream fis=new FileInputStream(toRead);
			        ObjectInputStream ois=new ObjectInputStream(fis);

			        HashMap<String,List<String>> mapInFile=(HashMap<String,List<String>>)ois.readObject();
			        System.out.println(mapInFile.size());
			        ois.close();
			        fis.close();
			
			        //Reading the Term Frequency Map     
			        File toRead4=new File("OutputD2/PersonTFValuesFinal");
			        FileInputStream fis4=new FileInputStream(toRead4);
			        ObjectInputStream ois4=new ObjectInputStream(fis4);

			        HashMap<String,HashMap<String,Integer>> TFMap2=(HashMap<String,HashMap<String,Integer>>)ois4.readObject();
			        ois4.close();
			        fis4.close(); 
			       //System.out.println(TFMap2.get("mrs kimball"));
			        
			   //Reading the NSIM Map     
			        File toRead3=new File("OutputD2/NSimMap");
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
			      	String path="Combined/"; //Read articles repository
			      	int length;
			       //  int totallength=0;
			         DocTF=TFMap2.get(person);
			       // System.out.println(DocTF);
			        for(int i=0;i<persondocs.size();i++)
			        {


			     	   doc1=persondocs.get(i);
			     	  // System.out.println(doc1);
			     	   int Nsim=NSimMap.get(doc1);
			     	   text1=ob.getText(path+doc1);
			     	 String ab=persondocs.get(i);

			      	Double tf=(double)DocTF.get(ab)/53;
			     	 length=ob.Tokenize(path+doc1, text1);
			      double normlength=(double)length/19895; 
			     List1.add(tf);
			     List2.add(normlength);
			     List3.add((double)Nsim/61536); //61536 earlier //2508 NSim_Again

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
			        
	                
	                File f1=new File("OutputD2/rankedNSIM.csv");
			        BufferedWriter bw1=new BufferedWriter(new FileWriter(f1));
			        File f2=new File("OutputD2/rankedNDL.csv");
			        BufferedWriter bw2=new BufferedWriter(new FileWriter(f2));
			        File f3=new File("OutputD2/rankedNTF.csv");
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
			   
					File fileF=new File("OutputD2/wordVec_all.csv");
					File fileW=new File("OutputD2/AllFeatures_D2.csv");


	BufferedReader br = new BufferedReader(new FileReader(fileF));
	
		BufferedWriter wr = new BufferedWriter(new FileWriter(fileW));

	String line=br.readLine();
	while((line=br.readLine())!=null){
	String[] arr = line.trim().split(",");
				String person = arr[0];
				arr[1]=RankNDL.get(person).toString();
				arr[2]=RankNSim.get(person).toString();
				arr[3]=RankNTF.get(person).toString();
				wr.write(person+','+arr[1]+','+arr[2]+','+arr[3]);
				String[] newArray = Arrays.copyOfRange(arr, 4, arr.length);
				for (String ar:newArray){
					wr.write(","+ar);
				}
				wr.write("\n");
				
		}
		br.close();
		wr.close();
		
	}
}
