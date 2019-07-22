package net.lifemo.system;

import android.content.Context;
import android.content.SharedPreferences;

import net.lifemo.data_model.User;

public class UserSharedPrefManager {
  private static final String USER_SHARED_PREF_NAME = "user_shared_pref";
  private static final String USER_NAME = "user_name";
  private static final String EMAIL = "email";
  private static final String TOKEN = "token";
  private static final String CLIENT_KEY = "client_key";

  private SharedPreferences sharedPreferences;

  public UserSharedPrefManager(Context context) {
    sharedPreferences = context.getSharedPreferences(USER_SHARED_PREF_NAME, Context.MODE_PRIVATE);
  }

  public void saveUser(User user) {
    if (user != null) {
      SharedPreferences.Editor editor = sharedPreferences.edit();
      editor.putString(USER_NAME, user.getUser_name());
      //editor.putString(EMAIL, user.getEmail());
      editor.putString(TOKEN, user.getServer_token());
      editor.putString(CLIENT_KEY, user.getClient_key());
      editor.apply();
    }
  }

  public User getUser() {
    User user = new User();
    user.setUser_name(sharedPreferences.getString(USER_NAME, ""));
    //user.setEmail(sharedPreferences.getString(EMAIL, ""));
    user.setServer_token(sharedPreferences.getString(TOKEN ,""));
    user.setClient_key(sharedPreferences.getString(CLIENT_KEY , ""));
    return user;
  }

}
