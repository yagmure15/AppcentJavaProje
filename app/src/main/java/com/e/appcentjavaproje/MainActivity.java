package com.e.appcentjavaproje;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.ColorSpace;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.e.appcentjavaproje.Database.DataHelperClass;
import com.e.appcentjavaproje.Model.ModelAdapter;
import com.e.appcentjavaproje.Model.ModelItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private FloatingActionButton btnAddTarget;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    private EditText hedefAdi,aciklama;
    private ArrayList<ModelItem> mModelList;
    private ModelAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView tarihsec,gununTarihi;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private DataHelperClass myDb;
    private DateFormat kaldf , df ;
    private Calendar kal;
    private int dateAy, dateYil, dateGun,rasYil,rasAy,rasGun,check,ID,radioId;
    private DatePickerDialog datePickerDialog;
    private String sonTarih, ilkTarih,strHedef,strAciklama,oncelik;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        initAll();
        drawNavInit();
        isUserNull();
        getDate(gununTarihi);
        builtRecyclerView();
        showData();
        addTarget();


    }

    private void builtRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mModelList = new ArrayList<ModelItem>();
        mModelList = getData2();
        mAdapter = new ModelAdapter(mModelList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
    public void veriEkle(String hedefadi, String aciklama,String baslangictarihi, String bitistarihi, String oncelik, int yapildimi) {

        ModelItem raporlarModel = new ModelItem(hedefadi,aciklama, baslangictarihi,bitistarihi,oncelik,yapildimi);
        DataHelperClass dataHelperClass = new DataHelperClass(MainActivity.this);
        dataHelperClass.insertData(raporlarModel);
        showData();
    }
    private void addTarget() {
        btnAddTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater layoutInflater = MainActivity.this.getLayoutInflater();
                View view = layoutInflater.inflate(R.layout.dialog_hedef_ekle, null);
                hedefAdi = view.findViewById(R.id.hedef_adi);
                tarihsec = view.findViewById(R.id.tarihsec);
                aciklama = view.findViewById(R.id.aciklama);
                radioGroup = view.findViewById(R.id.radio_priority);
                getDate(tarihsec);
                builder.setView(view);
                tarihsec.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datepickerChooser(tarihsec);
                    }
                });

                builder.setPositiveButton(R.string.ekle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        strHedef = hedefAdi.getText().toString();
                        strAciklama = String.valueOf(aciklama.getText());
                        radioId = radioGroup.getCheckedRadioButtonId();
                        radioButton = view.findViewById(radioId);
                        oncelik = (String) radioButton.getText().toString();
                        veriEkle(strHedef,strAciklama,ilkTarih,sonTarih,oncelik,0);
                    }
                });
                builder.setNegativeButton(R.string.vazgec, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                });
                builder.show();
            }
        });
    }

    public ArrayList<ModelItem> getData2() {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, -1);
        dt = c.getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatter.format(dt);

        Cursor res = myDb.getData(strDate);
        if (res.getCount() == 0) {
            return mModelList;
        } else if (res.getCount() > 0) {
            while (res.moveToNext()) {
                mModelList.add(0, new ModelItem(
                        res.getInt(0),
                        res.getString(1),
                        res.getString(2),
                        res.getString(3),
                        res.getString(4),
                        res.getString(5),
                        res.getInt(6)
                ));
            }
        }
        return mModelList;
    }

    public void showData(){
            builtRecyclerView();
            mAdapter.setOnItemClickListener(new ModelAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    int  ID = mModelList.get(position).getId();
                    Intent intent = new Intent(MainActivity.this, TargetDetails.class);
                    intent.putExtra("ID",ID);
                    startActivity(intent);
                }
                @Override
                public void onDeleteClick(final int position) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(R.string.yapilan_gorevi_sil);
                    builder.setPositiveButton(R.string.sil, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // do somting
                            if (mModelList!= null){
                                int ID = mModelList.get(position).getId();
                                myDb.delteData(String.valueOf(ID));
                                Toast.makeText(MainActivity.this, "Görev Silindi", Toast.LENGTH_SHORT).show();
                                removeItem(position);
                            }

                        }
                    });
                    builder.setNegativeButton(R.string.vazgec, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
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
                    if (mModelList!= null){
                        check = mModelList.get(position).getYapildiMi();
                        ID = mModelList.get(position).getId();
                        if (check == 0){
                            check=1;
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    removeItem(position);
                                    myDb.updateData(String.valueOf(ID),check);
                                }
                            }, 1000);
                        }
                    }

                }
            });

    }

    private void getDate(TextView tv) {
        Calendar mevcutTarih=Calendar.getInstance();
        dateYil = mevcutTarih.get(Calendar.YEAR);
        dateAy = mevcutTarih.get(Calendar.MONTH);
        dateGun = mevcutTarih.get(Calendar.DAY_OF_MONTH);
        df = new SimpleDateFormat("dd MMMM E", Locale.getDefault());
        ilkTarih = df.format(mevcutTarih.getTime());
        sonTarih = df.format(mevcutTarih.getTime());
        tv.setText(ilkTarih);
    }
    private void datepickerChooser(TextView v) {
        Locale trlocale= new Locale("tr-TR");
        datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                kal =Calendar.getInstance();
                rasYil= kal.get(Calendar.YEAR);
                rasAy= kal.get(Calendar.MONTH);
                rasGun = kal.get(Calendar.DAY_OF_MONTH);
                kal.set(year,month,dayOfMonth);
                kaldf = new SimpleDateFormat("dd MMMM YYYY", Locale.getDefault());
                sonTarih = kaldf.format(kal.getTime());
                v.setText(sonTarih);
            }
        },dateYil,dateAy,dateGun);
        datePickerDialog.show();
    }
    private void initAll(){
        myDb = new DataHelperClass(this);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        btnAddTarget = findViewById(R.id.hedefekle);
        mRecyclerView = findViewById(R.id.recyclerView);
        mModelList = new ArrayList<>();
        gununTarihi = findViewById(R.id.gunun_tarihi);



    }
    private void isUserNull() {
        if (currentUser==null){
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }
    }
    @SuppressLint("WrongConstant")
    private void UserMenuSelector(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_anasayfa:
                drawerLayout.closeDrawer(Gravity.START);
                break;
            case R.id.nav_tamamlanan:
                gitTamamlanan();
                break;
            case R.id.nav_cikis:
                googleSignOut();
                break;

        }
    }
    private void gitTamamlanan() {
        Intent i = new Intent(MainActivity.this, CompActivity.class);
        startActivity(i);
    }

    //BU KISIM DA SOLDAKİ NAVİGASYON BARIN BUTON İLE AÇILIP KAPANMASI İÇİN
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void googleSignOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
    }
    private void drawNavInit(){
        //YANDAN AÇILAN MENU İLE İLGİLİ KODLAR/
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.toolbarbg));
        getSupportActionBar().getDisplayOptions();
        navigationView = (NavigationView) findViewById(R.id.navigation_layout);
        View navView = navigationView.inflateHeaderView(R.layout.navigation_header);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                UserMenuSelector(item);
                return false;
            }
        });

    }
    public void removeItem(int position) {
        myDb.delteData(String.valueOf(position));
        mModelList.remove(position);
        mAdapter.notifyItemRemoved(position);


    }


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            Collections.swap(mModelList, fromPosition, toPosition);

            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };




}