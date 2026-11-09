package co.edu.usbcali.autosusbcali.service;

import co.edu.usbcali.autosusbcali.domain.Rol;
import co.edu.usbcali.autosusbcali.dto.request.ClearModificationRolRequest;
import co.edu.usbcali.autosusbcali.dto.response.ObtenerRolResponse;
import co.edu.usbcali.autosusbcali.mapper.RolMapper;
import co.edu.usbcali.autosusbcali.repository.RolRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class RolService implements IRolService {

    private final RolRepository rolRepository;

    @Override
    public List<ObtenerRolResponse> obtenerTodos() {
        log.info("Iniciando obtenertodos()");
        log.warn("Hola soy Jose Manuel");
        return RolMapper.listaRolesAListaObtenerRolResponse(rolRepository.findAll());
    }

    @Override
    public List<ObtenerRolResponse> buscarRolPorEstado(Boolean activo) {
        log.info("Buscando roles filtrados por estado activo: {}", activo);
        List<Rol> rolesPorEstado = rolRepository.findByActivo(activo);
        log.info("Se encontraron {} roles con estado activo={}", rolesPorEstado.size(), activo);
        return RolMapper.listaRolesAListaObtenerRolResponse(rolesPorEstado);
    }

    @Override
    public ObtenerRolResponse obtenerRolPorNombre(String nombre) {
        log.info("Buscando rol por nombre: {}", nombre);
        Rol rolPorNombre = rolRepository.findByNombre(nombre)
                .orElseThrow(() -> {
                    log.warn("No se ha encontrado el rol con nombre: {}", nombre);
                    return new RuntimeException("No se ha encontrado el rol por nombre: " + nombre);
                });
        return RolMapper.rolAObtenerRolResponse(rolPorNombre);
    }

    @Override
    public ObtenerRolResponse obtenerRolPorId(Long id) {
        log.info("Buscando rol por id: {}", id);
        Rol rolPorId = rolRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se ha encontrado el rol con id: {}", id);
                    return new RuntimeException("No se ha encontrado el rol por id " + id);
                });
        return RolMapper.rolAObtenerRolResponse(rolPorId);
    }

    @Override
    @Transactional
    public ObtenerRolResponse crearRol(ClearModificationRolRequest crearRolRequest) throws Exception {
        log.info("Iniciando proceso de creación de rol");

        if (crearRolRequest == null) {
            log.warn("El objeto crearRolRequest recibido es nulo");
            throw new Exception("El objeto CrearRolRequest no puede ser nulo.");
        }
        if (crearRolRequest.nombre() == null || crearRolRequest.nombre().isBlank()) {
            log.warn("Intento de crear rol con nombre nulo o vacío");
            throw new Exception("El nombre no puede ser nulo ni esta vacio.");
        }
        if (crearRolRequest.descripcion() == null || crearRolRequest.descripcion().isBlank()) {
            log.warn("Intento de crear rol con descripción nula o vacía");
            throw new Exception("La descripción no puede ser nula ni esta vacia.");
        }

        if (rolRepository.existsByNombre(crearRolRequest.nombre())) {
            log.warn("Intento de crear un rol duplicado con nombre: {}", crearRolRequest.nombre());
            throw new Exception("Ya existe un rol con nombre " + crearRolRequest.nombre());
        }

        Rol rol = RolMapper.crearRolRequestARol(crearRolRequest);
        Rol rolGuardado = rolRepository.save(rol);

        log.info("Rol creado y persistido exitosamente con ID: {}", rolGuardado.getId());
        return RolMapper.rolAObtenerRolResponse(rolGuardado);
    }

    @Override
    @Transactional
    public ObtenerRolResponse modificarRol(Long id, ClearModificationRolRequest crearModificarRolRequest) throws Exception {
        log.info("Iniciando modificación para el rol con ID: {}", id);

        if (id == null) {
            log.warn("El id proporcionado para modificar es nulo");
            throw new Exception("El id no puede ser nulo.");
        }
        if (crearModificarRolRequest == null) {
            log.warn("El DTO de modificación recibido es nulo");
            throw new Exception("El objeto CrearModificarRolRequest no puede ser nulo.");
        }
        if (crearModificarRolRequest.nombre() == null || crearModificarRolRequest.nombre().isBlank()) {
            log.warn("Intento de actualizar con nombre nulo o vacío");
            throw new Exception("El nombre no puede ser nulo ni estar vacío.");
        }
        if (crearModificarRolRequest.descripcion() == null || crearModificarRolRequest.descripcion().isBlank()) {
            log.warn("Intento de actualizar con descripción nula o vacía");
            throw new Exception("La descripcion no puede ser nula ni estar vacía.");
        }

        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontró el rol con id {} para modificar", id);
                    return new Exception("No existe un rol con el id " + id);
                });

        if (!rol.getNombre().equals(crearModificarRolRequest.nombre())
                && rolRepository.existsByNombre(crearModificarRolRequest.nombre())) {
            log.warn("Conflicto: Ya existe otro rol con el nombre: {}", crearModificarRolRequest.nombre());
            throw new Exception("Ya existe otro rol con el mismo nombre.");
        }

        rol.setNombre(crearModificarRolRequest.nombre());
        rol.setDescripcion(crearModificarRolRequest.descripcion());

        Rol rolActualizado = rolRepository.save(rol);
        log.info("Rol con ID {} modificado exitosamente", id);

        return RolMapper.rolAObtenerRolResponse(rolActualizado);
    }

    // ... (tus otros métodos existentes) ...

    @Override
    @Transactional
    public void eliminarRolFisico(Long id) throws Exception {
        log.info("Iniciando eliminación física (DELETE) del rol con ID: {}", id);

        if (id == null) {
            log.warn("El id proporcionado para la eliminación física es nulo");
            throw new Exception("El id no puede ser nulo.");
        }

        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontró el rol con ID {} para eliminación física", id);
                    return new Exception("No existe un rol con el id " + id);
                });

        // Borrado definitivo de la base de datos
        rolRepository.delete(rol);
        log.info("Rol con ID {} eliminado físicamente de la base de datos", id);
    }

    @Override
    @Transactional
    public void eliminarRolLogico(Long id) throws Exception {
        log.info("Iniciando eliminación lógica (desactivación) del rol con ID: {}", id);

        if (id == null) {
            log.warn("El id proporcionado para la eliminación lógica es nulo");
            throw new Exception("El id no puede ser nulo.");
        }

        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontró el rol con ID {} para eliminación lógica", id);
                    return new Exception("No existe un rol con el id " + id);
                });

        // Validación: verificar si ya estaba inactivo
        if (Boolean.FALSE.equals(rol.getActivo())) {
            log.warn("El rol con ID {} ya se encuentra inactivo", id);
            throw new Exception("El rol ya se encuentra inactivo.");
        }

        // Cambio de estado (Baja lógica)
        rol.setActivo(false);
        rolRepository.save(rol);

        log.info("Rol con ID {} desactivado (eliminación lógica) exitosamente", id);
    }
}