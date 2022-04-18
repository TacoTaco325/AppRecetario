package com.example.healthycook;

public class Receta {
    private String id, Nombre, Calorias, Imagen;

    public Receta() {
    }

    public Receta(String id, String nombre, String calorias, String imagen) {
        this.id = id;
        this.Nombre = nombre;
        this.Calorias = calorias;
        this.Imagen = imagen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getCalorias() {
        return Calorias;
    }

    public void setCalorias(String calorias) {
        Calorias = calorias;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }
}
