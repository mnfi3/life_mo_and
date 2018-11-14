package com.farjami.mohsen.pinterest.view.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.farjami.mohsen.pinterest.R;
import com.farjami.mohsen.pinterest.adapter.RelatedPostsAdapter;
import com.farjami.mohsen.pinterest.api_services.AccountApiService;
import com.farjami.mohsen.pinterest.api_services.PostsApiService;
import com.farjami.mohsen.pinterest.data_model.Post;
import com.farjami.mohsen.pinterest.data_model.Tag;
import com.farjami.mohsen.pinterest.data_model.User;
import com.farjami.mohsen.pinterest.system.ConnectivityListener;
import com.farjami.mohsen.pinterest.system.G;
import com.farjami.mohsen.pinterest.system.UserSharedPrefManager;
import com.farjami.mohsen.pinterest.view.custom_views.recycler_view_anim.adapters.ScaleInAnimationAdapter;
import com.farjami.mohsen.pinterest.view.custom_views.recycler_view_anim.adapters.SlideInBottomAnimationAdapter;
import com.farjami.mohsen.pinterest.view.my_views.MyViews;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ActivityShowPost extends AppCompatActivity {

  ImageView img_back;
  TextView txt_page_name;
  TextView txt_user_name;
  Button btn_share_post;
  Button btn_save_post;
  ImageView img_post_image;
  TextView txt_post_description;
  TextView txt_tags_of_post;
  RecyclerView rcv_posts_in_show_post;
  ProgressBar prg_show_post_info;

  PostsApiService apiService;


  private Snackbar snackbar;
  private ConnectivityListener connectivityListener;
  CoordinatorLayout lyt_show_post;

  int id = 0;
  String image = "" ;
  String main_image_url = "" ;
  String description = "";

  boolean isLoggedIn = false;
  UserSharedPrefManager prefManager;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_show_post);

    img_back= (ImageView) findViewById(R.id.img_back);
    txt_page_name= (TextView) findViewById(R.id.txt_page_name);
    txt_user_name= (TextView) findViewById(R.id.txt_user_name);
    btn_share_post= (Button) findViewById(R.id.btn_share_post);
    btn_save_post= (Button) findViewById(R.id.btn_save_post);
    img_post_image= (ImageView) findViewById(R.id.img_post_image);
    txt_post_description= (TextView) findViewById(R.id.txt_post_description);
    txt_tags_of_post= (TextView) findViewById(R.id.txt_tags_of_post);
    rcv_posts_in_show_post= (RecyclerView) findViewById(R.id.rcv_posts_in_show_post);
    prg_show_post_info= (ProgressBar) findViewById(R.id.prg_show_post_info);
    lyt_show_post= (CoordinatorLayout) findViewById(R.id.lyt_show_post);

    checkLoggedIn();


    txt_page_name.setText("Photo");

    prg_show_post_info.setVisibility(View.VISIBLE);

    img_back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });








    btn_share_post.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(!isLoggedIn){
          MyViews.makeText(ActivityShowPost.this, "Share This Photo Failed . You Should Login", Toast.LENGTH_SHORT);
          return;
        }else {

          try {
            sharePostImage();

            UserSharedPrefManager prefManager = new UserSharedPrefManager(ActivityShowPost.this);
            User user = prefManager.getUser();
            String token = user.getServer_token();
            String client_key = user.getClient_key();
            int post_id = id;
            JSONObject jsonObject = new JSONObject();
            try {
              jsonObject.put("token", token);
              jsonObject.put("client_key", client_key);
              jsonObject.put("post_id", post_id);
            } catch (JSONException e) {
              e.printStackTrace();
            }

            apiService = new PostsApiService(ActivityShowPost.this);
            apiService.addToSharedPosts(jsonObject, new PostsApiService.onAddToSharedPostsReceived() {
              @Override
              public void onReceived(int success) {

              }
            });

          } catch (IOException e) {
            e.printStackTrace();
          }

        }
          //MyViews.makeText(ActivityShowPost.this, "error check bmp", Toast.LENGTH_SHORT);
      }
    });


    btn_save_post.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if(!isLoggedIn){
            MyViews.makeText(ActivityShowPost.this, "Save This Post Failed . You Should Login", Toast.LENGTH_SHORT);
            return;
          }else {

            UserSharedPrefManager prefManager = new UserSharedPrefManager(ActivityShowPost.this);
            User user = prefManager.getUser();
            String token = user.getServer_token();
            String client_key = user.getClient_key();
            int post_id = id;
            JSONObject jsonObject = new JSONObject();
            try {
              jsonObject.put("token", token);
              jsonObject.put("client_key", client_key);
              jsonObject.put("post_id", post_id);
            } catch (JSONException e) {
              e.printStackTrace();
            }

            apiService = new PostsApiService(ActivityShowPost.this);
            apiService.addToSavedPosts(jsonObject, new PostsApiService.onAddToSavedPostsReceived() {
              @Override
              public void onReceived(int success) {
                String message = "";
                if (success == 1) {
                  message = "This Photo Added In Your Saved Photos";
                } else {
                  message = "Failed To Add This Photo In Your Saved Photos . Maybe Already You Added This!";
                }
                MyViews.makeText(ActivityShowPost.this, message, Toast.LENGTH_SHORT);
              }
            });

          }
        }
      });

