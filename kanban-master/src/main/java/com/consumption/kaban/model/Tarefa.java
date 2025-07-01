package com.consumption.kaban.model;

import com.consumption.kaban.enums.TarefaStatusEnum;

import java.util.Date;

public  class Tarefa {

    protected int id;
    protected String titulo;
    protected String descricao;
    protected int projetoId;
    protected Date prazo;
    protected Boolean comPrazo;
    protected TarefaStatusEnum status;

    public Tarefa(String titulo, String descricao, TarefaStatusEnum status) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = status;

    }
    public Tarefa(String titulo, String descricao, TarefaStatusEnum status, Date prazo) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = status;
        this.prazo = prazo;
    }


    public TarefaStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TarefaStatusEnum status) {
        this.status = status;
    }


    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }

    public String getDescricao() { return descricao; }


    public Boolean getComPrazo() {
        return comPrazo;
    }

    public void setComPrazo(Boolean comPrazo) {
        this.comPrazo = comPrazo;
    }
    public int getProjetoId() {
        return projetoId;
    }

    public void setProjetoId(int projetoId) {
        this.projetoId = projetoId;
    }



    public java.sql.Date getPrazo() {
        return (java.sql.Date) prazo;
    }

    public void setPrazo(Date prazo) {
        this.prazo = prazo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

}