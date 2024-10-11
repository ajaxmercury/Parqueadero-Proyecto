# Parqueadero Proyecto
import java.util.ArrayList;

public class SistemaGestionParqueadero {

   
   public static class Usuario {
        private String nombre;
        private String rol;
        private String id;

   public Usuario(String nombre, String rol, String id) {
            this.nombre = nombre;
            this.rol = rol;
            this.id = id;
        }
    public String getNombre() {
            return nombre;
        }
       public void setNombre(String nombre) {
            this.nombre = nombre;
        }
      public String getRol() {
            return rol;
        }
      public void setRol(String rol) {
            this.rol = rol;
        }
    public String getId() {
            return id;
        }
    public void setId(String id) {
            this.id = id;
        }
    public String toString() {
            return "Usuario: " + nombre + ", Rol: " + rol + ", ID: " + id;
        }
    }


  public static class GestionUsuarios {
        private ArrayList<Usuario> usuarios;

   public GestionUsuarios() {
            this.usuarios = new ArrayList<>();
        }
     public void crearUsuario(String nombre, String rol, String id) {
            Usuario nuevoUsuario = new Usuario(nombre, rol, id);
            usuarios.add(nuevoUsuario);
            System.out.println("Usuario creado: " + nuevoUsuario);
        }
     public Usuario buscarUsuario(String id) {
            for (Usuario usuario : usuarios) {
                if (usuario.getId().equals(id)) {
                    System.out.println("Usuario encontrado: " + usuario);
                    return usuario;
                }
            }
            System.out.println("Usuario no encontrado.");
            return null;
        }
     public void modificarUsuario(String id, String nuevoNombre, String nuevoRol) {
            Usuario usuario = buscarUsuario(id);
            if (usuario != null) {
                usuario.setNombre(nuevoNombre);
                usuario.setRol(nuevoRol);
                System.out.println("Usuario modificado: " + usuario);
            }
        }

   public void eliminarUsuario(String id) {
            Usuario usuario = buscarUsuario(id);
            if (usuario != null) {
                usuarios.remove(usuario);
                System.out.println("Usuario eliminado: " + usuario);
            }
        }
    }

   
  public static class ControlAcceso {
        public boolean verificarAcceso(String codigoQR) {
            if (codigoQR != null && codigoQR.length() == 6) {
                System.out.println("Acceso permitido.");
                return true;
            } else {
                System.out.println("Acceso denegado.");
                return false;
            }
        }
    }

    
  public static void main(String[] args) {
        GestionUsuarios gestionUsuarios = new GestionUsuarios();
        ControlAcceso controlAcceso = new ControlAcceso();
        gestionUsuarios.crearUsuario();
        gestionUsuarios.crearUsuario();
        gestionUsuarios.buscarUsuario();
        gestionUsuarios.modificarUsuario();
        gestionUsuarios.eliminarUsuario();
       controlAcceso.verificarAcceso();
        controlAcceso.verificarAcceso();
        }
}
