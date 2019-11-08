package whatsapp.cursoandroid.com.whatsapp.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Caio on 22/09/2016.
 */

// @JsonIgnoreProperties({"id", "senha"})
@IgnoreExtraProperties
public class Usuario {


    private String nome;
    private String email;

    private String senha;
    private String id;

    public Usuario(){


    }
    //dataSnapshot.getValue(Usuario.class)

    public void Salvar(){

        DatabaseReference databaseReferencia = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usuarioReferencia = databaseReferencia.child("usuarios");

        usuarioReferencia.child(getId()).setValue(this);

    }
    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}

