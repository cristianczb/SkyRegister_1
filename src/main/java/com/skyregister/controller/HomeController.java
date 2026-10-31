package com.skyregister.controller;

import com.skyregister.repository.DroneRepository;
import com.skyregister.repository.FlightRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/** Controlador del tablero principal con indicadores resumidos del sistema. */
@Controller
public class HomeController {
    private final DroneRepository droneRepository;
    private final FlightRepository flightRepository;

    public HomeController(DroneRepository droneRepository, FlightRepository flightRepository) {
        this.droneRepository = droneRepository;
        this.flightRepository = flightRepository;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("activePage", "home");
        model.addAttribute("totalDrones", droneRepository.countAll());
        model.addAttribute("availableDrones", droneRepository.countAvailable());
        model.addAttribute("totalFlights", flightRepository.countAll());
        model.addAttribute("scheduledFlights", flightRepository.countScheduled());
        model.addAttribute("recentFlights", flightRepository.findRecent(5));
        return "index";
    }
}
