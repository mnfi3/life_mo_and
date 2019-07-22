package net.lifemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.farjami.mohsen.pinterest.R;
import net.lifemo.data_model.Post;
import net.lifemo.data_model.Tag;
import net.lifemo.view.activity.ActivityShowPost;
import net.lifemo.view.my_views.MyViews;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ListViewHolder>{


  private Context context;
  private List <Tag> tags;
//  private TextView txt_tags;
  String tagText = "";

  private final OnItemClickListener listener;

  private ArrayList<Integer> selectedTagsId = new ArrayList<Integer>();
  private ArrayList<String> selectedTagsName = new ArrayList<String>();
  private String tagsText;

  public TagsAdapter(Context context, List <Tag> tags, OnItemClickListener listener){
    this.context = context;
    this.tags = tags;
    this.listener = listener;

  }

//  public TagsAdapter(Context context, List <Tag> tags, TextView textView){
//    this.context = context;
//    this.tags = tags;
//    this.txt_tags = textView;
//  }

  @Override
  public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view= LayoutInflater.from(context).inflate(R.layout.item_tag_in_post,parent,false);

    final ListViewHolder viewHolder = new ListViewHolder(view);

    view.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //listener.onItemClick(new Tag());
      }
    });

    return viewHolder;
  }






  @Override
  public void onBindViewHolder(ListViewHolder holder, int position) {

    final Tag tag = tags.get(position);


    holder.txt_tag.setText(tag.getName());


    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        boolean isExist = false;
        for (int i=0;i<selectedTagsId.size();i++){
          if(selectedTagsId.get(i) == tag.getId()){
            selectedTagsId.remove(i);
            selectedTagsName.remove(i);
            isExist = true;
            break;
          }
        }

        if (!isExist) {
          if(selectedTagsId.size() > 4){
            MyViews.makeText((AppCompatActivity) context,"you cant add more than 5 tag for your photo!", Toast.LENGTH_SHORT);
          }else {
            selectedTagsId.add(tag.getId());
            selectedTagsName.add(tag.getName());
          }
        }

        tagsText = "";
        for (int i=0;i<selectedTagsName.size();i++){
          tagsText += "#" + selectedTagsName.get(i) + " ";
        }



        listener.onItemClick(tag);
      }
    });


  }

  @Override
  public int getItemCount() {
    return tags.size();
  }

  public class ListViewHolder extends RecyclerView.ViewHolder{

    private TextView txt_tag;

    public ListViewHolder(View itemView) {
      super(itemView);
      txt_tag= (TextView) itemView.findViewById(R.id.txt_tag);

    }


  }


  public ArrayList<Integer> getSelectedTags(){
    return this.selectedTagsId;
  }

  public String getTagsText(){
    return tagsText;
  }







}