package com.skyregister.repository;

import com.skyregister.model.Drone;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

/** Acceso a datos de drones con consultas parametrizadas mediante JdbcTemplate. */
@Repository
public class DroneRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Drone> droneRowMapper = (rs, rowNum) -> {
        Date maintenanceDate = rs.getDate("last_maintenance");
        return new Drone(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("model"),
                rs.getString("serial_number"),
                rs.getString("status"),
                rs.getInt("battery_level"),
                maintenanceDate == null ? null : maintenanceDate.toLocalDate()
        );
    };

    public DroneRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Drone> findAll() {
        return jdbcTemplate.query("SELECT * FROM drones ORDER BY id", droneRowMapper);
    }

    public Optional<Drone> findById(Long id) {
        List<Drone> results = jdbcTemplate.query(
                "SELECT * FROM drones WHERE id = ?", droneRowMapper, id);
        return results.stream().findFirst();
    }

    public boolean existsBySerialNumber(String serialNumber, Long excludedId) {
        String sql = excludedId == null
                ? "SELECT COUNT(*) FROM drones WHERE serial_number = ?"
                : "SELECT COUNT(*) FROM drones WHERE serial_number = ? AND id <> ?";
        Integer count = excludedId == null
                ? jdbcTemplate.queryForObject(sql, Integer.class, serialNumber)
                : jdbcTemplate.queryForObject(sql, Integer.class, serialNumber, excludedId);
        return count != null && count > 0;
    }

    public void save(Drone drone) {
        // El INSERT y el UPDATE están separados para mantener explícita la operación CRUD.
        if (drone.getId() == null) {
            jdbcTemplate.update(
                    "INSERT INTO drones (name, model, serial_number, status, battery_level, last_maintenance) " +
                            "VALUES (?, ?, ?, ?, ?, ?)",
                    drone.getName(), drone.getModel(), drone.getSerialNumber(), drone.getStatus(),
                    drone.getBatteryLevel(), drone.getLastMaintenance());
        } else {
            jdbcTemplate.update(
                    "UPDATE drones SET name = ?, model = ?, serial_number = ?, status = ?, " +
                            "battery_level = ?, last_maintenance = ? WHERE id = ?",
                    drone.getName(), drone.getModel(), drone.getSerialNumber(), drone.getStatus(),
                    drone.getBatteryLevel(), drone.getLastMaintenance(), drone.getId());
        }
    }

    public void deleteById(Long id) {
        // La base de datos impide borrar un dron relacionado con vuelos; el controlador informa el caso.
        jdbcTemplate.update("DELETE FROM drones WHERE id = ?", id);
    }

    public int countAll() {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM drones", Integer.class);
        return count == null ? 0 : count;
    }

    public int countAvailable() {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM drones WHERE status = ?", Integer.class, "Disponible");
        return count == null ? 0 : count;
    }
}
