package models;

import play.db.jpa.Model;

/**
 * @author <a href="mailto:alexander.hanschke@gmail.com">Alexander Hanschke</a>
 */
public class Authentication extends Model {

    public String provider;
    public String token;

}
