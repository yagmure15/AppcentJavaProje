package com.e.appcentjavaproje.Model;



public class ModelItem {


    String hedefadi, baslamaTarihi, aciklama, OncelikDurumu, bitisTarihi;
    int id;

    int yapildiMi;


    public ModelItem() {
    }
    public ModelItem( int id , String hedefadi,  String aciklama, String baslamaTarihi,String bitisTarihi, String oncelikDurumu, int yapildiMi) {
        this.hedefadi = hedefadi;
        this.id = id;
        this.baslamaTarihi = baslamaTarihi;
        this.aciklama = aciklama;

        OncelikDurumu = oncelikDurumu;
        this.bitisTarihi = bitisTarihi;

        this.id = id;
        this.yapildiMi = yapildiMi;
    }

    public ModelItem(  String hedefadi,  String aciklama, String baslamaTarihi,String bitisTarihi, String oncelikDurumu, int yapildiMi) {
        this.hedefadi = hedefadi;

        this.baslamaTarihi = baslamaTarihi;
        this.aciklama = aciklama;

        OncelikDurumu = oncelikDurumu;
        this.bitisTarihi = bitisTarihi;

        this.id = id;
        this.yapildiMi = yapildiMi;
    }

    public String getHedefadi() {
        return hedefadi;
    }

    public void setHedefadi(String hedefadi) {
        this.hedefadi = hedefadi;
    }

    public String getBaslamaTarihi() {
        return baslamaTarihi;
    }

    public void setBaslamaTarihi(String baslamaTarihi) {
        this.baslamaTarihi = baslamaTarihi;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }




    public String getOncelikDurumu() {
        return OncelikDurumu;
    }

    public void setOncelikDurumu(String oncelikDurumu) {
        OncelikDurumu = oncelikDurumu;
    }

    public String getBitisTarihi() {
        return bitisTarihi;
    }

    public void setBitisTarihi(String bitisTarihi) {
        this.bitisTarihi = bitisTarihi;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYapildiMi() {
        return yapildiMi;
    }

    public void setYapildiMi(int yapildiMi) {
        this.yapildiMi = yapildiMi;
    }
}