package br.com.whatsappclone.cursoandroid.whatsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import br.com.whatsappclone.cursoandroid.whatsapp.R;
import br.com.whatsappclone.cursoandroid.whatsapp.model.Conversa;

/**
 * Created by silsilva on 21/07/2017.
 */

public class ConversaAdapter extends ArrayAdapter<Conversa>{

    private Context contexto;
    private ArrayList<Conversa> conversas;

    public ConversaAdapter(Context context, ArrayList<Conversa> objects) {
        super(context, 0, objects);
        this.contexto = context;
        this.conversas = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        //Verificar se a lista está preenchida
        if (conversas != null){

            //Inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);

            //Montar a view a partir do xml
            view = inflater.inflate(R.layout.lista_contatos, parent, false);

            //Recuperar elemento para exibição
            TextView textoNome = (TextView) view.findViewById(R.id.tv_titulo);
            TextView textoUltimaMensagem = (TextView) view.findViewById(R.id.tv_subtitulo);

            Conversa conversa = conversas.get(position);
            textoNome.setText(conversa.getNome());
            textoUltimaMensagem.setText(conversa.getMensagem());

        }
        return view;
    }
}
