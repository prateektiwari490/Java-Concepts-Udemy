package input_output;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class DecodingWithCompatibleEncodingSheme {
	
	public static void encodingSync() {
		/*try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("encoding"), "UTF-16BE"))){
			System.out.println(br.readLine());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		try {
			System.out.println(new String("€".getBytes("UTF-8"), "UTF-8"));
			System.out.println(new String("€".getBytes("UTF-8"), "UTF-16BE"));
			System.out.println(new String("a".getBytes("US-ASCII"), "UTF-16BE"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		encodingSync();
	}

}
