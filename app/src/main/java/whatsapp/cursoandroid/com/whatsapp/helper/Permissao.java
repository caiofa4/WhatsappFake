package whatsapp.cursoandroid.com.whatsapp.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Caio on 21/09/2016.
 */
public class Permissao {

    public static boolean validaPermissoes(int requestCode, Activity activity, String[] permissoes){

        if (Build.VERSION.SDK_INT >= 23){

            List<String> listaPermissoes = new ArrayList<String>();

            //verifica quais permissoes passadas ja foram autorizadas
            for (String permissao : permissoes){

                Boolean validaPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;

                if (!validaPermissao){
                    listaPermissoes.add(permissao);
                }

            }

            //caso alista esteja vazia, nao fazer solicitaçoes
            if (listaPermissoes.isEmpty() == true) return true;

            String[] novasPermissoes = new String[listaPermissoes.size()];
            listaPermissoes.toArray(novasPermissoes);

            //Solicita permissao
            ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode);

        }

        return true;

    }

}
