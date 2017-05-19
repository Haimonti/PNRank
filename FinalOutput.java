import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//FILE REQUIRED TO RUN ONCE INFLUENTIAL PEOPLE OUTPUT IS AVAILABLE 
public class FinalOutput {
	public static void main(String args[]) throws Exception{
	Map<String,List<String>> RevertMap=new HashMap<String,List<String>>();
	FileReader fr = new FileReader("FinalOutput/FinalOutput100Topics13-07.csv"); 
	BufferedReader br = new BufferedReader(fr); 
	String line;
	while((line = br.readLine()) != null)
	{
		String split[]=line.split(",");
		//System.out.println(split[0]+" "+split[1]);
		
		if(RevertMap.containsKey(split[0]))
		 {
		 
			List<String> val2 =RevertMap.get(split[0]);
			
			val2.add(split[1]);
		 	val2.add(split[2]);
		 	val2.add(split[3]);
	 RevertMap.put(split[0],val2);
		 }
		 else
		 {
			 List<String> val2=new ArrayList<String>();
			 	val2.add(split[1]);
			 	val2.add(split[2]);
			 	val2.add(split[3]);
			 	//String split[]=entity.split(" ");
				//if(split.length>1)
		 RevertMap.put(split[0],val2);
		 //System.out.println("Key= "+values.size()+"People list: "+ val2);
		 }
	}	
	System.out.println(RevertMap);
	
	br.close();	
	File fileF=new File("FinalOutput/FinalOutputCoref100Topics.csv");
	BufferedWriter writer1 = new BufferedWriter(new FileWriter(fileF));

	 for(Map.Entry<String,List<String>> entry1 :RevertMap.entrySet()){
	        //System.out.println(entry1.getKey());
	        	String person=entry1.getKey();
	        	List<String> vals=entry1.getValue();
	        	writer1.write(person);
	        	for(String val:vals){
	        		writer1.write(","+val);
	        	}
	        	writer1.write("\n");
	 }
	 writer1.close();
	
	
	 /*FileReader fr2 = new FileReader("FinalOutput/FinalOutputCoref.csv"); 
		BufferedReader br2 = new BufferedReader(fr2); 
		String line2;
		File fileF2=new File("FinalOutput/FinalOutputCoref2.csv");
		BufferedWriter writer2 = new BufferedWriter(new FileWriter(fileF2));
		while((line2 = br2.readLine()) != null)
		{
			String split2[]=line2.split(",");
		for(int j=1;j<split2.length;j++)
		{
			writer2.write(split2[j]+",");
		}
		writer2.write("\n");
		}
		writer2.close();
		br2.close();
	*/
	
	
	
	}
	
	
	
	
	
}
