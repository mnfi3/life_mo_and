package com.farjami.mohsen.pinterest.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.farjami.mohsen.pinterest.R;
import com.farjami.mohsen.pinterest.data_model.User;
import com.farjami.mohsen.pinterest.system.UserSharedPrefManager;
import com.farjami.mohsen.pinterest.view.my_views.MyViews;

public class ActivityAccount extends AppCompatActivity {

  ImageView img_back;
  TextView txt_page_name;
  Button btn_logout;
  Button btn_change_password;
  Button btn_my_posts;
  LinearLayout lyt_account;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_account);

    img_back= (ImageView) findViewById(R.id.img_back);
    txt_page_name= (TextView) findViewById(R.id.txt_page_name);
    btn_logout= (Button) findViewById(R.id.btn_logout);
    btn_change_password= (Button) findViewById(R.id.btn_change_password);
    btn_my_posts= (Button) findViewById(R.id.btn_my_posts);
    lyt_account= (LinearLayout) findViewById(R.id.lyt_account);

    txt_page_name.setText("Me");

    img_back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });


    btn_my_posts.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(ActivityAccount.this, ActivityMyPosts.class);
        startActivity(intent);
      }
    });

      btn_logout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          AlertDialog.Builder builder;
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(ActivityAccount.this, android.R.style.Theme_Material_Dialog_Alert);
          } else {
            builder = new AlertDialog.Builder(ActivityAccount.this);
          }

          builder.setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                // continue with delete
                saveUser("", "", "");
                MyViews.makeText(ActivityAccount.this, "You are Logged Out Successfully .", Toast.LENGTH_SHORT);
                finish();


              }
            })
            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                // do nothing
              }
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();


        }
      });


    btn_change_password.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(ActivityAccount.this, ActivityChangePassword.class);
        startActivity(intent);
      }
    });

  }


  private void saveUser(String user_name,String token,String client_key){
    UserSharedPrefManager prefManager=new UserSharedPrefManager(ActivityAccount.this);
    User user = new User();
    user.setUser_name(user_name);
    user.setServer_token(token);
    user.setClient_key(client_key);

    prefManager.saveUser(user);
    //MyViews.makeText(ActivityMain.this,"You are Logged Out Successfully .", Toast.LENGTH_LONG);
  }

}
