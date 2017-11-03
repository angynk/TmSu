package com.transmilenio.transmisurvey.models.db;

import com.transmilenio.transmisurvey.app.MyApplication;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;


public class Cuadro extends RealmObject {

    @PrimaryKey
    private int id;
    @Required
    private String fecha;
    private String diaSemana;
    private String servicio;
    private String numBus;
    private String aforador;
    private int recorrido;
    private int numPuerta;

    @Ignore
    private boolean isSelected;

    private Date now;
    private String nombreEncuesta;

    private RealmList<Registro> registros;

    public Cuadro() {
        this.id = MyApplication.cuadroID.incrementAndGet();
    }

    public Cuadro(String fecha, String diaSemana, String servicio, String numBus, int recorrido,String nombreEncuesta) {
        this.fecha = fecha;
        this.diaSemana = diaSemana;
        this.servicio = servicio;
        this.numBus = numBus;
        this.recorrido = recorrido;
        this.id = MyApplication.cuadroID.incrementAndGet();
        this.registros = new RealmList<Registro>();
        this.now = new Date();
        this.nombreEncuesta = nombreEncuesta;
    }

    public int getId() {
        return id;
    }


    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getNumBus() {
        return numBus;
    }

    public void setNumBus(String numBus) {
        this.numBus = numBus;
    }

    public int getRecorrido() {
        return recorrido;
    }

    public void setRecorrido(int recorrido) {
        this.recorrido = recorrido;
    }


    public RealmList<Registro> getRegistros() {
        return registros;
    }

    public void setRegistros(RealmList<Registro> registros) {
        this.registros = registros;
    }

    public Date getNow() {
        return now;
    }

    public void setNow(Date now) {
        this.now = now;
    }

    public String getNombreEncuesta() {
        return nombreEncuesta;
    }

    public void setNombreEncuesta(String nombreEncuesta) {
        this.nombreEncuesta = nombreEncuesta;
    }

    public int getNumPuerta() {
        return numPuerta;
    }

    public void setNumPuerta(int numPuerta) {
        this.numPuerta = numPuerta;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getAforador() {
        return aforador;
    }

    public void setAforador(String aforador) {
        this.aforador = aforador;
    }
}
