package com.pp.perfectposture.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pp.perfectposture.domain.Sensor;
import com.pp.perfectposture.domain.SensorValue;

@Service
public class GcmMessageService {
	private final String USER_AGENT = "Mozilla/5.0";
    private final Logger log = LoggerFactory.getLogger(GcmMessageService.class);

	private void sendPost(String regId, int postureId) throws Exception {
		String url = "https://android.googleapis.com/gcm/send";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
 
		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		con.setRequestProperty("Authorization", " key=AIzaSyD6_vTT615RGHGxZrGMTGmHC_ExDSfOCGI");
 
		String urlParameters = "registration_id="+regId+"&posture="+postureId;
		log.debug("urlParamerter ", urlParameters);
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
 
		int responseCode = con.getResponseCode();
		log.debug("\nSending 'POST' request to URL : " + url);
		log.debug("Post parameters : " + urlParameters);
		log.debug("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		//print result
		log.debug(response.toString());
 
	}
	
	public void checkPosture(Sensor s){
		String regId = s.getUser().getGcmCredentials().getRegId();
		log.debug("registration id ", regId);
		//int postureId = getPostureId(values[values.length]);
		int postureId = 0;
		log.debug("Posture ", postureId);
		try {
			sendPost(regId, postureId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getPostureId(SensorValue s){
		int postureId = -1;
		Long sen1 = s.getSen1();
		Long sen2 = s.getSen2();
		Long sen3 = s.getSen3();
		Long sen4 = s.getSen4();
		if(sen1 == 0 && sen2 == 0 && sen3 == 1 && sen4 == 1){
			postureId = 0;
		} else if(sen1==1 && sen2==1 && sen3==0 && sen4==0) {
			postureId = 1;
		} else if((sen1 == 1 && sen3== 1 && sen2 == 0 && sen4 ==0) ||(sen1 == 0 && sen3== 0 && sen2 == 1 && sen4 ==1)) {
			postureId = 2;
		}
		return postureId;
	}
}
