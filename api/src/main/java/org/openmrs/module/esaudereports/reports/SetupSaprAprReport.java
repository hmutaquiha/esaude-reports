package org.openmrs.module.esaudereports.reports;

import org.openmrs.Location;
import org.openmrs.module.esaudereports.EsaudeDataExportManager;
import org.openmrs.module.esaudereports.reporting.library.dimension.CommonDimension;
import org.openmrs.module.esaudereports.reporting.library.dimension.SaprAprDimension;
import org.openmrs.module.esaudereports.reporting.library.indicator.SaprAprIndicators;
import org.openmrs.module.esaudereports.reporting.utils.ColumnParameters;
import org.openmrs.module.esaudereports.reporting.utils.EmrReportingUtils;
import org.openmrs.module.esaudereports.reporting.utils.ReportUtils;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Hamilton Mutaquiha on 8/23/17.
 */
@Component
public class SetupSaprAprReport extends EsaudeDataExportManager {
	
	@Autowired
	private SaprAprIndicators indicators;
	
	@Autowired
	private CommonDimension dimension;
	
	@Autowired
	private SaprAprDimension saprAprDimension;
	
	@Override
	public String getExcelDesignUuid() {
		return "f2147824-87eb-11e7-98e7-e334661788be";
	}
	
	@Override
	public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
		List<ReportDesign> reportDesigns = new ArrayList<ReportDesign>();
		reportDesigns.add(buildReportDesign(reportDefinition));
		return reportDesigns;
	}
	
	@Override
	public ReportDesign buildReportDesign(ReportDefinition reportDefinition) {
		return createExcelTemplateDesign(getExcelDesignUuid(), reportDefinition, "SAPR_APR.xls");
	}
	
	@Override
	public String getUuid() {
		return "81e5e83e-87ec-11e7-bd7b-936806d95642";
	}
	
	@Override
	public String getName() {
		return "RELATORIO SAPR/APR - ARV";
	}
	
	@Override
	public String getDescription() {
		return "PEPFAR Report for ARV indicators";
	}
	
	@Override
	public ReportDefinition constructReportDefinition() {
		ReportDefinition rd = new ReportDefinition();
		rd.setUuid(getUuid());
		rd.setName(getName());
		rd.setDescription(getDescription());
		rd.setParameters(getParameters());
		rd.addDataSetDefinition("S", Mapped.mapStraightThrough(dataSetDefinition()));
		return rd;
	}
	
	private DataSetDefinition dataSetDefinition() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("S");
		dsd.setParameters(getParameters());
		
		String indParams = "startDate=${startDate},endDate=${endDate},location=${location}";
		
		//add dimensions to the dsd
		//		dsd.addDimension("age", ReportUtils.map(dimension.dimForQualityImprovement(), "onDate=${endDate}"));
		
		dsd.addDimension("DAPR", ReportUtils.map(saprAprDimension.dimForSaprApr(), indParams));
		
		//bulid the column parameters here
		
		ColumnParameters breastFeeding = new ColumnParameters("LACTANTE", "LACTANTE", "DAPR=LACTANTE");
		ColumnParameters pregnant = new ColumnParameters("GRAVIDA", "GRAVIDA", "DAPR=GRAVIDA");
		ColumnParameters total = new ColumnParameters("Total", "Total", "");
		
		//form columns as list to be used in the dsd
		List<ColumnParameters> allColumns = Arrays.asList(breastFeeding, pregnant, total);
		
		EmrReportingUtils.addRow(dsd, "INILACTANT", "",
		    ReportUtils.map(indicators.inCareWhoStartedTreatmentInPeriod(), indParams), allColumns,
		    Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "INIARVGRAV", "",
		    ReportUtils.map(indicators.inCareWhoStartedTreatmentInPeriod(), indParams), allColumns,
		    Arrays.asList("01", "02", "03"));
		
		return dsd;
	}
	
	@Override
	public String getVersion() {
		return "0.1";
	}
	
	@Override
	public List<Parameter> getParameters() {
		return Arrays.asList(new Parameter("startDate", "Data Inicial", Date.class), new Parameter("endDate", "Data Final",
		        Date.class), new Parameter("revisionEndDate", "Data Final Revisão", Date.class), new Parameter("location",
		        "Unidade Sanitária", Location.class));
	}
}
