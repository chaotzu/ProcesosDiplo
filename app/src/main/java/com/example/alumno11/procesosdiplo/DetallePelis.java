package com.example.alumno11.procesosdiplo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.alumno11.procesosdiplo.webservice.Entity;
import com.example.alumno11.procesosdiplo.webservice.JSONParser;
import com.example.alumno11.procesosdiplo.webservice.Petition;

import java.util.List;
import java.util.Map;

public class DetallePelis extends AppCompatActivity {

    private Video video = null;
    private String imdb = "";


    private TextView titleTextView = null;
    private TextView yearTextView = null;
    private TextView releasedTextView = null;
    private TextView directorTextView = null;
    private TextView writerTextView = null;
    private TextView actorsTextView = null;
    private TextView plotTextView = null;
    private TextView ratingsTextView = null;
    private ImageView posterImageView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pelis);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        imdb = intent.getStringExtra("imdbID");


        // Toast.makeText(getBaseContext(), imdb, Toast.LENGTH_SHORT).show();

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        yearTextView = (TextView) findViewById(R.id.yearTextView);
        releasedTextView = (TextView) findViewById(R.id.releasedTextView);
        directorTextView = (TextView) findViewById(R.id.directorTextView);
        writerTextView = (TextView) findViewById(R.id.writerTextView);
        actorsTextView = (TextView) findViewById(R.id.actorsTextView);
        plotTextView = (TextView) findViewById(R.id.plotTextView);
        ratingsTextView = (TextView) findViewById(R.id.ratingsTextView);
        posterImageView = (ImageView) findViewById(R.id.posterImageView);


        obtenerInformacion();

    }

    private class ObtenerInformacionTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... voids) {

            try {

                JSONParser jsonParser = new JSONParser();
                //videos = jsonParser.getVideos("http://www.omdbapi.com/?s=mario%20bros&apikey=2b28d307&r=json", new Petition(Entity.NONE));

                video = jsonParser.getVideo(voids[0],
                        new Petition(Entity.NONE));

                if(video != null){
                    return true;
                } else {
                    return false;
                }

            } catch (Exception e) {
                return false;
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //moviesProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            //moviesProgressBar.setVisibility(View.GONE);

            if(aBoolean){
                setInfo();
            } else {
                Toast.makeText(getBaseContext(), "Hubo un error", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            //moviesProgressBar.setVisibility(View.GONE);
            Toast.makeText(getBaseContext(), "Cancelado", Toast.LENGTH_SHORT).show();
        }
    }


    private void setInfo(){

        Glide.with(getBaseContext()).load(video.getPoster()).into(posterImageView);


        titleTextView.setText(video.getTitle());
        yearTextView.setText(video.getYear());
        releasedTextView.setText(video.getDate());
        directorTextView.setText(video.getDirector());
        writerTextView.setText(video.getEscritor());
        actorsTextView.setText(video.getActores());
        plotTextView.setText(video.getPlot());

        String ratingString = "";
        List<Map<String, String>> ratings = video.getRatings();


        for (Map<String, String> rating:ratings
                ) {

            ratingString += rating.get("source") + ": " + rating.get("value") + "\n";

        }

        ratingsTextView.setText(ratingString);



        // Toast.makeText(getBaseContext(), video.toString(), Toast.LENGTH_SHORT).show();

    }

    private void obtenerInformacion(){

        if(NetworkConnection.isConnectionAvailable(getBaseContext())) {
            DetallePelis.ObtenerInformacionTask obtenerInformacionTask = new DetallePelis.ObtenerInformacionTask();
            obtenerInformacionTask.execute("http://www.omdbapi.com/?i=" + imdb +  "&apikey=2b28d307&r=json");
        } else {
            Toast.makeText(getBaseContext(), "No hay conexión", Toast.LENGTH_SHORT).show();
        }

    }

}
