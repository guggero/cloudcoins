package ch.cloudcoins.contact.boundary;

import ch.cloudcoins.MessageKey;
import ch.cloudcoins.contact.entity.ContactMessage;
import ch.cloudcoins.security.boundary.PermitAll;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;

import static ch.cloudcoins.EntityResource.businessError;
import static ch.cloudcoins.EntityResource.ok;
import static ch.cloudcoins.NetUtil.sendPOST;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Stateless
@Path("/contact")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class ContactMessageResource {

    private static final String RECAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify";
    private static final String RECAPTCHA_SECRET = "6Lf-tzEUAAAAAMOptS2n8hXQ8NVpoUMPFWP5xxNA";

    @PersistenceContext
    private EntityManager entityManager;

    @POST
    @PermitAll
    public Response saveMessage(@QueryParam("captcha") String captcha, ContactMessage message) throws Exception {
        String captchaResponse = sendPOST(RECAPTCHA_URL, new HashMap<String, String>() {{
            put("secret", RECAPTCHA_SECRET);
            put("response", captcha);
        }});
        if (captchaResponse.contains("\"success\": true")) {
            entityManager.persist(message);
            return ok();
        } else {
            return businessError(MessageKey.ERROR_INVALID_INPUT);
        }
    }
}
