package controllers;

import models.User;
import play.Logger;
import play.Play;
import play.libs.OAuth2;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Router;
import play.mvc.With;

/**
 * @author <a href="mailto:alexander.hanschke@gmail.com">Alexander Hanschke</a>
 */
public class Facebook extends Controller {

    @Before
    static void before() {
        if (!Security.isConnected()) {
            Game.index();
        }

        if (Game.player().logout) {
            Game.player().logout = Boolean.FALSE;
            Game.player().save();

            Security.logout();
        }
    }

    public static OAuth2 FACEBOOK = new OAuth2(
        "https://graph.facebook.com/oauth/authorize",
        "https://graph.facebook.com/oauth/access_token",
            Play.configuration.getProperty("facebook.app.api.key"),
            Play.configuration.getProperty("facebook.app.secret")
    );

    public static void connect() {
        if (OAuth2.isCodeResponse()) {
            OAuth2.Response response = FACEBOOK.retrieveAccessToken(authURL());
            User player = Game.player();
            player.token = response.accessToken;
            player.save();

            Game.profile();
        }

        FACEBOOK.retrieveVerificationCode(authURL());
    }

    static String authURL() {
        return Router.getFullUrl("Facebook.connect");
    }
}
