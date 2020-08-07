// Master Java Exceptions with Best Practices
package exceptions;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TryWithResourcesDemo {
	static String inFileStr = "walden.jpg";
	static String outFileStr = "walden-out.jpg";
		
	public static void fileCopyWithArm() throws IOException {
		System.out.println("\nInside fileCopyWithArm ...");				
		
		try (Test t = new Test(); Test2 t2 = new Test2(); BufferedInputStream in = new BufferedInputStream(new FileInputStream(inFileStr));
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outFileStr))) {
			byte[] byteBuf = new byte[4000];
			int numBytesRead;
			while ((numBytesRead = in.read(byteBuf)) != -1) {
				out.write(byteBuf, 0, numBytesRead);
			}		
			
			throw new IOException("Important Exception!!");			
		}		
	}	
	
	public static void main(String[] args) {
		try {
			fileCopyWithArm();
		} catch (IOException e) {
			e.printStackTrace();
			
			/*
			Throwable[] throwables = e.getSuppressed();
			System.out.println(throwables[0].getMessage());
			System.out.println(throwables[1].getMessage());
			*/
		}
		
	}
}

class Test implements AutoCloseable {
	@Override
	public void close() throws IOException {
		throw new IOException("Trivial Exception");			
	}		
}

class Test2 implements AutoCloseable {
	@Override
	public void close() throws IOException {
		throw new IOException("Trivial Exception 2");			
	}		
}