//    intent=getIntent();
//    id=intent.getIntExtra("ID",0);
//    image_url=intent.getStringExtra("IMAGE");
//    name=intent.getStringExtra("NAME");
//    price=intent.getIntExtra("PRICE",0);
//    count=intent.getIntExtra("COUNT",0);


    Post post = new Post();
    if (savedInstanceState == null) {
      Bundle extras = getIntent().getExtras();
      if(extras != null) {
        id = extras.getInt("ID");
        image = extras.getString("IMAGE");
        main_image_url = extras.getString("MAIN_IMAGE");
        description = extras.getString("DESCRIPTION");

        post.setId(id) ;
        post.setImageUrl(image) ;
        post.setDescription(description) ;
        //post.setTags(extras.get("TAGS")) ;
      }
    } else {
      id = (int) savedInstanceState.getSerializable("ID");
      image = (String) savedInstanceState.getSerializable("IMAGE");
      main_image_url = (String) savedInstanceState.getSerializable("MAIN_IMAGE");
      description = (String) savedInstanceState.getSerializable("DESCRIPTION");

      post.setId(id) ;
      post.setImageUrl(image) ;
      post.setDescription(description);
    }

    txt_post_description.setText(description);


    //load image with low quality
//    Picasso.with(ActivityShowPost.this).load(image).into(img_post_image);
    //img_post_image.getLayoutParams().height= ViewGroup.LayoutParams.MATCH_PARENT;

    //LOAD MAIN IMAGE
    Picasso.with(ActivityShowPost.this).load(main_image_url)
//      .noFade()
//      .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
//      .networkPolicy(NetworkPolicy.NO_CACHE)
//      .skipMemoryCache()
      .fetch(new Callback(){
        @Override
        public void onSuccess() {
          prg_show_post_info.setVisibility(View.GONE);
          img_post_image.getLayoutParams().height= ViewGroup.LayoutParams.MATCH_PARENT;
          img_post_image.setAlpha(0f);
          Picasso.with(ActivityShowPost.this).load(main_image_url)
//            .noFade()
//            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
//            .skipMemoryCache()
            .into(img_post_image);
          img_post_image.animate().setDuration(700).alpha(1f).start();



        }

        @Override
        public void onError() {

        }
      });





    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put("id", id);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    apiService = new PostsApiService(ActivityShowPost.this);
    apiService.getSpecialPost(jsonObject, new PostsApiService.onSpecialPostReceived() {
      @Override
      public void onReceived(Post post, String user_name) {

        txt_user_name.setText(user_name);
        List<Tag> tags = post.getTags();
        String tagsText = "";
        final String main_image = post.getMainImageUrl();



        //Picasso.with(ActivityShowPost.this).load(main_image).into(img_post_image);


//        Picasso.with(ActivityShowPost.this).load(main_image).noFade()
//          .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
//          .networkPolicy(NetworkPolicy.NO_CACHE)
//          .skipMemoryCache()
//          .fetch(new Callback(){
//          @Override
//          public void onSuccess() {
//            //img_post_image.getLayoutParams().height= ViewGroup.LayoutParams.MATCH_PARENT;
//            img_post_image.setAlpha(0f);
//            Picasso.with(ActivityShowPost.this).load(main_image).noFade()
//              .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
//              .skipMemoryCache()
//              .into(img_post_image);
//            img_post_image.animate().setDuration(900).alpha(1f).start();
//            prg_show_post_info.setVisibility(View.GONE);
//
//
//
//          }
//
//          @Override
//          public void onError() {
//
//          }
//        });






        for(int i=0 ;  i < tags.size() ; i++){
          tagsText += "#" + tags.get(i).getName() + " ";
        }

        txt_tags_of_post .setText(tagsText);

        //get related posts
        rcv_posts_in_show_post.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        rcv_posts_in_show_post.setNestedScrollingEnabled(false);
        getRelatedPosts();
      }


    });







  }

  private void getRelatedPosts(){
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put("id", id);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    apiService.getRelatedPosts(jsonObject, new PostsApiService.onRelatedPostsReceived() {
      @Override
      public void onReceived(List<Post> posts) {
        RelatedPostsAdapter adapter = new RelatedPostsAdapter(ActivityShowPost.this, posts);
        SlideInBottomAnimationAdapter alphaAdapter = new SlideInBottomAnimationAdapter(adapter);
        rcv_posts_in_show_post.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
      }
    });
  }











  private void checkLoggedIn(){
    prefManager = new UserSharedPrefManager(ActivityShowPost.this);
    User user;
    user = prefManager.getUser();

    String user_name = user.getUser_name();
    String token = user.getServer_token();
    String client_key = user.getClient_key();


    if( !(client_key .equals( G.getClientKey(user_name)))){
      //Log.i(TAG, "checkLoginWithToken: "+"step1");
      isLoggedIn = false;
      return;
    }

//    if(checkLoginWithToken()){
//      isLoggedIn = true;
//    }else {
//      isLoggedIn = false;
//    }

    AccountApiService apiService = new AccountApiService(ActivityShowPost.this);
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
  }


  private void sharePostImage() throws IOException {
    img_post_image.setDrawingCacheEnabled(true);
    Bitmap bitmap = img_post_image.getDrawingCache();

//To share it via Intent**
    try {
      String fileName = G.APP_NAME + "_" + Integer.toString(id);
      File file = new File(ActivityShowPost.this.getCacheDir(), fileName + ".png");
      FileOutputStream fOut = new FileOutputStream(file);
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
      fOut.flush();
      fOut.close();
      file.setReadable(true, false);
      final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
      intent.setType("image/png");
      startActivity(intent);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }



  protected void onResume() {
    super.onResume();
    checkLoggedIn();
  }

  @Override
  protected void onStart() {
    super.onStart();
    checkLoggedIn();

    connectivityListener = new ConnectivityListener(lyt_show_post, snackbar);
    registerReceiver(connectivityListener,new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
  }

  @Override
  protected void onStop() {
    super.onStop();
    checkLoggedIn();

    unregisterReceiver(connectivityListener);
  }





}
