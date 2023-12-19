/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.util.InOutStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Gabriel Simbania
 */
public class UtilInputOutputStream {

    /**
     *
     * @param by
     * @return
     * @throws IOException
     */
    public static File transformardebytesAfile(byte[] by) throws IOException {
        File archivoDestino = new File("tmp");
        OutputStream out = new FileOutputStream(archivoDestino);
        out.write(by);
        out.close();
        return archivoDestino;
    }
    
    
    public static byte[] trasformarFileabytes(File archi) throws FileNotFoundException, IOException {

        FileInputStream ficheroStream = new FileInputStream(archi);
        byte contenido[] = new byte[(int) archi.length()];
        ficheroStream.read(contenido);
        return contenido;
    }

}
