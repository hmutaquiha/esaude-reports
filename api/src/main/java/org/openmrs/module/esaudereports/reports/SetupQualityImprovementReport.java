/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.esaudereports.reports;

import org.openmrs.Location;
import org.openmrs.module.esaudereports.EsaudeDataExportManager;
import org.openmrs.module.esaudereports.reporting.library.dimension.CommonDimension;
import org.openmrs.module.esaudereports.reporting.library.indicator.QualityImprovementIndicators;
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
 * Created by Nicholas Ingosi on 7/27/17.
 */
@Component
public class SetupQualityImprovementReport extends EsaudeDataExportManager {
	
	@Autowired
	private QualityImprovementIndicators indicators;
	
	@Autowired
	private CommonDimension commonDimension;
	
	/**
	 * @return the uuid for the report design for exporting to Excel
	 */
	@Override
	public String getExcelDesignUuid() {
		return "c200541e-72ce-11e7-b45c-507b9dc4c741";
	}
	
	@Override
	public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
		List<ReportDesign> l = new ArrayList<ReportDesign>();
		l.add(buildReportDesign(reportDefinition));
		return l;
	}
	
	/**
	 * Build the report design for the specified report, this allows a user to override the report
	 * design by adding properties and other metadata to the report design
	 * 
	 * @param reportDefinition
	 * @return The report design
	 */
	@Override
	public ReportDesign buildReportDesign(ReportDefinition reportDefinition) {
		return createExcelTemplateDesign(getExcelDesignUuid(), reportDefinition, "QualityImprovement.xls");
	}
	
	@Override
	public String getUuid() {
		return "d1275b54-72ce-11e7-8b29-507b9dc4c741";
	}
	
	@Override
	public String getName() {
		return "MELHORIA DE QUALIDADE";
	}
	
	@Override
	public String getDescription() {
		return "Ficha de Relatório de Desempenho dos Indicadores da Unidade Sanitária";
	}
	
	@Override
	public ReportDefinition constructReportDefinition() {
		ReportDefinition rd = new ReportDefinition();
		rd.setUuid(getUuid());
		rd.setName(getName());
		rd.setDescription(getDescription());
		rd.addParameters(getParameters());
		rd.addDataSetDefinition("Q", Mapped.mapStraightThrough(dataSetDefinition()));
		return rd;
	}
	
	@Override
	public String getVersion() {
		return "0.1";
	}
	
	private DataSetDefinition dataSetDefinition() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setParameters(getParameters());
		dsd.setName("Q");
		
		String indParams = "startDate=${startDate},endDate=${endDate},location=${location},revisionEndDate=${revisionEndDate}";
		//add dimensions to the dsd
		dsd.addDimension("age", ReportUtils.map(commonDimension.dimForQualityImprovement(), "onDate=${endDate}"));
		
		//bulid the column parameters here
		ColumnParameters children = new ColumnParameters("0-14", "0-14", "age=0-14");
		ColumnParameters adults = new ColumnParameters("15+", "15+", "age=15+");
		ColumnParameters total = new ColumnParameters("Total", "Total", "");
		
		//form columns as list to be used in the dsd
		List<ColumnParameters> allColumns = Arrays.asList(children, adults, total);
		
		//build the design here, all patients in the system with different aggregation
		
		EmrReportingUtils.addRow(dsd, "2N", "", ReportUtils.map(indicators.clinicalConsulationsNumerator(), indParams),
		    allColumns, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "2D", "", ReportUtils.map(indicators.clinicalConsulationsDenominator(), indParams),
		    allColumns, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "3N1", "",
		    ReportUtils.map(indicators.followUpClinicalConsulationsNumerator1(), indParams), allColumns,
		    Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "3N2", "",
		    ReportUtils.map(indicators.followUpClinicalConsulationsNumerator2(), indParams), allColumns,
		    Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "3N3", "",
		    ReportUtils.map(indicators.followUpClinicalConsulationsNumerator3(), indParams), allColumns,
		    Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "3D", "", ReportUtils.map(indicators.followUpCConsulationsDenominator(), indParams),
		    allColumns, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "4N1", "", ReportUtils.map(indicators.clinicalProcessFillNumerator1(), indParams),
		    allColumns, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "4N2", "", ReportUtils.map(indicators.clinicalProcessFillNumerator2(), indParams),
		    allColumns, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "4D1", "", ReportUtils.map(indicators.clinicalProcessFillDenominator(), indParams),
		    allColumns, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "4D2", "", ReportUtils.map(indicators.clinicalConsulationsDenominator(), indParams),
		    allColumns, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "7N1", "", ReportUtils.map(indicators.tbTrackingNumerator1(), indParams), allColumns,
		    Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "7N2", "", ReportUtils.map(indicators.tbTrackingNumerator1(), indParams), allColumns,
		    Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "7N3", "", ReportUtils.map(indicators.tbTrackingNumerator3(), indParams), allColumns,
		    Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "7D1", "", ReportUtils.map(indicators.tbTrackingDenominator1(), indParams),
		    allColumns, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "7D2", "", ReportUtils.map(indicators.tbTrackingDenominator1(), indParams),
		    allColumns, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "7D3", "", ReportUtils.map(indicators.tbTrackingDenominator3(), indParams),
		    allColumns, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "8N1", "", ReportUtils.map(indicators.iTSTrackingNumerator1(), indParams), allColumns,
		    Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "8D1", "", ReportUtils.map(indicators.iTSTrackingDenominator1(), indParams),
		    allColumns, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "11N1", "", ReportUtils.map(indicators.wHOStateNumerator1(), indParams), allColumns,
		    Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "11N2", "", ReportUtils.map(indicators.wHOStateNumerator2(), indParams), allColumns,
		    Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "11D1", "", ReportUtils.map(indicators.wHOStateDenominator1(), indParams), allColumns,
		    Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "11D2", "", ReportUtils.map(indicators.wHOStateDenominator1(), indParams), allColumns,
		    Arrays.asList("01", "02", "03"));
		return dsd;
	}
	
	@Override
	public List<Parameter> getParameters() {
		return Arrays.asList(new Parameter("startDate", "Data Inicial Inclusão", Date.class), new Parameter("endDate",
		        "Data Final Inclusão", Date.class), new Parameter("revisionEndDate", "Data Final Revisão", Date.class),
		    new Parameter("location", "Unidade Sanitária", Location.class));
	}
}
