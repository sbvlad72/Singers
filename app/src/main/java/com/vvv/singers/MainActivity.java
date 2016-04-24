package com.vvv.singers;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends ActionBarActivity {

    final private String szUrl = "http://download.cdn.yandex.net/mobilization-2016/artists.json";
    private final ArrayList<SingerData> alSingers = new ArrayList<SingerData>(); //ArrayList для хранения данных об исполнителях
    private SingersAdapter adapter;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("VVV", "MainActivity onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // создаем ListView Для отображения списка исполнителей
        lv = (ListView) findViewById(R.id.listView);
        // создаем адаптер для ListView
        adapter = new SingersAdapter(MainActivity.this, R.id.listView, alSingers);
        //назначаем обработчик нажатий на элементы списка
        lv.setOnItemClickListener(itemClickListener);
        // инициализируем ImageView для отображения картинок
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));

        //запускаем AsyncTask для считывания данных из сети и заполнения структуры данных для ListView
        new ParseTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.clear_cache) {
            // удаление сохраненных в кэше данных запроса
            try {
                // проверяем состояние SD-карты
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    //получаем путь к кэшу приложения
                    String path = getExternalCacheDir().getPath();
                    //формируем хэш по полученному url
                    Integer iHash = szUrl.hashCode();
                    String fName = iHash.toString(); //преобразуем значение хэш в строку
                    File file = new File(path, fName);
                    if (file.exists()) { // проверим существование файла в кэше
                        file.delete();
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace(); // ошибка при работе с кэшем
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // обработчик нажатий на элемент списка
    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //получаем текущего исполнителя по позиции
            SingerData singer = (SingerData) parent.getItemAtPosition(position);

            //готовим intent Для отображения в DetailActivity
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            //поля для отображения в DetailActivity
            intent.putExtra("com.vvv.singers.TITLE", singer.getName());
            intent.putExtra("com.vvv.singers.GENRES", singer.getGenres());
            intent.putExtra("com.vvv.singers.CREATION", singer.getCreation());
            intent.putExtra("com.vvv.singers.IMAGE", singer.getCoverBig());
            intent.putExtra("com.vvv.singers.DESCRIPTION", singer.getDescription());
            //создание DetailActivity
            startActivity(intent);
            //включение анимации
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);

        }
    };

    //класс для разбора данных из сети в асинхронном режиме
    private class ParseTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            // получаем данные с внешнего ресурса
            //Log.d(TAG, "получаем данные с внешнего ресурса");
            try {
                //сначала пытаемся получить данные с вншнего кэша
                resultJson = GetFromCache(szUrl);
                if ( resultJson == null ) {
                    Log.d("CACHE", "из url: "+szUrl);
                    // из кэша не получили получаем из url
                    URL url = new URL(szUrl);

                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();

                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    resultJson = buffer.toString();

                    //сохраняем полученные данные во внешний кэш
                    SaveToCache(szUrl, resultJson);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            //Log.d(TAG, resultJson);
            return resultJson;
        }

        //собственно разбор JSON-данных
        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);

            JSONObject dataJsonObj = null;

            try {
                //создаем массив json-объектов
                JSONArray jsSingers = new JSONArray(strJson);
                // в цикле обрабатываем массив
                for (int i = 0; i < jsSingers.length(); i++) {
                    HashMap<String, Object> hm = new HashMap<String, Object>();
                    //получим данные очередного исполнителя
                    JSONObject jsSinger = jsSingers.getJSONObject(i);

                    //создаем объект SingerData (данные об одном исполнителе) и добавляем его в ArrayList
                    alSingers.add(new SingerData(jsSinger));

                    //обнавляем ListView
                    lv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //проверка наличия картинки в кэше и возврат ссылки на файловую систему
        protected String GetFromCache(String pUrl){
            try {
                // проверяем состояние SD-карты
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    //получаем путь к кэшу приложения
                    String path = MainActivity.this.getExternalCacheDir().getPath();
                    //формируем хэш по полученному url
                    Integer iHash = pUrl.hashCode();
                    String fName = iHash.toString(); //преобразуем значение хэш в строку
                    File file = new File(path, fName);
                    if (file.exists()) { // проверим существование файла в кэше
                        Log.d("CACHE", "из кэша: "+path+fName);
                        //открываем файл для чтения
                        FileInputStream fIn = new FileInputStream(file);
                        StringBuffer buffer = new StringBuffer();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(fIn));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            buffer.append(line);
                        }

                        return buffer.toString();
                    } else {
                        return null;  // файла с кэш-данными не существует
                    }
                } else {
                    return null;  //нет доступа к внешнему кэшу
                }
            }
            catch (Exception e){
                return null; // ошибка при работе с кэшем
            }
        }

        //проверка наличия картинки в кэше и возврат ссылки на файловую систему
        protected void SaveToCache(String pUrl, String pJSONData){
            try {
                // проверяем состояние SD-карты
                if ( Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ){
                    //получаем путь к кэшу приложения
                    String path = MainActivity.this.getExternalCacheDir().getPath();
                    //формируем хэш по полученному url
                    Integer iHash = pUrl.hashCode();
                    String fName = iHash.toString(); //преобразуем значение хэш в строку
                    File file = new File(path, fName);
                    FileOutputStream fOut = new FileOutputStream(file);
                    OutputStreamWriter writer = new OutputStreamWriter(fOut);
                    writer.write(pJSONData);
                    writer.close();
                    Log.d("CACHE", "сохранили в кэш: " + path + fName);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

    }

}
