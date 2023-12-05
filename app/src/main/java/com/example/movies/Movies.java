package com.example.movies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

class Movie {
    String name;
    String year;
    String director;
    Movie(String name, String year) {
        this.name = name;
        this.year = year;
        this.director = "";
    }
    Movie(String name, String year, String director) {
        this(name, year);
        this.director = director;
    }
    public String toString() {   // used by ListView
        return name + "\n(" + year + ")";
    }
    public String getString() {
        return name + "|" + year + "|" + director;
    }
}

public class Movies extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Movie> movies;

    public static final int EDIT_MOVIE_CODE = 1;
    public static final int ADD_MOVIE_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movies_list);

        try {
            FileInputStream fis = openFileInput("movies.dat");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(fis));
            String movieInfo = null;
            movies = new ArrayList<Movie>();
            while ((movieInfo = br.readLine()) != null) {
                String[] tokens = movieInfo.split("\\|");
                if (tokens.length == 3) {
                    movies.add(new Movie(tokens[0], tokens[1], tokens[2]));
                } else {
                    movies.add(new Movie(tokens[0], tokens[1]));
                }
            }
        } catch (IOException e) {
            String[] moviesList = getResources().getStringArray(R.array.movies_array);
            movies = new ArrayList<>(moviesList.length);
            for (int i = 0; i < moviesList.length; i++) {
                String[] tokens = moviesList[i].split("\\|");
                movies.add(new Movie(tokens[0], tokens[1]));
            }
        }

        listView = findViewById(R.id.movies_list);
        listView.setAdapter(
                new ArrayAdapter<Movie>(this, R.layout.movie, movies));

        // show movie for possible edit when tapped
        listView.setOnItemClickListener((p, V, pos, id) ->
                showMovie(pos));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                addMovie();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showMovie(int pos) {
        Bundle bundle = new Bundle();
        Movie movie = movies.get(pos);
        bundle.putInt(AddEditMovie.MOVIE_INDEX, pos);
        bundle.putString(AddEditMovie.MOVIE_NAME, movie.name);
        bundle.putString(AddEditMovie.MOVIE_YEAR, movie.year);
        bundle.putString(AddEditMovie.MOVIE_DIRECTOR, movie.director);
        Intent intent = new Intent(this, AddEditMovie.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, EDIT_MOVIE_CODE);
    }

    private void addMovie() {
        Intent intent = new Intent(this, AddEditMovie.class);
        startActivityForResult(intent, ADD_MOVIE_CODE);
    }

    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {
            return;
        }

        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return;
        }

        // gather all info passed back by launched activity
        String name = bundle.getString(AddEditMovie.MOVIE_NAME);
        String year = bundle.getString(AddEditMovie.MOVIE_YEAR);
        String director = bundle.getString(AddEditMovie.MOVIE_DIRECTOR);
        int index = bundle.getInt(AddEditMovie.MOVIE_INDEX);

        if (requestCode == EDIT_MOVIE_CODE) {
            Movie movie = movies.get(index);
            movie.name = name;
            movie.year = year;
            movie.director = director;
        } else {
            movies.add(new Movie(name, year, director));
        }

        // redo the adapter to reflect change^K
        listView.setAdapter(
                new ArrayAdapter<Movie>(this, R.layout.movie, movies));
    }
}