package net.lifemo.data_model;

import java.util.ArrayList;
import java.util.List;

public class Post {

  private int id;
  private String imageUrl;
  private String mainImageUrl;
  private String description;
  private ArrayList<Tag> tags = new ArrayList<Tag>() ;
  private int saveCount;
  private int shareCount;
  private String username;



  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public int getSaveCount() {
    return saveCount;
  }

  public void setSaveCount(int saveCount) {
    this.saveCount = saveCount;
  }

  public int getShareCount() {
    return shareCount;
  }

  public void setShareCount(int shareCount) {
    this.shareCount = shareCount;
  }


  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public ArrayList<Tag> getTags() {
    return tags;
  }

  public void setTags(ArrayList<Tag> tags) {
    this.tags = tags;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getMainImageUrl() {
    return mainImageUrl;
  }

  public void setMainImageUrl(String mainImageUrl) {
    this.mainImageUrl = mainImageUrl;
  }
}
