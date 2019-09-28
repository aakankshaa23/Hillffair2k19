package com.appteam.hillfair2k19.Wall;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.appteam.hillfair2k19.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class WallFragment extends Fragment implements View.OnClickListener {

    public static ArrayList<String> imageArray = new ArrayList<>();
    public static ArrayList<String> likesArray = new ArrayList<>();
    public static ArrayList<Boolean> likedArray = new ArrayList<>();
    String user_id;
    String firebase_id;
    ProgressBar loadwall;
    int set = 0;
    SwipeRefreshLayout swiperefresh;
    private WallAdapter wallAdapter;
    private FloatingActionButton fab;
    private RecyclerView fifthRec;
    private List<wall> wallList = new ArrayList<>();
    private Activity activity;
    private int PICK_PHOTO_CODE = 1046;
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
        View view = inflater.inflate(R.layout.fragment_wall, container, false);
        loadwall = view.findViewById(R.id.loadwall);
        fab = view.findViewById(R.id.fab);
        ((View) fab).setOnClickListener(this);
        SharedPreferences prefs = this.getActivity().getSharedPreferences("roll number", Context.MODE_PRIVATE);
        user_id = prefs.getString("name", "gsbs");
        fifthRec = view.findViewById(R.id.fifthRec);
        wallAdapter = new WallAdapter(wallList, activity);

        fifthRec.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
        fifthRec.setAdapter(wallAdapter);
        getData();
        swiperefresh = view.findViewById(R.id.swiperefresh);
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
    void getData(){
        wallList.add(new wall("http://13.235.43.83:8000/uploads/1569240326536.jpg","1",0));
    }


//    void getData() {
//        loadwall.setVisibility(View.VISIBLE);
//        wallList.clear();
//        imageArray.clear();
//        likedArray.clear();
//        likedArray.clear();
//        SharedPreferences prefs = activity.getSharedPreferences("number", Context.MODE_PRIVATE);
//        final String check = prefs.getString("roll number", "17mi524");
//        firebase_id = "12345";
//        AndroidNetworking.get(activity.getString(R.string.baseUrl) + "/feed/1/" + firebase_id )
//                .build()
//                .getAsJSONArray(new JSONArrayRequestListener() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        try {
//                            loadwall.setVisibility(View.GONE);
//                            System.out.println(response);
//                            int users = response.length();
//                            for (int i = 0; i < users; i++) {
//                                JSONObject json = response.getJSONObject(i);
//                                String likes = json.getString("likes");
//                                String imgUrl = json.getString("image_url");
//                                int liked = json.getInt("liked");
//                                wallList.add(new wall(imgUrl, likes , liked));
//                            }
//                            wallAdapter.notifyDataSetChanged();
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    @Override
//                    public void onError(ANError error) {
//                        // handle error
//                    }
//                });
////        notifyDataSetChanged()
//        wallAdapter.notifyDataSetChanged();
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (intent.resolveActivity(activity.getPackageManager()) != null) {
                    startActivityForResult(intent, PICK_PHOTO_CODE);
                }
                break;
        }
    }
}
