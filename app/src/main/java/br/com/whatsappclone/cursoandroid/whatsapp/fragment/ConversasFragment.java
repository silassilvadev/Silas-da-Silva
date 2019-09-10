package br.com.whatsappclone.cursoandroid.whatsapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.whatsappclone.cursoandroid.whatsapp.R;
import br.com.whatsappclone.cursoandroid.whatsapp.activity.ConversaActivity;
import br.com.whatsappclone.cursoandroid.whatsapp.adapter.ConversaAdapter;
import br.com.whatsappclone.cursoandroid.whatsapp.config.ConfiguracaoFirebase;
import br.com.whatsappclone.cursoandroid.whatsapp.helper.Base64Custom;
import br.com.whatsappclone.cursoandroid.whatsapp.helper.Preferencias;
import br.com.whatsappclone.cursoandroid.whatsapp.model.Conversa;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversasFragment extends Fragment {

    private ListView listaConversas;
    private ConversaAdapter adapter;
    private ArrayList<Conversa> conversas;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerConversas;

    public ConversasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListenerConversas);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);

        //Montar ListView e Adapter
        listaConversas = (ListView) view.findViewById(R.id.lv_conversas);
        conversas = new ArrayList<>();
        adapter = new ConversaAdapter(getActivity(), conversas);
        listaConversas.setAdapter(adapter);

        //Recuperar dados do usuário
        Preferencias preferencias = new Preferencias(getActivity());
        String identificadorUsuarioLogado = preferencias.getIdentificador();

        //Recuperar conversas do Firebase
        firebase = ConfiguracaoFirebase.getFirebase()
                .child("conversas")
                .child(identificadorUsuarioLogado);

        valueEventListenerConversas = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                conversas.clear();

                for (DataSnapshot dados: dataSnapshot.getChildren()){

                    Conversa conversa = dados.getValue(Conversa.class);
                    conversas.add(conversa);

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        listaConversas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), ConversaActivity.class);

                Conversa conversa = conversas.get(position);

                intent.putExtra("nome", conversa.getNome());
                String email = Base64Custom.decoficarBase64(conversa.getIdUsuario());
                intent.putExtra("email", email);
                startActivity(intent);

            }
        });

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerConversas);
    }
}
