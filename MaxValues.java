import java.io.*;
import java.util.*;


public class MaxValues{
	
	
	public ArrayList<String> traverseFiles(File inputDir, ArrayList<String> Documents2 )
	{
        
		//ArrayList <String> Documents=new ArrayList <String>();
		if (inputDir.isDirectory()) {
			//System.out.println("Checking for directory...");
	        String[] children = inputDir.list();
	        for (int i = 0; children != null && i < children.length; i++) {
	            traverseFiles(new File(inputDir, children[i]), Documents2);
	        }
	    }
	    if (inputDir.isFile()) 
	    	{ Documents2.add(inputDir.getAbsolutePath());}//change it if needed
	  
	    return Documents2;
	   // System.out.println(Documents.size());
	}


//Code to find max NTF FOR EACH person
public void maxNTF() throws Exception{
	File toRead2=new File("OutputD2/PersonTFValuesFinal");
    FileInputStream fis2=new FileInputStream(toRead2);
    ObjectInputStream ois2=new ObjectInputStream(fis2);
    HashMap<String,HashMap<String,Integer>> mapInFile2=(HashMap<String,HashMap<String,Integer>>)ois2.readObject();
    ois2.close();
    fis2.close();
    
	//System.out.println(mapInFile2.size());
    HashMap<String,HashMap<String,Float>> PersonTFWt=new HashMap <String,HashMap<String,Float>>();
	HashMap<String,Integer> Map3=new HashMap<String,Integer>();
    	ArrayList<Integer> l1= new ArrayList<Integer>();
    
    for(Map.Entry<String,HashMap<String,Integer>> entry1 :mapInFile2.entrySet()){
    	HashMap<String,Float>NewMap=new HashMap<String,Float>();
    	String person=entry1.getKey();
    	HashMap<String,Integer> Map2=entry1.getValue();
    	
    	l1.addAll(Map2.values());
      
    }
    	int tf=0;
    	
    Collections.sort(l1);
    System.out.println(l1.size());
    tf=l1.get(l1.size()-1);
    System.out.println(tf);
  
}

public void maxLen() throws Exception{
	
	

 //Code to find maximum length document in the dataset
  
  DocumentSimilarity ob=new DocumentSimilarity();
	   ArrayList<String> Documents2=new ArrayList<String>(); 
		File folder=new File("Combined");
		MaxValues obj=new MaxValues();
		Documents2=obj.traverseFiles(folder, Documents2);
		System.out.println(Documents2.size());
			ArrayList<Float> DocLengths=new ArrayList<Float>();

		for(String doc: Documents2)
		{
			
			String text=ob.getText(doc);
			int DocLen=ob.Tokenize(doc, text);
			DocLengths.add((float)DocLen);
		}//774 for 50 documents and 18053 for all 14020 documents
		float MaxDocLength=Collections.max(DocLengths);
System.out.println(MaxDocLength);

	
	
}




/* File toRead3=new File("DocLengthsMap");
        FileInputStream fis3=new FileInputStream(toRead3);
        ObjectInputStream ois3=new ObjectInputStream(fis3);

        HashMap<String,Integer> mapInFile3=(HashMap<String,Integer>)ois3.readObject();

        ois3.close();
        fis3.close();
        
 File toRead4=new File("TFMap");
        FileInputStream fis4=new FileInputStream(toRead4);
        ObjectInputStream ois4=new ObjectInputStream(fis4);

        HashMap<String,HashMap<String,Float>> TFMap2=(HashMap<String,HashMap<String,Float>>)ois4.readObject();
        ois4.close();
        fis4.close();
        
for(Map.Entry<String,List<String>> entry1 :TFMap2.entrySet()){
        	String person=entry1.getKey();
        	
            HashMap<String,Float>DocTF=new HashMap<String,Float>();
            	DocTF=TFMap2.get(person);
List<String> persondocs=new ArrayList<String>();
 		   persondocs.addAll(entry1.getValue());
           
           Float tf=DocTF.get(ab);

    	 length=ob.Tokenize(doc1, text1);
   //   totallength+=length;
     double normlength=(double)length/18053; 
  */   
    	public static void main(String[] args) throws Exception {
            MaxValues obj=new MaxValues();
            obj.maxNTF();
			obj.maxLen();
            
            
            
        } 
}  
     
     