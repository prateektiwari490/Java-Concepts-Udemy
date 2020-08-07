package coding_excercise.coding_11_exception;

public class MissingGradeException extends Exception {
	private int studentIdList;

	public MissingGradeException(int studentIdList) {
		super();
		this.studentIdList = studentIdList;
	}

	public int getStudentIdList() {
		return studentIdList;
	}

}
