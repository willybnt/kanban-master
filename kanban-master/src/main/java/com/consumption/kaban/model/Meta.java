package com.consumption.kaban.model;

import com.consumption.kaban.enums.TarefaStatusEnum;

import java.util.Date;

public class Meta {
    private int id;
    protected int projetoId;

    private String descricao;

    private boolean concluida;

    public boolean getComPrazo() {
        return comPrazo;
    }

    public void setComPrazo(boolean comPrazo) {
        this.comPrazo = comPrazo;
    }

    private Date prazo;
    private boolean comPrazo;

    public int getProjetoId() {
        return projetoId;
    }

    public void setProjetoId(int projetoId) {
        this.projetoId = projetoId;
    }



    // Construtor sem ID (útil para novos registros)
    public Meta(String descricao, Date prazo,boolean concluida) {
        this.descricao = descricao;
        this.prazo = prazo;
        this.concluida = false;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }

    public Date getPrazo() {
        return prazo;
    }

    public void setPrazo(Date prazo) {
        this.prazo = prazo;
    }



}
