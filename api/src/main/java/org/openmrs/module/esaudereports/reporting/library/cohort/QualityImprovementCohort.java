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

import org.openmrs.Location;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by Nicholas Ingosi on 7/27/17.
 */
@Component
public class QualityImprovementCohort {
	
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
}
