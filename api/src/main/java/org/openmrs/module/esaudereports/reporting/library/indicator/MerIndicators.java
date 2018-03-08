package org.openmrs.module.esaudereports.reporting.library.indicator;

import org.openmrs.module.esaudereports.reporting.library.cohort.MerCohort;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.openmrs.module.esaudereports.reporting.utils.EmrReportingUtils.cohortIndicator;
import static org.openmrs.module.esaudereports.reporting.utils.ReportUtils.map;

/**
 * Created by Hamilton Mutaquiha on 2/16/18.
 */
@Component
public class MerIndicators {
	
	@Autowired
	private MerCohort cohort;
	
	/**
	 * Pacientes que iniciaram tarv num período
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator artStartInPeriod() {
		return cohortIndicator("# de pacientes que iniciaram tarv num período",
		    map(cohort.artStartInPeriod(), "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * Pacientes que iniciaram tarv num período por idade
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator artStartInPeriodByAge(Integer minAge, Integer maxAge) {
		return cohortIndicator(
		    "# de pacientes que iniciaram tarv num período por idade",
		    map(cohort.artStartInPeriodByAge(), "startDate=${startDate},endDate=${endDate},minAgeInclusive=" + minAge
		            + ",maxAgeExclusive=" + maxAge + ",location=${location}"));
	}
	
	/**
	 * Pacientes que iniciaram tarv num período por sexo e idade
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator artStartInPeriodByGenderAndAge(Character gender, Integer minAge, Integer maxAge) {
		return cohortIndicator(
		    "# de pacientes que iniciaram tarv num período por sexo e idade",
		    map(cohort.artStartInPeriodByGenderAndAge(), "startDate=${startDate},endDate=${endDate},gender=" + gender
		            + ",minAgeInclusive=" + minAge + ",maxAgeExclusive=" + maxAge + ",location=${location}"));
	}
	
	/**
	 * Pacientes grávidas que iniciaram tarv num período
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator artStartInPeriodPregnants() {
		return cohortIndicator("# de pacientes grávidas que iniciaram tarv num período",
		    map(cohort.artStartInPeriodPregnants(), "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * Pacientes lactantes que iniciaram tarv num período
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator artStartInPeriodBreastfeedings() {
		return cohortIndicator("# de pacientes lactantes que iniciaram tarv num período",
		    map(cohort.artStartInPeriodBreastfeedings(), "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * Pacientes que iniciaram tarv num período com confirmação de TB
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator artStartInPeriodWithTB() {
		return cohortIndicator("# de pacientes que iniciaram tarv num período com confirmação de TB",
		    map(cohort.artStartInPeriodWithTB(), "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * Pacientes em tarv num período
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator inTreatmentInPeriod() {
		return cohortIndicator("# de pacientes em tarv num período",
		    map(cohort.inTreatmentInPeriod(), "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * Pacientes em tarv num período por idade
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator inTreatmentInPeriodByAge(Integer minAge, Integer maxAge) {
		return cohortIndicator(
		    "# de pacientes em tarv num período por idade",
		    map(cohort.inTreatmentInPeriodByAge(), "startDate=${startDate},endDate=${endDate},minAgeInclusive=" + minAge
		            + ",maxAgeExclusive=" + maxAge + ",location=${location}"));
	}
	
	/**
	 * Pacientes em tarv num período por sexo e idade
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator inTreatmentInPeriodByGenderAndAge(Character gender, Integer minAge, Integer maxAge) {
		return cohortIndicator(
		    "# de pacientes em tarv num período por sexo e idade",
		    map(cohort.inTreatmentInPeriodByGenderAndAge(), "startDate=${startDate},endDate=${endDate},gender=" + gender
		            + ",minAgeInclusive=" + minAge + ",maxAgeExclusive=" + maxAge + ",location=${location}"));
	}
	
	/**
	 * Pacientes que iniciaram tarv num período
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator artStartInPeriodRetention(int months) {
		return cohortIndicator("# de pacientes que iniciaram tarv num período",
		    map(cohort.artStartInPeriodRetention(), "endDate=${endDate},location=${location},months=" + months));
	}
	
	/**
	 * Pacientes que iniciaram tarv num período por idade
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator artStartInPeriodByAgeRetention(Integer minAge, Integer maxAge, int months) {
		return cohortIndicator(
		    "# de pacientes que iniciaram tarv num período por idade",
		    map(cohort.artStartInPeriodByAgeRetention(), "endDate=${endDate},minAgeInclusive=" + minAge
		            + ",maxAgeExclusive=" + maxAge + ",location=${location},months=" + months));
	}
	
	/**
	 * Pacientes que iniciaram tarv num período por sexo e idade
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator artStartInPeriodByGenderAndAgeRetention(Character gender, Integer minAge, Integer maxAge,
	        int months) {
		return cohortIndicator(
		    "# de pacientes que iniciaram tarv num período por sexo e idade",
		    map(cohort.artStartInPeriodByGenderAndAgeRetention(), "endDate=${endDate},gender=" + gender
		            + ",minAgeInclusive=" + minAge + ",maxAgeExclusive=" + maxAge + ",location=${location},months=" + months));
	}
	
	/**
	 * Pacientes grávidas que iniciaram tarv num período
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator artStartInPeriodPregnantsRetention(int months) {
		return cohortIndicator("# de pacientes grávidas que iniciaram tarv num período",
		    map(cohort.artStartInPeriodPregnantsRetention(), "endDate=${endDate},location=${location},months=" + months));
	}
	
	/**
	 * Pacientes lactantes que iniciaram tarv num período
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator artStartInPeriodBreastfeedingsRetention(int months) {
		return cohortIndicator(
		    "# de pacientes lactantes que iniciaram tarv num período",
		    map(cohort.artStartInPeriodBreastfeedingsRetention(), "endDate=${endDate},location=${location},months=" + months));
	}
	
	/**
	 * Pacientes em tarv num período
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator inTreatmentInPeriodRetention(int months) {
		return cohortIndicator("# de pacientes em tarv num período",
		    map(cohort.inTreatmentInPeriodRetention(), "endDate=${endDate},location=${location},months=" + months));
	}
	
	/**
	 * Pacientes em tarv num período por idade
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator inTreatmentInPeriodByAgeRetention(Integer minAge, Integer maxAge, int months) {
		return cohortIndicator(
		    "# de pacientes em tarv num período por idade",
		    map(cohort.inTreatmentInPeriodByAgeRetention(), "endDate=${endDate},minAgeInclusive=" + minAge
		            + ",maxAgeExclusive=" + maxAge + ",location=${location},months=" + months));
	}
	
	/**
	 * Pacientes em tarv num período por sexo e idade
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator inTreatmentInPeriodByGenderAndAgeRetention(Character gender, Integer minAge, Integer maxAge,
	        int months) {
		return cohortIndicator(
		    "# de pacientes em tarv num período por sexo e idade",
		    map(cohort.inTreatmentInPeriodByGenderAndAgeRetention(), "endDate=${endDate},gender=" + gender
		            + ",minAgeInclusive=" + minAge + ",maxAgeExclusive=" + maxAge + ",location=${location},months=" + months));
	}
	
	/**
	 * Pacientes grávidas em tarv num período
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator inTreatmentInPeriodPregnantsRetention(int months) {
		return cohortIndicator("# de pacientes grávidas em tarv num período",
		    map(cohort.inTreatmentInPeriodPregnantsRetention(), "endDate=${endDate},location=${location},months=" + months));
	}
	
	/**
	 * Pacientes lactantes em tarv num período
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator inTreatmentInPeriodBreastfeedingsRetention(int months) {
		return cohortIndicator(
		    "# de pacientes lactantes em tarv num período",
		    map(cohort.inTreatmentInPeriodBreastfeedingsRetention(), "endDate=${endDate},location=${location},months="
		            + months));
	}
	
	/**
	 * Pacientes com carga viral reportada nos últimos 12 meses
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator patientsWithViralLoadReportedLast12Months() {
		return cohortIndicator("# de pacientes com carga viral reportada nos últimos 12 meses",
		    map(cohort.patientsWithViralLoadReportedLast12Months(), "endDate=${endDate},location=${location}"));
	}
	
	/**
	 * Pacientes com carga viral reportada nos últimos 12 meses por idade
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator patientsWithViralLoadReportedLast12MonthsByAge(Integer minAge, Integer maxAge) {
		return cohortIndicator(
		    "# de pacientes com carga viral reportada nos últimos 12 meses por idade",
		    map(cohort.patientsWithViralLoadReportedLast12MonthsByAge(), "endDate=${endDate},minAgeInclusive=" + minAge
		            + ",maxAgeExclusive=" + maxAge + ",location=${location}"));
	}
	
	/**
	 * Pacientes com carga viral reportada nos últimos 12 meses por sexo e idade
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator patientsWithViralLoadReportedLast12MonthsByGenderAndAge(Character gender, Integer minAge,
	        Integer maxAge) {
		return cohortIndicator(
		    "# de pacientes com carga viral reportada nos últimos 12 meses por sexo e idade",
		    map(cohort.patientsWithViralLoadReportedLast12MonthsByGenderAndAge(), "endDate=${endDate},gender=" + gender
		            + ",minAgeInclusive=" + minAge + ",maxAgeExclusive=" + maxAge + ",location=${location}"));
	}
	
	/**
	 * Pacientes grávidas com carga viral reportada nos últimos 12 meses
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator patientsWithViralLoadReportedLast12MonthsPregnants() {
		return cohortIndicator("# de pacientes grávidas com carga viral reportada nos últimos 12 meses",
		    map(cohort.patientsWithViralLoadReportedLast12MonthsPregnants(), "endDate=${endDate},location=${location}"));
	}
	
	/**
	 * Pacientes lactantes com carga viral reportada nos últimos 12 meses
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator patientsWithViralLoadReportedLast12MonthsBreastfeedings() {
		return cohortIndicator("# de pacientes lactantes com carga viral reportada nos últimos 12 meses",
		    map(cohort.patientsWithViralLoadReportedLast12MonthsBreastfeedings(), "endDate=${endDate},location=${location}"));
	}
	
	/**
	 * Pacientes com carga viral indetectavel nos últimos 12 meses
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator patientsWithUndetectableViralLoadLast12Months() {
		return cohortIndicator("# de pacientes com carga viral indetectavel nos últimos 12 meses",
		    map(cohort.patientsWithUndetectableViralLoadLast12Months(), "endDate=${endDate},location=${location}"));
	}
	
	/**
	 * Pacientes com carga viral indetectavel nos últimos 12 meses por idade
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator patientsWithUndetectableViralLoadLast12MonthsByAge(Integer minAge, Integer maxAge) {
		return cohortIndicator(
		    "# de pacientes com carga viral indetectavel nos últimos 12 meses por idade",
		    map(cohort.patientsWithUndetectableViralLoadLast12MonthsByAge(), "endDate=${endDate},minAgeInclusive=" + minAge
		            + ",maxAgeExclusive=" + maxAge + ",location=${location}"));
	}
	
	/**
	 * Pacientes com carga viral indetectavel nos últimos 12 meses por sexo e idade
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator patientsWithUndetectableViralLoadLast12MonthsByGenderAndAge(Character gender, Integer minAge,
	        Integer maxAge) {
		return cohortIndicator(
		    "# de pacientes com carga viral indetectavel nos últimos 12 meses por sexo e idade",
		    map(cohort.patientsWithUndetectableViralLoadLast12MonthsByGenderAndAge(), "endDate=${endDate},gender=" + gender
		            + ",minAgeInclusive=" + minAge + ",maxAgeExclusive=" + maxAge + ",location=${location}"));
	}
	
	/**
	 * Pacientes grávidas com carga viral indetectavel nos últimos 12 meses
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator patientsWithUndetectableViralLoadLast12MonthsPregnants() {
		return cohortIndicator("# de pacientes grávidas com carga viral indetectavel nos últimos 12 meses",
		    map(cohort.patientsWithUndetectableViralLoadLast12MonthsPregnants(), "endDate=${endDate},location=${location}"));
	}
	
	/**
	 * Pacientes lactantes com carga viral indetectavel nos últimos 12 meses
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator patientsWithUndetectableViralLoadLast12MonthsBreastfeedings() {
		return cohortIndicator(
		    "# de pacientes lactantes com carga viral indetectavel nos últimos 12 meses",
		    map(cohort.patientsWithUndetectableViralLoadLast12MonthsBreastfeedings(),
		        "endDate=${endDate},location=${location}"));
	}
}
