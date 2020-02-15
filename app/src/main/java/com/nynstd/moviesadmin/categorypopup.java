package com.nynstd.moviesadmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class categorypopup extends DialogFragment {
    CategoriesModel cModel;
    String id ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.categoriespopup,container,false);
        Button savecatbtn = view.findViewById(R.id.savecat);
        Button cancelbtn = view.findViewById(R.id.cancelcatbtn);
        final EditText edcat_name = view.findViewById(R.id.edt_cat);
        if(cModel != null){
            edcat_name.setText(cModel.cat_Name);
        }

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        savecatbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(edcat_name.getText().toString().equals("")){
                Toast.makeText(getContext(),"Enter Category",Toast.LENGTH_LONG).show();
                }
                else {
                     FirebaseFirestore db = FirebaseFirestore.getInstance();
                     CollectionReference ref = db.collection("categories");
                     CategoriesModel categoriesModel = new CategoriesModel();
                     categoriesModel.cat_Name = edcat_name.getText().toString();
                     if(cModel != null){
                         ref.document(id).set(categoriesModel);
                         Toast.makeText(getContext(),"Updated",Toast.LENGTH_LONG).show();
                         cModel = null;
                         id = "";
                     }
                     else {
                         ref.add(categoriesModel);
                         Toast.makeText(getContext(),"Saved",Toast.LENGTH_LONG).show();
                     }
                     edcat_name.setText("");
                }
            }
        });
        return view;
    }
}
