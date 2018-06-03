package privacyanalyzer.backend.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.vaadin.ui.Grid;

import privacyanalyzer.backend.ApkTrackerAssociationRepository;
import privacyanalyzer.backend.TrackerRepository;
import privacyanalyzer.backend.data.LibraryModel;
import privacyanalyzer.backend.data.entity.ApkModel;
import privacyanalyzer.backend.data.entity.ApkTrackerAssociation;
import privacyanalyzer.backend.data.entity.Permission;
import privacyanalyzer.backend.data.entity.PermissionMethodCallModel;
import privacyanalyzer.backend.data.entity.Tracker;

@Service
public class TrackerService implements Serializable {

	

	
	private List<Tracker> trackerList;
	
	private final TrackerRepository trackerRepository;
	private final ApkTrackerAssociationRepository apkTrackerAssociationRepository;
	
	@Autowired
	public TrackerService(TrackerRepository trackerRepository,ApkTrackerAssociationRepository apkTrackerAssociationRepository) {
		this.trackerRepository=trackerRepository;
		this.apkTrackerAssociationRepository=apkTrackerAssociationRepository;
		getAllTrackers();
	}
	


	
	private void getAllTrackers() {
		trackerList=trackerRepository.findAll();

	}

	public List<Tracker> getTrackerList() {
		return trackerList;
	}

	public void setTrackerList(ArrayList<Tracker> trackerList) {
		this.trackerList = trackerList;
	}

	
	public Tracker exists(String name) {
		
		for(Tracker t:getTrackerList()) {
			
			if (t.getName().equalsIgnoreCase(name)) {
				return t;
			}
			
		}
		
		
		return null;
	}
	
	
	public void saveTrackers(LibraryModel[] libModels,ApkModel apkmodel) {
		for (int i = 0; i < libModels.length; i++) {
			/*System.out.println(libModels[i].getLibrary());*/
			
			Tracker t=exists(libModels[i].getLibrary());
			if (t!=null) {
				apkTrackerAssociationRepository.save(new ApkTrackerAssociation(apkmodel,t));
			}
			
		}
	}
	
	
	
	public void setGrid(ApkModel apkmodel, Grid<Tracker> grid) {
		List<Tracker> mylist=apkTrackerAssociationRepository.findAllTrackersByApkModel(apkmodel); //new ArrayList<Tracker>();
		
		/*
		for (int i = 0; i < libModels.length; i++) {
			//System.out.println(libModels[i].getLibrary());
			
			Tracker t=exists(libModels[i].getLibrary());
			if (t!=null) {
				mylist.add(t);
			}
			
		}*/
		if (mylist.size()==0) {
			grid.addColumn(Tracker::getName).setCaption("No trackers found");
			grid.setHeightByRows(1);
			return;
		}
		grid.removeAllColumns();
		grid.setSelectionMode(Grid.SelectionMode.NONE);
		grid.addColumn(Tracker::getName).setCaption("Name");
		grid.addColumn(Tracker::getWebsite).setCaption("Website");
		grid.setItems(mylist);
		//grid.setWidth("100%");

		if (mylist.size() == 0) {
			grid.setHeightByRows(1);
		} else if (mylist.size() >= 10) {
			grid.setHeightByRows(10);
		} else {
			grid.setHeightByRows(mylist.size());
		}
		grid.setVisible(true);
	}
	
	
	

}
