package vn.poly.chaton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity {

    MaterialEditText edemail,edpassword;
    AppCompatButton btn_dangnhap,btn_dangki;
    FirebaseAuth auth;
    CheckBox cbRemember;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        anhxa();

        cbRemember = (CheckBox) findViewById(R.id.checkbox);

        //check box
        sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
        //lay gia tri
        edemail.setText(sharedPreferences.getString("taikhoan",""));
        edpassword.setText(sharedPreferences.getString("matkhau",""));
        cbRemember.setChecked(sharedPreferences.getBoolean("checked",false));

        btn_dangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        btn_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = edemail.getText().toString();
                String txt_password = edpassword.getText().toString();

                if (TextUtils.isEmpty(txt_email)||TextUtils.isEmpty(txt_password)){
                    Toast.makeText(LoginActivity.this,"khong duoc de trong :D",Toast.LENGTH_SHORT).show();
                }else {
                    auth.signInWithEmailAndPassword(txt_email,txt_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(LoginActivity.this,MainActivity3.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        //check
                                        if (cbRemember.isChecked()){
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("taikhoan", txt_email);
                                            editor.putString("matkhau", txt_password);
                                            editor.putBoolean("checked", true);
                                            editor.commit();
                                        }else {
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.remove("taikhoan");
                                            editor.remove("matkhau");
                                            editor.remove("checked");
                                            editor.commit();
                                        }
                                    }else {
                                        Toast.makeText(LoginActivity.this,"error :D",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
    private void anhxa() {
        edemail = findViewById(R.id.edemail);
        edpassword = findViewById(R.id.edpassword);
        btn_dangnhap = findViewById(R.id.btn_dangnhap);
        btn_dangki = findViewById(R.id.btn_dangki);
        auth = FirebaseAuth.getInstance();
    }
}