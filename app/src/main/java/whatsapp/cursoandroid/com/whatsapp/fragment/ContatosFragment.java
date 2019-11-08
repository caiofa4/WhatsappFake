package whatsapp.cursoandroid.com.whatsapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import whatsapp.cursoandroid.com.whatsapp.R;
import whatsapp.cursoandroid.com.whatsapp.activity.ConversaActivity;
import whatsapp.cursoandroid.com.whatsapp.adapter.ContatoAdapter;
import whatsapp.cursoandroid.com.whatsapp.helper.Preferencias;
import whatsapp.cursoandroid.com.whatsapp.model.Contato;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Contato> contatos;
    private DatabaseReference databaseReferencia;
    private ValueEventListener valueEventListenercontato;


    public ContatosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        contatos = new ArrayList<>();


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

        //Montar listview e adapter
        listView = (ListView) view.findViewById(R.id.lv_contatos);
        /*adapter = new ArrayAdapter(
                            getActivity(),
                            R.layout.lista_contatos,
                            contatos
                            );*/
        adapter = new ContatoAdapter(getActivity(), contatos);
        listView.setAdapter(adapter);


        //recuperar contatos no firebase
        Preferencias preferencias = new Preferencias(getActivity());
        String identificadorUsuarioLogado = preferencias.getIdentificador();
        databaseReferencia = FirebaseDatabase.getInstance().getReference()
                            .child("contatos")
                            .child(identificadorUsuarioLogado);

        //Listener para recuperar contatos
        valueEventListenercontato = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //limpar a lista
                contatos.clear();

                //Listar contatos para usuario
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Contato contato = dados.getValue(Contato.class);
                    contatos.add(contato);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        //adicionar evento clique na lista
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ConversaActivity.class);

                //Recupera dados a ser passados
                Contato contato = contatos.get(i);


                //Envia dados
                intent.putExtra("nome", contato.getNome());
                intent.putExtra("email", contato.getEmail());
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReferencia.addValueEventListener(valueEventListenercontato);
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReferencia.removeEventListener(valueEventListenercontato);
    }

}
