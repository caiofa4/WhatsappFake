package whatsapp.cursoandroid.com.whatsapp.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Caio on 21/09/2016.
 */
public class Preferencias {

    private Context contexto;
    private SharedPreferences preferences;
    private String nomeArquivo = "whatsapp.pref";
    private int MODE = 0;
    private SharedPreferences.Editor editor;

    private String CHAVE_IDENTIFICADOR = "identificadorUsuario";
    private String CHAVE_NOME = "nome";


    public Preferencias (Context contextoParametro) {

        contexto = contextoParametro;
        preferences = contexto.getSharedPreferences(nomeArquivo, MODE);
        editor = preferences.edit();
    }

    public void salvarDados (String identificador, String nome) {
        editor.putString(CHAVE_IDENTIFICADOR, identificador);
        editor.putString(CHAVE_NOME, nome);
        editor.commit();
    }

    public String getIdentificador () {
        return preferences.getString(CHAVE_IDENTIFICADOR, null);
    }

    public String getNome () {
        return preferences.getString(CHAVE_NOME, null);
    }

    /*public HashMap<String, String> getDadosUsuario (){
        HashMap<String, String> dadosUsuario = new HashMap<>();

        dadosUsuario.put(CHAVE_NOME, preferences.getString(CHAVE_NOME, null));
        dadosUsuario.put(CHAVE_TELEFONE, preferences.getString(CHAVE_TELEFONE, null));
        dadosUsuario.put(CHAVE_TOKEN, preferences.getString(CHAVE_TOKEN, null));

        return dadosUsuario;

    }*/



}
