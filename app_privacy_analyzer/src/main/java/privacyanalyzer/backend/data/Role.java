package privacyanalyzer.backend.data;

public class Role {
	public static final String BARISTA = "barista";
	public static final String BAKER = "baker";
	public static final String ADMIN = "admin";
	public static final String GUEST = "guest";
	public static final String USER="user";

	private Role() {
		// Static methods and fields only
	}

	public static String[] getAllRoles() {
		return new String[] { BARISTA, BAKER, ADMIN,GUEST,USER };
	}

}
