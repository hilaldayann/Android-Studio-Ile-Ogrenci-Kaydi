package com.gop.ogrencikayitprojesi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HD_Ogrenciler extends AppCompatActivity {
    FirebaseAuth hd_mUser;
    RecyclerView hd_analiste;
    ArrayList<HD_OgrenciBilgi> hd_ogrenciListe;
    FirebaseFirestore hd_db;
    HD_OgrenciAdapter hd_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_d__ogrenciler);
        hd_mUser = FirebaseAuth.getInstance();
        hd_analiste = findViewById(R.id.hd_recyclerView);
        hd_ogrenciListe = new ArrayList<>();
        hd_db = FirebaseFirestore.getInstance();

        hd_adapter = new HD_OgrenciAdapter(hd_ogrenciListe);
        hd_analiste.setHasFixedSize(true);
        hd_analiste.setLayoutManager(new LinearLayoutManager(HD_Ogrenciler.this));
        hd_analiste.setAdapter(hd_adapter);

        hd_ogrencileriGetir();

    }

    public void hd_ogrencileriGetir(){

        hd_db.collection("HD_OgrenciKayitProjesi").orderBy("ogrNo", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("getOgrenciler", document.getId() + " => " + document.getData());
                                Log.d("getOgrenciler", document.get("ogrNo").toString());

                                hd_ogrenciListe.add(new HD_OgrenciBilgi(document.get("ogrNo").toString(),
                                        document.get("ogrAdi").toString(),
                                        document.get("ogrSoyadi").toString(),
                                        document.get("ePosta").toString(),
                                        document.get("url").toString()));
                            }

                            Log.d("getOgrenciler", hd_ogrenciListe.size()+"");

                            hd_adapter.notifyDataSetChanged();
                        } else {
                            Log.d("getOgrenciler", "Ogrencileri Sunucudan Çekerken Bir Hata Oluştu...", task.getException());
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hd_kullanici_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.item1:
                Toast.makeText(getApplicationContext(),"Hakkımızda Seçildi",Toast.LENGTH_LONG).show();
                return true;

            case R.id.yeniogrenci:
                Toast.makeText(getApplicationContext(),"Yeni Bir Ogrenci Eklenecek",Toast.LENGTH_LONG).show();
                startActivity(new Intent(HD_Ogrenciler.this, HD_YeniOgrenciEkle.class));
                return true;

            case R.id.cikisyap:
                Toast.makeText(getApplicationContext(),"Çıkış Yapılacak",Toast.LENGTH_LONG).show();
                hd_mUser.signOut();
                startActivity(new Intent(HD_Ogrenciler.this, HD_GirisSayfasi.class));
                finish();
                return true;

            case R.id.anasayfa:
                Toast.makeText(getApplicationContext(),"AnaSayfa Seçildi",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}