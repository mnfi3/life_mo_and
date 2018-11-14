package com.farjami.mohsen.pinterest.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.farjami.mohsen.pinterest.R;
import com.farjami.mohsen.pinterest.adapter.OnItemClickListener;
import com.farjami.mohsen.pinterest.adapter.TagsAdapter;
import com.farjami.mohsen.pinterest.api_services.PostsApiService;
import com.farjami.mohsen.pinterest.data_model.Tag;
import com.farjami.mohsen.pinterest.data_model.User;
import com.farjami.mohsen.pinterest.system.ConnectivityListener;
import com.farjami.mohsen.pinterest.system.G;
import com.farjami.mohsen.pinterest.system.UserSharedPrefManager;
import com.farjami.mohsen.pinterest.system.Utils;
import com.farjami.mohsen.pinterest.view.my_views.MyViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ActivityUpload extends AppCompatActivity {

  ImageView img_back;
  TextView txt_page_name;
  ImageView img_upload_image;
  EditText edt_image_description;
  RecyclerView rcv_all_tags;
  TextView txt_added_tags;
  Button btn_upload_post;

  private boolean isSelectedPhoto = false;

  private ProgressDialog dialog = null;

  private Snackbar snackbar;
  private ConnectivityListener connectivityListener;
  RelativeLayout lyt_upload;

  TagsAdapter adapter;

  List<Integer> selectedTagsId = new ArrayList<Integer>();
  String tagText = "";


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_upload);

    img_back= (ImageView) findViewById(R.id.img_back);
    txt_page_name= (TextView) findViewById(R.id.txt_page_name);
    img_upload_image= (ImageView) findViewById(R.id.img_upload_image);
    edt_image_description= (EditText) findViewById(R.id.edt_image_description);
    rcv_all_tags= (RecyclerView) findViewById(R.id.rcv_all_tags);
    txt_added_tags= (TextView) findViewById(R.id.txt_added_tags);
    btn_upload_post= (Button) findViewById(R.id.btn_upload_post);
    lyt_upload= (RelativeLayout) findViewById(R.id.lyt_upload);


    txt_page_name.setText("upload your image");


    dialog = new ProgressDialog(ActivityUpload.this);
    dialog.setMessage("Uploading Image...");
    dialog.setCancelable(false);



    img_back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    img_upload_image.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, Utils.REQCODE);
        isSelectedPhoto = true;
      }
    });


    btn_upload_post.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!checkEntry()){
          MyViews.makeText(ActivityUpload.this, "your description is too short",Toast.LENGTH_SHORT);
          return;
        }

        if(!isSelectedPhoto){
          MyViews.makeText(ActivityUpload.this, "please select an image",Toast.LENGTH_SHORT);
          return;
        }

        if(selectedTagsId.size() == 0){
          MyViews.makeText(ActivityUpload.this, "please insert tag for your photo",Toast.LENGTH_SHORT);
          return;
        }

        String description = edt_image_description.getText().toString();
        JSONArray tagsIdArray = new JSONArray();
        for(int i=0;i<selectedTagsId.size();i++){
          try {
            tagsIdArray.put(i,selectedTagsId.get(i));
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }

        UserSharedPrefManager prefManager = new UserSharedPrefManager(ActivityUpload.this);
        User user = prefManager.getUser();
        String token = user.getServer_token();
        String client_key = user.getClient_key();

        img_upload_image.setDrawingCacheEnabled(false);
        Bitmap image = ((BitmapDrawable) img_upload_image.getDrawable()).getBitmap();
        image = getResizedBitmap(image, 700);
        Bitmap image_low = getResizedBitmap(image, 250);
        //Bitmap image_low = image;

        dialog.show();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream byteArrayOutputStreamLow = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        image_low.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamLow);

        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        String encodedImageLow = Base64.encodeToString(byteArrayOutputStreamLow.toByteArray(), Base64.DEFAULT);

        JSONObject jsonObject = new JSONObject();
        try {
          jsonObject.put("token", token);
          jsonObject.put("client_key",client_key);

          jsonObject.put("image", encodedImage);
          jsonObject.put("image_low", encodedImageLow);
          jsonObject.put("description", description);
          jsonObject.put("name", G.APP_NAME);
          jsonObject.put("tags_id",tagsIdArray);

        } catch (JSONException e) {

        }


        PostsApiService apiService = new PostsApiService(ActivityUpload.this);
        apiService.uploadPost(jsonObject, new PostsApiService.onUploadPostReceived() {
          @Override
          public void onReceived(int success) {
            dialog.dismiss();
            if (success == 1) {
              MyViews.makeText(ActivityUpload.this, "your image uploaded successfully", Toast.LENGTH_SHORT);


            }else {
              MyViews.makeText(ActivityUpload.this, "your image uploading failed . please try again", Toast.LENGTH_SHORT);
            }
            finish();
          }
        });
      }
    });






    rcv_all_tags.setLayoutManager((new LinearLayoutManager(ActivityUpload.this, LinearLayoutManager.HORIZONTAL, true)));

    PostsApiService apiService = new PostsApiService(ActivityUpload.this);
    apiService.getTags(new PostsApiService.onTagsReceived() {
      @Override
      public void onReceived(List<Tag> tags) {
       // ActivityUpload.this.tags = tags;
//        adapter = new TagsAdapter(ActivityUpload.this, tags, txt_added_tags);
        adapter = new TagsAdapter(ActivityUpload.this, tags, new OnItemClickListener() {
          @Override
          public void onItemClick(Tag tag) {
            //Log.i("upload", "onItemClick: ");
            selectedTagsId = adapter.getSelectedTags();
            tagText = adapter.getTagsText();
            txt_added_tags.setText(tagText);
          }
        });
        rcv_all_tags.setAdapter(adapter);

      }
    });




  }


  private boolean checkEntry(){
    if(edt_image_description.getText().toString().length() < 3){
      return false;
    }

    return true;
  }



  public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
    int width = bm.getWidth();
    int height = bm.getHeight();
    float scaleWidth = ((float) newWidth) / width;
    float scaleHeight = ((float) newHeight) / height;
    // CREATE A MATRIX FOR THE MANIPULATION
    Matrix matrix = new Matrix();
    // RESIZE THE BIT MAP
    matrix.postScale(scaleWidth, scaleHeight);

    // "RECREATE" THE NEW BITMAP
    Bitmap resizedBitmap = Bitmap.createBitmap(
      bm, 0, 0, width, height, matrix, false);
    //bm.recycle();
    return resizedBitmap;
  }


  public Bitmap getResizedBitmap(Bitmap bm, int newWidth) {
    int width = bm.getWidth();
    int height = bm.getHeight();
    float scaleWidth = ((float) newWidth) / width;
   //float scaleHeight = ((float) newHeight) / height;
    float scaleHeight = scaleWidth;
    // CREATE A MATRIX FOR THE MANIPULATION
    Matrix matrix = new Matrix();
    // RESIZE THE BIT MAP
    matrix.postScale(scaleWidth,scaleHeight);

    // "RECREATE" THE NEW BITMAP
    Bitmap resizedBitmap = Bitmap.createBitmap(
      bm, 0, 0, width, height, matrix, false);
    //bm.recycle();
    return resizedBitmap;
  }








  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == Utils.REQCODE && resultCode == RESULT_OK && data != null) {
      Uri selectedImageUri = data.getData();
      img_upload_image.setImageURI(selectedImageUri);
      img_upload_image.setScaleType(ImageView.ScaleType.FIT_XY);
    }
  }


  @Override
  protected void onStart() {
    super.onStart();
    connectivityListener = new ConnectivityListener(lyt_upload, snackbar);
    registerReceiver(connectivityListener,new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

  }

  @Override
  protected void onStop() {
    super.onStop();
    unregisterReceiver(connectivityListener);
  }
}
