package com.example.appmascotas;

public class Mascotas {

    private int id;
    private String tipo, nombre, color;
    private double pesokg;

    public Mascotas(int id, String tipo, String nombre, String color, double pesokg) {
        this.id = id;
        this.tipo = tipo;
        this.nombre = nombre;
        this.color = color;
        this.pesokg = pesokg;
    }

    public Mascotas(String tipo, String nombre, String color, double pesokg) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.color = color;
        this.pesokg = pesokg;
    }

    public Mascotas() {
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPesokg() {
        return pesokg;
    }

    public void setPesokg(double pesokg) {
        this.pesokg = pesokg;
    }

    @Override
    public String toString() {
        return tipo + " -> " + nombre;
    }
}
