package com.skyregister.repository;

import com.skyregister.model.Flight;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/** Consultas JDBC para listar y registrar vuelos. */
@Repository
public class FlightRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Flight> flightRowMapper = (rs, rowNum) -> {
        Flight flight = new Flight();
        flight.setId(rs.getLong("id"));
        flight.setDroneId(rs.getLong("drone_id"));
        flight.setDroneName(rs.getString("drone_name"));
        flight.setOrigin(rs.getString("origin"));
        flight.setDestination(rs.getString("destination"));
        Timestamp timestamp = rs.getTimestamp("scheduled_at");
        flight.setScheduledAt(timestamp == null ? null : timestamp.toLocalDateTime());
        flight.setDurationMinutes(rs.getInt("duration_minutes"));
        flight.setStatus(rs.getString("status"));
        flight.setNotes(rs.getString("notes"));
        return flight;
    };

    public FlightRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Flight> findAll() {
        // El JOIN agrega el nombre del dron para mostrarlo sin una consulta adicional por fila.
        return jdbcTemplate.query("SELECT f.*, d.name AS drone_name FROM flights f " +
                "JOIN drones d ON d.id = f.drone_id ORDER BY f.scheduled_at DESC", flightRowMapper);
    }

    public List<Flight> findRecent(int limit) {
        return jdbcTemplate.query("SELECT f.*, d.name AS drone_name FROM flights f " +
                        "JOIN drones d ON d.id = f.drone_id ORDER BY f.scheduled_at DESC LIMIT ?",
                flightRowMapper, limit);
    }

    public void save(Flight flight) {
        jdbcTemplate.update("INSERT INTO flights (drone_id, origin, destination, scheduled_at, " +
                        "duration_minutes, status, notes) VALUES (?, ?, ?, ?, ?, ?, ?)",
                flight.getDroneId(), flight.getOrigin(), flight.getDestination(),
                Timestamp.valueOf(flight.getScheduledAt()), flight.getDurationMinutes(),
                flight.getStatus(), flight.getNotes());
    }

    public int countAll() {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM flights", Integer.class);
        return count == null ? 0 : count;
    }

    public int countScheduled() {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM flights WHERE status = ?", Integer.class, "Programado");
        return count == null ? 0 : count;
    }
}
