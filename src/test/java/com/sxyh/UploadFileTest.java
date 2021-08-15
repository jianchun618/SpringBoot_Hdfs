package com.sxyh;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UploadFileTest {

	public static void main(String[] args) throws ClientProtocolException, IOException {
//		upload("http://localhost:8080/uploadToHdfs", "d:/zabbix_agentd.conf");
		upload("http://106.14.11.146:32003/uploadToHdfs", "d:/zabbix_agentd.conf");
	}
	
	private static void upload(String uploadUrl, String filePath) throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		CloseableHttpResponse response = null;
		try {
			HttpPost post = new HttpPost(uploadUrl);

			HttpEntity req = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
			        .addPart("file", new FileBody(new File(filePath))).build();
			
			post.setEntity(req);
			

			System.out.println("executing request: " + post.getRequestLine());
			response = httpclient.execute(post);

			HttpEntity re = response.getEntity();
			System.out.println(response.getStatusLine());
			if (re != null) {
				System.out.println("Response content: "
				        + new BufferedReader(new InputStreamReader(re.getContent())).readLine());
			}
			EntityUtils.consume(re);
		} finally {
			try {
				response.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
