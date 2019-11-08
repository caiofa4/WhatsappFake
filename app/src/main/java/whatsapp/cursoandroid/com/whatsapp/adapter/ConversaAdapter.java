package whatsapp.cursoandroid.com.whatsapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import whatsapp.cursoandroid.com.whatsapp.R;
import whatsapp.cursoandroid.com.whatsapp.model.Conversa;

/**
 * Created by Caio on 28/09/2016.
 */

public class ConversaAdapter extends ArrayAdapter<Conversa> {

    private ArrayList<Conversa> conversas;
    private Context context;
    private Conversa conversa;


    public ConversaAdapter(Context c, ArrayList<Conversa> objects) {
        super(c, 0, objects);
        this.context = c;
        this.conversas = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        //verifica se a lista esta preenchida
        if (conversas!=null){

            //inicializa objetos para montagem do layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //montar a view a partir do xml
            view = inflater.inflate(R.layout.lista_conversas, parent, false);

            //recuperar elementos da tela
            TextView nome = (TextView) view.findViewById(R.id.text_nome);
            TextView ultimaMensagem = (TextView) view.findViewById(R.id.text_ultima_conversa);

            //setar valores nos componentes da tela
            conversa = conversas.get(position);
            nome.setText(conversa.getNome());
            ultimaMensagem.setText(conversa.getMensagem());


        }else {

        }


        return view;

    }
}
