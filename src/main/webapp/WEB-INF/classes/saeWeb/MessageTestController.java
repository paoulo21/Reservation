package saeWeb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
public class MessageTestController {

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/testMessage")
    public String testMessage() {
        Locale locale = Locale.FRANCE; // Testez avec d'autres locales si nécessaire
        return messageSource.getMessage("title.calendrier", null, locale);
    }
}

