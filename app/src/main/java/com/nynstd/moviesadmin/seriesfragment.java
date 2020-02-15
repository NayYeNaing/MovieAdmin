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
import java.util.zip.Inflater;

public class seriesfragment extends Fragment {
    public seriesfragment(){

    }
    public static ArrayList<String> docId = new ArrayList<>();
    ProgressBar progressBar_series;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.seriesfragment,container,false);
        FloatingActionButton seriesfloatbtn = view.findViewById(R.id.addseriesbtn);
        final EditText search_series = view.findViewById(R.id.searchseries_et);
        progressBar_series = view.findViewById(R.id.seriesprogressbar);
        recyclerView = view.findViewById(R.id.series_RV);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference sref = db.collection("Series");
        sref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<SeriesModel> seriesModelsArray = new ArrayList<>();
                docId.clear();
                for (DocumentSnapshot d : queryDocumentSnapshots){
                    seriesModelsArray.add(d.toObject(SeriesModel.class));
                    docId.add(d.getId());
                }
                SeriesAdapter seriesAdapter = new SeriesAdapter(seriesModelsArray);
                LinearLayoutManager lm = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
                recyclerView.setAdapter(seriesAdapter);
                recyclerView.setLayoutManager(lm);
                progressBar_series.setVisibility(View.GONE);
            }
        });

        search_series.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(search_series.getText().toString().equals("")){
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference sref = db.collection("Series");
                    sref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            ArrayList<SeriesModel> seriesModelsArray = new ArrayList<>();
                            for (DocumentSnapshot d : queryDocumentSnapshots){
                                seriesModelsArray.add(d.toObject(SeriesModel.class));
                            }
                            SeriesAdapter seriesAdapter = new SeriesAdapter(seriesModelsArray);
                            LinearLayoutManager lm = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
                            recyclerView.setAdapter(seriesAdapter);
                            recyclerView.setLayoutManager(lm);
                            progressBar_series.setVisibility(View.GONE);
                        }
                    });
                }
                else {
                    sref.whereEqualTo("title",search_series.getText().toString()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            ArrayList<SeriesModel> seriesModelsArray = new ArrayList<>();
                            for (DocumentSnapshot d : queryDocumentSnapshots){
                                seriesModelsArray.add(d.toObject(SeriesModel.class));
                            }
                            SeriesAdapter seriesAdapter = new SeriesAdapter(seriesModelsArray);
                            LinearLayoutManager lm = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
                            recyclerView.setAdapter(seriesAdapter);
                            recyclerView.setLayoutManager(lm);
                            progressBar_series.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

        seriesfloatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addseries addseries = new addseries();
                addseries.show(getFragmentManager(),"addseries");
            }
        });

        return view;
    }

    public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.SeriesHolder>{
        ArrayList<SeriesModel> arrayList_seriesmodel = new ArrayList<>();

        public SeriesAdapter(ArrayList<SeriesModel> arrayList_seriesmodel) {
            this.arrayList_seriesmodel = arrayList_seriesmodel;
        }

        @NonNull
        @Override
        public SeriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view2 = inflater.inflate(R.layout.seriesrvstyle,parent,false);
            SeriesHolder seriesHolder = new SeriesHolder(view2);
            return seriesHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final SeriesHolder holder, final int position) {
            holder.sr.setText(position+1+"");
            holder.title.setText(arrayList_seriesmodel.get(position).title);
            Glide.with(getContext()).load(arrayList_seriesmodel.get(position).image).into(holder.seriesImage);
            holder.slayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(getContext(),holder.title);
                    popupMenu.getMenuInflater().inflate(R.menu.popupeditcategories,popupMenu.getMenu());
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if(menuItem.getItemId() == R.id.delete_cat){
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                CollectionReference sref = db.collection("Series");
                                sref.document(seriesfragment.docId.get(position)).delete();
                                sref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        ArrayList<SeriesModel> seriesModelsArray = new ArrayList<>();
                                        docId.clear();
                                        for (DocumentSnapshot d : queryDocumentSnapshots){
                                            seriesModelsArray.add(d.toObject(SeriesModel.class));
                                            docId.add(d.getId());
                                        }
                                        SeriesAdapter seriesAdapter = new SeriesAdapter(seriesModelsArray);
                                        LinearLayoutManager lm = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
                                        recyclerView.setAdapter(seriesAdapter);
                                        recyclerView.setLayoutManager(lm);
                                        progressBar_series.setVisibility(View.GONE);
                                    }
                                });

                            }
                            if(menuItem.getItemId() == R.id.edit_cat){
                                addseries addseries = new addseries();
                                addseries.sModel = arrayList_seriesmodel.get(position);
                                addseries.id = docId.get(position);
                                addseries.show(getFragmentManager(),"Edit");
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
            return arrayList_seriesmodel.size();
        }

        public class SeriesHolder extends  RecyclerView.ViewHolder{
            TextView sr,title;
            ImageView seriesImage;
            LinearLayout slayout;
            public SeriesHolder(@NonNull View itemView) {
                super(itemView);
                sr = itemView.findViewById(R.id.series_no);
                title = itemView.findViewById(R.id.series_title_tv);
                seriesImage = itemView.findViewById(R.id.Series_image);
                slayout = itemView.findViewById(R.id.sereis_list_layout);
            }
        }
    }
}



