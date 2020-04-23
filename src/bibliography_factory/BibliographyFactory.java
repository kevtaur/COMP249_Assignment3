// ----------------------------------
// Assignment 3
// Written by: Kevin Ve 40032669
// ----------------------------------

package bibliography_factory;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.io.BufferedReader;

/**Kevin Ve 40032669
 * COMP 249
 * Assignment 3
 * Due Date: March 30, 2020
 * 
 * BibliographyFactory
 * given some amount of latex bib files with a number of articles
 * read each article in each file and write them into 3 different formats; IEEE, ACM and NJ
 * if latex file cannot be found then program quits
 * if an article is not valid then its output files are deleted
 * once writes are successful, prompt user for a valid file name and display its contents
 * @author KevinVe
 * @see Article
 * @see JsonData
 * @see FileInvalidException
 */
public class BibliographyFactory {
	/**{@value #STARTING_FILE} = first file to read
	 */
	final static int STARTING_FILE = 1  ; // loop from, used for testing, default = 1
	
	/**{@value #ENDING_FILE} = last file to read
	 */
	final static int ENDING_FILE = 10;    // loop to, default = 10
	
	/**{@value #PATH} = location of file directory
	 */
	final static String PATH = "files\\"; //file path for all input and output files
	
	public static void main(String[] args) {
		File[] fileInputArray = new File[ENDING_FILE]; 			//array of input files used to keep its path and file name
		FileReader[] readArray = new FileReader[ENDING_FILE];   //array of input streams for each file
		
		File[][] fileOutputArray = new File[ENDING_FILE][3];		  // array of output files for cleanup. 0=IEEE. 1=ACM, 2=NJ
		PrintWriter[][] writeArray = new PrintWriter[ENDING_FILE][3]; // array of output streams for each file
		
		System.out.println("Welcome to Kevin Ve's Bibliography Factory!");
		
		//main steps of BibliographyFactory each separated into static methods
		initializeInputFiles(fileInputArray, readArray);
		initializeOutputFiles(fileOutputArray, writeArray);
		System.out.print("\n");
		
		processFilesForValidation(readArray, writeArray, fileInputArray, fileOutputArray);
		
		for (PrintWriter[] pwAr : writeArray) //flush and close all outputs
			for(PrintWriter pw : pwAr)
				pw.close();
		
		displayContents();
		
		System.out.println("Bibliography Factory finised!");
	}

	/**initializeInputFiles
	 * Take 2 arrays of type File and FileReader and initialize each element
	 * If a file cannot be found, all opened streams will be closed and the program will exit.
	 * @param fileInputArray array of File, ranging through each file to be read
	 * @param readArray array of FileReader, ranging through each file
	 */
	private static void initializeInputFiles(File[] fileInputArray, FileReader[] readArray) {
		for (int i=STARTING_FILE; i<= ENDING_FILE; i++) { // open all files and store in array fileArray
			try {
				fileInputArray[i-1] = new File(PATH+"Latex"+i+".bib"); //store appropriate file 
				readArray[i-1] = new FileReader(fileInputArray[i-1]);  //open its corresponding input stream
				
			} catch (FileNotFoundException e) {
				System.out.println("Could not open input file Latex"+i+".bib for reading.\nPlease check if file exists! Program will terminate after closing any open files");
				try {
					for (int x = 0; x < i; x++)	//close all opened FileReaders thus far
						readArray[x].close();
				} catch (Exception e1) {} //shouldn't happen there do nothing
				System.exit(0);
			}
		}
	}
	
