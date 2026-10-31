package com.skyregister.controller;

import com.skyregister.model.Drone;
import com.skyregister.repository.DroneRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

/** Endpoints web para consultar, crear, editar y eliminar drones. */
@Controller
public class DroneController {
    private final DroneRepository droneRepository;

    public DroneController(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    @GetMapping("/drones")
    public String list(Model model) {
        model.addAttribute("activePage", "drones");
        model.addAttribute("drones", droneRepository.findAll());
        return "drones/list";
    }

    @GetMapping("/drones/new")
    public String createForm(Model model) {
        model.addAttribute("activePage", "drones");
        model.addAttribute("drone", new Drone());
        model.addAttribute("pageTitle", "Registrar dron");
        model.addAttribute("formAction", "/drones");
        return "drones/form";
    }

    @PostMapping("/drones")
    public String create(@Valid @ModelAttribute("drone") Drone drone,
                         BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        validateSerial(drone, result);
        if (result.hasErrors()) {
            model.addAttribute("activePage", "drones");
            model.addAttribute("pageTitle", "Registrar dron");
            model.addAttribute("formAction", "/drones");
            return "drones/form";
        }
        droneRepository.save(drone);
        redirectAttributes.addFlashAttribute("successMessage", "Dron registrado correctamente.");
        return "redirect:/drones";
    }

    @GetMapping("/drones/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return droneRepository.findById(id).map(drone -> {
            model.addAttribute("activePage", "drones");
            model.addAttribute("drone", drone);
            model.addAttribute("pageTitle", "Editar dron");
            model.addAttribute("formAction", "/drones/" + id);
            return "drones/form";
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("errorMessage", "No se encontró el dron solicitado.");
            return "redirect:/drones";
        });
    }

    @PostMapping("/drones/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("drone") Drone drone,
                         BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (droneRepository.findById(id).isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "No se encontró el dron solicitado.");
            return "redirect:/drones";
        }
        drone.setId(id);
        validateSerial(drone, result);
        if (result.hasErrors()) {
            model.addAttribute("activePage", "drones");
            model.addAttribute("pageTitle", "Editar dron");
            model.addAttribute("formAction", "/drones/" + id);
            return "drones/form";
        }
        droneRepository.save(drone);
        redirectAttributes.addFlashAttribute("successMessage", "Cambios guardados correctamente.");
        return "redirect:/drones";
    }

    @PostMapping("/drones/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            droneRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Dron eliminado correctamente.");
        } catch (DataIntegrityViolationException exception) {
            // No se permite eliminar un dron que tenga vuelos asociados.
            redirectAttributes.addFlashAttribute("errorMessage",
                    "No se puede eliminar este dron porque tiene vuelos asociados.");
        }
        return "redirect:/drones";
    }

    /** Verifica la unicidad del número de serie antes de guardar. */
    private void validateSerial(Drone drone, BindingResult result) {
        if (drone.getSerialNumber() != null && !drone.getSerialNumber().isBlank()
                && droneRepository.existsBySerialNumber(drone.getSerialNumber(), drone.getId())) {
            result.rejectValue("serialNumber", "duplicate", "Ya existe un dron con ese número de serie.");
        }
    }
}
