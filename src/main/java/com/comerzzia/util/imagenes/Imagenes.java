/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.util.imagenes;

import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import es.mpsistemas.util.log.Logger;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Admin
 */
public class Imagenes {

    private static final Logger log = Logger.getMLogger(Imagenes.class);

    public static void cambiarImagenPublicidad(JLabel label) {
        ImageIcon ii = cambiarImagen("publicidad", 138, 563);
        if (ii != null) {
            label.setVerticalAlignment(JLabel.CENTER);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVisible(false);
            //label.setIcon(ii);
        }
    }

    public static void cambiarImagenArticulo(JLabel label, String referencia) {
        log.info("cambiarImagenArticulo() - Cambiando imagen para referencia:" + referencia);
        ImageIcon ii = cambiarImagen(referencia, 400, 300);
        if (ii != null) {
            label.setVerticalAlignment(JLabel.CENTER);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setIcon(ii);
        }
    }

    public static void cambiarImagenArticuloTamanyo(JLabel label, String referencia, Integer ancho, Integer alto) {
        log.info("cambiarImagenArticulo() - Cambiando imagen para referencia:" + referencia);
        ImageIcon ii = cambiarImagen(referencia, ancho, alto);
        if (ii != null) {
            label.setVerticalAlignment(JLabel.CENTER);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setIcon(ii);
        }
    }

    private static ImageIcon cambiarImagen(String codigo, int ancho, int alto) {

        if (codigo == null) {
            String prefijo = Variables.getVariable(Variables.POS_UI_SKIN);
            URL imagenPorDefecto = prefijo.getClass().getResource("/skin/" + prefijo + "/" + prefijo + "_sinfoto.jpg");
            return cargaImagenPorDefecto(imagenPorDefecto, ancho, alto);
        }
        if (Sesion.getDatosConfiguracion().isModoDesarrollo()) {
            URL imagenPorDefecto = null;
            if (codigo.equals("publicidad")) {
                String prefijo = Variables.getVariable(Variables.POS_UI_SKIN);               
                imagenPorDefecto = prefijo.getClass().getResource("/skin/" + prefijo + "/" + prefijo + "_banner.png");
//                imagenPorDefecto = null;
            } else {
                String prefijo = Variables.getVariable(Variables.POS_UI_SKIN);
                imagenPorDefecto = prefijo.getClass().getResource("/skin/" + prefijo + "/" + prefijo + "_sinfoto.jpg");
//                imagenPorDefecto = null;
            }
            return cargaImagenPorDefecto(imagenPorDefecto, ancho, alto);
        }
        if (codigo.equals("publicidad")) {
            try {
                //URL uri = new URL(VariablesAlm.getVariable(VariablesAlm.URL_IMAGENES) + "?publicidad=" + codigo);
                 String prefijo = Variables.getVariable(Variables.POS_UI_SKIN);
                URL uri = prefijo.getClass().getResource("/skin/" + prefijo + "/" + prefijo + "_sinfoto.jpg");
                Image i = ImageIO.read(uri);
                Image resized = null;
                int width = i.getWidth(null);
                int height = i.getHeight(null);

                if (width > height) {
                    Float f = ((float) height) / (((float) width) / ancho);
                    resized = i.getScaledInstance(ancho, f.intValue(), java.awt.Image.SCALE_SMOOTH);
                } else {
                    Float f = ((float) width) / (((float) height) / alto);
                    resized = i.getScaledInstance(f.intValue(), alto, java.awt.Image.SCALE_SMOOTH);
                }
                ImageIcon ii = new ImageIcon(resized);

                return ii;
            } catch (Exception ex) {
                String prefijo = Variables.getVariable(Variables.POS_UI_SKIN);
                ancho = 1;
                alto = 1;
                URL imagenPorDefecto = prefijo.getClass().getResource("/skin/" + prefijo + "/" + prefijo + "_sinfoto.jpg");
                return cargaImagenPorDefecto(imagenPorDefecto, ancho, alto);
            }
        } else if (codigo.indexOf(".gif") != -1) {
            try {
                log.info("Código de imagen de artículo : " + codigo);
                URL uri = new URL(VariablesAlm.getVariable(VariablesAlm.URL_IMAGENES) + "?codart=" + codigo);
                ImageIcon imgIcon = new ImageIcon(uri);
                return new ImageIcon(uri);
            } catch (Exception ex) {
                // Si no consigue contactar con el servidor, utiliza una imagen por defecto.
                String prefijo = Variables.getVariable(Variables.POS_UI_SKIN);
                URL imagenPorDefecto = prefijo.getClass().getResource("/skin/" + prefijo + "/" + prefijo + "_sinfoto.jpg");

                return cargaImagenPorDefecto(imagenPorDefecto, ancho, alto);
            }
        } else {
            try {
                log.info("Código de imagen de artículo : " + codigo);
                URL uri = new URL(VariablesAlm.getVariable(VariablesAlm.URL_IMAGENES) + "?codart=" + codigo);
                Image i = ImageIO.read(uri);
                Image resized = null;
                int width = i.getWidth(null);
                int height = i.getHeight(null);

                if (width > height) {
                    Float f = ((float) height) / (((float) width) / ancho);
                    resized = i.getScaledInstance(ancho, f.intValue(), java.awt.Image.SCALE_SMOOTH);
                } else {
                    Float f = ((float) width) / (((float) height) / alto);
                    resized = i.getScaledInstance(f.intValue(), alto, java.awt.Image.SCALE_SMOOTH);
                }
                ImageIcon ii = new ImageIcon(resized);

                return ii;

            } catch (Exception ex) {
                // Si no consigue contactar con el servidor, utiliza una imagen por defecto.
                String prefijo = Variables.getVariable(Variables.POS_UI_SKIN);
                URL imagenPorDefecto = prefijo.getClass().getResource("/skin/" + prefijo + "/" + prefijo + "_sinfoto.jpg");

                return cargaImagenPorDefecto(imagenPorDefecto, ancho, alto);
            }
        }
    }

