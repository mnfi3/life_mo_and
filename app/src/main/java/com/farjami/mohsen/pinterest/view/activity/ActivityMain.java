package com.farjami.mohsen.pinterest.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.farjami.mohsen.pinterest.R;
import com.farjami.mohsen.pinterest.adapter.MainPostsAdapter;
import com.farjami.mohsen.pinterest.adapter.OnBottomReachedListener;
import com.farjami.mohsen.pinterest.api_services.AccountApiService;
import com.farjami.mohsen.pinterest.data_model.Post;
import com.farjami.mohsen.pinterest.api_services.PostsApiService;
import com.farjami.mohsen.pinterest.data_model.User;
import com.farjami.mohsen.pinterest.system.ConnectivityListener;
import com.farjami.mohsen.pinterest.system.G;
import com.farjami.mohsen.pinterest.system.UserSharedPrefManager;
import com.farjami.mohsen.pinterest.view.custom_views.recycler_view_anim.adapters.ScaleInAnimationAdapter;
import com.farjami.mohsen.pinterest.view.custom_views.recycler_view_anim.adapters.SlideInBottomAnimationAdapter;
import com.farjami.mohsen.pinterest.view.my_views.MyViews;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityMain extends AppCompatActivity {

  private static final String TAG = "ActivityMain";

  RelativeLayout lyt_search;
  RecyclerView   rcv_posts;
  ImageView btn_login;
  ImageView btn_explore;
  ImageView btn_upload;
  ImageView btn_saved;
  LinearLayout lyt_bottom_buttons;
  ProgressBar prg_get_posts;
  CoordinatorLayout lyt_menu;
  private boolean isRefreshing = false;

  private int post_get_step = 0;

  private int last_id = 0;
  private ArrayList<Integer> last_ids = new ArrayList<Integer>();

  private Snackbar snackbar;
  private ConnectivityListener connectivityListener;


  private boolean isLoggedIn = true;

  UserSharedPrefManager prefManager;
  MainPostsAdapter adapter;

  SwipeRefreshLayout refreshLayout;



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);


    lyt_search= (RelativeLayout) findViewById(R.id.lyt_search);
    rcv_posts= (RecyclerView) findViewById(R.id.rcv_posts);
    btn_login= (ImageView) findViewById(R.id.btn_login);
    btn_explore= (ImageView) findViewById(R.id.btn_explore);
    btn_upload= (ImageView) findViewById(R.id.btn_upload);
    btn_saved= (ImageView) findViewById(R.id.btn_saved);
    lyt_bottom_buttons = (LinearLayout) findViewById(R.id.lyt_bottom_buttons);
    lyt_menu = (CoordinatorLayout) findViewById(R.id.lyt_menu);
    prg_get_posts = (ProgressBar) findViewById(R.id.prg_get_posts);

    refreshLayout = ((SwipeRefreshLayout) findViewById(R.id.swiperefresh));

    //delete app cache
    G.deleteCache(ActivityMain.this);

    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        isRefreshing = true;
        if(adapter != null){
          adapter.clear();
          last_ids.clear();
          last_id = 0;
          post_get_step = 0;
          getPosts();
        }
      }
    });


    checkLoggedIn();



    lyt_search.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(ActivityMain.this,ActivitySearch.class);
        startActivity(intent);
      }
    });

    btn_login.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(isLoggedIn){
          Intent intent = new Intent(ActivityMain.this, ActivityAccount.class);
          startActivity(intent);
        }else {
          Intent intent = new Intent(ActivityMain.this,ActivityLogin.class);
          startActivity(intent);
        }

      }
    });

    btn_explore.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(ActivityMain.this,ActivitySearch.class);
        startActivity(intent);
      }
    });

    btn_upload.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(isLoggedIn){
          Intent intent = new Intent(ActivityMain.this,ActivityUpload.class);
          startActivity(intent);
        }else {
          Intent intent = new Intent(ActivityMain.this,ActivityLogin.class);
          startActivity(intent);
        }


//        Intent intent = new Intent(ActivityMain.this,ActivityUpTest.class);
//        startActivity(intent);

      }
    });

    btn_saved.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(ActivityMain.this,ActivitySaved.class);
        startActivity(intent);
      }
    });


    rcv_posts.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
