package net.lifemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.farjami.mohsen.pinterest.R;
import net.lifemo.api_services.PostsApiService;
import net.lifemo.data_model.Post;
import net.lifemo.data_model.User;
import net.lifemo.system.G;
import net.lifemo.system.UserSharedPrefManager;
import net.lifemo.view.activity.ActivityShowPost;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SavedPostsAdapter extends RecyclerView.Adapter<SavedPostsAdapter.ListViewHolder>{

  private Context context;
  private List <Post> posts;

  public SavedPostsAdapter(Context context, List <Post> posts){

    this.context = context;
    this.posts = posts;
  }

  @Override
  public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view= LayoutInflater.from(context).inflate(R.layout.item_post_in_saved_posts,parent,false);
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

    holder.btn_remove_post_from_saved.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
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
        apiService.removeFromSavedPosts(jsonObject, new PostsApiService.onRemoveFromSavedPostsReceived() {
          @Override
          public void onReceived(int success) {
            if(success == 1){
              holder.itemView.setVisibility(View.GONE);
            }
          }
        });

      }
    });

    holder.btn_share_post_from_saved.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

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
    private Button btn_remove_post_from_saved;
    private Button btn_share_post_from_saved;

    public ListViewHolder(View itemView) {
      super(itemView);
      image = (ImageView) itemView.findViewById(R.id.post_image);
      expandable_text= (TextView) itemView.findViewById(R.id.expandable_text);
      expand_text_view= (ExpandableTextView) itemView.findViewById(R.id.expand_text_view);
      btn_remove_post_from_saved= (Button) itemView.findViewById(R.id.btn_remove_post_from_saved);
      btn_share_post_from_saved= (Button) itemView.findViewById(R.id.btn_share_post_from_saved);

    }


  }
}