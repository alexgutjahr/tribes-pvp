package notifiers;

import models.User;
import play.mvc.Mailer;

/**
 * @author <a href="mailto:alexander.hanschke@gmail.com">Alexander Hanschke</a>
 */
public class Emails extends Mailer {

    public static void welcome(User user) {
        setSubject("Welcome to Tribes PVP!");
        addRecipient(user.email);
        setFrom("tribemaster@tribes.com");
        send(user);
    }
}
