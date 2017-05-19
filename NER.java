import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.nlp.util.Maps;

//This file is to be run once NER and Coref process is complete.
//calculate lengths of all documents
//create ntf map each person. 


public class NER{
	
@SuppressWarnings("unchecked")
public static void main (String args[]) throws Exception{
	/*BufferedReader reader1 = new BufferedReader(new FileReader("NER/FinalCorrectedPersonNames3.txt"));
	Set<String> lines = new LinkedHashSet<String>(); 
	String uniqline;
	//String[] split;
	while ((uniqline = reader1.readLine()) != null) 
	{
		lines.add(uniqline);
	}
	reader1.close();
	BufferedWriter writer1 = new BufferedWriter(new FileWriter("NER/FinalCorrectedPersonName3.txt"));
	for (String unique : lines) 
	{
		writer1.write(unique);
		writer1.newLine();
	}
	writer1.flush();
	writer1.close();*/
	/*BufferedReader reader1 = new BufferedReader(new FileReader("PersonNames8.txt"));
	String uniqline=null;
	ArrayList<String> lines = new ArrayList<String>(); 
	while ((uniqline = reader1.readLine()) != null) 
	{
		String split[]=uniqline.split("\t");
		for(int i=0;i<split.length;i++)
			lines.add(split[i]);
	
	}
	reader1.close();
			LinkedHashSet<String> uniqueValues = new LinkedHashSet<>(lines);
	BufferedWriter writer1 = new BufferedWriter(new FileWriter("PersonNames9.txt"));
	for (String unique : uniqueValues) 
	{
		writer1.write(unique);
		writer1.newLine();
	}
	writer1.flush();
	writer1.close();
	
	*/
	//Code to get person names greater than 1 gram---removing persons with single names only
	/*BufferedReader reader1 = new BufferedReader(new FileReader("NER/CorrectedwithoutPersonNames2.txt"));
	String uniqline=null;
	ArrayList<String> lines = new ArrayList<String>(); 
	while ((uniqline = reader1.readLine()) != null) 
	{
		String split[]=uniqline.split(" ");
		if(split.length>1)
			lines.add(uniqline);
	
	}
	reader1.close();
			LinkedHashSet<String> uniqueValues = new LinkedHashSet<>(lines);
	BufferedWriter writer1 = new BufferedWriter(new FileWriter("NER/MultiwithoutPersonNamesCorrected.txt"));
	for (String unique : uniqueValues) 
	{
		writer1.write(unique);
		writer1.newLine();
	}
	writer1.flush();
	writer1.close();
*/
	
	// Code to remove leading and trailing whitespace from a document
/*	FileReader fr = new FileReader("C:/Users/himanshu/Documents/final/72265 .txt"); 
	BufferedReader br = new BufferedReader(fr); 
	FileWriter fw = new FileWriter("outfile.txt"); 
	String line;

	while((line = br.readLine()) != null)
	{ 
	    line = line.trim(); // remove leading and trailing whitespace
	    if (!line.equals("")) // don't write out blank lines
	    {
	        fw.write(line, 0, line.length());
	    }
	} 
	fr.close();
	fw.close();
	*/
	/*File folder1=new File ("C:/Users/himanshu/Documents/CorrectedP");

	File[] listOfFiles1=folder1.listFiles();
	//FileReader fr = new FileReader("C:/Users/himanshu/Documents/final/72265 .txt"); 
	//FileReader fr = new FileReader("C:/Users/himanshu/workspace/Thesis/Ocr/73786.txt");
	//BufferedReader br = new BufferedReader(fr); 
	FileWriter fw = new FileWriter("AllArtLinedoc2.txt"); 
	
	
	
	for (int i = 0; i < listOfFiles1.length; i++) 
	{
		BufferedReader br = new BufferedReader(new FileReader(listOfFiles1[i]));
	StringBuilder sb = new StringBuilder();
	// adds 9 character string at beginning
	String line;
	while((line = br.readLine()) != null)
	{
		//System.out.println(line);
		sb.append(line);
		
	}
	
	
	fw.write(sb.toString()+"\n");
	
	br.close();

	}
	fw.close();
	
	/*
	/*FileReader fr = new FileReader("C:/Users/himanshu/Documents/final/72265 .txt"); 
	BufferedReader br = new BufferedReader(fr); 
	FileWriter fw = new FileWriter("linedoc72265.txt"); 
	String line;
	
	StringBuilder sb = new StringBuilder();
	// adds 9 character string at beginning
	
	while((line = br.readLine()) != null)
	
	line = lline.replace("\n", "").replace("\r", "");

*/
	
	
	//No of topics and people map
	/*Map<String,List<String>> RevertMap=new HashMap<String,List<String>>();
	FileReader fr = new FileReader("PersonTopic.csv"); 
	BufferedReader br = new BufferedReader(fr); 
	String line;
	while((line = br.readLine()) != null)
	{
		String split[]=line.split(",");
		//System.out.println(split[0]+" "+split[1]);
		
		if(RevertMap.containsKey(split[1]))
		 {
		 
			List<String> val2 =RevertMap.get(split[1]);
			 if(!val2.contains(split[0]))
			 val2.add(split[0]);
			 	RevertMap.put(split[1],val2);
		 }
		 else
		 {
			 List<String> val2=new ArrayList<String>();
			 	val2.add(split[0]);	
			 	//String split[]=entity.split(" ");
				//if(split.length>1)
		 RevertMap.put(split[1],val2);
		 //System.out.println("Key= "+values.size()+"People list: "+ val2);
		 }
		
			
	}
	//System.out.println(RevertMap);
	  for (Map.Entry<String, List<String>> entry : RevertMap.entrySet()) {	  
    	  List <String> persons=entry.getValue();
	  System.out.println("No. of topics: "+entry.getKey()+"  Number of people: " +persons.size());
	  
	  }*/
	
	/*FileReader fr = new FileReader("NER/OutputofNEROriginal.txt"); 
	BufferedReader br = new BufferedReader(fr); 
	String line;
	FileReader fr2 = new FileReader("NER/OutputofNERCorrectedOldDict.txt"); 
	BufferedReader br2 = new BufferedReader(fr2); 
	List<String> val1=new ArrayList<String>();
	List<String> val2=new ArrayList<String>();
	while((line = br.readLine()) != null)
	{
		val1.add(line.toLowerCase());
	}
	br.close();

	String line2;
	while((line2 = br2.readLine()) != null)
	{
		val2.add(line2.toLowerCase());
	}
	br2.close();
int count=0;
	for(String val: val1)
	{	if(val2.contains(val))
		{System.out.println(val);	
		count++;}
	}
	System.out.println("Matched entities: "+count+" out of "+val1.size());
	
	*/
	
	//Read topic output list from mallet and store in a hashmap for use in creating final map:
/*	BufferedReader reader1 = new BufferedReader(new FileReader("C:/mallet/USE IN THESIS OLD/output100.txt"));
	HashMap<String,Integer> topicmap = new HashMap<String,Integer>(); 
	String uniqline;
	String[] split;
	while ((uniqline = reader1.readLine()) != null) 
	{
	split=uniqline.split("\t");
	topicmap.put(split[0],Integer.parseInt(split[1]));
	System.out.println(split[0]+" "+split[1]);
	}
	reader1.close();
	System.out.println(topicmap.size());
	
	System.out.println("Writing Doc Topic map");
    File fileL=new File("DocTopicList100");
  FileOutputStream fosL=new FileOutputStream(fileL);
      ObjectOutputStream oosL=new ObjectOutputStream(fosL);
      oosL.writeObject(topicmap);
      oosL.flush();
      oosL.close();
      fosL.close();
  */    

	
	//Code to create NTF MAP FOR EACH DOCUMENT
/*
	File toRead2=new File("Topic100FinalOutput/PersonTFValues");
    FileInputStream fis2=new FileInputStream(toRead2);
    ObjectInputStream ois2=new ObjectInputStream(fis2);

    HashMap<String,HashMap<String,Integer>> mapInFile2=(HashMap<String,HashMap<String,Integer>>)ois2.readObject();
    ois2.close();
    fis2.close();
	System.out.println(mapInFile2.size());
    HashMap<String,HashMap<String,Float>> PersonTFWt=new HashMap <String,HashMap<String,Float>>();
	HashMap<String,Integer> Map3=new HashMap<String,Integer>();
    for(Map.Entry<String,HashMap<String,Integer>> entry1 :mapInFile2.entrySet()){
    	HashMap<String,Float>NewMap=new HashMap<String,Float>();
    	String person=entry1.getKey();
    	HashMap<String,Integer> Map2=entry1.getValue();
    	
    	ArrayList<Integer> l1= new ArrayList<Integer>();
    	l1.addAll(Map2.values());
    	int tf=0;
    	//for(int x:l1)
    	//{
    		//tf=tf+x;
    	//}
    Collections.sort(l1);

    tf=l1.get(l1.size()-1);
    //System.out.println(l1+ "max tf: "+tf);
    for(Map.Entry<String,Integer> entry2:Map2.entrySet()){
    	String doc=entry2.getKey();
    	int doctf=entry2.getValue();
    	//System.out.println(doctf+"\n");
    	//float wt=(float)doctf/tf;    //commenting for a while TO GET OLD THESIS RESULTS......uncomment later
    	//System.out.println(doc+" : "+wt);
    	float wt=(float) (1+Math.log10(doctf));
    	NewMap.put(doc,wt);
    
    }
    Map3.put(person,tf);
//System.out.println(person+" "+tf);
  PersonTFWt.put(person,NewMap);    
  //System.out.println(person+ "  "+ NewMap);
    }
  //  System.out.println(PersonTFWt);
    System.out.println("Writing person TF VALUES map");
	  File fileO=new File("FinalOutput/TFMap1709");
    FileOutputStream fos0=new FileOutputStream(fileO);
        ObjectOutputStream oos0=new ObjectOutputStream(fos0);
        oos0.writeObject(PersonTFWt);
//System.out.println(PersonTFWt);
        oos0.flush();
        oos0.close();
        fos0.close();
	
    File toRead=new File("FinalOutput/PersonDocList");
    FileInputStream fis=new FileInputStream(toRead);
    ObjectInputStream ois=new ObjectInputStream(fis);

    HashMap<String,List<String>> mapInFile=(HashMap<String,List<String>>)ois.readObject();

    ois.close();
    fis.close();

 */
 /*   
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

    System.out.println(MapSmall.size());
    System.out.println(MapMedium.size());
    System.out.println(MapLarge.size());
    */
  int tot=0;
   /* for(Map.Entry<String,List<String>> m1 :MapLarge.entrySet()){
    	String p=m1.getKey();
    	int t=Map3.get(p);
    	tot+=t;
    }
    float avgtf=(float)tot/MapLarge.size();
    System.out.println(tot+" "+MapLarge.size()+" " +avgtf);*/
    
/*
 //Code to find maximum length document in the dataset
  
  DocumentSimilarity ob=new DocumentSimilarity();
	   ArrayList<String> Documents2=new ArrayList<String>(); 
		File folder=new File("C:/Users/AAYUSHEE/workspace/Thesis/Corrected");
		NERDemo obj=new NERDemo();
		Documents2=obj.traverseFiles(folder, Documents2);
		System.out.println(Documents2.size());
	ArrayList<Float> DocLengths=new ArrayList<Float>();
		for(String doc: Documents2)
		{
			
			String text=ob.getText(doc);
			int DocLen=ob.Tokenize(doc, text);
			DocLengths.add((float)DocLen/774);
		}//774 for 50 doscuments and 18053 for all 14020 documents
		float MaxDocLength=Collections.max(DocLengths);
System.out.println(MaxDocLength+"=18053 ");
System.out.println(DocLengths);
*/
  //Code to get max length of all docs in a person's list 
 /* HashMap<String,Integer> MapL=new HashMap<String,Integer>();
  DocumentSimilarity ob=new DocumentSimilarity();
  ArrayList<String> Documents2=new ArrayList<String>(); 
	File folder=new File("C:/Users/AAYUSHEE/Documents/final");
	NERDemo obj=new NERDemo();
	Documents2=obj.traverseFiles(folder, Documents2);
	System.out.println(Documents2.size());
	String path="C:/Users/AAYUSHEE/Documents/final/";
//ArrayList<Float> DocLengths=new ArrayList<Float>();
for(Map.Entry<String,List<String>> m1 :mapInFile.entrySet()){
	String p=m1.getKey();
	List<String> docs=m1.getValue();
	int tok=0;
	for(String doc:docs)
		
	{
		String tex=ob.getText(path+doc);
		 tok+=ob.Tokenize(path+doc, tex);
	} //adding no. of words in all documents in which a person appears
	System.out.println(p+ " "+ tok);
	MapL.put(p,tok);
}
System.out.println("Writing person doc lengths map");
  File file1=new File("FinalOutput/DocLengthsMap1709");
FileOutputStream fos1=new FileOutputStream(file1);
    ObjectOutputStream oos1=new ObjectOutputStream(fos1);
    oos1.writeObject(MapL);
  System.out.println(MapL.size());
    oos1.flush();
    oos1.close();
    fos1.close();
    */
  
  
  
  //Code to find Max PNF across all persons
  
  
  /*File toRead2=new File("FinalOutput2310/PersonTFValues2310");
  FileInputStream fis2=new FileInputStream(toRead2);
  ObjectInputStream ois2=new ObjectInputStream(fis2);

  HashMap<String,HashMap<String,Integer>> mapInFile2=(HashMap<String,HashMap<String,Integer>>)ois2.readObject();
  ois2.close();
  fis2.close();
	System.out.println(mapInFile2.size());
  
	List<Integer> FreqList=new ArrayList<Integer>();
	for(Map.Entry<String,HashMap<String,Integer>> entry1 :mapInFile2.entrySet()){
  	
		HashMap<String,Integer> Map2=entry1.getValue();
  	ArrayList<Integer> l1= new ArrayList<Integer>();
  	l1.addAll(Map2.values());
  	Collections.sort(l1);
  	FreqList.add(l1.get(l1.size()-1));

  }
	
	Collections.sort(FreqList);
	//System.out.println(FreqList);
	System.out.println("Max frequency across all documents for any person is:"+ FreqList.get(FreqList.size()-1));
	*/
	
	File fileX=new File("FinalOutput2310/PersonTFValuesNER");
    FileInputStream fisx=new FileInputStream(fileX);
    ObjectInputStream oisx=new ObjectInputStream(fisx);

    HashMap<String,HashMap<String, Integer>> PersonTFMap=(HashMap<String,HashMap<String, Integer>>)oisx.readObject();
 //   System.out.println(DocTopicMap);
    oisx.close();
    fisx.close();
    
  //System.out.println(PersonTFMap.get("mrs kimball"));
    
	//READING THE MAP OF TF VALUES CREATED FROM NER HERE
  File fileX2=new File("FinalOutput2310/PersonTFValues2310");
  FileInputStream fisx2=new FileInputStream(fileX2);
  ObjectInputStream oisx2=new ObjectInputStream(fisx2);

  HashMap<String,HashMap<String, Integer>> MyMap=(HashMap<String,HashMap<String, Integer>>)oisx2.readObject();
//   System.out.println(DocTopicMap);
  oisx2.close();
  fisx2.close();

	
	HashMap<String,HashMap<String,Integer>>NewMap=new HashMap<String,HashMap<String,Integer>>();
System.out.println("This is COREF MAP"+MyMap.size());

System.out.println("This is NER MAP"+PersonTFMap.size());
	for (Map.Entry<String,HashMap<String, Integer>> entry : PersonTFMap.entrySet()) {	

	String person=entry.getKey();
	HashMap <String,Integer> values=entry.getValue();
	HashMap <String,Integer> SmallMap=new HashMap<String,Integer>();

//System.out.println("Trying to search for " +person+"	"+MyMap.containsKey(person.trim()));
		if(MyMap.containsKey(person.trim()))
				{
					
			for(Map.Entry<String, Integer> entry2: values.entrySet())
			{
				String doc=entry2.getKey();
				
				if(MyMap.get(person.trim()).containsKey(doc))
				{

				if( entry2.getValue()<MyMap.get(person.trim()).get(doc) )
				{
			//NewMap.put(person.trim(),MyMap.get(person));
					int freq=MyMap.get(person.trim()).get(doc);
				SmallMap.put(doc,freq);
				
				}
				else{
				int freq=MyMap.get(person.trim()).get(doc);
				SmallMap.put(doc,freq);}
				}
				else
					SmallMap.put(doc,entry2.getValue());
			
				
				NewMap.put(person.trim(),SmallMap);
	
			}
				}
		else
		NewMap.put(person.trim(),PersonTFMap.get(person));
		
		}
	
	
	String persons[]={"jefferson ingram","mrs darner","capt creeten","john wilson","alexander iii","george apro","itrerkiti nashua","john martin","thomas jefferson","aaron trow","capt williams","lillian russell","caleb morton","john doe","catherine ii","john dull","mrs herrmann","father ducey","m delabordeau","capt pinckney","john w","karl marie clavero","tim ion","john ii","mrs kendal","henry worley","tom wright","marie clavero","jesus christ","william i","mrs cleveland","mrs damer","conan doyle","ann arbor","lyman abbott","john j","john jacob astor","godfrey holmes","frank id","roel iliff","patty coverdale","george gould","elizabeth car","lisa j","elltalwlh harrow","otto knglmiman","daniel frohman","anil jr","carl werner"};
	for (String person: persons)
	{
		
	}
	 //Code for writing TF Map to disk
/*	System.out.println("Writing TFScore map");
        File fileL=new File("FinalOutput2310/PersonTFValues0111");
	    FileOutputStream fosL=new FileOutputStream(fileL);
	        ObjectOutputStream oosL=new ObjectOutputStream(fosL);
	        oosL.writeObject(NewMap);
	        oosL.flush();
	        oosL.close();
	        fosL.close(); */
    
}}
