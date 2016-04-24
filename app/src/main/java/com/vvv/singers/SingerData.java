package com.vvv.singers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by vvv on 13.04.2016.
 */
public class SingerData {
    String id;
    String name;
    String[] genres;
    String tracks;
    String albums;
    String description;
    String link;
    String coverSmall;
    String coverBig;

    private static final String TAG = "SingersData";

    public SingerData(JSONObject jsSinger) {
        JSONArray jsGenres;
        ArrayList<String> alGenres = new ArrayList<String>();
        JSONObject jsCover;
        try {
            this.id = jsSinger.getString("id");
            this.name = jsSinger.getString("name");
            this.tracks = jsSinger.getString("tracks");
            this.albums = jsSinger.getString("albums");
            this.description = jsSinger.getString("description");
            //this.link = jsSinger.getString("link");
            //заполним массив жанры
            jsGenres = jsSinger.getJSONArray("genres");
            for (int i = 0; i < jsGenres.length(); i++){
                alGenres.add(jsGenres.getString(i)); // сначала читаем JSONArray в ArrayList
            }
            this.genres = alGenres.toArray(new String[alGenres.size()]); // преобразуем ArrayList в массив строк

            jsCover = jsSinger.getJSONObject("cover");
            this.coverSmall = jsCover.getString("small");
            this.coverBig = jsCover.getString("big");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName(){return this.name;}

    public String getGenres(){ // возвращаем жанры в виде строки
        String szRet = "";
        try {
            for (int i = 0; i < this.genres.length; i++) {
                szRet = szRet + ", " + this.genres[i];
            }
            return szRet.substring(2);
        }
        catch (Exception e){
            return "Жанры не определены";
        }
    }

    public String getCreation(){ // возвращаем альбомы и песни в виде одной строки для вывода
        String szTmp;
        char cLast;
        cLast = albums.charAt(albums.length() - 1);
        if (cLast == '1'){
            szTmp = albums + " альбом";
        }else if ((cLast == '2') ||(cLast == '3')||(cLast == '4')){
            szTmp = albums + " альбома";
        }else {
            szTmp = albums + " альбомов";
        }
        cLast = tracks.charAt(tracks.length() - 1);
        if (cLast == '1'){
            szTmp = szTmp + ", " + tracks + " песня";
        }else if ((cLast == '2') ||(cLast == '3')||(cLast == '4')){
            szTmp = szTmp + ", " + tracks + " песни";
        }else {
            szTmp = szTmp + ", " + tracks + " песен";
        }
        return szTmp;
    }
    public String getTracks(){return this.tracks;}

    public String getAlbums() {return albums;};

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public String getCoverSmall() {
        return coverSmall;
    }

    public String getCoverBig() {
        return coverBig;
    }
}
