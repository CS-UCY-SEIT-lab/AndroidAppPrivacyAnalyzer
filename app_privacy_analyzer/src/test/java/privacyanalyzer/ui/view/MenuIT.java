package privacyanalyzer.ui.view;

import org.junit.Assert;
import org.junit.Test;

import privacyanalyzer.AbstractIT;

public class MenuIT extends AbstractIT {

	@Test
	public void adminSeesAdminMenus() {
		loginAsAdmin();
		MenuElement menu = $(MenuElement.class).first();
		Assert.assertNotNull(menu.getMenuLink("Users"));
		Assert.assertNotNull(menu.getMenuLink("Products"));
	}

	@Test
	public void baristaDoesNotSeeAdminMenus() {
		//loginAsBarista();
		MenuElement menu = $(MenuElement.class).first();
		Assert.assertNull(menu.getMenuLink("Users"));
		Assert.assertNull(menu.getMenuLink("Products"));
	}
}
