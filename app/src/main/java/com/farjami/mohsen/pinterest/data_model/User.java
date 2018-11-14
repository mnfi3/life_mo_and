package com.farjami.mohsen.pinterest.data_model;

import java.util.Set;

public class User {
  private String user_name = "";
  private String email = "";
  private String client_key = "";
  private String server_token = "";



  public String getUser_name() {
    return user_name;
  }

  public void setUser_name(String user_name) {
    this.user_name = user_name;
  }



  public String getClient_key() {
    return client_key;
  }

  public void setClient_key(String client_id) {
    this.client_key = client_id;
  }


  public String getServer_token() {
    return server_token;
  }

  public void setServer_token(String server_token) {
    this.server_token = server_token;
  }


  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
