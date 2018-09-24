package privacyanalyzer.ui.view.dashboard;

import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.Labels;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.Marker;
import com.vaadin.addon.charts.model.PlotOptionsColumn;
import com.vaadin.addon.charts.model.PlotOptionsLine;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.board.Row;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;


import privacyanalyzer.backend.data.entity.Permission;
import privacyanalyzer.backend.service.ApkService;

import privacyanalyzer.backend.service.PermissionService;
import privacyanalyzer.backend.service.TrackerService;
import privacyanalyzer.ui.navigation.NavigationManager;


/**
 * The dashboard view showing statistics about sales and deliveries.
 * <p>
 * Created as a single View class because the logic is so simple that using a
 * pattern like MVP would add much overhead for little gain. If more complexity
 * is added to the class, you should consider splitting out a presenter.
 */
@SpringView
public class DashboardView extends DashboardViewDesign implements View {

	private static final String DELIVERIES = "Deliveries";

	private static final String BOARD_ROW_PANELS = "board-row-panels";



	private final BoardLabel totalAPKlabel = new BoardLabel("APKs analyzed", "3/7", "today");
	private final BoardLabel DangerousAPKslabel = new BoardLabel("Dangerous APKs found", "1", "na");
	private final BoardBox notAvailableBox = new BoardBox(DangerousAPKslabel);
	private final BoardLabel addedTodayLabel = new BoardLabel("Average APK Score", "2", "new");
	private final BoardLabel DangerousPermissionlabel = new BoardLabel("Most Used Dangerous Permission", "1", "na");



	private final Grid<Permission> permissionGrid= new Grid<Permission>();

	private final Chart permissionPieChart= new Chart(ChartType.PIE);
	private final Chart permissionOverallChart= new Chart(ChartType.PIE);

	public DataSeries permissionTypeUsagePieData,permissionOverallPieData;
	
	
	private final ApkService apkService;
	private final PermissionService permissionService;
	private final TrackerService trackerService;
	
	@Autowired
	public DashboardView(PermissionService permissionService,ApkService apkService,TrackerService trackerService) {

		
		this.apkService=apkService;
		this.permissionService=permissionService;
		this.trackerService=trackerService;
	}

	

	
	private Long convertToLong(Object o){
        String stringToConvert = String.valueOf(o);
        Long convertedLong = Long.parseLong(stringToConvert);
        return convertedLong;

    }
	Component mostusedbar,dangerousbar;
	@PostConstruct
	public void init() {

		
		mostusedbar =new BasicBar(permissionService.getApkPermissionAssociationRepository().findTopUsedPermissions(new PageRequest(0, 5)),"5 Most Used IDENTIFIED permissions").getChart();
		dangerousbar =new BasicBar(permissionService.getApkPermissionAssociationRepository().findTopUsedDangerousPermissions(new PageRequest(0,5)),"5 Most Used IDENTIFIED  dangerous permissions").getChart();
		
		
		

		setResponsive(true);

		Row row = board.addRow(new BoardBox(totalAPKlabel), notAvailableBox, new BoardBox(addedTodayLabel)
				/*,new BoardBox(tomorrowLabel)*/);
		row.addStyleName("board-row-group");

		//row = board.addRow(/*new BoardBox(bar),*/ new BoardBox(DangerousPermissionlabel));
		//row.addStyleName("board-row-group");


		row = board.addRow(new BoardBox(mostusedbar));
		row.addStyleName(BOARD_ROW_PANELS);
		row = board.addRow(new BoardBox(dangerousbar));
		row.addStyleName(BOARD_ROW_PANELS);

		row = board.addRow(new BoardBox(permissionPieChart));
		row.addStyleName(BOARD_ROW_PANELS);
		row = board.addRow(new BoardBox(permissionGrid));
		row.addStyleName(BOARD_ROW_PANELS);
		row = board.addRow(new BoardBox(permissionOverallChart));
		row.addStyleName(BOARD_ROW_PANELS);
	
		initPermissionTypeUsageMyChart();
		initPermissionTypeOverallMyChart();
		refreshPermissionOverallPie(permissionService.getApkPermissionAssociationRepository().findAllTypesCount());
		//refreshPermissionTypeUsagePie(permissionService.getApkPermissionAssociationRepository().getPermissionIdentifications(permissionService.getPermissionRepository().findAll().get(0)),"");
		
		permissionService.setGridbyPermissions(permissionService.getPermissionRepository().findAll(), this.permissionGrid);
		this.permissionGrid.setSizeFull();
		this.permissionGrid.setSelectionMode(SelectionMode.SINGLE);
		this.permissionGrid.addSelectionListener(event -> {
		   Permission selected =  permissionGrid.getSelectedItems().iterator().next();
		    
		   List<Object[]> r=permissionService.getApkPermissionAssociationRepository().getPermissionIdentifications(selected);
		    refreshPermissionTypeUsagePie(r,selected.toString());
	

		});
	}

	
	
	
	private void refreshPermissionTypeUsagePie(List<Object[]> data,String name) {
		permissionPieChart.getConfiguration().setTitle("Permission Usage for "+name);
		permissionTypeUsagePieData= new DataSeries("Times used");
		for (Object[] temp:data) {
			String type = null;
	    	if (((String) temp[0]).equals("NotRequiredButUsed")) {
	    		type="Not declared but used";
	    		
	    	}else if (((String) temp[0]).equals("RequiredAndUsed")) {
	    		type="Declared and used";
	    		
	    	}else if (((String) temp[0]).equals("RequiredButNotUsed")) {
	    		type="Declared but not used";
	    		
	    	}else if (((String) temp[0]).equals("LibraryPermission")) {
	    		type="Library permission";
	    		
	    	}
			permissionTypeUsagePieData.add(new DataSeriesItem(type, Integer.parseInt((String.valueOf(temp[1]) ))));
    		
    	
    	
    }
		permissionPieChart.getConfiguration().setSeries(permissionTypeUsagePieData);
		permissionPieChart.drawChart();
	}
	
	
	private void initPermissionTypeUsageMyChart() {
		permissionPieChart.setId("permissionPie");
		permissionPieChart.setSizeFull();
		
		Configuration conf= permissionPieChart.getConfiguration();
		conf.setTitle("Select a permission from the grid below, to display its usage");
		permissionTypeUsagePieData=new DataSeries("Used");
		conf.addSeries(permissionTypeUsagePieData);
		conf.getyAxis().setTitle("");
		
	}
	
