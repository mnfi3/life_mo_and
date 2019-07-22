package net.lifemo.api_services;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import net.lifemo.data_model.Post;
import net.lifemo.data_model.Tag;
import net.lifemo.system.G;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PostsApiService {
  private Context context;
  public PostsApiService(Context context){
    this.context=context;
  }


  public void getMenuPosts(JSONObject jsonObject, final onMenuPostsReceived onMenuPostsReceived){
    String url = "/v1/get-all-posts";
    Log.i("LOG","start method");
    final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, G.MAIN_URL + url, jsonObject, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        List<Post> posts =new ArrayList<>();
        Integer last_id = 0;


        Log.i("LOG","connect to server");
        try {
          int success = response.getInt("success");
          int id = response.getInt("last_id");
          last_id = id;

          JSONArray postArray = null;
          postArray = response.getJSONArray("posts");
          if(success == 1 && postArray != null){
            Log.i("LOG","success=1");
            for(int i=0;i<postArray.length();i++){
              Log.i("LOG","step :" + i);
              Post post =new Post();
              JSONObject postObject=postArray.getJSONObject(i);
              post.setId(postObject.getInt("id"));
              post.setDescription(postObject.getString("description"));
              post.setImageUrl(postObject.getString("image_url_low"));
              post.setMainImageUrl(postObject.getString("image_url"));



//              JSONArray tagArray = postObject.getJSONArray("tags");
//
//              ArrayList<Tag> tags = new ArrayList<>();
//              if(tagArray.length()>0 && tagArray!=null){
//                for(int j = 0;j<tagArray.length();j++){
//                  Log.i("LOG","step :" + i + " # " +j);
//                  Tag tag = new Tag();
//                  JSONObject tagObject=tagArray.getJSONObject(j);
//                  tag.setId(tagObject.getInt("id"));
//                  tag.setName(tagObject.getString("name"));
//                  tags.add(tag);
//                }
//              }
//
//              post.setTags(tags);



//              JSONObject infoObject = postObject.getJSONObject("information");
//              if(infoObject !=null) {
//                post.setSaveCount(infoObject.getInt("saved_count"));
//                post.setShareCount(infoObject.getInt("shared_count"));
//              }else {
//                post.setSaveCount(0);
//                post.setShareCount(0);
//              }


              posts.add(post);

            }
          }

        } catch (JSONException e) {
          e.printStackTrace();
        }



        onMenuPostsReceived.onReceived(posts);
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(18000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }


  public void getSavedPosts(JSONObject tokenJson, final onSavedPostsReceived onSavedPostsReceived){
    String url = "/v1/get-saved-posts";
    Log.i("LOG","start method");
    final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, G.MAIN_URL + url, tokenJson, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        List<Post> posts =new ArrayList<>();

        Log.i("LOG","connect to server");
        try {
          int success = response.getInt("success");

          JSONArray postArray = null;
          postArray = response.getJSONArray("posts");
          if(success == 1 && postArray != null){
            Log.i("LOG","success=1");
            for(int i=0;i<postArray.length();i++){
              Log.i("LOG","step :" + i);
              Post post =new Post();
              JSONObject postObject=postArray.getJSONObject(i);
              post.setId(postObject.getInt("id"));
              post.setDescription(postObject.getString("description"));
              post.setImageUrl(postObject.getString("image_url_low"));
              post.setMainImageUrl(postObject.getString("image_url"));

              JSONArray tagArray = postObject.getJSONArray("tags");

              ArrayList<Tag> tags = new ArrayList<>();
              if(tagArray.length()>0 && tagArray!=null){
                for(int j = 0;j<tagArray.length();j++){
                  Log.i("LOG","step :" + i + " # " +j);
                  Tag tag = new Tag();
                  JSONObject tagObject=tagArray.getJSONObject(j);
                  tag.setId(tagObject.getInt("id"));
                  tag.setName(tagObject.getString("name"));
                  tags.add(tag);
                }
              }

              post.setTags(tags);

//              JSONObject infoObject = postObject.getJSONObject("information");
//              if(infoObject !=null) {
//                post.setSaveCount(infoObject.getInt("saved_count"));
//                post.setShareCount(infoObject.getInt("shared_count"));
//              }else {
//                post.setSaveCount(0);
//                post.setShareCount(0);
//              }


              posts.add(post);

            }
          }

        } catch (JSONException e) {
          e.printStackTrace();
        }



        onSavedPostsReceived.onReceived(posts);
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(18000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }


  public void getSearchedPosts(JSONObject jsonObject, final onSearchPostsReceived onSearchPostsReceived){
    String url = "/v1/get-search-result";
    final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, G.MAIN_URL + url, jsonObject, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        List<Post> posts =new ArrayList<>();

        try {
          int success = response.getInt("success");

          JSONArray postArray = null;
          postArray = response.getJSONArray("posts");
          if(success == 1 && postArray != null){
            for(int i=0;i<postArray.length();i++){
              Post post =new Post();
              JSONObject postObject=postArray.getJSONObject(i);
              post.setId(postObject.getInt("id"));
              post.setDescription(postObject.getString("description"));
              post.setImageUrl(postObject.getString("image_url_low"));
              post.setMainImageUrl(postObject.getString("image_url"));






//              JSONArray tagArray = postObject.getJSONArray("tags");
//              ArrayList<Tag> tags = new ArrayList<>();
//              if(tagArray.length()>0 && tagArray!=null){
//                for(int j = 0;j<tagArray.length();j++){
//                  Log.i("LOG","step :" + i + " # " +j);
//                  Tag tag = new Tag();
//                  JSONObject tagObject=tagArray.getJSONObject(j);
//                  tag.setId(tagObject.getInt("id"));
//                  tag.setName(tagObject.getString("name"));
//                  tags.add(tag);
//                }
//              }
//
//              post.setTags(tags);




//              JSONObject infoObject = postObject.getJSONObject("information");
//              if(infoObject !=null) {
//                post.setSaveCount(infoObject.getInt("saved_count"));
//                post.setShareCount(infoObject.getInt("shared_count"));
//              }else {
//                post.setSaveCount(0);
//                post.setShareCount(0);
//              }


              posts.add(post);

            }
          }

        } catch (JSONException e) {
          e.printStackTrace();
        }



        onSearchPostsReceived.onReceived(posts);
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onSearchPostsReceived.onReceived(new ArrayList<Post>());
      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(18000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }


  public void getSpecialPost(JSONObject post_id, final onSpecialPostReceived onSpecialPostReceived){
    String url = "/v1/get-special-post-with-id";
    Log.i("LOG","start method");
    final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, G.MAIN_URL + url, post_id, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        List<Post> posts =new ArrayList<>();
        Post post = new Post();
        String user_name = "";

        try {
          int success = response.getInt("success");
          if(success == 1) {
            user_name = response.getString("user_name");
            JSONObject postObject = response.getJSONObject("post");
            post.setId(postObject.getInt("id"));
            post.setDescription(postObject.getString("description"));
            post.setImageUrl(postObject.getString("image_url"));
            post.setMainImageUrl(postObject.getString("image_url"));


            JSONArray tagArray = postObject.getJSONArray("tags");

            ArrayList<Tag> tags = new ArrayList<>();
            if (tagArray.length() > 0 ) {
              for (int j = 0; j < tagArray.length(); j++) {
                Tag tag = new Tag();
                JSONObject tagObject = tagArray.getJSONObject(j);
                tag.setId(tagObject.getInt("id"));
                tag.setName(tagObject.getString("name"));
                tags.add(tag);
              }
            }

            post.setTags(tags);

            JSONObject infoObject = postObject.getJSONObject("information");
              if(infoObject !=null) {
                post.setSaveCount(infoObject.getInt("saved_count"));
                post.setShareCount(infoObject.getInt("shared_count"));
              }else {
                post.setSaveCount(0);
                post.setShareCount(0);
              }

          }



        } catch (JSONException e) {
          e.printStackTrace();
        }



        onSpecialPostReceived.onReceived(post, user_name);
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onSpecialPostReceived.onReceived(new Post(), "");
      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(18000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }


  public void getRelatedPosts(JSONObject post_id, final onRelatedPostsReceived onRelatedPostsReceived){
    String url = "/v1/get-related-posts";
    final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, G.MAIN_URL + url, post_id, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        List<Post> posts =new ArrayList<>();

        try {
          int success = response.getInt("success");

          JSONArray postArray = null;
          postArray = response.getJSONArray("posts");
          if(success == 1 && postArray != null){
            for(int i=0;i<postArray.length();i++){
              Post post =new Post();
              JSONObject postObject=postArray.getJSONObject(i);
              post.setId(postObject.getInt("id"));
              post.setDescription(postObject.getString("description"));
              post.setImageUrl(postObject.getString("image_url_low"));
              post.setMainImageUrl(postObject.getString("image_url"));

              JSONArray tagArray = postObject.getJSONArray("tags");

              ArrayList<Tag> tags = new ArrayList<>();
              if(tagArray.length()>0 && tagArray!=null){
                for(int j = 0;j<tagArray.length();j++){
                  Log.i("LOG","step :" + i + " # " +j);
                  Tag tag = new Tag();
                  JSONObject tagObject=tagArray.getJSONObject(j);
                  tag.setId(tagObject.getInt("id"));
                  tag.setName(tagObject.getString("name"));
                  tags.add(tag);
                }
              }

              post.setTags(tags);

//              JSONObject infoObject = postObject.getJSONObject("information");
//              if(infoObject !=null) {
//                post.setSaveCount(infoObject.getInt("saved_count"));
//                post.setShareCount(infoObject.getInt("shared_count"));
//              }else {
//                post.setSaveCount(0);
//                post.setShareCount(0);
//              }


              posts.add(post);

            }
          }

        } catch (JSONException e) {
          e.printStackTrace();
        }



        onRelatedPostsReceived.onReceived(posts);
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(18000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }


  public void removeFromSavedPosts(JSONObject jsonObject, final onRemoveFromSavedPostsReceived onRemoveFromSavedPostsReceived){
    String url = "/v1/remove-from-saved-posts";
    final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, G.MAIN_URL + url, jsonObject, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int success = 0;
        try {
          success = response.getInt("success");
        } catch (JSONException e) {
          e.printStackTrace();
        }
        onRemoveFromSavedPostsReceived.onReceived(success);
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onRemoveFromSavedPostsReceived.onReceived(0);
      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(18000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }


  public void removeFromUserPosts(JSONObject jsonObject, final onRemoveFromUserPostsReceived onRemoveFromUserPostsReceived){
    String url = "/v1/remove-from-user-posts";
    final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, G.MAIN_URL + url, jsonObject, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int success = 0;
        try {
          success = response.getInt("success");
        } catch (JSONException e) {
          e.printStackTrace();
        }
        onRemoveFromUserPostsReceived.onReceived(success);
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onRemoveFromUserPostsReceived.onReceived(0);
      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(18000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }

  public void addToSavedPosts(JSONObject jsonObject, final onAddToSavedPostsReceived onAddToSavedPostsReceived){
    String url = "/v1/add-to-saved-posts";
    final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, G.MAIN_URL + url, jsonObject, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int success = 0;
        try {
          success = response.getInt("success");
        } catch (JSONException e) {
          e.printStackTrace();
        }
        onAddToSavedPostsReceived.onReceived(success);
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onAddToSavedPostsReceived.onReceived(0);
      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(18000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }


  public void addToSharedPosts(JSONObject jsonObject, final onAddToSharedPostsReceived onAddToSharedPostsReceived){
    String url = "/v1/add-post-shared-count";
    final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, G.MAIN_URL + url, jsonObject, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int success = 0;
        try {
          success = response.getInt("success");
        } catch (JSONException e) {
          e.printStackTrace();
        }
        onAddToSharedPostsReceived.onReceived(success);
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onAddToSharedPostsReceived.onReceived(0);
      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(18000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }


  public void getUserPosts(JSONObject tokenJson, final onUserPostsReceived onUserPostsReceived){
    String url = "/v1/get-user-posts";
    final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, G.MAIN_URL + url, tokenJson, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        List<Post> posts =new ArrayList<>();

        try {
          int success = response.getInt("success");

          JSONArray postArray = null;
          postArray = response.getJSONArray("posts");
          if(success == 1 && postArray != null){
            for(int i=0;i<postArray.length();i++){
              Post post =new Post();
              JSONObject postObject=postArray.getJSONObject(i);
              post.setId(postObject.getInt("id"));
              post.setDescription(postObject.getString("description"));
              post.setImageUrl(postObject.getString("image_url"));
              post.setMainImageUrl(postObject.getString("image_url"));

              JSONArray tagArray = postObject.getJSONArray("tags");

              ArrayList<Tag> tags = new ArrayList<>();
              if(tagArray.length()>0 && tagArray!=null){
                for(int j = 0;j<tagArray.length();j++){
                  Log.i("LOG","step :" + i + " # " +j);
                  Tag tag = new Tag();
                  JSONObject tagObject=tagArray.getJSONObject(j);
                  tag.setId(tagObject.getInt("id"));
                  tag.setName(tagObject.getString("name"));
                  tags.add(tag);
                }
              }

              post.setTags(tags);

              JSONObject infoObject = postObject.getJSONObject("information");
              if(infoObject !=null) {
                post.setSaveCount(infoObject.getInt("saved_count"));
                post.setShareCount(infoObject.getInt("shared_count"));
              }else {
                post.setSaveCount(0);
                post.setShareCount(0);
              }


              posts.add(post);

            }
          }

        } catch (JSONException e) {
          e.printStackTrace();
        }



        onUserPostsReceived.onReceived(posts);
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(18000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }


  public void getTags(final onTagsReceived onTagsReceived){
    String url = "/v1/get-all-tags";
    final JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, G.MAIN_URL + url, null, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        List<Tag> tags =new ArrayList<>();

        try {
          int success = response.getInt("success");
          if(success == 1){
            JSONArray tagArray = response.getJSONArray("tags");
            for (int i=0;i<tagArray.length();i++){
              JSONObject tagObject = tagArray.getJSONObject(i);
              Tag tag = new Tag();
              tag.setId(tagObject.getInt("id"));
              tag.setName(tagObject.getString("name"));

              tags.add(tag);
            }
          }

        } catch (JSONException e) {
          e.printStackTrace();
        }



        onTagsReceived.onReceived(tags);
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onTagsReceived.onReceived(new ArrayList<Tag>());
      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(18000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);
  }




  public void uploadPost(JSONObject jsonObject, final onUploadPostReceived onUploadPostReceived){
    String url = "/v1/upload-post";
    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, G.MAIN_URL + url, jsonObject, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        int success = 0;
        try {
          success = response.getInt("success");
        } catch (JSONException e) {
          e.printStackTrace();
        }
        onUploadPostReceived.onReceived(success);
      }

    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        onUploadPostReceived.onReceived(1);
      }
    });

    request.setRetryPolicy(new DefaultRetryPolicy(18000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    Volley.newRequestQueue(context).add(request);

  }




  public interface onMenuPostsReceived {
    void onReceived(List<Post> posts);
  }



  public interface onSavedPostsReceived {
    void onReceived(List<Post> posts);
  }


  public interface onSearchPostsReceived {
    void onReceived(List<Post> posts);
  }

  public interface onSpecialPostReceived {
    void onReceived(Post post, String user_name);
  }

  public interface onRelatedPostsReceived {
    void onReceived(List<Post> posts);
  }

  public interface onRemoveFromSavedPostsReceived {
    void onReceived(int success);
  }

  public interface onRemoveFromUserPostsReceived {
    void onReceived(int success);
  }

  public interface onAddToSavedPostsReceived {
    void onReceived(int success);
  }

  public interface onAddToSharedPostsReceived {
    void onReceived(int success);
  }


  public interface onUserPostsReceived {
    void onReceived(List<Post> posts);
  }


  public interface onTagsReceived {
    void onReceived(List<Tag> tags);
  }

  public interface onUploadPostReceived {
    void onReceived(int success);
  }

}




