package org.apache.nutch.collamine.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.net.URLEncoder;
import java.io.InputStreamReader;
 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.ContentType;

public class Middleware {
  private static final String COLLAMINE_DOWNLOAD_URL = "http://127.0.0.1:9001/download/html/";
  private static final String COLLAMINE_UPLOAD_URL = "http://127.0.0.1:9001/upload/html/multipart/";

  public Middleware() {}

  public String downloadFrmCollamine(String url) throws Exception {
    //HTTP GET request
    HttpClient client = HttpClientBuilder.create().build();
    HttpGet request = new HttpGet(COLLAMINE_DOWNLOAD_URL+URLEncoder.encode(url, "UTF-8"));
 
    HttpResponse response = client.execute(request);
 
    System.out.println("\nSending 'GET' request to URL : " + COLLAMINE_DOWNLOAD_URL + url);
    System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
 
    if (response.getStatusLine().getStatusCode() > 200) // Not OK
        return null;

    BufferedReader rd = new BufferedReader(
                       new InputStreamReader(response.getEntity().getContent()));

    StringBuffer body = new StringBuffer();
    String line = "";
    while ((line = rd.readLine()) != null)
      body.append(line);
    System.out.println(body.toString().equals("not found") ? "Not found in collamine ):" : "Hey, I've found something from collamine!");

    return body.toString().equals("not found") ? null : body.toString();
  }

  public void uploadToCollamine(String domain, String url, double crawlTime, String contributor, InputStream doc, String docName) throws Exception {
    // HTTP POST request
    HttpClient client = HttpClientBuilder.create().build();
    HttpPost post = new HttpPost(COLLAMINE_UPLOAD_URL);

    HttpEntity en = MultipartEntityBuilder.create()
                      .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                      .addTextBody("domain", domain, ContentType.TEXT_PLAIN)
                      .addTextBody("url", url, ContentType.TEXT_PLAIN)
                      .addTextBody("crawltime", String.valueOf(crawlTime), ContentType.TEXT_PLAIN)
                      .addTextBody("contributor", contributor, ContentType.TEXT_PLAIN)
                      .addBinaryBody("document", doc, ContentType.TEXT_HTML, docName).build();
    post.setEntity(en);
    HttpResponse response = client.execute(post);
    
    System.out.println("\nSending 'POST' request to URL : " + COLLAMINE_UPLOAD_URL);
    System.out.println("Post parameters : " + post.getEntity());
    System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
    System.out.println("Uploaded " + url + " to Collamine (:\n");
  }
}