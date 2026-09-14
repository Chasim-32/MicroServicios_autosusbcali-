package co.edu.usbcali.autosusbcali.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles_permisos")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RolPermiso {

    @Id
    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;

    @ManyToOne
    @JoinColumn(name = "permiso_id")
    private Permiso permiso;
}