package com.farjami.mohsen.pinterest.view.activity;

import android.content.IntentFilter;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.farjami.mohsen.pinterest.R;
import com.farjami.mohsen.pinterest.adapter.SavedPostsAdapter;
import com.farjami.mohsen.pinterest.adapter.UserPostsAdapter;
import com.farjami.mohsen.pinterest.api_services.PostsApiService;
import com.farjami.mohsen.pinterest.data_model.Post;
import com.farjami.mohsen.pinterest.data_model.User;
import com.farjami.mohsen.pinterest.system.ConnectivityListener;
import com.farjami.mohsen.pinterest.system.UserSharedPrefManager;
import com.farjami.mohsen.pinterest.view.custom_views.recycler_view_anim.adapters.ScaleInAnimationAdapter;
import com.farjami.mohsen.pinterest.view.custom_views.recycler_view_anim.adapters.SlideInBottomAnimationAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ActivityMyPosts extends AppCompatActivity {

  ImageView img_back;
  TextView txt_page_name;
  RecyclerView rcv_my_posts;
  ProgressBar prg_show_my_post;

  UserSharedPrefManager prefManager;

  private Snackbar snackbar;
  private ConnectivityListener connectivityListener;
  CoordinatorLayout lyt_my_posts;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_posts);

    img_back = (ImageView) findViewById(R.id.img_back);
    txt_page_name = (TextView) findViewById(R.id.txt_page_name);
    rcv_my_posts = (RecyclerView) findViewById(R.id.rcv_my_posts);
    lyt_my_posts = (CoordinatorLayout) findViewById(R.id.lyt_my_posts);
    prg_show_my_post = (ProgressBar) findViewById(R.id.prg_show_my_post);




    img_back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });



    //rcv_my_posts.setLayoutManager(new GridLayoutManager(this,1,GridLayoutManager.VERTICAL,false));
   // rcv_my_posts.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
    rcv_my_posts.setLayoutManager(new GridLayoutManager(this,1,GridLayoutManager.VERTICAL,false));


    prefManager = new UserSharedPrefManager(ActivityMyPosts.this);
    User user;
    user = prefManager.getUser();

    String user_name = user.getUser_name();
    txt_page_name.setText(user_name);

    String token = user.getServer_token();
    String client_key = user.getClient_key();


    PostsApiService apiService = new PostsApiService(ActivityMyPosts.this);
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put("token", token);
      jsonObject.put("client_key", client_key);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    prg_show_my_post.setVisibility(View.VISIBLE);

    if(token.length() > 2 && client_key.length() >2){
      apiService.getUserPosts(jsonObject, new PostsApiService.onUserPostsReceived() {
        @Override
        public void onReceived(List<Post> posts) {
          prg_show_my_post.setVisibility(View.GONE);
          UserPostsAdapter adapter = new UserPostsAdapter(ActivityMyPosts.this, posts);
          SlideInBottomAnimationAdapter alphaAdapter = new SlideInBottomAnimationAdapter(adapter);
          rcv_my_posts.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
        }
      });
    }










  }



  @Override
  protected void onStart() {
    super.onStart();

    connectivityListener = new ConnectivityListener(lyt_my_posts, snackbar);
    registerReceiver(connectivityListener, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

  }

  @Override
  protected void onStop() {
    super.onStop();

    unregisterReceiver(connectivityListener);
  }


}
