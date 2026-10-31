package com.skyregister.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/** Modelo de dominio para representar un dron registrado en SkyRegister. */
public class Drone {
    private Long id;

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres.")
    private String name;

    @NotBlank(message = "El modelo es obligatorio.")
    @Size(max = 100, message = "El modelo no puede superar 100 caracteres.")
    private String model;

    @NotBlank(message = "El número de serie es obligatorio.")
    @Size(max = 80, message = "El número de serie no puede superar 80 caracteres.")
    private String serialNumber;

    @NotBlank(message = "Selecciona el estado del dron.")
    private String status = "Disponible";

    @NotNull(message = "Indica el nivel de batería.")
    @Min(value = 0, message = "La batería mínima es 0 %.")
    @Max(value = 100, message = "La batería máxima es 100 %.")
    private Integer batteryLevel = 100;

    private LocalDate lastMaintenance;

    public Drone() { }

    public Drone(Long id, String name, String model, String serialNumber,
                 String status, Integer batteryLevel, LocalDate lastMaintenance) {
        this.id = id;
        this.name = name;
        this.model = model;
        this.serialNumber = serialNumber;
        this.status = status;
        this.batteryLevel = batteryLevel;
        this.lastMaintenance = lastMaintenance;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getBatteryLevel() { return batteryLevel; }
    public void setBatteryLevel(Integer batteryLevel) { this.batteryLevel = batteryLevel; }
    public LocalDate getLastMaintenance() { return lastMaintenance; }
    public void setLastMaintenance(LocalDate lastMaintenance) { this.lastMaintenance = lastMaintenance; }
}
