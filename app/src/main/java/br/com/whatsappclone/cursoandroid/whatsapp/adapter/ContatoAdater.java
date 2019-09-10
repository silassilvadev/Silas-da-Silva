package br.com.whatsappclone.cursoandroid.whatsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.whatsappclone.cursoandroid.whatsapp.R;
import br.com.whatsappclone.cursoandroid.whatsapp.model.Contato;

/**
 * Created by silsilva on 20/07/2017.
 */

public class ContatoAdater extends ArrayAdapter<Contato> {

    private final Context contexto;
    private ArrayList<Contato> contatos;

    public ContatoAdater(Context context, ArrayList<Contato> objets) {
        super(context, 0, objets);
        this.contexto = context;
        this.contatos = objets;
    }

    //Monta cada item da lista
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        //Verificar se a lista está vazia
        if (contatos != null){

            //inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);

            //Montar view a partir do xml
            view = inflater.inflate(R.layout.lista_contatos, parent, false);

            //recupera elemento para exibição
            TextView nomeContato = (TextView) view.findViewById(R.id.tv_titulo);
            TextView emailContato = (TextView) view.findViewById(R.id.tv_subtitulo);

            Contato contato = contatos.get(position);
            nomeContato.setText(contato.getNome());
            emailContato.setText(contato.getEmail());

        }
        return view;
    }
}
