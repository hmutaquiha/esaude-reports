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
	
	/**
	 * PACIENTES NOTIFICADOS DO TRATAMENTO DE TB NO SERVICO TARV - ACTIVOS EM TARV
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator patientsActiveInARTNotifiedTBTreatmentOnARTService() {
		return cohortIndicator(
		    "PACIENTES NOTIFICADOS DO TRATAMENTO DE TB NO SERVICO TARV - ACTIVOS EM TARV",
		    map(cohort.patientsActiveInARTNotifiedTBTreatmentOnARTService(),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * PACIENTES NOTIFICADOS DO TRATAMENTO DE TB NO SERVICO TARV - NOVOS INICIOS
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator patientsNotifiedTBTreatmentOnARTServiceWhoStartedART() {
		return cohortIndicator(
		    "PACIENTES NOTIFICADOS DO TRATAMENTO DE TB NO SERVICO TARV - NOVOS INICIOS",
		    map(cohort.patientsNotifiedTBTreatmentOnARTServiceWhoStartedART(),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES ACTUALMENTE EM TARV COM RASTREIO DE TUBERCULOSE POSITIVO NUM DETERMINADO
	 * PERIODO
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator numberOfpatientsActiveInARTWithPositiveTBTracking() {
		return cohortIndicator(
		    "NUMERO DE PACIENTES ACTUALMENTE EM TARV COM RASTREIO DE TUBERCULOSE POSITIVO NUM DETERMINADO PERIODO",
		    map(cohort.patientsActiveInARTWithPositiveTBTracking(),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES ACTUALMENTE EM TARV COM RASTREIO DE TUBERCULOSE NEGATIVO NUM DETERMINADO
	 * PERIODO
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator numberOfPatientsActiveInARTWithNegativeTBTracking() {
		return cohortIndicator(
		    "NUMERO DE PACIENTES ACTUALMENTE EM TARV COM RASTREIO DE TUBERCULOSE NEGATIVO NUM DETERMINADO PERIODO",
		    map(cohort.patientsActiveInARTWithNegativeTBTracking(),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * INICIO DE TARV E COM RASTREIO TB POSITIVO - NUM PERIODO: EXCLUI TRANSFERIDOS DE COM DATA DE
	 * INICIO CONHECIDA
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator numberOfPatientsWithPositiveTBTrackingWhoStartedART() {
		return cohortIndicator(
		    "INICIO DE TARV E COM RASTREIO TB POSITIVO - NUM PERIODO: EXCLUI TRANSFERIDOS DE COM DATA DE INICIO CONHECIDA ",
		    map(cohort.patientsWithPositiveTBTrackingWhoStartedART(),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * INICIO DE TARV E COM RASTREIO TB NEGATIVO - NUM PERIODO: EXCLUI TRANSFERIDOS DE COM DATA DE
	 * INICIO CONHECIDA
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator numberOfPatientsWithNegativeTBTrackingWhoStartedART() {
		return cohortIndicator(
		    "INICIO DE TARV E COM RASTREIO TB NEGATIVO - NUM PERIODO: EXCLUI TRANSFERIDOS DE COM DATA DE INICIO CONHECIDA",
		    map(cohort.patientsWithNegativeTBTrackingWhoStartedART(),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES QUE ALGUMA VEZ ESTIVERAM EM TARV COM RASTREIO DE TB POSITIVO E TIVERAM
	 * TESTE DE DIAGNOSTICO DE TB (BK ou RX) POSITIVO
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator numberOfPatientsEverInARTWithPositiveTBTrackingWhoHadPositiveDiagnoseTest() {
		return cohortIndicator(
		    "NUMERO DE PACIENTES QUE ALGUMA VEZ ESTIVERAM EM TARV COM RASTREIO DE TB POSITIVO E TIVERAM TESTE DE DIAGNOSTICO DE TB (BK ou RX) POSITIVO",
		    map(cohort.patientsEverInARTWithPositiveTBTrackingWhoHadPositiveDiagnoseTest(),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES QUE ALGUMA VEZ ESTIVERAM EM TARV COM RASTREIO DE TB POSITIVO E TIVERAM
	 * TESTE DE DIAGNOSTICO DE TB (BK ou RX) NEGATIVO
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator numberOfPatientsEverInARTWithPositiveTBTrackingWhoHadNegativeDiagnoseTest() {
		return cohortIndicator(
		    "NUMERO DE PACIENTES QUE ALGUMA VEZ ESTIVERAM EM TARV COM RASTREIO DE TB POSITIVO E TIVERAM TESTE DE DIAGNOSTICO DE TB (BK ou RX) NEGATIVO",
		    map(cohort.patientsEverInARTWithPositiveTBTrackingWhoHadNegativeDiagnoseTest(),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES TARV QUE INICIARAM E TERMINARAM PROFILAXIA COM ISONIAZIDA O TERMINO DEVE
	 * TER DATA FIM PREENCHIDA
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator numberOfPatientsWhoStartedAndFinishedProphilaxyWithIsoniazida() {
		return cohortIndicator(
		    "NUMERO DE PACIENTES TARV QUE INICIARAM E TERMINARAM PROFILAXIA COM ISONIAZIDA O TERMINO DEVE TER DATA FIM PREENCHIDA",
		    map(cohort.patientsWhoStartedAndFinishedProphilaxyWithIsoniazida(),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES TARV QUE INICIARAM E TERMINARAM PROFILAXIA COM ISONIAZIDA O TERMINO DEVE
	 * TER DATA FIM PREENCHIDA: INICIO TPI PERIODO ANTERIOR
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator numberOfPatientsWhoStartedAndFinishedProphilaxyWithIsoniazidaPreviousPeriod() {
		return cohortIndicator(
		    "NUMERO DE PACIENTES TARV QUE INICIARAM E TERMINARAM PROFILAXIA COM ISONIAZIDA O TERMINO DEVE TER DATA FIM PREENCHIDA: INICIO TPI PERIODO ANTERIOR",
		    map(cohort.patientsWhoStartedAndFinishedProphilaxyWithIsoniazidaPreviousPeriod(),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES TARV QUE INICIARAM E CONTINUAM EM PROFILAXIA COM ISONIZIDA: INICIOU E NÃO
	 * TEM DATA FIM
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator numberOfPatientsWhoStartedAndContinueProphilaxyWithIsoniazida() {
		return cohortIndicator(
		    "NUMERO DE PACIENTES TARV QUE INICIARAM E CONTINUAM EM PROFILAXIA COM ISONIZIDA: INICIOU E NÃO TEM DATA FIM",
		    map(cohort.patientsWhoStartedAndContinueProphilaxyWithIsoniazida(),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES TAV QUE INICIARAM E CONTINUAM EM PROFILAXIA COM ISONIZIDA: INICIOU E NÃO
	 * TEM DATA FIM - INICIO TPI PERIODO ANTERIOR
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator numberOfPatientsWhoStartedAndContinueProphilaxyWithIsoniazidaPreviousPeriod() {
		return cohortIndicator(
		    "NUMERO DE PACIENTES TAV QUE INICIARAM E CONTINUAM EM PROFILAXIA COM ISONIZIDA: INICIOU E NÃO TEM DATA FIM - INICIO TPI PERIODO ANTERIOR",
		    map(cohort.patientsWhoStartedAndContinueProphilaxyWithIsoniazidaPreviousPeriod(),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES QUE ALGUMA VEZ INICIOU TARV E QUE INICIOU A PROFILAXIA COM TPI NUM
	 * DETERMINADO PERIODO
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator numberOfPatientsEverStartedARTAndStartedProphilaxyWithIsoniazida() {
		return cohortIndicator(
		    "NUMERO DE PACIENTES QUE ALGUMA VEZ INICIOU TARV E QUE INICIOU A PROFILAXIA COM TPI NUM DETERMINADO PERIODO",
		    map(cohort.patientsEverStartedARTAndStartedProphilaxyWithIsoniazida(),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES QUE ALGUMA VEZ INICIOU TARV E QUE INICIOU A PROFILAXIA COM TPI: PERIODO
	 * ANTERIOR - 6 MESES
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator numberOfPatientsEverStartedARTAndStartedProphilaxyWithIsoniazidaPreviousPeriod() {
		return cohortIndicator(
		    "NUMERO DE PACIENTES QUE ALGUMA VEZ INICIOU TARV E QUE INICIOU A PROFILAXIA COM TPI: PERIODO ANTERIOR - 6 MESES",
		    map(cohort.patientsEverStartedARTAndStartedProphilaxyWithIsoniazida(),
		        "startDate=${startDate-6m},endDate=${startDate-1d},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES QUE INICIARAM TARV - NUM PERIODO: MES ANTERIOR
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator numberOfPatientsWhoStartedARTLastMonth() {
		return cohortIndicator(
		    "NUMERO DE PACIENTES QUE INICIARAM TARV - NUM PERIODO: MES ANTERIOR",
		    map(cohort.aRTStartInPeriodExcludingTransfersFrom(),
		        "startDate=${startDate-29d},endDate=${startDate-1d},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES QUE INICIARAM TARV DENTRO DE 30 DIAS DEPOIS DE DECLARADAS ELEGIVEIS: MES
	 * ANTERIOR
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator numberOfPatientsWhoStartedARTLastMonth(int days) {
		return cohortIndicator(
		    "NUMERO DE PACIENTES QUE INICIARAM TARV DENTRO DE 30 DIAS DEPOIS DE DECLARADAS ELEGIVEIS: MES ANTERIOR",
		    map(cohort.patientsWhoStartedARTXDaysAfterDeclaredIlegible(),
		        "startDate=${startDate-29d},endDate=${startDate-1d},location=${location},days=" + days));
	}
	
	/**
	 * PACIENTES COM RESULTADO DE CARGA VIRAL NUM DETERMINADO PERIODO
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator patientsWithViralLoadResultsInPeriod() {
		return cohortIndicator(
		    "PACIENTES COM RESULTADO DE CARGA VIRAL NUM DETERMINADO PERIODO",
		    map(cohort.patientsWithViralLoadResultsDocumentedInPeriod(),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES COM CARGA VIRAL INDETECTAVEL (CV<1000): NUM DETERMINADO PERIODO
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator patientsWithUndetectableViralLoadInPeriod() {
		return cohortIndicator(
		    "NUMERO DE PACIENTES COM CARGA VIRAL INDETECTAVEL (CV<1000): NUM DETERMINADO PERIODO",
		    map(cohort.patientsWithUndetectableViralLoadInPeriod(),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES ELEGIVEIS A CARGA VIRAL DE ROTINA: SITIOS TESTAR E INICIAR - SEM INCLUIR
	 * ELEGIVEIS POR FALENCIA
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator numberOfPatientsIligebleToRotineViralLoadTestStartSitesExcludingTherapeuticFailure() {
		return cohortIndicator(
		    "NUMERO DE PACIENTES ELEGIVEIS A CARGA VIRAL DE ROTINA: SITIOS TESTAR E INICIAR - SEM INCLUIR ELEGIVEIS POR FALENCIA",
		    map(cohort.CONSULTA_CRIANCAS_MAIS_2_ANOS_MAIS_6_MESES_TARV(),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES ELEGIVEIS A CARGA VIRAL DE ROTINA: SITIOS NAO TESTAR E INICIAR - SEM
	 * INCLUIR ELEGIVEIS POR FALENCIA
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator numberOfPatientsIligebleToRotineViralLoadNotTestStartSitesExcludingTherapeuticFailure() {
		return cohortIndicator(
		    "NUMERO DE PACIENTES ELEGIVEIS A CARGA VIRAL DE ROTINA: SITIOS NAO TESTAR E INICIAR - SEM INCLUIR ELEGIVEIS POR FALENCIA",
		    map(cohort.patientsIligebleToRotineViralLoadTestNotStartSitesExcludingTherapeuticFailure(),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE GRÁVIDAS ELEGIVEIS A CARGA VIRAL
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator numberOfPregnantsIligebleToViralLoad() {
		return cohortIndicator("NUMERO DE GRÁVIDAS ELEGIVEIS A CARGA VIRAL",
		    map(cohort.pregnantsIligebleToViralLoad(), "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
	
	/**
	 * NUMERO DE PACIENTES ELEGIVEIS A CARGA VIRAL POR SUSPEITA DE FALENCIA TERAPEUTICA - TARGETED
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator numberOfPatientsIligebleToViralLoadForTherapeuticFailureSuspect() {
		return cohortIndicator(
		    "NUMERO DE PACIENTES ELEGIVEIS A CARGA VIRAL POR SUSPEITA DE FALENCIA TERAPEUTICA - TARGETED",
		    map(cohort.patientsIligebleToViralLoadForTherapeuticFailureSuspect(),
		        "startDate=${startDate},endDate=${endDate},location=${location}"));
	}
}