	private void initPermissionTypeOverallMyChart() {
		permissionOverallChart.setId("permissionOverallPie");
		permissionOverallChart.setSizeFull();
		
		Configuration conf= permissionOverallChart.getConfiguration();
		conf.setTitle("Permission Usage Overall");
		permissionOverallPieData=new DataSeries("Used");
		conf.addSeries(permissionOverallPieData);
		conf.getyAxis().setTitle("");
		
	}
	
	
	
	private void refreshPermissionOverallPie(List<Object[]> data) {
		permissionOverallChart.getConfiguration().setTitle("How permissions are used (overall)");
		permissionTypeUsagePieData= new DataSeries("Percentage (%)");
		int sum=0;
		for (Object[] temp:data) {
			sum+=Integer.parseInt((String.valueOf(temp[1]) ));
			
		}
		
		for (Object[] temp:data) {
			String type = null;
	    	if (((String) temp[0]).equals("NotRequiredButUsed")) {
	    		type="Not declared but used";
	    		
	    	}else if (((String) temp[0]).equals("RequiredAndUsed")) {
	    		type="Declared and used";
	    		
	    	}else if (((String) temp[0]).equals("RequiredButNotUsed")) {
	    		type="Declared but not used";
	    		
	    	}else if (((String) temp[0]).equals("LibraryPermission")) {
	    		type="Library permission";
	    		
	    	}
	    	double roundOff = Math.round(((double)Integer.parseInt((String.valueOf(temp[1]) ))/sum) * 10000.0) / 100.0;
	    	//System.out.println(roundOff);
			permissionTypeUsagePieData.add(new DataSeriesItem(type, roundOff));
    		
    	
    	
    }
		permissionOverallChart.getConfiguration().setSeries(permissionTypeUsagePieData);
		permissionOverallChart.drawChart();
	}
	

	




	@Override
	public void enter(ViewChangeEvent event) {
		
		updateLabels();
		
	}




	private void updateLabels() {
		totalAPKlabel.setContent(Long.toString(apkService.getRepository().count()));
		DangerousAPKslabel.setContent(Long.toString(apkService.getRepository().findDangerousApk()));
		
		
		double avgScore=apkService.getRepository().getAverageScore();
		addedTodayLabel.setContent(String.format("%.2f", avgScore));


		Permission p=permissionService.getApkPermissionAssociationRepository().findTop10UsedDangerousPermissions(new PageRequest(0, 1)).get(0);
		DangerousPermissionlabel.setContent(p.getPermissionName());
	}



}
