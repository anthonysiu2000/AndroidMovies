package com.example.movies;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.view.View;
import android.widget.EditText;

public class AddEditMovie extends AppCompatActivity {

    public static final String MOVIE_INDEX = "movieIndex";
    public static final String MOVIE_NAME = "movieName";
    public static final String MOVIE_YEAR = "movieYear";
    public static final String MOVIE_DIRECTOR = "movieDirector";

    private int movieIndex;
    private EditText movieName, movieYear, movieDirector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_movie);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // activates the up arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get the fields
        movieName = findViewById(R.id.movie_name);
        movieYear = findViewById(R.id.movie_year);
        movieDirector = findViewById(R.id.movie_director);

        // see if info was passed in to populate fields
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            movieIndex = bundle.getInt(MOVIE_INDEX);
            movieName.setText(bundle.getString(MOVIE_NAME));
            movieYear.setText(bundle.getString(MOVIE_YEAR));
            movieDirector.setText(bundle.getString(MOVIE_DIRECTOR));
        }
    }

    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void save(View view) {
        // gather all data from text fields
        String name = movieName.getText().toString();
        String year = movieYear.getText().toString();
        String director = movieDirector.getText().toString();

        // pop up dialog if errors in input, and return
        // name and year are mandatory
        if (name == null || name.length() == 0 ||
                year == null || year.length() == 0) {
            Bundle bundle = new Bundle();
            bundle.putString(MovieDialogFragment.MESSAGE_KEY,
                    "Name and year are required");
            DialogFragment newFragment = new MovieDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return; // does not quit activity, just returns from method
        }

        // make Bundle
        Bundle bundle = new Bundle();
        bundle.putInt(MOVIE_INDEX, movieIndex);
        bundle.putString(MOVIE_NAME,name);
        bundle.putString(MOVIE_YEAR,year);
        bundle.putString(MOVIE_DIRECTOR,director);

        // send back to caller
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish(); // pops activity from the call stack, returns to parent

    }
}