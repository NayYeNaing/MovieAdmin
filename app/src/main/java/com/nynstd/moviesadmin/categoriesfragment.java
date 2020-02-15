package com.nynstd.moviesadmin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class categoriesfragment extends Fragment {
    FirebaseFirestore db;
    CollectionReference ref;
    ArrayList<String> documents;
    ListView listView_cat;
    ProgressBar progressBar;

    public categoriesfragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.categoriesfragments,container,false);
        progressBar = view.findViewById(R.id.progressbar);
        final EditText edt_search = view.findViewById(R.id.cat_search);
        FloatingActionButton catpop = view.findViewById(R.id.catpop);
        catpop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categorypopup categorypopup = new categorypopup();
                categorypopup.show(getFragmentManager(),"CategoryAdd");
            }
        });
        listView_cat = view.findViewById(R.id.cat_list);
        documents=new ArrayList<>();
        loadData();

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!edt_search.getText().toString().equals("")){
                    searchdata(charSequence.toString());
            }
                else
                {
                    loadData();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void searchdata(String s) {
        ref.whereEqualTo("cat_Name",s).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<CategoriesModel> categoriesModelArrayList = new ArrayList<CategoriesModel>();
                documents.clear();
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    documents.add(documentSnapshot.getId());
                    CategoriesModel c = documentSnapshot.toObject(CategoriesModel.class);
                    categoriesModelArrayList.add(c);
                }
                catAdapter catAdapter = new catAdapter(categoriesModelArrayList);
                listView_cat.setAdapter(catAdapter);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void loadData(){
        db = FirebaseFirestore.getInstance();
        ref = db.collection("categories");
        ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<CategoriesModel> categoriesModelArrayList = new ArrayList<CategoriesModel>();
                documents.clear();
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    documents.add(documentSnapshot.getId());
                    CategoriesModel c = documentSnapshot.toObject(CategoriesModel.class);
                    categoriesModelArrayList.add(c);
                }
                catAdapter catAdapter = new catAdapter(categoriesModelArrayList);
                listView_cat.setAdapter(catAdapter);
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private class catAdapter extends BaseAdapter{
        ArrayList<CategoriesModel> arrayList_cat = new ArrayList<CategoriesModel>();

        public catAdapter(ArrayList<CategoriesModel> arrayList_cat) {
            this.arrayList_cat = arrayList_cat;
        }

        @Override
        public int getCount() {
            return arrayList_cat.size();
        }

        @Override
        public Object getItem(int i) {
            return arrayList_cat.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = getLayoutInflater();
            View view1 = inflater.inflate(R.layout.categorieslist,null);
            TextView tv_catno = view1.findViewById(R.id.cat_no);
            tv_catno.setText((i+1)+"");
            final CategoriesModel temp = arrayList_cat.get(i);
            final TextView tv_catname = view1.findViewById(R.id.cat_name);
            tv_catname.setText(temp.cat_Name);
            final LinearLayout cat_list = view1.findViewById(R.id.cat_list_layout);

            cat_list.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(getContext(),tv_catname);
                    MenuInflater menuInflater = popupMenu.getMenuInflater();
                    menuInflater.inflate(R.menu.popupeditcategories,popupMenu.getMenu());
                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if(menuItem.getItemId() == R.id.delete_cat){
                                ref.document(documents.get(i)).delete();
                                loadData();
                            }
                            if(menuItem.getItemId() == R.id.edit_cat){
                                categorypopup categorypopup = new categorypopup();
                                categorypopup.cModel= temp;
                                categorypopup.id =documents.get(i);
                                categorypopup.show(getFragmentManager(),"cat_edited");
                            }
                            return true;
                        }
                    });

                    return true;
                }
            });
            return view1;
        }
    }
}
