package com.example.echobeat.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.echobeat.R;
import com.example.echobeat.apdater.SongAdapter;
import com.example.echobeat.firebase.FirebaseHelper;
import com.example.echobeat.model.Song;

import java.util.List;

public class AllFragment extends Fragment {

    private RecyclerView recyclerViewSongs;
    private SongAdapter songAdapter;

    public AllFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_all, container, false);  // Update this line

        // Initialize RecyclerView
        recyclerViewSongs = rootView.findViewById(R.id.recyclerViewAll);  // Ensure this ID matches your layout file
        recyclerViewSongs.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Load data into RecyclerView
        loadSongs();

        return rootView;
    }

    private void loadSongs() {
        FirebaseHelper<Song> firebaseHelper = new FirebaseHelper<>();
        firebaseHelper.getRecentData("songs", "releaseYear", 20, Song.class, new FirebaseHelper.DataCallback<Song>() {
            @Override
            public void onCallback(List<Song> data) {
                if (data != null) {
                    songAdapter = new SongAdapter(getContext(), data);
                    recyclerViewSongs.setAdapter(songAdapter);
                }
            }
        });
    }

}
