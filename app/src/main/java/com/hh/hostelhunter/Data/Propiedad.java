package com.hh.hostelhunter.Data;

import java.io.Serializable;
import java.util.ArrayList;

public class Propiedad  implements Serializable {
    private int ID=-1;
    private String Propietario="";
    private String nombre="";
    private String descripcion="";
    private String direccion="";
    private int capacidad=-1;
    private String reglas="";
    private double precioPorNoche=-1;
    private ArrayList<String> urlFoto= new ArrayList<>();

    private int likes=0;

    // Constructor vacío
    public Propiedad() {
    }

    // Getters y setters para cada atributo
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
    public String getPropietario() {
        return Propietario;
    }

    public void setPropietario(String Propietario) {
        this.Propietario = Propietario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getReglas() {
        return reglas;
    }

    public void setReglas(String reglas) {
        this.reglas = reglas;
    }

    public double getPrecioPorNoche() {
        return precioPorNoche;
    }

    public void setPrecioPorNoche(double precioPorNoche) {
        this.precioPorNoche = precioPorNoche;
    }

    public ArrayList<String> getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto( ArrayList<String> urlFoto) {
        this.urlFoto = urlFoto;
    }


    @Override
    public String toString() {
        return "Propiedad{" +
                "Propietario='" + Propietario + '\'' +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", direccion='" + direccion + '\'' +
                ", capacidad=" + capacidad +
                ", reglas='" + reglas + '\'' +
                ", precioPorNoche=" + precioPorNoche +
                ", urlFoto='" + urlFoto + '\'' +
                '}';
    }
}
