package whatsapp.cursoandroid.com.whatsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import whatsapp.cursoandroid.com.whatsapp.R;
import whatsapp.cursoandroid.com.whatsapp.adapter.MensagemAdapter;
import whatsapp.cursoandroid.com.whatsapp.helper.Base64Custom;
import whatsapp.cursoandroid.com.whatsapp.helper.Preferencias;
import whatsapp.cursoandroid.com.whatsapp.model.Conversa;
import whatsapp.cursoandroid.com.whatsapp.model.Mensagem;

public class ConversaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText editMensagem;
    private ImageButton btMensagem;
    private ListView listView;
    private ArrayAdapter<Mensagem> arrayAdapter;
    private ArrayList<Mensagem> mensagens;
    private ValueEventListener valueEventListenerMensagem;
    private DatabaseReference databaseReferencia;
    private Conversa conversa;


    //Destinatario
    private String nomeUsuarioDestinatario;
    private String idUsuarioDestinatario;

    //Remetente
    private String idUsuarioLogado;
    private String nomeUsuarioLogado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        toolbar = (Toolbar) findViewById(R.id.tb_conversa);
        editMensagem = (EditText) findViewById(R.id.edit_mensagem);
        btMensagem = (ImageButton) findViewById(R.id.bt_enviar);
        listView = (ListView) findViewById(R.id.lv_mensagens);

        //recuperar os dados do usuario logado
        Preferencias preferencias = new Preferencias(ConversaActivity.this);
        idUsuarioLogado = preferencias.getIdentificador();
        nomeUsuarioLogado = preferencias.getNome();



        //Recuperar os dados enviados na intent
        Bundle extra = getIntent().getExtras();
        if (extra != null){

            //Recuperar dados do contato (destinatario)
            nomeUsuarioDestinatario = extra.getString("nome");
            idUsuarioDestinatario = extra.getString("email");

            idUsuarioDestinatario = Base64Custom.converterBase64(idUsuarioDestinatario);

        }


        //Configuraçao da toolbar
        toolbar.setTitle(nomeUsuarioDestinatario);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        //Montagem listview e adapter
        mensagens = new ArrayList<>();

        /*arrayAdapter = new ArrayAdapter<String>(
                ConversaActivity.this,
                android.R.layout.simple_list_item_1,
                mensagens
        );*/
        arrayAdapter = new MensagemAdapter(ConversaActivity.this, mensagens);
        listView.setAdapter(arrayAdapter);

        databaseReferencia = FirebaseDatabase.getInstance().getReference()
                    .child("mensagens")
                    .child(idUsuarioLogado)
                    .child(idUsuarioDestinatario);

        //criar listener para as mensagens
        valueEventListenerMensagem = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //limpar as mensagens
                mensagens.clear();

                //Recuperar mensagens
                for (DataSnapshot dados : dataSnapshot.getChildren()){

                    //recuperar mensagem individual
                    Mensagem mensagem = dados.getValue(Mensagem.class);

                    //adicionar na lista de mensagens
                    mensagens.add(mensagem);
                }
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReferencia.addValueEventListener(valueEventListenerMensagem);




        //Enviar mensagem
        btMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textoMensagem = editMensagem.getText().toString();

                //Testar se a mesnagem esta preenchida
                if (textoMensagem.isEmpty()){
                    Toast.makeText(ConversaActivity.this, "Digite uma mensagem", Toast.LENGTH_SHORT).show();
                } else{

                    //Salvar mensagem no firebase
                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario(idUsuarioLogado);
                    mensagem.setMensagem(textoMensagem);

                    //salvar mensagem remetente
                    salvarMensagemFirebase(idUsuarioLogado, idUsuarioDestinatario, mensagem);

                    //salvar mensagem destinatario
                    salvarMensagemFirebase(idUsuarioDestinatario, idUsuarioLogado, mensagem);



                    //salvar conversas no firebase para o remetente
                    conversa = new Conversa();
                    conversa.setIdUsuario(idUsuarioDestinatario);
                    conversa.setNome(nomeUsuarioDestinatario);
                    conversa.setMensagem(textoMensagem);
                    salvarConversaFirebase(idUsuarioLogado, idUsuarioDestinatario, conversa);


                    //salvar conversas no firebase para o destinatario
                    conversa = new Conversa();
                    conversa.setIdUsuario(idUsuarioLogado);
                    conversa.setNome( nomeUsuarioLogado);
                    conversa.setMensagem(textoMensagem);
                    salvarConversaFirebase(idUsuarioDestinatario, idUsuarioLogado, conversa);



                    //apagar texto digitado
                    editMensagem.setText("");


                }
            }
        });
    }


    private boolean salvarMensagemFirebase(String idRemetente, String idDestinatario, Mensagem mensagem){

        try{

            DatabaseReference databaseReferencia = FirebaseDatabase.getInstance().getReference().child("mensagens");
            databaseReferencia.child(idRemetente)
                                .child(idDestinatario)
                                .push()
                                .setValue(mensagem);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }


    private boolean salvarConversaFirebase(String idRemetente, String idDestinatario, Conversa conversa){

        try{

            DatabaseReference databaseReferencia = FirebaseDatabase.getInstance().getReference().child("conversas");
            databaseReferencia.child(idRemetente)
                    .child(idDestinatario)
                    .setValue(conversa);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        databaseReferencia.addValueEventListener(valueEventListenerMensagem);
    }

    @Override
    protected void onStop() {
        super.onStop();
        databaseReferencia.removeEventListener(valueEventListenerMensagem);
    }
}
