package org.jenkinsci.plugins.test1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.jenkinsci.plugins.test1.TextExamplePublisher;

import org.apache.commons.io.IOUtils;

public class ConnectionToServer {
	TextExamplePublisher tpub;
	TextExamplePublisher.DescriptorImpl dimp;
	public static void main(String[] args) throws IOException{
		ConnectionToServer cs=new ConnectionToServer();
		cs.send();
	}
	void send() throws IOException{
		URL url = new URL("https://importresults.qmetry.com/prod/importresults-qtm4j ");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		connection.setDoInput(true);
		connection.setDoOutput(true);

		StringBuilder jsonBody = new StringBuilder("{");
		jsonBody.append("\"format\":" + "\""+tpub.getSelection()+"\",");
		jsonBody.append("\"testRunName\":" + "\""+tpub.getTestrunname()+"\",");
		jsonBody.append("\"apiKey\":" + "\""+tpub.getApikey()+"\"");
		jsonBody.append("}");

		OutputStream os = connection.getOutputStream();
		os.write(jsonBody.toString().getBytes("UTF-8"));
		InputStream fis = connection.getInputStream();

		StringWriter response = new StringWriter();
		String encoding = "UTF-8";
		IOUtils.copy(fis, response, encoding);
		 System.out.println(response.toString());
	}
}
