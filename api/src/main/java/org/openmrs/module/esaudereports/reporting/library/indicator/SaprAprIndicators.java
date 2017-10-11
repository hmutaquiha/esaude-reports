package org.openmrs.module.esaudereports.reporting.library.indicator;

import org.openmrs.module.esaudereports.reporting.library.cohort.SaprAprCohort;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.openmrs.module.esaudereports.reporting.utils.EmrReportingUtils.cohortIndicator;
import static org.openmrs.module.esaudereports.reporting.utils.ReportUtils.map;

/**
 * Created by Hamilton Mutaquiha on 8/23/17.
 */
@Component
public class SaprAprIndicators {
	
	@Autowired
	private SaprAprCohort cohort;
	
	/**
	 * In Care who start treatment From start period to end period
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator inCareWhoStartedTreatmentInPeriod() {
		return cohortIndicator(
		    "# of Individuals with advanced HIV infection newly enrolled on ART (Age at Start ART)",
		    map(cohort.aRTStartInPeriodExcludingTransfersFrom(),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
}
