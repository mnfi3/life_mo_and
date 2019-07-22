package net.lifemo.view.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.farjami.mohsen.pinterest.R;
import net.lifemo.api_services.AccountApiService;
import net.lifemo.system.ConnectivityListener;
import net.lifemo.system.G;
import net.lifemo.view.my_views.MyViews;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class ActivityRecoveryPassword extends AppCompatActivity {

  ImageView img_back;
  TextView txt_page_name;
  EditText edt_email;
  Button btn_recovery_password;
  TextView txt_recovery_status;

  private Snackbar snackbar;
  private ConnectivityListener connectivityListener;
  LinearLayout lyt_recovery_password;



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recovery_password);

    img_back= (ImageView) findViewById(R.id.img_back);
    txt_page_name= (TextView) findViewById(R.id.txt_page_name);
    edt_email= (EditText) findViewById(R.id.edt_email);
    btn_recovery_password= (Button) findViewById(R.id.btn_recovery_password);
    txt_recovery_status= (TextView) findViewById(R.id.txt_recovery_status);
    lyt_recovery_password= (LinearLayout) findViewById(R.id.lyt_recovery_password);

    txt_page_name.setText("Recovery Your Password");

    img_back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });


    btn_recovery_password.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(G.PASSWORD_RESET_URL));
        startActivity(browserIntent);

//        if(!validEmail(edt_email.getText().toString())){
//          MyViews.makeText(ActivityRecoveryPassword.this,"your entered email not valid!", Toast.LENGTH_SHORT);
//          return;
//        }
//
//        String email = edt_email.getText().toString();
//
//        AccountApiService apiService = new AccountApiService(ActivityRecoveryPassword.this);
//        JSONObject jsonObject = new JSONObject();
//        try {
//          jsonObject.put("email",email);
//        } catch (JSONException e) {
//          e.printStackTrace();
//        }
//        apiService.recoveryPassword(jsonObject, new AccountApiService.onRecoveryPasswordComplete() {
//          @Override
//          public void onChangePassword(int success) {
//            if(success == 1){
//              //txt_recovery_status.setText("new password sent to your email . please check your email");
//              MyViews.makeText(ActivityRecoveryPassword.this,"password sent to your email . please check your email",Toast.LENGTH_SHORT);
//              finish();
//            }else {
//              MyViews.makeText(ActivityRecoveryPassword.this,"your password recovery failed . please try again",Toast.LENGTH_SHORT);
//            }
//          }
//        });
      }
    });

  }



  private boolean validEmail(String email) {
    Pattern pattern = Patterns.EMAIL_ADDRESS;
    return pattern.matcher(email).matches();
  }



  @Override
  protected void onStart() {
    super.onStart();

    connectivityListener = new ConnectivityListener(lyt_recovery_password, snackbar);
    registerReceiver(connectivityListener,new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

  }

  @Override
  protected void onStop() {
    super.onStop();

    unregisterReceiver(connectivityListener);
  }
}
