import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;


public class EmailLabApp {
    public static void main(String[] args) {
    	//logger 
    	final Logger logger = LogManager.getLogger(EmailLabApp.class);	
    
        String inputFile = "src/main/resources/input.txt";
        String templateFile = "src/main/resources/email_template.txt";
       
        //display name of Application
         System.out.println("Email Creator");
         
     // Check if the input file exists and is readable
        if (!isFileReadable(inputFile)) {
            System.err.println("Error: Input file '" + inputFile + "' does not exist or is not readable.");
            logger.error("Reading input file is having an error");
            return;
        }

        // Check if the template file exists and is readable
        if (!isFileReadable(templateFile)|| getFileLength(templateFile) == 0) {
            System.err.println("Error: Template file '" + templateFile + "' does not exist or is not readable.");
            logger.error("Reading emailTemplate file is having an error");
            return;
        }
        
     try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            
            logger.info("Reading input file: " + inputFile);
            
            while ((line = br.readLine()) != null) {
            	
            	logger.debug("Splitting line: " + line);
            	
                // Split the line into parts using a comma as the delimiter
                String[] parts = line.split(",");
                if (parts.length == 3) {
                	
//                    String firstName = parts[0].trim();
//                    firstName= toTitleCase(firstName); 
                	
                    String firstName = parts[0].trim().toLowerCase(); // Convert to lowercase
                    firstName = Character.toUpperCase(firstName.charAt(0)) + firstName.substring(1); // Convert to title case

                    String lastName = parts[1].trim();
                    String email = parts[2].trim().toLowerCase();
                    

                    // Read the email template
                    String template = readEmailTemplate(templateFile, logger);
//                    String template = readEmailTemplate(templateFile);
                    
                 // Log template reading
                     logger.info("Reading email template from file: " + templateFile);

                    
                 
                    // Replace placeholders with actual data
                    String emailContent = template.replace("{first_name}", firstName)
                                                 .replace("{last_name}", lastName)
                                                 .replace("{email}", email);
                    
                 // Log an informational message
                   logger.info("Sending email to: " + email);

                    // Send the email
                    sendEmail(email, "noreply@deals.com", "Deals!", emailContent);
                    
                   
                    
               
//                    System.out.println("Email sent to " + email);
                    System.out.println("===========================================");
                    System.out.println(emailContent);
                    logger.info("Sent email to: " + email + "Successfully");
                    
                }else {
                    logger.error("Error: Input line does not have the expected format (firstName,lastName,email): " + line);
                    System.err.println("Error: Input line does not have the expected format (firstName,lastName,email): " + line);
                }
            }
        } catch (IOException e) {
        	// Log an error message with the exception
            logger.error("An error occurred while processing emails", e);
            e.printStackTrace();
        }
     logger.info("Email processing completed."); // Log the end of email processing
    
    }
    
      // method for reading templates 
    
    public static String readEmailTemplate(String templateFile, Logger logger) {
//  public static String readEmailTemplate(String templateFile) {
   
        // Code to read the email template from the file and return it as a string
        // You can implement this method based on your file reading preferences
        // For simplicity, you can use FileReader and BufferedReader as shown above.
        // Make sure to handle exceptions and errors when reading the template.
        // Here's a basic example:

        StringBuilder templateBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(templateFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                templateBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
        	logger.error("An error occurred while reading the email template", e);
            e.printStackTrace();
        }
        return templateBuilder.toString();
    }

    // method for sending emails
    
    public static void sendEmail(String toEmail, String fromEmail, String subject, String emailContent) {
        // Code to send an email using JavaMail API
        
    	 // Check if the "To" address is empty and raise an exception if it is
        if (toEmail == null || toEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("To address cannot be empty.");
        }
        
        final String smtpHost = "smtp.gmail.com";
        final String smtpPort = "587";
        final String smtpUsername = "supriyasudini165@gmail.com";
        final String smtpPassword = "skaj pbdm zojk vbni";
        
        final Logger logger = LogManager.getLogger(EmailLabApp.class);
       
  
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
               protected PasswordAuthentication getPasswordAuthentication() {
//            	 Log SMTP server information acceptance
                logger.info("SMTP Host: " + smtpHost);
                logger.info("SMTP Port: " + smtpPort);
                logger.info("SMTP Username: " + smtpUsername);
                   return new PasswordAuthentication(smtpUsername, smtpPassword);
            }
        });

        try {
        	
        	// if to address is empty then 
        	// Check if the "To" address is empty before sending the email
            if (toEmail == null || toEmail.trim().isEmpty()) {
                throw new IllegalArgumentException("To address cannot be empty.");
            }
        	
        	
            Message emailMessage = new MimeMessage(session);
            emailMessage.setFrom(new InternetAddress(fromEmail));
            emailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            emailMessage.setSubject(subject);
            emailMessage.setContent(emailContent, "text/plain");
            Transport.send(emailMessage);
        } catch (MessagingException e) {
    	logger.error("An error occurred while sending an email", e);
            e.printStackTrace();
        }
    }
    
    // method whther the file is readable or not
        
        protected static boolean isFileReadable(String filePath) {
            File file = new File(filePath);

            // Check if the file exists
            if (!file.exists()) {
                return false;
            }
            
         // Check if the file is readable
            if (!file.canRead()) {
                return false;
            }

            // Check if the file is readable
            try {
                FileReader fileReader = new FileReader(file);
                fileReader.close(); // Close the file if it was successfully opened
                
                if (file.length() == 0) {
                    return false;
                }
                
                
                return true;
            } catch (IOException e) {
                return false;
            }
        }
        
        protected static long getFileLength(String filePath) {
            File file = new File(filePath);
            return file.length();
        }

 }

