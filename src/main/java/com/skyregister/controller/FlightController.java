package com.skyregister.controller;

import com.skyregister.model.Flight;
import com.skyregister.repository.DroneRepository;
import com.skyregister.repository.FlightRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Endpoints web para consultar y programar vuelos. */
@Controller
public class FlightController {
    private final FlightRepository flightRepository;
    private final DroneRepository droneRepository;

    public FlightController(FlightRepository flightRepository, DroneRepository droneRepository) {
        this.flightRepository = flightRepository;
        this.droneRepository = droneRepository;
    }

    @GetMapping("/flights")
    public String list(Model model) {
        model.addAttribute("activePage", "flights");
        model.addAttribute("flights", flightRepository.findAll());
        return "flights/list";
    }

    @GetMapping("/flights/new")
    public String createForm(Model model, RedirectAttributes redirectAttributes) {
        boolean hasAvailableDrone = droneRepository.findAll().stream()
                .anyMatch(drone -> "Disponible".equals(drone.getStatus()));
        if (!hasAvailableDrone) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "No hay drones disponibles. Registra uno o cambia el estado de un equipo.");
            return "redirect:/drones";
        }
        model.addAttribute("activePage", "flights");
        model.addAttribute("flight", new Flight());
        addFormOptions(model);
        return "flights/form";
    }

    @PostMapping("/flights")
    public String create(@Valid @ModelAttribute("flight") Flight flight,
                         BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        // Se vuelve a cargar el catálogo para conservar las opciones si hay errores de validación.
        model.addAttribute("activePage", "flights");
        addFormOptions(model);
        if (flight.getDroneId() != null) {
            var selectedDrone = droneRepository.findById(flight.getDroneId());
            if (selectedDrone.isEmpty()) {
                result.rejectValue("droneId", "invalid", "El dron seleccionado no existe.");
            } else if (!"Disponible".equals(selectedDrone.get().getStatus())) {
                result.rejectValue("droneId", "unavailable", "El dron seleccionado no está disponible.");
            }
        }
        if (result.hasErrors()) {
            return "flights/form";
        }
        flightRepository.save(flight);
        redirectAttributes.addFlashAttribute("successMessage", "Vuelo programado correctamente.");
        return "redirect:/flights";
    }

    private void addFormOptions(Model model) {
        // Solo se muestran drones disponibles para una nueva programación.
        model.addAttribute("drones", droneRepository.findAll().stream()
                .filter(drone -> "Disponible".equals(drone.getStatus()))
                .toList());
    }
}
