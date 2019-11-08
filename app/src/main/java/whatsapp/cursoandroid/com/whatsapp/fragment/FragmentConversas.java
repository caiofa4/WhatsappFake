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
import whatsapp.cursoandroid.com.whatsapp.adapter.ConversaAdapter;
import whatsapp.cursoandroid.com.whatsapp.helper.Base64Custom;
import whatsapp.cursoandroid.com.whatsapp.helper.Preferencias;
import whatsapp.cursoandroid.com.whatsapp.model.Conversa;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentConversas extends Fragment {

    private ListView listView;
    private ArrayAdapter<Conversa> arrayAdapter;
    private ArrayList<Conversa> conversas;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListenerConversas;


    public FragmentConversas() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);

        //montar listview e adaptar
        conversas = new ArrayList<>();
        listView = (ListView) view.findViewById(R.id.lv_conversas);
        arrayAdapter = new ConversaAdapter(getActivity(), conversas);

        listView.setAdapter(arrayAdapter);


        //recuperar conversas no firebase
        Preferencias preferencias = new Preferencias(getActivity());
        String idUsuarioLogado = preferencias.getIdentificador();


        //instancia do firebase
        databaseReference  = FirebaseDatabase.getInstance().getReference()
                .child("conversas")
                .child(idUsuarioLogado);

        valueEventListenerConversas = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                conversas.clear();

                for (DataSnapshot dados : dataSnapshot.getChildren()){
                    Conversa conversa = dados.getValue(Conversa.class);
                    conversas.add(conversa);
                }
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        databaseReference.addValueEventListener(valueEventListenerConversas);

        //adicionar evento de clique na lista
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //recuperar conversas para a posiçao
                Conversa conversa = conversas.get(position);

                //criar intent para ConversaActivity
                Intent intent = new Intent(getActivity(), ConversaActivity.class);
                String email = Base64Custom.decodificarBase64(conversa.getIdUsuario().toString());
                intent.putExtra("email", email);
                intent.putExtra("nome", conversa.getNome());
                startActivity(intent);
            }
        });



        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListenerConversas);
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(valueEventListenerConversas);
    }


}
