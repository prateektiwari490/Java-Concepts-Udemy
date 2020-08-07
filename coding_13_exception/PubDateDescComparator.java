package coding_13_exception;

import java.util.Comparator;

public class PubDateDescComparator implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		if(((Book)o2).getYear()-((Book)o1).getYear() == 0) {
			return ((Book)o1).getTitle().compareTo(((Book)o2).getTitle());
		}
		return ((Book)o2).getYear()-((Book)o1).getYear();
	}
}
