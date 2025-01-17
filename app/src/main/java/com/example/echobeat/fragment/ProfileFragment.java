package com.example.echobeat.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.echobeat.R;
import com.example.echobeat.apdater.PlaylistAdapter;
import com.example.echobeat.firebase.FirebaseHelper;
import com.example.echobeat.model.Playlist;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private RecyclerView recyclerView;
    private PlaylistAdapter playlistAdapter;
    private List<Playlist> playlistList;
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        playlistList = new ArrayList<>();
        playlistAdapter = new PlaylistAdapter(getContext(), playlistList);
        recyclerView.setAdapter(playlistAdapter);

        loadData();

        return view;
    }

    private void loadData() {
        FirebaseHelper<Playlist> playlistFirebaseHelper = new FirebaseHelper<>();
        playlistFirebaseHelper.getData("playlists", Playlist.class, new FirebaseHelper.DataCallback<Playlist>() {
            @Override
            public void onCallback(List<Playlist> data) {
                if (data != null) {
                    playlistList.clear();
                    playlistList.addAll(data);
                    playlistAdapter.notifyDataSetChanged();
                } else {
                    // Handle the error
                }
            }
        });
    }
}