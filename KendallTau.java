import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class KendallTau {

    // return Kendall tau distance between two permutations
    public static long distance(int[] a, int[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Array dimensions disagree");
        }
        int n = a.length;

        int[] ainv = new int[n];
        for (int i = 0; i < n; i++)
            ainv[a[i]] = i;

        Integer[] bnew = new Integer[n];
        for (int i = 0; i < n; i++)
            bnew[i] = ainv[b[i]];

        return Inversions.count(bnew);
    }


    


    public static void main(String[] args)throws Exception {
     
    	File f1=new File("FinalOutput2310/rankedNDL.csv");
		File f2=new File("FinalOutput2310/rankedNSIM.csv");
		File f3=new File("FinalOutput2310/rankedNTF.csv");
		BufferedReader br1=new BufferedReader(new FileReader(f1));
		BufferedReader br2=new BufferedReader(new FileReader(f2));
		BufferedReader br3=new BufferedReader(new FileReader(f3));

		HashMap<String,List<Integer>> MedianScore=new HashMap<String,List<Integer>>();
	
		 File f4=new File("FinalOutput2310/rankedMedianScores.csv");
	        BufferedWriter bw=new BufferedWriter(new FileWriter(f4));
		
		String line;
		int i=1;
		while ((line = br1.readLine()) != null) 
		{
			List<Integer> Score=new ArrayList<Integer>();

			String split[]=line.split(",");
			Score.add(i);
			i=i+1;
			MedianScore.put(split[0], Score);
		}
		br1.close();
		
	
		String line2;
		int j=1;
		while ((line2 = br2.readLine()) != null) 
		{
			String split[]=line2.split(",");
			List<Integer> Score=MedianScore.get(split[0]);
			Score.add(j);
			j=j+1;
			MedianScore.put(split[0], Score);

		}
		String line3;
		int k=1;
		while ((line3 = br3.readLine()) != null) 
		{
			String split[]=line3.split(",");
			List<Integer> Score=MedianScore.get(split[0]);
				Score.add(k);
				k=k+1;
			MedianScore.put(split[0], Score);

		}
		
		br2.close();
		br3.close();
		System.out.println(MedianScore);
		
		HashMap<String,Double>FinalRank=new HashMap<String,Double>();
	       Integer[]arrayscores = new Integer[3];
	       double median=0.0;
		 for(Map.Entry<String,List<Integer>> me:MedianScore.entrySet()){
		       List<Integer> scores=me.getValue();
		       scores.toArray(arrayscores);
		       median=KendallTau.CalcMedian(arrayscores);
		       FinalRank.put(me.getKey(),median);
		 }
		 
		 
		 Map<String,Double>RankedMedian=sortByValue(FinalRank);
		
		 for(Map.Entry<String,Double> me2:RankedMedian.entrySet()){
  
		 bw.write(me2.getKey()+","+me2.getValue()+"\n");

		 }
		 bw.close();
		 System.out.println(RankedMedian);
    	/*int n = 5;
        int[] a = {0,4,1,2,3};
        int[] b = {4,0,3,1,2};


        // print initial permutation
        for (int i = 0; i < n; i++)
            System.out.println(a[i] + " " + b[i]);
        System.out.println();

        System.out.println("inversions = " + KendallTau.distance(a, b));
   */
    }
    
    public static double CalcMedian(Integer[] arrayscores){
    	
    	 Arrays.sort(arrayscores);
        // System.out.print("Sorted Scores: ");
        
    
         // Calculate median (middle number)
         double median = 0;
         double pos1 = Math.floor((arrayscores.length - 1.0) / 2.0);
         double pos2 = Math.ceil((arrayscores.length - 1.0) / 2.0);
         if (pos1 == pos2 ) {
            median = arrayscores[(int)pos1];
         } else {
            median = (arrayscores[(int)pos1] + arrayscores[(int)pos2]) / 2.0 ;
         }
        
    return median;	
    }
 
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> unsortMap) {

	    List<Map.Entry<K, V>> list =
	            new LinkedList<Map.Entry<K, V>>(unsortMap.entrySet());

	    Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
	        public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
	            return (o1.getValue()).compareTo(o2.getValue());
	        }
	    });

	    Map<K, V> result = new LinkedHashMap<K, V>();
	    for (Map.Entry<K, V> entry : list) {
	        result.put(entry.getKey(), entry.getValue());
	    }

	    return result;

	}
    
}

