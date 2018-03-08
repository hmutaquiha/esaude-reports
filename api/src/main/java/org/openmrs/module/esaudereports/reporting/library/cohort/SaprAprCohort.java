package org.openmrs.module.esaudereports.reporting.library.cohort;

import org.openmrs.Concept;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.api.PatientSetService;
import org.openmrs.module.esaudereports.reporting.library.queries.sapr.apr.CohortQueries;
import org.openmrs.module.esaudereports.reporting.metadata.Dictionary;
import org.openmrs.module.esaudereports.reporting.metadata.Metadata;
import org.openmrs.module.esaudereports.reporting.utils.CoreUtils;
import org.openmrs.module.esaudereports.reporting.utils.ReportUtils;
import org.openmrs.module.reporting.cohort.definition.*;
import org.openmrs.module.reporting.common.RangeComparator;
import org.openmrs.module.reporting.common.SetComparator;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by Hamilton Mutaquiha on 8/23/17.
 */
@Component
public class SaprAprCohort {
	
	@Autowired
	private CommonCohortLibrary cohortLibrary;
	
	public CohortDefinition aRTStartIncludingTransfersFrom() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("INICIO DE TRATAMENTO ARV - NUM PERIODO: INCLUI TRANSFERIDOS DE COM DATA DE INICIO CONHECIDA (SQL)");
		cd.setDescription("Paciente que iniciou o tratamento ARV (Na unidade sanitaria seleccionada). São inclusos os transferidos de com data de inicio conhecida");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(CohortQueries.ART_START_IN_PERIOD_INCLUDE_TRANSFERS_FROM);
		
