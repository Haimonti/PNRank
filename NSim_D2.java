

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

class NSim_D2{
	
	
public static double readMedian() throws Exception{
				double avgmed = 0.0;
				String line2;
				File f2 = new File ("D:/Coref/AvgMedian.txt");
			BufferedReader reader2=new BufferedReader(new FileReader(f2));
int d = 0;
while ((line2 = reader2.readLine())!= null ){
	String [] arr = line2.split(",");
	avgmed+=Double.parseDouble(arr[1]);
	d+=1;
	//System.out.println(d);	
}
System.out.println("Average median: " +avgmed/d);
return avgmed/d;
}
public static void main(String args[]) throws Exception{

//double avgmed = readMedian();
double avgper = 19; //99th percentile
//double avgper = 0.43; //0th percentile

BufferedWriter writer2 = new BufferedWriter(new FileWriter("NSim.csv"));
File f=new File("D:/Coref/KLDivList.txt");
			BufferedReader reader=new BufferedReader(new FileReader(f));
			String line;
	
			while((line=reader.readLine())!=null){
				String[] spl = line.split(",");
		        int i = 1;
			    int count = 0;
				while (i!=spl.length){
					double kl = Double.parseDouble(spl[i]);
					if(kl>avgper){
					count+=1;
					}
					i+=1;
				}
			
			writer2.write(spl[0]+","+String.valueOf(count));
					writer2.write("\n");
					writer2.flush();
					System.out.println (count);
			}	
			
			writer2.close();

			
			}
			}