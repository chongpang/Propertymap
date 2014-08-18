package test.rjc.com.propertymap;

/**
 * Created by PangChong on 2014/08/15.
 */
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class AsyncHttpRequest extends  AsyncTask<Uri.Builder, Void, String> {
    private MapsActivity  mainActivity;

    public AsyncHttpRequest(MapsActivity activity) {

        // 呼び出し元のアクティビティ
        this.mainActivity = activity;
    }

    // このメソッドは必ずオーバーライドする必要があるよ
    // ここが非同期で処理される部分みたいたぶん。
    @Override
    protected String doInBackground(Uri.Builder... builder) {
        // httpリクエスト投げる処理を書く。
        HttpClient client = new DefaultHttpClient();
        HttpParams params = client.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 1000); //接続のタイムアウト
        HttpConnectionParams.setSoTimeout(params, 1000); //データ取得のタイムアウト
        String url = "http://192.168.1.48/index.php/getallprop";
        HttpGet get = new HttpGet(url);

        String str = "";
        try{
            HttpResponse response = client.execute(get);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpURLConnection.HTTP_OK){
                byte[] res = EntityUtils.toByteArray(response.getEntity());
                str = new String(res, "UTF-8");
            }
        }
        catch (Exception e) {
        }
        return str;
    }


    // このメソッドは非同期処理の終わった後に呼び出されます
    @Override
    protected void onPostExecute(String result) {
        // 取得した結果をテキストビューに入れちゃったり
        Log.d("TEST","Test: " + result.length());

        try{
            JSONArray obj = new JSONArray(result);
            //Log.d("TEST",obj.toString());

           // GEOcondeJSON geoJson = new GEOcondeJSON();
            //geoJson.parse(obj);

            for (int i = 0; i < obj.length(); i++) {
                JSONObject o = obj.getJSONObject(i);
                Log.d("111",o.toString());
                if(o.has("location")){
                    JSONObject loc = o.getJSONObject("location");
                    if(loc.length()>0){
                        this.mainActivity.moveToGeoJson(loc);
                    }
                }
            }


          }catch (JSONException e){
            Log.d("TEST",e.toString());
        }
    }
}