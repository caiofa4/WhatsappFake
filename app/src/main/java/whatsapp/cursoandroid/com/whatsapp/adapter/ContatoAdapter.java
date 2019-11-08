package whatsapp.cursoandroid.com.whatsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import whatsapp.cursoandroid.com.whatsapp.R;
import whatsapp.cursoandroid.com.whatsapp.model.Contato;

/**
 * Created by Caio on 26/09/2016.
 */
public class ContatoAdapter extends ArrayAdapter<Contato> {

    private Context context;
    private ArrayList<Contato> contatos;

    public ContatoAdapter(Context c, ArrayList<Contato> objects) {
        super(c, 0, objects);
        this.context = c;
        this.contatos = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view=null;

        //verifica se a lista esta preenchida
        if (contatos != null){

            //inicializa objetos para montagem do layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //monta a view a partir do xml
            view = inflater.inflate(R.layout.lista_contatos, parent, false);

            //recupera elementos para visualizaçao
            TextView textView = (TextView) view.findViewById(R.id.tv_nome);

            Contato contato = contatos.get(position);
            textView.setText(contato.getNome());
        }



        return view;
    }
}
