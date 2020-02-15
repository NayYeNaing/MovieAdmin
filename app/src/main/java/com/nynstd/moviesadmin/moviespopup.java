package com.nynstd.moviesadmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class moviespopup extends DialogFragment {
    MovieModel mModel;
    String id;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference ref;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addmovies,container,false);

        final Spinner sp_movie_cat,sp_movie_series;
        final ArrayList<String> moviecatarray = new ArrayList();
        final ArrayList<String> movieseriesArray = new ArrayList<>();
        sp_movie_cat = view.findViewById(R.id.sp_moviecat);
        sp_movie_series = view.findViewById(R.id.sp_movieseries);
        final EditText moviename = view.findViewById(R.id.et_movietitle);
        final EditText movieImageLink = view.findViewById(R.id.et_movieimagelink);
        final EditText movieVideo = view.findViewById(R.id.et_movievideo);
        Button save_movie_btn = view.findViewById(R.id.btn_savemovie);
        Button cancel_movie = view.findViewById(R.id.btn_cancelMovie);

        ref = db.collection("categories");
        ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                moviecatarray.clear();
                for(DocumentSnapshot ds : queryDocumentSnapshots){
                    moviecatarray.add(ds.toObject(CategoriesModel.class).cat_Name);
                }
                ArrayAdapter<String> movieCatArrayAdaptor = new ArrayAdapter<>(getContext(),android.R.layout.simple_dropdown_item_1line,moviecatarray);
                sp_movie_cat.setAdapter(movieCatArrayAdaptor);
            }
        });

        ref = db.collection("Series");
        ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                movieseriesArray.clear();
                for (DocumentSnapshot ds : queryDocumentSnapshots){
                    movieseriesArray.add(ds.toObject(SeriesModel.class).title);
                }
                ArrayAdapter<String> moviesSeriesAA = new ArrayAdapter<>(getContext(),android.R.layout.simple_dropdown_item_1line,movieseriesArray);
                sp_movie_series.setAdapter(moviesSeriesAA);
            }
        });

        cancel_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        save_movie_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(moviename.getText().toString().equals("")&&movieImageLink.getText().toString().equals("")
                        &&movieVideo.getText().toString().equals("")){
                    Toast.makeText(getContext(),"Please Input Something First",Toast.LENGTH_LONG).show();
                }
                else {
                    MovieModel movieModel = new MovieModel();
                    movieModel.movieTitle = moviename.getText().toString();
                    movieModel.movieImageLink = movieImageLink.getText().toString();
                    movieModel.movieCat = moviecatarray.get(sp_movie_cat.getSelectedItemPosition());
                    movieModel.movieVideo = movieVideo.getText().toString();
                    ref = db.collection("Movies");

                    ref.add(movieModel);
                    Toast.makeText(getContext(),"Save Successfully",Toast.LENGTH_LONG).show();
                    moviename.setText("");
                    movieImageLink.setText("");
                    movieVideo.setText("");
                }
            }
        });
        return view;
    }
}
