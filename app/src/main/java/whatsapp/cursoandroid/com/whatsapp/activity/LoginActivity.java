package whatsapp.cursoandroid.com.whatsapp.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Random;

import whatsapp.cursoandroid.com.whatsapp.R;
import whatsapp.cursoandroid.com.whatsapp.helper.Base64Custom;
import whatsapp.cursoandroid.com.whatsapp.helper.Permissao;
import whatsapp.cursoandroid.com.whatsapp.helper.Preferencias;
import whatsapp.cursoandroid.com.whatsapp.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText email, senha;
    private Button botaoLogar;
    private Usuario usuario;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.edit_login_email);
        senha = (EditText) findViewById(R.id.edit_login_senha);
        botaoLogar = (Button) findViewById(R.id.bt_logar);

        firebaseAuth = FirebaseAuth.getInstance();

        verificarUsuarioLogado();



        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                usuario = new Usuario();
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());
                validarLogin();

            }
        });


    }

    public void abrirCadastroUsuario (View view){

        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);

    }

    public void validarLogin (){

        firebaseAuth.signInWithEmailAndPassword(usuario.getEmail().toString(),
                                                usuario.getSenha().toString())
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //Recuperar dados do usuario
                            final String identificadorUsuarioLogado = Base64Custom.converterBase64(usuario.getEmail().toString());
                            databaseReference = FirebaseDatabase.getInstance().getReference()
                                    .child("usuarios")
                                    .child(identificadorUsuarioLogado);
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    Usuario usuario = dataSnapshot.getValue(Usuario.class);

                                    //Salvar email nas preferencias
                                    //String identificadorUsuarioLogado = Base64Custom.converterBase64(usuario.getEmail().toString());
                                    Preferencias preferencias = new Preferencias(LoginActivity.this);
                                    preferencias.salvarDados(identificadorUsuarioLogado, usuario.getNome());

                                    abrirTelaPrincipal();

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });




                        } else {
                            Log.i("LoggedUser", "Deu merda ao logar");
                            Toast.makeText(LoginActivity.this, "Deu merda ao logar", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    public void abrirTelaPrincipal(){

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    public void verificarUsuarioLogado(){

        if (firebaseAuth.getCurrentUser() != null) {
            abrirTelaPrincipal();
        }

    }



}
