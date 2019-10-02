package com.appteam.hillfair2k19;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.appteam.adapters.LeaderboardAdapter;
import com.appteam.model.Leaderboard;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LeaderboardFragment extends Fragment implements View.OnClickListener {
    ProgressBar loadwall;
    Boolean ToggleButtonState;
    private LeaderboardAdapter clubAdapter;
    private RecyclerView recyclerView;
    private List<Leaderboard> clubList = new ArrayList<>();
    private TextView point, referral, popular;
    private Activity activity;
    private IResult mResultCallback;
    private VolleyService mVolleyService;
    private ToggleButton toggleButton;

    public LeaderboardFragment() {
    }

    public LeaderboardFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        toggleButton = view.findViewById(R.id.toggleButton);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });

        point = view.findViewById(R.id.point);
        referral = view.findViewById(R.id.referral);
        loadwall = view.findViewById(R.id.loadwall);
        popular = view.findViewById(R.id.popular);
        popular.setOnClickListener(this);
        point.setOnClickListener(this);
        referral.setOnClickListener(this);
        recyclerView = view.findViewById(R.id.leaderboard);
        clubAdapter = new LeaderboardAdapter(clubList, activity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(clubAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        getData();
        Log.e("LeaderFragment", "onCreateView: ");
        return view;
    }

    public void getData() {

        clubList.clear();
        clubAdapter.notifyDataSetChanged();
        loadwall.setVisibility(View.VISIBLE);

        initVolleyCallback();

        mVolleyService = new VolleyService(mResultCallback, getContext());


        final VolleyService mVolleyService = new VolleyService(mResultCallback, getContext());

        ToggleButtonState = toggleButton.isChecked();

        if (ToggleButtonState)
            mVolleyService.getJsonArrayDataVolley("GETJSONARRAYLIFESAVER", getString(R.string.baseUrl) + "/faceSmash");
        else
            mVolleyService.getJsonObjectDataVolley("GETJSONARRAYLIFESAVER", getString(R.string.baseUrl) + "/quiz/leaderboard");


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popular:
                popular.setTextColor(getResources().getColor(R.color.black));
                point.setTextColor(getResources().getColor(R.color.hint));
                referral.setTextColor(getResources().getColor(R.color.hint));
                break;
            case R.id.point:
                point.setTextColor(getResources().getColor(R.color.black));
                popular.setTextColor(getResources().getColor(R.color.hint));
                referral.setTextColor(getResources().getColor(R.color.hint));
                break;
            case R.id.referral:
                referral.setTextColor(getResources().getColor(R.color.black));
                point.setTextColor(getResources().getColor(R.color.hint));
                popular.setTextColor(getResources().getColor(R.color.hint));
                break;
        }

    }


    void initVolleyCallback() {
        mResultCallback = new IResult() {
            JSONObject obj;

            @Override
            public void notifySuccess(String requestType, JSONObject response, JSONArray jsonArray) {


                if (response != null) {

                    Log.e("Hellcatt", response.toString());
                    //JsonObject
                    // Toast.makeText(getContext(), String.valueOf(response), Toast.LENGTH_SHORT).show();

                    try {
                        JSONArray array = response.getJSONArray("leaderboard");
                        for (int i = 0; i < array.length(); ++i) {
                            JSONObject object = array.getJSONObject(i);
                            String name = object.getString("Name");
                            String gender = object.getString("Gender");
                            int quiz_rating = object.getInt("quiz_rating");
                            String image_url=object.getString("image_url");

                            clubList.add(new Leaderboard(name,quiz_rating,gender,image_url));

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    clubAdapter.notifyDataSetChanged();
                    loadwall.setVisibility(View.INVISIBLE);
                } else {
                    Log.e("zHell", jsonArray.toString());
                    for(int i=0;i<jsonArray.length();++i){

                        try {
                            JSONObject object=jsonArray.getJSONObject(i);
                            String name =object.getString("name");
                            int rating=object.getInt("rating");
                            String gender=object.getString("gender");
                            String image_url=object.getString("url");
                            clubList.add(new Leaderboard(name,rating,gender,image_url));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        clubAdapter.notifyDataSetChanged();
                        loadwall.setVisibility(View.INVISIBLE);

                    }



//


                }
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.i("error", error.toString());
            }
        };

    }


}
