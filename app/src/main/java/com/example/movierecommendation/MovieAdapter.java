package com.example.movierecommendation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

class MovieItemViewHolder extends RecyclerView.ViewHolder{
    MovieAdapter.onItemRecommended itemRecommended;
    private ImageView imageView;
    private TextView movieName,movieCategory;
    private RadioGroup radioGroup;
    private RadioButton likeBtn,disLikeBtn;

    public MovieItemViewHolder(@NonNull View itemView, MovieAdapter.onItemRecommended recommended) {
        super(itemView);
        itemRecommended = recommended;
        imageView = itemView.findViewById(R.id.movieImage);
        movieName = itemView.findViewById(R.id.movieName);
        movieCategory = itemView.findViewById(R.id.movieCategory);
        radioGroup = itemView.findViewById(R.id.radioGroup);
        likeBtn = itemView.findViewById(R.id.likeBtn);
        disLikeBtn = itemView.findViewById(R.id.dislikeBtn);
    }

    public void bind(@NonNull Movies movies) {
        movieName.setText(movies.getMovieName());
        movieCategory.setText(movies.getMovieCategory());

        // AsyncTask to load image
        class ImageAsyncTask extends AsyncTask<String, String, Bitmap>{

            @Override
            protected Bitmap doInBackground(String... strings) {
                try {
                    URL imageUrl = new URL(strings[0]);
                    HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    return BitmapFactory.decodeStream(is, null, options);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                if(bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    imageView.setImageResource(R.drawable.error);
                }
            }
        }
        // Execute AsyncTask to load image
        new ImageAsyncTask().execute(movies.getMovieImageURL());

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(likeBtn.isChecked()){
                    itemRecommended.onItemRecommend(1,movies);
                }
                else if (disLikeBtn.isChecked()){
                    itemRecommended.onItemRecommend(0,movies);
                }
            }
        });

    }
}



public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    onItemRecommended mItemClickListener;
    private List<Movies> moviesList;

    public MovieAdapter(List<Movies> moviesList) {
        this.moviesList = moviesList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_with_like, parent, false);
        return new MovieItemViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MovieItemViewHolder) holder).bind(this.moviesList.get(position));
    }

    @Override
    public int getItemCount() {
        return this.moviesList.size();
    }

    public interface onItemRecommended{
        void onItemRecommend(int check,Movies movies);
    }

    public void updateMovieList(List<Movies> list){
        this.moviesList.clear();
        this.moviesList = list;
        notifyDataSetChanged();
    }

}
