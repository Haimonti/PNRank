
//pERFORMING Coreference resolution after NER process for getting new TF for persons

import java.util.Properties;

import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.coref.data.CorefChain.CorefMention;
import edu.stanford.nlp.coref.data.Mention;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.coref.data.Mention;
import edu.stanford.nlp.io.IOUtils;



import java.util.*;
import java.util.Map.Entry;
//import java.util.regex.Pattern;
import java.io.*;



//find all person names in documents
/** This is a demo of calling CRFClassifier programmatically.
 *  <p>
 *  Usage: <code> java -mx400m -cp "stanford-ner.jar:." NERDemo [serializedClassifier [fileName]]</code>
 *  <p>
 *  If arguments aren't specified, they default to
 *  ner-eng-ie.crf-3-all2006.ser.gz and some hardcoded sample text.
 *  <p>
 *  To use CRFClassifier from the command line:
 *  java -mx400m edu.stanford.nlp.ie.crf.CRFClassifier -loadClassifier
 *      [classifier] -textFile [file]
 *  Or if the file is already tokenized and one word per line, perhaps in
 *  a tab-separated value format with extra columns for part-of-speech tag,
 *  etc., use the version below (note the 's' instead of the 'x'):
 *  java -mx400m edu.stanford.nlp.ie.crf.CRFClassifier -loadClassifier
 *      [classifier] -testFile [file]
 *
 *  @author Jenny Finkel
 *  @author Christopher Manning
 */

public class NERCoref {
	
	public static <K, V extends Comparable<V>> Map<K, V> RankScores(final Map<K, V> myMap)
    {
        Comparator<K> Comparator1 = new Comparator<K>() {
            public int compare(K k1, K k2)
                {
                    int compare = myMap.get(k2).compareTo(myMap.get(k1));
                    if (compare == 0)
                        return 1;
                    else return compare;
                }
        };
        Map<K, V> sortedMap = new TreeMap<K, V>(Comparator1);
        sortedMap.putAll(myMap);
        return sortedMap;
    }
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

    @SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception
    {
	  try 
	  {
		  //File folder=new File ("C:/Users/AAYUSHEE/Documents/Corrected");
		  File folder=new File ("/Users/Haimonti/Research/IIITD/Aayushee/Code/Source-Code/Thesis/src/ReputedPersonDetection/data/final");
	
		  ArrayList <String> Documents= new ArrayList <String>();
		  NERCoref obj=new NERCoref();
		  Documents=obj.traverseFiles(folder,Documents); 
		  System.out.println("Number of documents in this folder " +Documents.size());
		  
		  HashMap<String,HashMap<String,Integer>>MyMap=new HashMap<String,HashMap<String,Integer>>();
		  for(int i=0;i<5;i++) // Documents.size()
		  {
			  System.out.println("Working on document no "+i);
			  File f=new File(Documents.get(i));
			  String fileContents = new String (IOUtils.slurpFile(Documents.get(i)));
			  Annotation document = new Annotation(fileContents);
			  Properties props = new Properties();
			  props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,mention,coref");
			  StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
			  pipeline.annotate(document);
			  //System.out.println("---");
			  //System.out.println("coref chains");
			  /*for (CorefChain cc : document.get(CorefCoreAnnotations.CorefChainAnnotation.class).values()) 
			  {
		      System.out.println("\t" + cc);
			  }
			  for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) 
			    {
			      System.out.println("---");
			      System.out.println("mentions");
			      for (Mention m : sentence.get(CorefCoreAnnotations.CorefMentionsAnnotation.class)) 
			      {
			        System.out.println("\t" + m);
			       }
			    }*/
			  Map<Integer, CorefChain> graph = document.get(CorefCoreAnnotations.CorefChainAnnotation.class);
			  for (Entry<Integer, CorefChain> entry : graph.entrySet()) 
			  {
			  	 CorefChain c=entry.getValue();
			  	 HashMap<String,Integer> TFMap2=new HashMap<String,Integer>();
			  	 
			  	 List<CorefMention> cms = c.getMentionsInTextualOrder();
			  	 String a = c.getRepresentativeMention().toString();
			  	 String b = a.replace("in sentence 1","" );
			  	 String d = b.replace("\"", "").trim();
			  	 if(MyMap.containsKey(d.toLowerCase()))
			  	 {	 
			  		TFMap2.putAll(MyMap.get(d.toLowerCase()));
			  		if(!TFMap2.containsKey(f.getName()))
			  		{
			  			TFMap2.put(f.getName(),cms.size());
			  			 MyMap.put(d.toLowerCase(), TFMap2);
			  		 }
			  	  }
			  	  else
			  	   {
			  	     TFMap2.put(f.getName(), cms.size());
			  		  MyMap.put(d.toLowerCase(), TFMap2);
			  		}
			  }// end of looping over documents
			  	
			  	//Reading the map of TF values from NER here
				File fileX=new File("/Users/Haimonti/Research/IIITD/Aayushee/Code/Source-Code/Thesis/src/ReputedPersonDetection/data/TopicFinalOutput/PersonTFValuesNER");
		        FileInputStream fisx=new FileInputStream(fileX);
		        ObjectInputStream oisx=new ObjectInputStream(fisx);

		        HashMap<String,HashMap<String, Integer>> PersonTFMap=(HashMap<String,HashMap<String, Integer>>)oisx.readObject();
		     	// System.out.println(DocTopicMap);
		        oisx.close();
		        fisx.close();
		        
		        HashMap<String,HashMap<String,Integer>>NewMap=new HashMap<String,HashMap<String,Integer>>();
				//System.out.println("This is COREF MAP"+MyMap.size());

		 		//System.out.println("This is NER MAP"+PersonTFMap.size());
				for (Map.Entry<String,HashMap<String, Integer>> enPerTF : PersonTFMap.entrySet()) 
				{		  
					String person=enPerTF.getKey();
					if(MyMap.containsKey(person.trim()))
					{
						NewMap.put(person.trim(),MyMap.get(person));
					}
					else
					NewMap.put(person.trim(),PersonTFMap.get(person));
				}
			   System.out.println("This is the person TFMap using NER and Coref:  "+NewMap);
			   System.out.println("Size of the new map is "+NewMap.size());
			  }
			  System.out.println("Done!");
		  //} // end of for loop over documents
	  }
		 catch (Exception e) 
		    { 
		       e.printStackTrace();
		    }
}
}