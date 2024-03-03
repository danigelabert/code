package Criptografia;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.sql.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import javax.crypto.*;

public class Criptografia {

    //EXERCICI 1.1
    public static void CreaClauSimetrica() {
        try {
            // Generar una clave DES
            KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
            SecretKey secretKey = keyGenerator.generateKey();

            // Convertir la clave a Base64
            byte[] keyBytes = secretKey.getEncoded();
            String base64Key = Base64.getEncoder().encodeToString(keyBytes);

            // Mostrar la clave en Base64
            System.out.println("Clave DES en Base64: " + base64Key);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //EXERCICI 1.2
    public static String HashSHA512(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] hashedBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convertim els bytes del hash a una representació hexadecimal
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


    //EXERCICI 1.3
    public static void ClauSimetricaAESDesdeHash(String text){
        String textoCodificado = text; // El texto codificado con SHA-256

        try {
            // Generar un hash SHA-256 a partir del texto codificado
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(textoCodificado.getBytes());

            // Truncar el hash a 16 bytes (128 bits) para AES-128
            byte[] truncatedHash = Arrays.copyOf(hash, 16);

            // Crear la clave AES
            SecretKey claveAES = new SecretKeySpec(truncatedHash, "AES");

            // Mostrar la clave en hexadecimal
            byte[] claveBytes = claveAES.getEncoded();
            StringBuilder hexClave = new StringBuilder();
            for (byte b : claveBytes) {
                hexClave.append(String.format("%02x", b));
            }
            System.out.println("Clave AES en hexadecimal: " + hexClave.toString());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    //EXERCICI 1.4
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    public static String generateSaltedSHA512(String input) {
        try {
            String salt = generateSalt();

            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] hashedBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convertimos los bytes del hash a una representación hexadecimal
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


    //EXERCICI 1.6
    public static void guardarUsuarioYPass(String usuario, String contrasena) throws SQLException, NoSuchAlgorithmException {
//        Connection connect = null;
        PreparedStatement pstmt = null;

        String DB_URL = "jdbc:mysql://localhost:3306/preexamen";
        String DB_USER = "daniruben";
        String DB_PASSWORD = "P@tata123";

        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            connection.setAutoCommit(false);

            // Generar salt aleatorio
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            // Convertir salt a hexadecimal
            StringBuilder hexSalt = new StringBuilder();
            for (byte b : salt) {
                hexSalt.append(String.format("%02x", b));
            }

            // Concatenar salt con contraseña
            String contrasenaConSalt = contrasena + hexSalt.toString();

            // Generar hash de la contraseña con salt
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedPassword = md.digest(contrasenaConSalt.getBytes());

            // Convertir hash a hexadecimal
            StringBuilder hexHash = new StringBuilder();
            for (byte b : hashedPassword) {
                hexHash.append(String.format("%02x", b));
            }

            Random randomID = new Random();
            int numeroAleatorio = randomID.nextInt(10001);


            // Guardar en la base de datos
            pstmt = connection.prepareStatement("INSERT INTO preexamen.exercici1 (iduser, user, contrasenya, salt) VALUES (?, ?, ?, ?)");
            pstmt.setString(1, String.valueOf(numeroAleatorio));
            pstmt.setString(2, usuario);
            pstmt.setString(3, hexHash.toString());
            pstmt.setString(4, hexSalt.toString());
            pstmt.executeUpdate();


            connection.commit();

            System.out.println("User i contrasenya guardada");
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
//            if (connection != null) {
//                connection.close();
//            }
        }
    }


    //EXERCICI 1.7
    public static boolean verificarUsuarioYContrasenya(String usuario, String contrasena) throws SQLException, NoSuchAlgorithmException {
        String DB_URL = "jdbc:mysql://localhost:3306/preexamen";
        String DB_USER = "daniruben";
        String DB_PASSWORD = "P@tata123";

        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Obtener el salt de la base de datos
            pstmt = connection.prepareStatement("SELECT salt, contrasenya FROM preexamen.exercici1 WHERE user = ?");
            pstmt.setString(1, usuario);
            resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                String hexSalt = resultSet.getString("salt");
                String hexHashFromDB = resultSet.getString("contrasenya");

                // Concatenar la contraseña proporcionada con el salt almacenado en la base de datos
                String contrasenaConSalt = contrasena + hexSalt;

                // Generar el hash de la contraseña proporcionada con el salt
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hashedPassword = md.digest(contrasenaConSalt.getBytes());

                // Convertir el hash generado a hexadecimal
                StringBuilder hexHash = new StringBuilder();
                for (byte b : hashedPassword) {
                    hexHash.append(String.format("%02x", b));
                }

                // Verificar si el hash generado coincide con el hash almacenado en la base de datos
                return hexHash.toString().equals(hexHashFromDB);
            } else {
                // Usuario no encontrado en la base de datos
                return false;
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    //EXERCICI 1.8
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        // Obté una instància del generador de parells de claus
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");

        // Inicialitza el generador amb la grandària de la clau
        keyGen.initialize(2048);

        // Genera el parell de claus
        KeyPair keyPair = keyGen.generateKeyPair();

        return keyPair;
    }



    //EXERCICI 1.9
    public static String encryptPassword(String password, PrivateKey privateKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // Obté una instància del xifrat amb RSA
        Cipher cipher = Cipher.getInstance("RSA");

        // Configura el xifrat en mode d'encriptació amb la clau privada
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        // Encripta la contrasenya
        byte[] encryptedPasswordBytes = cipher.doFinal(password.getBytes());

        // Converteix els bytes encriptats a una cadena Base64
        String encryptedPassword = Base64.getEncoder().encodeToString(encryptedPasswordBytes);

        return encryptedPassword;
    }



    //EXERCICI 1.10
    public static String encryptPasswordHash(String password, PrivateKey privateKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // Calcula el hash de la contrasenya
        byte[] passwordHash = generateHash(password);

        // Obté una instància del xifrat amb RSA
        Cipher cipher = Cipher.getInstance("RSA");

        // Configura el xifrat en mode d'encriptació amb la clau privada
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        // Encripta el hash de la contrasenya
        byte[] encryptedPasswordHashBytes = cipher.doFinal(passwordHash);

        // Converteix els bytes encriptats a una cadena Base64
        String encryptedPasswordHash = Base64.getEncoder().encodeToString(encryptedPasswordHashBytes);

        return encryptedPasswordHash;
    }

    public static byte[] generateHash(String password) throws NoSuchAlgorithmException {
        // Obté una instància de l'algoritme de hash (SHA-256 en aquest cas)
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        // Calcula el hash de la contrasenya
        byte[] hash = digest.digest(password.getBytes());

        return hash;
    }


    //EXERCICI 1.11
    public static byte[] desencryptPassword(byte[] passwordEncrpted, PublicKey publicKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        // Encripta la contrasenya
        return cipher.doFinal(passwordEncrpted);
    }


    //EXERCICI 1.13
    public static void PutKeytool(){
        try {
            // Ejecuta los comandos keytool para generar la clave simétrica y guardarla en un keystore
            Process process = Runtime.getRuntime().exec("keytool -genseckey -alias llave -keyalg AES -keysize 128 -storetype JCEKS -keystore symetric.keystore");
            process.waitFor();

            // Muestra el mensaje de éxito
            System.out.println("Clave simétrica generada con éxito:");

            // Lee la salida del comando keytool para listar las claves
            Process listProcess = Runtime.getRuntime().exec("keytool -list -keystore symetric.keystore -storetype JCEKS -alias clauSimetricaPreExamen");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(listProcess.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }



    //EXERCICI 1.14
    public static void GetKeytool(){
        String keystoreFile = "symetric.keystore";
        String keystoreType = "JCEKS";
        String alias = "clauSimetricaPreExamen";
        String keystorePassword = "patata123";

        try {
            KeyStore keystore = KeyStore.getInstance(keystoreType);
            FileInputStream fis = new FileInputStream(keystoreFile);
            char[] password = keystorePassword.toCharArray();
            keystore.load(fis, password);

            // Obtener la clave secreta
            KeyStore.PasswordProtection keyPassword = new KeyStore.PasswordProtection(password);
            KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) keystore.getEntry(alias, keyPassword);
            SecretKey secretKey = entry.getSecretKey();

            // Convertir la clave a una cadena hexadecimal para mostrarla
            byte[] keyBytes = secretKey.getEncoded();
            StringBuilder sb = new StringBuilder();
            for (byte b : keyBytes) {
                sb.append(String.format("%02X", b));
            }
            System.out.println("Clave simétrica generada: " + sb.toString());

            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public static void main(String[] args) throws SQLException, NoSuchAlgorithmException {

        //He hagut de fer aixo aixi perque sino peta
        //Al terminal java -cp ./mysql-connector-j-8.1.0.jar src/Criptografia/Criptografia.java
//        guardarUsuarioYPass("Dani", "Patata123");

        if (verificarUsuarioYContrasenya("Dani", "Paltata123")) {
            System.out.println("Contraseña correcta");
        } else {
            System.out.println("Contraseña incorrecta");
        }
    }

}