	/**initializeOutputFiles 
	 * Take 2 empty 2D arrays of File and PrintWriter and initialize each element.
	 * While initializing the fileOutputArray, it will create the files as needed if they do not exist.
	 * @param fileOutputArray 2D array of File, first ranging through the number of files then through each type (IEEE, ACM and NJ)
	 * @param writeArray 2D array of PrintWriter, ranging through number of files then each type 
	 */
	private static void initializeOutputFiles(File[][] fileOutputArray, PrintWriter[][] writeArray) {
		for (int a=STARTING_FILE; a <= ENDING_FILE; a++){ //instantiate files to write to
			fileOutputArray[a-1][0] = new File(PATH+"IEEE"+a+".json"); // 0 = IEEE files
			fileOutputArray[a-1][1]= new File(PATH+"ACM"+a+".json");   // 1 = ACM files
			fileOutputArray[a-1][2] = new File(PATH+"NJ"+a+".json");   // 2 = NJ files
			
			try {
				writeArray[a-1][0] = new PrintWriter(fileOutputArray[a-1][0]);
				writeArray[a-1][1] = new PrintWriter(fileOutputArray[a-1][1]);
				writeArray[a-1][2] = new PrintWriter(fileOutputArray[a-1][2]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**processFilesForValidation
	 * keeping track of number of articles and invalid files, loop through each input stream to write
	 * its corresponding 3 output streams (IEEE, ACM and NJ). If a file is invalid then skip processing it 
	 * altogether and delete its respective output files.
	 * @param readArray array of input streams
	 * @param writeArray array of output streams
	 * @param fileInputArray array of input files
	 * @param fileOutputArray array of output files
	 */
	private static void processFilesForValidation(FileReader[] readArray, PrintWriter[][] writeArray, File[] fileInputArray, File[][] fileOutputArray) {
		int totalArticleCount = 0;
		int articleCount;		    // per file
		int fileInvalidCount  = 0;
		for (int i=STARTING_FILE; i<=ENDING_FILE; i++) { // parse each file
			articleCount = 0; //initialize per file count
			try {
				articleCount = parseFile(readArray[i-1], writeArray[i-1]); //returns number of articles processed for tracking
				totalArticleCount += articleCount;
				
			} catch (FileInvalidException e) {  //if a file has an invalid field
				e.setFile(fileInputArray[i-1]); //send exception which file is invalid and inform user
				System.out.println(e.getMessage());
				
				for (int b = 0; b <3; b++) {      // delete its corresponding output files
					writeArray[i-1][b].close();   // file must be closed before it can be deleted
					fileOutputArray[i-1][b].delete(); 
				}
				
				fileInvalidCount++;
			}
		}	
		System.out.println(totalArticleCount + " total articles processed.");
		System.out.println("A total of " + fileInvalidCount + " files were invalid and could not be processed.\n"
						 + "All other " + (ENDING_FILE-STARTING_FILE+1-fileInvalidCount) + " other valid files have been created.");
	}
	
	/**parseFile
	 * loops through the input stream line by line. Once "@Article" is found then move on to article parsing
	 * once article is done parsing, write each article into the 3 formats in their respective output streams
	 * 
	 * @param in input stream
	 * @param out output stream
	 * @return number of articles processed in file
	 * @throws FileInvalidException thrown by parseArticle
	 * @see Article
	 * @see parseArticle
	 */
	private static int parseFile(FileReader in, PrintWriter[] out) throws FileInvalidException {
		Scanner fileIn = new Scanner(in);
		int articleCount = 0;
		
		while(fileIn.hasNextLine()) {
			String line = fileIn.nextLine();
			
			if (line.contains("@ARTICLE")) {		 //if start of article is encountered
				Article temp = parseArticle(fileIn); //get article and write output to respective files
				out[0].println(temp.toIEEEString());
				out[1].println("[" + (articleCount) + "]\t" + temp.toACMString());
				out[2].println(temp.toNJString());
				articleCount++;
			}
		}
		return articleCount;
	}
	
	/**parseArticle
	 * passing the inputStream immediate after the @ARTICLE tag is encountered
	 * create and fill in values for an Article class
	 * @param fileIn inputStream to be looped through
	 * @return article the article class holding all of data until the } tag
	 * @throws FileInvalidException thrown when article has an empty field (ie. page={})
	 * @see Article
	 * @see FileInvalidException
	 */
	private static Article parseArticle(Scanner fileIn) throws FileInvalidException {
		String line = fileIn.nextLine(); //get line after @ARTICLE
		Article article = new Article(); //create empty article to add values to through this method
		while(!line.equals("}")) {		 //loops through until end of article tag is found 
			line = fileIn.nextLine();
			
			if (line.split("=").length == 2) { 		 //if valid piece of JSON data
				JsonData data = parseJsonData(line); //get its field name and data
				if(data.getData().equals("")) 		 //if data is empty then the entire file is invalid
					throw new FileInvalidException(data.getName());
				
				switch (data.getName()) {
				case "author":
					article.setAuthor(data.getData().split(" and "));
					break;
				case "journal":
					article.setJournal(data.getData());
					break;
				case "title":
					article.setTitle(data.getData());
					break;
				case "year":
					article.setYear(Integer.parseInt(data.getData()));
					break;
				case "volume":
					article.setVolume(data.getData());
					break;
				case "number":
					article.setNumber(Integer.parseInt(data.getData()));
					break;
				case "pages":
					article.setPages(data.getData());
					break;
				case "keywords":
					article.setKeywords(data.getData().split(";"));
					break;
				case "doi":
					article.setDoi(data.getData());
					break;
				case "ISSN":
					article.setISSN(data.getData());
					break;
				case "month":
					article.setMonth(data.getData());
					break;
				}
			}
		}
		return article;
	}
	
	/**parseJsonData
	 * assuming passed string is a valid JSON object (ie. name={data})
	 * return JsonData object holding name and data for more accessible use
	 * @param input assumed valid JSON object
	 * @return JsonData holding valid JSON object's name and data
	 */
	private static JsonData parseJsonData(String input) {
		String[] a = input.split("=");
		String name = a[0];				//get string before the "="
		String data = a[1].substring(a[1].indexOf("{")+1,(a[1].indexOf("}"))); //get string between { and }
		return new JsonData(name, data);
	}
	
	/**displayContents
	 * prompt user for valid file name to be read and displays it
	 * if the user fails to input a valid name, user gets one last attempt
	 * once last attempt is failed then method moves on
	 */
	private static void displayContents() {
		FileReader f; 	//instantiate variables before try block
		BufferedReader reader;
		String line;
		Scanner keyIn = new Scanner(System.in);
		int failedInputs = 0; //flag for if user failed to input a file name, method moves on once failedInputs = 2
		
		while (failedInputs < 2) { //while user still has attempts remaining
			System.out.print("Please enter the name of one of the files you would like to review: ");
			try {
				f = new FileReader(PATH+keyIn.next());  //get file with FileReader
				reader = new BufferedReader(f);
				line = reader.readLine();
				while (line != null) {			//print every line in file
					System.out.println(line);
					line = reader.readLine();
				}
				
			} catch (FileNotFoundException e) {			//if file does not exist
				if (failedInputs == 0)					//increment failedInputs by one
					System.out.println("Could not open input file. File does not exist; possibly could not be created! You have one more attempt.\n");
				else									//move on from method loop
					System.out.println("Too many attempts.\n");
				failedInputs++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
