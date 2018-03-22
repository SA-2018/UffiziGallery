package it.univaq.uffizigallery.model;

/**
 * Created by Riccardo on 21/03/2018.
 */

public class Checkpoint {

    private long id;
    private String nome;
    private String in_out;
    private boolean attivo;
    private String tipo;
    private long childsize;
    private String location;


    public Checkpoint(){
        this.id = 0;
        this.nome = "";
        this.in_out = "";
        this.attivo = false;
        this.tipo = "";
        this.childsize = 0;
        this.location = "";
    }

    public Checkpoint(long id, String nome, String in_out, boolean attivo, String tipo, long childsize, String location){
        this.id = id;
        this.nome = nome;
        this.in_out = in_out;
        this.attivo = attivo;
        this.tipo = tipo;
        this.childsize = childsize;
        this.location = location;
    }

    //GET METHODS

    public long getId(){
        return this.id;
    }

    public String getNome(){
        return this.nome;
    }

    public String getIn_out(){
        return this.in_out;
    }

    public boolean getAttivo(){
        return this.attivo;
    }

    public String getTipo(){
        return this.tipo;
    }

    public long getChildsize(){
        return this.childsize;
    }

    public String getLocation(){
        return this.location;
    }

    //SET METHODS

    public void setId(long id){
        this.id = id;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setIn_out(String in_out){
        this.in_out = in_out;
    }

    public void setAttivo(boolean attivo){
        this.attivo = attivo;
    }

    public void setTipo(String tipo){
        this.tipo = tipo;
    }

    public void setChildsize(long childsize){
        this.childsize = childsize;
    }

    public void setLocation(String location){
        this.location = location;
    }

}