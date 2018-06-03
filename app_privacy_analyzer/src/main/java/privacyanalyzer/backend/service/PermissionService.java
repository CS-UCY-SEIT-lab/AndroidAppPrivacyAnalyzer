package privacyanalyzer.backend.service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.components.grid.HeaderRow;

import privacyanalyzer.backend.data.entity.ApkModel;
import privacyanalyzer.backend.data.entity.ApkPermissionAssociation;
import privacyanalyzer.backend.data.entity.Permission;
import privacyanalyzer.backend.data.entity.PermissionMethodCallModel;
import privacyanalyzer.ui.util.Paths;
import privacyanalyzer.backend.ApkPermissionAssociationRepository;
import privacyanalyzer.backend.ApkRepository;
import privacyanalyzer.backend.PermissionRepository;

@Service
public class PermissionService implements Serializable{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	private final PermissionRepository permissionRepository;
	private final ApkPermissionAssociationRepository apkPermissionAssociationRepository;
	@Autowired
	public PermissionService(PermissionRepository permissionRepository,ApkPermissionAssociationRepository apkPermissionAssociationRepository) {
		this.permissionRepository=permissionRepository;
		this.apkPermissionAssociationRepository=apkPermissionAssociationRepository;
	}
/*
	public List<Permission> getPermissions(List<String> permNames) {
		List<Permission> retList = new ArrayList<Permission>();

		for (String p : permNames) {
			try {
				retList.add((Permission) jdbcTemplate.query(
						"SELECT p.permissionName, p.permissionDesc,p.protectionLevel,p.permissionValue,pl.description FROM permissions p,protectionlevels pl WHERE p.protectionLevel=pl.name and p.permissionValue='"
								+ p + "'",
						(rs, rowNum) -> new Permission( rs.getString("permissionName"),
								rs.getString("permissionDesc"), rs.getString("protectionLevel"),
								rs.getString("permissionValue")))
						.toArray()[0]);
			} catch (ArrayIndexOutOfBoundsException e) {
				retList.add(new Permission(p));
			}
		}

		return retList;
	}*/

	public ApkPermissionAssociationRepository getApkPermissionAssociationRepository() {
		return apkPermissionAssociationRepository;
	}

	public void setGrid(List<String> permNames, Grid<Permission> grid) {
		List<Permission> myList=new ArrayList<Permission>();
		for (String s:permNames) {
			Permission p=permissionRepository.findByPermissionValue(s);
			//if (p==null) continue;
			myList.add(p);
		}
		grid.removeAllColumns();
		grid.setSelectionMode(Grid.SelectionMode.NONE);
		grid.addColumn(Permission::getPermissionName).setCaption("Permission Name")
				.setDescriptionGenerator(Permission::getPermissionDesc);
		grid.addColumn(Permission::getPermissionValue).setCaption("Permission Value")
				.setDescriptionGenerator(Permission::getPermissionDesc);
		grid.addColumn(Permission::getProtectionlvlName).setCaption("Protection Level")
				.setDescriptionGenerator(Permission::getProtectionlvlDesc);
		grid.setItems(myList);
		grid.setWidth("100%");
		
		if (permNames.size() == 0) {
			grid.setHeightByRows(1);
		}else if (permNames.size() >= 10) {
			grid.setHeightByRows(10);
		} else {
			grid.setHeightByRows(permNames.size());
		}
		grid.setVisible(true);
	}
	
	public void setGridbyPermissions(List<Permission> permissionlist, Grid<Permission> grid) {
		
		grid.removeAllColumns();
		grid.setSelectionMode(Grid.SelectionMode.NONE);
		
		if (permissionlist.size()==0) {
			grid.addColumn(Permission::getPermissionName).setCaption("No permissions found");
			grid.setHeightByRows(1);
			return;
		}
		
		Column<Permission, String> permNameColumn =grid.addColumn(Permission::getPermissionName).setCaption("Permission Name")
				.setDescriptionGenerator(Permission::getPermissionDesc);
		Column<Permission, String> permValueColumn = grid.addColumn(Permission::getPermissionValue).setCaption("Permission Value")
				.setDescriptionGenerator(Permission::getPermissionDesc);
		Column<Permission, String> permProtectionLvlNameColumn =grid.addColumn(Permission::getProtectionlvlName).setCaption("Protection Level")
				.setDescriptionGenerator(Permission::getProtectionlvlDesc);
		grid.setItems(permissionlist);
		//grid.setWidth("100%");
		
		TextField permNameFilter=new TextField();
		permNameFilter.setSizeFull();
		permNameFilter.setHeight("80%");
		permNameFilter.setPlaceholder("Search by name...");
		permNameFilter.addValueChangeListener(new ValueChangeListener<String>() {
			
			@Override
			public void valueChange(ValueChangeEvent<String> event) {
		        ListDataProvider<Permission> dataProvider = (ListDataProvider<Permission>) grid.getDataProvider();
		        dataProvider.setFilter(Permission::getPermissionName, s -> caseInsensitiveContains(s, event.getValue()));
				
			}
			
		    private Boolean caseInsensitiveContains(String where, String what) {
		        return where.toLowerCase().contains(what.toLowerCase());
		    }
		});
        HeaderRow filterRow = grid.appendHeaderRow();
       	filterRow.getCell(permNameColumn).setComponent(permNameFilter);
		
		if (permissionlist.size() == 0) {
			grid.setHeightByRows(1);
		}else if (permissionlist.size() >= 10) {
			grid.setHeightByRows(10);
		} else {
			grid.setHeightByRows(permissionlist.size());
		}
		grid.setVisible(true);
	}
	
	public void saveApkPermissions(List<String> plist,ApkModel apk,String type) {
		
		for (String pName:plist) {
			Permission p=permissionRepository.findByPermissionValue(pName);
			if (p==null) {
				p=new Permission(pName);
				//System.out.println(pName);
				permissionRepository.save(p);
				//System.out.println("Saved");
			}
			
			ApkPermissionAssociation assoc=new ApkPermissionAssociation(apk,p,type);
			apkPermissionAssociationRepository.save(assoc);
		}
		
		
	}
	
	
	public void printjsonPermissions() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		Gson g = new Gson();
		ArrayList<Permission> mylist = g.fromJson(new JsonReader(new FileReader(Paths.resourcepath+"permissions.json")),
				new TypeToken<ArrayList<Permission>>() {
				}.getType());
		System.out.println("LIST SIZE=: "+mylist.size());
		for (Permission p:mylist) {
			
		}
	}

	public PermissionRepository getPermissionRepository() {
		return permissionRepository;
	}

}
