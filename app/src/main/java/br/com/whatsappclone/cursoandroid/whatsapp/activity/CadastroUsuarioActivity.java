package br.com.whatsappclone.cursoandroid.whatsapp.activity;

import android.content.Intent;
import android.provider.ContactsContract;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import br.com.whatsappclone.cursoandroid.whatsapp.R;
import br.com.whatsappclone.cursoandroid.whatsapp.config.ConfiguracaoFirebase;
import br.com.whatsappclone.cursoandroid.whatsapp.helper.Base64Custom;
import br.com.whatsappclone.cursoandroid.whatsapp.helper.Preferencias;
import br.com.whatsappclone.cursoandroid.whatsapp.model.Usuario;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText senha;
    private Button botaoCadastrar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        nome = (EditText) findViewById(R.id.edit_cadastro_nome);
        email = (EditText) findViewById(R.id.edit_cadastro_email);
        senha = (EditText) findViewById(R.id.edit_cadastro_senha);
        botaoCadastrar = (Button) findViewById(R.id.bt_cadastrar);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email == null || senha == null || email.getText().toString().isEmpty() || senha.getText().toString().isEmpty()){
                    Toast.makeText(CadastroUsuarioActivity.this, "Digite todos os dados para efetuar o cadastro!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (senha.getText().toString().length() <= 4){
                    Toast.makeText(CadastroUsuarioActivity.this, "Sua senha deve ter no mínimo 4 caracteres!", Toast.LENGTH_SHORT).show();
                    return;
                }

                usuario = new Usuario();
                usuario.setNome(nome.getText().toString());
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());
                cadastrarUsuario();

            }
        });
    }
    private void cadastrarUsuario() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(CadastroUsuarioActivity.this, "Suecesso ao cadastrar usuário!", Toast.LENGTH_SHORT).show();

                            String identificadorUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                            usuario.setId(identificadorUsuario);
                            usuario.salvar();

                            Preferencias preferencias = new Preferencias(CadastroUsuarioActivity.this);
                            preferencias.salvarDados(identificadorUsuario, usuario.getNome());
                            abrirLoginUsuario();

                        } else {
                            String erroExcecao = "";
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException erroSenha){
                                erroExcecao = "Digite uma senha mais forte, contendo mais caracteres e com letras e números!";

                            } catch (FirebaseAuthInvalidCredentialsException erroEmail) {
                                erroExcecao = "O e-mail digitado é inválido, digite um novo e-mail!";

                            } catch (FirebaseAuthUserCollisionException erroUsuarioJaExiste){
                                erroExcecao = "Esse e-mail já está em uso no App!";

                            } catch (Exception e) {
                                erroExcecao = "Erro ao efetuar o cadastro!";
                                e.printStackTrace();
                            }
                            Toast.makeText(CadastroUsuarioActivity.this, erroExcecao, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void abrirLoginUsuario() {
        Intent intent = new Intent(CadastroUsuarioActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
