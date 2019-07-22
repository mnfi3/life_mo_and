package net.lifemo.view.activity;

import android.content.IntentFilter;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.farjami.mohsen.pinterest.R;
import net.lifemo.adapter.MainPostsAdapter;
import net.lifemo.adapter.SavedPostsAdapter;
import net.lifemo.data_model.Post;
import net.lifemo.api_services.PostsApiService;
import net.lifemo.data_model.User;
import net.lifemo.system.ConnectivityListener;
import net.lifemo.system.UserSharedPrefManager;
import net.lifemo.view.custom_views.recycler_view_anim.adapters.ScaleInAnimationAdapter;
import net.lifemo.view.custom_views.recycler_view_anim.adapters.SlideInBottomAnimationAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ActivitySaved extends AppCompatActivity {

  ImageView img_back;
  TextView txt_page_name;
  RecyclerView rcv_saved_posts;

  UserSharedPrefManager prefManager;

  private Snackbar snackbar;
  private ConnectivityListener connectivityListener;
  CoordinatorLayout lyt_saved;

  ProgressBar prg_saved;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_saved);

    img_back= (ImageView) findViewById(R.id.img_back);
    txt_page_name= (TextView) findViewById(R.id.txt_page_name);
    rcv_saved_posts= (RecyclerView) findViewById(R.id.rcv_saved_posts);
    lyt_saved= (CoordinatorLayout) findViewById(R.id.lyt_saved);
    prg_saved= (ProgressBar) findViewById(R.id.prg_saved);


    txt_page_name.setText("Saved Posts");

    prg_saved.setVisibility(View.VISIBLE);

    img_back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });




    rcv_saved_posts.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
    //rcv_posts.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
    //rcv_posts.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));


    prefManager = new UserSharedPrefManager(ActivitySaved.this);
    User user;
    user = prefManager.getUser();

    String token = user.getServer_token();
    String client_key = user.getClient_key();


    PostsApiService apiService = new PostsApiService(ActivitySaved.this);
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put("token", token);
      jsonObject.put("client_key", client_key);
    } catch (JSONException e) {
      e.printStackTrace();
    }


    if(token.length() > 2 && client_key.length() >2){
      apiService.getSavedPosts(jsonObject, new PostsApiService.onSavedPostsReceived() {
        @Override
        public void onReceived(List<Post> posts) {
          prg_saved.setVisibility(View.GONE);

          SavedPostsAdapter adapter = new SavedPostsAdapter(ActivitySaved.this, posts);
          SlideInBottomAnimationAdapter alphaAdapter = new SlideInBottomAnimationAdapter(adapter);
          rcv_saved_posts.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
        }
      });
    }



  }



  @Override
  protected void onStart() {
    super.onStart();

    connectivityListener = new ConnectivityListener(lyt_saved, snackbar);
    registerReceiver(connectivityListener,new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

  }

  @Override
  protected void onStop() {
    super.onStop();

    unregisterReceiver(connectivityListener);
  }
}
