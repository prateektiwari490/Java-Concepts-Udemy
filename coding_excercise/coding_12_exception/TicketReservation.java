package coding_excercise.coding_12_exception;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

public class TicketReservation {

	private static final int CONFIRMEDLIST_LIMIT = 10;
	private static final int WAITINGLIST_LIMIT = 3;

	private List<Passenger> confirmedList = new ArrayList<>();
	private Deque<Passenger> waitingList = new ArrayDeque<>();

	// This getter is used only by the junit test case.
	public List<Passenger> getConfirmedList() {
		return confirmedList;
	}

	/**
	 * Returns true if passenger could be added into either confirmedList or
	 * waitingList. Passenger will be added to waitingList only if confirmedList
	 * is full.
	 * 
	 * Return false if both passengerList & waitingList are full
	 */
	public boolean bookFlight(String firstName, String lastName, int age, String gender, String travelClass, String confirmationNumber) {
        System.out.println("confirmationNumber : " + confirmationNumber);
        System.out.println("firstName : " + firstName.toString());
        System.out.println("lastName : " + lastName);
        System.out.println("age : " + age);
        System.out.println("gender : " + gender);
        System.out.println("travelClass : " + travelClass);
        int length = confirmationNumber.length();
        String str = confirmationNumber.substring(1, length);
        System.out.println("Integer.parseInt(str) : " + Integer.parseInt(str));
        if (Integer.parseInt(str) > (CONFIRMEDLIST_LIMIT
				+ WAITINGLIST_LIMIT)) {
				    System.out.println("bookFlight beyond limit");
			return false;
		} else if (Integer.parseInt(str) <= CONFIRMEDLIST_LIMIT) {
				    System.out.println("bookFlight confirmed list");
			confirmedList.add(new Passenger(firstName, lastName, age, gender,
					travelClass, confirmationNumber));
		} else {
		    System.out.println("bookFlight waiting list");
			waitingList.add(new Passenger(firstName, lastName, age, gender,
					travelClass, confirmationNumber));
		}
		return true;

   }

   /**      
    * Removes passenger with the given confirmationNumber. Passenger could be      
    * in either confirmedList or waitingList. The implementation to remove the      
    * passenger should go in removePassenger() method and removePassenger method      
    * will be tested separately by the uploaded test scripts.      
    
    * If passenger is in confirmedList, then after removing that passenger, the      
    * passenger at the front of waitingList (if not empty) must be moved into      
    * passengerList. Use poll() method (not remove()) to extract the passenger      
    * from waitingList.      
    */     
   public boolean cancel(String confirmationNumber) {
	   int length = confirmationNumber.length();
       String str = confirmationNumber.substring(1, length);
       if(Integer.parseInt(str)<=CONFIRMEDLIST_LIMIT) {
    	   Passenger p = waitingList.poll();
    	   Passenger con = confirmedList.get(Integer.parseInt(str)-1);
    	   
           System.out.println("waitingList.poll() : " + p + " con : " + con);
//           if(!waitingList.isEmpty()) {
//        	   confirmedList.remove(Integer.parseInt(str));
//        	   confirmedList.set(Integer.parseInt(str),p);   
//           }
//        	
//    			confirmedList.add(waitingList.poll());
			System.out.println("cancel confirm");
		}
		else if(Integer.parseInt(str)<=(CONFIRMEDLIST_LIMIT+WAITINGLIST_LIMIT)) {
			waitingList.poll();
			System.out.println("cancel waiting");
		}
		else {
		    System.out.println("cancel beyond limit");
			return false;
		}
		return true;

   }

   /**      
    * Removes passenger with the given confirmation number.      
    * Returns true only if passenger was present and removed. Otherwise, return false.      
    */     
   public boolean removePassenger(Iterator<Passenger> iterator, String confirmationNumber) {   while(iterator.hasNext()) {
	   int length = confirmationNumber.length();
       String str = confirmationNumber.substring(1, length);
       System.out.println("iterator.next().getConfirmationNumber() : " + iterator.next().getConfirmationNumber());
			if(iterator.next().getConfirmationNumber() == confirmationNumber) {
				if(Integer.parseInt(str) <= CONFIRMEDLIST_LIMIT) {
					confirmedList.remove(Integer.parseInt(str));
					System.out.println("removePassenger confirm");
					return true;
				}
				if(Integer.parseInt(str)<=(CONFIRMEDLIST_LIMIT+WAITINGLIST_LIMIT)) {
				    System.out.println("removePassenger waitingList");
					waitingList.poll();
					return true;
				}
			}
		}
		return false;
   }
//	String firstName, String lastName, int age,
//	String gender, String travelClass, String confirmationNumber
	public static void main(String[] args) {
		TicketReservation tr = new TicketReservation();
		System.out.println("bookFlight : " + tr.bookFlight("f1","l1",21,"m","economy","c1"));
		System.out.println("cancel : " + tr.cancel("c1"));
		System.out.println("bookFlight : " + tr.bookFlight("f1","l1",21,"m","economy","c12"));
		System.out.println("cancel : " + tr.cancel("c12"));
	}

}