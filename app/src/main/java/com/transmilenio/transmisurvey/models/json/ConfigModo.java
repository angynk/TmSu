package com.transmilenio.transmisurvey.models.json;


import java.util.List;


public class ConfigModo {

    private List<ModoJSON> modos;

    public ConfigModo() {
    }

    public List<ModoJSON> getModos() {
        return modos;
    }

    public void setModos(List<ModoJSON> modos) {
        this.modos = modos;
    }
}
