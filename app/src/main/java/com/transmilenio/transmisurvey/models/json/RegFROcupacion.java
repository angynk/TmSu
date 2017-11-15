package com.transmilenio.transmisurvey.models.json;

/**
 * Created by nataly on 14/11/2017.
 */

public class RegFROcupacion {

    private String codigo;
    private String hora_paso;
    private Integer ocupacion;

    public RegFROcupacion() {
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

    public Integer getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(Integer ocupacion) {
        this.ocupacion = ocupacion;
    }
}
