
        import cc.mallet.pipe.*;
        import cc.mallet.pipe.iterator.CsvIterator;
        import cc.mallet.topics.ParallelTopicModel;
        import cc.mallet.types.Alphabet;
        import cc.mallet.types.FeatureSequence;
        import cc.mallet.types.InstanceList;
        import cc.mallet.types.LabelSequence;
        import edu.stanford.nlp.ie.AbstractSequenceClassifier;
        import edu.stanford.nlp.ie.crf.CRFClassifier;
        import edu.stanford.nlp.io.IOUtils;
        import edu.stanford.nlp.ling.CoreLabel;
        import edu.stanford.nlp.util.Triple;

        import java.io.*;
        import java.util.*;
        import java.util.regex.Pattern;

/**
 * @author Jayashree Chandrasekaran
 *
 */
public class AnnotateTitles {
    public static void main(String args[]){

        String serializedClassifier = "/Users/jayashree/Documents/Resources_for_PNRank/stanford-ner-2016-10-31/classifiers/english.all.3class.distsim.crf.ser.gz";

        if (args.length > 0)
        {
            serializedClassifier = args[0];
        }

        AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);






        //File folder=new File ("C:/Users/AAYUSHEE/Documents/final");
        File folder=new File ("/Users/jayashree/Documents/Resources_for_PNRank/final");
        //HASHMAP WITH PERSON NAME AND CORRESPONDING DOCUMENTS LIST
        HashMap<String, List<String>> hm = new HashMap<String, List<String>>();
        HashMap<String, List<String>> name_title_map = new HashMap<String, List<String>>();
        ArrayList<String> names = new ArrayList<String>();
        //HASHMAP TO CALCULATE PERSON-DOCUMENT TF VALUES
        HashMap<String,HashMap< String,Integer>> PersonTFMap = new HashMap<String,HashMap<String,Integer>>();
        //HASHMAP WITH NUMBER OF DOCUMENTS AND CORRESPONDING PEOPLE LIST
        HashMap<Integer, List<String>> hm2 = new HashMap<Integer, List<String>>();
        //HASHMAP WITH DOC IDS AND DOC NAMES
        HashMap< String,Integer> hm3 = new HashMap<String,Integer>();
        //HASHMAP WITH PERSON NAME AND THEIR CORRESPONDING TF VALUES
        ArrayList <String> Documents= new ArrayList <String>();
        //HASHMAP WITH PERSON NAME AND TITLES ASSOCIATED WITH IT
        HashMap<String, List<String>> titleHash = new HashMap<String, List<String>>();
        //List of Titles associated with entities
        StringBuffer titleBuf = new StringBuffer();
        NERDemo obj=new NERDemo();
        Documents=obj.traverseFiles(folder,Documents);
        //System.out.println(Documents.size());

