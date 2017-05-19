import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

//Trying to find out difference in lengths of ocr and original text lienes on an average
public class WindowSize {
    public static void main(String[] args) {

	try{
		String  path = "Original"; 
		 
		 
		  File  file3 = new File (path);
		  File [] original = file3.listFiles(); 
		  String  path2 = "Corrected"; 
		  File  file2 = new File (path2);
		  File [] corrected = file2.listFiles(); 
		  //Arrays.sort(corrected, new Comparator<File>(){
			//    public int compare(File f1, File f2)
			  //  {
			    //    return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
			    //} }); 
		  String  path3 = "Ocr"; 
		  File  file1 = new File (path3);
		  File [] ocr = file1.listFiles(); 

				  BufferedReader reader4=null;
				  BufferedReader reader5=null;
				  BufferedReader reader6=null;
			int l=0;
			double d=0;
				
				  for(int x=0;x<ocr.length;x++)
				  {
					  int length1=0;
					  int length2=0;

	int lines=0;
	int diff=0;
	
					  reader4 = new BufferedReader(new FileReader(ocr[x]));
						 reader5 = new BufferedReader(new FileReader(corrected[x]));
						 reader6 = new BufferedReader(new FileReader(original[x]));
						 String line1=null;
		//line1=reader4.readLine().toLowerCase();
					
						 while(reader4.ready())
		    					
					    	{ 
		line1=reader4.readLine().toLowerCase();
		
		String line3=null;
	  String line2=null;
	  line2=reader5.readLine().toLowerCase();
	  line3=reader6.readLine().toLowerCase();
	  if(line2==null)
		  System.out.println("Error in reader 5");
	    if(line3==null)
		  System.out.println("Error in reader 6");
	  
	//  System.out.println(line1.length() + " "+ line1);
	 // System.out.println(line2.length()+ " "+ line2);
	 // System.out.println(line3.length()+ " "+ line3);
	  String[] split1=line1.split(" ");
		String[] split2=line2.split(" ");
		String[] split3=line3.split(" ");
		 length1+=split1.length;
		length2+=split3.length;
		//System.out.println(split1.length+  " :ocr");
		//System.out.println(split2.length+  " :corrected");
		//System.out.println(split3.length+  " :original");
		++lines;
				// System.out.println(line1 + " : "+ line3);
					System.out.println(ocr[x].toString());
					if(Math.abs(split1.length-split3.length)>0)
					{++l;}
						
						diff+=Math.abs(split1.length-split3.length);
					System.out.println(diff+" " +split1.length+ " " +split3.length);
	
					
					    	}
						 System.out.println((double)diff/lines+" "+lines +" "+ length1+ " "+  (double)length1/lines + " "+length2+ " "+(double)length2/lines);
			
				  d=d+(double)diff/lines;
				  
				  }
				  System.out.println(d + " " + d/50 + " "+l);
				  

	}
						 catch(Exception e){}
}}
