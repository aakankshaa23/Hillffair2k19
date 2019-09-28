package com.appteam.hillfair2k19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.androidnetworking.AndroidNetworking;
import com.appteam.hillfair2k19.Wall.WallFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    IResult mResultCallback;
    IResult mResultCallbackAndroidNeworking;
    TextView text;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text=findViewById(R.id.text);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new WallFragment(MainActivity.this);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

            }
        });


        // For Volley Api Users

        //Initialising
        initVolleyCallback();
        VolleyService mVolleyService = new VolleyService(mResultCallback,this);

        //GET REQUEST SINGLE JSON OBJECT
//        mVolleyService.getJsonObjectDataVolley("GETCALL","https://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22");

//        GET REQUEST FOR JSON Array
        mVolleyService.getJsonArrayDataVolley("GETJSONARRAYLIFESAVER","https://lifesaverapp.herokuapp.com/controlpolice");

        // POST REQUEST JSON BODY
//        JSONObject jsonObject = new JSONObject();
//        try {
//    jsonObject.put("latitude","98.0");
//    jsonObject.put("longitude","98.0");
//    jsonObject.put("station","Austrailia");
//    jsonObject.put("mobile","12345678910");
//
//} catch (JSONException e) {
//            e.printStackTrace();
//        }
//        mVolleyService.postJsonDataVolley("POSTJSONDATALIFESAVER","https://lifesaverapp.herokuapp.com/controlpolice",jsonObject);




        //For Android Networking Users

        //Initialising
//        initAndroidNetworkingCallback();
//        AndroidNetworking.initialize(MainActivity.this);
//        com.appteam.hillfair2k19.AndroidNetworking androidNetworking = new com.appteam.hillfair2k19.AndroidNetworking(mResultCallbackAndroidNeworking,this);

//                 GET REQUEST SINGLE JSON OBJECT
//        androidNetworking.getJsonObjectAndroidNetworking("GETCALL","https://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22");


        //GET REQUEST FOR JSON ARRAY
//        androidNetworking.getJsonArrayAndroidNetworking("GETJSONARRAYFROMLIIFESAVER","https://lifesaverapp.herokuapp.com/controlpolice");


        //POST REQUEST A JSON OBJECT OR BODY
//        JSONObject jsonObject = new JSONObject();
//        try {
//    jsonObject.put("latitude","98.0");
//    jsonObject.put("longitude","98.0");
//    jsonObject.put("station","Hamirpur");
//    jsonObject.put("mobile","1234567890");
//
//} catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        androidNetworking.postJsondataAndroidNetworking("POSTINGJOSNBODYTOLIFESAVER","https://lifesaverapp.herokuapp.com/controlpolice",jsonObject);
    }

    void initVolleyCallback(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response , JSONArray jsonArray) {
                if (response != null)
                {
                    //JsonObject
                    Toast.makeText(MainActivity.this, String.valueOf(response), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //JsonArray
                    Toast.makeText(MainActivity.this, String.valueOf(jsonArray), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
            }
        };
    }
    void initAndroidNetworkingCallback(){
        mResultCallbackAndroidNeworking = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response , JSONArray jsonArray) {
                if (response != null)
                {
                    //JsonObject
                    Toast.makeText(MainActivity.this, String.valueOf(response), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //JsonArray
                    Toast.makeText(MainActivity.this, String.valueOf(jsonArray), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {

            }
        };
    }

}
