package nestedClasses;

import java.io.Serializable;
import java.util.Comparator;

public class Bookmark {
	private long id;
	private String title;
	private double rating;
	// strategy
	public static final Comparator<Bookmark> RATING_COMPARATOR = new RatingComparator();

	private static class RatingComparator
			implements
				Comparator<Bookmark>,
				Serializable {
		@Override
		public int compare(Bookmark o1, Bookmark o2) {
			// TODO Auto-generated method stub
			return o1.getRating() < o2.getRating() ? 1 : -1;
		}
	}

	// public helper class
	public static class ComparatorList {
		public static class RatingComparator
				implements
					Comparator<Bookmark>,
					Serializable {
			@Override
			public int compare(Bookmark o1, Bookmark o2) {
				// TODO Auto-generated method stub
				return o1.getRating() < o2.getRating() ? 1 : -1;
			}
		}

		public static class StringLengthComparator
				implements
					Comparator<Bookmark>,
					Serializable {

			@Override
			public int compare(Bookmark o1, Bookmark o2) {
				// TODO Auto-generated method stub
				return o1.getTitle().length() - o2.getTitle().length();
			}
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

}