        try {
//            BufferedWriter outp = new BufferedWriter(new FileWriter("/Users/jayashree/Documents/Output_PNRank/data/OutputofNERFinal.txt"));
//            BufferedWriter outTFMap = new BufferedWriter(new FileWriter("/Users/jayashree/Documents/Output_PNRank/data/TopicFinalOutput/PersonTFMapFinal.txt"));
            BufferedWriter outTitleMap = new BufferedWriter(new FileWriter("/Users/jayashree/Documents/Output_PNRank/data/Signature_feature.csv"));
//            BufferedWriter outTitle = new BufferedWriter(new FileWriter("/Users/jayashree/Documents/Output_PNRank/data/TopicFinalOutput/TitleList.txt"));
            //BufferedWriter outMalletData = new BufferedWriter(new FileWriter("/Users/Haimonti/Research/IIITD/Aayushee/Code/Source-Code/Thesis/src/ReputedPersonDetection/data/OCRDataMallet.txt"));
            //File[] listOfFiles = null;
            for (int i = 0; i < Documents.size(); i++) //
            {
                //System.out.println(Documents.get(i));
                String fileContents = IOUtils.slurpFile(Documents.get(i));
                File f = new File(Documents.get(i));
                hm3.put(f.getName(), i);
                String[] splitStr = new String[0];
                int numParts = 0;
                //Write the name of file and contents to a file - Use one time only!
                //This will be used by the LDA code later
                //outMalletData.write(f.getName());
                //outMalletData.write("\t");
                //outMalletData.write(fileContents);
                //outMalletData.write("\n");
                List<Triple<String, Integer, Integer>> entities = classifier.classifyToCharacterOffsets(fileContents);
                classifier.classifyToString(fileContents, "tabbedEntities", false);
                for (Triple<String, Integer, Integer> triple : entities) {

                    //System.out.println(triple.first() + ": " + fileContents.substring(triple.second(), triple.third()));
                    String ename = fileContents.substring(triple.second, triple.third);
                    //System.out.println("Entity name is "+ename);
                    String entity = ename.replaceAll("\\*", "").replaceAll("\\s+", " ").replaceAll("\\!", "");
                    if (entity.length() > 1) {
                        if (triple.first.equalsIgnoreCase("PERSON")) {
//                            outp.write(entity.trim() + " " + f.getName() + "\n");
                            String e = entity.trim().toLowerCase();
                            if (hm.containsKey(e)) {
                                List<String> val = hm.get(e);
                                if (!val.contains(f.getName()))
                                    val.add(f.getName());
                                hm.put(e, val);
                                //Code for calculating TF of each person entity
                                if (PersonTFMap.containsKey(e)) {
                                    HashMap<String, Integer> TFMap = new HashMap<String, Integer>();
                                    TFMap.putAll(PersonTFMap.get(e));
                                    //	HashMap<String,Integer> TFMap=PersonTFMap.get(e);
                                    if (TFMap.containsKey(f.getName())) {
                                        int Freq = TFMap.get(f.getName());
                                        Freq++;
                                        TFMap.put(f.getName(), Freq);
                                        PersonTFMap.put(e, TFMap);
//                                        outTFMap.write(entity.trim() + " " + Freq + "\n");
                                    } else {
                                        int Freq = 0;
                                        Freq++;
                                        TFMap.put(f.getName(), Freq);
                                        PersonTFMap.put(e, TFMap);
//                                        outTFMap.write(entity.trim() + " " + Freq + "\n");
                                    }

                                } // end if TF code
                            } // hm does not contain key
                            else {
                                HashMap<String, Integer> TFMap = new HashMap<String, Integer>();
                                List<String> val = new ArrayList<String>();
                                val.add(f.getName());
                                String split[] = e.split(" ");
                                if (split.length > 1) {
                                    hm.put(e, val);
                                    int Freq = 0;
                                    Freq++;
                                    TFMap.put(f.getName(), Freq);
                                    PersonTFMap.put(e, TFMap);
//                                    outTFMap.write(entity.trim() + " " + Freq + "\n");
                                }
                            }


                            if(!names.contains(entity.trim().length()>1)){
                                names.add(entity.trim());
                            }

                            // if (triple.first.equalsIgnoreCase("LOCATION"))
                            //System.out.println("Entity Name is "+entity.trim());
                            //Get the title of this person if it exists
                            // Search for the entity name in the text and split by it
                            splitStr = fileContents.split(entity.trim());
                            //StringTokenizer strSp = new StringTokenizer(fileContents,entity.trim());
                            numParts = splitStr.length;
                            //System.out.println("Number of partitions "+numParts);
                            for (int p = 0; p < (numParts - 1); p++) {
                                // Check the last word in the split before entity name
                                StringBuffer splitStrBuf = new StringBuffer(splitStr[p].trim());
                                if (!((splitStrBuf.length() == 0) || (splitStrBuf.equals("")))) {
                                    //System.out.println("Split "+p+" is "+splitStrBuf.toString());
                                    String revSplitStr = splitStrBuf.reverse().toString();
                                    StringTokenizer strTok = new StringTokenizer(revSplitStr, " ");
                                    //Reverse it again to get the title
                                    StringBuffer entTitleRev = new StringBuffer(strTok.nextToken());

                                    // Check if titles exist for this entity
                                    // If it does, fetch it and update
                                    if (titleHash.containsKey(entity.trim())) {
                                        List<String> currTitleHash = titleHash.get(entity.trim());
                                        String tmpRevTitle = entTitleRev.reverse().toString();
                                        if (!currTitleHash.contains(tmpRevTitle))
                                            currTitleHash.add(tmpRevTitle);
                                        titleHash.put(entity.trim(), currTitleHash);

                                    } else {
                                        // Create a new entry for this entity
                                        List<String> currTitleHash = new ArrayList<String>();
                                        currTitleHash.add(entTitleRev.reverse().toString());
                                        titleHash.put(entity.trim(), currTitleHash);
                                    }
                                    titleBuf.append(entTitleRev.toString());
                                    titleBuf.append("\n");
                                } // Ensure string split is not null
                            } // end of for loop over str partitions

                        } // end if PERSON tag
                    } // end if entity length > 1
                } // end first for loop

            }// end of second for loop.
            System.out.println("Size of names is" +names.size());
            //PRINTING THE HASHMAP OF PERSONS WITH DOCUMENTS IN WHICH THEY OCCUR
//        outp.close();
//        outTFMap.close();
            ArrayList<String> list_titles = new ArrayList<String>();
            try {

                BufferedReader br = new BufferedReader(new FileReader("/Users/jayashree/Documents/Resources_for_PNRank/AnnTitles.txt"));

                String sCurrentLine;

                while ((sCurrentLine = br.readLine()) != null) {
                    list_titles.add(sCurrentLine);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            ArrayList<String> list_titles_wiki = new ArrayList<String>();
            try {

                BufferedReader br = new BufferedReader(new FileReader("/Users/jayashree/Documents/Resources_for_PNRank/WikiTitles.txt"));

                String sCurrentLine;

                while ((sCurrentLine = br.readLine()) != null) {
                    list_titles_wiki.add(sCurrentLine);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
           // ArrayList<String> tempList = new ArrayList<String>();

            for (String element: names) {
                if(element.split(" ").length>1) {
                    ArrayList<String> emptyList = new ArrayList<String>();
                    name_title_map.put(element, emptyList);
                }

            }
            System.out.println("Size of name title map is before : " +name_title_map.size());
            System.out.println("The size of the titleHash map is "+titleHash.size());
            name_title_map.putAll(titleHash);
            System.out.println("Size of name title map is after : " +name_title_map.size());

            ArrayList<String> tempList = new ArrayList<String>();

            outTitleMap.write("Name of Person" +","+ "Titles"+","+"Wikipedia\n");
            for (Map.Entry<String, List<String>> titleEntry : name_title_map.entrySet()) {
                String keyTitle = titleEntry.getKey();
                List<String> titleVal = titleEntry.getValue();
                Iterator titleItr = titleVal.iterator();
                if (titleVal.isEmpty() && keyTitle.split(" ").length>1){
                    if(!tempList.contains(keyTitle.toLowerCase())) {
                        tempList.add(keyTitle.toLowerCase());
                        outTitleMap.write(keyTitle + "," + "0" + "," + "0 \n");
                    }
                }

                while (titleItr.hasNext()) {
                        Object element = titleItr.next();

                        if(keyTitle.split(" ").length>1){
                            if(!tempList.contains(keyTitle.toLowerCase())){
                                tempList.add(keyTitle.toLowerCase());
                                if(list_titles.contains(element.toString())){
                                    if(list_titles_wiki.contains(element.toString())) {
                                        outTitleMap.write(keyTitle + "," + "1"+","+" 1 \n");
                                    }else{
                                        outTitleMap.write(keyTitle + "," + "1"+","+" 0 \n");
                                    }
                                }else if(list_titles_wiki.contains(element.toString())){
                                    outTitleMap.write(keyTitle+","+"0"+","+"1 \n" );
                                }else{
                                    outTitleMap.write(keyTitle+","+"0"+","+"0 \n");
                                }
                                }
                            }
                        }



                    //outTitleMap.write(element+" ");
                }

            outTitleMap.close();


            //outTitle.write(titleBuf.toString());
//        outTitle.flush();
//        outTitle.close();
            System.out.print("Done!!");

        }catch(Exception e){
            e.printStackTrace();
        }
//        outTitleMap.close();
//        outTitle.write(titleBuf.toString());
//        outTitle.flush();
//        outTitle.close();
//        BufferedWriter output= new BufferedWriter(new FileWriter("/Users/jayashree/Documents/Output_PNRank/data/InvertedNEROutput.csv"));
//        for (Map.Entry<String, List<String>> entry : hm.entrySet())
//        {
//            String key = entry.getKey();
//            List<String> values = entry.getValue();
//            output.write(key+","+values.size()+"\n");
//            //System.out.println("Key = " + key+" Number of documents: "+values.size());
//            if(hm2.containsKey(values.size()))
//            {
//                List<String> val2 =hm2.get(values.size());
//                if(!val2.contains(key))
//                    val2.add(key);
//                hm2.put(values.size(),val2);
//            }
//            else
//            {
//                List<String> val2=new ArrayList<String>();
//                val2.add(key);
//                hm2.put(values.size(),val2);
//            }
//            //outMalletData.close();
//        } // end for loop iterating over Map
//
//
//        for (Map.Entry<Integer, List<String>> entry : hm2.entrySet())
//        {
//            List<String> values2 = entry.getValue();
//            System.out.println(entry.getKey()+ " "+values2);
//            output.write(entry.getKey()+","+values2.size()+"\n");
//        }
//        output.close();
//        System.out.println("Size of hashmap:number of entities and their corresponding number of documents: "+hm.size());
//        System.out.println("Size of hashmap3 Documents and their id: "+hm3.size());
//        System.out.println("Size of Person entities map: "+PersonTFMap.size());
//
//        //System.out.println("Number of entities with 1 document only: "+count);
//        //outp.close();
//
//        //LDA TOPIC MODEL CODE
//        Map<Integer,Integer> DocTopicMap=new HashMap<Integer,Integer>();
//        //Map<Integer,List<String>> hm4= new HashMap<Integer,List<String>>();
//
//        // Begin by importing documents from text to feature sequences
//        ArrayList<Pipe> pipeList = new ArrayList<Pipe>();
//
//        // Pipes: lowercase, tokenize, remove stopwords, map to features
//        pipeList.add( new CharSequenceLowercase() );
//        pipeList.add( new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")) );
//        pipeList.add(new TokenSequenceLowercase());
//        pipeList.add( new TokenSequenceRemoveStopwords(new File("/Users/jayashree/Documents/Resources_for_PNRank/lib/mallet-2.0.8/stoplists/en.txt"), "UTF-8", false, false, false) );
//        pipeList.add( new TokenSequence2FeatureSequence() );
//
//        InstanceList instances = new InstanceList (new SerialPipes(pipeList));
//
//        Reader fileReader = new InputStreamReader(new FileInputStream(new File("/Users/jayashree/Documents/Output_PNRank/data/OCRDataMallet.txt")), "UTF-8");
//        instances.addThruPipe(new CsvIterator(fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"),3, 2, 1)); // data, label, name fields
//        FileIterator iterator=new FileIterator(new File("Corrected"),new TxtFilter(),FileIterator.LAST_DIRECTORY);
        //instances.addThruPipe(iterator);

        // Create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
        //  Note that the first parameter is passed as the sum over topics, while
        //  the second is
//        int numTopics = 10;
//        ParallelTopicModel model = new ParallelTopicModel(numTopics, 1.0, 0.01);
//        model.addInstances(instances);
//        // Use two parallel samplers, which each look at one half the corpus and combine
//        //  statistics after every iteration.
//        model.setNumThreads(2);
//        // Run the model for 50 iterations and stop (this is for testing only,
//        //  for real applications, use 1000 to 2000 iterations)
//        model.setNumIterations(50);
//        model.estimate();
        // Show the words and topics in the first instance
//        // The data alphabet maps word IDs to strings
//        Alphabet dataAlphabet = instances.getDataAlphabet();
//        //for(int i=0;i<14020;i++)
//        //Keep a map of the document and its topic distribution
//        //Map<Integer,Float> DocTopicMp = new HashMap<Integer,Float>();
//        Map<Integer,double[]> DocTopicMp1 = new HashMap<Integer,double[]>();
//        for(int iDocs=0;iDocs<Documents.size();iDocs++)
//        {
//            FeatureSequence tokens = (FeatureSequence) model.getData().get(iDocs).instance.getData();  //document no?
//            LabelSequence topics = model.getData().get(iDocs).topicSequence;  //document no?
//
//            Formatter out = new Formatter(new StringBuilder(), Locale.US);
//            for (int position = 0; position < tokens.getLength(); position++)
//            {
//                out.format("%s-%d ", dataAlphabet.lookupObject(tokens.getIndexAtPosition(position)), topics.getIndexAtPosition(position));
//            }
//            System.out.println(out);
//
//            // Estimate the topic distribution of the ith instance,
//            //  given the current Gibbs state.
//            double[] topicDistribution = model.getTopicProbabilities(iDocs);
//            DocTopicMp1.put(iDocs, topicDistribution);
//
//            //sort the topic distribution array to get highest topic. topicDistribution.
//            //	Arrays.sort(topicDistribution);
//            Map<Integer,Float> TopicScoreMap = new HashMap<Integer,Float>();
//            for (int j = 0; j < numTopics; j++)
//                //System.out.println("topic no: "+j+" topic probability: "+topicDistribution[j]);
//                TopicScoreMap.put(j, (float)topicDistribution[j]);
//            //RankScores(TopicScoreMap);
//
//					/*for(int topicLen=0;topicLen<numTopics;topicLen++)
//					{
//						DocTopicMp.put(i,(float) topicDistribution[topicLen]);
//					}*/
//        } // for loop iterating over documents
//        int docid=0;
//
//
//        Map<String,Map<String,double[]>> FinalMap=new HashMap<String,Map<String,double[]>>();
//        BufferedWriter outFinalMap= new BufferedWriter(new FileWriter("/Users/jayashree/Documents/Output_PNRank/data/TopicFinalOutput/FinalMap.txt"));
        //List <Float> TopicsList=new ArrayList<Float>();
//        for (Map.Entry<String, List<String>> entry : hm.entrySet())
//        {
//            Map<String,double[]> PersonDocTopic=new HashMap<String,double[]>();
//            String person=entry.getKey();
//            List <String> persondocuments=entry.getValue();
//            for(String docfile: persondocuments)
//            {
//                double[] doctopic= new double[numTopics];
//                //System.out.println("docfile name is "+docfile);
//                docid=hm3.get(docfile);
//                //System.out.println("docid is "+docid);
//                doctopic=DocTopicMp1.get(docid);
//                PersonDocTopic.put(docfile,doctopic);
//                //if (!(TopicsList.contains(doctopic)))
//                //TopicsList.add(doctopic);
//                //System.out.print(person+" ");
//                outFinalMap.write(person+" ");
//                for (int topLs=0; topLs<numTopics; topLs++)
//                {
//                    //System.out.print(doctopic[topLs]+ " ");
//                    outFinalMap.write(doctopic[topLs]+ " ");
//                }
//                //System.out.print("\n");
//                outFinalMap.write("\n");
//            }
//            FinalMap.put(person,PersonDocTopic);
//            //writ.write(person+","+TopicsList.size()+"\n");
//
//        } // End for looping over Map
//        outFinalMap.close();
//

//        System.out.println("Writing person doc map");
//        File fileO=new File("/Users/jayashree/Documents/Output_PNRank/data/TopicFinalOutput/PersonDocList");
//        FileOutputStream fos0=new FileOutputStream(fileO);
//        ObjectOutputStream oos0=new ObjectOutputStream(fos0);
//        oos0.writeObject(hm);
//        System.out.println(hm);
//        oos0.flush();
//        oos0.close();
//        fos0.close();
//
//        System.out.println("Writing inverted person doc map");
//        File fileOn=new File("/Users/jayashree/Documents/Output_PNRank/data/TopicFinalOutput/InvertedPersonDocList");
//        FileOutputStream fos1=new FileOutputStream(fileOn);
//        ObjectOutputStream oos1=new ObjectOutputStream(fos1);
//        oos1.writeObject(hm2);
//        oos1.flush();
//        oos1.close();
//        fos1.close();
//
//        System.out.println("Writing doc id map");
//        File file=new File("/Users/jayashree/Documents/Output_PNRank/data/TopicFinalOutput/DocIDList");
//        FileOutputStream fos2=new FileOutputStream(file);
//        ObjectOutputStream oos2=new ObjectOutputStream(fos2);
//        oos2.writeObject(hm3);
//        oos2.flush();
//        oos2.close();
//        fos2.close();
//
//
//        System.out.println("Writing doc topic map");
//        File file1=new File("/Users/jayashree/Documents/Output_PNRank/data/TopicFinalOutput/DocTopicPrbDist");
//        FileOutputStream fos3=new FileOutputStream(file1);
//        ObjectOutputStream oos3=new ObjectOutputStream(fos3);
//        oos3.writeObject(DocTopicMp1);
//        oos3.flush();
//        oos3.close();
//        fos3.close();
//
//
//				  	  /*System.out.println("Writing Topic words map");
//			  	      File fileTwo=new File("Topic10FinalOutput/TopicWordsList");
//				  	    FileOutputStream fos4=new FileOutputStream(fileTwo);
//				  	        ObjectOutputStream oos4=new ObjectOutputStream(fos4);
//				  	        oos4.writeObject(hm4);
//				  	        oos4.flush();
//				  	        oos4.close();
//				  	        fos4.close();*/
//
//
//        System.out.println("Writing Final map");
//        File fileOne=new File("/Users/jayashree/Documents/Output_PNRank/data/TopicFinalOutput/FinalMap");
//        FileOutputStream fos=new FileOutputStream(fileOne);
//        ObjectOutputStream oos=new ObjectOutputStream(fos);
//        oos.writeObject(FinalMap);
//        oos.flush();
//        oos.close();
//        fos.close();
//
//
//				  	 /* System.out.println("Writing Topics List");
//	    		      File filem=new File("/Users/Haimonti/Research/IIITD/Aayushee/Code/Source-Code/Thesis/src/ReputedPersonDetection/data/TopicFinalOutput/PersonTopicsList");
//	    		      BufferedWriter bw=new BufferedWriter(new FileWriter(filem));
//	    		      for(Float top:TopicsList)
//	    		      {
//	    		      bw.write(top+"\n");
//	    		      }
//	    	        bw.close();*/
//
//        //Code for writing TF Map to disk
//        System.out.println("Writing TFScore map");
//        File fileL=new File("/Users/jayashree/Documents/Output_PNRank/data/TopicFinalOutput/PersonTFValuesNER");
//        FileOutputStream fosL=new FileOutputStream(fileL);
//        ObjectOutputStream oosL=new ObjectOutputStream(fosL);
//        oosL.writeObject(PersonTFMap);
//        System.out.println(PersonTFMap);
//        oosL.flush();
//        oosL.close();
//        fosL.close();
//        System.out.println("Done!");


    } // end of main method
}



