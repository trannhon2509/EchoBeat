package com.example.echobeat.apdater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.echobeat.R;
import com.example.echobeat.modelFirebase.Artist;

import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {

    private Context context;
    private List<Artist> artists;

    public ArtistAdapter(Context context, List<Artist> artists) {
        this.context = context;
        this.artists = artists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_artist_horizontal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Artist artist = artists.get(position);

        holder.artistName.setText(artist.getUsername());

        Glide.with(context)
                .load(artist.getProfilePicture()) // URL của ảnh từ Firebase
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_loading) // Hình ảnh thay thế khi đang tải
                        .error(R.drawable.ic_user)) // Hình ảnh thay thế khi tải lỗi
                .into(holder.artistImage);
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView artistName;
        ImageView artistImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            artistName = itemView.findViewById(R.id.artist_name);
            artistImage = itemView.findViewById(R.id.artist_image);
        }
    }
}
