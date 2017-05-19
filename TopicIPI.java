import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class TopicIPI {

	/** Topic wise statistics..topic wise IPI
	 * This class divides number of people according to the number of articles in which they occur.
	 * Small, Medium and Large Influential categories based on number of articles less than 4, between 4 and 16 and greater than 16.
	 * @param args
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		File toRead=new File("Topic10FinalOutput/PersonDocList");
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
	
   //     System.out.println(MapSmall.size());
     //   System.out.println(MapMedium.size());
       // System.out.println(MapLarge.size());
      
        
    	File toRead2=new File("Topic10FinalOutput/DocTopicList");
        FileInputStream fis2=new FileInputStream(toRead2);
        ObjectInputStream ois2=new ObjectInputStream(fis2);

        HashMap<String,Integer> mapInFile2=(HashMap<String,Integer>)ois2.readObject();

        ois2.close();
        fis2.close();
        HashMap<String,Set<Integer>> PersonUniqTopics=new HashMap<String,Set<Integer>>();
        for(Map.Entry<String,List<String>> m :mapInFile.entrySet()){
        	String p=m.getKey();
        	HashSet <Integer> UniqT=new HashSet<Integer>();
        	List<String> docs=m.getValue();
        	for(String doc: docs)
        		UniqT.add(mapInFile2.get(doc));
        	
        	 PersonUniqTopics.put(p,UniqT);
        }
//System.out.println(PersonUniqTopics);
        /* File toRead3=new File("DocIDList");
        FileInputStream fis3=new FileInputStream(toRead3);
        ObjectInputStream ois3=new ObjectInputStream(fis3);

        HashMap<String,Integer> mapInFile3=(HashMap<String,Integer>)ois3.readObject();

        ois3.close();
        fis3.close();*/
     
        
        //Code to find Avg doc length in dataset
        DocumentSimilarity ob=new DocumentSimilarity();
        /*int Tokens=ob.AvgLength();
        float AvgDocLen=Tokens/14020;
        System.out.println("Total tokens: "+Tokens);
        System.out.println("Avg Doc Length:"+AvgDocLen);*/
        
        File toRead4=new File("Topic10FinalOutput/TFMap");
        FileInputStream fis4=new FileInputStream(toRead4);
        ObjectInputStream ois4=new ObjectInputStream(fis4);

        HashMap<String,HashMap<String,Float>> TFMap=(HashMap<String,HashMap<String,Float>>)ois4.readObject();
        ois4.close();
        fis4.close();
        
        HashMap<String,HashMap<Integer,Float>> PersonIPI=new HashMap<String,HashMap<Integer,Float>>();
        
       //int totdocs=0;
     //  float avgperdoclen=0;
        for(Map.Entry<String,List<String>> entry1 :MapLarge.entrySet()){
       
        	String person=entry1.getKey();
       List<String> persondocs=entry1.getValue();
    //   totdocs=totdocs+persondocs.size();
	String doc1;
//	String doc2;
	String text1;
	//String text2;
	//Double score;
	String path="C:\\Users\\himanshu\\Documents\\CorrectedP\\";
	int length;
  //  int totallength=0;

	HashMap<String,Float>DocTF=TFMap.get(person);
	//System.out.println(DocTF);
	Set<Integer> UniqT1=PersonUniqTopics.get(person);
	float UniqT=(float)UniqT1.size()/30;
	HashMap<Integer,Float> MapTopic=new HashMap<Integer,Float>();

	for(int s: UniqT1)
	{

	       HashMap<String,Float> IScore=new HashMap<String,Float>();

	for(int i=0;i<persondocs.size();i++)
       {
    	   double Index=0;
    	//   int Nsim=0;
    	   int Tsim=0;

    	   doc1=path+persondocs.get(i);
     	   text1=ob.getText(doc1);

    	//int id1=mapInFile3.get(persondocs.get(i));
    	int tid1=mapInFile2.get(persondocs.get(i));
    	if(tid1==s)
    	{
    //	System.out.println("Topic ID of doc1 "+tid1);

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
    	// System.out.println("No. of similar articles found: "+Nsim);
     // float idf=(float) Math.log(14020/persondocs.size());
    	 float tf=DocTF.get(persondocs.get(i));
       length=ob.Tokenize(doc1, text1);
     //  totallength+=length;
       double normlength=(double)length/599;
       Index= normlength* (Tsim+tf);
  // System.out.println("NORM LENGTH = "+normlength+"	:	TF ="+tf+"	:	#TOPIC SIMILAR ARTICLES = "+Tsim+"	: 	#ARTICLES= " +persondocs.size()+ " 	:	#UNIQUE TOPICS= "+UniqT+"	:	DI="+Index);

    IScore.put(doc1,(float) Index);
    	
       }}
	
	//float perdoclen=(float)totallength/persondocs.size();
			//avgperdoclen+=perdoclen;
	Map<String, Float> MapT=NERDemo.RankScores(IScore);
	Map.Entry<String,Float> maxEntry = null;

	for(Map.Entry <String,Float> entry : MapT.entrySet()) {
	    if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
	        maxEntry = entry;
	    }
	}
	
     float IPI=maxEntry.getValue()+UniqT;
   //  maxEntry.setValue(IPI);
    MapTopic.put(s,IPI);
   // System.out.println("\nPERSON NAME = "+person+"	:	#ARTICLES = "+persondocs.size()+ "	:	IPI = "+IPI);

    }
       PersonIPI.put(person,MapTopic);
        }
        
  //    float AvgLenPerDoc=(float)avgperdoclen/mapInFile.size();
  //   Map<String,Float>New2Map= NERDemo.RankScores(PersonIPI);
    
    // float AvgDoc= (float)totdocs/mapInFile.size();
      // System.out.println(AvgDoc+ " "+AvgLenPerDoc);
      
       
       
       for(int topic=0;topic<30;topic++)
       {
           HashMap <String,Float> TPMap=new HashMap<String,Float>();
    	   for(Entry<String, HashMap<Integer, Float>> entry2 :PersonIPI.entrySet()){
        	   HashMap<Integer,Float> NewMap=entry2.getValue();
        	  Float sc= NewMap.get(topic);
        	  if(sc!=null)
           TPMap.put(entry2.getKey(),sc);
         //  System.out.println(entry2.getKey()+" "+sc);
           }
    	   Map<String,Float>Sorted=NERDemo.RankScores(TPMap);
    	   System.out.println("For topic "+topic+", Topmost IPI: "+ Sorted);
       }
       //float AvgIPI=TotIPI/New2Map.size();
  //System.out.println("Ttotal IPI"+TotIPI+" PersonIPI.size(): "+PersonIPI.size()+ "	Avg IPI: "+AvgIPI);   
	}

       
       
}

