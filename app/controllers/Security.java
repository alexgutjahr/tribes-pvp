package controllers;

import models.Role;
import models.User;
import notifiers.Emails;
import play.Logger;
import play.mvc.Controller;

import java.util.Date;

/**
 * @author <a href="mailto:alexander.hanschke@gmail.com">Alexander Hanschke</a>
 */
public class Security extends Controller {

    public static void login(User user) {

        Logger.info(
            "login - name: %s, password: %s, ip: %s",
            user.name, user.password, request.remoteAddress
        );

        validation.required("name", user.name);
        validation.required("password", user.password);

        if (validation.hasErrors()) {
            validation.keep();
            Game.login();
        }

        if (User.authenticate(user.name, user.password)) {
            User player = User.find("byName", user.name).first();

            if (player.suspended) {
                flash.error("Account has been suspended.");
                flash.keep();
                Game.login();
            }

            player.lastlogin = new Date();
            player.save();
            session.put("player", player.name);
            Game.home();
        }

        flash.error("Credentials are wrong.");
        flash.keep();

        Game.login();
    }

    public static void signup(String name, String email, String password, String confirmation) {

        Logger.info(
            "signup - name: %s, email: %s, password: %s, confirmation: %s, ip: %s",
            name, email, password, confirmation, request.remoteAddress
        );

        validation.required("name", name);
        validation.required("email", email);
        validation.email("email", email);
        validation.required("password", password);
        validation.required("confirmation", confirmation);

        if (validation.hasErrors()) {
            validation.keep();
            params.flash("name", "email");
            Game.signup();
        }

        if (User.nameExists(name)) {
            validation.addError("name", "User name already taken!");
            validation.keep();
            params.flash("email");
            Game.signup();
        }

        if (User.mailExists(email)) {
            validation.addError("email", "Mail address already in use!");
            validation.keep();
            params.flash("name");
            Game.signup();
        }

        if (!password.equals(confirmation)) {
            validation.addError("confirmation", "Passwords do not match!");
            validation.keep();
            params.flash("name", "email");
            Game.signup();
        }

        User player = User.make(name, email, password);
        player.save();
        Emails.welcome(player);

        session.put("player", player.name);

        Game.home();
    }

    public static void logout() {
        session.clear();
        Game.index();
    }

    public static boolean isConnected() {
        return
            session.contains("player") && Game.player() != null;
    }

    public static boolean isAdmin() {
        return isConnected() && Game.player().role == Role.ADMIN;
    }

}
