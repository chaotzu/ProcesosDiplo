package com.example.alumno11.procesosdiplo.webservice;

import android.os.Build;

import com.example.alumno11.procesosdiplo.Video;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alumno11 on 24/02/18.
 */

public class JSONParser {

    private HttpURLConnection connection = null;

    public JSONParser() {


    }

    public Video getVideo(String uri, Petition petition){
        Video video = null;

        //http://www.omdbapi.com/?i=tt2975590&apikey=2b28d307&r=json

        try {

            JSONObject videoJsonObject = new JSONObject(getJSONString(uri, petition));

            if(videoJsonObject != null) {


                //String id, String title, String year, String type, String poster
                video = new Video();

                video.setId(videoJsonObject.getString("imdbID"));
                video.setTitle(videoJsonObject.getString("Title"));
                video.setYear(videoJsonObject.getString("Year"));
                video.setType(videoJsonObject.getString("Type"));
                video.setPoster(videoJsonObject.getString("Poster"));
                video.setDate(videoJsonObject.getString("Released"));
                video.setPlot(videoJsonObject.getString("Plot"));
                video.setDirector(videoJsonObject.getString("Director"));
                video.setEscritor(videoJsonObject.getString("Writer"));
                video.setActores(videoJsonObject.getString("Actors"));


                List<Map<String, String>> ratings = new ArrayList<>();

                JSONArray ratingsJsonArray = videoJsonObject.getJSONArray("Ratings");
                if (ratingsJsonArray != null && ratingsJsonArray.length() > 0) {

                    JSONObject ratingJSONObject = null;
                    Map<String,String> rating = null;
                    for (int i = 0; i < ratingsJsonArray.length(); i++) {

                        ratingJSONObject = ratingsJsonArray.getJSONObject(i);

                        rating = new HashMap<>();
                        rating.put("source", ratingJSONObject.getString("Source"));
                        rating.put("value", ratingJSONObject.getString("Value"));

                        ratings.add(rating);

                    }

                }

                video.setRatings(ratings);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return video;
    }

    public List<Video> getVideos(String uri, Petition petition) {
        List<Video> videos = new ArrayList<>();

        try {

            JSONObject jsonObject = new JSONObject(getJSONString(uri, petition));
            if (jsonObject != null) {
                JSONArray videosJsonArray = jsonObject.getJSONArray("Search");
                if (videosJsonArray != null && videosJsonArray.length() > 0) {
                    for (int indice = 0; indice < videosJsonArray.length(); indice++) {
                        JSONObject videoJsonObject = videosJsonArray.getJSONObject(indice);
                        videos.add(new Video(videoJsonObject.getString("imdbID"), videoJsonObject.getString("Title"), videoJsonObject.getString("Year"), videoJsonObject.getString("Type"), videoJsonObject.getString("Poster")));
                    }
                }
            }

        } catch (JSONException e) {

            e.printStackTrace();

        }

        return videos;
    }

    private String getJSONString (String petitionUri, Petition petition) {

        InputStream inputStream = null;
        String jsonResult = null;

        try {
            connection = createConnection(petitionUri,petition);
            if (petition.getEntity() != Entity.POST) {
                inputStream = new BufferedInputStream(connection.getInputStream());
                jsonResult = convertInputStreamToString(inputStream);
            } else {
                if (Build.VERSION.SDK_INT< Build.VERSION_CODES.LOLLIPOP) {
                    inputStream = new BufferedInputStream(connection.getInputStream());
                    jsonResult = convertInputStreamToStringPost(inputStream);
                } else {
                    inputStream = new BufferedInputStream(connection.getInputStream());
                    jsonResult = convertInputStreamToString(inputStream);
                }
            }
        } catch (Exception e) {
            return null;
        }

        return jsonResult;
    }

    public HttpURLConnection createConnection(String petitionUri, Petition petition) {

        HttpURLConnection httpURLConnection = null;
        DataOutputStream dataOutputStream = null;

        try {

            switch (petition.getEntity()) {
                case POST:
                    URL url = new URL(petitionUri);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setInstanceFollowRedirects(false);
                    httpURLConnection.setConnectTimeout(petition.getTimeConnection());
                    httpURLConnection.setReadTimeout(petition.getTimeConnection());
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setRequestProperty("Content-Type","applications/json");
                    httpURLConnection.setRequestProperty("Accept","applications/json");
                    httpURLConnection.setRequestProperty("charset","utf-8");
                    httpURLConnection.setRequestProperty("Accept-Encoding","");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setUseCaches(false);

                    dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                    String paramsJSON = petition.getParamsPost();
                    dataOutputStream.write(paramsJSON.getBytes("UTF-8"));
                    dataOutputStream.flush();
                    dataOutputStream.close();
                    break;
                case GET:
                    String uri = petitionUri;
                    if (petition.getParamsGet() != null) {
                        uri+=petition.getParamsGet();
                    }
                    URL urlGet = new URL(petitionUri);
                    httpURLConnection = (HttpURLConnection) urlGet.openConnection();
                    httpURLConnection.setInstanceFollowRedirects(false);
                    httpURLConnection.setConnectTimeout(petition.getTimeConnection());
                    httpURLConnection.setReadTimeout(petition.getTimeConnection());
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setRequestProperty("charset","utf-8");
                    httpURLConnection.setUseCaches(false);
                    break;

                case FRIENDLY:
                    String uriFriendly = petitionUri;
                    if (petition.getParamFriendly() != null) {
                        uriFriendly+=petition.getParamFriendly();
                    }
                    URL urlFriendly = new URL(petitionUri);
                    httpURLConnection = (HttpURLConnection) urlFriendly.openConnection();
                    httpURLConnection.setInstanceFollowRedirects(false);
                    httpURLConnection.setConnectTimeout(petition.getTimeConnection());
                    httpURLConnection.setReadTimeout(petition.getTimeConnection());
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setRequestProperty("charset","utf-8");
                    httpURLConnection.setUseCaches(false);
                    break;
                default:
                    URL urlNone = new URL(petitionUri);
                    httpURLConnection = (HttpURLConnection) urlNone.openConnection();
                    httpURLConnection.setInstanceFollowRedirects(false);
                    httpURLConnection.setConnectTimeout(petition.getTimeConnection());
                    httpURLConnection.setReadTimeout(petition.getTimeConnection());
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setRequestProperty("charset","utf-8");
                    httpURLConnection.setUseCaches(false);
                    break;





            }

        } catch (MalformedURLException e) {

            e.printStackTrace();
            return null;

        } catch (SocketTimeoutException f) {

            f.printStackTrace();
            return null;

        } catch (Exception g) {

            g.printStackTrace();
            return null;

        }

        return httpURLConnection;

    }

    public static String convertInputStreamToString(InputStream inputStream) {
        String line = new String();
        String result = new String();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);

            while ((line = bufferedReader.readLine()) != null)
                result += line;

            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }

    public static String convertInputStreamToStringPost(InputStream inputStream) {
        String line = null;
        StringBuffer output = new StringBuffer("");
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);

            line = "";
            while ((line = bufferedReader.readLine()) != null)
                output.append(line);

            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toString();

    }


}
