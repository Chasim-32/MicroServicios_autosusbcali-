package co.edu.usbcali.autosusbcali.controller;

import co.edu.usbcali.autosusbcali.dto.request.ClearModificationRolRequest;
import co.edu.usbcali.autosusbcali.dto.response.ObtenerRolResponse;
import co.edu.usbcali.autosusbcali.service.IRolService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j // Habilita el registro de logs en el controlador
@RestController
@RequestMapping("/api/roles")
@AllArgsConstructor
public class RolController {

    private final IRolService rolService;

    @GetMapping
    public List<ObtenerRolResponse> obtenerTodos() {
        log.info("REST Request: Recibida petición GET para obtener todos los roles");
        return rolService.obtenerTodos();
    }

    @GetMapping("/activos")
    public List<ObtenerRolResponse> obtenerTodosActivos() {
        log.info("REST Request: Recibida petición GET para obtener roles activos");
        return rolService.buscarRolPorEstado(true);
    }

    @GetMapping("/inactivos")
    public List<ObtenerRolResponse> obtenerTodosInactivos() {
        log.info("REST Request: Recibida petición GET para obtener roles inactivos");
        return rolService.buscarRolPorEstado(false);
    }

    @GetMapping("/por-nombre/{nombre}")
    public ResponseEntity<ObtenerRolResponse> obtenerPorNombre(@PathVariable String nombre) {
        log.info("REST Request: Recibida petición GET para buscar rol por nombre: {}", nombre);
        ObtenerRolResponse rolResponse = rolService.obtenerRolPorNombre(nombre);
        if (rolResponse != null) {
            return new ResponseEntity<>(rolResponse, HttpStatus.OK);
        }
        log.warn("REST Response: Rol con nombre {} no encontrado", nombre);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/por-id/{id}")
    public ResponseEntity<ObtenerRolResponse> obtenerPorId(@PathVariable Long id) {
        log.info("REST Request: Recibida petición GET para buscar rol por ID: {}", id);
        ObtenerRolResponse rolResponse = rolService.obtenerRolPorId(id);
        if (rolResponse != null) {
            return new ResponseEntity<>(rolResponse, HttpStatus.OK);
        }
        log.warn("REST Response: Rol con ID {} no encontrado", id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/guardar")
    public ResponseEntity<ObtenerRolResponse> crearRol(
            @RequestBody ClearModificationRolRequest crearRolRequest) throws Exception {
        log.info("REST Request: Recibida petición POST para crear un nuevo rol");
        ObtenerRolResponse nuevoRol = rolService.crearRol(crearRolRequest);
        return new ResponseEntity<>(nuevoRol, HttpStatus.CREATED);
    }

    @PutMapping("/modificar/{id}")
    public ResponseEntity<ObtenerRolResponse> modificarRol(
            @PathVariable Long id,
            @RequestBody ClearModificationRolRequest crearModificarRolRequest) throws Exception {
        log.info("REST Request: Recibida petición PUT para modificar el rol con ID: {}", id);
        ObtenerRolResponse rolModificado = rolService.modificarRol(id, crearModificarRolRequest);
        return new ResponseEntity<>(rolModificado, HttpStatus.OK);
    }

    // ... (tus otros endpoints) ...

    // Endpoint para Eliminación Física (Borrado real)
    @DeleteMapping("/eliminar-fisico/{id}")
    public ResponseEntity<Void> eliminarRolFisico(@PathVariable Long id) throws Exception {
        log.info("REST Request: Petición DELETE para eliminación física del rol con ID: {}", id);
        rolService.eliminarRolFisico(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Endpoint para Eliminación Lógica (Desactivación / UPDATE)
    @PatchMapping("/eliminar-logico/{id}")
    public ResponseEntity<Void> eliminarRolLogico(@PathVariable Long id) throws Exception {
        log.info("REST Request: Petición PATCH para eliminación lógica del rol con ID: {}", id);
        rolService.eliminarRolLogico(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}