package com.nynstd.moviesadmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class addseries extends DialogFragment {
    SeriesModel sModel;
    String id;
    EditText series_title,series_ImageLink,series_video;
    Button series_save ;
    ArrayList<String> catgetfromspinnerArray = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference ref ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addseries,container,false);

        final Spinner seriesspinner = view.findViewById(R.id.series_spinner);
        series_title = view.findViewById(R.id.series_title_et);
        series_ImageLink = view.findViewById(R.id.series_Image_et);
        series_video= view.findViewById(R.id.series_Video_et);
        series_save = view.findViewById(R.id.btn_series_save);
        db = FirebaseFirestore.getInstance();
        ref = db.collection("categories");

        if(sModel != null){
            series_title.setText(sModel.title);
            series_ImageLink.setText(sModel.image);
            series_video.setText(sModel.video);
        }

        ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                catgetfromspinnerArray.clear();
                for(DocumentSnapshot ds : queryDocumentSnapshots){
                   catgetfromspinnerArray.add(ds.toObject(CategoriesModel.class).cat_Name);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                        (getContext(),android.R.layout.simple_dropdown_item_1line,catgetfromspinnerArray);
                seriesspinner.setAdapter(arrayAdapter);
            }
        });

        series_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (series_title.getText().toString().equals("") && series_ImageLink.getText().toString().equals("") && series_video.getText().toString().equals("")){
                    Toast.makeText(getContext(),"Error",Toast.LENGTH_LONG).show();
                }
                else {
                    ref = db.collection("Series");
                    SeriesModel seriesModel = new SeriesModel();
                    seriesModel.title = series_title.getText().toString();
                    seriesModel.image = series_ImageLink.getText().toString();
                    seriesModel.video = series_video.getText().toString();
                    seriesModel.categories = catgetfromspinnerArray.get(seriesspinner.getSelectedItemPosition());
                    if (sModel != null){
                        ref.document(id).set(seriesModel);
                        Toast.makeText(getContext(),"Updated",Toast.LENGTH_LONG).show();
                        sModel = null;
                        id = "";
                    }
                    else {
                        ref.add(seriesModel);
                        Toast.makeText(getContext(),"Save Successfully",Toast.LENGTH_LONG).show();
                    }
                    series_title.setText("");
                    series_ImageLink.setText("");
                    series_video.setText("");
                }
            }
        });

        return view;
    }
}
