// ----------------------------------
// Assignment 3
// Written by: Kevin Ve 40032669
// ----------------------------------

package bibliography_factory;

/**JsonData
 * Holds a piece of json name and data
 * @author KevinVe
 */
public class JsonData {
	private String name;
	private String data;
	
	/**@param name name of field
	 * @param data values inside of field
	 */
	public JsonData(String name, String data) {
		this.name = name;
		this.data = data;
	}
	
	public String getName() {return name;}
	public String getData() {return data;}
	
	public void setName(String name) {this.name = name;}
	public void setData(String data) {this.data = data;}
	
	
}
