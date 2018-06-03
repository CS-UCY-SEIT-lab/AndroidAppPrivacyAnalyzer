package privacyanalyzer.ui.view.admin.product;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.CssLayoutElement;
import com.vaadin.testbench.elements.GridElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.testbench.elements.VerticalLayoutElement;
import com.vaadin.testbench.elementsbase.ServerClass;

@ServerClass("privacyanalyzer.ui.view.admin.product.ProductAdminViewDesign")
@AutoGenerated
public class ProductAdminViewDesignElement extends VerticalLayoutElement {

	public TextFieldElement getSearch() {
		return $(com.vaadin.testbench.elements.TextFieldElement.class).id("search");
	}

	public ButtonElement getAdd() {
		return $(com.vaadin.testbench.elements.ButtonElement.class).id("add");
	}

	public CssLayoutElement getListParent() {
		return $(com.vaadin.testbench.elements.CssLayoutElement.class).id("listParent");
	}

	public GridElement getList() {
		return $(com.vaadin.testbench.elements.GridElement.class).id("list");
	}

	public VerticalLayoutElement getForm() {
		return $(com.vaadin.testbench.elements.VerticalLayoutElement.class).id("form");
	}

	public TextFieldElement getName() {
		return $(com.vaadin.testbench.elements.TextFieldElement.class).id("name");
	}

	public TextFieldElement getPrice() {
		return $(com.vaadin.testbench.elements.TextFieldElement.class).id("price");
	}

	public ButtonElement getUpdate() {
		return $(com.vaadin.testbench.elements.ButtonElement.class).id("update");
	}

	public ButtonElement getCancel() {
		return $(com.vaadin.testbench.elements.ButtonElement.class).id("cancel");
	}

	public ButtonElement getDelete() {
		return $(com.vaadin.testbench.elements.ButtonElement.class).id("delete");
	}
}