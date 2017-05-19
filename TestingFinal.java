
import java.io.*;

//import java.util.*;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

//N-grams evaluation algorithm for testing accuracy of spelling correction. Better version of TestingAgain.

class TestingFinal {
	static int tp=0;
	static int tn=0;
	static int fp=0;
	static int fn=0;
	public int calculate( String [] ocrline,String [] correctline,String [] originalline,int jstart,int jend,int i,int kstart,int kend)
	{
	//int tp=0,fp=0,tn=0,fn=0;
	int k=0;
	
	for(int j=jstart;j<=jend;j++)
	 {
		boolean flag1=true;
		boolean flag0=true;

		System.out.println(correctline[i]+" "+originalline[j]);
	 if (correctline[i].equals(originalline[j]))
	 {	 for(k=kstart;k<kend;k++)
		 {			 
			 if(ocrline[k].equals(correctline[i]))
     {
	  tn=tn+1;
	  System.out.println("tn="+tn);
	  flag0=false;
	  
	 return tn;
     } 
			 }
	
	  
//else if ((correctline[i].equals(originalline[j]))&&(ocrline[i].equals(correctline[i])))
	if(flag0==true)
		{tp=tp+1;
	 System.out.println("tp="+tp);
	     return tp;
		}

	 }
	 
	
	//if(!(ocrline[i].equals(correctline[i])) && flag0==true)
	 //if(flag0==true&&flag==true)
	 
	 else if(!correctline[i].equals(originalline[j]) && j==jend)
	 {
		 for(k=kstart; k<kend ;k++)
		 {
			 if(ocrline[k].equals(correctline[i]))
			 {
				 fn=fn+1;
				 System.out.println("fn="+fn);

				 flag1=false;
				 return fn;
				 
			 }
		 }
		 
		 if(flag1==true)
			 {fp=fp+1;
			 System.out.println("fp="+fp);}
			 return fp;
		    }
	 }
	/* else if((ocrline[i].equals(correctline[i])) && flag1==true)
	 		{
		    fn=fn+1;
			 System.out.println("fn="+fn);
			 return fn;
		    }*/

	//return tp,fp,tn,fn;
return 1;
	}
	
	
	
public static void main(String args[]) {
		
	        //System.out.println("Start time:" +System.currentTimeMillis());

try{
	String  path = "C:\\Users\\AAYUSHEE\\Pictures\\ANNOTATION\\ORIGINAL300"; 
	 
	 
	  File  file3 = new File (path);
	  File [] original = file3.listFiles(); 
	  String  path2 = "C:\\Users\\AAYUSHEE\\Pictures\\ANNOTATION\\CORRECTEDED3"; 
	  File  file2 = new File (path2);
	  File [] corrected = file2.listFiles(); 
	  //Arrays.sort(corrected, new Comparator<File>(){
		//    public int compare(File f1, File f2)
		  //  {
		    //    return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
		    //} }); 
	  String  path3 = "C:\\Users\\AAYUSHEE\\Pictures\\ANNOTATION\\OCR300"; 
	  File  file1 = new File (path3);
	  File [] ocr = file1.listFiles(); 

			  BufferedReader reader4=null;
			  BufferedReader reader5=null;
			  BufferedReader reader6=null;

			  for(int x=0;x<ocr.length;x++)
			  {
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
  
  System.out.println(line1);
  System.out.println(line2);
  System.out.println(line3);
    String[] split1=line1.split(" ");
	String[] split2=line2.split(" ");
	String[] split3=line3.split(" ");			
	TestingFinal obj=new TestingFinal();
	
	 
	for(int i=0;i<split2.length;i++) 
	{
		
		 if(split1.length<4||split2.length<4||split3.length<4)
			{
		    	//if (!split1[i].equals(split2[i]))
				
		    	obj.calculate(split1,split2,split3,0,split3.length-1,i,0,split1.length);		 
				 
			}
		    else{
		    
		    
		    if (i==0)
		   {
		   obj.calculate(split1, split2, split3, 0, 2, 0, 0, 3); //changed 2 to 3
		    	
		   }
		   
		   else if (i==1)
		   {
			   obj.calculate(split1, split2, split3, 0, 3, 1, 0, 4); //changed 3 to 4
			   
		   }
			else if(i==(split3.length-2)||i==(split3.length-1)||i==(split3.length)||i==(split3.length+1)||i==(split1.length)||i==(split1.length-1)||i==(split1.length+1)||(i==split1.length-2))
			{
			 
				obj.calculate(split1, split2, split3, i-2, split3.length-1, i, i-2, split1.length );
				
			 }
		    
		    
			else if(i>=(split3.length+2)||i>=(split1.length+2))
			{
				
				obj.calculate(split1, split2, split3, split3.length-3, split3.length-1, i, i-2, split1.length );
				
			}
			 

			else
			 {
				obj.calculate(split1, split2, split3, i-2, i+2, i, i-2, i+3); //changed i+2 to i+3
			}	
	
   }
 }
	
}
 reader4.close();
 reader5.close();
 reader6.close();
			  }
			  
			  System.out.println("True positives in all files: "+tp);
			  System.out.println("True negatives in all files: "+tn);
			  System.out.println("False positives in all files: "+fp);
			  System.out.println("False negatives in all files: "+fn);
			  int acc1=tp+tn;
			  	int acc2=tp+tn+fp+fn;
			  	float acc=(float)acc1/acc2;
			  System.out.println("Accuracy of algorithm="+acc*100);
			  float prec=(float)tp/(tp+fp);
			  System.out.println("Precision of algorithm="+prec*100);
			  float rec=(float)tp/(tp+fn);
			  System.out.println("Recall of algorithm="+rec*100);
			  System.out.println("tpr="+rec);
			  float fpr=(float)fp/(fp+tn);
			  System.out.println("fpr="+fpr);
			  
			  float auc=(float) ((0.5*rec*fpr)+(rec*(1-fpr))+(0.5*((1-rec)*(1-fpr))));
			  System.out.println(auc);
			  
}
catch(Exception e){
	e.printStackTrace();
}
}}
