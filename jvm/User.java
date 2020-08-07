package jvm;

public class User {
    private int id;
	private String name;
	private String email;
	private String gender;
	private String userType;
	
	User() {
	}
	
	User (int id, String name, String email, String gender, String userType) {
	    this.id = id;
		this.name = name;
		this.email = email;
		this.gender = gender;
		this.userType = userType;
	}
	
	public boolean saveWebLink(String url, String title) {
	   System.out.println("\nSaving new WebLink ...");
	   System.out.println("WebLink URL: " + url);
	   System.out.println("Title: " + title);
	   return true;
	}
	
	public void postBookmarkReview(int bookmarkId, String bookmarkType, String review) {
	    System.out.println("\nPosting a bookmark review ...");
	    System.out.println("bookmarkId: " + bookmarkId);
	    System.out.println("bookmarkType: " + bookmarkType);
		System.out.println("review: " + review);
		System.out.println("Approved?: NO");
	}
}
