package com.farjami.mohsen.pinterest.view.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.farjami.mohsen.pinterest.R;
import com.farjami.mohsen.pinterest.api_services.AccountApiService;
import com.farjami.mohsen.pinterest.data_model.User;
import com.farjami.mohsen.pinterest.system.ConnectivityListener;
import com.farjami.mohsen.pinterest.system.G;
import com.farjami.mohsen.pinterest.system.UserSharedPrefManager;
import com.farjami.mohsen.pinterest.view.my_views.MyViews;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityLogin extends AppCompatActivity {

  ImageView img_back;
  TextView txt_page_name;
  EditText edt_user_name;
  EditText edt_password;
  CheckBox chk_show_pass;
  TextView txt_forget_password;
  Button btn_sign_in;
  ProgressBar prg_login;
  Button btn_sign_up;

  AccountApiService apiService ;
  UserSharedPrefManager prefManager;

  private Snackbar snackbar;
  private ConnectivityListener connectivityListener;
  LinearLayout lyt_login;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);


    img_back= (ImageView) findViewById(R.id.img_back);
    txt_page_name= (TextView) findViewById(R.id.txt_page_name);
    edt_user_name = (EditText) findViewById(R.id.edt_user_name);
    edt_password= (EditText) findViewById(R.id.edt_password);
    chk_show_pass= (CheckBox) findViewById(R.id.chk_show_pass);
    txt_forget_password= (TextView) findViewById(R.id.txt_forget_password);
    btn_sign_in= (Button) findViewById(R.id.btn_sign_in);
    prg_login= (ProgressBar) findViewById(R.id.prg_login);
    btn_sign_up= (Button) findViewById(R.id.btn_sign_up);
    lyt_login= (LinearLayout) findViewById(R.id.lyt_login);



    if (savedInstanceState == null) {
      Bundle extras = getIntent().getExtras();
      if(extras != null) {
        String user_name= extras.getString("USER_NAME");
        String password= extras.getString("PASSWORD");
        edt_user_name.setText(user_name);
        edt_password.setText(password);
      }
    } else {
      String user_name= (String) savedInstanceState.getSerializable("USER_NAME");
      String password= (String) savedInstanceState.getSerializable("PASSWORD");
      edt_user_name.setText(user_name);
      edt_password.setText(password);
    }





    txt_page_name.setText("Login");

    img_back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    txt_forget_password.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(ActivityLogin.this, ActivityRecoveryPassword.class);
        startActivity(intent);
      }
    });


    btn_sign_up.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(ActivityLogin.this, ActivitySignup.class);
        startActivity(intent);
      }
    });

    chk_show_pass.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(chk_show_pass.isChecked()){
          edt_password.setInputType(InputType.TYPE_CLASS_TEXT);
        }else if(!chk_show_pass.isChecked()) {
          edt_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
        }
      }
    });

    btn_sign_in.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final String user_name=edt_user_name.getText().toString();
        String password=edt_password.getText().toString();
        password= G.getHashedString(G.getHashedString(password )+ G.SALT);

        if(checkEntry()){

          prg_login.setVisibility(View.VISIBLE);

          JSONObject jsonObject = new JSONObject();
          try {
            jsonObject.put("user_name", user_name);
            jsonObject.put("password", password);
          } catch (JSONException e) {
            e.printStackTrace();
          }

          apiService = new AccountApiService(ActivityLogin.this);
          apiService.signInUser(jsonObject, new AccountApiService.onSignInComplete() {
            @Override
            public void onSignIn(String token) {
              prg_login.setVisibility(View.INVISIBLE);
              String message;
              if(token.length() > 3){
                saveUser(user_name, token, G.getClientKey(user_name));
                //G.isLoggedIn = true;
                message = "You Are Logged In Successfully.";
                Intent intent = new Intent(ActivityLogin.this, ActivityMain.class);
                startActivity(intent);
                ActivityLogin.this.finish();

              }else{
                message = "Login Failed . Please Check Your User Name And Password And Try Again";
              }
              MyViews.makeText(ActivityLogin.this, message, Toast.LENGTH_LONG);
            }
          });

        }
      }
    });







  }


  private boolean checkEntry(){

    if(edt_user_name.getText().toString().length()<4 || edt_password.getText().toString().length()<4){
      MyViews.makeText(ActivityLogin.this,"your user name and password must be have more than 3 characters", Toast.LENGTH_SHORT);
      return false;
    }
    return true;
  }

  private void saveUser(String user_name,String token,String client_key){
    prefManager=new UserSharedPrefManager(ActivityLogin.this);
    User user = new User();
    user.setUser_name(user_name);
    user.setServer_token(token);
    user.setClient_key(client_key);

    prefManager.saveUser(user);
   // MyViews.makeText(ActivityLogin.this,"You are Logged In Successfully .",Toast.LENGTH_LONG);
  }


  protected void onStart() {
    super.onStart();

    connectivityListener = new ConnectivityListener(lyt_login, snackbar);
    registerReceiver(connectivityListener,new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

  }

  @Override
  protected void onStop() {
    super.onStop();

    unregisterReceiver(connectivityListener);
  }


}
