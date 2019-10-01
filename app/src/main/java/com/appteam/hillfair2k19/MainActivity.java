package com.appteam.hillfair2k19;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.SimpleLottieValueCallback;
import com.appteam.Wall.WallFragment;
import com.appteam.fragments.ClubsFragment;
import com.appteam.fragments.Coupons;
import com.appteam.fragments.ScheduleFragment;
import com.appteam.fragments.SponsersFragment;
import com.appteam.hillfair2k19.ui.notifications.NotificationsFragment;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static int home, games, schedule, coupon, currentSelected;
    public static String fireBaseID;
    private CircleImageView profile;
    private ImageButton homeButton, gamesButton, scheduleButton, couponButton;
    private TextView homeTextView, gamesTextView, scheduleTextView, couponTextView;
    private LinearLayout homeLinearLayout, gameLinearLayout, scheduleLinearLayout, couponLinearLayout;
    private View nav;
    private LottieAnimationView navAnim;
    private TextView title, profileNav, aboutNav, settingNav, sponsorNav, callNav, notifNav, mapNav, hillffairNav, contributorNav, facesmash, leaderboard, coreTeamNav, clubsNav;
    private boolean check = true;
    private RelativeLayout navDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linkViews();
        homeButton.performClick();
        final SharedPreferences sharedPreferences = getSharedPreferences("number", Context.MODE_PRIVATE);
        fireBaseID = sharedPreferences.getString("fireBaseId", null);

        SharedPreferences prefs = getSharedPreferences("number", Context.MODE_PRIVATE);
        Log.v("firebasexxxx",prefs.getString("fireBaseId","12345"));
        String check2 = prefs.getString("Image", "https://www.fluigent.com/wp-content/uploads/2018/07/default-avatar-BW.png");
        if (!check2.equals("https://www.fluigent.com/wp-content/uploads/2018/07/default-avatar-BW.png")) {
            Bitmap img = setProfile(check2);
            profile.setImageBitmap(img);
        } else {
            Picasso.with(MainActivity.this).load(check2).resize(80, 80).centerCrop().into(profile);
        }

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ProfileMain.class));
                finish();
            }
        });

        navAnim.addValueCallback(
                new KeyPath("**"),
                LottieProperty.COLOR_FILTER,
                new SimpleLottieValueCallback<ColorFilter>() {
                    @Override
                    public ColorFilter getValue(LottieFrameInfo<ColorFilter> frameInfo) {
                        return new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                    }
                }
        );

        home = homeLinearLayout.getMeasuredWidth();
        games = gameLinearLayout.getMeasuredWidth();
        schedule = scheduleLinearLayout.getMeasuredWidth();
        coupon = couponLinearLayout.getMeasuredWidth();

        animate(gameLinearLayout, gamesTextView, gamesButton);
        animate(scheduleLinearLayout, scheduleTextView, scheduleButton);
        animate(couponLinearLayout, couponTextView, couponButton);

        currentSelected = -1;

        handleClick(findViewById(R.id.homeTextView));

    }

    public Bitmap setProfile(String images) {
        byte[] decodedByte = Base64.decode(images, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public void linkViews() {

        homeButton = findViewById(R.id.homeButton);
        gamesButton = findViewById(R.id.gamesButton);
        scheduleButton = findViewById(R.id.scheduleButton);
        couponButton = findViewById(R.id.couponButton);

        homeTextView = findViewById(R.id.homeTextView);
        gamesTextView = findViewById(R.id.gameTextView);
        scheduleTextView = findViewById(R.id.scheduleTextView);
        couponTextView = findViewById(R.id.couponTextView);

        homeLinearLayout = findViewById(R.id.homeLayout);
        gameLinearLayout = findViewById(R.id.gamesLayout);
        scheduleLinearLayout = findViewById(R.id.scheduleLayout);
        couponLinearLayout = findViewById(R.id.couponsLayout);

        nav = findViewById(R.id.nav);
        navAnim = findViewById(R.id.navAnim);
        navDrawer = findViewById(R.id.navDrawer);
        profileNav = findViewById(R.id.profileNav);
        aboutNav = findViewById(R.id.aboutNav);
        settingNav = findViewById(R.id.settingNav);
        sponsorNav = findViewById(R.id.sponsorNav);
        callNav = findViewById(R.id.callNav);
        notifNav = findViewById(R.id.notifNav);
        hillffairNav = findViewById(R.id.hillffairNav);
        mapNav = findViewById(R.id.mapNav);
        contributorNav = findViewById(R.id.contributorNav);
        facesmash = findViewById(R.id.facesmash);
        leaderboard = findViewById(R.id.leaderboard);
        coreTeamNav = findViewById(R.id.coreTeamNav);
        clubsNav = findViewById(R.id.clubsNav);
        profile = findViewById(R.id.profile);

        profileNav.setOnClickListener(this);
        aboutNav.setOnClickListener(this);
        callNav.setOnClickListener(this);
        notifNav.setOnClickListener(this);
        mapNav.setOnClickListener(this);
        settingNav.setOnClickListener(this);
        nav.setOnClickListener(this);
        hillffairNav.setOnClickListener(this);
        contributorNav.setOnClickListener(this);
        facesmash.setOnClickListener(this);
        leaderboard.setOnClickListener(this);
        coreTeamNav.setOnClickListener(this);
        clubsNav.setOnClickListener(this);

    }

    public void handleClick(View view) {
        int id = view.getId();
        LinearLayout l;
        FragmentManager fragmentManager = getSupportFragmentManager();

        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (id) {
            case R.id.homeButton:
            case R.id.homeTextView:

                if (currentSelected != 0) {

                    currentSelected = 0;

                    l = findViewById(R.id.homeLayout);
                    WallFragment wallFragment = new WallFragment(this);
                    fragmentTransaction.replace(R.id.fragmentHolder, wallFragment);
                    fragmentTransaction.commit();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    animateReverse(l, homeTextView);
                    animate(gameLinearLayout, gamesTextView, gamesButton);
                    animate(scheduleLinearLayout, scheduleTextView, scheduleButton);
                    animate(couponLinearLayout, couponTextView, couponButton);

                    homeButton.setColorFilter(Color.argb(255, 255, 87, 51));
                    homeTextView.setTextColor(Color.argb(255, 255, 87, 51));

                }

                break;
            case R.id.gamesButton:
            case R.id.gameTextView:

                if (currentSelected != 1) {

                    gamesButton.setColorFilter(Color.argb(255, 255, 87, 51));
                    gamesTextView.setTextColor(Color.argb(255, 255, 87, 51));

                    currentSelected = 1;

                    l = findViewById(R.id.gamesLayout);
                    QuizCategories quizCategories = new QuizCategories(this);
                    fragmentTransaction.replace(R.id.fragmentHolder, quizCategories);
                    fragmentTransaction.commit();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    animateReverse(l, gamesTextView);
                    animate(homeLinearLayout, homeTextView, homeButton);
                    animate(scheduleLinearLayout, scheduleTextView, scheduleButton);
                    animate(couponLinearLayout, couponTextView, couponButton);

                }


                break;
            case R.id.scheduleButton:
            case R.id.scheduleTextView:

                if (currentSelected != 2) {

                    scheduleButton.setColorFilter(Color.argb(255, 255, 87, 51));
                    scheduleTextView.setTextColor(Color.argb(255, 255, 87, 51));

                    currentSelected = 2;

                    l = findViewById(R.id.scheduleLayout);
                    ScheduleFragment scheduleFragment = new ScheduleFragment(this);
                    fragmentTransaction.replace(R.id.fragmentHolder, scheduleFragment);
                    fragmentTransaction.commit();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    animateReverse(l, gamesTextView);
                    animate(gameLinearLayout, gamesTextView, gamesButton);
                    animate(homeLinearLayout, homeTextView, homeButton);
                    animate(couponLinearLayout, couponTextView, couponButton);
                    animateReverse(l, scheduleTextView);
                }
                break;
            case R.id.couponButton:
            case R.id.couponTextView:
                if (currentSelected != 3) {

                    couponButton.setColorFilter(Color.argb(255, 255, 87, 51));
                    couponTextView.setTextColor(Color.argb(255, 255, 87, 51));

                    currentSelected = 3;

                    l = findViewById(R.id.couponsLayout);
                    Coupons coupons = new Coupons();
                    fragmentTransaction.replace(R.id.fragmentHolder, coupons);
                    fragmentTransaction.commit();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                    animate(gameLinearLayout, gamesTextView, gamesButton);
                    animate(scheduleLinearLayout, scheduleTextView, scheduleButton);
                    animate(homeLinearLayout, homeTextView, homeButton);

                    animateReverse(l, couponTextView);
                }

                break;

        }


    }

    public void animate(final LinearLayout l, final TextView txtv, ImageButton imageButton) {

        imageButton.setColorFilter(Color.argb(255, 255, 255, 255));
        txtv.setTextColor(Color.argb(255, 255, 255, 255));

        txtv.setVisibility(View.GONE);
        ValueAnimator anim = ValueAnimator.ofInt(l.getMeasuredWidth(), 56);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = l.getLayoutParams();
                layoutParams.width = val;
                l.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(200);
        anim.start();

        l.getBackground().setColorFilter(Color.parseColor("#00FFFFFF"), PorterDuff.Mode.SRC_ATOP);


    }

    public void animateReverse(final LinearLayout l, final TextView txtv) {
        txtv.setVisibility(View.VISIBLE);
        int x = l.getId();
        int w;
        if (x == R.id.homeLayout) w = home;
        else if (x == R.id.gamesLayout) w = games;
        else if (x == R.id.scheduleLayout) w = schedule;
        if (x == R.id.couponsLayout) w = coupon;
        else w = schedule;

        l.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimaryLight), PorterDuff.Mode.SRC_ATOP);
        ValueAnimator anim = ValueAnimator.ofInt(56, 300);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = l.getLayoutParams();
                layoutParams.width = val;
                l.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(150);
        anim.start();


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (view.getId()) {
            case R.id.profileNav:
                startActivity(new Intent(this, ProfileMain.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                break;
            case R.id.notifNav:
                NotificationsFragment notificationsFragment = new NotificationsFragment();
                fragmentTransaction.replace(R.id.fragmentHolder, notificationsFragment);
                fragmentTransaction.commit();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                navDrawer();
                break;
            case R.id.contributorNav:
                startActivity(new Intent(MainActivity.this, ContributorsActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.facesmash:
                FaceSmash faceSmash = new FaceSmash();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.fragmentHolder, faceSmash);
                fragmentTransaction.commit();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                navDrawer();
                break;
            case R.id.leaderboard:
                LeaderboardFragment leaderboardFragment = new LeaderboardFragment();
                fragmentTransaction.replace(R.id.fragmentHolder, leaderboardFragment);
                fragmentTransaction.commit();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                navDrawer();
                break;

            case R.id.coreTeamNav:
                coreteam coreteam = new coreteam();
                fragmentTransaction.replace(R.id.fragmentHolder, coreteam);
                fragmentTransaction.commit();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                navDrawer();
                break;
            case R.id.sponsorNav:
                SponsersFragment sponsersFragment = new SponsersFragment();
                fragmentTransaction.replace(R.id.fragmentHolder, sponsersFragment);
                fragmentTransaction.commit();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                navDrawer();
                break;
            case R.id.clubsNav:
                ClubsFragment clubsFragment = new ClubsFragment();
                fragmentTransaction.replace(R.id.fragmentHolder, clubsFragment);
                fragmentTransaction.commit();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                navDrawer();
                break;
            case R.id.nav:
                navDrawer();
                break;

        }
    }

    private void navDrawer() {
        if (check) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
            valueAnimator.setDuration(1300);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (animation.getAnimatedFraction() <= 0.7f)
                        navAnim.setProgress(animation.getAnimatedFraction());
                }
            });
            valueAnimator.start();
            check = false;
            navDrawer.setVisibility(View.VISIBLE);
            profileNav.setVisibility(View.VISIBLE);
            sponsorNav.setVisibility(View.GONE);
//            notifNav.setVisibility(View.VISIBLE);
            contributorNav.setVisibility(View.VISIBLE);
            facesmash.setVisibility(View.VISIBLE);
            leaderboard.setVisibility(View.VISIBLE);
            coreTeamNav.setVisibility(View.VISIBLE);
            clubsNav.setVisibility(View.VISIBLE);

            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(navDrawer, "alpha", 0, 1);
            ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(aboutNav, "alpha", 0, 1);
            ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(profileNav, "alpha", 0, 1);
            ObjectAnimator objectAnimator4 = ObjectAnimator.ofFloat(settingNav, "alpha", 0, 1);
            ObjectAnimator objectAnimator5 = ObjectAnimator.ofFloat(sponsorNav, "alpha", 0, 1);
            ObjectAnimator objectAnimator1a = ObjectAnimator.ofFloat(mapNav, "alpha", 0, 1);
            ObjectAnimator objectAnimator2a = ObjectAnimator.ofFloat(notifNav, "alpha", 0, 1);
            ObjectAnimator objectAnimator3a = ObjectAnimator.ofFloat(callNav, "alpha", 0, 1);
            ObjectAnimator objectAnimator3aa = ObjectAnimator.ofFloat(hillffairNav, "alpha", 0, 1);

            ObjectAnimator objectAnimator6 = ObjectAnimator.ofFloat(aboutNav, "translationY", 15f, 0f);
            ObjectAnimator objectAnimator7 = ObjectAnimator.ofFloat(profileNav, "translationY", 15f, 0f);
            ObjectAnimator objectAnimator8 = ObjectAnimator.ofFloat(settingNav, "translationY", 15f, 0f);
            ObjectAnimator objectAnimator9 = ObjectAnimator.ofFloat(sponsorNav, "translationY", 15f, 0f);
            ObjectAnimator objectAnimator4a = ObjectAnimator.ofFloat(callNav, "translationY", 15f, 0f);
            ObjectAnimator objectAnimator5a = ObjectAnimator.ofFloat(mapNav, "translationY", 15f, 0f);
            ObjectAnimator objectAnimator6a = ObjectAnimator.ofFloat(notifNav, "translationY", 15f, 0f);
            ObjectAnimator objectAnimator6aa = ObjectAnimator.ofFloat(hillffairNav, "translationY", 15f, 0f);

            objectAnimator5.setDuration(400);
            objectAnimator4.setDuration(800);
            objectAnimator3.setDuration(800);
            objectAnimator6.setDuration(800);
            objectAnimator7.setDuration(800);
            objectAnimator8.setDuration(800);
            objectAnimator9.setDuration(800);
            objectAnimator2.setDuration(800);
            objectAnimator.setDuration(800);

            objectAnimator1a.setDuration(800);
            objectAnimator2a.setDuration(800);
            objectAnimator3aa.setDuration(800);
            objectAnimator3a.setDuration(800);
            objectAnimator4a.setDuration(800);
            objectAnimator5a.setDuration(800);
            objectAnimator6a.setDuration(800);
            objectAnimator6aa.setDuration(800);

            objectAnimator2.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator3.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator4.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator5.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator6.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator7.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator8.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator9.setInterpolator(new AnticipateOvershootInterpolator());

            objectAnimator1a.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator2a.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator3a.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator3aa.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator4a.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator5a.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator6a.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator6aa.setInterpolator(new AnticipateOvershootInterpolator());

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(objectAnimator, objectAnimator2, objectAnimator3, objectAnimator4, objectAnimator5, objectAnimator6, objectAnimator7, objectAnimator8, objectAnimator9, objectAnimator1a, objectAnimator2a, objectAnimator3a, objectAnimator3aa, objectAnimator4a, objectAnimator5a, objectAnimator6a, objectAnimator6aa);
            animatorSet.start();
            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    navDrawer.setVisibility(View.VISIBLE);
                    profileNav.setVisibility(View.VISIBLE);
                    sponsorNav.setVisibility(View.GONE);
//                    notifNav.setVisibility(View.VISIBLE);
                    contributorNav.setVisibility(View.VISIBLE);
                    facesmash.setVisibility(View.VISIBLE);
                    leaderboard.setVisibility(View.VISIBLE);
                    coreTeamNav.setVisibility(View.VISIBLE);
                    clubsNav.setVisibility(View.VISIBLE);

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

        } else {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
            valueAnimator.setDuration(1300);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (animation.getAnimatedFraction() <= 0.7f)
                        navAnim.setProgress(0.7f - animation.getAnimatedFraction());
                }
            });
            valueAnimator.start();
            check = true;
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(navDrawer, "alpha", 1, 0);


            ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(aboutNav, "alpha", 1, 0);
            ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(profileNav, "alpha", 1, 0);
            ObjectAnimator objectAnimator4 = ObjectAnimator.ofFloat(settingNav, "alpha", 1, 0);
            ObjectAnimator objectAnimator5 = ObjectAnimator.ofFloat(sponsorNav, "alpha", 1, 0);

            ObjectAnimator objectAnimator6 = ObjectAnimator.ofFloat(aboutNav, "translationY", 0f, 15f);
            ObjectAnimator objectAnimator7 = ObjectAnimator.ofFloat(profileNav, "translationY", 0f, 15f);
            ObjectAnimator objectAnimator8 = ObjectAnimator.ofFloat(settingNav, "translationY", 0f, 15f);
            ObjectAnimator objectAnimator9 = ObjectAnimator.ofFloat(sponsorNav, "translationY", 0f, 15f);

            ObjectAnimator objectAnimator4a = ObjectAnimator.ofFloat(callNav, "translationY", 0f, 15f);
            ObjectAnimator objectAnimator5a = ObjectAnimator.ofFloat(mapNav, "translationY", 0f, 15f);
            ObjectAnimator objectAnimator6a = ObjectAnimator.ofFloat(notifNav, "translationY", 0f, 15f);
            ObjectAnimator objectAnimator6aa = ObjectAnimator.ofFloat(hillffairNav, "translationY", 0f, 15f);

            ObjectAnimator objectAnimator1a = ObjectAnimator.ofFloat(mapNav, "alpha", 1, 0);
            ObjectAnimator objectAnimator2a = ObjectAnimator.ofFloat(notifNav, "alpha", 1, 0);
            ObjectAnimator objectAnimator3aa = ObjectAnimator.ofFloat(hillffairNav, "alpha", 1, 0);
            ObjectAnimator objectAnimator3a = ObjectAnimator.ofFloat(callNav, "alpha", 1, 0);

            objectAnimator.setDuration(250);
            objectAnimator5.setDuration(250);
            objectAnimator4.setDuration(250);
            objectAnimator3.setDuration(250);
            objectAnimator6.setDuration(250);
            objectAnimator7.setDuration(250);
            objectAnimator8.setDuration(250);
            objectAnimator9.setDuration(250);
            objectAnimator2.setDuration(250);

            objectAnimator1a.setDuration(250);
            objectAnimator2a.setDuration(250);
            objectAnimator3a.setDuration(250);
            objectAnimator3aa.setDuration(250);
            objectAnimator4a.setDuration(250);
            objectAnimator5a.setDuration(250);
            objectAnimator6a.setDuration(250);
            objectAnimator6aa.setDuration(250);

            objectAnimator1a.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator2a.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator3a.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator3aa.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator4a.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator5a.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator6a.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator6aa.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator2.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator3.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator4.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator5.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator6.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator7.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator8.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator9.setInterpolator(new AnticipateOvershootInterpolator());

            objectAnimator.setStartDelay(0);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(objectAnimator, objectAnimator2, objectAnimator3, objectAnimator4, objectAnimator5, objectAnimator6, objectAnimator7, objectAnimator8, objectAnimator9, objectAnimator1a, objectAnimator2a, objectAnimator3a, objectAnimator3aa, objectAnimator4a, objectAnimator5a, objectAnimator6a, objectAnimator6aa);
            animatorSet.start();
            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    navDrawer.setVisibility(View.GONE);
                    profileNav.setVisibility(View.GONE);
                    aboutNav.setVisibility(View.GONE);
                    settingNav.setVisibility(View.GONE);
                    sponsorNav.setVisibility(View.GONE);
                    callNav.setVisibility(View.GONE);
