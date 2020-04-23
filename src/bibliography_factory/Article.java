// ----------------------------------
// Assignment 3
// Written by: Kevin Ve 40032669
// ----------------------------------

package bibliography_factory;

/**Article
 * class to contain and handle every field of an article
 * 3 methods to return article as 3 different reference formats; IEEE, ACM and NJ
 * @author KevinVe
 */
public class Article {
	private String[] author;
	private String journal;
	private String title;
	private int year;
	private String volume;
	private int number;
	private String pages;
	private String[] keywords;
	private String doi;
	private String ISSN;
	private String month;

	/**Constructor with no values set later by main method
	 */
	public Article() { 
	}
	
	public String[] getAuthor() {return author;}
	public String getJournal() {return journal;}
	public String getTitle() {return title;}
	public int getYear() {return year;}
	public String getVolume() {return volume;}
	public int getNumber() {return number;}
	public String getPages() {return pages;}
	public String[] getKeywords() {return keywords;}
	public String getDoi() {return doi;}
	public String getISSN() {return ISSN;}
	public String getMonth() {return month;}
	
	public void setAuthor(String[] author) {this.author = author;}
	public void setJournal(String journal) {this.journal = journal;}
	public void setTitle(String title) {this.title = title;}
	public void setYear(int year) {this.year = year;}
	public void setVolume(String volume) {this.volume = volume;}
	public void setNumber(int number) {this.number = number;}
	public void setPages(String pages) {this.pages = pages;}
	public void setKeywords(String[] keywords) {this.keywords = keywords;}
	public void setDoi(String doi) {this.doi = doi;}
	public void setISSN(String iSSN) {ISSN = iSSN;}
	public void setMonth(String month) {this.month = month;}
	
	public String toIEEEString() {
		String authorOut = author[0];	//add appropriate delimiter between each author
		for (int i = 1; i < author.length; i++)
			authorOut += ", " + author[i];
		
		return authorOut + ". \"" + title + "\", " + journal + ", " + "vol. " + volume + 
				", no. " + number + ", p. " + pages + ", " + month + " " + year + ".\n";
	}
	
	public String toACMString() {
		return author[0] + " et al. " + year + ". " + title + ". " + journal + ". " + 
				volume + ", " + number + " (" + year + "), " + pages + ". DOI:https://doi.org/"
				+ doi + ".\n";
	}
	
	public String toNJString() {
		String authorOut = author[0];	//add appropriate delimiter between each author
		for (int i = 1; i < author.length; i++)
			authorOut += " & " + author[i];
		return authorOut + ". " + title + ". " + journal + ". " + volume + ", " + pages + "(" + year + ").\n";
	}
}
