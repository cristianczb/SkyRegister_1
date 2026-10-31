package com.skyregister.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/** Datos de un vuelo programado y del dron asignado. */
public class Flight {
    private Long id;

    @NotNull(message = "Selecciona un dron.")
    private Long droneId;
    private String droneName;

    @NotBlank(message = "El origen es obligatorio.")
    @Size(max = 150, message = "El origen no puede superar 150 caracteres.")
    private String origin;

    @NotBlank(message = "El destino es obligatorio.")
    @Size(max = 150, message = "El destino no puede superar 150 caracteres.")
    private String destination;

    @NotNull(message = "Selecciona la fecha y hora del vuelo.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime scheduledAt;

    @NotNull(message = "Indica la duración estimada.")
    @Min(value = 1, message = "La duración debe ser de al menos un minuto.")
    private Integer durationMinutes;

    @NotBlank(message = "Selecciona el estado del vuelo.")
    private String status = "Programado";

    @Size(max = 500, message = "Las notas no pueden superar 500 caracteres.")
    private String notes;

    public Flight() { }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getDroneId() { return droneId; }
    public void setDroneId(Long droneId) { this.droneId = droneId; }
    public String getDroneName() { return droneName; }
    public void setDroneName(String droneName) { this.droneName = droneName; }
    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
