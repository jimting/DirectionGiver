package jt.directiongiver000;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by lp123 on 2017/8/21.
 */

public class functionList
{
    public static String getWeather(String location,String date) throws IOException {
        String result = null;
        String weatherUrl = "http://140.121.197.130:8100/DG/GetWeather?location="+java.net.URLEncoder.encode(location, "UTF-8") +"&date="+date;
        Connection con = Jsoup.connect(weatherUrl).timeout(10000);
        Connection.Response resp = con.execute();
        Document doc = null;
        if (resp.statusCode() == 200)
        {
            doc = con.get();
        }
        result = doc.select("body").html().toString();
        System.out.println(result);
        return result;
    }

    public static Toilet[] getToilet(double userX,double userY) throws IOException {
                String toiletUrl = "http://140.121.197.130:8100/NearByServlet/GetWCServlet?longitude=" + userX + "&latitude=" + userY;
                Connection con = Jsoup.connect(toiletUrl).timeout(10000);
                Connection.Response resp = con.execute();
                Document doc = null;
                if (resp.statusCode() == 200) {
                    doc = con.get();
                }
                String result = doc.select("body").html().toString();

                Toilet[] tempToilet = null;
                try {
                    JSONArray jsonTotal = new JSONArray(result);
                    tempToilet = new Toilet[jsonTotal.length()];
                    if (jsonTotal.length() > 0)
                    {
                        for (int top = 0; top < jsonTotal.length(); top++)
                        {
                            JSONObject selectToilet = jsonTotal.getJSONObject(top);
                            String name = selectToilet.getString("NAME");
                            LatLng position = new LatLng(selectToilet.getDouble("PY"), selectToilet.getDouble("PX"));
                            String address = selectToilet.getString("ADDRESS");
                            String grade = selectToilet.getString("GRADE");
                            tempToilet[top] = new Toilet(name, position, address, grade);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(result);
                return tempToilet;
    }
}
