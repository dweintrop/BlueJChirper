package org.bluej.delta.client.shipper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.bluej.delta.util.Debug;
import org.bluej.delta.util.Pair;

import sun.net.www.protocol.http.HttpURLConnection;

public class ChirpHTMLShipper implements Shipper {

	private static URL url;
	private static String studentName;
	private static String studentID;
	
	public void initialise(String address, String studentName, String studentID) {
		this.studentName = studentName;
		this.studentID = studentID;
		initialise(address);
	}
	
	@Override
	public void initialise(String address) {
		
		try {
			url = new URL(address);
		} catch (MalformedURLException e) {
			System.out.println("Chirp HTML shipper initialization failed with:" + address);
			e.printStackTrace();
		}
		System.out.println("Chirp HTML shipper initialised with:" + address);
	}

	@Override
	public void ship(Packet p) {
		try {
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		
		params.put("STUDENT_NAME", studentName);
		params.put("STUDENT_ID", studentID);
		
        for (Iterator iter = p.getData().iterator(); iter.hasNext();) {
            Pair pair = (Pair) iter.next();
            String key = pair.getKey();
            Object value = pair.getValue();
            params.put(key, value);            
        }

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            
				postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
			
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");
        
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setInstanceFollowRedirects(false); 
        conn.setRequestProperty("charset", "utf-8");
        conn.setUseCaches (false);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream ());
        wr.write(postDataBytes);
        wr.flush();
        wr.close();
        conn.disconnect();
        
        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        for (int c; (c = in.read()) >= 0; System.out.print((char)c));
		
		} catch (Exception e) {
			System.out.println("Chirp HTML shipper post failed!");
			e.printStackTrace();
		}
	}

}