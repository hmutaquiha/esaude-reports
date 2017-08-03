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
package org.openmrs.module.esaudereports.reporting.library.cohort;

import org.openmrs.Concept;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.api.PatientSetService;
import org.openmrs.module.esaudereports.reporting.cohort.definition.DateObsValueBetweenCohortDefinition;
import org.openmrs.module.esaudereports.reporting.metadata.Dictionary;
import org.openmrs.module.esaudereports.reporting.metadata.Metadata;
import org.openmrs.module.esaudereports.reporting.utils.CoreUtils;
import org.openmrs.module.esaudereports.reporting.utils.ReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by Nicholas Ingosi on 7/27/17.
 */
@Component
public class QualityImprovementCohort {
	@Autowired
	private CommonCohortLibrary cohortLibrary;

	//encounter types
	EncounterType ADULTO_INICIAL_A = CoreUtils.getEncounterType(Metadata._EncounterType.ADULTO_INICIAL_A);
	EncounterType ADULTO_SEGUIMENTO = CoreUtils.getEncounterType(Metadata._EncounterType.ADULTO_SEGUIMENTO);
	EncounterType PEDIATRIA_INICIAL_A = CoreUtils.getEncounterType(Metadata._EncounterType.PEDIATRIA_INICIAL_A);
	EncounterType PEDIATRIA_SEGUIMENTO = CoreUtils.getEncounterType(Metadata._EncounterType.PEDIATRIA_SEGUIMENTO);
	EncounterType FARMACIA = CoreUtils.getEncounterType(Metadata._EncounterType.FARMACIA);

	//concepts
	Concept arvPlan = Dictionary.getConcept(Metadata._Concept.ANTIRETROVIRAL_PLAN);
	Concept arvDrugUsed = Dictionary.getConcept(Metadata._Concept.PREVIOUS_ANTIRETROVIRAL_DRUGS_USED_FOR_TREATMENT);
	Concept pregnant = Dictionary.getConcept(Metadata._Concept.PREGNANT);
	Concept gestation = Dictionary.getConcept(Metadata._Concept.GESTATION);
	
