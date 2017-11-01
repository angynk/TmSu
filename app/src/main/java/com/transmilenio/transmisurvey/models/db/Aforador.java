package com.transmilenio.transmisurvey.models.db;

import com.transmilenio.transmisurvey.app.MyApplication;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;



public class Aforador extends RealmObject {

    @PrimaryKey
    private int id;
    @Required
    private String user;
    @Required
    private String contrasena;

    public Aforador() {
        this.id = MyApplication.aforadorID.incrementAndGet();
    }

    public Aforador(String user, String contrasena) {
        this.user = user;
        this.contrasena = contrasena;
        this.id = MyApplication.aforadorID.incrementAndGet();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
