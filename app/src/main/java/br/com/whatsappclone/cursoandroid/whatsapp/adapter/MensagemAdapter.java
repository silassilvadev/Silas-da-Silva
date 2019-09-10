package br.com.whatsappclone.cursoandroid.whatsapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.whatsappclone.cursoandroid.whatsapp.R;
import br.com.whatsappclone.cursoandroid.whatsapp.helper.Preferencias;
import br.com.whatsappclone.cursoandroid.whatsapp.model.Contato;
import br.com.whatsappclone.cursoandroid.whatsapp.model.Mensagem;

/**
 * Created by silsilva on 21/07/2017.
 */

public class MensagemAdapter extends ArrayAdapter<Mensagem> {

    private Context contexto;
    private ArrayList<Mensagem> mensagens;

    public MensagemAdapter(Context context, ArrayList<Mensagem> objects) {
        super(context, 0, objects);
        this.contexto = context;
        this.mensagens = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        //Verifica se a lista está preenchida
        if (mensagens != null){

            //Recupera dados do usuário remetente
            Preferencias preferencias = new Preferencias(contexto);
            String idUsuarioRemetente = preferencias.getIdentificador();

            //Inicializa objeto para montagem do layout
            LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);

            //Recuperando mensagem
            Mensagem mensagem = mensagens.get(position);

            //Montar a view a partir do xml
            if (idUsuarioRemetente.equals(mensagem.getIdUsuario())) {
                view = inflater.inflate(R.layout.item_mensagem_direita, parent, false);
            } else {
                view = inflater.inflate(R.layout.item_mensagem_esquerda, parent, false);
            }

            //Recuperando elemento para exibição
            TextView textoMensagem = (TextView) view.findViewById(R.id.tv_mensagem);
            textoMensagem.setText(mensagem.getMensagem());

            //TextView textoMensagemRecebida = (TextView) view.findViewById(R.id.tv_mensagem_esquerda);
            //textoMensagem.setText(mensagem.getMensagem());

        }
        return view;
    }
}
