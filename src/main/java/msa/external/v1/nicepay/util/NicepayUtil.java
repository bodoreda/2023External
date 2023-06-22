package msa.external.v1.nicepay.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Collectors;

public class NicepayUtil {
    //server to server 통신
    public static String connectToServer(String data, String reqUrl) throws Exception{
        HttpURLConnection conn 		= null;
        BufferedReader resultReader = null;
        PrintWriter pw 				= null;
        URL url 					= null;

        int statusCode = 0;
        StringBuffer recvBuffer = new StringBuffer();
        try{
            url = new URL(reqUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(5000);
            conn.setDoOutput(true);

            pw = new PrintWriter(conn.getOutputStream());
            pw.write(data);
            pw.flush();

            statusCode = conn.getResponseCode();
            resultReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            for(String temp; (temp = resultReader.readLine()) != null;){
                recvBuffer.append(temp).append("\n");
            }

            if(!(statusCode == HttpURLConnection.HTTP_OK)){
                throw new Exception();
            }

            return recvBuffer.toString().trim();
        }catch (Exception e){
            return "9999";
        }finally{
            recvBuffer.setLength(0);

            try{
                if(resultReader != null){
                    resultReader.close();
                }
            }catch(Exception ex){
                resultReader = null;
            }

            try{
                if(pw != null) {
                    pw.close();
                }
            }catch(Exception ex){
                pw = null;
            }

            try{
                if(conn != null) {
                    conn.disconnect();
                }
            }catch(Exception ex){
                conn = null;
            }
        }
    }

    //JSON String -> HashMap 변환
    public static HashMap jsonStringToHashMap(String str) throws Exception{
        HashMap dataMap = new HashMap();
        JSONParser parser = new JSONParser();
        try{
            Object obj = parser.parse(str);
            JSONObject jsonObject = (JSONObject)obj;

            Iterator<String> keyStr = jsonObject.keySet().iterator();
            while(keyStr.hasNext()){
                String key = keyStr.next();
                Object value = jsonObject.get(key);

                dataMap.put(key, value);
            }
        }catch(Exception e){

        }
        return dataMap;
    }

    public static String makeOrderNumber(final String sequence) {
        return calculateDateFormat(0L, "yyMMddHHmmss") + sequence;
    }

    public static String calculateDateFormat(final long interval, final String format) {
        LocalDateTime dateTime = null;
        if (interval > 0) {
            dateTime = LocalDateTime.now().plusDays(interval);
        } else if (interval < 0) {
            dateTime = LocalDateTime.now().minusDays(Math.abs(interval));
        } else {
            dateTime = LocalDateTime.now();
        }

        return dateTime.format(DateTimeFormatter.ofPattern(format));
    }

    public static final synchronized String getyyyyMMddHHmmss(){
        SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
        return yyyyMMddHHmmss.format(new Date());
    }
}
