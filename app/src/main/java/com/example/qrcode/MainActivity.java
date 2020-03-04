package com.example.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

//import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements OnFinishListener{

    String scannedData,link,sheeturl;
    final Activity activity = this;
    EditText edtURL,edtShettID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle("ĐIỂM DANH");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);






    }



    @Override
    public void OnFinishDialog(String object, String sheet) {
        link =object;
        sheeturl=sheet;
    }
//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null) {
            scannedData = result.getContents();


            if (scannedData != null) {
                new SendRequest().execute();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Camera();
                    }
                }, 5000);

            }
            else
            {
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public class SendRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){ }

        protected String doInBackground(String... arg0) {

            try{

                //Enter script URL Here
                URL url = new URL("https://script.google.com/macros/s/AKfycbyy4sWwHIGYCXzu9ChT_vAGGFnbqLeSc__vBPofk4M2MU9A1Rs/exec");

                JSONObject postDataParams = new JSONObject();

                //Passing scanned code as parameter
                postDataParams.put("sdataurl",link);
                postDataParams.put("sdata",scannedData);
                postDataParams.put("sdatasheet",sheeturl);
                Log.e("params",postDataParams.toString());





                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);
               // Write data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));
                writer.flush();
                writer.close();
                os.close();



                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }

            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mnuCamera:
                Camera();
                break;
            case R.id.mnuSettings:
                Setting();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    public void Setting(){
        DialogCustomFragment dialogCustomFragment = DialogCustomFragment.newInstance(link,sheeturl);
        dialogCustomFragment.show(getSupportFragmentManager(),"");


    }
    public void Camera(){
                    IntentIntegrator integrator = new IntentIntegrator(activity);
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                    integrator.setPrompt("ĐẠI HỘI ĐẠI BIỂU ĐOÀN TNCS HỒ CHÍ MINH KHOA CÔNG NGHỆ THÔNG TIN NHIỆM KỲ 2019 - 2020");

                    integrator.setBeepEnabled(true);
                    integrator.setCameraId(0);
                    integrator.setBarcodeImageEnabled(true);
                    integrator.initiateScan();

    }
}
