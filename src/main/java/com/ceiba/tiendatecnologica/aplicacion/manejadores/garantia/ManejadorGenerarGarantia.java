package com.ceiba.tiendatecnologica.aplicacion.manejadores.garantia;

import com.ceiba.tiendatecnologica.dominio.GarantiaExtendida;
import com.ceiba.tiendatecnologica.dominio.Producto;
import com.ceiba.tiendatecnologica.dominio.excepcion.GarantiaExtendidaException;
import com.ceiba.tiendatecnologica.dominio.repositorio.RepositorioGarantiaExtendida;
import com.ceiba.tiendatecnologica.dominio.servicio.producto.ServicioObtenerProducto;
import com.ceiba.tiendatecnologica.dominio.servicio.vendedor.ServicioVendedor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@Component
public class ManejadorGenerarGarantia {

    private static final Logger logger = LoggerFactory.getLogger(ManejadorGenerarGarantia.class);

    private final ServicioVendedor servicioVendedor;

    public ManejadorGenerarGarantia(ServicioVendedor servicioVendedor) {
        this.servicioVendedor = servicioVendedor;
    }

    @Transactional
    public void ejecutar(String codigoProducto, String nombreCliente) {

        if(!servicioVendedor.tieneGarantia(codigoProducto)) {
            int quiantotyVocal = codigoProducto.split("(?<=[aeiouAEIOU])").length;
            if (quiantotyVocal == 4) {
                throw new GarantiaExtendidaException(ServicioVendedor.EL_PRODUCTO_NO_CUENTA_CON_GARANTIA);
            }
            servicioVendedor.generarGarantia(codigoProducto, nombreCliente);
        }

    }


}
