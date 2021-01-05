package com.example.proyectoindividual;

import java.io.Serializable;

public class Venta implements Serializable {

    private Producto producto;
    private String nombreCliente;
    private String dni;
    private String facbol;
    private String uidVendedor;
    private String cantidad;
    private String key;

    public Venta(Producto producto, String nombreCliente, String dni, String facbol, String uidVendedor, String cantidad, String key) {
        this.producto = producto;
        this.nombreCliente = nombreCliente;
        this.dni = dni;
        this.facbol = facbol;
        this.uidVendedor = uidVendedor;
        this.cantidad = cantidad;
        this.key = key;
    }

    public Venta() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getFacbol() {
        return facbol;
    }

    public void setFacbol(String facbol) {
        this.facbol = facbol;
    }

    public String getUidVendedor() {
        return uidVendedor;
    }

    public void setUidVendedor(String uidVendedor) {
        this.uidVendedor = uidVendedor;
    }
}
