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
package org.openmrs.module.esaudereports.reporting.library.indicator;

import org.openmrs.module.esaudereports.reporting.library.cohort.QualityImprovementCohort;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.openmrs.module.esaudereports.reporting.utils.EmrReportingUtils.cohortIndicator;
import static org.openmrs.module.esaudereports.reporting.utils.ReportUtils.map;

/**
 * Created by Nicholas Ingosi on 7/20/17. All indicators require parameters ${startDate} and
 * ${endDate}
 */
@Component
public class QualityImprovementIndicators {
	
	@Autowired
	private QualityImprovementCohort cohort;
	
	/**
	 * Clinical consultation numerator
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator clinicalConsulationsNumerator() {
		return cohortIndicator(
		    "Initial clinical consultations numerator",
		    map(cohort.clinicalConsulationsNumerator(),
		        "startDate=${startDate},endDate=${endDate},location=${location},revisionEndDate=${revisionEndDate}"));
	}
	
	/**
	 * Clinical consultation numerator
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator clinicalConsulationsDenominator() {
		return cohortIndicator(
		    "Initial clinical consultations denominator",
		    map(cohort.clinicalConsulationsDenominator(),
		        "startDate=${startDate},endDate=${endDate},location=${location},revisionEndDate=${revisionEndDate}"));
	}
	
	/**
	 * Follow up clinical consultation numerator
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator followUpClinicalConsulationsNumerator1() {
		return cohortIndicator(
		    "Follow up clinical consultations numerator",
		    map(cohort.followUpClinicalConsulationsNumerator1(),
		        "startDate=${startDate},endDate=${endDate},location=${location},revisionEndDate=${revisionEndDate}"));
	}
	
	/**
	 * Follow up clinical consultation numerator
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator followUpClinicalConsulationsNumerator2() {
		return cohortIndicator(
		    "Follow up clinical consultations numerator",
		    map(cohort.followUpClinicalConsulationsNumerator1(),
		        "startDate=${startDate},endDate=${endDate},location=${location},revisionEndDate=${revisionEndDate}"));
	}
	
	/**
	 * Follow up clinical consultation numerator
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator followUpClinicalConsulationsNumerator3() {
		return cohortIndicator(
		    "Follow up clinical consultations numerator",
		    map(cohort.followUpClinicalConsulationsNumerator3(),
		        "startDate=${startDate},endDate=${endDate},location=${location},revisionEndDate=${revisionEndDate}"));
	}
	
	/**
	 * Follow up clinical consultation denominator
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator followUpCConsulationsDenominator() {
		return cohortIndicator(
		    "Follow up consultations denominator",
		    map(cohort.followUpClinicalConsulationsDenominator(),
		        "startDate=${startDate},endDate=${endDate},location=${location},revisionEndDate=${revisionEndDate}"));
	}
	
	/**
	 * Clinical process fill numerator
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator clinicalProcessFillNumerator1() {
		return cohortIndicator(
		    "Clinical process fill numerator",
		    map(cohort.clinicalProcessFillNumerator1(),
		        "startDate=${startDate},endDate=${endDate},location=${location},revisionEndDate=${revisionEndDate}"));
	}
	
	/**
	 * Clinical process fill numerator
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator clinicalProcessFillNumerator2() {
		return cohortIndicator(
		    "Clinical process fill numerator",
		    map(cohort.clinicalProcessFillNumerator2(),
		        "startDate=${startDate},endDate=${endDate},location=${location},revisionEndDate=${revisionEndDate}"));
	}
	
	/**
	 * Clinical process fill denominator
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator clinicalProcessFillDenominator() {
		return cohortIndicator(
		    "Clinical process fill denominator",
		    map(cohort.clinicalProcessFillDenominator(),
		        "startDate=${startDate},endDate=${endDate},location=${location},revisionEndDate=${revisionEndDate}"));
	}
	
	/**
	 * TB tracking numerator
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator tbTrackingNumerator1() {
		return cohortIndicator(
		    "TB tracking numerator",
		    map(cohort.tBTrackingNumerator1(),
		        "startDate=${startDate},endDate=${endDate},location=${location},revisionEndDate=${revisionEndDate}"));
	}
	
	/**
	 * TB tracking numerator
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator tbTrackingNumerator3() {
		return cohortIndicator("TB tracking numerator",
		    map(cohort.tBTrackingNumerator3(), "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * TB tracking denominator
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator tbTrackingDenominator1() {
		return cohortIndicator(
		    "TB tracking denominator",
		    map(cohort.tBTrackingDenominator1(),
		        "startDate=${startDate},endDate=${endDate},location=${location},revisionEndDate=${revisionEndDate}"));
	}
	
	/**
	 * TB tracking denominator
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator tbTrackingDenominator3() {
		return cohortIndicator("TB tracking denominator",
		    map(cohort.tBTrackingDenominator3(), "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * ITS tracking numerator
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator iTSTrackingNumerator1() {
		return cohortIndicator(
		    "ITS tracking numerator",
		    map(cohort.iTSTrackingNumerator1(),
		        "startDate=${startDate},endDate=${endDate},location=${location},revisionEndDate=${revisionEndDate}"));
	}
	
	/**
	 * ITS tracking denominator
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator iTSTrackingDenominator1() {
		return cohortIndicator(
		    "ITS tracking denominator",
		    map(cohort.iTSTrackingDenominator1(),
		        "startDate=${startDate},endDate=${endDate},location=${location},revisionEndDate=${revisionEndDate}"));
	}
	
	/**
	 * WHO state numerator
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator wHOStateNumerator1() {
		return cohortIndicator(
		    "WHO state numerator",
		    map(cohort.wHOStateNumerator1(),
		        "startDate=${startDate},endDate=${endDate},location=${location},revisionEndDate=${revisionEndDate}"));
	}
	
	/**
	 * WHO state numerator
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator wHOStateNumerator2() {
		return cohortIndicator(
		    "WHO state numerator",
		    map(cohort.wHOStateNumerator2(),
		        "startDate=${startDate},endDate=${endDate},location=${location},revisionEndDate=${revisionEndDate}"));
	}
	
	/**
	 * WHO state denominator
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator wHOStateDenominator1() {
		return cohortIndicator(
		    "WHO state numerator",
		    map(cohort.wHOStateDenominator1(),
		        "startDate=${startDate},endDate=${endDate},location=${location},revisionEndDate=${revisionEndDate}"));
	}
}
