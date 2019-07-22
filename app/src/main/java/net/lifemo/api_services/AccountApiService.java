package net.lifemo.api_services;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import net.lifemo.system.G;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountApiService {
  private Context context;
  public AccountApiService(Context context){
    this.context=context;
  }





  public void signUpUser(JSONObject requestJsonObject, final onSignUpComplete onSignUpComplete){
    String url="/v1/sign-up";

    final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, G.MAIN_URL + url, requestJsonObject, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {

        String token = "";
        int success = 0;
        try {
          success = response.getInt("success");
          if (success == 1){
            token = response.getString("token");
            onSignUpComplete.onSignUp(token);
          }else {
            onSignUpComplete.onSignUp("");
          }

        } catch (JSONException e) {
          e.printStackTrace();
        }


      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onSignUpComplete.onSignUp("");
      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(12000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }



  public void signInUser(JSONObject requestJsonObject, final onSignInComplete onSignInComplete){
    String url="/v1/login-with-user-name";

    final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, G.MAIN_URL + url, requestJsonObject, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int success = 0;
        String token = "";
        try {
          success = response.getInt("success");
          token = response.getString("token");
          if(success == 1){
            onSignInComplete.onSignIn(token);
          }else{
            onSignInComplete.onSignIn("");
          }


        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
//        onSignInComplete.onSignIn("");
      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(12000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }



  public void signInUserWithToken(JSONObject requestJsonObject, final onSignInWithTokenComplete onSignInWithTokenComplete){
    String url="/v1/login-with-token";

    final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, G.MAIN_URL + url, requestJsonObject, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {

        int success = 0;
        try {
          success = response.getInt("success");
          onSignInWithTokenComplete.onSignInWithToken(success);
        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
//        onSignInWithTokenComplete.onSignInWithToken(0);
      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(12000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }



  public void changePassword(JSONObject requestJsonObject, final onChangePasswordComplete onChangePasswordComplete){
    String url="/v1/change-password";

    final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, G.MAIN_URL + url, requestJsonObject, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int success = 0;
        String token = "";
        try {
          success = response.getInt("success");
          token = response.getString("token");
          if(success == 1){
            onChangePasswordComplete.onChangePassword(token);
          }else{
            onChangePasswordComplete.onChangePassword("");
          }


        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(12000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }



  public void recoveryPassword(JSONObject requestJsonObject, final onRecoveryPasswordComplete onRecoveryPasswordComplete){
    String url="/v1/recovery-password";

    final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, G.MAIN_URL + url, requestJsonObject, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int success = 0;
        try {
          success = response.getInt("success");
          onRecoveryPasswordComplete.onChangePassword(success);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onRecoveryPasswordComplete.onChangePassword(0);
      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(12000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }





  public interface onSignUpComplete{
    void onSignUp(String token);
  }

  public interface onSignInComplete{
    void onSignIn(String token);
  }

  public interface onSignInWithTokenComplete{
    void onSignInWithToken(int success);
  }

  public interface onChangePasswordComplete{
    void onChangePassword(String token);

  }

  public interface onRecoveryPasswordComplete{
    void onChangePassword(int success);

  }
}
