package com.appteam.hillfair2k19;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static com.appteam.hillfair2k19.R.id.fragment;

public class QuizCategories extends Fragment {

    public SharedPreferences categoriesdata;
    public SharedPreferences.Editor editor;
    RelativeLayout quiz;
    Activity activity;
    private TextView faceSmash;

    public QuizCategories() {
        // required empty constructor
    }

    public QuizCategories(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_quiz_categories, container, false);
        quiz = view.findViewById(fragment);

        categoriesdata = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        editor = categoriesdata.edit();

        CardView Science_layout = view.findViewById(R.id.view1);
        CardView Anime_layout = view.findViewById(R.id.view2);
        CardView WebSeries_layout = view.findViewById(R.id.view3);
        CardView NIT_layout = view.findViewById(R.id.view4);
        CardView Sports_layout = view.findViewById(R.id.view5);
        CardView Mythology_layout = view.findViewById(R.id.view6);
        CardView Movies_layout = view.findViewById(R.id.view7);
        faceSmash = view.findViewById(R.id.facesmash);
        faceSmash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FaceSmash faceSmash = new FaceSmash();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.fragmentHolder, faceSmash);
                fragmentTransaction.commit();
            }
        });

        if (categoriesdata.getBoolean("Science", false)) {
            Science_layout.setVisibility(View.GONE);
        }
        if (categoriesdata.getBoolean("Anime", false)) {
            Anime_layout.setVisibility(View.GONE);
        }
        if (categoriesdata.getBoolean("Movies", false)) {
            Movies_layout.setVisibility(View.GONE);
        }
        if (categoriesdata.getBoolean("WebSeries", false)) {
            WebSeries_layout.setVisibility(View.GONE);
        }
        if (categoriesdata.getBoolean("Mythology", false)) {
            Mythology_layout.setVisibility(View.GONE);
        }
        if (categoriesdata.getBoolean("Sports", false)) {
            Sports_layout.setVisibility(View.GONE);
        }
        if (categoriesdata.getBoolean("Nit", false)) {
            NIT_layout.setVisibility(View.GONE);
        }

        Science_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("Science", true);
                editor.apply();


                Intent intent = new Intent(getActivity(), Quiz_QnA.class);
                intent.putExtra("Category", 1);
                startActivity(intent);
                getActivity().finish();
            }
        });

        Anime_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("Anime", true);
                editor.apply();

                Intent intent = new Intent(getActivity(), Quiz_QnA.class);
                intent.putExtra("Category", 2);
                startActivity(intent);
                getActivity().finish();

            }
        });

        WebSeries_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("WebSeries", true);
                editor.apply();

                Intent intent = new Intent(getActivity(), Quiz_QnA.class);
                intent.putExtra("Category", 3);
                startActivity(intent);
                getActivity().finish();

            }
        });

        NIT_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("Nit", true);
                editor.apply();

                Intent intent = new Intent(getActivity(), Quiz_QnA.class);
                intent.putExtra("Category", 4);
                startActivity(intent);
                getActivity().finish();

            }
        });

        Sports_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("Sports", true);
                editor.apply();


                Intent intent = new Intent(getActivity(), Quiz_QnA.class);
                intent.putExtra("Category", 5);
                startActivity(intent);
                getActivity().finish();

            }
        });

        Mythology_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editor.putBoolean("Mythology", true);
                editor.apply();


                Intent intent = new Intent(getActivity(), Quiz_QnA.class);
                intent.putExtra("Category", 6);
                startActivity(intent);
                getActivity().finish();

            }
        });

        Movies_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editor.putBoolean("Movies", true);
                editor.apply();


                Intent intent = new Intent(getActivity(), Quiz_QnA.class);
                intent.putExtra("Category", 7);
                startActivity(intent);
                getActivity().finish();

            }
        });
        return view;
    }
}