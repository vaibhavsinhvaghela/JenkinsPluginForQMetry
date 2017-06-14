package org.jenkinsci.plugins.test1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;



@SuppressWarnings("deprecation")
public class UploadToServer {
	
	String charset = "UTF-8";
	
	public void uploadToTheServer(String apikeyserver, String jiraurlserver,
    		String password, String testrunnameserver, String labelsserver, String sprintserver, String versionserver, 
    		String componentserver, String username, String fileserver, String selectionserver) throws IOException{
		
					String toEncode=username+":"+password;
			    	byte[] encodedBytes = Base64.getEncoder().encode(toEncode.getBytes());
			    	String encodedString= new String(encodedBytes);
			    	String basicAuth = "Basic " + encodedString;
		
			    	CloseableHttpClient httpClient = HttpClients.createDefault();
			    	HttpPost uploadFile = new HttpPost(jiraurlserver);
			    	uploadFile.addHeader("Authorization", basicAuth);
			    	
			    	MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			    	builder.addTextBody("apiKey", apikeyserver, ContentType.TEXT_PLAIN);
			    	builder.addTextBody("testRunName", testrunnameserver, ContentType.TEXT_PLAIN);
			    	builder.addTextBody("format", selectionserver, ContentType.TEXT_PLAIN);	

			    	// This attaches the file to the POST:
			    	File f = new File(fileserver);
			    	builder.addBinaryBody(
			    	    "file",
			    	    new FileInputStream(f),
			    	    ContentType.APPLICATION_OCTET_STREAM,
			    	    f.getName()
			    	);

			    	HttpEntity multipart = builder.build();
			    	uploadFile.setEntity(multipart);
			    	CloseableHttpResponse response = httpClient.execute(uploadFile);
			    	HttpEntity responseEntity = response.getEntity();
			    	//Execute and get the response.
			    	System.out.println(response.toString());
			    	httpClient.close();

			    	System.out.println("TAG HEREEEEEEEEE-------------------------------------------------------------------------");
				 		  
				    System.out.println("Username "+ ":" + username);
				    System.out.println("Password "+ ":" + password);
				    System.out.println("Basic AUTH "+ ":" + basicAuth);
				    System.out.println("format "+ ":" + selectionserver);
				    System.out.println("API Key "+ ":" + apikeyserver);
				    System.out.println("file "+ ":" + fileserver);
				    System.out.println("URL "+ ":" + jiraurlserver);			
	}
}
