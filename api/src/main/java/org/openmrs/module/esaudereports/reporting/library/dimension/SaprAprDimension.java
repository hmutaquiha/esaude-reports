package org.openmrs.module.esaudereports.reporting.library.dimension;

import org.openmrs.Location;
import org.openmrs.module.esaudereports.reporting.library.cohort.SaprAprCohort;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.indicator.dimension.CohortDefinitionDimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static org.openmrs.module.esaudereports.reporting.utils.ReportUtils.map;

/**
 * Created by administrator on 8/23/17.
 */
@Component
public class SaprAprDimension {
	
	@Autowired
	private SaprAprCohort saprAprCohort;
	
	/**
	 * Dimensions of age for children and adults for quality improvement report
	 * 
	 * @return CohortDefinitionDimension
	 */
	public CohortDefinitionDimension dimForSaprApr() {
		CohortDefinitionDimension dim = new CohortDefinitionDimension();
		dim.setName("DIMENSAO APR");
		dim.setDescription("Dimensão para indicadores do APR: Data Inicial: grávidas, Lactante, DAM e DAG");
		dim.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		dim.addParameter(new Parameter("endDate", "Data Final", Date.class));
		dim.addParameter(new Parameter("location", "Location", Location.class));
		dim.addCohortDefinition("L",
		    map(saprAprCohort.breastFeedingOrPuerpueras(), "startDate=${startDate},endDate=${endDate},location=${location}"));
		dim.addCohortDefinition(
		    "G",
		    map(saprAprCohort.pregnantsInscribedOnARTService(),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
		dim.addCohortDefinition("C0T14_START_DATE",
		    map(saprAprCohort.children0To14Years(), "endDate=${endDate},location=${location}"));
		dim.addCohortDefinition("A15+_START_DATE",
		    map(saprAprCohort.adults15PlusYears(), "endDate=${endDate},location=${location}"));
		dim.addCohortDefinition("M<1_START_DATE",
		    map(saprAprCohort.yearStartedART(), "endDate=${endDate},location=${location},minAge=0,maxAge=1,gender=M"));
		dim.addCohortDefinition("M0T14_START_DATE",
		    map(saprAprCohort.yearStartedART(), "endDate=${endDate},location=${location},minAge=0,maxAge=15,gender=M"));
		dim.addCohortDefinition("M1T4_START_DATE",
		    map(saprAprCohort.yearStartedART(), "endDate=${endDate},location=${location},minAge=1,maxAge=5,gender=M"));
		dim.addCohortDefinition("M1T9_START_DATE",
		    map(saprAprCohort.yearStartedART(), "endDate=${endDate},location=${location},minAge=1,maxAge=10,gender=M"));
		dim.addCohortDefinition("M5T14_START_DATE",
		    map(saprAprCohort.yearStartedART(), "endDate=${endDate},location=${location},minAge=5,maxAge=15,gender=M"));
		dim.addCohortDefinition("M10T14_START_DATE",
		    map(saprAprCohort.yearStartedART(), "endDate=${endDate},location=${location},minAge=10,maxAge=15,gender=M"));
		dim.addCohortDefinition("M15T19_START_DATE",
		    map(saprAprCohort.yearStartedART(), "endDate=${endDate},location=${location},minAge=15,maxAge=20,gender=M"));
		dim.addCohortDefinition("M15+_START_DATE",
		    map(saprAprCohort.yearStartedART(), "endDate=${endDate},location=${location},minAge=15,maxAge=500,gender=M"));
		dim.addCohortDefinition("M20T24_START_DATE",
		    map(saprAprCohort.yearStartedART(), "endDate=${endDate},location=${location},minAge=20,maxAge=25,gender=M"));
		dim.addCohortDefinition("M25T49_START_DATE",
		    map(saprAprCohort.yearStartedART(), "endDate=${endDate},location=${location},minAge=25,maxAge=50,gender=M"));
		dim.addCohortDefinition("M50+_START_DATE",
		    map(saprAprCohort.yearStartedART(), "endDate=${endDate},location=${location},minAge=50,maxAge=500,gender=M"));
		dim.addCohortDefinition("F<1_START_DATE",
		    map(saprAprCohort.yearStartedART(), "endDate=${endDate},location=${location},minAge=0,maxAge=1,gender=F"));
		dim.addCohortDefinition("F0T14_START_DATE",
		    map(saprAprCohort.yearStartedART(), "endDate=${endDate},location=${location},minAge=0,maxAge=15,gender=F"));
		dim.addCohortDefinition("F1T4_START_DATE",
		    map(saprAprCohort.yearStartedART(), "endDate=${endDate},location=${location},minAge=1,maxAge=5,gender=F"));
		dim.addCohortDefinition("F1T9_START_DATE",
		    map(saprAprCohort.yearStartedART(), "endDate=${endDate},location=${location},minAge=1,maxAge=10,gender=F"));
		dim.addCohortDefinition("F5T14_START_DATE",
		    map(saprAprCohort.yearStartedART(), "endDate=${endDate},location=${location},minAge=5,maxAge=15,gender=F"));
		dim.addCohortDefinition("F10T14_START_DATE",
		    map(saprAprCohort.yearStartedART(), "endDate=${endDate},location=${location},minAge=10,maxAge=15,gender=F"));
		dim.addCohortDefinition("F15T19_START_DATE",
		    map(saprAprCohort.yearStartedART(), "endDate=${endDate},location=${location},minAge=15,maxAge=20,gender=F"));
		dim.addCohortDefinition("F15+_START_DATE",
		    map(saprAprCohort.yearStartedART(), "endDate=${endDate},location=${location},minAge=15,maxAge=500,gender=F"));
		dim.addCohortDefinition("F20T24_START_DATE",
		    map(saprAprCohort.yearStartedART(), "endDate=${endDate},location=${location},minAge=20,maxAge=25,gender=F"));
		dim.addCohortDefinition("F25T49_START_DATE",
		    map(saprAprCohort.yearStartedART(), "endDate=${endDate},location=${location},minAge=25,maxAge=50,gender=F"));
		dim.addCohortDefinition("F50+_START_DATE",
		    map(saprAprCohort.yearStartedART(), "endDate=${endDate},location=${location},minAge=50,maxAge=500,gender=F"));
		dim.addCohortDefinition("DETECTABLEVL",
		    map(saprAprCohort.patientsWithDetectableViralLoadLast12Months(), "endDate=${endDate},location=${location}"));
		dim.addCohortDefinition("UNDETECTABLEVL",
		    map(saprAprCohort.patientsWithUndetectableViralLoadLast12Months(), "endDate=${endDate},location=${location}"));
		dim.addCohortDefinition(
		    "CVROTINA",
		    map(saprAprCohort.pregnantsBreastfeedingAndChildrenWithViralLoadResultLast12Months(),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
		dim.addCohortDefinition(
		    "CVTARGETED",
		    map(saprAprCohort.nonPregnantsBreastfeedingAndChildrenWithViralLoadResultLast12Months(),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
		dim.addCohortDefinition(
		    "GAAC",
		    map(saprAprCohort.patientsCurrentlyInARTAndActiveOnGAAC(),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
		
		return dim;
	}
	
}
