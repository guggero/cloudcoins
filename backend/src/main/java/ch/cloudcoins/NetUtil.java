package ch.cloudcoins;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class NetUtil {

    public static String sendPOST(String url, Map<String, String> wwwFormParams) throws Exception {
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Java8");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        StringBuilder params = new StringBuilder();
        for (String paramName : wwwFormParams.keySet()) {
            params.append("&")
                    .append(paramName)
                    .append("=")
                    .append(URLEncoder.encode(wwwFormParams.get(paramName), "UTF-8"));
        }
        if (params.length() == 0) {
            params.append("&");
        }

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(params.toString().substring(1));
        wr.flush();
        wr.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
