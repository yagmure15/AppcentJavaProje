package com.e.appcentjavaproje;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.e.appcentjavaproje.Database.DataHelperClass;
import com.e.appcentjavaproje.Model.ModelAdapter;
import com.e.appcentjavaproje.Model.ModelItem;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class CompActivity extends AppCompatActivity {
    private DataHelperClass myDb;
    private RecyclerView mRecyclerViewTamamlanan;
    private ArrayList<ModelItem> mModelListTamamlanan;
    private ModelAdapter mAdapterTamamlanan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp);
        initAll();
        tamamlananVerileriGoster();
    }
    public void tamamlananVerileriGoster(){

        Cursor res = myDb.getCompeteData();
        if (res.getCount() == 0){
            return;
        } else if ( res.getCount() > 0 ){

            while (res.moveToNext()){

                mModelListTamamlanan.add(0,new ModelItem(
                        res.getInt(0),
                        res.getString(1),
                        res.getString(2),
                        res.getString(3),
                        res.getString(4),
                        res.getString(5),
                        res.getInt(6)


                ));

            }

            mAdapterTamamlanan = new ModelAdapter(mModelListTamamlanan);
            mRecyclerViewTamamlanan.hasFixedSize();
            mRecyclerViewTamamlanan.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerViewTamamlanan.setAdapter(mAdapterTamamlanan);

            mAdapterTamamlanan.setOnItemClickListener(new ModelAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    int  ID = mModelListTamamlanan.get(position).getId();
                    Intent intent = new Intent(CompActivity.this, TargetDetails.class);
                    intent.putExtra("ID",ID);
                    startActivity(intent);
                }

                @Override
                public void onDeleteClick(final int position) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CompActivity.this);
                    builder.setTitle(R.string.yapilan_gorevi_sil);
                    builder.setPositiveButton(R.string.sil, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // do somting
                            if (mModelListTamamlanan!= null){

                                int ID = mModelListTamamlanan.get(position).getId();
                                myDb.delteData(String.valueOf(ID));
                                Toast.makeText(CompActivity.this, String.valueOf(ID), Toast.LENGTH_SHORT).show();
                                removeItem(position);
                            }

                        }
                    });
                    builder.setNegativeButton(R.string.vazgec, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // do somting
                        }
                    });
                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                        }
                    });
                    builder.show();
                }

                @Override
                public void onIsChecked(int position) {


                }
            });
        }
    }
    private void initAll(){
        getSupportActionBar().setTitle("Tamamlanan Görevler");
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.toolbarbg));
        mRecyclerViewTamamlanan = findViewById(R.id.recyclerView2);
        myDb = new DataHelperClass(this);
      //  gununTarihi = findViewById(R.id.gunun_tarihi);
        mModelListTamamlanan = new ArrayList<>();
    }
    public void removeItem(int position) {
        myDb.delteData(String.valueOf(position));
        mModelListTamamlanan.remove(position);
        mAdapterTamamlanan.notifyItemRemoved(position);


    }
}