//    rcv_posts.setHasFixedSize(true);
//    rcv_posts.setItemViewCacheSize(20);
//    rcv_posts.setDrawingCacheEnabled(true);
//    rcv_posts.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

    getPosts();

    rcv_posts.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (!recyclerView.canScrollVertically(1)) {
          if(!last_ids.contains(last_id) && last_id > 5){
            getPosts();
          }
          //Log.i(TAG,"scroll finished");
        }
      }
    });





  }


  private void getPosts(){
    post_get_step++;
    if(!isRefreshing) {
      prg_get_posts.setVisibility(View.VISIBLE);
    }


    final PostsApiService apiService = new PostsApiService(ActivityMain.this);
    JSONObject  jsonObject = new JSONObject();
    try {
      jsonObject.put("id",last_id);
      jsonObject.put("count",12);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    last_ids.add(last_id);
    apiService.getMenuPosts(jsonObject, new PostsApiService.onMenuPostsReceived() {
      @Override
      public void onReceived(List<Post> posts) {
        prg_get_posts.setVisibility(View.GONE);
        refreshLayout.setRefreshing(false);
        isRefreshing = false;

        if (posts.size() > 0) {
          last_id = findLastId(posts);
        }

        if(post_get_step == 1) {
          adapter = new MainPostsAdapter(ActivityMain.this, posts);
          //rcv_posts.setAdapter(adapter);
          SlideInBottomAnimationAdapter alphaAdapter = new SlideInBottomAnimationAdapter(adapter);
          rcv_posts.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
        }else {

         // adapter.notifyItemInserted(posts.size() - 1);
          //adapter.notifyDataSetChanged();
          adapter.notifyData(posts);
//          SlideInBottomAnimationAdapter alphaAdapter = new SlideInBottomAnimationAdapter(adapter);
//          rcv_posts.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
        }


      }
    });
  }

  private boolean checkLoggedIn(){
    prefManager = new UserSharedPrefManager(ActivityMain.this);
    User user;
    user = prefManager.getUser();

    String user_name = user.getUser_name();
    String token = user.getServer_token();
    String client_key = user.getClient_key();


    if( !(client_key .equals( G.getClientKey(user_name)))){
      //Log.i(TAG, "checkLoginWithToken: "+"step1");
      isLoggedIn = false;
      return false;
    }

//    if(checkLoginWithToken()){
//      isLoggedIn = true;
//    }else {
//      isLoggedIn = false;
//    }

    AccountApiService apiService = new AccountApiService(ActivityMain.this);
    JSONObject requestJsonObject = new JSONObject();
    try {
      requestJsonObject.put("user_name", user_name);
      requestJsonObject.put("token", token);
      requestJsonObject.put("client_key", G.getClientKey(user_name));

      apiService.signInUserWithToken(requestJsonObject, new AccountApiService.onSignInWithTokenComplete() {
        @Override
        public void onSignInWithToken(int success) {
          if(success == 1){
            isLoggedIn = true;
          }else {
            isLoggedIn = false;
          }


        }
      });
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return isLoggedIn;


  }



  private int findLastId(List<Post> posts){
    if(posts.size() == 0){
      return 0;
    }
    int lastId = posts.get(0).getId();
    for (int i=1;i<posts.size();i++){
      if(posts.get(i).getId() < lastId){
        lastId = posts.get(i).getId();
      }
    }
    return lastId;
  }

  private void saveUser(String user_name,String token,String client_key){
    prefManager=new UserSharedPrefManager(ActivityMain.this);
    User user = new User();
    user.setUser_name(user_name);
    user.setServer_token(token);
    user.setClient_key(client_key);

    prefManager.saveUser(user);
    //MyViews.makeText(ActivityMain.this,"You are Logged Out Successfully .", Toast.LENGTH_LONG);
  }

  protected void onResume() {
    super.onResume();
    checkLoggedIn();
  }

  @Override
  protected void onStart() {
    super.onStart();
    checkLoggedIn();

    connectivityListener = new ConnectivityListener(lyt_menu, snackbar);
    registerReceiver(connectivityListener,new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

  }

  @Override
  protected void onStop() {
    super.onStop();
    checkLoggedIn();

    unregisterReceiver(connectivityListener);
  }
}
