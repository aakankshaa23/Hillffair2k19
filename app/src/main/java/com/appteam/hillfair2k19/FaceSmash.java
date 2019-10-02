package com.appteam.hillfair2k19;


import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class FaceSmash extends Fragment {


    private Animation outAnimation;
    private Animation inAnimation;

    private IResult mResultCallback;
    private VolleyService mVolleyService;

    private ArrayList<String> imageUrls;
    private ArrayList<String> firebaseIds;
    private ArrayList<String> genders;
    private ArrayList<String> ratings;

    private ImageView firstPersonImage;
    private ImageView secondPersonImage;

//    ArrayList<User> users;


    private HashMap<String, Boolean> hashMap;

    private int firstImage;
    private int secondImage;

    private boolean flag = false;

    private Animation popOut;
    private Animation popIn;
    ProgressBar loadwall;
    ProgressBar loadwall2;


    public FaceSmash() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageUrls = new ArrayList<>();
        firebaseIds = new ArrayList<>();
        genders = new ArrayList<>();
        ratings = new ArrayList<>();
        hashMap = new HashMap<>();



        popOut = AnimationUtils.loadAnimation(getContext(),
                R.anim.pop_out);

        popIn = AnimationUtils.loadAnimation(getContext(),
                R.anim.pop_in);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View inflateView = inflater.inflate(R.layout.fragment_face_smash, container, false);

        loadwall=inflateView.findViewById(R.id.loadwall);
        loadwall2=inflateView.findViewById(R.id.loadwall2);

        loadwall.setVisibility(View.VISIBLE);
        loadwall2.setVisibility(View.VISIBLE);

        Animation bottomUp = AnimationUtils.loadAnimation(getContext(),
                R.anim.bottom_up);
        Animation bottomDown = AnimationUtils.loadAnimation(getContext(),
                R.anim.bottom_down);


        CardView cardView1 = inflateView.findViewById(R.id.card1);
        CardView cardView2 = inflateView.findViewById(R.id.card2);

//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//
//
//        cardView2.setTranslationY(metrics.heightPixels / 2);
//        ObjectAnimator animation = ObjectAnimator.ofFloat(cardView1, "translationY", metrics.heightPixels / 2);
//        animation.setDuration(1000);
//        animation.start();
//
//        animation = ObjectAnimator.ofFloat(cardView2, "translationY", -metrics.heightPixels / 2);
//        animation.setDuration(1000);
//        animation.start();


        initVolleyCallback();

        mVolleyService = new VolleyService(mResultCallback, getContext());


        final VolleyService mVolleyService = new VolleyService(mResultCallback, getContext());


        mVolleyService.getJsonArrayDataVolley("GETJSONARRAYLIFESAVER", getString(R.string.baseUrl) + "/faceSmash");


        outAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fadeout);
        inAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fadein);


        firstPersonImage = inflateView.findViewById(R.id.firstPersonImage);
        secondPersonImage = inflateView.findViewById(R.id.secondPersonImage);


        firstPersonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ImageView btn2 = inflateView.findViewById(R.id.heartImageView2);
                btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        animateHeart(btn2);
                    }
                });
                animateHeart(btn2);

                firstPersonImage.startAnimation(popOut);


                popOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        firstPersonImage.startAnimation(popIn);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });


                postresult(firstImage);


                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms

                        if (imageUrls.size() < 2) handleNoIMages();

                        else {
                            changeImage();
                        }
                    }
                }, 600);


            }
        });


        secondPersonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext(), "f", Toast.LENGTH_SHORT).show();

                final ImageView btn = inflateView.findViewById(R.id.heartImageView);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        animateHeart(btn);
                    }
                });
                animateHeart(btn);

                secondPersonImage.startAnimation(popOut);

                popOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        secondPersonImage.startAnimation(popIn);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });


                postresult(secondImage);


                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms


                        if (imageUrls.size() < 2) handleNoIMages();

                        else {

                            firstPersonImage.setClickable(true);
                            secondPersonImage.setClickable(true);
                            changeImage();
                        }

                    }
                }, 600);

            }
        });

        return inflateView;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void animateHeart(final ImageView view) {
        Log.i("S", "ssf");
        view.setImageResource(R.drawable.orange_heart);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        prepareAnimation(scaleAnimation);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        prepareAnimation(alphaAnimation);

        AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(alphaAnimation);
        animation.addAnimation(scaleAnimation);
        animation.setDuration(500);
        animation.setFillAfter(true);

        view.startAnimation(animation);

    }

    private Animation prepareAnimation(Animation animation) {
        animation.setRepeatCount(1);
        animation.setRepeatMode(Animation.REVERSE);
        return animation;
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
                } else {
                    Log.e("zHell", jsonArray.toString());

//                    //JsonArray
//                    Toast.makeText(getContext(), String.valueOf(jsonArray), Toast.LENGTH_SHORT).show();


                    for (int i = 0; i < jsonArray.length(); i++) {

                        try {
                            obj = jsonArray.getJSONObject(i);
                            String url = obj.getString("url").replace("\\\\", "");
                            Log.e("URLs", url);
                            if (url.contains("https") || url.contains("http"))
                                imageUrls.add(url);

                            String id = obj.getString("firebase_id");
                            firebaseIds.add(id);

                            String gender = obj.getString("gender");
                            genders.add(gender);

                            String rating = obj.getString("rating");
                            ratings.add(rating);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                    if (imageUrls.size() >= 1 && !flag) {

                        changeImage();
                    }


                }
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.i("error", error.toString());
            }
        };

    }


    void handleNoIMages() {
        firstPersonImage.setClickable(false);
        secondPersonImage.setClickable(false);
        Log.d("sizes", String.valueOf(imageUrls.size()));
        new AlertDialog.Builder(getContext())
                .setTitle("No Entries")
                .setMessage("Sorry No Entries Left!")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation

                        //TODO: redirect to home page
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    private void postresult(int winner) {

        String userId = null;
        FirebaseUser currentFirebaseUser = null;
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            userId = currentFirebaseUser.getUid();
            if (userId != null&&firebaseIds.size()>=firstImage&&firebaseIds.size()>=secondImage&&firebaseIds.size()>0) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("firebase_id", userId);
                    jsonObject.put("ID1", firebaseIds.get(firstImage));
                    jsonObject.put("ID2", firebaseIds.get(secondImage));
                    jsonObject.put("WID", firebaseIds.get(winner));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mVolleyService.postJsonDataVolley("POSTJSONDATALIFESAVER", getString(R.string.baseUrl) + "/faceSmash", jsonObject);
            }
        }
    }

    private void changeImage() {
        loadwall2.setVisibility(View.VISIBLE);
        loadwall.setVisibility(View.VISIBLE);

        Log.d("size", String.valueOf(imageUrls.size()));

        int n = imageUrls.size() * imageUrls.size();
        boolean flag2 = false;

        while (!flag2 && n != 0) {
            n--;
            int firstUrl = new Random().nextInt(imageUrls.size());
            int secondUrl = new Random().nextInt(imageUrls.size());

            if (!hashMap.containsKey(imageUrls.get(firstUrl) + imageUrls.get(secondUrl)) && !(imageUrls.get(firstUrl).equals(imageUrls.get(secondUrl)))) {

                Picasso.with(getContext()).load(imageUrls.get(firstUrl)).into(firstPersonImage);
                Picasso.with(getContext()).load(imageUrls.get(secondUrl)).into(secondPersonImage);

                loadwall.setVisibility(View.INVISIBLE);
                loadwall2.setVisibility(View.INVISIBLE);

                firstImage = firstUrl;
                secondImage = secondUrl;

                flag = true;
                flag2 = true;
                hashMap.put(imageUrls.get(firstUrl) + imageUrls.get(secondUrl), true);
                hashMap.put(imageUrls.get(secondUrl) + imageUrls.get(firstUrl), true);

            }
        }

        if (!flag2) {
            handleNoIMages();
        }
    }

}
