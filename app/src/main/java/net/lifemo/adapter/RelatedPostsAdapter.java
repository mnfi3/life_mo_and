package net.lifemo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.farjami.mohsen.pinterest.R;
import net.lifemo.data_model.Post;
import net.lifemo.system.G;
import net.lifemo.view.activity.ActivityShowPost;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RelatedPostsAdapter extends RecyclerView.Adapter<RelatedPostsAdapter.ListViewHolder>{

  private Context context;
  private List <Post> posts;

  public RelatedPostsAdapter(Context context, List <Post> posts){

    this.context = context;
    this.posts = posts;
  }

  @Override
  public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view= LayoutInflater.from(context).inflate(R.layout.item_post_in_main_menu,parent,false);
    return new ListViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ListViewHolder holder, int position) {
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
        int id = post.getId();
        String image_url = post.getImageUrl();
        String description = post.getDescription();
        Intent intent=new Intent(context, ActivityShowPost.class);
//        ((Activity)context).finish();

        intent.putExtra("ID", id);
        intent.putExtra("IMAGE", image_url);
        intent.putExtra("DESCRIPTION", description);
        intent.putExtra("MAIN_IMAGE", post.getMainImageUrl());
        //intent.putExtra("TAGS", post.getTags());
//        intent.putExtra("SAVE_COUNT", post.getSaveCount());
//        intent.putExtra("SHARE_COUNT", post.getShareCount());
//        intent.putExtra("USERNAME", post.getUsername());
//        intent.putExtra("DESCRIPTION", post.getDescription());
        context.startActivity(intent);
        
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

    public ListViewHolder(View itemView) {
      super(itemView);
      image = (ImageView) itemView.findViewById(R.id.post_image);
      expandable_text= (TextView) itemView.findViewById(R.id.expandable_text);
      expand_text_view= (ExpandableTextView) itemView.findViewById(R.id.expand_text_view);

    }


  }
}