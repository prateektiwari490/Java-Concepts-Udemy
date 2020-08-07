package exceptions.assertions.p1;

public class C {
	
	public void test(int i) {
		assert i > 0 : "invalid i in C.test";
	}
}
