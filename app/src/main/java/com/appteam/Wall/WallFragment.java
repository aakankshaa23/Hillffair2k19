package com.appteam.Wall;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.UploadRequest;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class WallFragment extends Fragment implements View.OnClickListener {
    private int GALLERY = 1, CAMERA = 2;
    public final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 10;
    Bitmap selectedImage = null;
    int openDialog = 1;
    private byte[] byteArray;
    private Bitmap bmp, img;
    private int PICK_PHOTO_CODE = 1046;

    public static ArrayList<String> imageArray = new ArrayList<>();
    public static ArrayList<String> likesArray = new ArrayList<>();
    public static ArrayList<Boolean> likedArray = new ArrayList<>();
    String user_id;
    String firebase_id;
    ProgressBar loadwall;
    int set = 0;
    private String base64b;
    String img_Url;
    SwipeRefreshLayout swiperefresh;
    private WallAdapter wallAdapter;

    private FloatingActionButton fab;
    private RecyclerView fifthRec;
    private List<wall> wallList = new ArrayList<>();
    private Activity activity;

    public WallFragment() {
    }

    public WallFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        AndroidNetworking.initialize(getActivity().getApplicationContext());
        View view = inflater.inflate(com.appteam.hillfair2k19.R.layout.fragment_wall, container, false);
        loadwall = view.findViewById(com.appteam.hillfair2k19.R.id.loadwall);
        fab = view.findViewById(com.appteam.hillfair2k19.R.id.fab);
        ((View) fab).setOnClickListener(this);
        SharedPreferences prefs = this.getActivity().getSharedPreferences("roll number", Context.MODE_PRIVATE);
        user_id = prefs.getString("name", "gsbs");
        fifthRec = view.findViewById(com.appteam.hillfair2k19.R.id.fifthRec);
        wallAdapter = new WallAdapter(wallList, activity);

        fifthRec.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
        fifthRec.setAdapter(wallAdapter);
        getData();
        swiperefresh = view.findViewById(com.appteam.hillfair2k19.R.id.swiperefresh);
        swiperefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getData();
                        swiperefresh.setRefreshing(false);
                        try {
                            TimeUnit.SECONDS.sleep(0);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

        );
        Log.e("WallFragment", "onCreateView: ");
        return view;
    }
//    void getData(){
//        wallList.add(new wall("http://13.235.43.83:8000/uploads/1569240326536.jpg","1",0));
//    }


    void getData() {
        loadwall.setVisibility(View.VISIBLE);
        wallList.clear();
//        imageArray.clear();
//        likedArray.clear();
//        likedArray.clear();
        SharedPreferences prefs = activity.getSharedPreferences("number", Context.MODE_PRIVATE);
        final String check = prefs.getString("roll number", "17mi524");
        firebase_id = "12345";
        AndroidNetworking.get(activity.getString(com.appteam.hillfair2k19.R.string.baseUrl) + "/feed/1/" + firebase_id )
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            loadwall.setVisibility(View.GONE);
                            System.out.println(response);
                            Log.e("response",response.toString());
                            JSONArray feed=response.getJSONArray("feed");
                            int users=feed.length();
                            for (int i = 0; i < users; i++) {
                                JSONObject json = feed.getJSONObject(i);
                                String likes = json.getString("likes");
                                String imgUrl = json.getString("image_url");
                                int liked = json.getInt("liked");
                                String postid=json.getString("post_id");
                                wallList.add(new wall(imgUrl, likes , liked,postid));
                            }
                            wallAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
//        notifyDataSetChanged()
        wallAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case com.appteam.hillfair2k19.R.id.fab:
                choosePhotoFromGallery();
                UploadRequest requestId = MediaManager.get().upload(byteArray)
                        .unsigned("xf7gsy1r")
                        .callback(new UploadCallback(){
                            @Override
                            public void onStart(String requestId) {
                            }

                            @Override
                            public void onProgress(String requestId, long bytes, long totalBytes) {
                            }

                            @Override
                            public void onSuccess(String requestId, Map resultData) {
                                System.out.println(resultData.get("url"));
                                img_Url = String.valueOf(resultData.get("url"));
                                Toast.makeText(getActivity(), img_Url + "ABCD", Toast.LENGTH_SHORT).show();
                                post();
//                                startActivity(new Intent(Profile.this, MainActivity.class));
//                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                                editor.putString("ImageURL", String.valueOf(resultData.get("url")));
//                                editor.commit();
//                                finish();
                            }

                            @Override
                            public void onError(String requestId, ErrorInfo error) {
                                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                Log.v("ErrorCloud",String.valueOf(error));
                            }
                            @Override
                            public void onReschedule(String requestId, ErrorInfo error) {
                            }
                        });
                break;
        }
    }
    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (galleryIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(galleryIntent, GALLERY);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        openDialog = 0;
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {

            if (data != null) {
                Uri photoUri = data.getData();
                try {
                    selectedImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    public void post(){
        byte[] data1 = new byte[0];
        try {
            data1 = img_Url.getBytes("UTF-8");
            base64b = Base64.encodeToString(data1, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(com.appteam.hillfair2k19.R.string.baseUrl) + "/feed/"+firebase_id+"/"+img_Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                });
        queue.add(stringRequest);

    }

}
