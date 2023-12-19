
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Gabriel Simbania
 */
public class VariosTest {

    public static void main(String[] args) {

        System.out.println("Random 1 "+ Math.abs(ThreadLocalRandom.current().nextInt(100000,999999)));
        System.out.println("Random 1 "+ Math.abs(ThreadLocalRandom.current().nextInt(100000,999999)));
        System.out.println("Random 1 "+ Math.abs(ThreadLocalRandom.current().nextInt(100000,999999)));
        System.out.println("Random 1 "+ Math.abs(ThreadLocalRandom.current().nextInt(100000,999999)));
        System.out.println("Random 1 "+ Math.abs(ThreadLocalRandom.current().nextInt(100000,999999)));
        
        System.out.println("Random 2 "+Math.abs(ThreadLocalRandom.current().nextInt()));
        System.out.println("Random 3 "+Math.abs(ThreadLocalRandom.current().nextInt()));
        
        String encodedString = Base64.getEncoder().encodeToString("Asakus$2019".getBytes());
        System.out.println(encodedString);
        
        byte[] decodedBytes = Base64.getDecoder().decode("QXNha3VzJDIwMTk=");
        System.out.println(new String(decodedBytes));
        
        

    }

}
