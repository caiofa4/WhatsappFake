package whatsapp.cursoandroid.com.whatsapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import whatsapp.cursoandroid.com.whatsapp.R;
import whatsapp.cursoandroid.com.whatsapp.adapter.TabAdapter;
import whatsapp.cursoandroid.com.whatsapp.helper.Base64Custom;
import whatsapp.cursoandroid.com.whatsapp.helper.Preferencias;
import whatsapp.cursoandroid.com.whatsapp.helper.SlidingTabLayout;
import whatsapp.cursoandroid.com.whatsapp.model.Contato;
import whatsapp.cursoandroid.com.whatsapp.model.Usuario;

public class MainActivity extends AppCompatActivity {

    private Button botaoSair;

    private FirebaseAuth firebaseAuth;

    private Toolbar toolbar;

    private SlidingTabLayout slidingtablayout;
    private ViewPager viewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("Whatsapp");
        setSupportActionBar(toolbar);

        slidingtablayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);
        viewPager = (ViewPager) findViewById(R.id.vp_pagina);

        slidingtablayout.setDistributeEvenly(true);
        slidingtablayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.textColorAccent));

        //configurar Adapter
        TabAdapter tabadapter = new TabAdapter( getSupportFragmentManager() );
        viewPager.setAdapter(tabadapter);

        slidingtablayout.setViewPager(viewPager);






/*        botaoSair = (Button) findViewById(R.id.bt_sair);
        botaoSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });*/



        //mensagensReferencia.setValue("Teste dessa porra");




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.item_sair:
                deslogarUsuario();
                return true;
            case R.id.item_configuracoes:
                return true;
            case R.id.item_adicionar:
                abrirCadastroContato();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void deslogarUsuario(){
        firebaseAuth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }

    public void abrirCadastroContato(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //Configurações
        alertDialog.setTitle("Novo Contato");
        alertDialog.setMessage("E-mail do usuário");
        alertDialog.setCancelable(false);

        //Criar o campo de texto
        final EditText editText = new EditText(this);
        alertDialog.setView(editText);

        //Definir o botão positivo
        alertDialog.setPositiveButton("cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String emailContato = editText.getText().toString();

                //Valida se o email foi ditigado
                if (emailContato.isEmpty()){
                    Toast.makeText(MainActivity.this, "Digite um email", Toast.LENGTH_SHORT).show();
                } else {
                    final String identificadorContato = Base64Custom.converterBase64(emailContato);

                    //recuperar a instancia do Firebase
                    DatabaseReference databaseReferencia = FirebaseDatabase.getInstance().getReference();
                    databaseReferencia = databaseReferencia.child("usuarios").child(identificadorContato);

                    //fazer uma unica consulta no firebase
                    databaseReferencia.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            //Verifica se foi retornado algum dado
                            if (dataSnapshot.getValue() != null){

                                //Recuperar dados do contato a ser adicionado
                                Usuario usuarioContato = new Usuario();
                                usuarioContato = dataSnapshot.getValue(Usuario.class);

                                //Recuperar os dados do usuario logado
                                Preferencias preferencias = new Preferencias(MainActivity.this);
                                String identificadorUsuarioLogado = preferencias.getIdentificador();
                                Contato contato = new Contato();

                                contato.setIdentificadorUsuario(identificadorContato);
                                contato.setEmail(usuarioContato.getEmail());
                                contato.setNome(usuarioContato.getNome());

                                //Salvar dados Firebase
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                                                                                        .child("contatos")
                                                                                        .child(identificadorUsuarioLogado)
                                                                                        .child(identificadorContato);
                                databaseReference.setValue(contato);









                            }else{
                                Toast.makeText(MainActivity.this, "Usuario não possui cadastro", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                }

            }
        });

        //Definir o botao negativo
        alertDialog.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alertDialog.create();
        alertDialog.show();

    }
}
