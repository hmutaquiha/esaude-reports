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

import org.openmrs.module.esaudereports.reporting.library.cohort.CommonCohortLibrary;
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
public class EsaudeReportIndicators {
	
	@Autowired
	private CommonCohortLibrary cohortLibrary;
	
	/**
	 * All patients in the database
	 */
	public CohortIndicator allPatients() {
		return cohortIndicator("all patients",
		    map(cohortLibrary.allPatients(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	}
	
	/**
	 * All male as a fraction of all patients
	 */
	public CohortIndicator allMalesVsAllPatients() {
		return cohortIndicator("male over all patients", map(cohortLibrary.males(), ""),
		    map(cohortLibrary.allPatients(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	}
	
	/**
	 * All male as a fraction of all patients
	 */
	public CohortIndicator allFemalseVsAllPatients() {
		return cohortIndicator("male over all patients", map(cohortLibrary.females(), ""),
		    map(cohortLibrary.allPatients(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	}
}
