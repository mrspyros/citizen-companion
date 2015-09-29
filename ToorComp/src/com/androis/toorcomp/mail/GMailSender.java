package com.androis.toorcomp.mail;

/**
 * @author MrSpyros
 *
 *  This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   any later version.

 *  This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

import javax.activation.DataHandler;   
import javax.activation.DataSource;   
import javax.activation.FileDataSource;
import javax.mail.Message;   
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;   
import javax.mail.Session;   
import javax.mail.Transport;   
import javax.mail.internet.InternetAddress;   
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;   
import javax.mail.internet.MimeMultipart;

import com.androis.toorcomp.mail.JSSEProvider;

import android.util.Log;

import java.io.ByteArrayInputStream;   
import java.io.File;
import java.io.IOException;   
import java.io.InputStream;   
import java.io.OutputStream;   
import java.security.Security;   
import java.util.Properties;   

public class GMailSender extends javax.mail.Authenticator {   
    private String mailhost = "smtp.gmail.com";   
    private String user;   
    private String password;   
    private Session session;   

    static {   
        Security.addProvider(new JSSEProvider());   
    }  

    public GMailSender(String user, String password) {   
        this.user = user;   
        this.password = password;   

        Properties props = new Properties();   
        props.setProperty("com.androis.toorcomp.mail.transport.protocol", "smtp");   
        props.setProperty("com.androis.toorcomp.mail.host", mailhost);   
        props.put("com.androis.toorcomp.mail.smtp.auth", "true");   
        props.put("com.androis.toorcomp.mail.smtp.port", "465");   
        props.put("com.androis.toorcomp.mail.smtp.socketFactory.port", "465");   
        props.put("com.androis.toorcomp.mail.smtp.socketFactory.class",   
                "javax.net.ssl.SSLSocketFactory");   
        props.put("com.androis.toorcomp.mail.smtp.socketFactory.fallback", "false");   
        props.setProperty("com.androis.toorcomp.mail.smtp.quitwait", "false");   

        session = Session.getDefaultInstance(props, this);   
    }   

    protected PasswordAuthentication getPasswordAuthentication() {   
        return new PasswordAuthentication(user, password);   
    }   



    public synchronized void sendMail(String subject, String body,  File attachment, String sender, String recipients ) throws Exception {   
        try{
        	int FLAG=1;
             MimeMessage message = new MimeMessage(session);
             message.setSender(new InternetAddress(sender));
             message.setSubject(subject);

             MimeBodyPart mbp1 = new MimeBodyPart();
             MimeBodyPart mbp2 = new MimeBodyPart();
             mbp1.setText(body);
             
             // ------ if no photo selected ------------
             try{
            
            	 if (CheckFileExists(attachment.toString())){
            	      FileDataSource fds = new FileDataSource(attachment); //set attachment to filedatasource
                      mbp2.setDataHandler(new DataHandler(fds)); //add the filedatasource object to your 2nd mimebodypart
                      mbp2.setFileName(fds.getName());
            	 }else {
            		 FLAG=0;
            	 }
             
             
             }catch(Exception e){
                 FLAG=0;
                 Log.e("FileDataSource", e.getMessage(), e);
             }
                 
                 
             Multipart mp = new MimeMultipart();
             mp.addBodyPart(mbp1); 
             if (FLAG==1) mp.addBodyPart(mbp2);

             message.setContent(mp);
        if (recipients.indexOf(',') > 0)   
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));   
        else  
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));   
        Transport.send(message);   
        }catch(Exception e){
        	Log.e("GmailsenderGlobal", e.getMessage(), e);
        }
   
       
    
    
    
    }
    
    
private Boolean CheckFileExists(String path){
		
		File file = new File(path);
		return file.exists();
	}
    
    
    public class ByteArrayDataSource implements DataSource {   
        private byte[] data;   
        private String type;   

        public ByteArrayDataSource(byte[] data, String type) {   
            super();   
            this.data = data;   
            this.type = type;   
        }   

        public ByteArrayDataSource(byte[] data) {   
            super();   
            this.data = data;   
        }   

        public void setType(String type) {   
            this.type = type;   
        }   

        public String getContentType() {   
            if (type == null)   
                return "application/octet-stream";   
            else  
                return type;   
        }   

        public InputStream getInputStream() throws IOException {   
            return new ByteArrayInputStream(data);   
        }   

        public String getName() {   
            return "ByteArrayDataSource";   
        }   

        public OutputStream getOutputStream() throws IOException {   
            throw new IOException("Not Supported");   
        }   
    }   
}  