//                    notifNav.setVisibility(View.GONE);
                    hillffairNav.setVisibility(View.GONE);
                    mapNav.setVisibility(View.GONE);
                    facesmash.setVisibility(View.GONE);
                    leaderboard.setVisibility(View.GONE);
                    coreTeamNav.setVisibility(View.GONE);
                    clubsNav.setVisibility(View.GONE);

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (check)
        super.onBackPressed();
        else
        {
            check = true;
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
            valueAnimator.setDuration(1300);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (animation.getAnimatedFraction() <= 0.7f)
                        navAnim.setProgress(0.7f - animation.getAnimatedFraction());
                }
            });
            valueAnimator.start();
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(navDrawer, "alpha", 1, 0);


            ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(aboutNav, "alpha", 1, 0);
            ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(profileNav, "alpha", 1, 0);
            ObjectAnimator objectAnimator4 = ObjectAnimator.ofFloat(settingNav, "alpha", 1, 0);
            ObjectAnimator objectAnimator5 = ObjectAnimator.ofFloat(sponsorNav, "alpha", 1, 0);

            ObjectAnimator objectAnimator6 = ObjectAnimator.ofFloat(aboutNav, "translationY", 0f, 15f);
            ObjectAnimator objectAnimator7 = ObjectAnimator.ofFloat(profileNav, "translationY", 0f, 15f);
            ObjectAnimator objectAnimator8 = ObjectAnimator.ofFloat(settingNav, "translationY", 0f, 15f);
            ObjectAnimator objectAnimator9 = ObjectAnimator.ofFloat(sponsorNav, "translationY", 0f, 15f);

            ObjectAnimator objectAnimator4a = ObjectAnimator.ofFloat(callNav, "translationY", 0f, 15f);
            ObjectAnimator objectAnimator5a = ObjectAnimator.ofFloat(mapNav, "translationY", 0f, 15f);
            ObjectAnimator objectAnimator6a = ObjectAnimator.ofFloat(notifNav, "translationY", 0f, 15f);
            ObjectAnimator objectAnimator6aa = ObjectAnimator.ofFloat(hillffairNav, "translationY", 0f, 15f);

            ObjectAnimator objectAnimator1a = ObjectAnimator.ofFloat(mapNav, "alpha", 1, 0);
            ObjectAnimator objectAnimator2a = ObjectAnimator.ofFloat(notifNav, "alpha", 1, 0);
            ObjectAnimator objectAnimator3aa = ObjectAnimator.ofFloat(hillffairNav, "alpha", 1, 0);
            ObjectAnimator objectAnimator3a = ObjectAnimator.ofFloat(callNav, "alpha", 1, 0);

            objectAnimator.setDuration(250);
            objectAnimator5.setDuration(250);
            objectAnimator4.setDuration(250);
            objectAnimator3.setDuration(250);
            objectAnimator6.setDuration(250);
            objectAnimator7.setDuration(250);
            objectAnimator8.setDuration(250);
            objectAnimator9.setDuration(250);
            objectAnimator2.setDuration(250);

            objectAnimator1a.setDuration(250);
            objectAnimator2a.setDuration(250);
            objectAnimator3a.setDuration(250);
            objectAnimator3aa.setDuration(250);
            objectAnimator4a.setDuration(250);
            objectAnimator5a.setDuration(250);
            objectAnimator6a.setDuration(250);
            objectAnimator6aa.setDuration(250);

            objectAnimator1a.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator2a.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator3a.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator3aa.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator4a.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator5a.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator6a.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator6aa.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator2.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator3.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator4.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator5.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator6.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator7.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator8.setInterpolator(new AnticipateOvershootInterpolator());
            objectAnimator9.setInterpolator(new AnticipateOvershootInterpolator());

            objectAnimator.setStartDelay(0);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(objectAnimator, objectAnimator2, objectAnimator3, objectAnimator4, objectAnimator5, objectAnimator6, objectAnimator7, objectAnimator8, objectAnimator9, objectAnimator1a, objectAnimator2a, objectAnimator3a, objectAnimator3aa, objectAnimator4a, objectAnimator5a, objectAnimator6a, objectAnimator6aa);
            animatorSet.start();
            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    navDrawer.setVisibility(View.GONE);
                    profileNav.setVisibility(View.GONE);
                    aboutNav.setVisibility(View.GONE);
                    settingNav.setVisibility(View.GONE);
                    sponsorNav.setVisibility(View.GONE);
                    callNav.setVisibility(View.GONE);
//                    notifNav.setVisibility(View.GONE);
                    hillffairNav.setVisibility(View.GONE);
                    mapNav.setVisibility(View.GONE);
                    facesmash.setVisibility(View.GONE);
                    leaderboard.setVisibility(View.GONE);
                    coreTeamNav.setVisibility(View.GONE);
                    clubsNav.setVisibility(View.GONE);

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }
}
