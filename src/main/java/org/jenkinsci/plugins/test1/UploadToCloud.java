package org.jenkinsci.plugins.test1;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;

public class UploadToCloud {
	
	public void UploadToTheCloud(String apikey, String qtm4jurl, String file, String testrunname, 
    		String labels, String sprint, String versions, String component, String selection){
	try {
		//con.sendingPostRequest();
		URL url = new URL(qtm4jurl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		connection.setDoInput(true);
		connection.setDoOutput(true);

		StringBuilder jsonBody = new StringBuilder("{");
		jsonBody.append("\"format\":" + "\""+selection+"\",");
		jsonBody.append("\"testRunName\":" + "\""+testrunname+"\",");
		jsonBody.append("\"apiKey\":" + "\""+apikey+"\"");
		/*jsonBody.append("\"labels\":" + "\""+labels+"\"");
		jsonBody.append("\"versions\":" + "\""+versions+"\"");
		jsonBody.append("\"sprint\":" + "\""+sprint+"\"");*/
		jsonBody.append("}");

		OutputStream os = connection.getOutputStream();
		os.write(jsonBody.toString().getBytes("UTF-8"));
		InputStream fis = connection.getInputStream();

		StringWriter response = new StringWriter();
		String encoding = "UTF-8";
		IOUtils.copy(fis, response, encoding);
		System.out.println(response.toString());
		System.out.println(S3BucketReportUploader.uploadToS3(response.toString(),file));   		 
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}  
	}
}
