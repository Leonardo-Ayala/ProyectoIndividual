package com.example.proyectoindividual;

import com.google.firebase.storage.StorageReference;

import java.io.Serializable;

public class Producto implements Serializable {
    public String key;
    public String tipo;
    public String marca;
    public String descripcion;
    public String stock;
    public String precio;
    private StorageReference imagenProducto;

    public Producto(String marca, String tipo, String descripcion, String stock, String precio,String key) {
        this.tipo = tipo;
        this.marca = marca;
        this.descripcion = descripcion;
        this.stock = stock;
        this.precio = precio;
        this.key = key;
    }

    public Producto(String marca, String tipo, String descripcion, String stock, StorageReference imagenProducto,String key , String precio) {
        this.key = key;
        this.tipo = tipo;
        this.marca = marca;
        this.descripcion = descripcion;
        this.stock = stock;
        this.imagenProducto = imagenProducto;
        this.precio = precio;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public StorageReference getImagenProducto() {
        return imagenProducto;
    }

    public void setImagenProducto(StorageReference imagenProducto) {
        this.imagenProducto = imagenProducto;
    }

    public Producto() {
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
