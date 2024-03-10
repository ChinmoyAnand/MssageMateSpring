package com.whatsapp.send.service;

import com.whatsapp.send.model.MessageBody;
import com.whatsapp.send.model.WhatsAppMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class SendService {

    Logger logger = LoggerFactory.getLogger(SendService.class);

    @Value("${whatsapp.version}")
    private String version;

    @Value("${whatsapp.phoneID}")
    private String phoneId;

    @Value("${whatsapp.base.url}")
    private String baseUrl;

    @Value("${whatsapp.token}")
    private String token;

    public String messageService(MessageBody body){
        logger.info("Inside service layer");
        for(String senderNumber:body.sendingNumbers()) {

            WhatsAppMessage message = new WhatsAppMessage(
                    "whatsapp",
                    senderNumber,
                    "template",
                    new WhatsAppMessage.Template(body.messageTemplate(), new WhatsAppMessage.Template.Language("en_US"))
            );
            try {
                ResponseEntity<String> response = callExternalAPI(message);
                return "SUCCESS";
            }catch (HttpClientErrorException.NotFound e) {
                logger.error("404 Not Found: " + e.getMessage());
                return e.getMessage();
            } catch (HttpClientErrorException e) {
                logger.error("HTTP Client Error: " + e.getStatusCode() + " " + e.getStatusText());
                return e.getStatusCode() + " " + e.getStatusText();
            } catch (Exception e) {
                logger.error("Error: " + e.getMessage());
                return e.getMessage();
            }
        }
        return "SUCCESS";
    }

    private ResponseEntity<String> callExternalAPI(WhatsAppMessage message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<WhatsAppMessage> requestEntity = new HttpEntity<>(message, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl+version+"/"+phoneId+"/messages",
                HttpMethod.POST,
                requestEntity,
                String.class);

        logger.info("Response from external API: " + response);
        return response;
    }

}
