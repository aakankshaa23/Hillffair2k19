package com.appteam.Wall;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.appteam.hillfair2k19.FaceSmash;
import com.appteam.hillfair2k19.R;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class WallFragment extends Fragment implements View.OnClickListener {

    Bitmap selectedImage = null;
    int openDialog = 1, GALLERY = 1, CAMERA = 2;
    ProgressBar loadwall;
    String img_Url;
    SwipeRefreshLayout swiperefresh;
    private byte[] byteArray;
    private WallAdapter wallAdapter;
    private FloatingActionButton fab;
    private RecyclerView fifthRec;
    private List<wall> wallList = new ArrayList<>();
    private Activity activity;
    private String id;
    private TextView faceSmash;
    private int index = 1;
    private TextView showMore;

    public WallFragment() {
    }

    public WallFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("number", Context.MODE_PRIVATE);
        id = sharedPreferences.getString("fireBaseId", null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        AndroidNetworking.initialize(getActivity().getApplicationContext());
        View view = inflater.inflate(com.appteam.hillfair2k19.R.layout.fragment_wall, container, false);
        loadwall = view.findViewById(com.appteam.hillfair2k19.R.id.loadwall);
        fab = view.findViewById(com.appteam.hillfair2k19.R.id.fab);
        ((View) fab).setOnClickListener(this);
        fifthRec = view.findViewById(com.appteam.hillfair2k19.R.id.fifthRec);
        wallAdapter = new WallAdapter(wallList, activity);
        faceSmash = view.findViewById(R.id.facesmash);
        showMore = view.findViewById(R.id.showmore);
        faceSmash.setOnClickListener(this);
        showMore.setOnClickListener(this);

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

    void getData() {
        Log.v("firebase_idxxxx",id);
        wallList.clear();
        AndroidNetworking.get(getActivity().getString(R.string.baseUrl) + "/feed/" + String.valueOf(index) + "/" + id)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("failure"))
                            {
                                index = 1;
                                getData();
                            }
                            System.out.println(response);
                            Log.e("response", response.toString());
                            JSONArray feed = response.getJSONArray("feed");
                            int users = feed.length();
                            for (int i = 0; i < users; i++) {
                                JSONObject json = feed.getJSONObject(i);
                                String likes = json.getString("likes");
                                String imgUrl = json.getString("image_url");
                                int liked = json.getInt("liked");
                                String postid = json.getString("post_id");
                                if (imgUrl.contains("http"))
                                wallList.add(new wall(imgUrl, likes, liked, postid));
                                loadwall.setVisibility(View.GONE);
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
                break;
            case R.id.facesmash:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FaceSmash faceSmash = new FaceSmash();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.fragmentHolder, faceSmash);
                fragmentTransaction.commit();
                break;
            case R.id.showmore:
                index++;
                getData();
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
                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.JPEG, 50, bs);
                    byteArray = bs.toByteArray();
                    loadwall.setVisibility(View.VISIBLE);
                    String requestId = MediaManager.get().upload(byteArray)
                            .unsigned("xf7gsy1r")
                            .callback(new UploadCallback() {
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
//                                    Toast.makeText(getActivity(), img_Url, Toast.LENGTH_SHORT).show();
                                    loadwall.setVisibility(View.VISIBLE);
                                    post();

                                }

                                @Override
                                public void onError(String requestId, ErrorInfo error) {
                                    Log.v("ErrorCloud", String.valueOf(error));
                                }

                                @Override
                                public void onReschedule(String requestId, ErrorInfo error) {
                                }
                            })
                            .dispatch(getActivity());

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void post() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.baseUrl) + "/feed",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(activity, response, Toast.LENGTH_SHORT).show();
                        index = 1;
                        getData();
                        loadwall.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(activity, String.valueOf(error), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("firebase_id", id);
                params.put("image_url", img_Url);
                return params;
            }
        };
        queue.add(stringRequest);
    }

}
