package com.example.henrique.sharedtext;

import com.google.firebase.database.Exclude;

public class Tag {
    private String titulo;
    private String texto;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }


    public Tag(String titulo, String texto) {
        this.titulo = titulo;
        this.texto = texto;
    }

    public Tag(String titulo, String texto, int posicao) {
        this.titulo = titulo;
        this.texto = texto;
    }

    public  Tag(){}
}
