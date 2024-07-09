package com.example.echobeat.Repository;


import android.content.Context;

import com.example.echobeat.activity.loginModel.AddInfomation;
import com.example.echobeat.model.Artist;

public class ArtistRepository {

    private final Context context;

    public ArtistRepository(Context context) {
        this.context = context;
    }

    public boolean saveArtist(Artist artist) {

        return true;
    }
}
