package br.com.whatsappclone.cursoandroid.whatsapp.model;

/**
 * Created by silsilva on 20/07/2017.
 */

public class Mensagem {

    private String idUsuario;
    private String mensagem;

    public Mensagem() {

    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
