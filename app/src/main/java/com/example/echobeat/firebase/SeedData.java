package com.example.echobeat.firebase;

import com.example.echobeat.model.Album;
import com.example.echobeat.model.Artist;
import com.example.echobeat.model.Category;
import com.example.echobeat.model.Playlist;
import com.example.echobeat.model.Song;
import com.example.echobeat.model.User;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class SeedData {

    private FirebaseHelper<Album> albumHelper;
    private FirebaseHelper<Artist> artistHelper;
    private FirebaseHelper<Category> categoryHelper;
    private FirebaseHelper<Playlist> playlistHelper;
    private FirebaseHelper<Song> songHelper;
    private FirebaseHelper<User> userHelper;

    public SeedData() {
        albumHelper = new FirebaseHelper<>();
        artistHelper = new FirebaseHelper<>();
        categoryHelper = new FirebaseHelper<>();
        playlistHelper = new FirebaseHelper<>();
        songHelper = new FirebaseHelper<>();
        userHelper = new FirebaseHelper<>();
    }

    public void seedAllData() {
        seedAlbums(10); // Truyền số lượng album cần tạo vào đây
        seedArtists(15); // Truyền số lượng nghệ sĩ cần tạo vào đây
        seedCategories(8); // Truyền số lượng danh mục cần tạo vào đây
        seedPlaylists(10); // Truyền số lượng playlist cần tạo vào đây
        seedSongs(100); // Truyền số lượng bài hát cần tạo vào đây
        seedUsers(10); // Truyền số lượng người dùng cần tạo vào đây
    }

    private void seedAlbums(int count) {
        for (int i = 1; i <= count; i++) {
            int albumId = i;
            int userId = getRandomUserId();
            String albumName = "Album " + albumId;
            int releaseYear = 2023 - (i % 2); // alternating release years
            String albumImage = "album" + albumId + ".jpg";
            int categoryId = getRandomCategoryId();

            Album album = new Album(albumId, userId, albumName, releaseYear, albumImage, categoryId);
            albumHelper.addData("albums", album);
        }
    }



    private void seedArtists(int count) {
        for (int i = 1; i <= count; i++) {
            int artistId = i;
            String artistName = "artist" + artistId;
            String email = "artist" + artistId + "@example.com";
            String artistImage = "artist" + artistId + ".jpg";
            String password = "password";
            int userId = getRandomUserId();
            String bio = "Artist " + artistId + " bio";
            List<String> songIds = Arrays.asList(String.valueOf(i), String.valueOf(i + 1), String.valueOf(i + 2)); // Example songIds
            String genre = (i % 2 == 0) ? "Rock" : "Pop";

            Artist artist = new Artist(artistId, artistName, email, artistImage, 1, "asdhjfgahjsdgfhjsadgfhjasg", password, userId, bio, songIds, genre);
            artistHelper.addData("artists", artist);


        }
    }



    private void seedCategories(int count) {
        for (int i = 1; i <= count; i++) {
            Category category = new Category(i, "Category " + i);
            categoryHelper.addData("categories", category);
        }
    }



    private void seedPlaylists(int count) {
        for (int i = 1; i <= count; i++) {
            int playlistId = i;
            int userId = getRandomUserId();
            String playlistName = "Playlist " + playlistId;
            String playlistDescription = "Playlist description " + playlistId;
            String playlistImage = "playlist" + playlistId + ".jpg";

            Playlist playlist = new Playlist(playlistId, userId, playlistName, playlistDescription, playlistImage);
            playlistHelper.addData("playlists", playlist);
        }
    }




    private void seedSongs(int count) {
        Random random = new Random();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(2023, Calendar.JANUARY, 1); // Set start date to January 1, 2023
        long startMillis = startCalendar.getTimeInMillis();

        Calendar endCalendar = Calendar.getInstance(); // Current date
        long endMillis = endCalendar.getTimeInMillis();

        for (int i = 1; i <= count; i++) {
            int songId = i;
            String userId = getRandomUserIdAsString();
            String songUrl = "song" + songId + ".mp3";
            String songTitle = "Song " + songId;
            int songDuration = 180 + (i * 10); // incrementing duration

            // Generate random date between start date and end date
            long randomMillis = startMillis + (long) (random.nextDouble() * (endMillis - startMillis));
            Date releaseYear = new Date(randomMillis);

            String pictureSong = "https://th.bing.com/th/id/OIP.4Acoxt6K25NRPbtIVvxmQQAAAA?rs=1&pid=ImgDetMain";
            String categoryId = getRandomCategoryIdAsString();

            Song song = new Song(String.valueOf(songId), userId, songUrl, songTitle, songDuration, releaseYear, pictureSong, categoryId);
            songHelper.addData("songs", song);
        }
    }


    private void seedUsers(int count) {
        for (int i = 1; i <= count; i++) {
            User user = new User(i, ("User " + i), ("User" + i + "@example.com"), "https://th.bing.com/th/id/OIP.4Acoxt6K25NRPbtIVvxmQQAAAA?rs=1&pid=ImgDetMain", getRandomRoleId(), "sdhgfahsdgfhjagsdfhjgsadhjfggh");
            userHelper.addData("users", user);
        }
    }


    private int getRandomUserId() {
        Random random = new Random();
        int randomUserId = random.nextInt(10) + 1; // Assuming user IDs range from 1 to 10
        return randomUserId;
    }



    private String getRandomUserIdAsString() {
        return String.valueOf(getRandomUserId());
    }

    private int getRandomCategoryId() {
        Random random = new Random();
        int randomCategoryId = random.nextInt(8) + 1; // Assuming category IDs range from 1 to 8
        return randomCategoryId;
    }

    private String getRandomCategoryIdAsString() {
        return String.valueOf(getRandomCategoryId());
    }

    private int getRandomRoleId() {
        Random random = new Random();
        int randomCategoryId = random.nextInt(2) + 1; // Assuming category IDs range from 1 to 8
        return randomCategoryId;
    }

}
