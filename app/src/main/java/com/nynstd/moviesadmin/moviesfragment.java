package com.nynstd.moviesadmin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class moviesfragment extends Fragment {
    public moviesfragment(){

    }

    ProgressBar progressBar;
    RecyclerView recyclerView;
    public static ArrayList<String> arrayList = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference mref;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.moviesfragments,container,false);
        FloatingActionButton addmovies = view.findViewById(R.id.addmoviesbtn);
        final EditText search_movies = view.findViewById(R.id.searchmovies_et);
        final ProgressBar progressBar = view.findViewById(R.id.moviesprogressbar);
        final RecyclerView recyclerView = view.findViewById(R.id.movies_RV);


        mref = db.collection("Movies");
        mref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<MovieModel> movieModelsArrayList = new ArrayList<>();
                arrayList.clear();
                for(DocumentSnapshot ds : queryDocumentSnapshots){
                    movieModelsArrayList.add(ds.toObject(MovieModel.class));
                    arrayList.add(ds.getId());
                }
                moviesAdapter moviesAdapter = new moviesAdapter(movieModelsArrayList);
                LinearLayoutManager lm = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
                recyclerView.setAdapter(moviesAdapter);
                recyclerView.setLayoutManager(lm);
                progressBar.setVisibility(View.GONE);
            }
        });

        search_movies.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String name = search_movies.getText().toString();
                mref.orderBy("movieTitle").startAt(name).endAt(name+'\uf8ff').addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        ArrayList<MovieModel> movieModelsArrayList = new ArrayList<>();
                        arrayList.clear();
                        for(DocumentSnapshot ds : queryDocumentSnapshots){
                            movieModelsArrayList.add(ds.toObject(MovieModel.class));
                        }
                        moviesAdapter moviesAdapter = new moviesAdapter(movieModelsArrayList);
                        LinearLayoutManager lm = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
                        recyclerView.setAdapter(moviesAdapter);
                        recyclerView.setLayoutManager(lm);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        addmovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moviespopup moviespopup = new moviespopup();
                moviespopup.show(getFragmentManager(),"moviespopup");
            }
        });
        return view;
    }

    public class moviesAdapter extends RecyclerView.Adapter<moviesAdapter.movieHolder>{
        ArrayList<MovieModel> MovieModelArrayList = new ArrayList<>();

        public moviesAdapter(ArrayList<MovieModel> movieModelArrayList) {
            MovieModelArrayList = movieModelArrayList;
        }

        @NonNull
        @Override
        public movieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.seriesrvstyle,parent,false);
            movieHolder movieHolder = new movieHolder(view);
            return movieHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final movieHolder holder, final int position) {
            holder.sr.setText(position+1+"");
            holder.title.setText(MovieModelArrayList.get(position).movieTitle);
            Glide.with(getContext()).load(MovieModelArrayList.get(position).movieImageLink).into(holder.moviesImage);
            holder.mlayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(getContext(),holder.title);
                    popupMenu.getMenuInflater().inflate(R.menu.popupeditcategories,popupMenu.getMenu());
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if(menuItem.getItemId() == R.id.delete_cat){
                                mref = db.collection("Movies");
                                mref.document(moviesfragment.arrayList.get(position)).delete();
                                mref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        ArrayList<MovieModel> movieModelsArrayList = new ArrayList<>();
                                        arrayList.clear();
                                        for(DocumentSnapshot ds : queryDocumentSnapshots){
                                            movieModelsArrayList.add(ds.toObject(MovieModel.class));
                                            arrayList.add(ds.getId());
                                        }
                                        moviesAdapter moviesAdapter = new moviesAdapter(movieModelsArrayList);
                                        LinearLayoutManager lm = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
                                        recyclerView.setAdapter(moviesAdapter);
                                        recyclerView.setLayoutManager(lm);
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                            }
                            if(menuItem.getItemId() == R.id.edit_cat){
                                moviespopup moviespopup = new moviespopup();
                                moviespopup.mModel = MovieModelArrayList.get(position);
                                moviespopup.id = arrayList.get(position);
                                moviespopup.show(getFragmentManager(),"Edit");
                            }
                            return true;
                        }
                    });
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return MovieModelArrayList.size();
        }

        public class movieHolder extends RecyclerView.ViewHolder{
            TextView sr,title;
            ImageView moviesImage;
            LinearLayout mlayout;
            public movieHolder(@NonNull View itemView) {
                super(itemView);
                sr = itemView.findViewById(R.id.series_no);
                title = itemView.findViewById(R.id.series_title_tv);
                moviesImage = itemView.findViewById(R.id.Series_image);
                mlayout = itemView.findViewById(R.id.sereis_list_layout);
            }
        }
    }
}
