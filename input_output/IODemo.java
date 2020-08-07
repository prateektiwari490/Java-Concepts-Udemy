package input_output;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class IODemo {
	static String inFileStr = "walden.jpg"; // there must be file in this case
	// there is no file named walden...
	static String outFileStr = "walden-out.jpg";

	private static void fileCopyNoBuffer() {
		System.out.println("Inside fileCopyNoBuffer : ");
		long startTime, elapsedTime; // for speed benchmarking

		// print file length
		File fileIn = new File(inFileStr);
		System.out.println("File size is : " + fileIn.length() + " bytes");

		try (FileInputStream in = new FileInputStream(inFileStr);
				FileOutputStream out = new FileOutputStream(outFileStr)) {
			startTime = System.nanoTime();
			int byteRead;
			// read a raw byte, return an int of 0 to 255
			while ((byteRead = in.read()) != -1) {
				// write the least significance byte of int, drop the upper 3
				// bytes

				out.write(byteRead);
			}
			elapsedTime = System.nanoTime() - startTime;
			System.out.println(
					"elapsed time is : " + (elapsedTime / 1000000.0) + " msec");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void fileCopyWithBufferAndArray() {
		System.out.println("Inside fileCopyWithBufferAndArray ...");

		long startTime, elapsedTime;
		startTime = System.nanoTime();
		try (BufferedInputStream in = new BufferedInputStream(
				new FileInputStream(inFileStr));
				BufferedOutputStream out = new BufferedOutputStream(
						new FileOutputStream(outFileStr))) {
			byte[] byteBuf = new byte[4000];
			int numByteRead;
			while ((numByteRead = in.read(byteBuf)) != -1) {
				out.write(byteBuf, 0, numByteRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		elapsedTime = System.nanoTime();
		System.out.println(
				"elapsed time is : " + (elapsedTime / 1000000.0) + " msec");
	}

	public static void applyEncoding() {
		System.out.println("\nInside applyEncoding ...");
		// System.out.println("Default Character Encoding: " +
		// System.getProperty("file.encoding"));

		// Ensure Eclipse property is set as UTF8
		printEncodingDetails("luke");
		printEncodingDetails("�"); // Euro (Reference:
		// http://stackoverflow.com/questions/34922333/how-does-java-fit-a-3-byte-unicode-character-into-a-char-type)
		printEncodingDetails("\u1F602"); // Non-BMP Unicode Code Point ~ Tears
		// of Joy Emoji (one of Smiley
		// graphic symbol)
	}
	private static void printEncodingDetails(String symbol) {
		System.out.println("\nSymbol: " + symbol);
		try {
			System.out.println(
					"ASCII: " + Arrays.toString(symbol.getBytes("US-ASCII")));
			System.out.println("ISO-8859-1: "
					+ Arrays.toString(symbol.getBytes("ISO-8859-1")));
			System.out.println(
					"UTF-8: " + Arrays.toString(symbol.getBytes("UTF-8")));
			System.out.println(
					"UTF-16: " + Arrays.toString(symbol.getBytes("UTF-16")));
			System.out.println("UTF-16 BE: "
					+ Arrays.toString(symbol.getBytes("UTF-16BE")));
			System.out.println("UTF-16 LE: "
					+ Arrays.toString(symbol.getBytes("UTF-16LE")));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
	}

	private static void readFromStandardInput() {
		System.out.println("Input readFromStandardInput : ");
		String data;

		System.out
		.println("Enter \"start\" to continue (Using BufferedReader):");

		try (BufferedReader in = new BufferedReader(
				new InputStreamReader(System.in, "UTF-8"))) {
			while ((data = in.readLine()) != null && !data.equals("start")) {
				System.out.println("Do not enter \"start\". Try again...");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Correct");

		// Alternative of BufferedReader is Scanner class which is present in
		// java.util.Scanner

		// System.out.println("Enter \"start\" to continue using
		// (java.util.Scanner)...");
		// Scanner sc = new Scanner(System.in);
		//
		// while(!(data = sc.nextLine()).equals("start")) {
		// System.out.println("Do not enter \"start\". Try again...");
		// }
		// sc.close();
		// System.out.println("Correct");

		/**
		 * Scanner ~ a text scanner for parsing primitives & string ~ breaks its
		 * input into tokens using a delimited pattern (default: whitespace) ~
		 * when System.in is used, internally constructor uses an
		 * InputStreamReader to read from it ~ hasXXX & nextXXX can be used
		 * together ~ InputMismatchException is thrown ~ From Java 5 onwards
		 */

		Scanner s1 = new Scanner("Hello, How are you?");
		while (s1.hasNext()) {
			System.out.println(s1.next());
		}
		s1.close();
	}

	private static void fileMethodDemo() {
		System.out.println("Inside fileMethodDemo...");
		File f = new File(
				"D:\\eclipse-workspace\\Java_Udemy\\src\\..\\walden.jpg");
		// File f = new File("walden.jpg");
		System.out.println("getAbsolutePath() : " + f.getAbsolutePath());

		try {
			System.out.println("getCanonicalPath() : " + f.getCanonicalPath());
			System.out.println("createNewFile() : " + f.createNewFile());
		} catch (IOException e) {}

		System.out.println("separator : " + f.separator);
		System.out.println("separatorChar : " + f.separatorChar);
		System.out.println("getParent() : " + f.getParent());
		System.out.println("lastModified() : " + f.lastModified());
		System.out.println("exists() : " + f.exists());
		System.out.println("isFile() : " + f.isFile());
		System.out.println("isDirectory() : " + f.isDirectory());
		System.out.println("length() : " + f.length());

		System.out.println();
		System.out.println("My working or user directory: "
				+ System.getProperty("user.dir"));
		System.out.println("new File(\"testdir\").mkdir(): "
				+ new File("testdir").mkdir());
		System.out.println("new File(\"testdir\\test\").mkdir(): "
				+ new File("testdir\\test").mkdir());
		System.out.println("new File(\"testdir\").delete(): "
				+ new File("testdir").delete());
		System.out.println("new File(\"testdir\\test1\\test2\").mkdir(): "
				+ new File("testdir\\test1\\test2").mkdir());
		System.out.println("new File(\"testdir\\test1\\test2\").mkdirs(): "
				+ new File("testdir\\test1\\test2").mkdirs());

		try {
			File f2 = new File("temp.txt");
			System.out.println("f2.createNewFile(): " + f2.createNewFile());
			System.out.println("f2.renameTo(...): "
					+ f2.renameTo(new File("testdir\\temp1.txt"))); // move!!
		} catch (IOException e) {
		}

	}

	private static void dirFilter(boolean applyFilter) {
		System.out.println("\nInside dirFilter ...");

		File path = new File(".");
		String[] list;

		if (!applyFilter)
			list = path.list();
		else
			list = path.list(new DirFilter());

		// Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);
		for (String dirItem : list)
			System.out.println(dirItem);
	}

	public static void main(String[] args) {
		// applyEncoding();
		// fileCopyNoBuffer();
		// fileCopyWithBufferAndArray();

		// readFromStandardInput();

		fileMethodDemo();
		dirFilter(false);

		// System.out.println(System.getProperty("file.encoding")); // Or
		// System.out.println(Charset.defaultCharset());
	}
}

class DirFilter implements FilenameFilter {
	// Holds filtering criteria
	public boolean accept(File file, String name) {
		return name.endsWith(".jpg") || name.endsWith(".JPG");
	}
}