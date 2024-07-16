package com.example.echobeat.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.echobeat.modelFirebase.Song;

import java.util.List;

public class PlaylistFragmentViewModel extends ViewModel {
    private final MutableLiveData<List<Song>> songs = new MutableLiveData<>();

    public LiveData<List<Song>> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songList) {
        songs.setValue(songList);
    }
    public boolean isSongsLoaded() {
        return songs.getValue() != null;
    }

}