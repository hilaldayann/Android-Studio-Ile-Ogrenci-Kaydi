package com.gop.ogrencikayitprojesi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HD_KayitOl extends AppCompatActivity {
    EditText hd_ad, hd_soyad, hd_email, hd_sifre, hd_telefon;
    private FirebaseAuth hd_mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_d__kayit_ol);
        hd_ad = findViewById(R.id.hd_etxt_ad);
        hd_soyad = findViewById(R.id.hd_etxt_soyad);
        hd_email = findViewById(R.id.hd_etxt_email);
        hd_sifre = findViewById(R.id.hd_etxt_sifre);
        hd_telefon = findViewById(R.id.hd_etxt_telefon);

        hd_mAuth = FirebaseAuth.getInstance();
    }

    public void hd_btn_kayit_ol(View view) {
        String hd_uad = hd_ad.getText().toString();
        String hd_usoyad = hd_soyad.getText().toString();
        String hd_uemail = hd_email.getText().toString();
        String hd_usifre = hd_sifre.getText().toString();
        String hd_utelefon = hd_telefon.getText().toString();
        if (!hd_uemail.isEmpty() && !hd_usifre.isEmpty()) {
            hd_mAuth.createUserWithEmailAndPassword(hd_uemail, hd_usifre)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("HD_KAYITOL", "createUserWithEmail:success");
                                FirebaseUser hd_user = hd_mAuth.getCurrentUser();
                                Toast.makeText(HD_KayitOl.this, "Kaydınız başarı ile oluşturuldu," +
                                                " Giriş sayfasına yönlendiriliyorsunuz...",
                                        Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(HD_KayitOl.this, HD_GirisSayfasi.class));

                            } else {
                                Log.w("HD_KAYITOL", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(HD_KayitOl.this, "Kaydınız oluşturulamadı," +
                                                " Giriş sayfasına yönlendiriliyorsunuz...",
                                        Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(HD_KayitOl.this, HD_GirisSayfasi.class));
                            }
                        }
                    });
        }
    }
}