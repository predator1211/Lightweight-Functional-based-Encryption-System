import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class ImageEncryption {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            // Get the image file path from the user
            System.out.print("Enter the path to the image file: ");
            String imagePath = scanner.nextLine();

            // Read the image
            BufferedImage image = ImageIO.read(new File(imagePath));

            // Get the key from the user or generate it programmatically
            String key = generateKey(); // Function to generate the key

            // Print the generated key
            System.out.println("Generated Key: " + key);

            // Store the generated key in a text file
            storeKey(key);

            // Encrypt the red channel of the image
            BufferedImage encryptedImage = encryptImage(image, key);

            // Save the encrypted image
            saveImage(encryptedImage, imagePath);

            System.out.println("Image encrypted and saved successfully.");

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Function to generate the key
    private static String generateKey() {
        // Implement your key generation logic here
        // For demonstration purposes, returning a simple key
        return "ExampleKey123";
    }

    // Encrypt the image using the provided function
    private static BufferedImage encryptImage(BufferedImage image, String key) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage encryptedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        try {
            // Encryption process using the provided function
            // Replace this with the appropriate encryption logic based on the function given
            // For illustration, performing a simple manipulation
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x, y);

                    // Perform your encryption operation here
                    // For example, XOR operation
                    int encryptedRGB = rgb ^ key.hashCode();

                    // Set the pixel in the encrypted image
                    encryptedImage.setRGB(x, y, encryptedRGB);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encryptedImage;
    }

    // Save the image
    private static void saveImage(BufferedImage image, String originalImagePath) throws IOException {
        String encryptedImagePath = originalImagePath.substring(0, originalImagePath.lastIndexOf('.'))
                + "_encrypted.png"; // Change extension or file name as needed

        // Write the encrypted image to a file
        ImageIO.write(image, "png", new File(encryptedImagePath));
    }

    // Store the key in a text file
    private static void storeKey(String key) {
        try (FileWriter writer = new FileWriter("keys.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(writer);
             PrintWriter out = new PrintWriter(bufferedWriter)) {
            out.println(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
