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

public class HD_GirisSayfasi extends AppCompatActivity {
    EditText hd_email, hd_sifre;
    private FirebaseAuth hd_mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_d__giris_sayfasi);
        hd_email = findViewById(R.id.hd_etxt_girisEmail);
        hd_sifre = findViewById(R.id.hd_etxt_girisSifre);

        hd_mAuth = FirebaseAuth.getInstance();

        FirebaseUser hd_kullanici = hd_mAuth.getCurrentUser();

        if(hd_kullanici != null){
            startActivity(new Intent(HD_GirisSayfasi.this, HD_Ogrenciler.class));
            finish();
        }

    }

    public void hd_girisYap(View view) {
        hd_mAuth.signInWithEmailAndPassword(hd_email.getText().toString(), hd_sifre.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d("HD_GİRİS", "signInWithEmail:success");
                            FirebaseUser hd_user = hd_mAuth.getCurrentUser();
                            Toast.makeText(HD_GirisSayfasi.this, "Girişiniz Başarı İle Gerçekleştirilmiştir." +
                                            "Öğrenciler Sayfasına Yönlendiriliyorsunuz...",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(HD_GirisSayfasi.this, HD_Ogrenciler.class));

                        } else {

                            Log.w("HD_GİRİS", "signInWithEmail:failure", task.getException());
                            Toast.makeText(HD_GirisSayfasi.this, "Girmiş Olduğunuz e mail ya da şifre hatalıdır," +
                                            "Bilgileribizi tekrar kontrol ediniz...",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    public void hd_kayitOl(View view) {
        startActivity(new Intent(HD_GirisSayfasi.this,HD_KayitOl.class));
    }
}