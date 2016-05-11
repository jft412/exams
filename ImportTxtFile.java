// MSIS Capstone 498 - Spring 2016 - Northwestern University - Team 4
// 04-25-2016

// ImportTxtFile reads in a .txt file, parses it into associated rows and columns,
// then creates an ArrayList of the columns based on the related .txt file
package cis498;

import java.io.*;
import java.util.ArrayList;


public class ImportTxtFile {

	public static String[][] matrix;
	public static String[] parseByRows;
	public static String[] nameCol;
	ArrayList<String[]> allColumns;
    
    int linenumber = 0;
    int numberOfCols;
    
	// readFile() reads .txt file into an object, that object is then parsed by | delimiter
    // into a matrix like 2D array, the 2D array is separated into columns which are 
    // stored in allColumns ArrayList
	public void readFile(String file) throws IOException {
		        
		// The name of the file to open.
        String fileName = file;
        
        // FileReader reads text files in the default encoding.
        FileReader fileReaderLine = new FileReader(fileName);        

        
        
        // count number lines in file - need to use separate BufferedReader
    	LineNumberReader lnr = new LineNumberReader(fileReaderLine);    	        		    
    	        while (lnr.readLine() != null){
    	        	linenumber++;
    	        } 	 
    	lnr.close();
    	// end - count number of lines
    	            
    	            
    	    
    	// FileReader reads text files in the default encoding.
    	FileReader fileReader = new FileReader(fileName);
            
        // Always wrap FileReader in BufferedReader.
        BufferedReader bufferedReader = new BufferedReader(fileReader);
            
        // to write results to a string
        StringBuffer fileContents = new StringBuffer();
            
        // This will reference one line at a time
        String line = bufferedReader.readLine();           
            
        // determine number of columns to create
        String[] parse = line.split("\\|");
		numberOfCols = parse.length;        		    
		    
		// create new array objects based on column and row numbers
		matrix = new String[linenumber][numberOfCols]; // X x X matrix of parsed text file
		parseByRows = new String[linenumber]; // hold each line in a separate dimension of an array
		nameCol = new String[linenumber]; // separated columns from matrix
		allColumns = new ArrayList<String[]>(); // arraylist of columns from each file
		    
		
		// counters to iterate into 2d array (matrix)
        int i = 0;            
        // separate data into a matrix by [rows][columns] using 2D array
        while(line != null) {
                
            	// Insert line into an array (parseByRows[])
            	if(i<=(linenumber-1)){   		       		    		
            		
            		parseByRows[i] = line;
            		//System.out.println("Number i: "+i+"\nArray Row: "+parseByRows[i]);            		
            		
            		// parse the line by pipe delimiter
            		String[] items = parseByRows[i].split("\\|");
            		            		            		
            		// using 2D array, place each parsed item into a column of each row/line
		        	for(int k=0; k<=(numberOfCols-1); k++){
		        		
	            		// skips first row iteration - which is the column names
		        		if(i==0){
            			continue;
	            		}
		        		
			        	matrix[i][k] = items[k];
			        	//System.out.println("K = "+k+" Value["+i+"]["+k+"] "+matrix[i][k]);
			        	
		        	} // - end for 
		        	
            	} // - end if
            	
            // skip to next line at \n then read next line into line String
            fileContents.append(line).append("\n");
		    line = bufferedReader.readLine();		      
		       	        
		    // increase i counters
			i++;
			    
		 } // - end while loop
            
            
          // Always close files.
          bufferedReader.close(); 
            
            
          // put each column from matrix into own column            
          for(int k1=0; k1<=(numberOfCols-1); k1++){
		            
            	// i1=1 so doesn't include column name in array            		
            	for(int i1=1; i1<=(linenumber-1); i1++){ 
		            	
		            // puts every row from this column into its own array and prints out row value   
		            nameCol[i1]= matrix[i1][k1];
		            //System.out.println(nameCol[i1]);
		            	 	 
            	} // - end for 
		         
            	// insert each separated column into arraylist		         
            	allColumns.add(nameCol);
		         
            	// clear nameCol so can receive another set of values
            	nameCol = new String[linenumber];
			
            	// System.out.println(Arrays.toString(allColumns.get(k1)));

           } // - end for
             
 }   // - end readFile()                     
   	

} // end class - ImportTxtFile
	
	