// ----------------------------------
// Assignment 3
// Written by: Kevin Ve 40032669
// ----------------------------------

package bibliography_factory;

import java.io.File;

/**FileInvalidException
 * thrown when an article is invalid
 * ie. when a field in an article is an empty field (page={})
 * @see Article
 * @author KevinVe
 */
public class FileInvalidException extends Exception{
	private static final long serialVersionUID = 6698251025346979581L;
	
	private File f;			//for getting invalid files path and name of file
	private String field;	//for getting field that caused exception to be thrown
	
	/**Default Constructor
	 * Default error message
	 */
	public FileInvalidException() {
		super("Error: Input file cannot be parsed due to missing information (i.e. month= {}, title= {}, etc.)");
	}
	
	/**Parameterized Constructor
	 * saves field that caused this exception to be thrown 
	 * @param field name of field
	 */
	public FileInvalidException(String field) {
		this.field = field;
	}
	
	/**setFile
	 * saves file that caused this exception to be thrown
	 * not part of constructor because exception is thrown before it is know from which file it came from
	 * @param f File that caused exception to be thrown
	 */
	public void setFile(File f) { this.f = f;}
	
	/**getMessage
	 * overrides Exception.getMessage() to produce own informative message
	 */
	public String getMessage() {
		return "ERROR: Detected empty field in file "+ f.getPath() + "\n"
				+ "File is Invalid: " + field + " is empty. Processing has stopped. Other empty fields may be present.\n"
				+ "====================================================================================\n" ;
	}
}
