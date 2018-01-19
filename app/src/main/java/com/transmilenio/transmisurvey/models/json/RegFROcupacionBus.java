package com.transmilenio.transmisurvey.models.json;

/**
 * Created by nataly on 11/01/2018.
 */

public class RegFROcupacionBus {

    private String codigo;
    private String hora_paso;
    private Integer num_bus;


    public RegFROcupacionBus() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getHora_paso() {
        return hora_paso;
    }

    public void setHora_paso(String hora_paso) {
        this.hora_paso = hora_paso;
    }

    public Integer getNum_bus() {
        return num_bus;
    }

    public void setNum_bus(Integer num_bus) {
        this.num_bus = num_bus;
    }

}
