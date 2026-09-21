package controller;

import co.edu.usbcali.autosusbcali.domain.Rol;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import repository.RolRepository;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@AllArgsConstructor
public class RolController {
    private final RolRepository rolRepository;

    @GetMapping
    public List<Rol> obtenerTodos(){
        return rolRepository.findAll();
    }
}
