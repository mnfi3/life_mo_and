package net.lifemo.view.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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
import net.lifemo.api_services.AccountApiService;
import net.lifemo.data_model.User;
import net.lifemo.system.ConnectivityListener;
import net.lifemo.system.G;
import net.lifemo.system.UserSharedPrefManager;
import net.lifemo.view.my_views.MyViews;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityChangePassword extends AppCompatActivity {

  ImageView img_back;
  TextView txt_page_name;
  EditText edt_user_name;
  EditText edt_old_password;
  EditText edt_new_password;
  CheckBox chk_show_pass;
  Button btn_change_password;
  ProgressBar prg_change_password;

  AccountApiService apiService ;
  UserSharedPrefManager prefManager;

  private Snackbar snackbar;
  private ConnectivityListener connectivityListener;
  LinearLayout lyt_change_password;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_change_password);


    img_back= (ImageView) findViewById(R.id.img_back);
    txt_page_name= (TextView) findViewById(R.id.txt_page_name);
    edt_user_name = (EditText) findViewById(R.id.edt_user_name);
    edt_old_password = (EditText) findViewById(R.id.edt_old_password);
    edt_new_password = (EditText) findViewById(R.id.edt_new_password);
    chk_show_pass= (CheckBox) findViewById(R.id.chk_show_pass);

    btn_change_password = (Button) findViewById(R.id.btn_change_password);
    lyt_change_password= (LinearLayout) findViewById(R.id.lyt_change_password);
    prg_change_password= (ProgressBar) findViewById(R.id.prg_change_password);



    if (savedInstanceState == null) {
      Bundle extras = getIntent().getExtras();
      if(extras != null) {
        String user_name= extras.getString("USER_NAME");
        String password= extras.getString("PASSWORD");
        edt_user_name.setText(user_name);
        edt_old_password.setText(password);
      }
    } else {
      String user_name= (String) savedInstanceState.getSerializable("USER_NAME");
      String password= (String) savedInstanceState.getSerializable("PASSWORD");
    edt_user_name.setText(user_name);
    edt_old_password.setText(password);
  }





  txt_page_name.setText("Change Password");

  img_back.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      finish();
    }
  });




  btn_change_password.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      Intent intent = new Intent(ActivityChangePassword.this, ActivitySignup.class);
      startActivity(intent);
    }
  });

  chk_show_pass.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      if(chk_show_pass.isChecked()){
        edt_old_password.setInputType(InputType.TYPE_CLASS_TEXT);
        edt_new_password.setInputType(InputType.TYPE_CLASS_TEXT);
      }else if(!chk_show_pass.isChecked()) {
        edt_old_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
        edt_new_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
      }
    }
  });

  btn_change_password.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      final String user_name=edt_user_name.getText().toString();
      String oldPassword= edt_old_password.getText().toString();
      String newPassword= edt_new_password.getText().toString();
      oldPassword= G.getHashedString(G.getHashedString(oldPassword )+ G.SALT);
      newPassword= G.getHashedString(G.getHashedString(newPassword )+ G.SALT);

      if(checkEntry()){

        prg_change_password.setVisibility(View.VISIBLE);

        JSONObject jsonObject = new JSONObject();
        try {
          jsonObject.put("user_name", user_name);
          jsonObject.put("old_password", oldPassword);
          jsonObject.put("new_password", newPassword);
        } catch (JSONException e) {
          e.printStackTrace();
        }

        apiService = new AccountApiService(ActivityChangePassword.this);
        apiService.changePassword(jsonObject, new AccountApiService.onChangePasswordComplete() {
          @Override
          public void onChangePassword(String token) {
            prg_change_password.setVisibility(View.INVISIBLE);
            String message;
            if(token.length() > 3){
              saveUser(user_name, token, G.getClientKey(user_name));
              //G.isLoggedIn = true;
              message = "Your Password Changed Successfully";
              ActivityChangePassword.this.finish();
            }else{
              message = "Change Password Failed . Please Check Your User Name And Passwords And Try Again";
            }
            MyViews.makeText(ActivityChangePassword.this, message, Toast.LENGTH_LONG);
          }
        });

      }
    }
  });







  }


  private boolean checkEntry(){

    if(edt_user_name.getText().toString().length()<4 || edt_old_password.getText().toString().length()<4 || edt_new_password.getText().toString().length()<4){
      MyViews.makeText(ActivityChangePassword.this,"your user name and passwords must be have more than 3 characters", Toast.LENGTH_SHORT);
      return false;
    }
    return true;
  }

  private void saveUser(String user_name,String token,String client_key){
    prefManager=new UserSharedPrefManager(ActivityChangePassword.this);
    User user = new User();
    user.setUser_name(user_name);
    user.setServer_token(token);
    user.setClient_key(client_key);

    prefManager.saveUser(user);
  }


  protected void onStart() {
    super.onStart();

    connectivityListener = new ConnectivityListener(lyt_change_password, snackbar);
    registerReceiver(connectivityListener,new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

  }

  @Override
  protected void onStop() {
    super.onStop();

    unregisterReceiver(connectivityListener);
  }


}
