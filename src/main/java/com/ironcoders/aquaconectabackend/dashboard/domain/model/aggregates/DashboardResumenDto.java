package com.ironcoders.aquaconectabackend.dashboard.domain.model.aggregates;

public class DashboardResumenDto {
    private long totalProveedores;
    private long totalResidentes;
    private long suscripcionesActivas;
    private float ingresosTotales;
    private float ingresosMensual;

    // Constructor completo
    public DashboardResumenDto(long totalProveedores, long totalResidentes,
                               long suscripcionesActivas, float ingresosTotales, float ingresosMensual) {
        this.totalProveedores = totalProveedores;
        this.totalResidentes = totalResidentes;
        this.suscripcionesActivas = suscripcionesActivas;
        this.ingresosTotales = ingresosTotales;
        this.ingresosMensual = ingresosMensual;
    }

    // Constructor vacío
    public DashboardResumenDto() {}

    // Getters y setters
    public long getTotalProveedores() {
        return totalProveedores;
    }

    public void setTotalProveedores(long totalProveedores) {
        this.totalProveedores = totalProveedores;
    }

    public long getTotalResidentes() {
        return totalResidentes;
    }

    public void setTotalResidentes(long totalResidentes) {
        this.totalResidentes = totalResidentes;
    }



    public long getSuscripcionesActivas() {
        return suscripcionesActivas;
    }

    public void setSuscripcionesActivas(long suscripcionesActivas) {
        this.suscripcionesActivas = suscripcionesActivas;
    }

    public float getIngresosTotales() {
        return ingresosTotales;
    }

    public void setIngresosTotales(float ingresosTotales) {
        this.ingresosTotales = ingresosTotales;
    }

    public float getIngresosMensual() {
        return ingresosMensual;
    }

    public void setIngresosMensual(float ingresosMensual) {
        this.ingresosMensual = ingresosMensual;
    }
}
