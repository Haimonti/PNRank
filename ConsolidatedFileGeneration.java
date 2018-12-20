package org.deeplearning4j.examples.nlp.word2vec;

import com.sun.tools.javac.util.ArrayUtils;
import org.deeplearning4j.models.embeddings.WeightLookupTable;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentencePreProcessor;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.util.ArrayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.*;
import java.util.*;

/**
 * Created by agibsonccc on 10/9/14.
 *
 * Neural net that processes text into wordvectors. See below url for an in-depth explanation.
 * https://deeplearning4j.org/word2vec.html
 */

/**
 * 
 * downloaded from: dl4j-examples
 *
 */


public class ConsolidatedFileGeneration {

    private static Logger log = LoggerFactory.getLogger(Word2VecRawTextExample.class);

    public static void main(String[] args) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/jayashree/Desktop/output_washpost.txt"));

        // Gets Path to Text file
        SentenceIterator iter = new LineSentenceIterator(new File("/Users/jayashree/Desktop/new/WordToVecInput1.txt"));
        iter.setPreProcessor(new SentencePreProcessor() {
            @Override
            public String preProcess(String sentence) {
                return sentence.toLowerCase();
            }
        });
        // Strip white space before and after for each line

        // Split on white spaces in the line to get words
        TokenizerFactory t = new DefaultTokenizerFactory();

        /*
            CommonPreprocessor will apply the following regex to each token: [\d\.:,"'\(\)\[\]|/?!;]+
            So, effectively all numbers, punctuation symbols and some special symbols are stripped off.
            Additionally it forces lower case for all tokens.
         */
        t.setTokenPreProcessor(new CommonPreprocessor());

        log.info("Building model....");
        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(1)
                .iterations(10)
                .layerSize(100)
                .seed(100)
                .windowSize(2)
                .layerSize(100)
                .iterate(iter)
                .tokenizerFactory(t)
                .build();
//


//        Word2Vec vec = new Word2Vec.Builder().layerSize(100).windowSize(2)
//                .layerSize(100).iterate(iter).tokenizerFactory(t).build();
        /*Check about the syntax for BarnesHutTsne to plot

        BarnesHutTsne tsne = new BarnesHutTsne.Builder()
                .setMaxIter(1000)
                .stopLyingIteration(250)
                .learningRate(500)
                .useAdaGrad(false)
                .theta(0.5)
                .setMomentum(0.5)
                .normalize(true)
                .usePca(false)
                .build();
        vec.lookupTable().plotVocab(tsne);
        */

        log.info("Fitting Word2Vec model....");
        vec.fit();

        log.info("Writing word vectors to text file....");

        // Write word vectors to file
        WordVectorSerializer.writeWordVectors(vec, "pathToWriteto.txt");

        //Looking up word vectors from the file and copying them into weightLookupTable

        WeightLookupTable weightLookupTable = vec.lookupTable();
        Iterator<INDArray> vectors = weightLookupTable.vectors();


        //Create two maps nameAndWindows <Key: list of window words , value : named entity > and
        // nameAndWindows1 <Key: named entity, value:list of window words >
        HashMap<String,ArrayList<String> > nameAndWindows1= new HashMap<>();
        HashMap<ArrayList<String>,String > nameAndWindows= new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("/Users/jayashree/Desktop/new/inputForNameWindowHashMap1.txt"))) {
            String fullLine;
            int c = 0;

            while ((fullLine = reader.readLine()) != null) {
                c++;
                String[] tempArray= fullLine.split("#");
//                System.out.println(c);

                String[] wordsInWindow= tempArray[1].trim().split(" ");
                nameAndWindows.put(new ArrayList<>(Arrays.asList(wordsInWindow)),tempArray[0]);
                nameAndWindows1.put(tempArray[0],new ArrayList<>(Arrays.asList(wordsInWindow)));
            }
        }


