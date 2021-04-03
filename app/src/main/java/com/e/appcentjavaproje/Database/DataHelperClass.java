package com.e.appcentjavaproje.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.e.appcentjavaproje.Model.ModelItem;

import java.text.DateFormat;
import java.time.LocalDateTime;

public class DataHelperClass extends SQLiteOpenHelper {
    public static final String vt_ad = "todo.db";
    public static final String tablo = "gorevler";

    public DataHelperClass(@Nullable Context context) {
        super(context, vt_ad, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + tablo +
                "  (ID INTEGER PRIMARY KEY AUTOINCREMENT " +
                ", HedefAdi TEXT," +
                " aciklama TEXT," +
                " baslamaTarihi TEXT," +
                " bitisTarihi TEXT," +
                " oncelikDurumu TEXT," +
                " yapildiMi INTEGER)";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + tablo;
        db.execSQL(sql);
    }

    public long insertData(ModelItem modelItem) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("HedefAdi", modelItem.getHedefadi());
        contentValues.put("aciklama",modelItem.getAciklama() );
        contentValues.put("baslamaTarihi", modelItem.getBaslamaTarihi());
        contentValues.put("bitisTarihi", modelItem.getBitisTarihi());
        contentValues.put("oncelikDurumu", modelItem.getOncelikDurumu());
        contentValues.put("yapildiMi", modelItem.getYapildiMi());
        long result = db.insert(tablo, null, contentValues);
        return result;

    }

    public Cursor getData(String dateParametre){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res  = db.rawQuery("select * from gorevler where yapildiMi = 0 and bitisTarihi >= '"+dateParametre+"'", null );
        return res;
    }
    public Cursor getCompeteData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res  = db.rawQuery("select * from gorevler where yapildiMi = 1", null );
        return res;
    }
    public Cursor getIdData(int id){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res  = db.rawQuery("select * from gorevler where ID = "+ id, null );
        return res;
    }

    public boolean updateData (String id,String ad, String aciklama,String bitis,String oncelik,int yapildiMi ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", id);
        contentValues.put("HedefAdi", ad);
        contentValues.put("aciklama",aciklama );

        contentValues.put("bitisTarihi", bitis);
        contentValues.put("oncelikDurumu", oncelik);
        contentValues.put("yapildiMi", yapildiMi);

        db.update(tablo, contentValues,"ID = ? ", new  String[] { id });

        return true;
    }
    public boolean updateData (String id,int yapildiMi ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", id);
        contentValues.put("yapildiMi", yapildiMi);

        db.update(tablo, contentValues,"ID = ? ", new  String[] { id });

        return true;
    }

    public Integer delteData (String id ){

        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(tablo , "ID = ?", new String[] {id});


    }




}