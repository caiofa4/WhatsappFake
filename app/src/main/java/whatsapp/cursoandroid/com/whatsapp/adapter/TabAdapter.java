package whatsapp.cursoandroid.com.whatsapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import whatsapp.cursoandroid.com.whatsapp.fragment.ContatosFragment;
import whatsapp.cursoandroid.com.whatsapp.fragment.FragmentConversas;

/**
 * Created by Caio on 23/09/2016.
 */
public class TabAdapter extends FragmentStatePagerAdapter {

    private String[] tituloAbas = {"CONVERSAS", "CONTATOS"};            //tem q ser maiusculas


    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new FragmentConversas();
                break;
            case 1:
                fragment = new ContatosFragment();
                break;

        }


        return fragment;
    }

    @Override
    public int getCount() {
        return tituloAbas.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return tituloAbas[position];
    }
}
