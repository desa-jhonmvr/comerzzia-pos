/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.util;

import com.comerzzia.jpos.dto.UsuarioDTO;

/**
 *
 * @author Gabriel Simbania
 */
public class UtilUsuario {

    /**
     * @author Gabriel Simbania
     * @param usuario
     * @return 
     */
    public static UsuarioDTO verificarUsuarioDTO(String usuario) {

        String usuarioAdmin = "ADMINISTRADOR";
        String codEmpleadoAdmin = "99990";
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        if (usuarioAdmin.equals(usuario.toUpperCase())) {
            usuarioDTO.setCodEmpleado(Long.parseLong(codEmpleadoAdmin));
        } else {
            usuarioDTO.setCodEmpleado(Long.parseLong(usuario));
        }

        return usuarioDTO;

    }

}
