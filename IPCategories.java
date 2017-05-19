import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;


public class IPCategories {

	private static final HashMap<String, Float> Float = null;
	private static final Map<? extends String, ? extends java.lang.Float> String = null;

	/**
	 * This class divides number of people according to the number of articles in which they occur.
	 * Small, Medium and Large Influential categories based on number of articles less than 4, between 4 and 16 and greater than 16.
	 *It also finds all influential people by reading all previously created maps, calculates statistics
	 *and creates the influential people list
	 * @param args
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		
		//Reading the map of person names and the list of news articles in which they occur
		File toRead=new File("FinalOutput/PersonDocList");
        FileInputStream fis=new FileInputStream(toRead);
        ObjectInputStream ois=new ObjectInputStream(fis);

        HashMap<String,List<String>> mapInFile=(HashMap<String,List<String>>)ois.readObject();
        System.out.println(mapInFile.size());
        ois.close();
        fis.close();
        /*
        HashMap<String,List<String>> MapSmall= new HashMap<String,List<String>>();
        HashMap<String,List<String>> MapMedium=new HashMap<String,List<String>>();
        HashMap<String,List<String>> MapLarge=new HashMap<String,List<String>>();
        
        //print All data in MAP
        for(Map.Entry<String,List<String>> m :mapInFile.entrySet()){
           System.out.println(m.getKey()+"  "+m.getValue());
       String person=m.getKey();
       List<String> docs=m.getValue();
       if(docs.size()<4)
    	   MapSmall.put(person, docs);
        if(docs.size()>=4 && docs.size()<16)
        	MapMedium.put(person, docs);
        if(docs.size()>=16)
        	MapLarge.put(person, docs);
        
        }*/
	
   //     System.out.println(MapSmall.size());
     //   System.out.println(MapMedium.size());
       // System.out.println(MapLarge.size());
      
        //Reading the list containing document id and the topic id associated with each document
    	File toRead2=new File("FinalOutput/DocTopicList");
        FileInputStream fis2=new FileInputStream(toRead2);
        ObjectInputStream ois2=new ObjectInputStream(fis2);

        
        HashMap<String,Integer> mapInFile2=(HashMap<String,Integer>)ois2.readObject();
        ois2.close();
        fis2.close();
        
        //Read Person name , corresponding list of article and their topics to find number of unique topics in which a person occurs
        HashMap<String,Float> PersonUniqTopics=new HashMap<String,Float>();
        for(Map.Entry<String,List<String>> m :mapInFile.entrySet()){
        	String p=m.getKey();
        	LinkedHashSet <Integer> UniqT=new LinkedHashSet<Integer>();
        	List<String> docs=m.getValue();
        	for(String doc: docs)
        		UniqT.add(mapInFile2.get(doc));
        	 PersonUniqTopics.put(p,(float)UniqT.size()/30);
        	 //Change above 30 to 100 if 100 topic model is being considered
        	 
        }
        
//System.out.println(PersonUniqTopics);
         File toRead3=new File("FinalOutput/DocLengthsMap");
        FileInputStream fis3=new FileInputStream(toRead3);
        ObjectInputStream ois3=new ObjectInputStream(fis3);

        HashMap<String,Integer> mapInFile3=(HashMap<String,Integer>)ois3.readObject();

        ois3.close();
        fis3.close();
  //  System.out.println(mapInFile3);
        
        //Code to find Avg doc length in dataset
        DocumentSimilarity ob=new DocumentSimilarity();
        /*int Tokens=ob.AvgLength();
        float AvgDocLen=Tokens/14020;
        System.out.println("Total tokens: "+Tokens);
        System.out.println("Avg Doc Length:"+AvgDocLen);*/
        
        File toRead4=new File("FinalOutput/TFMap");
        FileInputStream fis4=new FileInputStream(toRead4);
        ObjectInputStream ois4=new ObjectInputStream(fis4);

        HashMap<String,HashMap<String,Float>> TFMap2=(HashMap<String,HashMap<String,Float>>)ois4.readObject();
        ois4.close();
        fis4.close();
     //   System.out.println("tHIS TFMap is printing ok..."+TFMap2);	//This is working fine
        //File toRead5=new File("Topic100FinalOutput/DocDateMap");
        //FileInputStream fis5=new FileInputStream(toRead5);
       // ObjectInputStream ois5=new ObjectInputStream(fis5);

    //    HashMap<String,String> DocDateMap=(HashMap<String,String>)ois5.readObject();
      //  ois5.close();
        //fis5.close();
        
        
        //HashMap<String,HashMap<String,Float>>NewMap=new HashMap<String,HashMap<String,Float>>();
        HashMap<String,Float> PersonIPI=new HashMap<String,Float>();
        File fileF=new File("FinalOutput/FinalOutput30Topics20-09.csv"); //Change the file name based on topic modelbegin considered
		BufferedWriter writer1 = new BufferedWriter(new FileWriter(fileF));

