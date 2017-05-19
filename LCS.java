import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


//Used to compare accuracy of LCS algorithm with out N Gram algorithm
public class LCS {
	static int tp=0;
	static int tn=0;
	static int fp=0;
	static int fn=0;

	 public int calculate( String [] ocrline,String [] correctline,String [] originalline,int i)
		{
		//int tp=0,fp=0,tn=0,fn=0;
		boolean flag0=true;
		boolean flag1=true;
		 
			System.out.println(correctline[i]+" "+ocrline[i]);
		List<String> lcs1=LCS.LongestCommonSubsequence(correctline,originalline);
		System.out.println(lcs1);
		if(lcs1.contains(correctline[i])&& (!(ocrline[i].equals(correctline[i]))))	
	     {
		  tp=tp+1;
		  System.out.println("tp="+tp);
		  flag0=false;
		 return tp;
		  
		  }
		  if (lcs1.contains(correctline[i])&&(ocrline[i].equals(correctline[i])))
		      {
					 tn=tn+1;
			  System.out.println("tn="+tn);
			  flag1=false;
			return tn;
			  // break; 
		      }

		 if(!(ocrline[i].equals(correctline[i])) && flag0==true)
		 //if(flag0==true&&flag==true)
		 		{
			    fp=fp+1;
				 System.out.println("fp="+fp);
				 return fp;
			    }
		 
		 else if((ocrline[i].equals(correctline[i])) && flag1==true)
		 		{
			    fn=fn+1;
				 System.out.println("fn="+fn);
				 return fn;
			    }
		
		 return 1;
		//return tp,fp,tn,fn;
		 
		 
		}
	    
	 public static List<String> LongestCommonSubsequence(String[] s1, String[] s2)
	 {
	         int[][] num = new int[s1.length+1][s2.length+1];  //2D array, initialized to 0

	         //Actual algorithm
	         for (int i = 1; i <= s1.length; i++)
	                 for (int j = 1; j <= s2.length; j++)
	                         if (s1[i-1].equals(s2[j-1]))
	                                 num[i][j] = 1 + num[i-1][j-1];
	                         else
	                                 num[i][j] = Math.max(num[i-1][j], num[i][j-1]);

	       //  System.out.println("length of LCS = " + num[s1.length][s2.length]);

	         int s1position = s1.length, s2position = s2.length;
	         List<String> result = new LinkedList<String>();

	         while (s1position != 0 && s2position != 0)
	         {
	                 if (s1[s1position - 1].equals(s2[s2position - 1]))
	                 {
	                         result.add(s1[s1position - 1]);
	                         s1position--;
	                         s2position--;
	                 }
	                 else if (num[s1position][s2position - 1] >= num[s1position][s2position])
	                 {
	                         s2position--;
	                 }
	                 else
	                 {
	                         s1position--;
	                 }
	         }
	         Collections.reverse(result);
	         return result;
	 }
	
	
	
	    public static void main(String[] args) {

	    	
	    	
	    	String[] xa = {"lead", "contours", "in", "the" ,"saturn" ,"rated" ,"zone" ,"envy" ,"irons" ,"alternative"};
	        String[] yb = {"lead", "contours", "in", "the", "saturated" ,"zone" ,"environs" ,"of", "alternative"};
	    	LCS obj=new LCS();
	 
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
int d=0,e=0,f=0;
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
	  //  		TestingFinal obj=new TestingFinal();
	    		
	    		 
	    		for(int i=0;i<split1.length;i++) 
	    		{
	    			
	    			
	    				 
	    					obj.calculate(split1, split2, split3, i);
	    					// System.out.println("d="+d+ "  tp="+tp+"  fp="+fp+"  tn="+tn+" fn="+fn);
	    				 	
	    		
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
	    			//	  String a= obj.meth(xa,yb);
	    				//    System.out.println("calling from main: "+a);
	    				 	  
	    	}
	    	catch(Exception e){
	    		e.printStackTrace();
	    	}
	    
	    }
	 
	   
	    
	   public String meth(String x,String y){
	    	
	        int M = x.length();
	        int N = y.length();

	        // opt[i][j] = length of LCS of x[i..M] and y[j..N]
	        int[][] opt = new int[M+1][N+1];

	        // compute length of LCS and all subproblems via dynamic programming
	        for (int i = M-1; i >= 0; i--) {
	            for (int j = N-1; j >= 0; j--) {
	                if (x.charAt(i) == y.charAt(j))
	                    opt[i][j] = opt[i+1][j+1] + 1;
	                else 
	                    opt[i][j] = Math.max(opt[i+1][j], opt[i][j+1]);
	            }
	        }
String lcs="";
	        // recover LCS itself and print it to standard output
	        int i = 0, j = 0;
	        while(i < M && j < N) {
	            if (x.charAt(i) == y.charAt(j)) {
	               // System.out.print(x.charAt(i));
	                lcs+=x.charAt(i);
	               
	                i++;
	                j++;
	            }
	            else if (opt[i+1][j] >= opt[i][j+1]) i++;
	            else                                 j++;
	        }
	    //    System.out.println(lcs);
	//        System.out.println();
return lcs;}
	           

	}
