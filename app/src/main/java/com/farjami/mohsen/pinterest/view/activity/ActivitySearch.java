package com.farjami.mohsen.pinterest.view.activity;

import android.content.IntentFilter;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.farjami.mohsen.pinterest.R;
import com.farjami.mohsen.pinterest.adapter.MainPostsAdapter;
import com.farjami.mohsen.pinterest.api_services.PostsApiService;
import com.farjami.mohsen.pinterest.data_model.Post;
import com.farjami.mohsen.pinterest.system.ConnectivityListener;
import com.farjami.mohsen.pinterest.view.custom_views.recycler_view_anim.adapters.ScaleInAnimationAdapter;
import com.farjami.mohsen.pinterest.view.custom_views.recycler_view_anim.adapters.SlideInBottomAnimationAdapter;
import com.farjami.mohsen.pinterest.view.my_views.MyViews;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ActivitySearch extends AppCompatActivity {

  ImageView img_back;
  ImageView img_search;
  EditText edt_search;
  RecyclerView rcv_search_posts;
  ProgressBar prg_search;
  TextView txt_no_result;

  PostsApiService apiService;

  private Snackbar snackbar;
  private ConnectivityListener connectivityListener;

  CoordinatorLayout lyt_search;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);

    img_back= (ImageView) findViewById(R.id.img_back);
    img_search= (ImageView) findViewById(R.id.img_search);
    edt_search= (EditText) findViewById(R.id.edt_search);
    rcv_search_posts= (RecyclerView) findViewById(R.id.rcv_search_posts);
    prg_search= (ProgressBar) findViewById(R.id.prg_search);
    txt_no_result= (TextView) findViewById(R.id.txt_no_result);
    lyt_search= (CoordinatorLayout) findViewById(R.id.lyt_search);


    rcv_search_posts.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
    //rcv_posts.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
    //rcv_posts.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

    img_back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });


    img_search.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(checkEntry()){
          txt_no_result.setVisibility(View.GONE);
          rcv_search_posts.setVisibility(View.INVISIBLE);
          prg_search.setVisibility(View.VISIBLE);

          String text = edt_search.getText().toString();
          apiService = new PostsApiService(ActivitySearch.this);
          JSONObject jsonObject = new JSONObject();
          try {
            jsonObject.put("text", text);
          } catch (JSONException e) {
            e.printStackTrace();
          }
          apiService.getSearchedPosts(jsonObject, new PostsApiService.onSearchPostsReceived() {
            @Override
            public void onReceived(List<Post> posts) {
              prg_search.setVisibility(View.GONE);

              if(posts.size() < 1){
                txt_no_result.setVisibility(View.VISIBLE);
              }else {
                txt_no_result.setVisibility(View.GONE);
                rcv_search_posts.setVisibility(View.VISIBLE);
                MainPostsAdapter adapter = new MainPostsAdapter(ActivitySearch.this, posts);
                SlideInBottomAnimationAdapter alphaAdapter = new SlideInBottomAnimationAdapter(adapter);
                rcv_search_posts.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));

              }


            }
          });
        }



      }
    });

    edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {


          if(checkEntry()){
            txt_no_result.setVisibility(View.GONE);
            rcv_search_posts.setVisibility(View.INVISIBLE);
            prg_search.setVisibility(View.VISIBLE);

            String text = edt_search.getText().toString();
            apiService = new PostsApiService(ActivitySearch.this);
            JSONObject jsonObject = new JSONObject();
            try {
              jsonObject.put("text", text);
            } catch (JSONException e) {
              e.printStackTrace();
            }
            apiService.getSearchedPosts(jsonObject, new PostsApiService.onSearchPostsReceived() {
              @Override
              public void onReceived(List<Post> posts) {
                prg_search.setVisibility(View.GONE);

                if(posts.size() < 1){
                  txt_no_result.setVisibility(View.VISIBLE);
                }else {
                  txt_no_result.setVisibility(View.GONE);
                  rcv_search_posts.setVisibility(View.VISIBLE);
                  MainPostsAdapter adapter = new MainPostsAdapter(ActivitySearch.this, posts);
                  SlideInBottomAnimationAdapter alphaAdapter = new SlideInBottomAnimationAdapter(adapter);
                  rcv_search_posts.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));

                }


              }
            });
          }


          return true;
        }
        return false;
      }
    });

  }

  private boolean checkEntry(){

    if(edt_search.getText().toString().length() < 2){
      MyViews.makeText(ActivitySearch.this,"your text to search must has more than 1 character", Toast.LENGTH_SHORT);
      return false;
    }
    return true;
  }






  @Override
  protected void onStart() {
    super.onStart();

    connectivityListener = new ConnectivityListener(lyt_search, snackbar);
    registerReceiver(connectivityListener,new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

  }

  @Override
  protected void onStop() {
    super.onStop();

    unregisterReceiver(connectivityListener);
  }

}
