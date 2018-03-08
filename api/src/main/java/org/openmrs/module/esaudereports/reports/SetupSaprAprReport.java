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
import org.openmrs.module.reporting.indicator.CohortIndicator;
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
	
	public static final String MAPPINGS_3_MONTHS = "startDate=${endDate-6m+1d},endDate=${endDate-3m},location=${location}";
	
	public static final String MAPPINGS_6_MONTHS = "startDate=${endDate-9m+1d},endDate=${endDate-6m},location=${location}";
	
	public static final String MAPPINGS_12_MONTHS = "startDate=${endDate-15m+1d},endDate=${endDate-12m},location=${location}";
	
	public static final String MAPPINGS_24_MONTHS = "startDate=${endDate-27m+1d},endDate=${endDate-24m},location=${location}";
	
	public static final String MAPPINGS_36_MONTHS = "startDate=${endDate-39m+1d},endDate=${endDate-36m},location=${location}";
	
	public static final String MAPPINGS_48_MONTHS = "startDate=${endDate-51m+1d},endDate=${endDate-48m},location=${location}";
	
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
		
		dsd.addDimension("DAPR", ReportUtils.map(saprAprDimension.dimForSaprApr(), indParams));
		dsd.addDimension("gender", ReportUtils.map(dimension.gender()));
		dsd.addDimension("age", ReportUtils.map(dimension.arvAgeBandsInYears(), "onDate=${endDate}"));
		
		addRowToDataSet(dsd, indicators.inCareWhoStartedTreatmentInPeriod(), "INIARV", indParams);
		addRowToDataSet(dsd, indicators.currentlyOnARTExcludingLostToFollowUp(), "CURRARV", indParams);
		addRowToDataSetRetention(dsd, indicators.currentlyOnARTAndActiveOnGAAC(), "GAAC", indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPatientsInCohort(MAPPINGS_6_MONTHS), "SI", indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPatientsInCohortActive(MAPPINGS_6_MONTHS), "SA", indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPatientsInCohortDead(MAPPINGS_6_MONTHS), "SO", indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPatientsInCohortLostToFollowUp(MAPPINGS_6_MONTHS), "SL", indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPatientsInCohortTransferedTo(MAPPINGS_6_MONTHS), "ST", indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPatientsInCohortSuspended(MAPPINGS_6_MONTHS), "SS", indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPregnantsInCohort(MAPPINGS_6_MONTHS), "SIP", indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPregnantsInCohortActive(MAPPINGS_6_MONTHS), "SAP", indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPregnantsInCohortDead(MAPPINGS_6_MONTHS), "SOP", indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPregnantsInCohortLostToFollowUp(MAPPINGS_6_MONTHS), "SLP",
		    indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPregnantsInCohortTransferedTo(MAPPINGS_6_MONTHS), "STP", indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPregnantsInCohortSuspended(MAPPINGS_6_MONTHS), "SSP", indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfBreastfeedingsInCohort(MAPPINGS_6_MONTHS), "SIB", indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfBreastfeedingsInCohortActive(MAPPINGS_6_MONTHS), "SAB", indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfBreastfeedingsInCohortDead(MAPPINGS_6_MONTHS), "SOB", indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfBreastfeedingsInCohortLostToFollowUp(MAPPINGS_6_MONTHS), "SLB",
		    indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfBreastfeedingsInCohortTransferedTo(MAPPINGS_6_MONTHS), "STB",
		    indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfBreastfeedingsInCohortSuspended(MAPPINGS_6_MONTHS), "SSB",
		    indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPatientsInCohort(MAPPINGS_12_MONTHS), "T0412SI", indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPatientsInCohortActive(MAPPINGS_12_MONTHS), "T0412SA", indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPatientsInCohortDead(MAPPINGS_12_MONTHS), "T0412SO", indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPatientsInCohortLostToFollowUp(MAPPINGS_12_MONTHS), "T0412SL",
		    indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPatientsInCohortTransferedTo(MAPPINGS_12_MONTHS), "T0412ST",
		    indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPatientsInCohortSuspended(MAPPINGS_12_MONTHS), "T0412SS", indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPregnantsInCohort(MAPPINGS_12_MONTHS), "T0412SIP", indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPregnantsInCohortActive(MAPPINGS_12_MONTHS), "T0412SAP", indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPregnantsInCohortDead(MAPPINGS_12_MONTHS), "T0412SOP", indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPregnantsInCohortLostToFollowUp(MAPPINGS_12_MONTHS), "T0412SLP",
		    indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPregnantsInCohortTransferedTo(MAPPINGS_12_MONTHS), "T0412STP",
		    indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPregnantsInCohortSuspended(MAPPINGS_12_MONTHS), "T0412SSP",
		    indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfBreastfeedingsInCohort(MAPPINGS_12_MONTHS), "T0412SIB", indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfBreastfeedingsInCohortActive(MAPPINGS_12_MONTHS), "T0412SAB",
		    indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfBreastfeedingsInCohortDead(MAPPINGS_12_MONTHS), "T0412SOB",
		    indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfBreastfeedingsInCohortLostToFollowUp(MAPPINGS_12_MONTHS),
		    "T0412SLB", indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfBreastfeedingsInCohortTransferedTo(MAPPINGS_12_MONTHS), "T0412STB",
		    indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfBreastfeedingsInCohortSuspended(MAPPINGS_12_MONTHS), "T0412SSB",
		    indParams);
		
		addRowToDataSetRetention(dsd, indicators.numberOfPatientsInCohort(MAPPINGS_24_MONTHS), "T0424SI", indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPatientsInCohortActive(MAPPINGS_24_MONTHS), "T0424SA", indParams);
		
		addRowToDataSetRetention(dsd, indicators.numberOfPatientsInCohort(MAPPINGS_36_MONTHS), "T0436SI", indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPatientsInCohortActive(MAPPINGS_36_MONTHS), "T0436SA", indParams);
		
		addRowToDataSetRetention(dsd, indicators.numberOfPatientsInCohort(MAPPINGS_48_MONTHS), "T0448SI", indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPatientsInCohortActive(MAPPINGS_48_MONTHS), "T0448SA", indParams);
		
		addRowToDataSetWithStartAge(dsd, indicators.everInTreatment(), "T05", indParams);
		
		addRowToDataSetWithStartAge(dsd, indicators.patientsInARTSecondLineRegimen(), "T06", indParams);
		addRowToDataSetWithStartAge(dsd, indicators.numberOfPatientsWhoLeftARTProgram(8), "T07", indParams);
		addRowToDataSetWithStartAge(dsd, indicators.numberOfPatientsWhoLeftARTProgram(10), "T08", indParams);
		addRowToDataSetWithStartAge(dsd, indicators.numberOfPatientsWhoLeftARTProgram(9), "T09", indParams);
		addRowToDataSetWithStartAge(dsd, indicators.numberOfPatientsWhoLeftARTProgram(7), "T10", indParams);
		
		addRowToDataSetWithStartAge(dsd, indicators.numberOfPatientsWithClinicTreatmentFailure(), "T14-C", indParams);
		addRowToDataSetWithStartAge(dsd, indicators.numberOfPatientsWithImunologicTreatmentFailure(), "T14-I", indParams);
		addRowToDataSetWithStartAge(dsd, indicators.numberOfPatientsWithTreatmentFailure(), "T15", indParams);
		
		addRowToDataSet(dsd, indicators.numberOfPatientsInARTAndForMoreThan6MonthsInTreatment(), "T18", indParams);
		
		addRowToDataSetViralLoad(dsd, indicators.numberOfPatientsinARTAndForMoreThan6MonthsWithViralLoadResults(), "T19",
		    indParams);
		
		addRowToDataSet(dsd, indicators.numberOfPatientsWithViralLoadResultsLast12Months(), "T20", indParams);
		addRowToDataSet(dsd, indicators.numberOfPatientsWithUndetectableViralLoadLast12Months(), "T21", indParams);
		
		addRowToDataSetWithStartAge(dsd, indicators.numberOfPatientsNotifiedTBTreatmentWhoStartedART(), "T02", indParams);
		
		addRowToDataSet(dsd, indicators.numberOfPatientsWithViralLoadResultsLast12Months(), "T19-1", indParams);
		
		addRowToDataSetRetention(dsd, indicators.patientsActiveInARTNotifiedTBTreatmentOnARTService(), "T03-10-A", indParams);
		addRowToDataSetRetention(dsd, indicators.patientsNotifiedTBTreatmentOnARTServiceWhoStartedART(), "T03-10-I",
		    indParams);
		
		addRowToDataSetRetention(dsd, indicators.numberOfpatientsActiveInARTWithPositiveTBTracking(), "T03-11-01-PA",
		    indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPatientsActiveInARTWithNegativeTBTracking(), "T03-11-01-NA",
		    indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPatientsWithPositiveTBTrackingWhoStartedART(), "T03-11-01-PI",
		    indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPatientsWithNegativeTBTrackingWhoStartedART(), "T03-11-01-NI",
		    indParams);
		
		addRowToDataSetRetention(dsd,
		    indicators.numberOfPatientsEverInARTWithPositiveTBTrackingWhoHadPositiveDiagnoseTest(), "T03-11-02-P", indParams);
		addRowToDataSetRetention(dsd,
		    indicators.numberOfPatientsEverInARTWithPositiveTBTrackingWhoHadNegativeDiagnoseTest(), "T03-11-02-N", indParams);
		
		addRowToDataSetRetention(dsd, indicators.numberOfPatientsWhoStartedAndFinishedProphilaxyWithIsoniazida(),
		    "T23-01-F-C", indParams);
		addRowToDataSetRetention(dsd,
		    indicators.numberOfPatientsWhoStartedAndFinishedProphilaxyWithIsoniazidaPreviousPeriod(), "T23-01-F-P",
		    indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPatientsWhoStartedAndContinueProphilaxyWithIsoniazida(),
		    "T23-01-C-C", indParams);
		addRowToDataSetRetention(dsd,
		    indicators.numberOfPatientsWhoStartedAndContinueProphilaxyWithIsoniazidaPreviousPeriod(), "T23-01-C-P",
		    indParams);
		
		addRowToDataSetRetention(dsd, indicators.numberOfPatientsEverStartedARTAndStartedProphilaxyWithIsoniazida(),
		    "T24-01-C", indParams);
		addRowToDataSetRetention(dsd,
		    indicators.numberOfPatientsEverStartedARTAndStartedProphilaxyWithIsoniazidaPreviousPeriod(), "T24-01-P",
		    indParams);
		
		addRowToDataSetWithStartAge(dsd, indicators.numberOfPatientsInCohort(MAPPINGS_3_MONTHS), "T04-07-SI", indParams);
		addRowToDataSetWithStartAge(dsd, indicators.numberOfPatientsInCohortActive(MAPPINGS_3_MONTHS), "T04-07-SA",
		    indParams);
		addRowToDataSetWithStartAge(dsd, indicators.numberOfPatientsInCohortDead(MAPPINGS_3_MONTHS), "T04-07-SO", indParams);
		addRowToDataSetWithStartAge(dsd, indicators.numberOfPatientsInCohortLostToFollowUp(MAPPINGS_3_MONTHS), "T04-07-SL",
		    indParams);
		addRowToDataSetWithStartAge(dsd, indicators.numberOfPatientsInCohortTransferedTo(MAPPINGS_3_MONTHS), "T04-07-ST",
		    indParams);
		addRowToDataSetWithStartAge(dsd, indicators.numberOfPatientsInCohortSuspended(MAPPINGS_3_MONTHS), "T04-07-SS",
		    indParams);
		
		addRowToDataSetRetention(dsd, indicators.numberOfPatientsInCohort(MAPPINGS_3_MONTHS), "T04-03-SI", indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPatientsInCohortActive(MAPPINGS_3_MONTHS), "T04-03-SA", indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPatientsInCohortDead(MAPPINGS_3_MONTHS), "T04-03-SO", indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPatientsInCohortLostToFollowUp(MAPPINGS_3_MONTHS), "T04-03-SL",
		    indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPatientsInCohortTransferedTo(MAPPINGS_3_MONTHS), "T04-03-ST",
		    indParams);
		addRowToDataSetRetention(dsd, indicators.numberOfPatientsInCohortSuspended(MAPPINGS_3_MONTHS), "T04-03-SS",
		    indParams);
		
		addRowToDataSetWithStartAge(dsd, indicators.numberOfPatientsInCohort(MAPPINGS_24_MONTHS), "T04-24-SI", indParams);
		addRowToDataSetWithStartAge(dsd, indicators.numberOfPatientsInCohortActive(MAPPINGS_24_MONTHS), "T04-24-SA",
		    indParams);
		addRowToDataSetWithStartAge(dsd, indicators.numberOfPatientsInCohortDead(MAPPINGS_24_MONTHS), "T04-24-SO", indParams);
		addRowToDataSetWithStartAge(dsd, indicators.numberOfPatientsInCohortLostToFollowUp(MAPPINGS_24_MONTHS), "T04-24-SL",
		    indParams);
		addRowToDataSetWithStartAge(dsd, indicators.numberOfPatientsInCohortTransferedTo(MAPPINGS_24_MONTHS), "T04-24-ST",
		    indParams);
		addRowToDataSetWithStartAge(dsd, indicators.numberOfPatientsInCohortSuspended(MAPPINGS_24_MONTHS), "T04-24-SS",
		    indParams);
		
		addRowToDataSetWithStartAge(dsd, indicators.numberOfPatientsInCohort(MAPPINGS_36_MONTHS), "T04-36-SI", indParams);
		addRowToDataSetWithStartAge(dsd, indicators.numberOfPatientsInCohortActive(MAPPINGS_36_MONTHS), "T04-36-SA",
		    indParams);
		addRowToDataSetWithStartAge(dsd, indicators.numberOfPatientsInCohortDead(MAPPINGS_36_MONTHS), "T04-36-SO", indParams);
		addRowToDataSetWithStartAge(dsd, indicators.numberOfPatientsInCohortLostToFollowUp(MAPPINGS_36_MONTHS), "T04-36-SL",
		    indParams);
		addRowToDataSetWithStartAge(dsd, indicators.numberOfPatientsInCohortTransferedTo(MAPPINGS_36_MONTHS), "T04-36-ST",
		    indParams);
		addRowToDataSetWithStartAge(dsd, indicators.numberOfPatientsInCohortSuspended(MAPPINGS_36_MONTHS), "T04-36-SS",
		    indParams);
		
		addRowToDataSetWithStartAge(dsd, indicators.numberOfPatientsWhoStartedARTLastMonth(), "MQSTART", indParams);
		addRowToDataSetWithStartAge(dsd, indicators.numberOfPatientsWhoStartedARTLastMonth(31), "MQINI2S", indParams);
		addRowToDataSetWithStartAge(dsd, indicators.numberOfPatientsWhoStartedARTLastMonth(15), "MQINI30", indParams);
		
		addRowToDataSetWithStartAge(dsd, indicators.patientsWithViralLoadResultsInPeriod(), "CVPERIOD", indParams);
		addRowToDataSetWithStartAge(dsd, indicators.patientsWithUndetectableViralLoadInPeriod(), "CVINDPERIOD", indParams);
		
		addRowToDataSetWithStartAge(dsd,
		    indicators.numberOfPatientsIligebleToRotineViralLoadTestStartSitesExcludingTherapeuticFailure(), "CVROTTS",
		    indParams);
		addRowToDataSetWithStartAge(dsd,
		    indicators.numberOfPatientsIligebleToRotineViralLoadNotTestStartSitesExcludingTherapeuticFailure(),
		    "CVROTNAOTS", indParams);
		addRowToDataSetWithStartAge(dsd, indicators.numberOfPregnantsIligebleToViralLoad(), "CVROTELGRA", indParams);
		addRowToDataSetWithStartAge(dsd, indicators.numberOfPatientsIligebleToViralLoadForTherapeuticFailureSuspect(),
		    "CVTAREL", indParams);
		
		return dsd;
	}
	
	private void addRowToDataSet(CohortIndicatorDataSetDefinition dsd, CohortIndicator indicator, String columnName,
	        String indParams) {
		
		//bulid the column parameters here
		ColumnParameters breastFeeding = new ColumnParameters("LACTANTE", "LACTANTE", "DAPR=L"); // TODO verificar esta desagragacao esta a trazer
		// valor errado
		ColumnParameters pregnant = new ColumnParameters("GRAVIDA", "GRAVIDA", "DAPR=G");
		ColumnParameters maleLessThan1Year = new ColumnParameters("<1M", "<1M", "gender=M|age=<1");
		ColumnParameters male1To4 = new ColumnParameters("1-4M", "1-4M", "gender=M|age=1-4");
		ColumnParameters male5To9 = new ColumnParameters("5-9M", "5-9M", "gender=M|age=5-9");
		ColumnParameters male5To14 = new ColumnParameters("5-14M", "5-14M", "gender=M|age=5-14");
		ColumnParameters male10To14 = new ColumnParameters("10-14M", "10-14M", "gender=M|age=10-14");
		ColumnParameters male15To19 = new ColumnParameters("15-19M", "15-19M", "gender=M|age=15-19");
		ColumnParameters male15More = new ColumnParameters("15+M", "15+M", "gender=M|age=15+");
		ColumnParameters male20To24 = new ColumnParameters("20-24M", "20-24M", "gender=M|age=20-24");
		ColumnParameters male25To49 = new ColumnParameters("25-49M", "25-49M", "gender=M|age=25-49");
		ColumnParameters male50More = new ColumnParameters("50+M", "50+M", "gender=M|age=50+");
		ColumnParameters femaleLessThan1Year = new ColumnParameters("<1F", "<1F", "gender=F|age=<1");
		ColumnParameters female1To4 = new ColumnParameters("1-4F", "1-4F", "gender=F|age=1-4");
		ColumnParameters female5To9 = new ColumnParameters("5-9F", "5-9F", "gender=F|age=5-9");
		ColumnParameters female5To14 = new ColumnParameters("5-14F", "5-14F", "gender=F|age=5-14");
		ColumnParameters female10To14 = new ColumnParameters("10-14F", "10-14F", "gender=F|age=10-14");
		ColumnParameters female15To19 = new ColumnParameters("15-19F", "15-19F", "gender=F|age=15-19");
		ColumnParameters female15More = new ColumnParameters("15+F", "15+F", "gender=F|age=15+");
		ColumnParameters female20To24 = new ColumnParameters("20-24F", "20-24F", "gender=F|age=20-24");
		ColumnParameters female25To49 = new ColumnParameters("25-49F", "25-49F", "gender=F|age=25-49");
		ColumnParameters female50More = new ColumnParameters("50+F", "50+F", "gender=F|age=50+");
		
		ColumnParameters male20More = new ColumnParameters("20+M", "20+M", "gender=M|age=20+");
		ColumnParameters female20More = new ColumnParameters("20+F", "20+F", "gender=F|age=20+");
		ColumnParameters cvRotina = new ColumnParameters("CVROTINA", "CVROTINA", "DAPR=CVROTINA");
		ColumnParameters cvTargeted = new ColumnParameters("CVTARGETED", "CVTARGETED", "DAPR=CVTARGETED");
		ColumnParameters activeGAAC = new ColumnParameters("GAAC", "GAAC", "DAPR=GAAC");
		
		//form columns as list to be used in the dsd
		List<ColumnParameters> allColumns = Arrays.asList(breastFeeding, pregnant, maleLessThan1Year, male1To4, male5To9,
		    male5To14, male10To14, male15To19, male15More, male20To24, male25To49, male50More, femaleLessThan1Year,
		    female1To4, female5To9, female5To14, female10To14, female15To19, female15More, female20To24, female25To49,
		    female50More, male20More, female20More, cvRotina, cvTargeted, activeGAAC);
		
		EmrReportingUtils.addRow(dsd, columnName, "", ReportUtils.map(indicator, indParams), allColumns, Arrays.asList("01",
		    "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
		    "20", "21", "22", "23", "24", "25", "26", "27"));
	}
	
	private void addRowToDataSetRetention(CohortIndicatorDataSetDefinition dsd, CohortIndicator indicator,
	        String columnName, String indParams) {
		
		//bulid the column parameters here
		ColumnParameters male0To14 = new ColumnParameters("0-14M", "0-14M", "gender=M|age=0-14");
		ColumnParameters male15More = new ColumnParameters("15+M", "15+M", "gender=M|age=15+");
		ColumnParameters female0To14 = new ColumnParameters("0-14F", "0-14F", "gender=F|age=0-14");
		ColumnParameters female15More = new ColumnParameters("15+F", "15+F", "gender=F|age=15+");
		
		ColumnParameters maleLess1_StartDate = new ColumnParameters("M<1", "M<1", "DAPR=M<1_START_DATE");
		ColumnParameters male0To14_StartDate = new ColumnParameters("M0T14", "M0T14", "DAPR=M0T14_START_DATE");
		ColumnParameters male1To4_StartDate = new ColumnParameters("M1T4", "M1T4", "DAPR=M1T4_START_DATE");
		ColumnParameters male1To9_StartDate = new ColumnParameters("M1T9", "M1T9", "DAPR=M1T9_START_DATE");
		ColumnParameters male5To14_StartDate = new ColumnParameters("M5T14", "M5T14", "DAPR=M5T14_START_DATE");
		ColumnParameters male10To14_StartDate = new ColumnParameters("M10T14", "M10T14", "DAPR=M10T14_START_DATE");
		ColumnParameters male15To19_StartDate = new ColumnParameters("M15T19", "M15T19", "DAPR=M15T19_START_DATE");
		ColumnParameters male15Plus_StartDate = new ColumnParameters("M15+", "M15+", "DAPR=M15+_START_DATE");
		ColumnParameters male20To24_StartDate = new ColumnParameters("M20T24", "M20T24", "DAPR=M20T24_START_DATE");
		ColumnParameters male25To49_StartDate = new ColumnParameters("M25T49", "M25T49", "DAPR=M25T49_START_DATE");
		ColumnParameters male50Plus_StartDate = new ColumnParameters("M50+", "M50+", "DAPR=M50+_START_DATE");
		ColumnParameters femaleLess1_StartDate = new ColumnParameters("F<1", "F<1", "DAPR=F<1_START_DATE");
		ColumnParameters female0To14_StartDate = new ColumnParameters("F0T14", "F0T14", "DAPR=F0T14_START_DATE");
		ColumnParameters female1To4_StartDate = new ColumnParameters("F1T4", "F1T4", "DAPR=F1T4_START_DATE");
		ColumnParameters female1To9_StartDate = new ColumnParameters("F1T9", "F1T9", "DAPR=F1T9_START_DATE");
		ColumnParameters female5To14_StartDate = new ColumnParameters("F5T14", "F5T14", "DAPR=F5T14_START_DATE");
		ColumnParameters female10To14_StartDate = new ColumnParameters("F10T14", "F10T14", "DAPR=F10T14_START_DATE");
		ColumnParameters female15To19_StartDate = new ColumnParameters("F15T19", "F15T19", "DAPR=F15T19_START_DATE");
		ColumnParameters female15Plus_StartDate = new ColumnParameters("F15+", "F15+", "DAPR=F15+_START_DATE");
		ColumnParameters female20To24_StartDate = new ColumnParameters("F20T24", "F20T24", "DAPR=F20T24_START_DATE");
		ColumnParameters female25To49_StartDate = new ColumnParameters("F25T49", "F25T49", "DAPR=F25T49_START_DATE");
		ColumnParameters female50Plus_StartDate = new ColumnParameters("F50+", "F50+", "DAPR=F50+_START_DATE");
		
		//form columns as list to be used in the dsd
		List<ColumnParameters> allColumns = Arrays.asList(male0To14, male15More, female0To14, female15More,
		    maleLess1_StartDate, male0To14_StartDate, male1To4_StartDate, male1To9_StartDate, male5To14_StartDate,
		    male10To14_StartDate, male15To19_StartDate, male15Plus_StartDate, male20To24_StartDate, male25To49_StartDate,
		    male50Plus_StartDate, femaleLess1_StartDate, female0To14_StartDate, female1To4_StartDate, female1To9_StartDate,
		    female5To14_StartDate, female10To14_StartDate, female15To19_StartDate, female15Plus_StartDate,
		    female20To24_StartDate, female25To49_StartDate, female50Plus_StartDate);
		
		EmrReportingUtils.addRow(dsd, columnName, "", ReportUtils.map(indicator, indParams), allColumns, Arrays.asList("01",
		    "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
		    "20", "21", "22", "23", "24", "25", "26"));
	}
	
	private void addRowToDataSetWithStartAge(CohortIndicatorDataSetDefinition dsd, CohortIndicator indicator,
	        String columnName, String indParams) {
		
		//bulid the column parameters here
		ColumnParameters male = new ColumnParameters("Male", "Male", "gender=M");
		ColumnParameters female = new ColumnParameters("Female", "Female", "gender=F");
		ColumnParameters total = new ColumnParameters("total", "total", "");
		ColumnParameters all0To14 = new ColumnParameters("0-14", "0-14", "age=0-14");
		ColumnParameters all15More = new ColumnParameters("15+", "15+", "age=15+");
		ColumnParameters all0To14_StartDate = new ColumnParameters("0-14-START-DATE", "0-14-START-DATE",
		        "DAPR=C0T14_START_DATE");
		ColumnParameters all15More_StartDate = new ColumnParameters("15+-START-DATE", "15+-START-DATE",
		        "DAPR=A15+_START_DATE");
		
		ColumnParameters maleLess1_StartDate = new ColumnParameters("M<1", "M<1", "DAPR=M<1_START_DATE");
		ColumnParameters male1To4_StartDate = new ColumnParameters("M1T4", "M1T4", "DAPR=M1T4_START_DATE");
		ColumnParameters male5To14_StartDate = new ColumnParameters("M5T14", "M5T14", "DAPR=M5T14_START_DATE");
		ColumnParameters male15Plus_StartDate = new ColumnParameters("M15+", "M15+", "DAPR=M15+_START_DATE");
		ColumnParameters femaleLess1_StartDate = new ColumnParameters("F<1", "F<1", "DAPR=F<1_START_DATE");
		ColumnParameters female1To4_StartDate = new ColumnParameters("F1T4", "F1T4", "DAPR=F1T4_START_DATE");
		ColumnParameters female5To14_StartDate = new ColumnParameters("F5T14", "F5T14", "DAPR=F5T14_START_DATE");
		ColumnParameters female15Plus_StartDate = new ColumnParameters("F15+", "F15+", "DAPR=F15+_START_DATE");
		
		ColumnParameters pregnant = new ColumnParameters("GRAVIDA", "GRAVIDA", "DAPR=G");
		ColumnParameters all2To5 = new ColumnParameters("2-5", "2-5", "age=2-5");
		ColumnParameters all6To14 = new ColumnParameters("6-14", "6-14", "age=6-14");
		
		//form columns as list to be used in the dsd
		List<ColumnParameters> allColumns = Arrays.asList(male, female, total, all0To14, all15More, all0To14_StartDate,
		    all15More_StartDate, maleLess1_StartDate, male1To4_StartDate, male5To14_StartDate, male15Plus_StartDate,
		    femaleLess1_StartDate, female1To4_StartDate, female5To14_StartDate, female15Plus_StartDate, pregnant, all2To5,
		    all6To14);
		
		EmrReportingUtils.addRow(dsd, columnName, "", ReportUtils.map(indicator, indParams), allColumns, Arrays.asList("01",
		    "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18"));
	}
	
	private void addRowToDataSetViralLoad(CohortIndicatorDataSetDefinition dsd, CohortIndicator indicator,
	        String columnName, String indParams) {
		
		//bulid the column parameters here
		ColumnParameters detectableVL = new ColumnParameters("DETECTABLEVL", "DETECTABLEVL", "DAPR=DETECTABLEVL");
		ColumnParameters undetectableVL = new ColumnParameters("UNDETECTABLEVL", "UNDETECTABLEVL", "DAPR=UNDETECTABLEVL");
		
		//form columns as list to be used in the dsd
		List<ColumnParameters> allColumns = Arrays.asList(detectableVL, undetectableVL);
		
		EmrReportingUtils.addRow(dsd, columnName, "", ReportUtils.map(indicator, indParams), allColumns,
		    Arrays.asList("01", "02"));
	}
	
	@Override
	public String getVersion() {
		return "0.1";
	}
	
	@Override
	public List<Parameter> getParameters() {
		return Arrays.asList(new Parameter("startDate", "Data Inicial", Date.class), new Parameter("endDate", "Data Final",
		        Date.class), new Parameter("location", "Unidade Sanitária", Location.class));
	}
}
