package br.com.whatsappclone.cursoandroid.whatsapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.whatsappclone.cursoandroid.whatsapp.R;
import br.com.whatsappclone.cursoandroid.whatsapp.config.ConfiguracaoFirebase;
import br.com.whatsappclone.cursoandroid.whatsapp.helper.Base64Custom;
import br.com.whatsappclone.cursoandroid.whatsapp.helper.Preferencias;
import br.com.whatsappclone.cursoandroid.whatsapp.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText senha;
    private Button botaoLogar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerUsuario;
    private String identificadorUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificarUsuarioLogado();

        email = (EditText) findViewById(R.id.edit_login_email);
        senha = (EditText) findViewById(R.id.edit_login_senha);
        botaoLogar = (Button) findViewById(R.id.bt_logar);

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email == null || senha == null || email.getText().toString().isEmpty() || senha.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "Digite todos os dados para continuar!", Toast.LENGTH_SHORT).show();
                    return;
                }

                usuario = new Usuario();
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());
                validarLogin();

            }
        });

    }

    private void verificarUsuarioLogado() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if (autenticacao.getCurrentUser() != null){
            abrirTelaPrincipal();
        }
    }

    private void validarLogin() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){

                            identificadorUsuarioLogado = Base64Custom.codificarBase64(usuario.getEmail());

                            firebase = ConfiguracaoFirebase.getFirebase()
                                    .child("usuarios")
                                    .child(identificadorUsuarioLogado);

                            valueEventListenerUsuario = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    Usuario usuarioRecuperado =  dataSnapshot.getValue(Usuario.class);

                                    Preferencias preferencias = new Preferencias(LoginActivity.this);
                                    preferencias.salvarDados(identificadorUsuarioLogado, usuarioRecuperado.getNome());

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            };

                            firebase.addValueEventListener(valueEventListenerUsuario);

                            abrirTelaPrincipal();
                            Toast.makeText(LoginActivity.this, "Sucesso ao logar usuário!!!", Toast.LENGTH_SHORT).show();

                        } else {
                            String erroLogin = "";
                            try {
                                throw task.getException();

                            } catch (FirebaseAuthInvalidUserException usuarioInvalido) {
                                erroLogin = "Email não existe ou foi desativado!";
                            } catch (FirebaseAuthInvalidCredentialsException passwordInvalida){
                                erroLogin = "Senha inválida! Tente novamente!";
                            } catch (Exception e) {
                                erroLogin = "Erro ao efetuar login!";
                                e.printStackTrace();
                            }
                            Toast.makeText(LoginActivity.this, erroLogin, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void abrirCadastroUsuario (View view){
        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);
    }
}
