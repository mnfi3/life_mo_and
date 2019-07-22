package net.lifemo.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
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
import net.lifemo.api_services.AccountApiService;
import net.lifemo.system.ConnectivityListener;
import net.lifemo.system.G;
import net.lifemo.view.my_views.MyViews;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class ActivitySignup extends AppCompatActivity {

  ImageView img_back;
  TextView  txt_page_name;
  EditText  edt_email;
  EditText  edt_user_name;
  EditText  edt_password;
  EditText  edt_repeat_password;
  CheckBox  chk_show_pass;
  Button    btn_sign_up;
  ProgressBar prg_sign_up;

  AccountApiService apiService;


  private Snackbar snackbar;
  private ConnectivityListener connectivityListener;
  LinearLayout lyt_sign_up;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signup);


    img_back= (ImageView) findViewById(R.id.img_back);
    txt_page_name= (TextView) findViewById(R.id.txt_page_name);
    edt_email= (EditText) findViewById(R.id.edt_email);
    edt_user_name= (EditText) findViewById(R.id.edt_user_name);
    edt_password= (EditText) findViewById(R.id.edt_password);
    edt_repeat_password= (EditText) findViewById(R.id.edt_repeat_password);
    chk_show_pass= (CheckBox) findViewById(R.id.chk_show_pass);
    btn_sign_up= (Button) findViewById(R.id.btn_sign_up);
    prg_sign_up= (ProgressBar) findViewById(R.id.prg_sign_up);
    lyt_sign_up= (LinearLayout) findViewById(R.id.lyt_sign_up);



    txt_page_name.setText("Sign Up");

    img_back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    chk_show_pass.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(chk_show_pass.isChecked()){
          edt_password.setInputType(InputType.TYPE_CLASS_TEXT);
          edt_repeat_password.setInputType(InputType.TYPE_CLASS_TEXT);
        }else if(!chk_show_pass.isChecked()) {
          edt_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
          edt_repeat_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
        }
      }
    });


    btn_sign_up.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String email=edt_email.getText().toString();
        String user_name=edt_user_name.getText().toString();
        String password=edt_password.getText().toString();
        String client_key = G.getClientKey(user_name);
        password= G.getHashedString(G.getHashedString(password )+ G.SALT);

        if(checkEntry()){
          prg_sign_up.setVisibility(View.VISIBLE);
          apiService = new AccountApiService(ActivitySignup.this);
          JSONObject jsonObject = new JSONObject();

          try {
            jsonObject.put("email", email);
            jsonObject.put("user_name", user_name);
            jsonObject.put("password", password);
            jsonObject.put("client_key", client_key);
          } catch (JSONException e) {
            e.printStackTrace();
          }

          apiService.signUpUser(jsonObject, new AccountApiService.onSignUpComplete() {
            @Override
            public void onSignUp(String token) {
              prg_sign_up.setVisibility(View.INVISIBLE);
              if(token.length() > 3){
                String message="You Signed Up Successfully . Please Login";
                MyViews.makeText(ActivitySignup.this,message,Toast.LENGTH_SHORT);

                Intent intent=new Intent(ActivitySignup.this,ActivityLogin.class);
                intent.putExtra("USER_NAME",edt_user_name.getText().toString());
                intent.putExtra("PASSWORD",edt_password.getText().toString());
                startActivity(intent);
                ActivitySignup.this.finish();

              }else {
                String message="Please Check Your Email and User Name . Maybe This User Name Or Email Was Exist!";
                MyViews.makeText(ActivitySignup.this,message,Toast.LENGTH_SHORT);
              }
            }
          });
        }
      }
    });


  }


  private boolean checkEntry(){

    if(edt_email.getText().toString().length()<1 || edt_user_name.getText().toString().length()<1 || edt_password.getText().toString().length()<1 || edt_repeat_password.getText().toString().length()<1){
      MyViews.makeText(ActivitySignup.this,"please enter your information", Toast.LENGTH_SHORT);
      return false;
    }

    else if(!(edt_password.getText().toString().equals(edt_repeat_password.getText().toString()))){
      MyViews.makeText(ActivitySignup.this,"your entered passwords not match",Toast.LENGTH_SHORT);
      return false;
    }

    else if( edt_email.getText().toString().length()<4){
      MyViews.makeText(ActivitySignup.this,"your email must be has more than 3 characters",Toast.LENGTH_SHORT);
      return false;
    }

    else if( edt_user_name.getText().toString().length()<4){
      MyViews.makeText(ActivitySignup.this,"your username must be has more than 3 characters",Toast.LENGTH_SHORT);
      return false;
    }

    else if( edt_password.getText().toString().length()<4){
      MyViews.makeText(ActivitySignup.this,"your password must be has more than 3 characters",Toast.LENGTH_SHORT);
      return false;
    }
    else if(!validEmail(edt_email.getText().toString())){
      MyViews.makeText(ActivitySignup.this,"your email not valid!",Toast.LENGTH_SHORT);
      return false;
    }

    return true;
  }


  private boolean validEmail(String email) {
    Pattern pattern = Patterns.EMAIL_ADDRESS;
    return pattern.matcher(email).matches();
  }







  @Override
  protected void onStart() {
    super.onStart();
    connectivityListener = new ConnectivityListener(lyt_sign_up, snackbar);
    registerReceiver(connectivityListener,new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
  }

  @Override
  protected void onStop() {
    super.onStop();
    unregisterReceiver(connectivityListener);
  }
}
