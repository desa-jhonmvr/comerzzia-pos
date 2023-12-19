/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.util;

import com.comerzzia.jpos.util.JsonUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Gabriel Simbania
 *
 */
public class ClienteRest {

    /**
     * @author Gabriel Simbania
     * @param <T>
     * @param url
     * @param object
     * @param headers
     * @param clsReturn
     * @return
     * @throws ConnectException
     */
    public <T> T clientRestPOST(String url, Object object, Map<String, String> headers, Class<T> clsReturn)
            throws ConnectException, IOException {

        HttpURLConnection conn = null;
        try {

            URL urlRest = new URL(url);
            conn = (HttpURLConnection) urlRest.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            if (headers != null) {
                for (Entry<String, String> entry : headers.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            String input;

            if (object instanceof String) {
                input = (String) object;
            } else {
                input = JsonUtil.objectToJson(object);
            }

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED && conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new ConnectException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            String response = StringUtils.EMPTY;
            while ((output = br.readLine()) != null) {
                response += output;
            }

            conn.disconnect();
            return JsonUtil.jsonToObject(response, clsReturn);

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * @author Gabriel Simbania
     * @param mensaje
     * @return
     * @throws IOException
     */
    public String clientGetByHttpConnection(String mensaje) throws IOException {

        BufferedReader reader = null;
        StringBuilder stringBuilder = null;

        try {
            URL url = new URL(mensaje);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            con.setRequestProperty("Accept", "application/json");
            con.setConnectTimeout(25000);
            con.connect();

            if (con.getResponseCode() != HttpURLConnection.HTTP_CREATED && con.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new ConnectException("Failed : HTTP error code : " + con.getResponseCode());
            }

            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return stringBuilder.toString();

    }

}
