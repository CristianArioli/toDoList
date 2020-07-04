package com.example.todolist;

public class Tarefa {
    private int id;
    private String nome;
    private String descricao;
    private int importancia;
    private String data;
    private String hora;

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getImportancia() {
        return importancia;
    }

    public String getData() {
        return data;
    }

    public String getHora() {
        return hora;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setImportancia(int importancia) {
        this.importancia = importancia;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Tarefa(int id, String nome, String descricao, int importancia, String data, String hora){
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.importancia = importancia;
        this.data = data;
        this.hora = hora;
    }

    public Tarefa() {
    }
}
