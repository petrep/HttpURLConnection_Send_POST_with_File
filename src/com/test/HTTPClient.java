package com.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class HTTPClient {

	public static void main(String[] args) throws UnsupportedEncodingException, IOException {
		System.out.println("HTTPClient starts..");
		
		System.out.println("Starting ClientWithPostAndGet");
		ClientWithPostAndGet myClient1 = new ClientWithPostAndGet();
		myClient1.Client();
		
		
		
		System.out.println("Starting Client_with_MultiPart");


	}

}