		return cd;
	}
	
	public CohortDefinition patientsTransferedFromOnARTProgram() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PROGRAMA: PACIENTES TRANSFERIDOS DE NO PROGRAMA DE TRATAMENTO ARV: NUM PERIODO");
		cd.setDescription("São pacientes que entraram no programa de tratamento ARV num periodo vindos transferidos de ");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(CohortQueries.PATIENTS_TRANFERED_FROM_ON_ART_TRAEATMENT_PROGRAM_IN_PERIOD);
		
		return cd;
	}
	
	public CohortDefinition aRTStartInPeriodExcludingTransfersFrom() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("INICIO DE TRATAMENTO ARV - NUM PERIODO: EXCLUI TRANSFERIDOS DE COM DATA DE INICIO CONHECIDA (SQL)");
		cd.setDescription("Sao pacientes que iniciaram tratamento ARV num periodo excluindo os transferidos de com a data de inicio conhecida e mesmo que coincida no periodo");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("INICIO", ReportUtils.map(aRTStartIncludingTransfersFrom(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("TRANSFDEPRG", ReportUtils.map(patientsTransferedFromOnARTProgram(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.setCompositionString("INICIO NOT TRANSFDEPRG");
		return cd;
	}
	
	public CohortDefinition patientsWithDateOfBirthUpdatedOnARTService() {
		DateObsCohortDefinition cd = new DateObsCohortDefinition();
		
		EncounterType ADULTO_SEGUIMENTO = CoreUtils.getEncounterType(Metadata._EncounterType.ADULTO_SEGUIMENTO_6);
		
		EncounterType ADULTO_INICIAL_A = CoreUtils.getEncounterType(Metadata._EncounterType.ADULTO_INICIAL_A_5);
		
		cd.setName("PACIENTES COM DATA DE PARTO ACTUALIZADO NO SERVICO TARV");
		cd.setDescription("Sao pacientes com data de parto actualizado no servico tarv. Repare que os parametros 'Data Inicial' e 'Data Final' refere-se a data de parto e nao data de registo (actualizacao)");
		cd.setQuestion(Dictionary.getConcept(Dictionary.DATE_OF_DELIVERY));
		cd.setEncounterTypeList(Arrays.asList(ADULTO_SEGUIMENTO, ADULTO_INICIAL_A));
		cd.setTimeModifier(PatientSetService.TimeModifier.ANY);
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setOperator1(RangeComparator.GREATER_EQUAL);
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.setOperator2(RangeComparator.LESS_EQUAL);
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		
		return cd;
	}
	
	public CohortDefinition aRTStartForBeingBreastfeeding() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		
		cd.setName("INICIO DE TARV POR SER LACTANTE");
		cd.setDescription("São pacientes que iniciaram TARV por serem lactantes. Conceito 6334");
		cd.addParameter(new Parameter("location", "Unidade Sanitaria", Location.class));
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addSearch("hasEncounter", ReportUtils.map(
		    cohortLibrary.hasEncounter(CoreUtils.getEncounterType(Metadata._EncounterType.ADULTO_SEGUIMENTO_6)),
		    "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));
		cd.addSearch("hasObs", ReportUtils.map(cohortLibrary.hasObs(
		    Dictionary.getConcept(Dictionary.CRITERIA_FOR_ART_START), PatientSetService.TimeModifier.FIRST,
		    Dictionary.getConcept(Dictionary.BREASTFEEDING)), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		cd.setCompositionString("hasObs AND hasEncounter");
		
		return cd;
	}
	
	public CohortDefinition pregnantsInscribedOnARTService() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("GRAVIDAS INSCRITAS NO SERVIÇO TARV");
		cd.setDescription("São pacientes que estão gravidas durante a abertura do processo ou durante o seguimento no serviço TARV e que foi notificado como nova gravidez durante o seguimemento.");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(CohortQueries.PREGNANTS_INSCRIBED_ON_ART_SERVICE);
		
		return cd;
	}
	
	public CohortDefinition patientsWhoGaveBirthTwoYearsAgo() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PROGRAMA: PACIENTES QUE DERAM PARTO HÁ DOIS ANOS ATRÁS DA DATA DE REFERENCIA - LACTANTES");
		cd.setDescription("São pacientes inscritos no programa de PTV e que foram actualizados como parto num periodo de 2 anos atrás da data de referencia");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(CohortQueries.PATIENTS_WHO_GAVE_BIRTH_TWO_YEARS_AGO);
		
		return cd;
	}
	
	public CohortDefinition registeredBreastFeeding() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("LACTANTES REGISTADAS");
		cd.setDescription("São pacientes que foram actualizados como lactantes na ficha de seguimento");
		cd.addParameter(new Parameter("location", "Unidade Sanitaria", Location.class));
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addSearch("hasEncounter", ReportUtils.map(
		    cohortLibrary.hasEncounter(CoreUtils.getEncounterType(Metadata._EncounterType.ADULTO_SEGUIMENTO_6)),
		    "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));
		cd.addSearch("hasObs", ReportUtils.map(cohortLibrary.hasObs(Dictionary.getConcept(Dictionary.BREASTFEEDING),
		    PatientSetService.TimeModifier.LAST, Dictionary.getConcept(Dictionary.YES)),
		    "onOrAfter=${startDate},onOrBefore=${endDate}"));
		cd.setCompositionString("hasObs AND hasEncounter");
		
		return cd;
	}
	
	public CohortDefinition breastFeedingOrPuerpueras() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("LACTANTES OU PUERPUERAS (POS-PARTO) REGISTADAS: PROCESSO CLINICO E FICHA DE SEGUIMENTO");
		cd.setDescription("São pacientes puerpueras ou lactantes registadas. O registo pode ser na ficha de seguimento ou no processo clinico durante a abertura de processo ");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("FEMININO", ReportUtils.map(cohortLibrary.females(), ""));
		cd.addSearch("DILLNG", ReportUtils.map(
		    DATAPARTO_OR_INICIOLACTANTE_OR_LACTANTEPROGRAMA_OR_LACTANTE_AND_NOT_GRAVIDAS(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.setCompositionString("FEMININO AND DILLNG");
		
		return cd;
	}
	
	public CohortDefinition dil() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setDescription("dil");//comprise of DATAPARTO OR INICIOLACTANTE OR LACTANTEPROGRAMA
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("DATAPARTO", ReportUtils.map(patientsWithDateOfBirthUpdatedOnARTService(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("INICIOLACTANTE", ReportUtils.map(aRTStartForBeingBreastfeeding(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("LACTANTEPROGRAMA",
		    ReportUtils.map(patientsWhoGaveBirthTwoYearsAgo(), "startDate=${startDate},location=${location}"));
		
		cd.setCompositionString("DATAPARTO OR INICIOLACTANTE OR LACTANTEPROGRAMA");
		return cd;
		
	}
	
	public CohortDefinition DATAPARTO_OR_INICIOLACTANTE_OR_LACTANTEPROGRAMA_OR_LACTANTE() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("LACTANTE",
		    ReportUtils.map(registeredBreastFeeding(), "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("DIL", ReportUtils.map(dil(), "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.setCompositionString("DIL OR LACTANTE");
		return cd;
	}
	
	public CohortDefinition DATAPARTO_OR_INICIOLACTANTE_OR_LACTANTEPROGRAMA_OR_LACTANTE_AND_NOT_GRAVIDAS() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("DILL", ReportUtils.map(DATAPARTO_OR_INICIOLACTANTE_OR_LACTANTEPROGRAMA_OR_LACTANTE(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("GRAVIDAS", ReportUtils.map(pregnantsInscribedOnARTService(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.setCompositionString("DILL AND NOT GRAVIDAS");
		return cd;
	}
	
	public CohortDefinition currentlyOnARTExcludingLostToFollowUp() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("TARV",
		    ReportUtils.map(currentlyOnART(), "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("NAONOTIFICADO", ReportUtils.map(lostToFollowUp(), "endDate=${endDate},location=${location}"));
		cd.setCompositionString("TARV AND NOT NAONOTIFICADO");
		return cd;
	}
	
	public CohortDefinition currentlyOnART() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("ALGUMAVEZTARV",
		    ReportUtils.map(everBeenOnART(), "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("SAIDAPROGRAMA",
		    ReportUtils.map(patientsWhoLeftARTProgram(), "endDate=${endDate},location=${location}"));
		cd.setCompositionString("ALGUMAVEZTARV AND NOT SAIDAPROGRAMA");
		return cd;
	}
	
	public CohortDefinition everBeenOnART() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("CONCEITO1255", ReportUtils.map(CONCEITO1255_OR_PROGRAMA_OR_CONCEITODATA(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("FRIDAFILA", ReportUtils.map(
		    cohortLibrary.hasEncounter(CoreUtils.getEncounterType(Metadata._EncounterType.FARMACIA_18)),
		    "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));
		cd.setCompositionString("CONCEITO1255 OR FRIDAFILA");
		return cd;
	}
	
	public CohortDefinition CONCEITO1255_OR_PROGRAMA_OR_CONCEITODATA() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("CONCEITO1255",
		    ReportUtils.map(haveBeenInART(), "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("PROGRAMA", ReportUtils.map(patientsInscribedOnARTProgram(), "endDate=${endDate},location=${location}"));
		cd.addSearch("CONCEITODATA",
		    ReportUtils.map(aRTStartUsingDateConcept(), "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.setCompositionString("CONCEITO1255 OR PROGRAMA OR CONCEITODATA");
		return cd;
	}
	
	public CohortDefinition haveBeenInART() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		EncounterType adultoSeguimento = CoreUtils.getEncounterType(Metadata._EncounterType.ADULTO_SEGUIMENTO_6);
		EncounterType pediatriaSeguimento = CoreUtils.getEncounterType(Metadata._EncounterType.PEDIATRIA_SEGUIMENTO_9);
		EncounterType farmacia = CoreUtils.getEncounterType(Metadata._EncounterType.FARMACIA_18);
		Concept start = Dictionary.getConcept(Dictionary.START);
		Concept transferedFrom = Dictionary.getConcept(Dictionary.TRANSFERED_FROM);
		Concept artManagement = Dictionary.getConcept(Metadata._Concept.ART_MANAGEMENT);
		
		cd.setName("ALGUMA VEZ ESTEVE EM TRATAMENTO ARV - PERIODO FINAL");
		cd.setDescription("Pacientes que alguma vez esteve em tratamento ARV (Iniciou ou veio transferido de outra us em TARV) até um determinado periodo final");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("hasEncounters", ReportUtils.map(
		    cohortLibrary.hasEncounter(adultoSeguimento, pediatriaSeguimento, farmacia),
		    "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));
		cd.addSearch("hasObs", ReportUtils.map(
		    cohortLibrary.hasObs(artManagement, PatientSetService.TimeModifier.ANY, start, transferedFrom),
		    "onOrAfter=${startDate},onOrBefore=${endDate}"));
		cd.setCompositionString("hasObs AND hasEncounters");
		
		return cd;
	}
	
	public CohortDefinition patientsInscribedOnARTProgram() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PROGRAMA: PACIENTES INSCRITOS NO PROGRAMA TRATAMENTO ARV (TARV) - PERIODO FINAL");
		cd.setDescription("Sao pacientes inscritos no programa de tratamento ARV até um determinado periodo final");
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(CohortQueries.PATIENTS_INSCRIBED_ON_ART_PROGRAM);
		
		return cd;
	}
	
	public CohortDefinition aRTStartUsingDateConcept() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		
		EncounterType adultoSeguimento = CoreUtils.getEncounterType(Metadata._EncounterType.ADULTO_SEGUIMENTO_6);
		EncounterType pediatriaSeguimento = CoreUtils.getEncounterType(Metadata._EncounterType.PEDIATRIA_SEGUIMENTO_9);
		EncounterType farmacia = CoreUtils.getEncounterType(Metadata._EncounterType.FARMACIA_18);
		Concept artStartDAte = Dictionary.getConcept(Dictionary.ART_START_DATE);
		
		cd.setName("INICIO DE TARV USANDO O CONCEITO DE DATA - PERIODO FINAL");
		cd.setDescription("São pacientes que iniciaram TARV registado no conceito 'Data de Inicio de TARV' na ficha de seguimento");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("hasEncounters", ReportUtils.map(
		    cohortLibrary.hasEncounter(adultoSeguimento, pediatriaSeguimento, farmacia),
		    "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));
		cd.addSearch("hasObs", ReportUtils.map(cohortLibrary.hasObs(artStartDAte, PatientSetService.TimeModifier.ANY),
		    "onOrAfter=${startDate},onOrBefore=${endDate}"));
		cd.setCompositionString("hasObs AND hasEncounters");
		
		return cd;
	}
	
	public CohortDefinition patientsWhoLeftARTProgram() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PROGRAMA: PACIENTES QUE SAIRAM DO PROGRAMA DE TRATAMENTO ARV: PERIODO FINAL");
		cd.setDescription("ão pacientes que desde a ultima data marcada para levantamento até a data final passam mais de 60 dias sem voltar e que ainda não foram notificados como Abandono.");
		cd.addParameter(new Parameter("endDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(CohortQueries.PATIENTS_WHO_LEFT_ART_PROGRAM);
		
		return cd;
	}
	
	public CohortDefinition lostToFollowUp() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("ABANDONO NÃO NOTIFICADO - TARV");
		cd.setDescription("São pacientes que sairam do programa de tratamento ARV até um determinado periodo final. Inclui todo tipo de saída: ABANDONO, OBITO, TRANSFERIDO PARA e SUSPENSO");
		cd.addParameter(new Parameter("endDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(CohortQueries.LOST_TO_FOLLOW_UP);
		
		return cd;
	}
	
	public CohortDefinition patientsCurrentlyInARTAndActiveOnGAAC() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES ACTUALMENTE EM TARV E QUE ESTAO ACTIVOS NO GAAC");
		cd.setDescription("São pacientes que estão actualmente em tratamento ARV e que estão activos em algum grupo GAAC");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("TARV", ReportUtils.map(currentlyOnARTExcludingLostToFollowUp(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("GAAC", ReportUtils.map(patientsActiveOnGAAC(), "endDate=${endDate},location=${location}"));
		cd.setCompositionString("TARV AND GAAC");
		return cd;
	}
	
	public CohortDefinition patientsActiveOnGAAC() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES ACTIVOS NO GAAC ATÉ UM DETERMINADO PERIODO");
		cd.setDescription("São pacientes que incritos no Gaac e que ainda estão activos até um determinado periodo final");
		cd.addParameter(new Parameter("endDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(CohortQueries.PATIENTS_ACTIVE_ON_GAAC);
		
		return cd;
	}
	
	public CohortDefinition patientsInCohortActive(CohortDefinition aRTStartCohort, String mappings) {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("COORTE 6 MESES - ACTIVOS");
		cd.setDescription("São pacientes que iniciaram TARV no período da coorte de 6-9 meses da final e que ainda estão activos");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("INICIO", ReportUtils.map(aRTStartCohort, mappings));
		cd.addSearch("ACTIVO", ReportUtils.map(currentlyOnARTExcludingLostToFollowUp(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.setCompositionString("INICIO AND ACTIVO");
		return cd;
	}
	
	public CohortDefinition patientsInCohortDead(CohortDefinition aRTStartCohort, String mappings) {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("COORTE 6 MESES - OBITOS");
		cd.setDescription("São pacientes na coorte e que são óbitos");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("INICIO", ReportUtils.map(aRTStartCohort, mappings));
		cd.addSearch("OBITO", ReportUtils.map(patientsWhoPassedAway(), "endDate=${endDate},location=${location}"));
		cd.setCompositionString("INICIO AND OBITO");
		return cd;
	}
	
	public CohortDefinition patientsInCohortLostToFollowUp(CohortDefinition aRTStartCohort, String mappings) {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("COORTE 6 MESES - ABANDONOS");
		cd.setDescription("São pacientes que iniciaram TARV no período da coorte e que abandonaram");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("INICIO", ReportUtils.map(aRTStartCohort, mappings));
		cd.addSearch("ABANDONO", ReportUtils.map(patientsWhoLostToFollowUp(), "endDate=${endDate},location=${location}"));
		cd.setCompositionString("INICIO AND ABANDONO");
		return cd;
	}
	
	public CohortDefinition patientsInCohortTransferedTo(CohortDefinition aRTStartCohort, String mappings) {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("COORTE 6 MESES - TRANSFERIDOS PARA");
		cd.setDescription("São pacientes na coorte de que foram transferidos para outra US");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("INICIO", ReportUtils.map(aRTStartCohort, mappings));
		cd.addSearch("TRANSFERIDOPARA", ReportUtils.map(patientsTransferedTo(), "endDate=${endDate},location=${location}"));
		cd.setCompositionString("INICIO AND TRANSFERIDOPARA");
		return cd;
	}
	
	public CohortDefinition patientsInCohortSuspended(CohortDefinition aRTStartCohort, String mappings) {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("COORTE 6 MESES - SUSPENSO");
		cd.setDescription("Pacientes na coorte e que foram suspenso");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("INICIO", ReportUtils.map(aRTStartCohort, mappings));
		cd.addSearch("SUSPENSO", ReportUtils.map(patientsSuspendedOnTreatment(), "endDate=${endDate},location=${location}"));
		cd.setCompositionString("INICIO AND SUSPENSO");
		return cd;
	}
	
	public CohortDefinition patientsWhoPassedAway() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PROGRAMA: PACIENTES QUE SAIRAM DO PROGRAMA DE TRATAMENTO ARV - OBITOU: PERIODO FINAL");
		cd.setDescription("São pacientes que sairam do programa de TARV tratamento mas que obitou");
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(CohortQueries.PATIENTS_WHO_PASSED_AWAY);
		
		return cd;
	}
	
	public CohortDefinition patientsWhoAbandoned() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PROGRAMA: PACIENTES QUE SAIRAM DO PROGRAMA DE TRATAMENTO ARV - ABANDONO: PERIODO FINAL");
		cd.setDescription("São pacientes que sairam do programa de TARV tratamento mas que abandonou ao tratamento");
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(CohortQueries.PATIENTS_WHO_ABANDONED);
		
		return cd;
	}
	
	public CohortDefinition patientsTransferedTo() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PROGRAMA: PACIENTES QUE SAIRAM DO PROGRAMA DE TRATAMENTO ARV - TRANSFERIDO PARA: PERIODO FINAL");
		cd.setDescription("São pacientes que sairam do programa de TARV tratamento mas transferidos para outra unidade sanitária");
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(CohortQueries.PATIENTS_TRANSFERED_TO);
		
		return cd;
	}
	
	public CohortDefinition patientsSuspendedOnTreatment() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PROGRAMA: PACIENTES QUE SAIRAM DO PROGRAMA DE TRATAMENTO ARV - SUSPENSO: PERIODO FINAL");
		cd.setDescription("São pacientes que sairam do programa de TARV tratamento mas que foi suspenso ao tratamento");
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(CohortQueries.PATIENTS_SUSPENDED_ON_TREATMENT);
		
		return cd;
	}
	
	public CohortDefinition preganantsWhoStartedART() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("GRAVIDAS QUE INICIARAM TARV");
		cd.setDescription("São gravidas inscritas no periodo de reportagem e que iniciaram tarv no mesmo periodo.");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("INICIOTARV", ReportUtils.map(aRTStartIncludingTransfersFrom(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("GRAVIDAS", ReportUtils.map(pregnantsInscribedOnARTService(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("FEMININO", ReportUtils.map(cohortLibrary.females(), ""));
		cd.setCompositionString("GRAVIDAS AND INICIOTARV AND FEMININO");
		return cd;
	}
	
	public CohortDefinition breastfeedingsWhoStartedART() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("LACTANTES QUE INICIARAM TARV");
		cd.setDescription("São gravidas inscritas no periodo de reportagem e que iniciaram tarv no mesmo periodo.");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("INICIOTARV", ReportUtils.map(aRTStartIncludingTransfersFrom(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("LACTANTE",
		    ReportUtils.map(registeredBreastFeeding(), "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.setCompositionString("LACTANTE AND INICIOTARV");
		return cd;
	}
	
	public CohortDefinition children0To14Years() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("CRIANCAS DE 0-14 ANOS QUE INCIARAM TARV - IDADE NO INICIO DE TARV");
		cd.setDescription("São crianças de 0-14 anos que iniciaram TARV e a idade é calculada na data de inicio de TARV");
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(CohortQueries.CHILDREN_0_14_YEAR_STARTED_ART);
		
		return cd;
	}
	
	public CohortDefinition adults15PlusYears() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("ADULTOS DE 15+ ANOS QUE INICIARAM TARV - IDADE NO INICIO DE TARV");
		cd.setDescription("São adultos de 15+ anos, que iniciaram TARV e a idade é calculada na data de inicio de TARV");
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(CohortQueries.ADULTS_15_PLUS_YEAR_STARTED_ART);
		
		return cd;
	}
	
	public CohortDefinition patientsCurrentlyOnARTSecondLineRegimen() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		
		cd.setName("PACIENTES ACTUALMENTE EM TARV E QUE ESTAO NA SEGUNDA LINHA DE ARV - PERIODO FINAL (ABANDONO RETIRA NOTIFICADO E NAO NOTIFICADO)");
		cd.setDescription("São pacientes que actualmente estao em tarv (abandono retira notificado e nao notificado) e que estejam na segunda linha de tratamento ARV");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("ACTUALARV", ReportUtils.map(currentlyOnARTExcludingLostToFollowUp(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("SEGUNDALINHA",
		    ReportUtils.map(patientsOnSecondLineRegimen(), "endDate=${endDate},location=${location}"));
		cd.setCompositionString("ACTUALARV AND SEGUNDALINHA");
		
		return cd;
	}
	
	private CohortDefinition patientsOnSecondLineRegimen() {
		CodedObsCohortDefinition cd = new CodedObsCohortDefinition();
		
		Concept drug1 = Dictionary.getConcept(Dictionary.ABC_3TC_EFV_2_Linha);
		Concept drug2 = Dictionary.getConcept(Dictionary.ABC_3TC_LPVr_2_Linha);
		Concept drug3 = Dictionary.getConcept(Dictionary.ABC_3TC_NVP_Linha);
		Concept drug4 = Dictionary.getConcept(Dictionary.AZT_3TC_ABC_EFV);
		Concept drug5 = Dictionary.getConcept(Dictionary.AZT_3TC_ABC_LPVr);
		Concept drug6 = Dictionary.getConcept(Dictionary.AZT_3TC_LPV_2_Linha);
		Concept drug7 = Dictionary.getConcept(Dictionary.D4T_3TC_ABC_EFV);
		Concept drug8 = Dictionary.getConcept(Dictionary.D4T_3TC_ABC_LPVr);
		Concept drug9 = Dictionary.getConcept(Dictionary.TDF_3TC_EFV_Linha);
		Concept drug10 = Dictionary.getConcept(Dictionary.TENOFOVIR_LAMIVUDINA_LOPINAVIR);
		Concept drug11 = Dictionary.getConcept(Dictionary.ZIDOVUDINA_DADINOSE_LOPINAVIR);
		
		cd.setName("PACIENTES QUE ESTAO NA SEGUNDA LINHA DE ARV - PERIODO FINAL");
		cd.setDescription("São pacientes cujo o ultimo medicamento ARV levantado na farmacia pertence a segunda linha de ARVs");
		cd.setQuestion(Dictionary.getConcept(Dictionary.REGIMEN));
		cd.setEncounterTypeList(Arrays.asList(CoreUtils.getEncounterType(Metadata._EncounterType.FARMACIA_18)));
		cd.setTimeModifier(PatientSetService.TimeModifier.LAST);
		cd.setOperator(SetComparator.IN);
		cd.setValueList(Arrays.asList(drug1, drug2, drug3, drug4, drug5, drug6, drug7, drug8, drug9, drug10, drug11));
		cd.addParameter(new Parameter("location", "Unidade Sanitaria", Location.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		
		return cd;
	}
	
	public CohortDefinition patientsWhoLostToFollowUp() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("ABANDONO AO TRATAMENTO ARV (ABANDONO NOTIFICADO E NAO NOTIFICADO)");
		cd.setDescription("Sao pacientes declarado como abandono e aqueles que excederam mais de 60 dias e ainda nao foram declarados como abandono");
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("NOTIFICADO", ReportUtils.map(patientsWhoAbandoned(), "endDate=${endDate},location=${location}"));
		cd.addSearch("NAONOTIFICADO", ReportUtils.map(lostToFollowUp(), "endDate=${endDate},location=${location}"));
		cd.setCompositionString("NOTIFICADO OR NAONOTIFICADO");
		return cd;
	}
	
	public CohortDefinition clinicTreatmentFailure() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("FALHAS CLINICAS - SQL");
		cd.setDescription("São pacientes que tiveram falhas clinicas, isto é, são pacientes que 6 meses apos inicio de TARV aumentaram o seu estadio");
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(CohortQueries.CLINIC_TREATMENT_FAILURE);
		
		return cd;
	}
	
	public CohortDefinition patientsInARTNotifiedClinicTreatmentFailure() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES ACTUALMENTE EM TARV E NOTIFICADOS DE FALHAS CLINICAS");
		cd.setDescription("Sao pacientes que estão actualmente em tratamento ARV mas que já tem falhas clinicas");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("TARV", ReportUtils.map(currentlyOnARTExcludingLostToFollowUp(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("FALHACLINICA", ReportUtils.map(clinicTreatmentFailure(), "endDate=${endDate},location=${location}"));
		cd.setCompositionString("TARV AND FALHACLINICA");
		return cd;
	}
	
	public CohortDefinition imunologicTreatmentFailure() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("FALHAS IMUNOLOGICAS - SQL");
		cd.setDescription("Pacientes que tiveram falhas imunologicas, isto é, pacientes com baixo cd4 6 meses apos inicio de TARV");
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(CohortQueries.IMUNOLOGIC_TREATMENT_FAILURE);
		
		return cd;
	}
	
	public CohortDefinition patientsInARTNotifiedImunologicTreatmentFailure() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES ACTUALMENTE EM TARV E NOTIFICADOS DE FALHAS IMUNOLOGICAS");
		cd.setDescription("Sao pacientes actualmente em TARV e que foram notificados de falhas imunologicas");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("TARV", ReportUtils.map(currentlyOnARTExcludingLostToFollowUp(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("IMUNOLOGICA", ReportUtils.map(imunologicTreatmentFailure(), "endDate=${endDate},location=${location}"));
		cd.setCompositionString("TARV AND IMUNOLOGICA");
		return cd;
	}
	
	public CohortDefinition patientsInARTNotifiedTreatmentFailure() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES ACTUALMENTE EM TARV E NOTIFICADOS DE FALHAS CLINICAS OU IMUNOLOGICAS");
		cd.setDescription("São pacientes que estão actualmente em TARV e que foram diagnosticados de falhas clinicas ou imunologicas");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("TARV", ReportUtils.map(currentlyOnARTExcludingLostToFollowUp(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("FALHACLINICA", ReportUtils.map(clinicTreatmentFailure(), "endDate=${endDate},location=${location}"));
		cd.addSearch("IMUNOLOGICA", ReportUtils.map(imunologicTreatmentFailure(), "endDate=${endDate},location=${location}"));
		cd.setCompositionString("TARV AND (FALHACLINICA OR IMUNOLOGICA)");
		return cd;
	}
	
	public CohortDefinition patientsInARTForMoreThanXMonths() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES QUE ESTAO A MAIS DE 6 MESES EM TARV");
		cd.setDescription("Sao pacientes que iniciaram tarv ha mais de 6 meses");
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addParameter(new Parameter("months", "Months", Integer.class));
		cd.setQuery(CohortQueries.PATIENTS_IN_ART_FOR_MORE_THAN_X_MONTHS);
		
		return cd;
	}
	
	public CohortDefinition currentlyInARTAndForMoreThan6MonthsInTreatment() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES ACTUALMENTE EM TARV E QUE ESTAO HA MAIS DE 6 MESES EM TRATAMENTO");
		cd.setDescription("Sao pacientes actualmente em TARV e que estão há mais de 6 meses em tratamento ARV");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("TARV", ReportUtils.map(currentlyOnARTExcludingLostToFollowUp(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("HA6MESES",
		    ReportUtils.map(patientsInARTForMoreThanXMonths(), "endDate=${endDate},location=${location},months=6"));
		cd.setCompositionString("TARV AND HA6MESES");
		return cd;
	}
	
	public CohortDefinition patientsWithViralLoadResultsLast12Months() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES COM RESULTADO DE CARGA VIRAL NOS ULTIMOS 12 MESES ");
		cd.setDescription("São pacientes que tiveram resultado de carga viral nos últimos 12 meses");
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(CohortQueries.PATIENTS_WITH_VIRAL_LOAD_RESULTS_LAST_12_MONTHS);
		
		return cd;
	}
	
	public CohortDefinition inARTAndForMoreThan6MonthsWithViralLoadResults() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES ACTUALMENTE EM TARV E QUE ESTAO HA MAIS DE 6 MESES EM TARV COM RESULTADO DE CARGA VIRAL NOS ULTIMOS 12 MESES");
		cd.setDescription("São pacientes actualmente em TARV e que estão há mais de 6 meses em TARV, com resultado de carga viral registado nos ultimos 12 meses");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("TARV6MESES", ReportUtils.map(currentlyInARTAndForMoreThan6MonthsInTreatment(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("CARGAVIRAL12MESES",
		    ReportUtils.map(patientsWithViralLoadResultsLast12Months(), "endDate=${endDate},location=${location}"));
		cd.setCompositionString("TARV6MESES AND CARGAVIRAL12MESES");
		return cd;
	}
	
	public CohortDefinition patientsWithDetectableViralLoadLast12Months() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES COM CARGA VIRAL DETECTAVEL NOS ULTIMOS 12 MESES");
		cd.setDescription("Sao pacientes cujo o ultimo resultado de carga viral nos ultimos 12 meses foi maior ou igual a 1000");
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(CohortQueries.PATIENTS_WITH_DETECTABLE_VIRAL_LOAD_LAST_12_MONTHS);
		
		return cd;
	}
	
	public CohortDefinition patientsWithUndetectableViralLoadLast12Months() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES COM CARGA VIRAL INDETECTAVEL NOS ULTIMOS 12 MESES");
		cd.setDescription("Sao pacientes cujo o ultimo resultado de carga viral nos ultimos 12 meses foi menor que 1000");
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(CohortQueries.PATIENTS_WITH_UNDETECTABLE_VIRAL_LOAD_LAST_12_MONTHS);
		
		return cd;
	}
	
	public CohortDefinition pregnantsAndBreastfeedingInscribedInARTService() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("GRAVIDAS INSCRITAS NO SERVICO TARV INCLUINDO PUERPUERAS");
		cd.setDescription("Sao gravidas inscritas no servico TARV num periodo incluindo puerpueras do mesmo periodo");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("GRAVIDAS", ReportUtils.map(pregnantsInscribedOnARTService(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("PUERPUERA", ReportUtils.map(breastFeedingOrPuerpueras(), "endDate=${endDate},location=${location}"));
		cd.setCompositionString("GRAVIDAS OR PUERPUERA");
		return cd;
	}
	
	public CohortDefinition pregnantsBreastfeedingAndChildrenWithViralLoadResultLast12Months() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES COM RESULTADO DE CARGA VIRAL NOS ULTIMOS 12 MESES: GRÁVIDAS, LACTANTES E CRIANÇAS 2-5 ANOS");
		cd.setDescription("São pacientes que tiveram resultado de carga viral nos ultimos 12 meses e são grávidas, lactantes e crianças dos 2-5 anos - Rotina");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("GRAVIDALACTANTE", ReportUtils.map(pregnantsAndBreastfeedingInscribedInARTService(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("CARGAVIRAL",
		    ReportUtils.map(patientsWithViralLoadResultsLast12Months(), "endDate=${endDate},location=${location}"));
		cd.addSearch("CRIANCA", ReportUtils.map(cohortLibrary.agedAtLeastAgedAtMost(2, 5), "effectiveDate=${endDate}"));
		cd.setCompositionString("(GRAVIDALACTANTE OR CRIANCA) AND CARGAVIRAL");
		return cd;
	}
	
	public CohortDefinition nonPregnantsBreastfeedingAndChildrenWithViralLoadResultLast12Months() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES COM RESULTADO DE CARGA VIRAL NOS ULTIMOS 12 MESES: NÃO GRÁVIDAS, LACTANTES E CRIANÇAS 2-5 ANOS");
		cd.setDescription("São pacientes que tiveram resultado de carga viral nos ultimos 12 meses e são grávidas, lactantes e crianças dos 2-5 anos - Rotina");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("GRAVIDALACTANTE", ReportUtils.map(pregnantsAndBreastfeedingInscribedInARTService(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("CARGAVIRAL",
		    ReportUtils.map(patientsWithViralLoadResultsLast12Months(), "endDate=${endDate},location=${location}"));
		cd.addSearch("CRIANCA", ReportUtils.map(cohortLibrary.agedAtLeastAgedAtMost(2, 5), "effectiveDate=${endDate}"));
		cd.setCompositionString("CARGAVIRAL NOT (GRAVIDALACTANTE OR CRIANCA)");
		return cd;
	}
	
	public CohortDefinition patientsNotifiedToBeInTBTreatment() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		
		EncounterType adultoSeguimento = CoreUtils.getEncounterType(Metadata._EncounterType.ADULTO_SEGUIMENTO_6);
		EncounterType pediatriaSeguimento = CoreUtils.getEncounterType(Metadata._EncounterType.PEDIATRIA_SEGUIMENTO_9);
		Concept yes = Dictionary.getConcept(Dictionary.YES);
		Concept tbTreatment = Dictionary.getConcept(Dictionary.TB_TREATMENT);
		
		cd.setName("PACIENTES NOTIFICADOS COMO ESTANDO EM TRATAMENTO DE TUBERCULOSE - SEGUIMENTO");
		cd.setDescription("São pacientes que foram notificados como estando em tratamento de tuberculose - Ficha de seguimento - SIM");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("hasEncounters", ReportUtils.map(cohortLibrary.hasEncounter(adultoSeguimento, pediatriaSeguimento),
		    "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));
		cd.addSearch("hasObs", ReportUtils.map(cohortLibrary.hasObs(tbTreatment, PatientSetService.TimeModifier.ANY, yes),
		    "onOrAfter=${startDate},onOrBefore=${endDate}"));
		
		cd.setCompositionString("hasObs AND hasEncounters");
		
		return cd;
	}
	
	public CohortDefinition patientsWhoStartedTBTreatmentWithDateOfStartNotified() {
		DateObsCohortDefinition cd = new DateObsCohortDefinition();
		
		EncounterType ADULTO_SEGUIMENTO = CoreUtils.getEncounterType(Metadata._EncounterType.ADULTO_SEGUIMENTO_6);
		EncounterType PEDIATRIA_SEGUIMENTO = CoreUtils.getEncounterType(Metadata._EncounterType.PEDIATRIA_SEGUIMENTO_9);
		EncounterType TUBERCULOSE_LIVRO = CoreUtils.getEncounterType(Metadata._EncounterType.TUBERCULOSE_LIVRO);
		EncounterType TUBERCULOSE_RASTREIO = CoreUtils.getEncounterType(Metadata._EncounterType.TUBERCULOSE_RASTREIO);
		EncounterType TUBERCULOSE_PROCESSO = CoreUtils.getEncounterType(Metadata._EncounterType.TUBERCULOSE_PROCESSO);
		
		cd.setName("INICIO DE TRATAMENTO DE TUBERCULOSE DATA NOTIFICADA NAS FICHAS DE: SEGUIMENTO, RASTREIO E LIVRO TB");
		cd.setDescription("Pacientes que iniciram TB no com a data de inicio de tratamento de TB notificada nas fichas de seguimento adulto e pediatria, rastreio de tuberculose e livro de TB");
		cd.setQuestion(Dictionary.getConcept(Dictionary.TB_TREATMENT_START_DATE));
		cd.setEncounterTypeList(Arrays.asList(TUBERCULOSE_LIVRO, ADULTO_SEGUIMENTO, PEDIATRIA_SEGUIMENTO,
		    TUBERCULOSE_RASTREIO, TUBERCULOSE_PROCESSO));
		cd.setTimeModifier(PatientSetService.TimeModifier.ANY);
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setOperator1(RangeComparator.GREATER_EQUAL);
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.setOperator2(RangeComparator.LESS_EQUAL);
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		
		return cd;
	}
	
	public CohortDefinition patientsInscribedOnTBProgram() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PROGRAMA: PACIENTES INSCRITOS NO PROGRAMA DE TUBERCULOSE - NUM PERIODO");
		cd.setDescription("São pacientes inscritos no programa de tuberculose num determinado periodo");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(CohortQueries.PATIENTS_INSCRIBED_ON_TB_PROGRAM);
		
		return cd;
	}
	
	public CohortDefinition patientsWhoStartedTBTreatmentNotifiedInARTService() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		
		EncounterType adultoSeguimento = CoreUtils.getEncounterType(Metadata._EncounterType.ADULTO_SEGUIMENTO_6);
		EncounterType pediatriaSeguimento = CoreUtils.getEncounterType(Metadata._EncounterType.PEDIATRIA_SEGUIMENTO_9);
		EncounterType tBProcess = CoreUtils.getEncounterType(Metadata._EncounterType.TUBERCULOSE_PROCESSO);
		Concept start = Dictionary.getConcept(Dictionary.START);
		Concept tbTreatment = Dictionary.getConcept(Dictionary.TB_TREATMENT);
		
		cd.setName("PACIENTES QUE INICIARAM TRATAMENTO DA TUBERCULOSE NOTIFICADOS NO SERVICO TARV - FEV12");
		cd.setDescription("Pacientes que iniciaram o tratamento da tuberculose, e que este inicio foi documentado na ficha de seguimento do paciente no serviço TARV.");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("hasEncounters", ReportUtils.map(
		    cohortLibrary.hasEncounter(adultoSeguimento, pediatriaSeguimento, tBProcess),
		    "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));
		cd.addSearch("hasObs", ReportUtils.map(cohortLibrary.hasObs(tbTreatment, PatientSetService.TimeModifier.ANY, start),
		    "onOrAfter=${startDate},onOrBefore=${endDate}"));
		cd.setCompositionString("hasObs AND hasEncounters");
		
		return cd;
	}
	
	public CohortDefinition patientsNotifiedTBTreatmentDifferentSources() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES NOTIFICADOS DO TRATAMENTO DE TB NO SERVICO TARV: DIFERENTES FONTES");
		cd.setDescription("São pacientes notificados do tratamento de tuberculose notificados em diferentes fontes: Antecedentes clinicos adulto e pediatria, seguimento, rastreio de tb, livro de TB.");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("DATAINICIO", ReportUtils.map(patientsWhoStartedTBTreatmentWithDateOfStartNotified(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("TBPROGRAMA", ReportUtils.map(patientsInscribedOnTBProgram(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("INICIOST", ReportUtils.map(patientsWhoStartedTBTreatmentNotifiedInARTService(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		
		cd.setCompositionString("DATAINICIO OR TBPROGRAMA OR INICIOST");
		return cd;
	}
	
	public CohortDefinition patientsNotifiedTBTreatment() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES NOTIFICADOS COMO ESTANDO EM TRATAMENTO DE TUBERCULOSE (SEGUIMENTO - SIM OU INICIO TB)");
		cd.setDescription("São pacientes notificados como estando em tratamento de TB: Este tratamento foi notificado na ficha de seguimento (Tratamento de TB - SIM) ou inicio de tratamento de TB incluindo insricao no programa de TB");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("TBSIM", ReportUtils.map(patientsNotifiedToBeInTBTreatment(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("INICIOTB", ReportUtils.map(patientsNotifiedTBTreatmentDifferentSources(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		
		cd.setCompositionString("TBSIM OR INICIOTB");
		return cd;
	}
	
	public CohortDefinition patientsNotifiedTBTreatmentWhoStartedART() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES NOTIFICADOS COMO ESTANDO EM TRATAMENTO DE TUBERCULOSE E QUE INICIARAM O TARV");
		cd.setDescription("São pacientes que foram notificados como estando em tratamento de tuberculose num periodo e que iniciaram o TARV no mesmo periodo");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("TBNOTIFICADO",
		    ReportUtils.map(patientsNotifiedTBTreatment(), "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("INICIOTARV", ReportUtils.map(aRTStartInPeriodExcludingTransfersFrom(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		
		cd.setCompositionString("TBNOTIFICADO AND INICIOTARV");
		return cd;
	}
	
	public CohortDefinition yearStartedART() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("IDADE NO INICIO DE TARV");
		cd.setDescription("São adultos de 15+ anos, que iniciaram TARV e a idade é calculada na data de inicio de TARV");
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addParameter(new Parameter("minAge", "Minimum Age", Integer.class));
		cd.addParameter(new Parameter("maxAge", "Maximum Age", Integer.class));
		cd.addParameter(new Parameter("gender", "Gender", Character.class));
		cd.setQuery(CohortQueries.YEAR_STARTED_ART);
		
		return cd;
	}
	
	public CohortDefinition patientsActiveInARTNotifiedTBTreatmentOnARTService() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES NOTIFICADOS DO TRATAMENTO DE TB NO SERVICO TARV - ACTIVOS EM TARV");
		cd.setDescription("Pacientes notificados do tratamento TB no serviço TARV em todas as fontes e que são activos em TARV no periodo de report");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("NOTIFICADOSTB", ReportUtils.map(patientsNotifiedTBTreatmentDifferentSources(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("ACTIVOSTARV", ReportUtils.map(currentlyOnARTExcludingLostToFollowUp(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		
		cd.setCompositionString("ACTIVOSTARV AND NOTIFICADOSTB");
		return cd;
	}
	
	public CohortDefinition patientsNotifiedTBTreatmentOnARTServiceWhoStartedART() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES NOTIFICADOS DO TRATAMENTO DE TB NO SERVICO TARV - NOVOS INICIOS");
		cd.setDescription("Pacientes notificados do tratamento de TB no serviço tarv em todas as fontes");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("NOTIFICADOSTB", ReportUtils.map(patientsNotifiedTBTreatmentDifferentSources(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("NOVOSINICIOS", ReportUtils.map(aRTStartInPeriodExcludingTransfersFrom(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		
		cd.setCompositionString("NOTIFICADOSTB AND NOVOSINICIOS");
		return cd;
	}
	
	public CohortDefinition patientsWithPositiveTBTracking() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		
		EncounterType adultoSeguimento = CoreUtils.getEncounterType(Metadata._EncounterType.ADULTO_SEGUIMENTO_6);
		EncounterType pediatriaSeguimento = CoreUtils.getEncounterType(Metadata._EncounterType.PEDIATRIA_SEGUIMENTO_9);
		Concept YES = Dictionary.getConcept(Dictionary.YES);
		Concept screeningForTB = Dictionary.getConcept(Dictionary.SCREENING_FOR_TB);
		
		cd.setName("PACIENTES COM RASTREIO DE TUBERCULOSE POSITIVO");
		cd.setDescription("São pacientes que tiveram rastreio de tuberculose positivo");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("hasEncounters", ReportUtils.map(cohortLibrary.hasEncounter(adultoSeguimento, pediatriaSeguimento),
		    "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));
		cd.addSearch("hasObs", ReportUtils.map(
		    cohortLibrary.hasObs(screeningForTB, PatientSetService.TimeModifier.LAST, YES),
		    "onOrAfter=${startDate},onOrBefore=${endDate}"));
		cd.setCompositionString("hasObs AND hasEncounters");
		
		return cd;
	}
	
	public CohortDefinition patientsWithNegativeTBTracking() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		
		EncounterType adultoSeguimento = CoreUtils.getEncounterType(Metadata._EncounterType.ADULTO_SEGUIMENTO_6);
		EncounterType pediatriaSeguimento = CoreUtils.getEncounterType(Metadata._EncounterType.PEDIATRIA_SEGUIMENTO_9);
		Concept NO = Dictionary.getConcept(Dictionary.NO);
		Concept tbTracking = Dictionary.getConcept(Dictionary.SCREENING_FOR_TB);
		
		cd.setName("PACIENTES COM RASTREIO DE TUBERCULOSE NEGATIVO");
		cd.setDescription("São pacientes que tiveram o ultimo rastreio de tuberculose negativo");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("hasEncounters", ReportUtils.map(cohortLibrary.hasEncounter(adultoSeguimento, pediatriaSeguimento),
		    "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));
		cd.addSearch("hasObs", ReportUtils.map(cohortLibrary.hasObs(tbTracking, PatientSetService.TimeModifier.LAST, NO),
		    "onOrAfter=${startDate},onOrBefore=${endDate}"));
		cd.setCompositionString("hasObs AND hasEncounters");
		
		return cd;
	}
	
	public CohortDefinition patientsActiveInARTWithPositiveTBTracking() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES ACTUALMENTE EM TARV COM RASTREIO DE TUBERCULOSE POSITIVO NUM DETERMINADO PERIODO");
		cd.setDescription("Sao pacientes que estão actualmente em TARV (ACTIVOS) e que tiveram o ultimo rastreio de tuberculose positivo num determinado periodo");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("RASTREIOPOSITIVO", ReportUtils.map(patientsWithPositiveTBTracking(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("ACTUALTARV", ReportUtils.map(currentlyOnARTExcludingLostToFollowUp(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		
		cd.setCompositionString("ACTUALTARV AND RASTREIOPOSITIVO");
		return cd;
	}
	
	public CohortDefinition patientsActiveInARTWithNegativeTBTracking() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES ACTUALMENTE EM TARV COM RASTREIO DE TUBERCULOSE POSITIVO NUM DETERMINADO PERIODO");
		cd.setDescription("Sao pacientes que estão actualmente em TARV (ACTIVOS) e que tiveram o ultimo rastreio de tuberculose negativo num determinado periodo");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("RASTREIONEGATIVO", ReportUtils.map(patientsWithNegativeTBTracking(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("ACTUALTARV", ReportUtils.map(currentlyOnARTExcludingLostToFollowUp(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		
		cd.setCompositionString("ACTUALTARV AND RASTREIONEGATIVO");
		return cd;
	}
	
	public CohortDefinition patientsWithPositiveTBTrackingWhoStartedART() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("INICIO DE TARV E COM RASTREIO TB POSITIVO - NUM PERIODO: EXCLUI TRANSFERIDOS DE COM DATA DE INICIO CONHECIDA");
		cd.setDescription("Sao pacientes que iniciaram tratamento ARV num periodo excluindo os transferidos de com a data de inicio conhecida e mesmo que coincida no periodo e que tiveram rastreio de TB positivo");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("RASTREIOTBPOS", ReportUtils.map(patientsWithPositiveTBTracking(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("INICIOTARV", ReportUtils.map(aRTStartInPeriodExcludingTransfersFrom(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		
		cd.setCompositionString("RASTREIOTBPOS AND INICIOTARV");
		return cd;
	}
	
	public CohortDefinition patientsWithNegativeTBTrackingWhoStartedART() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("INICIO DE TARV E COM RASTREIO DE TB NEGATIVO - NUM PERIODO: EXCLUI TRANSFERIDOS DE COM DATA DE INICIO CONHECIDA");
		cd.setDescription("Sao pacientes que iniciaram tratamento ARV num periodo excluindo os transferidos de com a data de inicio conhecida e mesmo que coincida no periodo e que tiveram rastreio de TB negativo");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("RASTREIOTBNEG", ReportUtils.map(patientsWithNegativeTBTracking(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("INICIOTARVTB", ReportUtils.map(aRTStartInPeriodExcludingTransfersFrom(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		
		cd.setCompositionString("INICIOTARVTB AND RASTREIOTBNEG");
		return cd;
	}
	
	public CohortDefinition patientsWHOHadPositiveTBDiagnoseTest() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		
		EncounterType adultoSeguimento = CoreUtils.getEncounterType(Metadata._EncounterType.ADULTO_SEGUIMENTO_6);
		EncounterType pediatriaSeguimento = CoreUtils.getEncounterType(Metadata._EncounterType.PEDIATRIA_SEGUIMENTO_9);
		Concept POSITIVE = Dictionary.getConcept(Dictionary.POSITIVE);
		Concept TB_INVESTIGATION_RESULT = Dictionary.getConcept(Dictionary.TB_INVESTIGATION_RESULT_BK_RX);
		
		cd.setName("PACIENTES QUE TIVERAM TESTE DE DIAGNOSTICO DE TUBERCULOSE POSITIVO: BK OU RX");
		cd.setDescription("São pacientes que depois de um rastreio positivo, foram submetidos ao teste de confirmação de tuberculose por BK ou RX e que este teste foi positivo");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("hasEncounters", ReportUtils.map(cohortLibrary.hasEncounter(adultoSeguimento, pediatriaSeguimento),
		    "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));
		cd.addSearch("hasObs", ReportUtils.map(
		    cohortLibrary.hasObs(TB_INVESTIGATION_RESULT, PatientSetService.TimeModifier.LAST, POSITIVE),
		    "onOrAfter=${startDate},onOrBefore=${endDate}"));
		cd.setCompositionString("hasObs AND hasEncounters");
		
		return cd;
	}
	
	public CohortDefinition patientsWHOHadNegativeTBDiagnoseTest() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		
		EncounterType adultoSeguimento = CoreUtils.getEncounterType(Metadata._EncounterType.ADULTO_SEGUIMENTO_6);
		EncounterType pediatriaSeguimento = CoreUtils.getEncounterType(Metadata._EncounterType.PEDIATRIA_SEGUIMENTO_9);
		Concept NEGATIVE = Dictionary.getConcept(Dictionary.NEGATIVE);
		Concept TB_INVESTIGATION_RESULT = Dictionary.getConcept(Dictionary.TB_INVESTIGATION_RESULT_BK_RX);
		
		cd.setName("PACIENTES QUE TIVERAM TESTE DE DIAGNOSTICO DE TUBERCULOSE NEGATIVO: BK OU RX");
		cd.setDescription("São pacientes que teste de investigação de tuberculose negativo");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("hasEncounters", ReportUtils.map(cohortLibrary.hasEncounter(adultoSeguimento, pediatriaSeguimento),
		    "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));
		cd.addSearch("hasObs", ReportUtils.map(
		    cohortLibrary.hasObs(TB_INVESTIGATION_RESULT, PatientSetService.TimeModifier.LAST, NEGATIVE),
		    "onOrAfter=${startDate},onOrBefore=${endDate}"));
		cd.setCompositionString("hasObs AND hasEncounters");
		
		return cd;
	}
	
	public CohortDefinition patientsEverInARTWithPositiveTBTrackingWhoHadPositiveDiagnoseTest() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES QUE ALGUMA VEZ ESTIVERAM EM TARV COM RASTREIO DE TB POSITIVO E TIVERAM TESTE DE DIAGNOSTICO DE TB POSITIVO: BK OU RX - PERIODO FINAL (COMPOSICAO)");
		cd.setDescription("Pacientes que alguma vez estiveram em TARV com rastreio de TB positivo e tiveram teste de diagnostico de TB positivo: BK ou RX - Periodo Final");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("RASTREIOTBPOS", ReportUtils.map(patientsWithPositiveTBTracking(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("ESTEVETARV",
		    ReportUtils.map(everBeenOnART(), "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("TESTETBPOS", ReportUtils.map(patientsWHOHadPositiveTBDiagnoseTest(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		
		cd.setCompositionString("ESTEVETARV AND RASTREIOTBPOS AND TESTETBPOS");
		return cd;
	}
	
	public CohortDefinition patientsEverInARTWithPositiveTBTrackingWhoHadNegativeDiagnoseTest() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES QUE ALGUMA VEZ ESTIVERAM EM TARV COM RASTREIO DE TB POSITIVO E TIVERAM TESTE DE DIAGNOSTICO DE TB NEGATIVO: BK OU RX PERIODO FINAL (COMPOSICAO)");
		cd.setDescription("Pacientes que alguma vez estiveram em TARV com rastreio de TB positivo e tiveram teste de diagnostico de TB negativo: BK ou RX - Periodo Final");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("RASTREIOTBPOS", ReportUtils.map(patientsWithPositiveTBTracking(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("ESTEVETARV",
		    ReportUtils.map(everBeenOnART(), "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("TESTETBNEG", ReportUtils.map(patientsWHOHadNegativeTBDiagnoseTest(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		
		cd.setCompositionString("ESTEVETARV AND RASTREIOTBPOS AND TESTETBNEG");
		return cd;
	}
	
	public CohortDefinition patientsWhoStartedProphilaxyWithIsoniazida() {
		DateObsCohortDefinition cd = new DateObsCohortDefinition();
		
		EncounterType ADULTO_SEGUIMENTO = CoreUtils.getEncounterType(Metadata._EncounterType.ADULTO_SEGUIMENTO_6);
		EncounterType PEDIATRIA_SEGUIMENTO = CoreUtils.getEncounterType(Metadata._EncounterType.PEDIATRIA_SEGUIMENTO_9);
		
		cd.setName("PACIENTES QUE INICIARAM PROFILAXIA COM ISONIAZIDA");
		cd.setDescription("Pacientes que iniciaram profilaxia com Isoniazida num periodo. Repare que e diferente de pacientes que receberam profilaxia");
		cd.setQuestion(Dictionary.getConcept(Dictionary.PROPHILAXY_WITH_ISONIAZIDA_START_DATE));
		cd.setEncounterTypeList(Arrays.asList(ADULTO_SEGUIMENTO, PEDIATRIA_SEGUIMENTO));
		cd.setTimeModifier(PatientSetService.TimeModifier.FIRST);
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setOperator1(RangeComparator.GREATER_EQUAL);
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.setOperator2(RangeComparator.LESS_EQUAL);
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		
		return cd;
	}
	
	public CohortDefinition patientsWhoFinishedProphilaxyWithIsoniazida() {
		DateObsCohortDefinition cd = new DateObsCohortDefinition();
		
		EncounterType ADULTO_SEGUIMENTO = CoreUtils.getEncounterType(Metadata._EncounterType.ADULTO_SEGUIMENTO_6);
		EncounterType PEDIATRIA_SEGUIMENTO = CoreUtils.getEncounterType(Metadata._EncounterType.PEDIATRIA_SEGUIMENTO_9);
		
		cd.setName("PACIENTES QUE TERMINARAM A PROFILAXIA COM ISONIAZIDA NUM PERIODO");
		cd.setDescription("Pacientes que terminaram a profilaxia com a isoniazida num determinado periodo");
		cd.setQuestion(Dictionary.getConcept(Dictionary.PROPHILAXY_WITH_ISONIAZIDA_END_DATE));
		cd.setEncounterTypeList(Arrays.asList(ADULTO_SEGUIMENTO, PEDIATRIA_SEGUIMENTO));
		cd.setTimeModifier(PatientSetService.TimeModifier.LAST);
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setOperator1(RangeComparator.GREATER_EQUAL);
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.setOperator2(RangeComparator.LESS_EQUAL);
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		
		return cd;
	}
	
	public CohortDefinition patientsWhoStartedAndFinishedProphilaxyWithIsoniazida() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES QUE INICIARAM E TERMINARAM PROFILAXIA COM ISONIAZIDA O TERMINO DEVE TER DATA FIM PREENCHIDA");
		cd.setDescription("Pacientes que iniciaram e terminaram a profilaxia com isoniazida, o termino é marcado por preenchimento de data do fim");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("INICIOTPI", ReportUtils.map(patientsWhoStartedProphilaxyWithIsoniazida(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("ALGUMAVEZTARV",
		    ReportUtils.map(everBeenOnART(), "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("TERMINAR", ReportUtils.map(patientsWhoFinishedProphilaxyWithIsoniazida(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		
		cd.setCompositionString("ALGUMAVEZTARV AND TERMINAR AND INICIOTPI");
		return cd;
	}
	
	public CohortDefinition patientsWhoStartedAndFinishedProphilaxyWithIsoniazidaPreviousPeriod() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES QUE INICIARAM E TERMINARAM PROFILAXIA COM ISONIAZIDA O TERMINO DEVE TER DATA FIM PREENCHIDA: INICIO TPI PERIODO ANTERIOR");
		cd.setDescription("Pacientes que iniciaram e terminaram a profilaxia com isoniazida, o termino é marcado por preenchimento de data do fim. O inicio é num periodo de 6 meses anteriores ao periodo de reportagem");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("INICIOTPI", ReportUtils.map(patientsWhoStartedProphilaxyWithIsoniazida(),
		    "startDate=${startDate-6m},endDate=${startDate-1d},location=${location}"));
		cd.addSearch("ALGUMAVEZTARV",
		    ReportUtils.map(everBeenOnART(), "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("TERMINAR", ReportUtils.map(patientsWhoFinishedProphilaxyWithIsoniazida(),
		    "startDate=${startDate-6m},endDate=${endDate},location=${location}"));
		
		cd.setCompositionString("ALGUMAVEZTARV AND TERMINAR AND INICIOTPI");
		return cd;
	}
	
	public CohortDefinition patientsWhoStartedAndContinueProphilaxyWithIsoniazida() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES QUE INICIARAM E CONTINUAM EM PROFILAXIA COM ISONIZIDA: INICIOU E NÃO TEM DATA FIM");
		cd.setDescription("São pacientes que iniciaram a profilaxia com isoniziada e que ainda não terminaram. Não terminar significa que não tem data de fim preenchida");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("INICIOTPI", ReportUtils.map(patientsWhoStartedProphilaxyWithIsoniazida(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("ALGUMAVEZTARV",
		    ReportUtils.map(everBeenOnART(), "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("TERMINAR", ReportUtils.map(patientsWhoFinishedProphilaxyWithIsoniazida(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		
		cd.setCompositionString("(ALGUMAVEZTARV AND INICIOTPI) AND NOT TERMINAR");
		return cd;
	}
	
	public CohortDefinition patientsWhoStartedAndContinueProphilaxyWithIsoniazidaPreviousPeriod() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES QUE INICIARAM E CONTINUAM EM PROFILAXIA COM ISONIZIDA: INICIOU E NÃO TEM DATA FIM - INICIO TPI PERIODO ANTERIOR");
		cd.setDescription("São pacientes que iniciaram a profilaxia com isoniziada e que ainda não terminaram. Não terminar significa que não tem data de fim preenchida. O inicio de TPI é no periodo de 6 meses anteriores ao periodo de reportagem");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("INICIOTPI", ReportUtils.map(patientsWhoStartedProphilaxyWithIsoniazida(),
		    "startDate=${startDate-6m},endDate=${startDate-1d},location=${location}"));
		cd.addSearch("ALGUMAVEZTARV",
		    ReportUtils.map(everBeenOnART(), "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("TERMINAR", ReportUtils.map(patientsWhoFinishedProphilaxyWithIsoniazida(),
		    "startDate=${startDate-6m},endDate=${endDate},location=${location}"));
		
		cd.setCompositionString("(ALGUMAVEZTARV AND INICIOTPI) AND NOT TERMINAR");
		return cd;
	}
	
	public CohortDefinition patientsEverStartedARTAndStartedProphilaxyWithIsoniazida() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES QUE ALGUMA VEZ INICIOU TARV E QUE INICIOU A PROFILAXIA COM TPI NUM DETERMINADO PERIODO");
		cd.setDescription("Sao pacientes que alguma vez iniciou tarv e que iniciou o tratamento profilactico com isoniazida num determinado periodo");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("PROFILAXIATPI", ReportUtils.map(patientsWhoStartedProphilaxyWithIsoniazida(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("ALGUMAVEZTARV",
		    ReportUtils.map(everBeenOnART(), "startDate=${startDate},endDate=${endDate},location=${location}"));
		
		cd.setCompositionString("ALGUMAVEZTARV AND PROFILAXIATPI");
		return cd;
	}
	
	public CohortDefinition patientsWhoStartedARTXDaysAfterDeclaredIlegible() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("MQ_PACIENTES QUE INICIARAM TARV DENTRO DE X DIAS DEPOIS DE DECLARADAS ELEGIVEIS");
		cd.setDescription("Sao pacientes que iniciaram tarv, com data de elegibilidade preenchida e que iniciaram TARV dentro de X dias depois de declaradas elegiveis");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addParameter(new Parameter("days", "Days", Integer.class));
		cd.setQuery(CohortQueries.PATIENTS_WHO_STARTED_ART_X_DAYS_AFTER_DECLARED_ILEGIBLE);
		
		return cd;
	}
	
	public CohortDefinition patientsWithViralLoadResultsDocumentedInPeriod() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES COM RESULTADO DE CARGA VIRAL DOCUMENTADA NUM DETERMINADO PERIODO");
		cd.setDescription("São pacientes com resultado de carga viral documentada num determinado perido");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(CohortQueries.PATIENTS_WITH_VIRAL_LOAD_RESULTS_DOCUMENTED_IN_PERIOD);
		
		return cd;
	}
	
	public CohortDefinition patientsWithUndetectableViralLoadInPeriod() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES COM CARGA VIRAL INDETECTAVEL (CV<1000): NUM DETERMINADO PERIODO");
		cd.setDescription("São pacientes cujo o ultimo resultado de carga viral num determinado período foi menor 1000");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(CohortQueries.PATIENTS_WITH_UNDETECTABLE_VIRAL_LOAD_IN_PERIOD);
		
		return cd;
	}
	
	public CohortDefinition patientsWithAtLeastOneClinicConsultation() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES COM PELO MENOS UMA CONSULTA CLINICA NUM DETERMINADO PERIODO");
		cd.setDescription("São pacientes que têm pelo menos uma consulta clínica (seguimento) num determinado periodo");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(CohortQueries.PATIENTS_WITH_AT_LEAST_ONE_CLINIC_CONSULTATION);
		
		return cd;
	}
	
	public CohortDefinition patientsWithImmunologicFaults() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("FALHAS IMUNOLOGICAS - SQL");
		cd.setDescription("Pacientes que tiveram falhas imunologicas, isto é, pacientes com baixo cd4 6 meses apos inicio de TARV");
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(CohortQueries.PATIENTS_WITH_IMMUNOLOGIC_FAULTS);
		
		return cd;
	}
	
	public CohortDefinition patientsWithViralLoadExamRegistered() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		
		EncounterType adultoSeguimento = CoreUtils.getEncounterType(Metadata._EncounterType.ADULTO_SEGUIMENTO_6);
		EncounterType pediatriaSeguimento = CoreUtils.getEncounterType(Metadata._EncounterType.PEDIATRIA_SEGUIMENTO_9);
		EncounterType misauLaboratorio = CoreUtils.getEncounterType(Metadata._EncounterType.MISAU_LABORATORIO);
		
		Concept HIV_VIRAL_LOAD = Dictionary.getConcept(Dictionary.HIV_VIRAL_LOAD);
		
		cd.setName("PACIENTES COM EXAME DE CARGA VIRAL REGISTADO: PERIODO FINAL");
		cd.setDescription("Pacientes com exame de carga viral registado até um determinado perido final");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("hasEncounters", ReportUtils.map(
		    cohortLibrary.hasEncounter(adultoSeguimento, pediatriaSeguimento, misauLaboratorio),
		    "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));
		cd.addSearch("hasObs", ReportUtils.map(
		    cohortLibrary.hasNumericObs(HIV_VIRAL_LOAD, PatientSetService.TimeModifier.ANY), "onOrBefore=${endDate}"));
		cd.setCompositionString("hasObs AND hasEncounters");
		
		return cd;
	}
	
	public CohortDefinition patientsWithUndetectableViralLoadFinalPeriod() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		
		EncounterType adultoSeguimento = CoreUtils.getEncounterType(Metadata._EncounterType.ADULTO_SEGUIMENTO_6);
		EncounterType pediatriaSeguimento = CoreUtils.getEncounterType(Metadata._EncounterType.PEDIATRIA_SEGUIMENTO_9);
		EncounterType misauLaboratorio = CoreUtils.getEncounterType(Metadata._EncounterType.MISAU_LABORATORIO);
		
		Concept HIV_VIRAL_LOAD = Dictionary.getConcept(Dictionary.HIV_VIRAL_LOAD);
		
		cd.setName("PACIENTES COM CARGA VIRAL INDETECTAVEL (CV<1000): PERIODO FINAL");
		cd.setDescription("São pacientes cuja a ultima carga viral registada foi menor que 1000");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("hasEncounters", ReportUtils.map(
		    cohortLibrary.hasEncounter(adultoSeguimento, pediatriaSeguimento, misauLaboratorio),
		    "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));
		cd.addSearch("hasObs", ReportUtils.map(
		    cohortLibrary.hasNumericObs(HIV_VIRAL_LOAD, PatientSetService.TimeModifier.LAST, 1000.0),
		    "onOrBefore=${endDate}"));
		cd.setCompositionString("hasObs AND hasEncounters");
		
		return cd;
	}
	
	public CohortDefinition patientsWithDetectableViralLoadFinalPeriod() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		
		EncounterType adultoSeguimento = CoreUtils.getEncounterType(Metadata._EncounterType.ADULTO_SEGUIMENTO_6);
		EncounterType pediatriaSeguimento = CoreUtils.getEncounterType(Metadata._EncounterType.PEDIATRIA_SEGUIMENTO_9);
		EncounterType misauLaboratorio = CoreUtils.getEncounterType(Metadata._EncounterType.MISAU_LABORATORIO);
		
		Concept HIV_VIRAL_LOAD = Dictionary.getConcept(Dictionary.HIV_VIRAL_LOAD);
		
		cd.setName("PACIENTES COM CARGA VIRAL DETECTAVEL (>=1000): PERIODO FINAL");
		cd.setDescription("São pacientes com a ultima carga viral registado foi maior que 1000 (Detectavel)");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("hasEncounters", ReportUtils.map(
		    cohortLibrary.hasEncounter(adultoSeguimento, pediatriaSeguimento, misauLaboratorio),
		    "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));
		cd.addSearch("hasObs", ReportUtils.map(
		    cohortLibrary.hasNumericObs(HIV_VIRAL_LOAD, PatientSetService.TimeModifier.LAST, 1000.0, 1000000.0),
		    "onOrAfter=${startDate},onOrBefore=${endDate}"));
		cd.setCompositionString("hasObs AND hasEncounters");
		
		return cd;
	}
	
	public CohortDefinition patientsEverInARTWithNoViralLoadExam() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES QUE ALGUMA VEZ ESTEVE EM TRATAMENTO ARV SEM EXAME DE CARGA VIRAL");
		cd.setDescription("São pacientes que alguma vez esteve em tratamento ARV sem exame de carga viral registado");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("CARGAVIRAL", ReportUtils.map(patientsWithViralLoadExamRegistered(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("ALGUMAVEZTARV",
		    ReportUtils.map(everBeenOnART(), "startDate=${startDate},endDate=${endDate},location=${location}"));
		
		cd.setCompositionString("ALGUMAVEZTARV AND NOT CARGAVIRAL");
		return cd;
	}
	
	public CohortDefinition CONSULTA_CRIANCAS_2_A_5_ANOS_MAIS_6_MESES_TARV() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("CONSULTA + CRIANCAS 2 A 5 ANOS + MAIS_6_MESES_TARV");
		cd.setDescription("Composição CONSULTA + CRIANCAS 2 A 5 ANOS + MAIS 6 MESES TARV");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("CONSULTA", ReportUtils.map(patientsWithAtLeastOneClinicConsultation(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("CRIANCA2A5ANOS",
		    ReportUtils.map(cohortLibrary.agedAtLeastAgedAtMost(2, 5), "effectiveDate=${endDate}"));
		cd.addSearch("MAIS6MESESTARV",
		    ReportUtils.map(patientsInARTForMoreThanXMonths(), "endDate=${endDate},location=${location},months=6"));
		
		cd.setCompositionString("CONSULTA AND CRIANCA2A5ANOS AND MAIS6MESESTARV");
		return cd;
	}
	
	public CohortDefinition TARVSEMCARGAVIRAL_CARGAVIRALINDETECTAVEL() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("TARVSEMCARGAVIRAL OR CARGAVIRALINDETECTAVEL");
		cd.setDescription("Composition TARVSEMCARGAVIRAL + CARGAVIRALINDETECTAVEL");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("TARVSEMCARGAVIRAL", ReportUtils.map(patientsEverInARTWithNoViralLoadExam(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("CARGAVIRALINDETECTAVEL", ReportUtils.map(patientsWithUndetectableViralLoadFinalPeriod(),
		    "startDate=${startDate},endDate=${endDate-12m},location=${location}"));
		
		cd.setCompositionString("TARVSEMCARGAVIRAL OR CARGAVIRALINDETECTAVEL");
		return cd;
	}
	
	public CohortDefinition patientsIlegibleToViralLoadTestStartSites() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES ELEGIVEIS A CARGA VIRAL DE ROTINA: SITIOS TESTAR E INICIAR");
		cd.setDescription("Pacientes com elegiveis a carga viral de rotina. Criterios de elegibilidade 1) Pacientes com idade maior e igual a 2 anos 2) Nao gravidas 3) ter consulta clinica no periodo 4) Em tarv a mais de 6 meses 5) Sem carga viral ou com carga viral há mais de 12 meses e que esta carga viral foi menor que 1000");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("CONSULTA", ReportUtils.map(CONSULTA_CRIANCAS_MAIS_2_ANOS_MAIS_6_MESES_TARV(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("GRAVIDAS", ReportUtils.map(pregnantsInscribedOnARTService(),
		    "startDate=${endDate-10m},endDate=${endDate},location=${location}"));
		cd.addSearch("CARGAVIRALINDETECTAVEL", ReportUtils.map(TARVSEMCARGAVIRAL_CARGAVIRALINDETECTAVEL(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		
		cd.setCompositionString("(CONSULTA AND NOT GRAVIDAS) AND CARGAVIRALINDETECTAVEL");
		return cd;
	}
	
	public CohortDefinition patientsIlegibleToViralLoadNotTestStartSites() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES ELEGIVEIS A CARGA VIRAL DE ROTINA: SITIOS NAO TESTAR E INICIAR");
		cd.setDescription("Pacientes com elegiveis a carga viral de rotina. Criterios de elegibilidade 1) Pacientes com entre 2-5 anos 2) Nao gravidas 3) ter consulta clinica no periodo 4) Em tarv a mais de 6 meses 5) Sem carga viral ou com carga viral há mais de 12 meses e que esta carga viral foi menor que 1000");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("CONSULTA", ReportUtils.map(CONSULTA_CRIANCAS_2_A_5_ANOS_MAIS_6_MESES_TARV(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("TARVSEMCARGAVIRAL", ReportUtils.map(TARVSEMCARGAVIRAL_CARGAVIRALINDETECTAVEL(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		
		cd.setCompositionString("CONSULTA AND TARVSEMCARGAVIRAL");
		return cd;
	}
	
	public CohortDefinition CONSULTA_CRIANCAS_MAIS_2_ANOS_MAIS_6_MESES_TARV() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("CONSULTA + CRIANCAS MAIS DE 2 ANOS + MAIS 6 MESES TARV");
		cd.setDescription("Composição CONSULTA + CRIANCAS MAIS DE 2 ANOS + MAIS 6 MESES TARV");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("CONSULTA", ReportUtils.map(patientsWithAtLeastOneClinicConsultation(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("IDADE2ANOS", ReportUtils.map(cohortLibrary.agedAtLeast(2), "effectiveDate=${endDate}"));
		cd.addSearch("MAIS6MESESTARV",
		    ReportUtils.map(patientsInARTForMoreThanXMonths(), "endDate=${endDate},location=${location},months=6"));
		
		cd.setCompositionString("CONSULTA AND MAIS6MESESTARV AND IDADE2ANOS");
		return cd;
	}
	
	public CohortDefinition CVDETECTAVEL_FALHAIMUNOLOGICA_SEMCARGAVIRAL() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("CVDETECTAVEL OU (FALHAIMUNOLOGICA + SEMCARGAVIRAL)");
		cd.setDescription("Composição CVDETECTAVEL OU (FALHAIMUNOLOGICA + SEMCARGAVIRAL)");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("CVDETECTAVEL", ReportUtils.map(patientsWithDetectableViralLoadFinalPeriod(),
		    "startDate=${startDate},endDate=${endDate-6m},location=${location}"));
		cd.addSearch("FALHAIMUNOLOGICA",
		    ReportUtils.map(patientsWithImmunologicFaults(), "endDate=${endDate},location=${location}"));
		cd.addSearch("SEMCARGAVIRAL", ReportUtils.map(patientsEverInARTWithNoViralLoadExam(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		
		cd.setCompositionString("CVDETECTAVEL OR (FALHAIMUNOLOGICA AND SEMCARGAVIRAL)");
		return cd;
	}
	
	public CohortDefinition patientsIligebleToViralLoadForTherapeuticFailureSuspect() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES ELEGIVEIS A CARGA VIRAL POR SUSPEITA DE FALENCIA TERAPEUTICA - TARGETED");
		cd.setDescription("Pacientes elegiveis a carga viral por suspeita de falencia terapeutica. 1) Paciente com idade maior ou igual a 2 anos 2) Consulta clinica no periodo 3) Em TARV há mais de 6 meses 4) Ultima carga viral medida há 6 meses com valor superior a 1000 ou suspeita de falencia imunologica sem carga viral");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("CONSULTA", ReportUtils.map(CONSULTA_CRIANCAS_MAIS_2_ANOS_MAIS_6_MESES_TARV(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("CVDETECTAVEL", ReportUtils.map(CVDETECTAVEL_FALHAIMUNOLOGICA_SEMCARGAVIRAL(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		
		cd.setCompositionString("CONSULTA AND NOT CVDETECTAVEL");
		return cd;
	}
	
	public CohortDefinition patientsIligebleToRotineViralLoadTestStartSitesExcludingTherapeuticFailure() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES ELEGIVEIS A CARGA VIRAL DE ROTINA: SITIOS TESTAR E INICIAR - SEM INCLUIR ELEGIVEIS POR FALENCIA");
		cd.setDescription("Pacientes com elegiveis a carga viral de rotina. Criterios de elegibilidade 1) Pacientes com idade maior e igual a 2 anos 2) Nao gravidas 3) ter consulta clinica no periodo 4) Em tarv a mais de 6 meses 5) Sem carga viral ou com carga viral há mais de 12 meses e que esta carga viral foi menor que 1000. Retiramos aqueles que coincide a elegibilidade por Rotina e Targeted");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("ROTINATESTARINICIAR", ReportUtils.map(patientsIlegibleToViralLoadTestStartSites(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("FALHAIMUNOLOGICA", ReportUtils.map(patientsIligebleToViralLoadForTherapeuticFailureSuspect(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		
		cd.setCompositionString("ROTINATESTARINICIAR AND NOT FALHAIMUNOLOGICA");
		return cd;
	}
	
	public CohortDefinition patientsIligebleToRotineViralLoadTestNotStartSitesExcludingTherapeuticFailure() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES ELEGIVEIS A CARGA VIRAL DE ROTINA: SITIOS NAO TESTAR E INICIAR - SEM INCLUIR ELEGIVEIS POR FALENCIA");
		cd.setDescription("Pacientes com elegiveis a carga viral de rotina. Criterios de elegibilidade 1) Pacientes com entre 2-5 anos 2) Nao gravidas 3) ter consulta clinica no periodo 4) Em tarv a mais de 6 meses 5) Sem carga viral ou com carga viral há mais de 12 meses e que esta carga viral foi menor que 1000 ");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("ROTINAELEGIVEL", ReportUtils.map(patientsIlegibleToViralLoadNotTestStartSites(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("TARGETED", ReportUtils.map(patientsIligebleToViralLoadForTherapeuticFailureSuspect(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		
		cd.setCompositionString("ROTINAELEGIVEL NOT TARGETED");
		return cd;
	}
	
	public CohortDefinition pregnantsIligebleToViralLoad() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("GRÁVIDAS ELEGIVEIS A CARGA VIRAL");
		cd.setDescription("São gravidas inscritas e que estão há mais de 3 meses em TARV");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addSearch("GRAVIDAS", ReportUtils.map(pregnantsInscribedOnARTService(),
		    "startDate=${startDate-6m},endDate=${endDate},location=${location}"));
		cd.addSearch("MAIS3MESESTARV",
		    ReportUtils.map(patientsInARTForMoreThanXMonths(), "endDate=${endDate},location=${location},months=3"));
		cd.addSearch("CONSULTA", ReportUtils.map(patientsWithAtLeastOneClinicConsultation(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		
		cd.setCompositionString("GRAVIDAS AND MAIS3MESESTARV AND CONSULTA");
		return cd;
	}
}
