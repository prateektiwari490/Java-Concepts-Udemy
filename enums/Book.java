package enums;

public class Book {
	public enum BookGenre {
		BIOGRAPHY(12) {
			public boolean isKidFriendly(int age) {
				return age >= minAgeToRead;
			}
		},
		HORROR(15) {
			public boolean isKidFriendly(int age) {
				return false;
			}
		};

		public abstract boolean isKidFriendly(int age);

		private BookGenre(int minAgeToRead) {
			this.minAgeToRead = minAgeToRead;
		}

		protected int minAgeToRead;
		public int getMinAgeToRead() {
			return minAgeToRead;
		}

		/*
		 * public boolean isKidFriendly(int age) { switch (this) { case
		 * BIOGRAPHY : return age >= minAgeToRead; case HORROR : return false; }
		 * return false; //throw new AssertionError("Unknown operation: " +
		 * this); }
		 */

	}

	public static void main(String[] args) {
		for (BookGenre bookGenre : BookGenre.values()) {
			System.out.print("\nName: " + bookGenre); // toString
			System.out.print(", name(): " + bookGenre.name());
			System.out.print(", Ordinal: " + bookGenre.ordinal());
			System.out.print(
					", Declaring Class: " + bookGenre.getDeclaringClass());
			System.out.print(", compareTo(HORROR): "
					+ bookGenre.compareTo(BookGenre.HORROR));
			System.out.print(
					", equals(HORROR): " + bookGenre.equals(BookGenre.HORROR));
			System.out.print(", minAgeToRead: " + bookGenre.getMinAgeToRead());
			System.out.print(", isKidFriendly: " + bookGenre.isKidFriendly(25));
		}
	}

}