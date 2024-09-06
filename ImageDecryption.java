import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class ImageDecryption {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            // Get the path to the encrypted image from the user
            System.out.print("Enter the path to the encrypted image: ");
            String encryptedImagePath = scanner.nextLine();

            // Provide the generated key from Encryption code
            String generatedKey = "2B7E151628AED2A6ABF7158809CF4F3C"; // Replace with the generated key from Encryption code
            byte[] encodedKey = hexStringToByteArray(generatedKey);
            SecretKey secretKey = new SecretKeySpec(encodedKey, "AES");

            // Read the encrypted image
            BufferedImage encryptedImage = ImageIO.read(new File(encryptedImagePath));

            // Create a decryption cipher
            Cipher decryptionCipher = Cipher.getInstance("AES");
            decryptionCipher.init(Cipher.DECRYPT_MODE, secretKey);

            // Decrypt the red channel of the image
            BufferedImage decryptedImage = decryptRedChannel(encryptedImage, decryptionCipher);

            // Save the decrypted image
            saveImage(decryptedImage, encryptedImagePath);

            System.out.println("Image red channel decrypted and saved successfully.");

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Decrypt the red channel of the image
    private static BufferedImage decryptRedChannel(BufferedImage image, Cipher decryptionCipher) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage decryptedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        try {
            // Process each pixel of the image
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int encryptedRGB = image.getRGB(x, y);

                    // Extract encrypted red channel
                    int encryptedRed = (encryptedRGB >> 16) & 0xFF;

                    // Decrypt the red channel
                    byte[] decryptedRed = decryptionCipher.doFinal(new byte[]{(byte) encryptedRed});

                    // Combine decrypted red channel with intact green and blue channels
                    int decryptedRGB = (0xFF << 24) | (decryptedRed[0] & 0xFF) << 16 | (encryptedRGB & 0x00FFFF);

                    // Set the pixel in the decrypted image
                    decryptedImage.setRGB(x, y, decryptedRGB);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return decryptedImage;
    }

    // Save the image
    private static void saveImage(BufferedImage image, String encryptedImagePath) throws IOException {
        String decryptedImagePath = encryptedImagePath.substring(0, encryptedImagePath.lastIndexOf('_'))
                + "_decrypted.png"; // Change the file name as needed

        // Write the decrypted image to a file
        ImageIO.write(image, "png", new File(decryptedImagePath));
    }

    // Convert hexadecimal string to byte array
    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
