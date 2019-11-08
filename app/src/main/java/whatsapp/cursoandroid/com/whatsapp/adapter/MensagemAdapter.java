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
import whatsapp.cursoandroid.com.whatsapp.helper.Preferencias;
import whatsapp.cursoandroid.com.whatsapp.model.Contato;
import whatsapp.cursoandroid.com.whatsapp.model.Mensagem;

/**
 * Created by Caio on 27/09/2016.
 */
public class MensagemAdapter extends ArrayAdapter<Mensagem> {

    private Context context;
    private ArrayList<Mensagem> mensagens;

    public MensagemAdapter(Context c, ArrayList<Mensagem> objects) {
        super(c, 0, objects);
        this.context = c;
        this.mensagens = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        //verifica se a lista esta preenchida
        if (mensagens !=null){

            //recuperar mensagens
            Mensagem mensagem = mensagens.get(position);

            //recuperar usuario logado
            Preferencias preferencias = new Preferencias(context);
            String idUsuarioLogado = preferencias.getIdentificador();

            //Inicializa objeto para montagem do layout
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //montar a view a partir do xml
            if (idUsuarioLogado.equals(mensagem.getIdUsuario())){
                view = layoutInflater.inflate(R.layout.item_mensagem_direita, parent, false);
            } else{
                view = layoutInflater.inflate(R.layout.item_mensagem_esquerda, parent, false);
            }


            //Recuperar elementos para exibiçao
            TextView textView = (TextView) view.findViewById(R.id.tv_mensagem);
            textView.setText(mensagem.getMensagem());

        }

        return view;
    }
}