    private static ImageIcon cargaImagenPorDefecto(URL imagenPorDefecto, int ancho, int alto) {
        // Si no consigue contactar con el servidor, utiliza una imagen por defecto.
        try {
            Image i = ImageIO.read(imagenPorDefecto);
            Image resized = null;
            int width = i.getWidth(null);
            int height = i.getHeight(null);

            if (width > height) {
                Float f = ((float) height) / (((float) width) / ancho);

                resized = i.getScaledInstance(ancho, f.intValue(), java.awt.Image.SCALE_SMOOTH);

            } else {
                Float f = ((float) width) / (((float) height) / alto);

                resized = i.getScaledInstance(f.intValue(), alto, java.awt.Image.SCALE_SMOOTH);
            }
            ImageIcon ii = new ImageIcon(resized);
            return ii;
        } catch (Exception e) {
            log.error("cargaImagenPorDefecto() - Error cargando imagen por defecto: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Obtenemos el logo.
     *
     * @param codigoLogo
     * @param ancho_maximo
     * @param alto_maximo
     * @return
     */
    public static BufferedImage obtenerLogo(String codigoLogo, int ancho_maximo, int alto_maximo) {
        try {
            log.debug("obtenerLogo() - Obteniendo Imagen de logo... " + codigoLogo);
            URL uri = new URL(VariablesAlm.getVariable(VariablesAlm.URL_IMAGENES) + "?codigoLogo=" + codigoLogo);
            log.debug("URI:  " + uri.toString());
            BufferedImage i = ImageIO.read(uri);
            Image resized = null;
            int ancho_imagen = i.getWidth(null);

            // Si la imagen es mas alta o ancha de la cuenta, la escalamos
            if (ancho_imagen > ancho_maximo) {
                log.debug("obtenerLogo() - Reescalando Imagen...  ");
                resized = crearCopiaAEscala(i, ancho_maximo, alto_maximo);
            } else {
                log.debug("obtenerLogo() - La imagen no precisa reescalado. ");
                // Si el ancho de la imagen está dentro
                // de los valores aceptados la devolvemos sin escala                  
                Sesion.getLogos().put(codigoLogo, i);
                return i;
            }

            //Obtenemos una BufferedImage a partir de una ImageIcon
            resized = new ImageIcon(resized).getImage();
            BufferedImage bufferedImage = new BufferedImage(
                    resized.getWidth(null),
                    resized.getHeight(null),
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = bufferedImage.createGraphics();
            g.drawImage(resized, 0, 0, null);
            g.dispose();
            //Introducimos la nueva imagen en el mapa de logos
            Sesion.getLogos().put(codigoLogo, bufferedImage);
            return bufferedImage;
        } catch (Exception ex) {
            log.debug("obtenerLogo() - Error en al obtención de la imagen: " + ex.getMessage(), ex);
            return null;
        }
    }

    /*
     * Calcula el factor de escala mínimo y en base a eso escala la imagen 
     * según el dicho factor. 
     * @param nMaxWidth minimo tamaño para el ancho
     * @param nMaxHeight minimo tamaño para el alto
     * @param imgSrc la imágen 
     */
    private static Image crearCopiaAEscala(BufferedImage imgSrc, int maxAnchoImagen, int maxAltoImagen) {
        int nHeight = imgSrc.getHeight();
        int nWidth = imgSrc.getWidth();
        double scaleX = (double) maxAnchoImagen / (double) nWidth;
        double scaleY = (double) maxAltoImagen / (double) nHeight;
        double fScale = Math.min(scaleX, scaleY);
        return scale(fScale, imgSrc);
    }

    /*
     * escala una imagen en porcentaje.
     * @param scale ejemplo: scale=0.6 (escala la imágen al 60%)
     * @param srcImg una imagen BufferedImage
     * @return un BufferedImage escalado
     */
    private static BufferedImage scale(double scale, BufferedImage srcImg) {
        if (scale == 1) {
            return srcImg;
        }
        AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(scale, scale), null);

        return op.filter(srcImg, null);

    }
}
