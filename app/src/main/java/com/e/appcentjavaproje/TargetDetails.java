package com.e.appcentjavaproje;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.e.appcentjavaproje.Database.DataHelperClass;
import com.e.appcentjavaproje.Model.ModelAdapter;
import com.e.appcentjavaproje.Model.ModelItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static android.os.Build.ID;

public class TargetDetails extends AppCompatActivity {
    private DataHelperClass myDb;
    private ArrayList<ModelItem> mModelList;
    private EditText hedefAdi, aciklama;
    private Button guncelle, sil, tamamlandi;
    private ImageView imageView;
    private String baglangicTarihi, bitisTarihi, oncelik,txtHedefAdi,txtAciklama;
    private int yapildiMi,id,radioId;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private DatePickerDialog datePickerDialog;
    private Calendar kal;
    private DateFormat kaldf,df;
    private int dateAy, dateYil, dateGun;
    private TextView tvDatePic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target_details);

        initData();
        getDate();
        verileriGoster();



        sil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.delteData(String.valueOf(id));
                anaSayfayaDon();
            }
        });
        tamamlandi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yapildiMi ==0){
                    yapildiMi=1;
                }else {
                    yapildiMi =0;
                }
            myDb.updateData(String.valueOf(id),yapildiMi);
                anaSayfayaDon();
            }
        });
        guncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtHedefAdi = hedefAdi.getText().toString();
                txtAciklama = aciklama.getText().toString();

                radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                oncelik = (String) radioButton.getText().toString();
                Toast.makeText(TargetDetails.this, "Güncellendi", Toast.LENGTH_SHORT).show();
                myDb.updateData(String.valueOf(id),txtHedefAdi,txtAciklama,"güncelleme",oncelik,yapildiMi);
                anaSayfayaDon();
            }

        });
        tvDatePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepickerChooser(tvDatePic);
            }
        });



    }
    private void getDate() {
        Calendar mevcutTarih=Calendar.getInstance();
        dateYil = mevcutTarih.get(Calendar.YEAR);
        dateAy = mevcutTarih.get(Calendar.MONTH);
        dateGun = mevcutTarih.get(Calendar.DAY_OF_MONTH);
        df = new SimpleDateFormat("dd MMMM E", Locale.getDefault());

        tvDatePic.setText(df.format(mevcutTarih.getTime()));

    }
    private void initData() {
        tvDatePic = findViewById(R.id.tarihsec2);
        hedefAdi = findViewById(R.id.hedef_adi);
        aciklama = findViewById(R.id.aciklama);
        guncelle = findViewById(R.id.guncelle);
        sil = findViewById(R.id.sil);
        tamamlandi = findViewById(R.id.tamamlandi);
        radioGroup = findViewById(R.id.radio_priority);
        imageView = findViewById(R.id.image_tc);
        mModelList = new ArrayList<>();
        myDb = new DataHelperClass(this);
        Intent intent = getIntent();
        id = intent.getIntExtra("ID",0);

        getSupportActionBar().setTitle("Görev Detayı");
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.toolbarbg));

    }

    private void anaSayfayaDon() {
        Intent intent1 = new Intent(TargetDetails.this,MainActivity.class);
        startActivity(intent1);
        finish();
    }

    public void verileriGoster(){

        Cursor res = myDb.getIdData(id);
        if (res.getCount() == 0){
            Toast.makeText(this, "veriler geliyor", Toast.LENGTH_SHORT).show();

            //message
            return;
        } else if ( res.getCount() > 0 ){

            while (res.moveToNext()){

                mModelList.add(0,new ModelItem(
                        res.getInt(0),
                        res.getString(1),
                        res.getString(2),
                        res.getString(3),
                        res.getString(4),
                        res.getString(5),
                        res.getInt(6)


                ));


            }

            hedefAdi.setText(mModelList.get(0).getHedefadi());
            aciklama.setText(mModelList.get(0).getAciklama());

            oncelik = mModelList.get(0).getOncelikDurumu();

            switch (oncelik) {
                case "Normal":
                    radioButton = findViewById(R.id.radioMale);
                    radioButton.setChecked(true);
                    break;
                case "Yüksek":
                    radioButton = findViewById(R.id.radioFemale);
                    radioButton.setChecked(true);
                    break;
            }

            yapildiMi = mModelList.get(0).getYapildiMi();

            switch (yapildiMi){
                case 0:
                    tamamlandi.setText("Tamamlandı");
                    imageView.setImageResource(R.drawable.wrong);
                    break;
                case 1:
                    tamamlandi.setText("Tamamlanmadı");
                    imageView.setImageResource(R.drawable.tick);
                    break;
            }

        }
    }

    private void datepickerChooser(TextView v) {

        getDate();
        Locale trlocale= new Locale("tr-TR");

        datePickerDialog = new DatePickerDialog(TargetDetails.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                kal = Calendar.getInstance();
                kal.set(year,month,dayOfMonth);
                kaldf = new SimpleDateFormat("dd MMMM YYYY", Locale.getDefault());

                v.setText(kaldf.format(kal.getTime()));




            }
        },dateYil,dateAy,dateGun);

        datePickerDialog.show();


    }
}