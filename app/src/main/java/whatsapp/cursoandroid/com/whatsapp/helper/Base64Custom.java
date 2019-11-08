package whatsapp.cursoandroid.com.whatsapp.helper;

import android.util.Base64;

/**
 * Created by Caio on 23/09/2016.
 */
public class Base64Custom {


    public static String converterBase64(String texto){
        String textoConvertido = Base64.encodeToString(texto.getBytes(), Base64.DEFAULT);
        return textoConvertido.trim();
    }

    public static String decodificarBase64(String textoConvertido){
        byte[] byteDecodificado = Base64.decode(textoConvertido, Base64.DEFAULT);
        return new String(byteDecodificado);
    }



}
