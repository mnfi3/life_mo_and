package net.lifemo.view.my_views;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.farjami.mohsen.pinterest.R;

public class MyViews extends AppCompatActivity {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }


  public static void makeText(AppCompatActivity appCompatActivity, CharSequence text, int duration){

   // Activity activity=appCompatActivity;
    //Context context=appCompatActivity;
    //AppCompatActivity appCompatActivity=context;
    ContextWrapper contextWrapper=new ContextWrapper(appCompatActivity);
    LayoutInflater inflater= appCompatActivity.getLayoutInflater();
    View layout = inflater.inflate(R.layout.custom_view_toast,
      (ViewGroup) appCompatActivity.findViewById(R.id.lyt_toast));
    TextView txt_toast_text = (TextView) layout.findViewById(R.id.txt_toast_text);
    txt_toast_text.setText(text);

    Toast toast = new Toast(contextWrapper.getApplicationContext());
    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
    toast.setDuration(Toast.LENGTH_LONG);
    toast.setView(layout);
    toast.show();
  }

  public static Typeface getIranSansFont(Context context){
    return Typeface.createFromAsset(context.getAssets(), "fonts/IRAN_Sans.ttf");
  }
  public static Typeface getIranSansBoldFont(Context context){
    return Typeface.createFromAsset(context.getAssets(), "fonts/IRAN_Sans_Bold.ttf");
  }
  public static Typeface getIranSansLightFont(Context context){
    return Typeface.createFromAsset(context.getAssets(), "fonts/IRAN_Sans_Light.ttf");
  }
  public static Typeface getIranSansMediumFont(Context context){
    return Typeface.createFromAsset(context.getAssets(), "fonts/IRAN_Sans_Medium.ttf");
  }
  public static Typeface getIranSansUltraLightFont(Context context){
    return Typeface.createFromAsset(context.getAssets(), "fonts/IRAN_Sans_UltraLight.ttf");
  }
}
