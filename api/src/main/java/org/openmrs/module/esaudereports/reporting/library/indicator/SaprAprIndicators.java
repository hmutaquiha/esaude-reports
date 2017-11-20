package org.openmrs.module.esaudereports.reporting.library.indicator;

import org.openmrs.module.esaudereports.reporting.library.cohort.SaprAprCohort;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
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
	
	/**
	 * Currently in treatment From the beginning that MISAU start TARV services (retira abandonos
	 * notificados e nao notificados)
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator currentlyOnARTExcludingLostToFollowUp() {
		return cohortIndicator(
		    "Pacientes actualmente em TARV desde que o inicio dos Servicos TARV até a data o perido final. A retirada de saida de abandono inclui abandonos notificados e não notificados",
		    map(cohort.currentlyOnARTExcludingLostToFollowUp(),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES ACTUALMENTE EM TARV E QUE ESTAO ACTIVOS NO GAAC
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator currentlyOnARTAndActiveOnGAAC() {
		return cohortIndicator(
		    "Numero de pacientes actualmente em TARV e que estão activos no GAAC",
		    map(cohort.patientsCurrentlyInARTAndActiveOnGAAC(),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES NA COORTE - INICIO
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator numberOfPatientsInCohort(String mappings) {
		return cohortIndicator("São pacientes que iniciaram tarv no periodo da coorte",
		    map(cohort.aRTStartIncludingTransfersFrom(), mappings));
	}
	
	/**
	 * NUMERO DE PACIENTES NA COORTE - ACTIVO
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator numberOfPatientsInCohortActive(String mappings) {
		return cohortIndicator(
		    "São pacientes que iniciaram TARV no período da coorte e que ainda estão activos",
		    map(cohort.patientsInCohortActive(cohort.aRTStartIncludingTransfersFrom(), mappings),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES NA COORTE - OBITO
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator numberOfPatientsInCohortDead(String mappings) {
		return cohortIndicator(
		    "São pacientes na coorte e que são óbitos",
		    map(cohort.patientsInCohortDead(cohort.aRTStartIncludingTransfersFrom(), mappings),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES NA COORTE - ABANDONO
	 * 
	 * @return CohortIndicator
	 * @param mappings
	 */
	public CohortIndicator numberOfPatientsInCohortLostToFollowUp(String mappings) {
		return cohortIndicator(
		    "São pacientes que estão na coorte e que são abandonos",
		    map(cohort.patientsInCohortLostToFollowUp(cohort.aRTStartIncludingTransfersFrom(), mappings),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES NA COORTE - TRANSFERIDOS PARA
	 * 
	 * @return CohortIndicator
	 * @param mappings
	 */
	public CohortIndicator numberOfPatientsInCohortTransferedTo(String mappings) {
		return cohortIndicator(
		    "São pacientes que estão na coorte e que foram transferidos para",
		    map(cohort.patientsInCohortTransferedTo(cohort.aRTStartIncludingTransfersFrom(), mappings),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES NA COORTE - SUSPENSO
	 * 
	 * @return CohortIndicator
	 * @param mappings
	 */
	public CohortIndicator numberOfPatientsInCohortSuspended(String mappings) {
		return cohortIndicator(
		    "São pacientes que estão na coorte e que foram suspenso",
		    map(cohort.patientsInCohortSuspended(cohort.aRTStartIncludingTransfersFrom(), mappings),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES NA COORTE - GRAVIDAS - INICIO
	 * 
	 * @return CohortIndicator
	 * @param mappings
	 */
	public CohortIndicator numberOfPregnantsInCohort(String mappings) {
		return cohortIndicator("São gravidas que iniciaram tarv no periodo da coorte de 6 meses",
		    map(cohort.preganantsWhoStartedART(), mappings));
	}
	
	/**
	 * NUMERO DE PACIENTES NA COORTE - GRAVIDAS - ACTIVO
	 * 
	 * @return CohortIndicator
	 * @param mappings
	 */
	public CohortIndicator numberOfPregnantsInCohortActive(String mappings) {
		return cohortIndicator(
		    "São gravidas que iniciaram TARV no período da coorte e que ainda estão activos",
		    map(cohort.patientsInCohortActive(cohort.preganantsWhoStartedART(), mappings),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES NA COORTE - GRAVIDAS - OBITO
	 * 
	 * @return CohortIndicator
	 * @param mappings
	 */
	public CohortIndicator numberOfPregnantsInCohortDead(String mappings) {
		return cohortIndicator(
		    "São gravidas na coorte e que são óbitos",
		    map(cohort.patientsInCohortDead(cohort.preganantsWhoStartedART(), mappings),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES NA COORTE - GRAVIDAS - ABANDONO
	 * 
	 * @return CohortIndicator
	 * @param mappings
	 */
	public CohortIndicator numberOfPregnantsInCohortLostToFollowUp(String mappings) {
		return cohortIndicator(
		    "São gravidas que estão na coorte e que são abandonos",
		    map(cohort.patientsInCohortLostToFollowUp(cohort.preganantsWhoStartedART(), mappings),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES NA COORTE - GRAVIDAS - TRANSFERIDOS PARA
	 * 
	 * @return CohortIndicator
	 * @param mappings
	 */
	public CohortIndicator numberOfPregnantsInCohortTransferedTo(String mappings) {
		return cohortIndicator(
		    "São gravidas que estão na coorte e que foram transferidos para",
		    map(cohort.patientsInCohortTransferedTo(cohort.preganantsWhoStartedART(), mappings),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES NA COORTE - GRAVIDAS - SUSPENSO
	 * 
	 * @return CohortIndicator
	 * @param mappings
	 */
	public CohortIndicator numberOfPregnantsInCohortSuspended(String mappings) {
		return cohortIndicator(
		    "São gravidas que estão na coorte e que foram suspenso",
		    map(cohort.patientsInCohortSuspended(cohort.preganantsWhoStartedART(), mappings),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES NA COORTE - LACTANTES - INICIO
	 * 
	 * @return CohortIndicator
	 * @param mappings
	 */
	public CohortIndicator numberOfBreastfeedingsInCohort(String mappings) {
		return cohortIndicator("São lactantes que iniciaram tarv no periodo da coorte de 6 meses",
		    map(cohort.breastfeedingsWhoStartedART(), mappings));
	}
	
	/**
	 * NUMERO DE PACIENTES NA COORTE - LACTANTES - ACTIVO
	 * 
	 * @return CohortIndicator
	 * @param mappings
	 */
	public CohortIndicator numberOfBreastfeedingsInCohortActive(String mappings) {
		return cohortIndicator(
		    "São lactantes que iniciaram TARV no período da coorte e que ainda estão activos",
		    map(cohort.patientsInCohortActive(cohort.breastfeedingsWhoStartedART(), mappings),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES NA COORTE - LACTANTES - OBITO
	 * 
	 * @return CohortIndicator
	 * @param mappings
	 */
	public CohortIndicator numberOfBreastfeedingsInCohortDead(String mappings) {
		return cohortIndicator(
		    "São lactantes na coorte e que são óbitos",
		    map(cohort.patientsInCohortDead(cohort.breastfeedingsWhoStartedART(), mappings),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES NA COORTE - LACTANTES - ABANDONO
	 * 
	 * @return CohortIndicator
	 * @param mappings
	 */
	public CohortIndicator numberOfBreastfeedingsInCohortLostToFollowUp(String mappings) {
		return cohortIndicator(
		    "São lactantes que estão na coorte e que são abandonos",
		    map(cohort.patientsInCohortLostToFollowUp(cohort.breastfeedingsWhoStartedART(), mappings),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES NA COORTE - LACTANTES - TRANSFERIDOS PARA
	 * 
	 * @return CohortIndicator
	 * @param mappings
	 */
	public CohortIndicator numberOfBreastfeedingsInCohortTransferedTo(String mappings) {
		return cohortIndicator(
		    "São lactantes que estão na coorte e que foram transferidos para",
		    map(cohort.patientsInCohortTransferedTo(cohort.breastfeedingsWhoStartedART(), mappings),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES NA COORTE - LACTANTES - SUSPENSO
	 * 
	 * @return CohortIndicator
	 * @param mappings
	 */
	public CohortIndicator numberOfBreastfeedingsInCohortSuspended(String mappings) {
		return cohortIndicator(
		    "São lactantes que estão na coorte e que foram suspenso",
		    map(cohort.patientsInCohortSuspended(cohort.breastfeedingsWhoStartedART(), mappings),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * Ever in treatment From the beginning that MISAU start TARV services
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator everInTreatment() {
		return cohortIndicator(
		    "Pacientes que alguma vez esteve em tratamento ARV desde o inicio do Servico TARV até a data final",
		    map(cohort.everBeenOnART(), "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES ACTUALEMTE EM TARV QUE ESTAO NA 2 LINHA - PERIODO FINAL (ABANDONO RETIRA
	 * NOTIFICADO E NAO NOTIFICADO)
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator patientsInARTSecondLineRegimen() {
		return cohortIndicator(
		    "Sao pacientes que actualmente estao em tarv e que estao na segunda linha de tratamento ARV ate um determinado periodo final (abandono retira notificado e nao notificado",
		    map(cohort.patientsCurrentlyOnARTSecondLineRegimen(),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES NA COORTE - ESTADO
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator numberOfPatientsWhoLeftARTProgram(int state) {
		CohortDefinition cohortDef = state == 7 ? cohort.patientsTransferedTo() : state == 8 ? cohort
		        .patientsSuspendedOnTreatment() : state == 9 ? cohort.patientsWhoLostToFollowUp() : cohort
		        .patientsWhoPassedAway();
		return cohortIndicator("São pacientes na coorte e que são state",
		    map(cohortDef, "endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES COM FALHAS CLINICAS
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator numberOfPatientsWithClinicTreatmentFailure() {
		return cohortIndicator(
		    "São pacientes com subida de estadio para III ou IV 6 meses apos inicio de TARV",
		    map(cohort.patientsInARTNotifiedClinicTreatmentFailure(),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES COM FALHAS IMUNOLOGICAS
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator numberOfPatientsWithImunologicTreatmentFailure() {
		return cohortIndicator(
		    "São pacientes notificados as falhas imunologias, isto é, baixo cd4 6 meses apos inicio de ARV ou continuamente abaixo de 100",
		    map(cohort.patientsInARTNotifiedImunologicTreatmentFailure(),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES COM FALHAS CLINICAS E IMUNOLOGICAS
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator numberOfPatientsWithTreatmentFailure() {
		return cohortIndicator(
		    "Sao pacientes actualmente em TARV e que foram notificados de falhas clinicas e imunologicas",
		    map(cohort.patientsInARTNotifiedTreatmentFailure(),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES ACTUALMENTE EM TARV E QUE ESTAO HA MAIS DE 6 MESES EM TRATAMENTO
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator numberOfPatientsInARTAndForMoreThan6MonthsInTreatment() {
		return cohortIndicator(
		    "São pacientes que estão actualmente em TARV e que estão há mais de seis meses em TARV",
		    map(cohort.currentlyInARTAndForMoreThan6MonthsInTreatment(),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES ACTUALMENTE EM TARV E QUE ESTAO HA MAIS DE 6 MESES EM TARV COM RESULTADO
	 * DE CARGA VIRAL NOS ULTIMOS 12 MESES
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator numberOfPatientsinARTAndForMoreThan6MonthsWithViralLoadResults() {
		return cohortIndicator(
		    "São pacientes actualmente em TARV há mais de 6 meses com carga viral registada nos ultimos 12 meses",
		    map(cohort.inARTAndForMoreThan6MonthsWithViralLoadResults(),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES COM RESULTADO DE CARGA VIRAL NOS ULTIMOS 12 MESES
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator numberOfPatientsWithViralLoadResultsLast12Months() {
		return cohortIndicator("São pacientes que tiveram resultado de carga viral nos ultimos 12 meses",
		    map(cohort.patientsWithViralLoadResultsLast12Months(), "endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES COM CARGA VIRAL INDETECTAVEL NOS ULTIMOS 12 MESES
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator numberOfPatientsWithUndetectableViralLoadLast12Months() {
		return cohortIndicator(
		    "São pacientes com resultado de carga viral indetectavel, isto é, carga viral menor que 1000 copias",
		    map(cohort.patientsWithUndetectableViralLoadLast12Months(), "endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES NOTIFICADOS COMO ESTANDO EM TRATAMENTO DE TUBERCULOSE E QUE INICIARAM O
	 * TARV
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator numberOfPatientsNotifiedTBTreatmentWhoStartedART() {
		return cohortIndicator(
		    "Pacientes que foram notificados como estando em tratamento de tuberculose e que iniciaram TARV",
		    map(cohort.patientsNotifiedTBTreatmentWhoStartedART(),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
}
