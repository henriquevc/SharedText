package com.example.henrique.sharedtext;

public class Tag {
    private String titulo;
    private String texto;
    private String chave;

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

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public Tag(String titulo, String texto) {
        this.titulo = titulo;
        this.texto = texto;
    }
    public  Tag(){}
}
