package org.openmrs.module.esaudereports.reports;

import org.openmrs.Location;
import org.openmrs.module.esaudereports.EsaudeDataExportManager;
import org.openmrs.module.esaudereports.reporting.library.indicator.MerIndicators;
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
 * Created by Hamilton Mutaquiha on 2/14/18.
 */
@Component
public class SetupPepfarMerReport extends EsaudeDataExportManager {
	
	@Autowired
	private MerIndicators indicators;
	
	@Override
	public String getExcelDesignUuid() {
		return "1e3c3d14-1186-11e8-a4bf-c3bff82f2d68";
	}
	
	@Override
	public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
		List<ReportDesign> reportDesigns = new ArrayList<ReportDesign>();
		reportDesigns.add(buildReportDesign(reportDefinition));
		return reportDesigns;
	}
	
	@Override
	public ReportDesign buildReportDesign(ReportDefinition reportDefinition) {
		return createExcelTemplateDesign(getExcelDesignUuid(), reportDefinition, "PEPFAR_MER.xls");
	}
	
	@Override
	public String getUuid() {
		return "069d5f08-1186-11e8-bd57-2759a579b57e";
	}
	
	@Override
	public String getName() {
		return "PEPFAR MER REPORT";
	}
	
	@Override
	public String getDescription() {
		return "PEPFAR is partnering with the international community to drive towards the UNAIDS 90-90-90 global goals: 90 percent of people living with HIV know their HIV status, 90 percent of people who know their HIV status are accessing treatment, and 90 percent of people on treatment have suppressed viral loads";
	}
	
	@Override
	public ReportDefinition constructReportDefinition() {
		ReportDefinition rd = new ReportDefinition();
		rd.setUuid(getUuid());
		rd.setName(getName());
		rd.setDescription(getDescription());
		rd.setParameters(getParameters());
		rd.addDataSetDefinition("M", Mapped.mapStraightThrough(dataSetDefinition()));
		return rd;
	}
	
	private DataSetDefinition dataSetDefinition() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("S");
		dsd.setParameters(getParameters());
		String indParams = "startDate=${startDate},endDate=${endDate},location=${location}";
		
		ColumnParameters total = new ColumnParameters("total", "total", "");
		
		List<ColumnParameters> allColumns = Arrays.asList(total);
		
		addDataTxNewIndicator(dsd, indParams, allColumns);
		addDataTxCurrIndicator(dsd, indParams, allColumns);
		addDataTxRetIndicatorNumerator(dsd, indParams, allColumns, 12);
		addDataTxRetIndicatorDenominator(dsd, indParams, allColumns, 12);
		addDataTxPvlsIndicatorNumerator(dsd, indParams, allColumns);
		addDataTxPvlsIndicatorDenominator(dsd, indParams, allColumns);
		
		return dsd;
	}
	
	private void addDataTxNewIndicator(CohortIndicatorDataSetDefinition dsd, String indParams,
	        List<ColumnParameters> allColumns) {
		EmrReportingUtils.addRow(dsd, "TXNEWALL", "INICO TARV", ReportUtils.map(indicators.artStartInPeriod(), indParams),
		    allColumns, Arrays.asList("01"));
		
		EmrReportingUtils.addRow(dsd, "INIGRAVIDA", "INICO TARV - GRAVIDA",
		    ReportUtils.map(indicators.artStartInPeriodPregnants(), indParams), allColumns, Arrays.asList("01"));
		
		EmrReportingUtils.addRow(dsd, "INILACTANTE", "INICO TARV - LACTANTE",
		    ReportUtils.map(indicators.artStartInPeriodBreastfeedings(), indParams), allColumns, Arrays.asList("01"));
		
		EmrReportingUtils.addRow(dsd, "T020A1C", "INICO TARV - CRIANCAS 0-11 MESES",
		    ReportUtils.map(indicators.artStartInPeriodByAge(0, 1), indParams), allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T021A10C", "INICO TARV - CRIANCAS 1-9 ANOS",
		    ReportUtils.map(indicators.artStartInPeriodByAge(1, 10), indParams), allColumns, Arrays.asList("01"));
		
		EmrReportingUtils.addRow(dsd, "T0210A14MA", "INICO TARV - MASC 10-14",
		    ReportUtils.map(indicators.artStartInPeriodByGenderAndAge('M', 10, 15), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T0215A19MA", "INICO TARV - MASC 15-19",
		    ReportUtils.map(indicators.artStartInPeriodByGenderAndAge('M', 15, 20), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T0220A24MA", "INICO TARV - MASC 20-24",
		    ReportUtils.map(indicators.artStartInPeriodByGenderAndAge('M', 20, 25), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T0225A29MA", "INICO TARV - MASC 25-29",
		    ReportUtils.map(indicators.artStartInPeriodByGenderAndAge('M', 25, 30), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T0230A34MA", "INICO TARV - MASC 30-34",
		    ReportUtils.map(indicators.artStartInPeriodByGenderAndAge('M', 30, 35), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T0235A39MA", "INICO TARV - MASC 35-40",
		    ReportUtils.map(indicators.artStartInPeriodByGenderAndAge('M', 35, 40), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T0240A49MA", "INICO TARV - MASC 40-49",
		    ReportUtils.map(indicators.artStartInPeriodByGenderAndAge('M', 40, 50), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T0250MAIMA", "INICO TARV - MASC 50+",
		    ReportUtils.map(indicators.artStartInPeriodByGenderAndAge('M', 50, 9999), indParams), allColumns,
		    Arrays.asList("01"));
		
		EmrReportingUtils.addRow(dsd, "T0210A14FA", "INICO TARV - FEM 10-14",
		    ReportUtils.map(indicators.artStartInPeriodByGenderAndAge('F', 10, 15), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T0215A19FA", "INICO TARV - FEM 15-19",
		    ReportUtils.map(indicators.artStartInPeriodByGenderAndAge('F', 15, 20), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T0220A24FA", "INICO TARV - FEM 20-24",
		    ReportUtils.map(indicators.artStartInPeriodByGenderAndAge('F', 20, 25), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T0225A29FA", "INICO TARV - FEM 25-29",
		    ReportUtils.map(indicators.artStartInPeriodByGenderAndAge('F', 25, 30), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T0230A34FA", "INICO TARV - FEM 30-34",
		    ReportUtils.map(indicators.artStartInPeriodByGenderAndAge('F', 30, 35), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T0235A39FA", "INICO TARV - FEM 35-39",
		    ReportUtils.map(indicators.artStartInPeriodByGenderAndAge('F', 35, 40), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T0240A49FA", "INICO TARV - FEM 40-49",
		    ReportUtils.map(indicators.artStartInPeriodByGenderAndAge('F', 40, 50), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T0250MAIFA", "INICO TARV - FEM 50+",
		    ReportUtils.map(indicators.artStartInPeriodByGenderAndAge('F', 50, 9999), indParams), allColumns,
		    Arrays.asList("01"));
		
		EmrReportingUtils.addRow(dsd, "TX_NTBALL", "INICO TARV - TB",
		    ReportUtils.map(indicators.artStartInPeriodWithTB(), indParams), allColumns, Arrays.asList("01"));
	}
	
	private void addDataTxCurrIndicator(CohortIndicatorDataSetDefinition dsd, String indParams,
	        List<ColumnParameters> allColumns) {
		EmrReportingUtils.addRow(dsd, "TXCURRALL", "EM TARV", ReportUtils.map(indicators.inTreatmentInPeriod(), indParams),
		    allColumns, Arrays.asList("01"));
		
		EmrReportingUtils.addRow(dsd, "TXCURm0t11", "EM TARV - CRIANCAS 0-11 MESES",
		    ReportUtils.map(indicators.inTreatmentInPeriodByAge(0, 1), indParams), allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "TXCURa1t9", "EM TARV - CRIANCAS 1-9 ANOS",
		    ReportUtils.map(indicators.inTreatmentInPeriodByAge(1, 10), indParams), allColumns, Arrays.asList("01"));
		
		EmrReportingUtils.addRow(dsd, "T0310A14MA", " EM TARV - MASC 10-14",
		    ReportUtils.map(indicators.inTreatmentInPeriodByGenderAndAge('M', 10, 15), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T0315A19MA", " EM TARV - MASC 15-19",
		    ReportUtils.map(indicators.inTreatmentInPeriodByGenderAndAge('M', 15, 20), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T0320A24MA", " EM TARV - MASC 20-24",
		    ReportUtils.map(indicators.inTreatmentInPeriodByGenderAndAge('M', 20, 25), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T0325A29MA", " EM TARV - MASC 25-29",
		    ReportUtils.map(indicators.inTreatmentInPeriodByGenderAndAge('M', 25, 30), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T0330A34MA", " EM TARV - MASC 30-34",
		    ReportUtils.map(indicators.inTreatmentInPeriodByGenderAndAge('M', 30, 35), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T0335A39MA", " EM TARV - MASC 35-40",
		    ReportUtils.map(indicators.inTreatmentInPeriodByGenderAndAge('M', 35, 40), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T0340A49MA", " EM TARV - MASC 40-49",
		    ReportUtils.map(indicators.inTreatmentInPeriodByGenderAndAge('M', 40, 50), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T0350MAIMA", " EM TARV - MASC 50+",
		    ReportUtils.map(indicators.inTreatmentInPeriodByGenderAndAge('M', 50, 9999), indParams), allColumns,
		    Arrays.asList("01"));
		
		EmrReportingUtils.addRow(dsd, "T0310A14FA", " EM TARV - FEM 10-14",
		    ReportUtils.map(indicators.inTreatmentInPeriodByGenderAndAge('F', 10, 15), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T0315A19FA", " EM TARV - FEM 15-19",
		    ReportUtils.map(indicators.inTreatmentInPeriodByGenderAndAge('F', 15, 20), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T0320A24FA", " EM TARV - FEM 20-24",
		    ReportUtils.map(indicators.inTreatmentInPeriodByGenderAndAge('F', 20, 25), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T0325A29FA", " EM TARV - FEM 25-29",
		    ReportUtils.map(indicators.inTreatmentInPeriodByGenderAndAge('F', 25, 30), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T0330A34FA", " EM TARV - FEM 30-34",
		    ReportUtils.map(indicators.inTreatmentInPeriodByGenderAndAge('F', 30, 35), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T0335A39FA", " EM TARV - FEM 35-39",
		    ReportUtils.map(indicators.inTreatmentInPeriodByGenderAndAge('F', 35, 40), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T0340A49FA", " EM TARV - FEM 40-49",
		    ReportUtils.map(indicators.inTreatmentInPeriodByGenderAndAge('F', 40, 50), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T0350MAIFA", " EM TARV - FEM 50+",
		    ReportUtils.map(indicators.inTreatmentInPeriodByGenderAndAge('F', 50, 9999), indParams), allColumns,
		    Arrays.asList("01"));
	}
	
	private void addDataTxRetIndicatorNumerator(CohortIndicatorDataSetDefinition dsd, String indParams,
	        List<ColumnParameters> allColumns, int months) {
		EmrReportingUtils.addRow(dsd, "T04SAALL", "ACTIVO EM TARV",
		    ReportUtils.map(indicators.inTreatmentInPeriodRetention(months), indParams), allColumns, Arrays.asList("01"));
		
		EmrReportingUtils.addRow(dsd, "T04SAPI", "ACTIVO EM TARV - GRAVIDA",
		    ReportUtils.map(indicators.inTreatmentInPeriodPregnantsRetention(months), indParams), allColumns,
		    Arrays.asList("01"));
		
		EmrReportingUtils.addRow(dsd, "T04SABI", "ACTIVO EM TARV - LACTANTE",
		    ReportUtils.map(indicators.inTreatmentInPeriodBreastfeedingsRetention(months), indParams), allColumns,
		    Arrays.asList("01"));
		
		EmrReportingUtils.addRow(dsd, "T04SA0A11M", "ACTIVO EM TARV - CRIANCAS 0-11 MESES",
		    ReportUtils.map(indicators.inTreatmentInPeriodByAgeRetention(0, 1, months), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T04SA1A9A", "ACTIVO EM TARV - CRIANCAS 1-9 ANOS",
		    ReportUtils.map(indicators.inTreatmentInPeriodByAgeRetention(1, 10, months), indParams), allColumns,
		    Arrays.asList("01"));
		
		EmrReportingUtils.addRow(dsd, "T04SA1014M", "ACTIVO EM TARV - MASC 10-14",
		    ReportUtils.map(indicators.inTreatmentInPeriodByGenderAndAgeRetention('M', 10, 15, months), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T04SA1519M", "ACTIVO EM TARV - MASC 15-19",
		    ReportUtils.map(indicators.inTreatmentInPeriodByGenderAndAgeRetention('M', 15, 20, months), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T04SA2024M", "ACTIVO EM TARV - MASC 20-24",
		    ReportUtils.map(indicators.inTreatmentInPeriodByGenderAndAgeRetention('M', 20, 25, months), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T04SA2529M", "ACTIVO EM TARV - MASC 25-29",
		    ReportUtils.map(indicators.inTreatmentInPeriodByGenderAndAgeRetention('M', 25, 30, months), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T04SA3034M", "ACTIVO EM TARV - MASC 30-34",
		    ReportUtils.map(indicators.inTreatmentInPeriodByGenderAndAgeRetention('M', 30, 35, months), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T04SA3539M", "ACTIVO EM TARV - MASC 35-40",
		    ReportUtils.map(indicators.inTreatmentInPeriodByGenderAndAgeRetention('M', 35, 40, months), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T04SA4049M", "ACTIVO EM TARV - MASC 40-49",
		    ReportUtils.map(indicators.inTreatmentInPeriodByGenderAndAgeRetention('M', 40, 50, months), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T04SA50MAM", "ACTIVO EM TARV - MASC 50+",
		    ReportUtils.map(indicators.inTreatmentInPeriodByGenderAndAgeRetention('M', 50, 9999, months), indParams),
		    allColumns, Arrays.asList("01"));
		
		EmrReportingUtils.addRow(dsd, "T04SA1014F", "ACTIVO EM TARV - FEM 10-14",
		    ReportUtils.map(indicators.inTreatmentInPeriodByGenderAndAgeRetention('F', 10, 15, months), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T04SA1519F", "ACTIVO EM TARV - FEM 15-19",
		    ReportUtils.map(indicators.inTreatmentInPeriodByGenderAndAgeRetention('F', 15, 20, months), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T04SA2024F", "ACTIVO EM TARV - FEM 20-24",
		    ReportUtils.map(indicators.inTreatmentInPeriodByGenderAndAgeRetention('F', 20, 25, months), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T04SA2529F", "ACTIVO EM TARV - FEM 25-29",
		    ReportUtils.map(indicators.inTreatmentInPeriodByGenderAndAgeRetention('F', 25, 30, months), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T04SA3034F", "ACTIVO EM TARV - FEM 30-34",
		    ReportUtils.map(indicators.inTreatmentInPeriodByGenderAndAgeRetention('F', 30, 35, months), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T04SA3539F", "ACTIVO EM TARV - FEM 35-39",
		    ReportUtils.map(indicators.inTreatmentInPeriodByGenderAndAgeRetention('F', 35, 40, months), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T04SA4049F", "ACTIVO EM TARV - FEM 40-49",
		    ReportUtils.map(indicators.inTreatmentInPeriodByGenderAndAgeRetention('F', 40, 50, months), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T04SA50MAF", "ACTIVO EM TARV - FEM 50+",
		    ReportUtils.map(indicators.inTreatmentInPeriodByGenderAndAgeRetention('F', 50, 9999, months), indParams),
		    allColumns, Arrays.asList("01"));
	}
	
	private void addDataTxRetIndicatorDenominator(CohortIndicatorDataSetDefinition dsd, String indParams,
	        List<ColumnParameters> allColumns, int months) {
		EmrReportingUtils.addRow(dsd, "T04SIALL", "ACTIVO EM TARV",
		    ReportUtils.map(indicators.artStartInPeriodRetention(months), indParams), allColumns, Arrays.asList("01"));
		
		EmrReportingUtils.addRow(dsd, "T04SIPI", "ACTIVO EM TARV - GRAVIDA",
		    ReportUtils.map(indicators.artStartInPeriodPregnantsRetention(months), indParams), allColumns,
		    Arrays.asList("01"));
		
		EmrReportingUtils.addRow(dsd, "T04SIBI", "ACTIVO EM TARV - LACTANTE",
		    ReportUtils.map(indicators.artStartInPeriodBreastfeedingsRetention(months), indParams), allColumns,
		    Arrays.asList("01"));
		
		EmrReportingUtils.addRow(dsd, "T04SI0A11M", "ACTIVO EM TARV - CRIANCAS 0-11 MESES",
		    ReportUtils.map(indicators.artStartInPeriodByAgeRetention(0, 1, months), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T04SI1A9A", "ACTIVO EM TARV - CRIANCAS 1-9 ANOS",
		    ReportUtils.map(indicators.artStartInPeriodByAgeRetention(1, 10, months), indParams), allColumns,
		    Arrays.asList("01"));
		
		EmrReportingUtils.addRow(dsd, "T04SI1014M", "ACTIVO EM TARV - MASC 10-14",
		    ReportUtils.map(indicators.artStartInPeriodByGenderAndAgeRetention('M', 10, 15, months), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T04SI1519M", "ACTIVO EM TARV - MASC 15-19",
		    ReportUtils.map(indicators.artStartInPeriodByGenderAndAgeRetention('M', 15, 20, months), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T04SI2024M", "ACTIVO EM TARV - MASC 20-24",
		    ReportUtils.map(indicators.artStartInPeriodByGenderAndAgeRetention('M', 20, 25, months), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T04SI2529M", "ACTIVO EM TARV - MASC 25-29",
		    ReportUtils.map(indicators.artStartInPeriodByGenderAndAgeRetention('M', 25, 30, months), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T04SI3034M", "ACTIVO EM TARV - MASC 30-34",
		    ReportUtils.map(indicators.artStartInPeriodByGenderAndAgeRetention('M', 30, 35, months), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T04SI3539M", "ACTIVO EM TARV - MASC 35-40",
		    ReportUtils.map(indicators.artStartInPeriodByGenderAndAgeRetention('M', 35, 40, months), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T04SI4049M", "ACTIVO EM TARV - MASC 40-49",
		    ReportUtils.map(indicators.artStartInPeriodByGenderAndAgeRetention('M', 40, 50, months), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T04SI50MAM", "ACTIVO EM TARV - MASC 50+",
		    ReportUtils.map(indicators.artStartInPeriodByGenderAndAgeRetention('M', 50, 9999, months), indParams),
		    allColumns, Arrays.asList("01"));
		
		EmrReportingUtils.addRow(dsd, "T04SI1014F", "ACTIVO EM TARV - FEM 10-14",
		    ReportUtils.map(indicators.artStartInPeriodByGenderAndAgeRetention('F', 10, 15, months), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T04SI1519F", "ACTIVO EM TARV - FEM 15-19",
		    ReportUtils.map(indicators.artStartInPeriodByGenderAndAgeRetention('F', 15, 20, months), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T04SI2024F", "ACTIVO EM TARV - FEM 20-24",
		    ReportUtils.map(indicators.artStartInPeriodByGenderAndAgeRetention('F', 20, 25, months), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T04SI2529F", "ACTIVO EM TARV - FEM 25-29",
		    ReportUtils.map(indicators.artStartInPeriodByGenderAndAgeRetention('F', 25, 30, months), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T04SI3034F", "ACTIVO EM TARV - FEM 30-34",
		    ReportUtils.map(indicators.artStartInPeriodByGenderAndAgeRetention('F', 30, 35, months), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T04SI3539F", "ACTIVO EM TARV - FEM 35-39",
		    ReportUtils.map(indicators.artStartInPeriodByGenderAndAgeRetention('F', 35, 40, months), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T04SI4049F", "ACTIVO EM TARV - FEM 40-49",
		    ReportUtils.map(indicators.artStartInPeriodByGenderAndAgeRetention('F', 40, 50, months), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T04SI50MAF", "ACTIVO EM TARV - FEM 50+",
		    ReportUtils.map(indicators.artStartInPeriodByGenderAndAgeRetention('F', 50, 9999, months), indParams),
		    allColumns, Arrays.asList("01"));
	}
	
	private void addDataTxPvlsIndicatorNumerator(CohortIndicatorDataSetDefinition dsd, String indParams,
	        List<ColumnParameters> allColumns) {
		EmrReportingUtils.addRow(dsd, "T22CVIALL", "CARGA VIRAL INDETECTAVEL",
		    ReportUtils.map(indicators.patientsWithUndetectableViralLoadLast12Months(), indParams), allColumns,
		    Arrays.asList("01"));
		
		EmrReportingUtils.addRow(dsd, "T22CVIGRAV", "CARGA VIRAL INDETECTAVEL - GRAVIDA",
		    ReportUtils.map(indicators.patientsWithUndetectableViralLoadLast12MonthsPregnants(), indParams), allColumns,
		    Arrays.asList("01"));
		
		EmrReportingUtils.addRow(dsd, "T22CVILACT", "CARGA VIRAL INDETECTAVEL - LACTANTE",
		    ReportUtils.map(indicators.patientsWithUndetectableViralLoadLast12MonthsBreastfeedings(), indParams),
		    allColumns, Arrays.asList("01"));
		
		EmrReportingUtils.addRow(dsd, "T22CVI0A11M", "CARGA VIRAL INDETECTAVEL - CRIANCAS 0-11 MESES",
		    ReportUtils.map(indicators.patientsWithUndetectableViralLoadLast12MonthsByAge(0, 1), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T22CVI1A9A", "CARGA VIRAL INDETECTAVEL - CRIANCAS 1-9 ANOS",
		    ReportUtils.map(indicators.patientsWithUndetectableViralLoadLast12MonthsByAge(1, 10), indParams), allColumns,
		    Arrays.asList("01"));
		
		EmrReportingUtils.addRow(dsd, "T22CVI1014M", "CARGA VIRAL INDETECTAVEL - MASC 10-14",
		    ReportUtils.map(indicators.patientsWithUndetectableViralLoadLast12MonthsByGenderAndAge('M', 10, 15), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T22CVI1519M", "CARGA VIRAL INDETECTAVEL - MASC 15-19",
		    ReportUtils.map(indicators.patientsWithUndetectableViralLoadLast12MonthsByGenderAndAge('M', 15, 20), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T22CVI2024M", "CARGA VIRAL INDETECTAVEL - MASC 20-24",
		    ReportUtils.map(indicators.patientsWithUndetectableViralLoadLast12MonthsByGenderAndAge('M', 20, 25), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T22CVI2529M", "CARGA VIRAL INDETECTAVEL - MASC 25-29",
		    ReportUtils.map(indicators.patientsWithUndetectableViralLoadLast12MonthsByGenderAndAge('M', 25, 30), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T22CVI3034M", "CARGA VIRAL INDETECTAVEL - MASC 30-34",
		    ReportUtils.map(indicators.patientsWithUndetectableViralLoadLast12MonthsByGenderAndAge('M', 30, 35), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T22CVI3539M", "CARGA VIRAL INDETECTAVEL - MASC 35-40",
		    ReportUtils.map(indicators.patientsWithUndetectableViralLoadLast12MonthsByGenderAndAge('M', 35, 40), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T22CVI4049M", "CARGA VIRAL INDETECTAVEL - MASC 40-49",
		    ReportUtils.map(indicators.patientsWithUndetectableViralLoadLast12MonthsByGenderAndAge('M', 40, 50), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T22CVI50MAM", "CARGA VIRAL INDETECTAVEL - MASC 50+", ReportUtils.map(
		    indicators.patientsWithUndetectableViralLoadLast12MonthsByGenderAndAge('M', 50, 9999), indParams), allColumns,
		    Arrays.asList("01"));
		
		EmrReportingUtils.addRow(dsd, "T22CVI1014F", "CARGA VIRAL INDETECTAVEL - FEM 10-14",
		    ReportUtils.map(indicators.patientsWithUndetectableViralLoadLast12MonthsByGenderAndAge('F', 10, 15), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T22CVI1519F", "CARGA VIRAL INDETECTAVEL - FEM 15-19",
		    ReportUtils.map(indicators.patientsWithUndetectableViralLoadLast12MonthsByGenderAndAge('F', 15, 20), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T22CVI2024F", "CARGA VIRAL INDETECTAVEL - FEM 20-24",
		    ReportUtils.map(indicators.patientsWithUndetectableViralLoadLast12MonthsByGenderAndAge('F', 20, 25), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T22CVI2529F", "CARGA VIRAL INDETECTAVEL - FEM 25-29",
		    ReportUtils.map(indicators.patientsWithUndetectableViralLoadLast12MonthsByGenderAndAge('F', 25, 30), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T22CVI3034F", "CARGA VIRAL INDETECTAVEL - FEM 30-34",
		    ReportUtils.map(indicators.patientsWithUndetectableViralLoadLast12MonthsByGenderAndAge('F', 30, 35), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T22CVI3539F", "CARGA VIRAL INDETECTAVEL - FEM 35-39",
		    ReportUtils.map(indicators.patientsWithUndetectableViralLoadLast12MonthsByGenderAndAge('F', 35, 40), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T22CVI4049F", "CARGA VIRAL INDETECTAVEL - FEM 40-49",
		    ReportUtils.map(indicators.patientsWithUndetectableViralLoadLast12MonthsByGenderAndAge('F', 40, 50), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T22CVI50MAF", "CARGA VIRAL INDETECTAVEL - FEM 50+", ReportUtils.map(
		    indicators.patientsWithUndetectableViralLoadLast12MonthsByGenderAndAge('F', 50, 9999), indParams), allColumns,
		    Arrays.asList("01"));
	}
	
	private void addDataTxPvlsIndicatorDenominator(CohortIndicatorDataSetDefinition dsd, String indParams,
	        List<ColumnParameters> allColumns) {
		EmrReportingUtils.addRow(dsd, "T20CVALL", "CARGA VIRAL INDETECTAVEL",
		    ReportUtils.map(indicators.patientsWithViralLoadReportedLast12Months(), indParams), allColumns,
		    Arrays.asList("01"));
		
		EmrReportingUtils.addRow(dsd, "T20CVGRAV", "CARGA VIRAL INDETECTAVEL - GRAVIDA",
		    ReportUtils.map(indicators.patientsWithViralLoadReportedLast12MonthsPregnants(), indParams), allColumns,
		    Arrays.asList("01"));
		
		EmrReportingUtils.addRow(dsd, "T20CVLACT", "CARGA VIRAL INDETECTAVEL - LACTANTE",
		    ReportUtils.map(indicators.patientsWithViralLoadReportedLast12MonthsBreastfeedings(), indParams), allColumns,
		    Arrays.asList("01"));
		
		EmrReportingUtils.addRow(dsd, "T20CV0A11M", "CARGA VIRAL INDETECTAVEL - CRIANCAS 0-11 MESES",
		    ReportUtils.map(indicators.patientsWithViralLoadReportedLast12MonthsByAge(0, 1), indParams), allColumns,
		    Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T20CV1A9A", "CARGA VIRAL INDETECTAVEL - CRIANCAS 1-9 ANOS",
		    ReportUtils.map(indicators.patientsWithViralLoadReportedLast12MonthsByAge(1, 10), indParams), allColumns,
		    Arrays.asList("01"));
		
		EmrReportingUtils.addRow(dsd, "T20CV1014M", "CARGA VIRAL INDETECTAVEL - MASC 10-14",
		    ReportUtils.map(indicators.patientsWithViralLoadReportedLast12MonthsByGenderAndAge('M', 10, 15), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T20CV1519M", "CARGA VIRAL INDETECTAVEL - MASC 15-19",
		    ReportUtils.map(indicators.patientsWithViralLoadReportedLast12MonthsByGenderAndAge('M', 15, 20), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T20CV2024M", "CARGA VIRAL INDETECTAVEL - MASC 20-24",
		    ReportUtils.map(indicators.patientsWithViralLoadReportedLast12MonthsByGenderAndAge('M', 20, 25), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T20CV2529M", "CARGA VIRAL INDETECTAVEL - MASC 25-29",
		    ReportUtils.map(indicators.patientsWithViralLoadReportedLast12MonthsByGenderAndAge('M', 25, 30), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T20CV3034M", "CARGA VIRAL INDETECTAVEL - MASC 30-34",
		    ReportUtils.map(indicators.patientsWithViralLoadReportedLast12MonthsByGenderAndAge('M', 30, 35), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T20CV3539M", "CARGA VIRAL INDETECTAVEL - MASC 35-40",
		    ReportUtils.map(indicators.patientsWithViralLoadReportedLast12MonthsByGenderAndAge('M', 35, 40), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T20CV4049M", "CARGA VIRAL INDETECTAVEL - MASC 40-49",
		    ReportUtils.map(indicators.patientsWithViralLoadReportedLast12MonthsByGenderAndAge('M', 40, 50), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T20CV50MAM", "CARGA VIRAL INDETECTAVEL - MASC 50+",
		    ReportUtils.map(indicators.patientsWithViralLoadReportedLast12MonthsByGenderAndAge('M', 50, 9999), indParams),
		    allColumns, Arrays.asList("01"));
		
		EmrReportingUtils.addRow(dsd, "T20CV1014F", "CARGA VIRAL INDETECTAVEL - FEM 10-14",
		    ReportUtils.map(indicators.patientsWithViralLoadReportedLast12MonthsByGenderAndAge('F', 10, 15), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T20CV1519F", "CARGA VIRAL INDETECTAVEL - FEM 15-19",
		    ReportUtils.map(indicators.patientsWithViralLoadReportedLast12MonthsByGenderAndAge('F', 15, 20), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T20CV2024F", "CARGA VIRAL INDETECTAVEL - FEM 20-24",
		    ReportUtils.map(indicators.patientsWithViralLoadReportedLast12MonthsByGenderAndAge('F', 20, 25), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T20CV2529F", "CARGA VIRAL INDETECTAVEL - FEM 25-29",
		    ReportUtils.map(indicators.patientsWithViralLoadReportedLast12MonthsByGenderAndAge('F', 25, 30), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T20CV3034F", "CARGA VIRAL INDETECTAVEL - FEM 30-34",
		    ReportUtils.map(indicators.patientsWithViralLoadReportedLast12MonthsByGenderAndAge('F', 30, 35), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T20CV3539F", "CARGA VIRAL INDETECTAVEL - FEM 35-39",
		    ReportUtils.map(indicators.patientsWithViralLoadReportedLast12MonthsByGenderAndAge('F', 35, 40), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T20CV4049F", "CARGA VIRAL INDETECTAVEL - FEM 40-49",
		    ReportUtils.map(indicators.patientsWithViralLoadReportedLast12MonthsByGenderAndAge('F', 40, 50), indParams),
		    allColumns, Arrays.asList("01"));
		EmrReportingUtils.addRow(dsd, "T20CV50MAF", "CARGA VIRAL INDETECTAVEL - FEM 50+",
		    ReportUtils.map(indicators.patientsWithViralLoadReportedLast12MonthsByGenderAndAge('F', 50, 9999), indParams),
		    allColumns, Arrays.asList("01"));
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
