    package co.edu.usbcali.autosusbcali.mapper;

    import co.edu.usbcali.autosusbcali.domain.Rol;
    import co.edu.usbcali.autosusbcali.dto.request.CrearRolRequest;
    import co.edu.usbcali.autosusbcali.dto.response.ObtenerRolResponse;
    import tools.jackson.core.tree.ObjectTreeNode;

    import java.util.ArrayList;
    import java.util.List;

    public class RolMapper {
        public static ObtenerRolResponse rolAObtenerRolResponse(Rol rol) {
            ObtenerRolResponse obtenerRolResponse =
                    new ObtenerRolResponse(rol.getId(), rol.getNombre());
            return obtenerRolResponse;
        }

        public static List<ObtenerRolResponse> listaRolesAListaObtenerRolResponse(List<Rol> roles) {
            return roles.stream().map(RolMapper::rolAObtenerRolResponse).toList();
        }
        public static  Rol crearRolRequestARol(CrearRolRequest crearRolRequest) {

            return new Rol(null,
                    crearRolRequest.nombre(),
                    crearRolRequest.descripcion(),
                    true
            );
            /*Rol rol = new Rol();
            rol.setNombre(crearRolRequest.nombre());
            rol.setDescripcion(crearRolRequest.descripcion());
            rol.setActivo(true);

            return rol;*/

        }
    }