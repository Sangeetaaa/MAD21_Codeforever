package com.scitech.codegram;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.time.Duration;
import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter <BlogAdapter.BlogViewHolder> {

    public List<Blog> blogList;

    public static class BlogViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public TextView blogTitle, blogDescription, blogUsername, blogLikes;
        public ImageView blogImage;
        public Context ctx;
        public CardView blogCard;
        public MaterialButton likeButton;
        public MaterialButton shareButton;

        public BlogViewHolder(View itemView, Context ctx) {
            super(itemView);
            this.ctx = ctx;
            mView = itemView;
            blogCard = (CardView) mView.findViewById(R.id.blog_cardView);
            blogTitle = (TextView) mView.findViewById(R.id.blogTitle);
            blogDescription = (TextView) mView.findViewById(R.id.blogDescription);
            blogUsername = (TextView) mView.findViewById(R.id.blogUsername);
            blogLikes = (TextView) mView.findViewById(R.id.blogLikes);
            blogImage = (ImageView) mView.findViewById(R.id.blogImage);
            likeButton = mView.findViewById(R.id.likeButton);
            shareButton = mView.findViewById(R.id.shareButton);
        }

        public void setDetails(String title, String desc, String username, String likes, String image) {

            blogTitle.setText(title);
            blogDescription.setText(desc);
            blogUsername.setText(username);
            blogLikes.setText(likes);
            Glide.with(ctx).load(image).into(blogImage);
        }
    }

    public BlogAdapter(List<Blog> blogList) {
        this.blogList = blogList;
    }

    @NotNull
    @Override
    public BlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.blog_view, parent, false);

        final BlogViewHolder blogHolder = new BlogViewHolder(itemView, parent.getContext());

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference forumDatabase = FirebaseDatabase.getInstance().getReference("TechForum/");

       blogHolder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String blogId = blogList.get(blogHolder.getAdapterPosition()).getBlogId();
                forumDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot walker : dataSnapshot.getChildren()){
                            if(walker.getKey().equals(blogId))
                            {
                                Blog blog = walker.getValue(Blog.class);
                                if(!blog.likeArray.contains(user.getUid())){
                                    blog.likeArray.add(user.getUid());
                                    blog.setLikes(String.valueOf(blog.likeArray.size()));
                                    forumDatabase.child(blogId).setValue(blog);
                                }
                                forumDatabase.removeEventListener(this);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NotNull DatabaseError databaseError) {
                    }
                });
            }
        });

       blogHolder.shareButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               try {
                   CardView cardView = blogHolder.blogCard;
                   cardView.setDrawingCacheEnabled(true);
                   Bitmap bitmap =  getBitmapFromView(cardView);
                   cardView.setDrawingCacheEnabled(false);

                   String mPath =
                           Environment.getExternalStorageDirectory().toString() + "/Pictures/"+ blogHolder.blogTitle.getText().toString()+".jpg";

                   File imageFile = new File(mPath);
                   FileOutputStream outputStream = new
                           FileOutputStream(imageFile);
                   int quality = 100;
                   bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                   outputStream.flush();
                   outputStream.close();
                   Context myctx  = v.getContext();
                   Uri photoURI = FileProvider.getUriForFile(myctx, myctx.getApplicationContext().getPackageName() + ".provider", imageFile);
                   Intent shareIntent = new Intent(Intent.ACTION_SEND);
                   shareIntent.setType("image/jpg");
                   shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                   shareIntent.putExtra(Intent.EXTRA_STREAM, photoURI);
                   myctx.startActivity(Intent.createChooser(shareIntent,"Share Via"));



               } catch (Throwable e) {
                   Toast.makeText(v.getContext(), "Couldn't share!", Toast.LENGTH_SHORT);
                   e.printStackTrace();

               }


           }
       });

        return blogHolder;
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

    @Override
    public void onBindViewHolder(BlogViewHolder holder, int position) {
        Blog newBlog = blogList.get(position);

        holder.setDetails(newBlog.getTitle(), newBlog.getDescription(),
                newBlog.getUsername(), newBlog.getLikes(), newBlog.getImage());
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }
}