        int totdocs=0;
    //   float avgperdoclen=0;
      // System.out.println(mapInFile);
        for(Map.Entry<String,List<String>> entry1 :mapInFile.entrySet()){
      System.out.println(entry1.getKey());
        	String person=entry1.getKey();
        	
            HashMap<String,Float>DocTF=new HashMap<String,Float>();
        	List<Float> IScore=new ArrayList<Float>();
            List<String> persondocs=new ArrayList<String>();
 		   persondocs.addAll(entry1.getValue());
 		//   System.out.println(persondocs);
        	totdocs=totdocs+persondocs.size();
	String doc1;
//	String doc2;
	String text1;
	//String text2;
	//Double score;
	String path="C:\\Users\\AAYUSHEE\\Documents\\final\\"; //Read articles repository
	int length;
 //  int totallength=0;
   

	DocTF=TFMap2.get(person);
//	System.out.println("Having problem here...."+DocTF);
	int totlen=mapInFile3.get(person);
	//System.out.println(mapInFile3);
	float UniqT=PersonUniqTopics.get(person);
//	System.out.println(totlen);

	//Start calculating DI for every document in a person's list
	//First calculating the paarameter NSIM for each person's list of articles
	for(int i=0;i<persondocs.size();i++)
       {
    	   double Index=0;
    	//   int Nsim=0;
    	   int Tsim=0;
    	   
    	   doc1=path+persondocs.get(i);
    	//int id1=mapInFile3.get(persondocs.get(i));
    	int tid1=mapInFile2.get(persondocs.get(i));
    
    	
   //	System.out.println("Topic ID of doc1 "+tid1);

    	   text1=ob.getText(doc1);
    	 for(int j=0;j<persondocs.size();j++)
     { 
    	 if(j!=i){
    	//	 doc2=path+persondocs.get(j);
    	 	//int id2=mapInFile3.get(persondocs.get(j));
        	int tid2=mapInFile2.get(persondocs.get(j));
      //  	System.out.println("Topic ID of doc2 "+tid2 );
        	if(tid1==tid2)
        	{//System.out.println(tid1+ "  :  "+tid2);
        		Tsim++;}
       }
    	 }
    	 String ab=persondocs.get(i);
     	Float tf=DocTF.get(ab);

    	 length=ob.Tokenize(doc1, text1);
   //   totallength+=length;
     double normlength=(double)length/18053; //To calculate NDL, divide by maximum document length=18053
     //or by total length of articles in a person's list as follows:
    //  double normlength=(double)length/totlen;
     

       Index= normlength + (float)Tsim/persondocs.size()+tf;
  System.out.println(tid1+" Person: "	+person+	"	NORM LENGTH = "+normlength+"	:	TF ="+DocTF.get(ab)+"	:	#TOPIC SIMILAR ARTICLES = "+(float)Tsim+"	: 	#ARTICLES= " +persondocs.size()+ " 	:	#ARTICLE NO.= "+persondocs.get(i)+"	:	DI="+Index);
  writer1.write(person+","+normlength+","+DocTF.get(ab)+","+(float)Tsim+","+persondocs.get(i));
    writer1.write("\n");
  IScore.add((float) Index);
    	
       }
	//float perdoclen=(float)totallength/persondocs.size();
			//avgperdoclen+=perdoclen;
    //Find maximum DI in the person's list and store this for every person , calculate IPI for every person then  
	Float IPI2 = Collections.max(IScore);
     float IPI=IPI2+UniqT;
    System.out.println("\nPERSON NAME = "+person+"	:	#UNIQUE ARTICLES = "+UniqT+ "	:	IPI = "+IPI+"\n");
       PersonIPI.put(person,IPI);
  
        }
        writer1.close();
        
     //Rank persons by their sorted IPI in decreasing order    
        
  //  float AvgLenPerDoc=(float)avgperdoclen/mapInFile.size();
     Map<String,Float>New2Map= NERDemo.RankScores(PersonIPI);
    
    // float AvgDoc= (float)totdocs/mapInFile.size();
   //    System.out.println(AvgDoc+ " "+AvgLenPerDoc);
      
     //Get statistics for each category of People Gazetteer, change the values of 4 and 16 to check for
     //different category values
     float TotIPI=0;
       int a=0;
       for(Map.Entry<String,Float> entry2 :New2Map.entrySet()){
    	   a++;
    	   List <String> docs=mapInFile.get(entry2.getKey());
    	   float t=PersonUniqTopics.get(entry2.getKey());
    	   if(docs.size()<4)
    	   System.out.println(entry2.getKey()+ "	"+entry2.getValue()+"	"+docs.size()+	"	Marginally Influential	"	+t+	" 	"+a);
    	   if(docs.size()>=4 && docs.size()<16)
        	   System.out.println(entry2.getKey()+ "	"+entry2.getValue()+"	"+docs.size()+	"	Medium Influential	"+t+	" 	"+a);
    	   if(docs.size()>=16)
        	   System.out.println(entry2.getKey()+ "	"+entry2.getValue()+"	"+docs.size()	+	"	Highly Influential	"+t+	" 	"+a);
       float IPIndex=entry2.getValue();
       TotIPI+=IPIndex;
       }
       float AvgIPI=TotIPI/New2Map.size();
 // System.out.println("Ttotal IPI"+TotIPI+" PersonIPI.size(): "+PersonIPI.size()+ "	Avg IPI: "+AvgIPI);   
	}

       
       
}
