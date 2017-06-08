package org.jenkinsci.plugins.test1;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HttpPostExample {
	public void sendPostReq() throws IOException{
		URL url = new URL("https://importresults.qmetry.com/prod/importresults-qtm4j ");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		connection.setDoInput(true);
		connection.setDoOutput(true);

		StringBuilder jsonBody = new StringBuilder("{");
		jsonBody.append("\"format\":" + "\"testng/xml\",");
		jsonBody.append("\"testRunName\":" + "\"Test Run\",");
		jsonBody.append("\"apiKey\":" + "\"541683b1b6f906a9b582c202ec766e38d914312a48e060adff5f018b2a457acd\"");
		jsonBody.append("}");

		OutputStream os = connection.getOutputStream();
		os.write(jsonBody.toString().getBytes("UTF-8"));
		InputStream fis = connection.getInputStream();

		StringWriter response = new StringWriter();
		String encoding = "UTF-8";
		IOUtils.copy(fis, response, encoding);
		 System.out.println(response.toString());
    }
    public static void main(String[] args) {
    	 System.out.println("Happening - CALL");
    	}
    
    
}