//      Code  commented while refactoring.
//        HashMap<String,ArrayList<String> > nameAndWindows1= new HashMap<>();
//        try (BufferedReader reader = new BufferedReader(new FileReader("/Users/jayashree/Desktop/new/inputForNameWindowHashMap1.txt"))) {
//            String fullLine;
//
//            while ((fullLine = reader.readLine()) != null) {
//                String[] tempArray= fullLine.split("#");
//                String[] wordsInWindow= tempArray[1].trim().split(" ");
//                nameAndWindows1.put(tempArray[0],new ArrayList<>(Arrays.asList(wordsInWindow)));
//            }
//        }

        System.out.println("other hashmap: "+ nameAndWindows1.size());



        // final_output_values condenses the word vectors and stores final word vectors for a named entity.
        // track map keeps track of how many times and entity occurs int he dataset.
        HashMap<String, double[] > final_output_values =  new HashMap< String, double[]>();
        HashMap<String, Integer > trackMap =  new HashMap< String, Integer>();
        for(Map.Entry entry : nameAndWindows1.entrySet()){
            String key= (String)entry.getKey();
            final_output_values.put(key,null);
            trackMap.put(key,1);

        }
        for(Map.Entry entry : nameAndWindows.entrySet()) {
            ArrayList<String> value = (ArrayList<String>) entry.getKey();
            String key = (String) entry.getValue();
            double[] word2vector = new double[100];
//            //Arrays.fill(word2vector,0,400,0.0);
//            //double[] wordvector = new double[400] ;
//
//            //int i= 0;
//            //System.out.println("Size of value: "+word2vector.length);

            if (final_output_values.containsKey(key)) {
                int i =0;
//                //System.out.println("Key occuring multiple times is : " + key);
                if (trackMap.containsKey(key)) {
                    int count = trackMap.get(key);
                    trackMap.put(key, count + 1);
                }
//
                double[] tempArr = final_output_values.get(key);
//                System.out.println("size" + tempArr.length);
//
                for (String word : value) {
//                    System.out.println("word: "+ value.size());
                    INDArray wordVectorMatrix = vec.getWordVectorMatrix(word);
                    double[] temp = vec.getWordVector(word);
                    if (temp != null) {


                        for (double c : temp) {
                            word2vector[i] = c + word2vector[i];
                            i++;
                        }
                        i=0;
                    } else {
                        for (int c = 0; c < 100; c++) {
                            word2vector[i] = 0.0+ word2vector[i];
                            i++;
                        }
                        i=0;
                    }

                }

                if(tempArr!=null){
                    for ( i = 0; i < word2vector.length; i++) {
                        word2vector[i] = word2vector[i] + tempArr[i];
                    }
                }
                final_output_values.put(key, word2vector);
            }
        }

        // condensing word vector to avg of word vectors
        for(Map.Entry entry: trackMap.entrySet()){
            String key = (String) entry.getKey();
            int value = (Integer) entry.getValue();
            if(value>1){
                double[] arr = final_output_values.get(key);
                for (int j = 0; j<arr.length;j++){
                    arr[j] = arr[j]/value;
                }
                final_output_values.put(key,arr);
            }
        }


        for (Map.Entry entry: final_output_values.entrySet()){
            String key = (String)entry.getKey();
            double[] value = final_output_values.get(key);
            if (value == null){
                value = new double[100];
                Arrays.fill(value,0.0);
                final_output_values.put(key,value);
            }
        }
        System.out.println("output: "+final_output_values.size());

//-------------------------------------------------------------------------------------

        // writing into csv file in format required
        BufferedWriter consolidatedWriter = new BufferedWriter(new FileWriter("/Users/jayashree/Documents/Output_WashPost/data/consolidatedFile1.csv"));
        BufferedWriter cw = new BufferedWriter(new FileWriter("/Users/jayashree/Desktop/defaulterList.txt"));
        HashMap<String, String> signatureMap =new HashMap<>();
        try(BufferedReader br3 = new BufferedReader(new FileReader("/Users/jayashree/Documents/Output_WashPost/data/Signature_feature.csv")))
        {
            String line3="";
            while((line3 = br3.readLine())!= null)
            {
                String[] signatutrFeature = line3.split(",");

                signatureMap.put(signatutrFeature[0], signatutrFeature[1]+"#"+signatutrFeature[2]);
            }
        }


//        System.out.println(signatureMap.get("joanna loh-man"));

        consolidatedWriter.write("Name of Person" +","+ "NDL Score"+","+"NSIM Score"+","+"NTF Score"+","+"Signature Titles"+","+"Signature Wikipedia"+",");
        //
        for(int i=1;i<101;i++){
            consolidatedWriter.write("Word Vector "+i+",");
        }
        consolidatedWriter.write("\n");

        HashMap<String, Double[]> myMap  =new HashMap<>();
        try(

                BufferedReader br2 = new BufferedReader(new FileReader("/Users/jayashree/Documents/Resources_WashPost/features/Combined3Features.csv"));
//                BufferedReader br1 = new BufferedReader(new FileReader("/Users/jayashree/Documents/Output_PNRank/data/features/rankedNSIM.csv"));
//                BufferedReader br = new BufferedReader(new FileReader("/Users/jayashree/Documents/Output_PNRank/data/features/rankedNDL.csv"))
        ) {
            String line="", line1="", line2="";
//            while ((line = br.readLine()) != null && (line1 = br1.readLine()) != null&& (line2 = br2.readLine()) != null ) {
            line2= br2.readLine();
            while ((line2 = br2.readLine()) != null ) {
                // use comma as separator

                String[] NDL= line2.split(",");
//                System.out.println(NDL[0]);
//                String[] NSIM = line1.split(",");
//                String[] NTF = line2.split(",");
                if (signatureMap.containsKey(NDL[0])) {
                    System.out.println(NDL[0]);
                    String[] sigfeatureArray = signatureMap.get(NDL[0].trim()).split("#");
                    //In the line below just change "yourMapforWordtoVec" with ur hashmap name with <String , Double[]>
                    consolidatedWriter.write(NDL[0] + "," + NDL[1] + "," + NDL[2] + "," + NDL[3] + "," + sigfeatureArray[0] + "," + sigfeatureArray[1] + "," + Arrays.toString(final_output_values.get(NDL[0].trim().replaceAll(" ", "-").trim())).replaceAll("\\[", " ").replaceAll("\\]", ""));
                    consolidatedWriter.write("\n");
                }
                else{
                    cw.write(NDL[0]+"\n");

                }
            }
        }
        consolidatedWriter.flush();
        consolidatedWriter.close();
        cw.flush();
        cw.close();
        System.out.println("Done!!!");
        // TODO resolve missing UiServer
//        UiServer server = UiServer.getInstance();
//        System.out.println("Started on port " + server.getPort());
    }
}

