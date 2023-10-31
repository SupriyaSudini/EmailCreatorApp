
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmailLabAppTest {

    static final Logger logger = LogManager.getLogger(EmailLabAppTest.class);

    @BeforeAll
    public static void setup() {
        // Any one-time setup or configuration can go here
    }

    @Test
    void testIsFileReadable() {
        String inputFile = "src/main/resources/input.txt";
        String nonexistentFile = "src/main/resources/nonexistent.txt";
        String unreadableFile = "src/main/resources/unreadable.txt";

        // Test readable file
        assertTrue(EmailLabApp.isFileReadable(inputFile), "Input file should be readable");

        // Test nonexistent file
        assertFalse(EmailLabApp.isFileReadable(nonexistentFile), "Nonexistent file should be unreadable");

        // Test unreadable file
        assertFalse(EmailLabApp.isFileReadable(unreadableFile), "Unreadable file should be unreadable");
    }


    @Test
    public void testSendEmailValidInput() {
        // Arrange: Prepare valid input data
        String toEmail = "test@example.com";
        String fromEmail = "noreply@deals.com";
        String subject = "Test Subject";
        String emailContent = "This is a test email.";

        // Act: Call the method you want to test
        boolean result = true; // Assume success by default

        try {
            EmailLabApp.sendEmail(toEmail, fromEmail, subject, emailContent);
        } catch (Exception e) {
            // If an exception occurs during email sending, mark the result as failure
            result = false;
        }
        // Assert: Verify the result
        assertTrue(result, "Email should be sent successfully");
    
    }  
    
    @Test
    public void testSendEmailEmptyToAddress() {
        // Arrange: Prepare invalid input data
        String toEmail = ""; // Empty "To" address
        String fromEmail = "noreply@deals.com";
        String subject = "Test Subject";
        String emailContent = "This is a test email.";

        // Act: Call the method you want to test
        boolean result = false; // Assume failure by default

        try {
            EmailLabApp.sendEmail(toEmail, fromEmail, subject, emailContent);
        } catch (Exception e) {
            // If an exception occurs during email sending, mark the result as success
            result = true;
        }

        // Assert: Verify the result
        assertTrue(result, "Email should fail due to an empty 'To' address");
    }

}

