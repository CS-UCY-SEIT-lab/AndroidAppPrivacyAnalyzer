package privacyanalyzer.ui.view.dashboard;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.AxisTitle;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataLabels;
import com.vaadin.addon.charts.model.HorizontalAlign;
import com.vaadin.addon.charts.model.LayoutDirection;
import com.vaadin.addon.charts.model.Legend;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.PlotOptionsBar;
import com.vaadin.addon.charts.model.Series;
import com.vaadin.addon.charts.model.Tooltip;
import com.vaadin.addon.charts.model.VerticalAlign;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.ui.Component;
import privacyanalyzer.backend.data.entity.Permission;

@SuppressWarnings("serial")
public class BasicBar extends AbstractVaadinChartExample {

	List<Object[]> data;
	String title;
	public BasicBar(List<Object[]> data,String title) {
		super();
		this.data=data;
		this.title=title;
	}
	
	
    @Override
    public String getDescription() {
        return "Basic bar";
    }

    @Override
    protected Component getChart() {
        Chart chart = new Chart(ChartType.BAR);
        chart.setSizeFull();
        Configuration conf = chart.getConfiguration();
        
        conf.setTitle(this.title);

        XAxis x = new XAxis();
        //x.setCategories("Africa", "America", "Asia", "Europe", "Oceania");
        for (Object[]p:data) {
        	
        	x.addCategory(((Permission) p[0]).getPermissionName());
        }
      
        
        x.setTitle((String) null);
        conf.addxAxis(x);
       
        YAxis y = new YAxis();
        y.setMin(0);
        AxisTitle title = new AxisTitle(null);
        title.setAlign(VerticalAlign.MIDDLE);
        y.setTitle(title);
       
        conf.addyAxis(y);

        //Tooltip tooltip = new Tooltip();
        //tooltip.setFormatter("'this.y +' times'");
        //conf.setTooltip(tooltip);

        PlotOptionsBar plot = new PlotOptionsBar();
        plot.setDataLabels(new DataLabels(true));
        
        conf.setPlotOptions(plot);

        Legend legend = new Legend();
        legend.setLayout(LayoutDirection.VERTICAL);
        legend.setAlign(HorizontalAlign.RIGHT);
        legend.setVerticalAlign(VerticalAlign.TOP);
        legend.setX(-150);
        legend.setY(100);
        legend.setFloating(true);
        legend.setBorderWidth(1);
        legend.setBackgroundColor(new SolidColor("#FFFFFF"));
        legend.setShadow(true);
      
        conf.setLegend(legend);

        conf.disableCredits();

        List<Series> series = new ArrayList<Series>();
        ListSeries ls=new ListSeries("Times Identified");
        	for (Object[]p:data) {
        		
        		ls.addData(convertToLong(p[1]));
        }
        	
        	series.add(ls);
        //series.add(new ListSeries("Year 1800", 107, 31, 635, 203, 2));
       // series.add(new ListSeries("Year 1900", 133, 156, 947, 408, 6));
       // series.add(new ListSeries("Year 2008", 973, 914, 4054, 732, 34));
        conf.setSeries(series);

        chart.drawChart(conf);
        
        return chart;
    }
	private Long convertToLong(Object o){
        String stringToConvert = String.valueOf(o);
        Long convertedLong = Long.parseLong(stringToConvert);
        return convertedLong;

    }
	
}