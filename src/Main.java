import Criptografia.Criptografia;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Base64;
//import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, SQLException {

        //EXERCICI 1.1
        System.out.println("EXERCICI 1.1");
        Criptografia.CreaClauSimetrica();


        //EXERCICI 1.2
        System.out.println("\n\nEXERCICI 1.2");
        System.out.println("El Hash del teu nom i cognom es: "+Criptografia.HashSHA512("Nom i cognom"));


        //EXERCICI 1.3
        System.out.println("\n\nEXERCICI 1.3");
        System.out.println("Mostrar clau simètrica d'un text: "+Criptografia.HashSHA512("Aquest es el text"));


        //EXERCICI 1.4
        System.out.println("\n\nEXERCICI 1.4");
        System.out.println("El Hash del teu nom i cognom am salt es: "+Criptografia.generateSaltedSHA512("Nom i cognom"));


//        EXERCICI 1.6
//        System.out.println("\n\nEXERCICI 1.6");
//        Criptografia.guardarUsuarioYPass("Userr", "Patata123");


        //EXERCICI 1.8
        System.out.println("\n\nEXERCICI 1.7");
        KeyPair keyPair = Criptografia.generateKeyPair();
        System.out.println("Clau pública: " + keyPair.getPublic());
        System.out.println("Clau privada: " + keyPair.getPrivate());


        //EXERCICI 1.9
        System.out.println("\n\nEXERCICI 1.9");
        try {
            String password = "Patata123";
            String encryptedPassword = Criptografia.encryptPassword(password, keyPair.getPrivate());
            System.out.println("Contrasenya encriptada: " + encryptedPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //EXERCICI 1.10
        System.out.println("\n\nEXERCICI 1.10");
        try {
            String password = "Patata123";
            String encryptedPasswordHash = Criptografia.encryptPasswordHash(password, keyPair.getPrivate());
            System.out.println("Hash de la contrasenya encriptat: " + encryptedPasswordHash);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //EXERCICI 1.11
        System.out.println("\n\nEXERCICI 1.11");
//        try {
//            String password = "Patata123";
//            String passwordDesen = Criptografia.encryptPassword(password, keyPair.getPrivate());
//            byte [] ContrasenyaDesencriptada = Criptografia.desencryptPassword(passwordDesen.getBytes(), keyPair.getPublic());
//            System.out.println("Hash de la contrasenya desencriptat: " + new String(ContrasenyaDesencriptada));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        //EXERCICI 1.12
        //Al cmd de windows
        // keytool -genseckey -alias clauSimetricaPreExamen -keyalg AES -keysize 128 -storetype JCEKS -keystore symetric.keystore
        // keytool -list -keystore symetric.keystore -storetype JCEKS -alias clauSimetricaPreExamen


        //EXERCICI 1.13
        System.out.println("\n\nEXERCICI 1.13");
//        Criptografia.GetKeytool();


        //EXERCICI 1.14
        System.out.println("\n\nEXERCICI 1.14");
//        Criptografia.PutKeytool();






    }
}
