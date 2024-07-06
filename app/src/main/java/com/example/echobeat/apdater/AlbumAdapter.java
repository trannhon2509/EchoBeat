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
import com.example.echobeat.model.Album;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private Context context;
    private List<Album> albums;

    public AlbumAdapter(Context context, List<Album> albums) {
        this.context = context;
        this.albums = albums;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_album_horizontal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Album album = albums.get(position);

        holder.albumTitle.setText(album.getTitle());

        // Load ảnh từ URL bằng Glide
        Glide.with(context)
                .load(album.getCoverImage()) // URL của ảnh từ Firebase
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_loading) // Hình ảnh thay thế khi đang tải
                        .error(R.drawable.ic_album)) // Hình ảnh thay thế khi tải lỗi
                .into(holder.albumImage);
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView albumTitle;
        ImageView albumImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            albumTitle = itemView.findViewById(R.id.album_title);
            albumImage = itemView.findViewById(R.id.album_image);
        }
    }
}
