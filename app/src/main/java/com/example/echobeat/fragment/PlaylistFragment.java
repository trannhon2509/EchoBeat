package com.example.echobeat.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.echobeat.R;
import com.example.echobeat.activity.PlayerActivity;
import com.example.echobeat.apdater.SongAdapter;
import com.example.echobeat.dbFirebase.FirebaseHelper;
import com.example.echobeat.modelFirebase.Song;
import com.example.echobeat.viewModel.AllFragmentViewModel;
import com.example.echobeat.viewModel.PlaylistFragmentViewModel;

import java.util.ArrayList;
import java.util.List;


public class PlaylistFragment extends Fragment {

    private RecyclerView recyclerViewPlayLists;
    private SongAdapter songAdapter;
    private PlaylistFragmentViewModel viewModel;
    public PlaylistFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);  // Retain this fragment across configuration changes
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_playlist, container, false);
        // Initialize RecyclerView
        recyclerViewPlayLists = rootView.findViewById(R.id.recycler_view_playLists);
        recyclerViewPlayLists.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(PlaylistFragmentViewModel.class);
        // Observe the ViewModel data
        viewModel.getSongs().observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                if (songs != null) {
                    songAdapter = new SongAdapter(getContext(), songs);
                    songAdapter.setOnItemClickListener(new SongAdapter.OnItemClickListener() {
                        // Inside onItemClick method in SongAdapter.OnItemClickListener
                        @Override
                        public void onItemClick(Song song) {
                            if (getContext() != null) {
                                // Ensure context is valid before showing Toast
                                Toast.makeText(getContext(), "Play song: " + song.getTitle(), Toast.LENGTH_SHORT).show();
                                // Pass song list and selected song to PlayerActivity
                                Intent intent = new Intent(getContext(), PlayerActivity.class);
                                intent.putParcelableArrayListExtra("SONG_LIST", (ArrayList<Song>) songs);
                                intent.putExtra("SONG_DATA", song); // Pass selected song
                                intent.putExtra("SONG_URL", song.getSongUrl());
                                startActivity(intent);
                            }
                        }

                    });
                    recyclerViewPlayLists.setAdapter(songAdapter);
                }
            }
        });
        // Load data if ViewModel is empty
        if (!viewModel.isSongsLoaded()) {
            loadSongs();
        }
        return rootView;
    }
    private void loadSongs() {
        FirebaseHelper<Song> firebaseHelper = new FirebaseHelper<>();
        firebaseHelper.getRecentData("songs", "releaseYear", 20, Song.class, new FirebaseHelper.DataCallback<Song>() {
            @Override
            public void onCallback(List<Song> data) {
                if (data != null) {
                    viewModel.setSongs(data);
                }
            }
        });
    }

}