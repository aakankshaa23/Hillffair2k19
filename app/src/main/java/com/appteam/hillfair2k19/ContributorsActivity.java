package com.appteam.hillfair2k19;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appteam.adapters.ContributorsAdaptor;
import com.appteam.hillfair2k19.model.contributorsItem;

import java.util.ArrayList;
import java.util.List;

public class ContributorsActivity extends AppCompatActivity {
    String BASE_URL = "https://github.com/";
    private List<contributorsItem> contributorsItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContributorsAdaptor contributorsAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contributors);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        contributorsAdaptor = new ContributorsAdaptor(contributorsItems, ContributorsActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(contributorsAdaptor);

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
        initData();
    }

    private void initData() {
        contributorsItems.add(new contributorsItem("Muskan Jhunjhunwalla", BASE_URL + "musukeshu.png", BASE_URL + "musukeshu"));
        contributorsItems.add(new contributorsItem("Anubhaw Bhalotia", BASE_URL + "anubhawbhalotia.png", BASE_URL + "anubhawbhalotia"));
        contributorsItems.add(new contributorsItem("Kartik Saxena", BASE_URL + "SaxenaKartik.png", BASE_URL + "SaxenaKartik"));
        contributorsItems.add(new contributorsItem("Bharat Shah", BASE_URL + "bharatshah1498.png", BASE_URL + "bharatshah1498"));

        //3rd Year
        contributorsItems.add(new contributorsItem("Daniyaal Khan", BASE_URL + "drtweety.png", BASE_URL + "drtweety"));
        contributorsItems.add(new contributorsItem("ThisIsNSH", BASE_URL + "ThisIsNSH.png", BASE_URL + "ThisIsNSH"));
        contributorsItems.add(new contributorsItem("Abhinav Lamba", BASE_URL + "Abhinavlamba.png", BASE_URL + "Abhinavlamba"));
        contributorsItems.add(new contributorsItem("Utkarsh Singh", BASE_URL + "utkarshsingh99.png", BASE_URL + "utkarshsingh99"));
        contributorsItems.add(new contributorsItem("Naman Lalit", BASE_URL + "naman99lalit.png", BASE_URL + "naman99lalit"));
        contributorsItems.add(new contributorsItem("Vishal Parmar", BASE_URL + "Vishal17599.png", BASE_URL + "Vishal17599"));
        contributorsItems.add(new contributorsItem("Divyanshu Dhawan", BASE_URL + "dextroxd.png", BASE_URL + "dextroxd"));
        contributorsItems.add(new contributorsItem("Akanksha Tiwari", BASE_URL + "aakankshaa23.png", BASE_URL + "aakankshaa23"));
        contributorsItems.add(new contributorsItem("Alisha Mehta", BASE_URL + "Alisha1116.png", BASE_URL + "Alisha1116"));
        contributorsItems.add(new contributorsItem("Arjun Anand", BASE_URL + "arjunluam.png", BASE_URL + "arjunluam"));
        contributorsItems.add(new contributorsItem("Tanvi Mahajan", BASE_URL + "tanvi003.png", BASE_URL + "tanvi003"));
        contributorsItems.add(new contributorsItem("Abhiraj Singh Rathore", BASE_URL + "AbhirajSinghRathore.png", BASE_URL + "AbhirajSingh99"));
        contributorsItems.add(new contributorsItem("Anshu Akansha", BASE_URL + "AnshuAkansha.png", BASE_URL + "AnshuAkansha"));

        //2st Year
        contributorsItems.add(new contributorsItem("Pawan Harariya", BASE_URL + "pawanharariya.png", BASE_URL + "pawanharariya"));
        contributorsItems.add(new contributorsItem("Moulik", BASE_URL + "moulikbhardwaj.png", BASE_URL + "maoulikbhardwaj"));
        contributorsItems.add(new contributorsItem("Shubham kumar", BASE_URL + "SkroX.png", BASE_URL + "SkroX"));
        contributorsItems.add(new contributorsItem("Shweta Gurnani", BASE_URL + "shwetagurnani.png", BASE_URL + "shwetagurnani"));
        contributorsItems.add(new contributorsItem("Shreya Agarwal", BASE_URL + "shreyaag770.png", BASE_URL + "shreyaag770"));
        contributorsItems.add(new contributorsItem("Chandan Sitlani", BASE_URL + "ChandanSitlani.png", BASE_URL + "ChandanSitlani"));
        contributorsItems.add(new contributorsItem("Ananya Sharma", BASE_URL + "Ananya-Sharma.png", BASE_URL + "Ananya-Sharma"));
        contributorsItems.add(new contributorsItem("Sarthak", BASE_URL + "Sarthak3661.png", BASE_URL + "Sarthak3661"));
        contributorsItems.add(new contributorsItem("Ayush Kaushal", BASE_URL + "Yashu017.png", BASE_URL + "Yashu017"));
        contributorsItems.add(new contributorsItem("Amishi Bansal", BASE_URL + "AmishiBansal.png", BASE_URL + "AmishiBansal"));
        contributorsItems.add(new contributorsItem("Pranjal Dureja", BASE_URL + "pranjaldureja0002.png", BASE_URL + "pranjaldureja0002"));
        contributorsItems.add(new contributorsItem("Rupali Mehra", BASE_URL + "rupalimehra.png", BASE_URL + "rupalimehra"));

        contributorsItems.add(new contributorsItem("Sushant Chandla", BASE_URL + "SushantChandla.png", BASE_URL + "SushantChandla"));
        contributorsItems.add(new contributorsItem("Nishant Chandla", BASE_URL + "NishantChandla.png", BASE_URL + "NishantChandla"));
        contributorsItems.add(new contributorsItem("Mrigank Anand", BASE_URL + "Spiderxm.png", BASE_URL + "Spiderxm"));
        contributorsItems.add(new contributorsItem("Kashika", BASE_URL + "Kashika1020.png", BASE_URL + "Kashika1020"));
        contributorsItems.add(new contributorsItem("Pankaj Sharma", BASE_URL + "Spankaj0029.png", BASE_URL + "Spankaj0029"));
        contributorsItems.add(new contributorsItem("Pankaj Sharma", BASE_URL + "PankajSharma191201.png", BASE_URL + "PankajSharma191201"));
        contributorsItems.add(new contributorsItem("Anshit", BASE_URL + "Anshit01.png", BASE_URL + "Anshit01"));
        contributorsItems.add(new contributorsItem("Utkarsh Rai", BASE_URL + "raiutkarsh09.png", BASE_URL + "raiutkarsh09"));
        contributorsItems.add(new contributorsItem("Gautam Verma", BASE_URL + "hey1729gautam@gmail.com.png", BASE_URL + "hey1729gautam@gmail.com"));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}

