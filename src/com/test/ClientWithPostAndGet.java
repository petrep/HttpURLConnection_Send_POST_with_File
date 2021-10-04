package com.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.Map;

public class ClientWithPostAndGet {
	public static String request;
	public static URL url;
	public static HttpURLConnection conn;
	public static String boundary;
	public static String CRLF;
	public static String charset; 
	
	public static void Client() throws UnsupportedEncodingException, IOException {
	// request = "http://example.com:8082/subfolder/subsubfolder/result?";
//	request = "https://ptsv2.com/t/et4ro-1560440878/post";
	 request = "http://httpbin.org/post";
//	request = "http://example.com/index.php";
	boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
	CRLF = "\r\n"; // Line separator required by multipart/form-data.

	url = new URL(request);
	conn = (HttpURLConnection) url.openConnection();
	charset = "UTF-8";
	conn.setDoOutput(true);
	
//	sendGet();
	sendPost();


	// Sends the http request
	int status = conn.getResponseCode();
	String respMsg = conn.getResponseMessage();

	// Reads http response
	BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	String inputLine;
	StringBuilder content = new StringBuilder();
	while ((inputLine = in.readLine()) != null) {
		content.append(inputLine);
		System.out.println(inputLine);
	}
	in.close();
//	conn = null;
	// System.out.println("http status is: " + status);
	// System.out.println("content is: " + content);
	// System.out.println("resp msg: " + respMsg);
}

public static void sendPost() throws IOException {
	// Sending a POST request
	
	File binaryFile = new File(ClientWithPostAndGet.class.getResource("screenshot.png").getFile());
	conn.setRequestMethod("POST");
	conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

	try (
	    OutputStream output = conn.getOutputStream();
	    PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
	) {
	    // Send Multipart parameter
	    writer.append("--" + boundary).append(CRLF);
	    writer.append("Content-Disposition: form-data; name=\"myMultiPartName1\"").append(CRLF);
	    writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
	    writer.append(CRLF).append("myMultiPartValue1").append(CRLF).flush();

	    // Send binary file.
	    writer.append("--" + boundary).append(CRLF);
	    writer.append("Content-Disposition: form-data; name=\"binaryFile\"; filename=\"" + binaryFile.getName() + "\"").append(CRLF);
	    writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(binaryFile.getName())).append(CRLF);
	    writer.append("Content-Transfer-Encoding: binary").append(CRLF);
	    writer.append(CRLF).flush();
	    Files.copy(binaryFile.toPath(), output);
	    output.flush(); // Important before continuing with writer!
	    writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.

	    // End of multipart/form-data.
	    writer.append("--" + boundary + "--").append(CRLF).flush();
	}

	
}

public static void sendGet() throws IOException {
	// Sidenote: The url has to end with '?' if we want to send GET parameters
	
	
	
	// *** As soon as I uncomment the parameters, the method changes from GET to POST:
//	Map<String, String> parameters = new HashMap<>();
//	parameters.put("parameter1", "value1");		
//	DataOutputStream out = new DataOutputStream(conn.getOutputStream());
//	out.writeBytes(getParamsString(parameters));
//	out.flush();
//	out.close();
	
	conn.setRequestMethod("GET");
	conn.setReadTimeout(5000);		
//	conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
	conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
	conn.addRequestProperty("User-Agent", "Mozilla");
	conn.addRequestProperty("Referer", "google.com");
	conn.setConnectTimeout(5000);
}

//public static String getParamsString(Map<String, String> params) throws UnsupportedEncodingException {
//	StringBuilder result = new StringBuilder();
//
//	for (Map.Entry<String, String> entry : params.entrySet()) {
//		result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
//		result.append("=");
//		result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
//		result.append("&");
//	}

//	String resultString = result.toString();
//	 System.out.println("resultString: " + resultString);
//	return resultString.length() > 0 ? resultString.substring(0, resultString.length() - 1) : resultString;
//}
}
