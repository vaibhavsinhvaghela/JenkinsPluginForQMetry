package org.jenkinsci.plugins.test1;

import org.apache.commons.io.IOUtils;
import javax.net.ssl.HttpsURLConnection;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URL;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class S3BucketReportUploader {
	public static String uploadToS3(String response,String fileurl) throws ParseException, IOException{
		 JSONParser parser = new JSONParser();
		 Object obj = parser.parse(response.toString());
		 JSONObject jsonObject = (JSONObject) obj;
		 String urlForUpload = (String) jsonObject.get("url");
		 URL url = new URL(urlForUpload);
		 HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		 connection.setRequestMethod("PUT");
		 connection.setRequestProperty("Content-Type", "multipart/form-data");
		 connection.setDoOutput(true);
		 connection.setDoInput(true);

		 FileInputStream file = new FileInputStream(fileurl);

		 OutputStream os = connection.getOutputStream();
		 IOUtils.copy(file, os);

		 InputStream fis = connection.getInputStream();
		 StringWriter writer = new StringWriter();
		 String encoding = "UTF-8";
		 IOUtils.copy(fis, writer, encoding);
		 if (connection.getResponseCode() == 200) {
		 	System.out.println("Success-------------------------------------FILE UPLOADED");
		 }else{
		 	System.out.println(writer.toString());
		 }			 
		 return urlForUpload;
	}
}
