/*
 * The MIT License
 *
 * Copyright 2020 ss.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ss.martin.platform.service.impl;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.resource.Emailv31;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ss.martin.platform.service.EmailService;
import ss.martin.platform.spring.config.PlatformConfiguration;
import ss.martin.platform.wrapper.EmailRequest;

/**
 * Mailjet email service implementation.
 * @author ss
 */
@Service
class MailjetSender implements EmailService {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(MailjetSender.class);
    /** Platform configuration. */
    @Autowired
    private PlatformConfiguration config;
    @Override
    public void sendEmail(EmailRequest emailRequest) throws Exception {
        LOG.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~ mailjet: send email ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        LOG.debug(emailRequest.toString());
        MailjetClient client = new MailjetClient(
            config.getMailjetApiKey(), config.getMailjetSecretKey(), new ClientOptions("v3.1"));
        JSONArray recipients = new JSONArray();
        for (EmailRequest.EmailContact recipient : emailRequest.getRecipients()) {
            recipients.put(new JSONObject().put("Email", recipient.getEmail()).put("Name", recipient.getName()));
        }
        JSONArray arrAttachments = new JSONArray();
        if (emailRequest.getAttachments() != null) {
            for (EmailRequest.EmailAttachment attachment : emailRequest.getAttachments()) {
                String base64 = Base64.getEncoder().encodeToString(
                           Files.readAllBytes(Paths.get(attachment.getFile().toURI())));
                JSONObject obj = new JSONObject()
                        .put("ContentType", attachment.getContentType())
                        .put("Filename", attachment.getName())
                        .put("Base64Content", base64);
                arrAttachments.put(obj);
            }
        }
        JSONArray props = new JSONArray()
            .put(new JSONObject().put(Emailv31.Message.FROM, new JSONObject()
            .put("Email", emailRequest.getSender().getEmail())
            .put("Name", emailRequest.getSender().getName()))
            .put(Emailv31.Message.TO, recipients)
            .put(Emailv31.Message.SUBJECT, emailRequest.getSubject())
            .put(Emailv31.Message.TEXTPART, emailRequest.getMessage() == null ? "" : emailRequest.getMessage())
            .put(Emailv31.Message.HTMLPART, "").put(Emailv31.Message.ATTACHMENTS, arrAttachments));
        MailjetRequest email = new MailjetRequest(Emailv31.resource).property(Emailv31.MESSAGES, props);
        MailjetResponse response = client.post(email);
        LOG.debug("response status: " + response.getStatus());
        LOG.debug("response data: " + response.getData());
        LOG.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
}
