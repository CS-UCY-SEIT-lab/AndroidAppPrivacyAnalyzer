package privacyanalyzer.backend.data;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Entity;


public class ApplicationPermissionsModel implements Serializable{

	private ArrayList<String> declared = new ArrayList<String>();
	private ArrayList<String> requiredAndUsed = new ArrayList<String>();
	private ArrayList<String> requiredButNotUsed = new ArrayList<String>();
	private ArrayList<String> notRequiredButUsed = new ArrayList<String>();

	public ApplicationPermissionsModel() {

	}

	public ArrayList<String> getDeclared() {
		return declared;
	}

	public void setDeclared(ArrayList<String> declared) {
		this.declared = declared;
	}

	public ArrayList<String> getRequiredAndUsed() {
		return requiredAndUsed;
	}

	public void setRequiredAndUsed(ArrayList<String> requiredAndUsed) {
		this.requiredAndUsed = requiredAndUsed;
	}

	public ArrayList<String> getRequiredButNotUsed() {
		return requiredButNotUsed;
	}

	public void setRequiredButNotUsed(ArrayList<String> requiredButNotUsed) {
		this.requiredButNotUsed = requiredButNotUsed;
	}

	public ArrayList<String> getNotRequiredButUsed() {
		return notRequiredButUsed;
	}

	public void setNotRequiredButUsed(ArrayList<String> notRequiredButUsed) {
		this.notRequiredButUsed = notRequiredButUsed;
	}

}
