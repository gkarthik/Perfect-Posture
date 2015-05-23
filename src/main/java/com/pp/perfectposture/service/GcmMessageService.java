package com.pp.perfectposture.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class GcmMessageService {
	private final String USER_AGENT = "Mozilla/5.0";
	
	private void sendPost() throws Exception {
		String url = "https://android.googleapis.com/gcm/send";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
 
		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		con.setRequestProperty("Authorization", " key=AIzaSyD6_vTT615RGHGxZrGMTGmHC_ExDSfOCGI");
 
		String urlParameters = "registration_id=APA91bHYBMYikVyIvTnhVYcNQxnKAoqpFTr5ciWx2yMVD_KD91i8AXd1OiyxwToLcl_xxspDW1pq4S-K3aS6B0XGLZR4kOQcCnKNLrAgu0Po30Fp03nagAo8WQGPcLtu1kzjDd6_quGhzJK0fIt_dD9siziY_WHduQ&message=helloworld";
 
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
 
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		//print result
		System.out.println(response.toString());
 
	}
	
	public static void main(String[] args){
		GcmMessageService ser = new GcmMessageService();
		try {
			ser.sendPost();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
