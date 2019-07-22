package net.lifemo.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.farjami.mohsen.pinterest.R;
import net.lifemo.api_services.PostsApiService;
import net.lifemo.data_model.Post;
import net.lifemo.data_model.Tag;
import net.lifemo.data_model.User;
import net.lifemo.system.G;
import net.lifemo.system.UserSharedPrefManager;
import net.lifemo.view.activity.ActivityShowPost;
import net.lifemo.view.my_views.MyViews;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class UserPostsAdapter extends RecyclerView.Adapter<UserPostsAdapter.ListViewHolder>{

  private Context context;
  private List <Post> posts;

  public UserPostsAdapter(Context context, List <Post> posts){

    this.context = context;
    this.posts = posts;
  }

  @Override
  public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view= LayoutInflater.from(context).inflate(R.layout.item_post_in_user_posts,parent,false);
    return new ListViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final ListViewHolder holder, final int position) {
    final Post post = posts.get(position);

    Picasso.with(context).
      load(post.getImageUrl())
//      .noFade()
//      .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
//      .skipMemoryCache()
      //.placeholder(context.getResources().getDrawable(R.drawable.default_good_image)).
      //.error(context.getResources().getDrawable(R.drawable.default_no_image))
      .into(holder.image);

    //holder.expand_text_view.setText(post.getDescription());
    SparseBooleanArray mTogglePositions = new SparseBooleanArray();
    holder.expand_text_view.setText(post.getDescription(),mTogglePositions, position);

    int saved_count = post.getSaveCount();
    int shared_count = post.getShareCount();

    holder.txt_saved_count.setText(Integer.toString(saved_count));
    holder.txt_shared_count.setText(Integer.toString(shared_count));

    List<Tag> tags = post.getTags();
    String tagsText = "";
    for(int i=0 ;  i < tags.size() ; i++){
      tagsText += "#" + tags.get(i).getName() + " ";
    }

    holder.txt_tags.setText(tagsText);




    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent=new Intent(context, ActivityShowPost.class);
        intent.putExtra("ID", post.getId());
        intent.putExtra("IMAGE", post.getImageUrl());
        intent.putExtra("MAIN_IMAGE", post.getMainImageUrl());
        intent.putExtra("DESCRIPTION", post.getDescription());
//        intent.putExtra("TAGS", post.getTags());
//        intent.putExtra("SAVE_COUNT", post.getSaveCount());
//        intent.putExtra("SHARE_COUNT", post.getShareCount());
//        intent.putExtra("USERNAME", post.getUsername());
//        intent.putExtra("DESCRIPTION", post.getDescription());
        context.startActivity(intent);



      }
    });

    holder.btn_remove_post_from_my_posts.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
          builder = new AlertDialog.Builder(context);
        }

        builder.setTitle("Delete Photo")
          .setMessage("Are you sure you want to remove this photo?")
          .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

              UserSharedPrefManager prefManager = new UserSharedPrefManager(context);
              User user = prefManager.getUser();
              String token = user.getServer_token();
              String client_key = user.getClient_key();
              int post_id = post.getId();
              JSONObject jsonObject = new JSONObject();
              try {
                jsonObject.put("token",token);
                jsonObject.put("client_key",client_key);
                jsonObject.put("post_id",post_id);
              } catch (JSONException e) {
                e.printStackTrace();
              }

              PostsApiService apiService = new PostsApiService(context);
              apiService.removeFromUserPosts(jsonObject, new PostsApiService.onRemoveFromUserPostsReceived() {
                @Override
                public void onReceived(int success) {
                  if(success == 1){
                    holder.itemView.setVisibility(View.GONE);
                    MyViews.makeText((AppCompatActivity) context,"your photo deleted", Toast.LENGTH_SHORT);
                  }
                }
              });



            }
          })
          .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
              // do nothing
            }
          })
          .setIcon(android.R.drawable.ic_delete)
          .show();













      }
    });




  }


  @Override
  public int getItemCount() {
    return posts.size();
  }

  public class ListViewHolder extends RecyclerView.ViewHolder{

    private ImageView image;
    private TextView expandable_text;
    private  ExpandableTextView expand_text_view;
    private Button btn_remove_post_from_my_posts;
    private TextView txt_saved_count;
    private TextView txt_shared_count;
    private TextView txt_tags;

    public ListViewHolder(View itemView) {
      super(itemView);
      image = (ImageView) itemView.findViewById(R.id.post_image);
      expandable_text= (TextView) itemView.findViewById(R.id.expandable_text);
      expand_text_view= (ExpandableTextView) itemView.findViewById(R.id.expand_text_view);
      btn_remove_post_from_my_posts = (Button) itemView.findViewById(R.id.btn_remove_post_from_my);
      txt_tags = (TextView) itemView.findViewById(R.id.txt_tags);
      txt_saved_count = (TextView) itemView.findViewById(R.id.txt_saved_count);
      txt_shared_count = (TextView) itemView.findViewById(R.id.txt_shared_count);


    }


  }





}