	/**
	 * Clinical consultation numerator
	 * 
	 * @return CohortDefinition
	 */
	public CohortDefinition clinicalConsulationsNumerator() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "";
		cd.setName("Clinical consultation numerator");
		cd.addParameter(new Parameter("startDate", "Start date", Date.class));
		cd.addParameter(new Parameter("endDate", "End date", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(sqlQuery);
		return cd;
	}
	
	/**
	 * Clinical consultation denominator
	 * 
	 * @return CohortDefinition
	 */
	public CohortDefinition clinicalConsulationsDenominator() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		String sqlQuery = "";
		cd.setName("Clinical consultation denominator");
		cd.addParameter(new Parameter("startDate", "Start date", Date.class));
		cd.addParameter(new Parameter("endDate", "End date", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(sqlQuery);
		return cd;
	}

	/**
	 * Patients based on encounters and programs
	 * @return CohortDefinition
	 */
	public CohortDefinition hasEncountersAndInProgramNumeratorSecondPart(){
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("onOrAfter", "Start date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "End date", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));

		cd.addSearch("encounter5and7", ReportUtils.map(cohortLibrary.hasEncounter(ADULTO_INICIAL_A, PEDIATRIA_INICIAL_A), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore},location=${location}"));
		cd.addSearch("inProgram", ReportUtils.map(cohortLibrary.enrolled(CoreUtils.getProgram(Metadata._Program.SERVICO_TARV_CUIDADO)), "enrolledOnOrAfter=${onOrAfter},enrolledOnOrBefore=${onOrBefore}"));
		cd.addSearch("encounter6and9", ReportUtils.map(cohortLibrary.hasEncounter(ADULTO_SEGUIMENTO, PEDIATRIA_SEGUIMENTO), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore},location=${location}"));
		cd.setCompositionString("(encounter5and7 OR inProgram) AND encounter6and9");
		return cd;
	}

	/**
	 * Patients based on encounters and programs
	 * @return CohortDefinition
	 */
	public CohortDefinition hasEncountersAndInProgramNumeratorFirstPart(){
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("onOrAfter", "Start date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "End date", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));

		//declare encounters types

		cd.addSearch("encountersPart1", ReportUtils.map(hasEncountersAndInProgramNumeratorSecondPart(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore},location=${location}"));
		cd.addSearch("hasObs", ReportUtils.map(cohortLibrary.hasObs(), "enrolledOnOrAfter=${onOrAfter},enrolledOnOrBefore=${onOrBefore}"));

		cd.setCompositionString("(encounter5and7 OR inProgram) AND encounter6and9");
		return cd;
	}

	public CohortDefinition hasEncountersAndObs(){
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("onOrAfter", "Start date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "End date", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("encounter186and9", ReportUtils.map(cohortLibrary.hasEncounter(FARMACIA,ADULTO_SEGUIMENTO, PEDIATRIA_SEGUIMENTO), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore},location=${location}"));
		cd.addSearch("arvPlan", ReportUtils.map(cohortLibrary.hasObs(arvPlan), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		cd.addSearch("arvDrugUsed", ReportUtils.map(cohortLibrary.hasObs(arvDrugUsed), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		cd.setCompositionString("encounter186and9 AND (arvPlan OR arvDrugUsed)");
		return cd;
	}

	public CohortDefinition hasEncountersAndValueDateObs(){
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("onOrAfter", "Start date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "End date", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));

		DateObsValueBetweenCohortDefinition dcd = new DateObsValueBetweenCohortDefinition();
		dcd.addParameter(new Parameter("onOrAfter", "Start date", Date.class));
		dcd.addParameter(new Parameter("onOrBefore", "End date", Date.class));
		dcd.setQuestion(Dictionary.getConcept(Metadata._Concept.HISTORICAL_DRUG_START_DATE));
		dcd.setTimeModifier(PatientSetService.TimeModifier.ANY);

		cd.addSearch("encounter186and9", ReportUtils.map(cohortLibrary.hasEncounter(FARMACIA,ADULTO_SEGUIMENTO, PEDIATRIA_SEGUIMENTO), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore},location=${location}"));
		cd.addSearch("valueDateObs", ReportUtils.map(dcd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		cd.setCompositionString("encounter186and9 AND valueDateObs");
		return cd;
	}

	public CohortDefinition hasEncounterAndInProgram() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("onOrAfter", "Start date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "End date", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("encounter18", ReportUtils.map(cohortLibrary.hasEncounter(FARMACIA), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore},location=${location}"));
		cd.addSearch("inProgram2", ReportUtils.map(cohortLibrary.enrolled(CoreUtils.getProgram(Metadata._Program.SERVICO_TARV_TRATAMENTO)), "enrolledOnOrAfter=${onOrAfter},enrolledOnOrBefore=${onOrBefore}"));
		cd.addSearch("inProgram1", ReportUtils.map(cohortLibrary.enrolled(CoreUtils.getProgram(Metadata._Program.SERVICO_TARV_CUIDADO)), "enrolledOnOrAfter=${onOrAfter},enrolledOnOrBefore=${onOrBefore}"));
		cd.addSearch("inProgram8", ReportUtils.map(cohortLibrary.enrolled(CoreUtils.getProgram(Metadata._Program.PTV_ETV)), "enrolledOnOrAfter=${onOrAfter},enrolledOnOrBefore=${onOrBefore}"));
		cd.setCompositionString("encounter18 OR inProgram2 OR inProgram1 OR inProgram8");
		return cd;
	}

	public CohortDefinition hasEncounterPregnantAndNumberOfWeeks(){
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("onOrAfter", "Start date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "End date", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("encounter5and6", ReportUtils.map(cohortLibrary.hasEncounter(ADULTO_INICIAL_A, ADULTO_SEGUIMENTO), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore},location=${location}"));
		cd.addSearch("isPregnant", ReportUtils.map(cohortLibrary.hasObs(pregnant, gestation), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		cd.addSearch("pregnancyweeks", ReportUtils.map(cohortLibrary.hasObs(pregnant, gestation), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		cd.setCompositionString("(encounter5and6 AND isPregnant) OR (encounter5and6 AND pregnancyweeks)");
		return cd;
	}

	public CohortDefinition notIncluded(){

	}
}
