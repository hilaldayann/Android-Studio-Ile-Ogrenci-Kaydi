package com.gop.ogrencikayitprojesi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HD_YeniOgrenciEkle extends AppCompatActivity {
    ImageView hd_foto;
    EditText hd_ogrNo, hd_ogrAdi, hd_ogrSoyadi, hd_dogumTarihi, hd_ogrTel, hd_ePosta, hd_adres;
    RadioGroup hd_rgCinsiyet;
    String hd_secilenCinsiyet;
    Spinner hd_bolum, hd_hazirlik;
    StringBuilder hd_sb;
    Button hd_ogrenciEkle;
    Bitmap hd_fotom;
    Uri hd_fotoData;
    FirebaseStorage hd_mstorage;
    FirebaseFirestore hd_veritabani;
    String hd_fotoURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_d__yeni_ogrenci_ekle);
        hd_foto = findViewById(R.id.hd_image_ogrenci2);
        hd_ogrNo = findViewById(R.id.hd_etxt_ogrNoo2);
        hd_ogrAdi = findViewById(R.id.hd_etxt_ogrAdi2);
        hd_ogrSoyadi = findViewById(R.id.hd_etxt_ogrSoyadi2);
        hd_dogumTarihi = findViewById(R.id.hd_etxt_dogumTarihi2);
        hd_ogrTel = findViewById(R.id.hd_etxt_telefon);
        hd_ePosta = findViewById(R.id.hd_etxt_ePosta2);
        hd_adres = findViewById(R.id.hd_etxt_adres2);
        hd_rgCinsiyet = findViewById(R.id.hd_radioGroup_cinsiyet);
        hd_bolum = findViewById(R.id.hd_spinner_bolum2);
        hd_hazirlik = findViewById(R.id.hd_spinner_hazirlik2);


        hd_ogrenciEkle = findViewById(R.id.hd_btn_kayitOlll2);
        hd_mstorage = FirebaseStorage.getInstance();
        hd_veritabani = FirebaseFirestore.getInstance();

        hd_ogrenciEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hd_ogrenciEkle_click(view);
            }
        });

        hd_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hd_fotoSec();
            }
        });

        Button hd_anasayfayaDon = findViewById(R.id.hd_btn_don);
        hd_anasayfayaDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HD_YeniOgrenciEkle.this,HD_Ogrenciler.class));
                finish();
            }
        });

    }

    public void hd_fotoSec(){
        Toast.makeText(HD_YeniOgrenciEkle.this,"Fotoğraf Seçim İşlemi Çağırıldı",
                Toast.LENGTH_SHORT).show();
        if(ContextCompat.checkSelfPermission(HD_YeniOgrenciEkle.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(HD_YeniOgrenciEkle.this, new String[]{Manifest.permission.
                    READ_EXTERNAL_STORAGE},10);
        }else{
            Intent hd_intentfoto = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(hd_intentfoto,15);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if(requestCode==10){
            if(grantResults.length>0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){

                Intent hd_intentfoto = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(hd_intentfoto,15);
            }
        }



        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {

        if(requestCode == 15 && resultCode == RESULT_OK &&
                data != null){
            hd_fotoData  = data.getData();

            try {

                hd_fotom = MediaStore.Images.Media.getBitmap(this.getContentResolver(),hd_fotoData);
                hd_foto.setImageBitmap(hd_fotom);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void hd_ogrenciEkle_click(View view) {
        int hd_rbCinsiyet = hd_rgCinsiyet.getCheckedRadioButtonId();
        RadioButton hd_radyoCinsiyet = findViewById(hd_rbCinsiyet);
        hd_secilenCinsiyet = hd_radyoCinsiyet.getText().toString();

        CheckBox hd_muzik = findViewById(R.id.hd_checkBox_muzik2);
        CheckBox hd_kitap = findViewById(R.id.hd_checkBox_kitap2);
        CheckBox hd_spor = findViewById(R.id.hd_checkBox_spor2);
        CheckBox hd_sozlesme = findViewById(R.id.hd_checkBox_sozlesme2);
        CheckBox hd_bildirim = findViewById(R.id.hd_checkBox_bildirim2);

        hd_sb = new StringBuilder();
        if (hd_muzik.isChecked())
            hd_sb.append("Müzik Dinlemek, ");
        if (hd_kitap.isChecked())
            hd_sb.append("Kitap Okumak, ");
        if (hd_spor.isChecked())
            hd_sb.append("Spor Yapmak, ");
        if (hd_sozlesme.isChecked())
            hd_sb.append("Sözleşme Kabul Edildi, ");
        if (hd_bildirim.isChecked())
            hd_sb.append("Bildirimler Açık, ");

        StorageReference hd_storageRef = hd_mstorage.getReference();

        long zaman = System.nanoTime();

        StorageReference hd_resim = hd_storageRef.child("OgrenciKayitProjesi/img"+
                String.valueOf(zaman)+".jpg");

        Bitmap hd_bitmap = ((BitmapDrawable) hd_foto.getDrawable()).getBitmap();
        ByteArrayOutputStream hd_baos = new ByteArrayOutputStream();
        hd_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, hd_baos);
        byte[] data = hd_baos.toByteArray();

        UploadTask hd_uploadTask = hd_resim.putBytes(data);

        hd_uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(HD_YeniOgrenciEkle.this, "Fotoğraf Yüklenirken Beklenmedik Bir Hata Oluştu", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                if (taskSnapshot.getMetadata().getReference() != null) {
                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri hd_uri) {
                            hd_fotoURL = hd_uri.toString();
                            Toast.makeText(HD_YeniOgrenciEkle.this,"Fotoğraf Başarılı Bir Şekilde Yüklendi.",
                                    Toast.LENGTH_SHORT).show();
                            Log.d("URL", hd_fotoURL);
                            Log.d("URL", hd_ogrNo.getText().toString());
                            Log.d("URL", hd_ogrAdi.getText().toString());
                            Log.d("URL", hd_ogrSoyadi.getText().toString());
                            Log.d("URL", hd_dogumTarihi.getText().toString());
                            Log.d("URL", hd_ogrTel.getText().toString());
                            Log.d("URL", hd_ePosta.getText().toString());
                            Log.d("URL", hd_adres.getText().toString());
                            Log.d("URL", hd_secilenCinsiyet);
                            Log.d("URL", hd_bolum.getSelectedItem().toString());
                            Log.d("URL", hd_hazirlik.getSelectedItem().toString());
                            Log.d("URL", hd_sb.toString());
                            OgrenciKaydet(hd_ogrNo.getText().toString(),
                                    hd_ogrAdi.getText().toString(),hd_ogrSoyadi.getText().toString()
                                    ,hd_dogumTarihi.getText().toString(),hd_ogrTel.getText().toString(),
                                    hd_ePosta.getText().toString(),hd_adres.getText().toString(),
                                    hd_secilenCinsiyet,hd_bolum.getSelectedItem().toString(),
                                    hd_hazirlik.getSelectedItem().toString(),
                                    hd_sb.toString(),hd_fotoURL);
                        }
                    });
                }
            }
        });
    }

    private  void OgrenciKaydet(String hd_ogrNoo, String hd_ogrAdii, String hd_ogrSoyadii, String hd_dogumTarihii, String hd_ogrTell, String hd_ePostaa, String hd_adress, String hd_cinsiyett, String hd_bolumSecimii, String hd_hazirlikk, String hd_urll, String hd_fotoURL){
        Map<String, Object> hd_ogrenci = new HashMap<>();
        hd_ogrenci.put("ogrNo", hd_ogrNoo);
        hd_ogrenci.put("ogrAdi", hd_ogrAdii);
        hd_ogrenci.put("ogrSoyadi", hd_ogrSoyadii);
        hd_ogrenci.put("dogumTarihi", hd_dogumTarihii);
        hd_ogrenci.put("ogrTel", hd_ogrTell);
        hd_ogrenci.put("ePosta", hd_ePostaa);
        hd_ogrenci.put("adres", hd_adress);
        hd_ogrenci.put("cinsiyet", hd_cinsiyett);
        hd_ogrenci.put("bolumSecimi", hd_bolumSecimii);
        hd_ogrenci.put("hazirlik", hd_hazirlikk);
        hd_ogrenci.put("url", hd_urll);

        hd_veritabani.collection("HD_OgrenciKayitProjesi")
                .add(hd_ogrenci)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Yeni Ogrenci", "Yeni Ogrenci Eklenmistir " + documentReference.getId());
                        Toast.makeText(HD_YeniOgrenciEkle.this,"Ogrenciler Kayit Edildi",
                                Toast.LENGTH_SHORT).show();

                        hd_foto.setImageResource(R.mipmap.ic_launcher);
                        hd_ogrNo.setText("");
                        hd_ogrAdi.setText("");
                        hd_ogrSoyadi.setText("");
                        hd_dogumTarihi.setText("");
                        hd_ogrTel.setText("");
                        hd_ePosta.setText("");
                        hd_adres.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("yeni veri", "Beklenmeyen Bir Hata Oluştu...", e);
                    }
                });
    }
}