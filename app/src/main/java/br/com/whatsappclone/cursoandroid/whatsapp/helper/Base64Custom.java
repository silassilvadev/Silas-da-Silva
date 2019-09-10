package br.com.whatsappclone.cursoandroid.whatsapp.helper;

import android.text.method.HideReturnsTransformationMethod;
import android.util.Base64;

/**
 * Created by silsilva on 20/07/2017.
 */

public class Base64Custom {

    public static String codificarBase64(String texto){
        //Utilização de um classe do Android para codificar o e-mail para poder salvar no Firebase
        //Utilizado também para retornar uma String e específicar caracteres que eu queira substituir
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "");
    }

    public static String decoficarBase64(String textoCodificado){
        return new String(Base64.decode(textoCodificado, Base64.DEFAULT));
    }
}
