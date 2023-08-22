package com.ceiba.tiendatecnologica.dominio.servicio.vendedor;

import com.ceiba.tiendatecnologica.dominio.GarantiaExtendida;
import com.ceiba.tiendatecnologica.dominio.Producto;
import com.ceiba.tiendatecnologica.dominio.excepcion.GarantiaExtendidaException;
import com.ceiba.tiendatecnologica.dominio.repositorio.RepositorioGarantiaExtendida;
import com.ceiba.tiendatecnologica.dominio.repositorio.RepositorioProducto;

import java.util.Calendar;

public class ServicioVendedor {

    public static final String EL_PRODUCTO_TIENE_GARANTIA = "El producto ya cuenta con una garantía extendida";
    public static final String EL_PRODUCTO_NO_CUENTA_CON_GARANTIA = "Este producto no cuenta con garantía extendida";

    private RepositorioProducto repositorioProducto;
    private RepositorioGarantiaExtendida repositorioGarantia;

    public ServicioVendedor(RepositorioProducto repositorioProducto, RepositorioGarantiaExtendida repositorioGarantia) {
        this.repositorioProducto = repositorioProducto;
        this.repositorioGarantia = repositorioGarantia;
    }

    public void generarGarantia(String codigo, String nombreCliente) {
        double precioGarantia = 0.0;
        Calendar init = Calendar.getInstance();
        Calendar end;
        Producto producto = repositorioProducto.obtenerPorCodigo(codigo);
        if (producto.getPrecio() > 500000) {
            precioGarantia = producto.getPrecio() * 0.20;
            end = rules(200);
        } else {
            precioGarantia = producto.getPrecio() * 0.10;
            end = rules(100);
        }
        GarantiaExtendida garantiaExtendida = new GarantiaExtendida(producto, init.getTime(),
                end.getTime(), precioGarantia, nombreCliente);
        repositorioGarantia.agregar(garantiaExtendida);
    }

    private Calendar rules(int days) {
        Calendar init = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.add(Calendar.DATE, days);
        end = ciclo(init,end);
        if (end.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            end.add(Calendar.DATE, 2);
        }
        return end;
    }
    private Calendar  ciclo (Calendar init,Calendar end){
        int cantidadDiaLunes = cantidadLunes(init,end);
        Calendar initTemp = (Calendar) init.clone();
        Calendar endTemp = (Calendar) end.clone();
        while (cantidadDiaLunes>0){
            initTemp = (Calendar) endTemp.clone();
            endTemp.add(Calendar.DATE, cantidadDiaLunes);
            cantidadDiaLunes=cantidadLunes(initTemp,endTemp);
        }
        return endTemp;
    }

    private int cantidadLunes(Calendar init,Calendar end ){
        Calendar initTemp = (Calendar) init.clone();
        int cantidadDiaLunes = 0;
        for (initTemp.getTime(); initTemp.before(end); initTemp.add(Calendar.DATE, 1), initTemp.getTime()) {
            if (initTemp.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                cantidadDiaLunes++;
            }
        }
        return cantidadDiaLunes;
    }

    public boolean tieneGarantia(String codigo) {
        if (repositorioGarantia.obtenerProductoConGarantiaPorCodigo(codigo) != null) {
			return true;
        }
        return false;
    }
}
