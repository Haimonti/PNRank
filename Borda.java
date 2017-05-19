import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class Borda {

	/**
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		File f1=new File("FinalOutput2310/rankedNDL.csv");
		File f2=new File("FinalOutput2310/rankedNSIM.csv");
		File f3=new File("FinalOutput2310/rankedNTF.csv");
		BufferedReader br1=new BufferedReader(new FileReader(f1));
		BufferedReader br2=new BufferedReader(new FileReader(f2));
		BufferedReader br3=new BufferedReader(new FileReader(f3));

		HashMap<String,Double> BordaScore=new HashMap<String,Double>();
		String line;
		while ((line = br1.readLine()) != null) 
		{
			String split[]=line.split(",");
			BordaScore.put(split[0], Double.parseDouble(split[2]));
		}
		String line2;
		while ((line2 = br2.readLine()) != null) 
		{
			String split[]=line2.split(",");
			Double oldval=BordaScore.get(split[0]);
				Double newval =oldval+Double.parseDouble(split[2]);
			BordaScore.put(split[0], newval);

		}
		String line3;
		while ((line3 = br3.readLine()) != null) 
		{
			String split[]=line3.split(",");
			Double oldval=BordaScore.get(split[0]);
				Double newval =(oldval+Double.parseDouble(split[2]))/3;
			BordaScore.put(split[0], newval);

		}
		
		
		br1.close();
		br2.close();
		br3.close();
		
		//Reading the map of person names and the list of news articles in which they occur
		File toRead=new File("FinalOutput2310/PersonDocList");
        FileInputStream fis=new FileInputStream(toRead);
        ObjectInputStream ois=new ObjectInputStream(fis);

        HashMap<String,List<String>> mapInFile=(HashMap<String,List<String>>)ois.readObject();
        System.out.println(mapInFile.size());
        ois.close();
        fis.close();

        Map<String, Double> sortedMap = NERDemo.RankScores(BordaScore);
      
        
		File f4=new File("FinalOutput2310/BordaScore4.csv");
	        BufferedWriter bw4=new BufferedWriter(new FileWriter(f4));
	        int i=0;
	        for(Map.Entry<String,Double> m1:sortedMap.entrySet()){
	        	
	        	String person=m1.getKey();
	        	List<String> docs=mapInFile.get(person);
	        bw4.write(++i+","+person+","+m1.getValue()+","+docs.size());
	        bw4.write("\n");
	        }
	        bw4.close();
	      
	        
	        
	        
	}
	
	

}
