package com.example.fitcal.bean;

import java.io.Serializable;

public class User implements Serializable {

    private float peso;
    private int altura;
    private int edad;
    private String genero;
    private String actividadFisica;
    private String objetivoPeso;
    private float tmb;
    private float gastoEnergia;

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getActividadFisica() {
        return actividadFisica;
    }

    public void setActividadFisica(String actividadFisica) {
        this.actividadFisica = actividadFisica;
    }

    public String getObjetivoPeso() {
        return objetivoPeso;
    }

    public void setObjetivoPeso(String objetivoPeso) {
        this.objetivoPeso = objetivoPeso;
    }

    public float getTmb() {
        return tmb;
    }

    public void setTmb(float tmb) {
        this.tmb = tmb;
    }

    public float getGastoEnergia() {
        return gastoEnergia;
    }

    public void setGastoEnergia(float gastoEnergia) {
        this.gastoEnergia = gastoEnergia;
    }
}
