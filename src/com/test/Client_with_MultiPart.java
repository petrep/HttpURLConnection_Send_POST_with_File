package com.test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

public class Client_with_MultiPart {

	public void StartMultipart() throws MalformedURLException, IOException {
	String url = "https://ptsv2.com/t/ro3lj-1633361921/post";
	String charset = "UTF-8";
	String param = "value";
	File textFile = new File("/home/peterpetrekanics/Pictures/test_pictures/screenshot.png");
	File binaryFile = new File("/home/peterpetrekanics/Pictures/test_pictures/screenshot.png");
	String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
	String CRLF = "\r\n"; // Line separator required by multipart/form-data.

	URLConnection connection = new URL(url).openConnection();
	connection.setDoOutput(true);
	connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

	try (
	    OutputStream output = connection.getOutputStream();
	    PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
	) {
	    // Send normal param.
	    writer.append("--" + boundary).append(CRLF);
	    writer.append("Content-Disposition: form-data; name=\"param\"").append(CRLF);
	    writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
	    writer.append(CRLF).append(param).append(CRLF).flush();

	    // Send text file.
	    writer.append("--" + boundary).append(CRLF);
	    writer.append("Content-Disposition: form-data; name=\"textFile\"; filename=\"" + textFile.getName() + "\"").append(CRLF);
	    writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF); // Text file itself must be saved in this charset!
	    writer.append(CRLF).flush();
	    Files.copy(textFile.toPath(), output);
	    output.flush(); // Important before continuing with writer!
	    writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.

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

	// Request is lazily fired whenever you need to obtain information about response.
	int responseCode = ((HttpURLConnection) connection).getResponseCode();
	System.out.println(responseCode); // Should be 200

}
}
