import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class MaxTopic {

	/**
	 * This class is used to read hashmaps of TopicWords and DocTopics to find out topic distribution among articles
	 * and get the topics and words with maximum occurrence across all articles for a person.
	 * 
	 * @param args
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		File toRead=new File("Topic10FinalOutput/DocTopicList");
        FileInputStream fis=new FileInputStream(toRead);
        ObjectInputStream ois=new ObjectInputStream(fis);

        HashMap<Integer,Integer> DocTopicMap=(HashMap<Integer,Integer>)ois.readObject();

        ois.close();
        fis.close();
        
        HashMap<Integer,List<Integer>> InvertedDocTopicMap=new HashMap<Integer,List<Integer>>();
        //print All data in MAP
        for(Map.Entry<Integer,Integer> m :DocTopicMap.entrySet()){
            int topic=m.getValue();
            int doc=m.getKey();
        	
        	
        	if(InvertedDocTopicMap.containsKey(topic))
			 {
			 
				List<Integer> Docs =InvertedDocTopicMap.get(topic);
				 if(!Docs.contains(doc))
				 Docs.add(doc);
				 InvertedDocTopicMap.put(topic,Docs);
			
			 }
			 else
			 {
				 List<Integer> Docs=new ArrayList<Integer>();
				 	Docs.add(doc);	
				 	InvertedDocTopicMap.put(topic,Docs);
			
			 }
        	
       }
       
        for(Map.Entry<Integer,List<Integer>> entry :InvertedDocTopicMap.entrySet()){
            int topic=entry.getKey();
            List<Integer> Docs=entry.getValue();
	
	System.out.println("Topic ID: "+topic+ " No. of Documents: "+Docs.size());
	
        }
        
        File toRead1=new File("Topic10FinalOutput/TopicWordsList");
        FileInputStream fis1=new FileInputStream(toRead1);
        ObjectInputStream ois1=new ObjectInputStream(fis1);

        HashMap<Integer,List<String>> TopicWordsMap=(HashMap<Integer,List<String>>)ois1.readObject();

        ois1.close();
        fis1.close();
       
        for(Map.Entry<Integer,List<String>> entry2 :TopicWordsMap.entrySet()){
            int topic=entry2.getKey();
            List<String> words=entry2.getValue();
	
	System.out.println("Topic ID: "+topic+ " Words: "+words);
	
        }
        
     
        File toRead3=new File("Topic10FinalOutput/DocIDList");
        FileInputStream fis3=new FileInputStream(toRead3);
        ObjectInputStream ois3=new ObjectInputStream(fis3);

        HashMap<String,Integer> DocIDMap=(HashMap<String,Integer>)ois3.readObject();

        ois3.close();
        fis3.close();
        
        
        
        
    //CREATING INFLUENTIAL PERSON CATEGORIES    
        File toRead2=new File("Topic10FinalOutput/PersonDocList");
        FileInputStream fis2=new FileInputStream(toRead2);
        ObjectInputStream ois2=new ObjectInputStream(fis2);

        HashMap<String,List<String>> mapInFile=(HashMap<String,List<String>>)ois2.readObject();

        ois2.close();
        fis2.close();
        
        HashMap<String,List<String>> MapSmall= new HashMap<String,List<String>>();
        HashMap<String,List<String>> MapMedium=new HashMap<String,List<String>>();
        HashMap<String,List<String>> MapLarge=new HashMap<String,List<String>>();
        
        //print All data in MAP
        for(Map.Entry<String,List<String>> m2 :mapInFile.entrySet()){
          // System.out.println(m2.getKey()+"  "+m2.getValue());
       String person=m2.getKey();
       List<String> docs=m2.getValue();
       if(docs.size()<4)
    	   MapSmall.put(person, docs);
        if(docs.size()>=4 && docs.size()<16)
        	MapMedium.put(person, docs);
        if(docs.size()>=16)
        	MapLarge.put(person, docs);
        
        }
        
         
        int count0=0,count1=0,count2=0,count3=0,count4=0,count5=0,count6=0,count7=0,count8=0,count9=0;
        for(Map.Entry<String,List<String>> entry3 :MapLarge.entrySet()){
        	System.out.println(entry3.getKey()+"  "+entry3.getValue());
        String person=entry3.getKey();
        List <Integer> Topics=new ArrayList<Integer>(); 
        List <String> persondocs=entry3.getValue();
        for(String doc:persondocs)
        {
        	int docid=DocIDMap.get(doc);
        	int tid=DocTopicMap.get(docid);	
   //     	if(!(Topics.contains(tid)))
        		Topics.add(tid);
        	if(tid==0)
        count0=count0+1;
        if(tid==1)
            count1=count1+1;
        if(tid==2)
            count2=count2+1;
        if(tid==3)
            count3=count3+1;
        if(tid==4)
            count4=count4+1;
        if(tid==5)
            count5=count5+1;
        if(tid==6)
            count6=count6+1;
        if(tid==7)
            count7=count7+1;
        if(tid==8)
            count8=count8+1;
        if(tid==9)
            count9=count9+1;
        
        }
        System.out.println(person+ " "+Topics);
        }
        System.out.println(count0);
        System.out.println(count1);
        System.out.println(count2);
        System.out.println(count3);
        System.out.println(count4);
        System.out.println(count5);
        System.out.println(count6);
        System.out.println(count7);
        System.out.println(count8);
        System.out.println(count9);
        
	
	
	}

}
