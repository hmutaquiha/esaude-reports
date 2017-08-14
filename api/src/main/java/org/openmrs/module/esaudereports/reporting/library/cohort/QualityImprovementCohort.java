/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 * <p>
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 * <p>
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.esaudereports.reporting.library.cohort;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Cohort;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.api.PatientSetService;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.hibernate.HibernateConceptDAO;
import org.openmrs.api.db.hibernate.HibernateEncounterDAO;
import org.openmrs.module.esaudereports.reporting.cohort.definition.DateObsValueBetweenCohortDefinition;
import org.openmrs.module.esaudereports.reporting.library.queries.*;
import org.openmrs.module.esaudereports.reporting.metadata.Metadata;
import org.openmrs.module.reporting.cohort.definition.*;
import org.openmrs.module.reporting.cohort.query.service.CohortQueryService;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.reporting.common.ObjectUtil;
import org.openmrs.module.reporting.common.RangeComparator;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

import java.util.*;

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
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("INSCRITOS NO PERIODO DE INCLUSAO E QUE TIVERAM CONSULTA CLINICA NO MESMO DIA DA INSCRIÇÃO");
		cd.setDescription("São pacientes inscritos no periodo de inclusao e que tiveram consulta clinica no mesmo dia da inscrição");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("revisionEndDate", "Data Final de Revisão", Date.class));
		cd.addParameter(new Parameter("location", "US", Location.class));
		cd.addSearch("INSCRITOS", Mapped.map(patientsSubscribedInclusionPeriodPreARTSample(),
		    "startDate=${startDate},endDate=${endDate},location=${location},revisionEndDate=${revisionEndDate}"));
		cd.addSearch("CONSULTAS", Mapped.map(patientsWithClinicConsultationOnSubscriptionDate(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.setCompositionString("INSCRITOS AND CONSULTAS");
		
		return cd;
	}
	
	/**
	 * Clinical consultation denominator
	 * 
	 * @return CohortDefinition
	 */
	public CohortDefinition clinicalConsulationsDenominator() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("INICIO TARV NO PERIODO DE INCLUSAO (AMOSTRA TARV)");
		cd.setDescription("São pacientes que iniciaram TARV dentro do periodo de inclusao");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("revisionEndDate", "Data Final de Revisão", Date.class));
		cd.addParameter(new Parameter("location", "US", Location.class));
		cd.setQuery(CohortQueries.SUBSCRIBED_INCLUSION_PERIOD_PRE_ART_SAMPLE);
		
		return cd;
	}
	
	public CohortDefinition followUpClinicalConsulationsNumerator1() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("INSCRITOS NO PERÍODO DE INCLUSÃO (AMOSTRA PRE-TARV), QUE TIVERAM 3 CONSULTAS DENTRO 6 MESES APOS INSCRICAO");
		cd.setDescription("Sao pacientes inscritos no periodo de inclusão, e que tiveram no minimo 3 consultas clinicas nos primeiros 6 meses apos a inscricao ");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("revisionEndDate", "Data Final de Revisão", Date.class));
		cd.addParameter(new Parameter("location", "US", Location.class));
		cd.addSearch("PRETARV", Mapped.map(patientsWith3ConsultationsWithin6MonthsAfterEnrollment(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("INSCRITOS", Mapped.map(patientsSubscribedInclusionPeriodPreARTSample(),
		    "startDate=${startDate},endDate=${endDate},location=${location},revisionEndDate=${revisionEndDate}"));
		cd.setCompositionString("PRETARV AND INSCRITOS");
		return cd;
	}
	
	public CohortDefinition followUpClinicalConsulationsNumerator2() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES TARV NO PERIODO DE INCLUSAO QUE TIVERAM PELO MENOS 3 CONSULTAS NOS PRIMEIROS 6 MESES DE TARV");
		cd.setDescription("São pacientes na amostra de TARV e que não tiveram episodio de saida antes dos 6 meses de TARV e que tiveram pelo menos 3 consultas clinicas nos primeiros 6 meses de tratamento ARV");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("revisionEndDate", "Data Final de Revisão", Date.class));
		cd.addParameter(new Parameter("location", "US", Location.class));
		cd.addSearch("CONSULTAS", Mapped.map(patientsWhoStartedARTWithAlLeast3ConsultationsBefore6MonthsOfART(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("INICIO", Mapped.map(patientsWhoStartedARTInclusionPeriod(),
		    "startDate=${startDate},endDate=${endDate},location=${location},revisionEndDate=${revisionEndDate}"));
		cd.setCompositionString("CONSULTAS AND INICIO");
		return cd;
	}
	
	public CohortDefinition followUpClinicalConsulationsNumerator3() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES NA AMOSTRA (INICIO TARV) E QUE TIVERAM CONSULTAS MENSAIS APOS INICIO DE TARV");
		cd.setDescription("Sao pacientes na amostra de inicio de TARV e que tiveram consultas mensais dentro do periodo em anáslise");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("revisionEndDate", "Data Final de Revisão", Date.class));
		cd.addParameter(new Parameter("location", "US", Location.class));
		cd.addSearch("CONSULTAS", Mapped.map(patientsWithMonthlyConsultations(),
		    "startDate=${startDate},endDate=${endDate},location=${location},revisionEndDate=${revisionEndDate}"));
		cd.addSearch("INICIO", Mapped.map(patientsWhoStartedARTInclusionPeriod(),
		    "startDate=${startDate},endDate=${endDate},location=${location},revisionEndDate=${revisionEndDate}"));
		cd.setCompositionString("CONSULTAS AND INICIO");
		return cd;
	}
	
	public CohortDefinition patientsWith3ConsultationsWithin6MonthsAfterEnrollment() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES QUE TIVERAM 3 CONSULTAS DENTRO DE 6 MESES APOS INSCRICAO");
		cd.setDescription("Sao pacientes que tiveram 3 consultas dentro de 6 meses apos inscrição");
		cd.addParameter(new Parameter("startDate", "Data Inicial Inclusao", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final Inclusao", Date.class));
		cd.addParameter(new Parameter("location", "US", Location.class));
		cd.setQuery(Category3CohortQueries.PATIENTS_WITH_3_CONSULTATIONS_WITHIN_6_MONTHS_AFTER_ENROLLMENT);
		
		return cd;
	}
	
	public CohortDefinition patientsWhoStartedARTWithAlLeast3ConsultationsBefore6MonthsOfART() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES QUE INICIARAM TARV E QUE TIVERAM NO MINIMO 3 CONSULTAS ANTES DOS 6 MESES DE TARV");
		cd.setDescription("Sao pacientes que iniciaram tarv e que tiveram pelo menos 3 consultas clinicas até os primeiros 6 meses depois de inicio de TARV");
		cd.addParameter(new Parameter("startDate", "Data Inicial Inclusao", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final Inclusao", Date.class));
		cd.addParameter(new Parameter("location", "US", Location.class));
		cd.setQuery(Category3CohortQueries.PATIENTS_WHO_STARTED_ART_WITH_AT_LEAST_3_CONSULTATIONS_BEFORE_6_MONTHS_OF_ART);
		
		return cd;
	}
	
	public CohortDefinition patientsWithMonthlyConsultations() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES COM CONSULTAS MENSAIS ");
		cd.setDescription("Sao pacientes que tem consultas mensais durante um periodo: O Calculo de consultas mensais é: O numero de consultas no periodo deve ser superior ou igual ao numero de meses desse periodo ");
		cd.addParameter(new Parameter("startDate", "Data Inicial Inclusao", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final Inclusao", Date.class));
		cd.addParameter(new Parameter("revisionEndDate", "Data Final de Avaliacao", Date.class));
		cd.addParameter(new Parameter("location", "US", Location.class));
		cd.setQuery(Category3CohortQueries.PATIENTS_WITH_MONTHLY_CONSULTATIONS);
		
		return cd;
	}
	
	public CohortDefinition followUpClinicalConsulationsDenominator() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("INICIO TARV NO PERIODO DE INCLUSAO (AMOSTRA TARV)");
		cd.setDescription("São pacientes que iniciaram TARV dentro do periodo de inclusao");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("revisionEndDate", "Data Final de Revisão", Date.class));
		cd.addParameter(new Parameter("location", "US", Location.class));
		cd.setQuery(Category3CohortQueries.ART_START_INCLUSION_PERIOD_ART_SAMPLE);
		return cd;
	}
	
	public CohortDefinition patientsWhoStartedARTInclusionPeriod() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("INICIO TARV NO PERIODO DE INCLUSAO (AMOSTRA TARV)");
		cd.setDescription("São pacientes que iniciaram TARV dentro do periodo de inclusao");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("revisionEndDate", "Data Final de Revisão", Date.class));
		cd.addParameter(new Parameter("location", "US", Location.class));
		cd.setQuery(CohortQueries.ART_START_INCLUSION_PERIOD_ART_SAMPLE);
		
		return cd;
	}
	
	public CohortDefinition patientsWithAtLeast3EnrollmentEvaluation3MonthsAfterARTStart() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES QUE TIVERAM PELO MENOS 3 AVALIAÇÕES DE ADESÃO DENTRO DE 3 MESES DEPOIS DE INICIO DE TARV)");
		cd.setDescription("São pacientes que tiveram 3 avaliações de adesão (Ficha de apoio psicossocial e PP) dentro de 3 meses depois de inicio de TARV. A avaliação de adesão feita no mesmo dia de inicio de TARV não é contada");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "US", Location.class));
		cd.addParameter(new Parameter("testStart", "Testar Iniciar", Boolean.class));
		cd.setQuery(CohortQueries.PATIENTS_WITH_AT_LEAST_3_ENROLLMENT_EVALUATION_3_MONTHS_AFTER_START_ART);
		return cd;
	}
	
	public CohortDefinition patientsARTSampleWithAtLeast3EnrollmentEvaluation3MonthsAfterARTStart() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES NA AMOSTRA TARV QUE TIVERAM PELO MENOS 3 AVALIAÇÕES DE ADESÃO DENTRO DE 3 MESES DEPOIS DE INICIO DE TARV");
		cd.setDescription("São pacientes na amostra de TARV de melhoria de qualidade e que tiveram pelo menos 3 avaliações de adesão nos primeiros 3 meses de inicio de TARV. A avalição de adesão que coincide com a data de inicio de TARV não é contad");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("revisionEndDate", "Data Final de Revisão", Date.class));
		cd.addParameter(new Parameter("location", "US", Location.class));
		cd.addParameter(new Parameter("testStart", "Testar Iniciar", Boolean.class));
		cd.addSearch("AMOSTRATARV", Mapped.mapStraightThrough(patientsWhoStartedARTInclusionPeriod()));
		cd.addSearch("AVALIACAOADESAO",
		    Mapped.mapStraightThrough(patientsWithAtLeast3EnrollmentEvaluation3MonthsAfterARTStart()));
		cd.setCompositionString("AMOSTRATARV AND AVALIACAOADESAO");
		
		return cd;
	}
	
	public CohortDefinition pregnantSubrcribedOnART() {
		SqlCohortDefinition cohort = new SqlCohortDefinition();
		cohort.setName("GRAVIDAS INSCRITAS NO SERVIÇO TARV)");
		cohort.setDescription("São pacientes que iniciaram TARV dentro do periodo de inclusao");
		cohort.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cohort.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cohort.addParameter(new Parameter("location", "US", Location.class));
		cohort.setQuery(CohortQueries.PREGNANTS_ENROLLED_ART_SERVICE);
		return cohort;
	}
	
	public CohortDefinition patientsSubscribedInclusionPeriodPreARTSample() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("INSCRITOS NO PERIODO DE INCLUSAO (AMOSTRA PRE-TARV)");
		cd.setDescription("São pacientes que abriram processo no priodo de inclusao (amostra pre-tarv)");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("revisionEndDate", "Data Final de Revisão", Date.class));
		cd.addParameter(new Parameter("location", "US", Location.class));
		cd.setQuery(Category2CohortQueries.SUBSCRIBED_INCLUSION_PERIOD_PRE_ART_SAMPLE);
		
		return cd;
	}
	
	public CohortDefinition patientsWithClinicConsultationOnSubscriptionDate() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES COM CONSULTA CLINICA NA MESMA DATA DA INSCRIÇÃO ");
		cd.setDescription("São pacientes que tiveram consulta clinica na mesma data da inscricao no servico TARV");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "US", Location.class));
		cd.setQuery(Category2CohortQueries.PATIENTS_WITH_CLINIC_CONSULTATION_ON_SUBSCRIPTION_DATE);
		
		return cd;
	}
	
	public CohortDefinition childrenWithDiagnoseFilledOnFirstConsultation() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES CRIANCAS COM DIAGNOSTICO PREENCHIDO NA PRIMEIRA CONSULTA");
		cd.setDescription("São pacientes crianças com o resultado de diagnostico preenchido na primeira consulta de seguimento");
		cd.addParameter(new Parameter("startDate", "Data Inicial Inclusao", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final Inclusao", Date.class));
		cd.addParameter(new Parameter("location", "US", Location.class));
		cd.setQuery(Category4CohortQueries.CHILDREN_WITH_DIAGNOSE_FILLED_ON_FIRST_CONSULTATION);
		
		return cd;
	}
	
	public CohortDefinition childrenWitPTVHistoryFilled() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES CRIANCAS COM HISTORIA DE PTV PREENCHIDA");
		cd.setDescription("São pacientes crianças com historia de PTV preenchida, isto é, tem preenchido: Exposição perinatal a ARV-mãe e Exposição perinatal a ARV- à nascença no processo clinico");
		cd.addParameter(new Parameter("startDate", "Data Inicial Inclusao", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final Avaliacao", Date.class));
		cd.addParameter(new Parameter("location", "US", Location.class));
		cd.setQuery(Category4CohortQueries.CHILDREN_WITH_PTV_HISTORY_FILLED);
		
		return cd;
	}
	
	public CohortDefinition patientsWithPersonalDetailsRegistered() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES COM DADOS PESSOAIS REGISTADOS ");
		cd.setDescription("São pacientes que tem preenchido os seguintes dados: Nome, Apelido, Sexo, data de nascimento/idade real ou aproximada");
		cd.setQuery(Category4CohortQueries.PATIENTS_WITH_PERSONAL_DETAILS_REGISTERED);
		
		return cd;
	}
	
	public CohortDefinition patientsInCohortSample() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("AMOSTRA DE PACIENTES NA COORTE");
		cd.setDescription("Amostra de pacientes que fazem parte da coorte de análise de melhoria de qualidade: Inscritos ou inicio tarv 3 meses antes do periodo de revisão");
		cd.addParameter(new Parameter("startDate", "Data Inicial Inclusao", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final Inclusao", Date.class));
		cd.addParameter(new Parameter("revisionEndDate", "Data Final de Avaliacao", Date.class));
		cd.addParameter(new Parameter("location", "US", Location.class));
		cd.addSearch("INSCRITOS", Mapped.map(patientsSubscribedInclusionPeriodPreARTSample(),
		    "startDate=${startDate},endDate=${endDate},location=${location},revisionEndDate=${revisionEndDate}"));
		cd.addSearch("INICIO", Mapped.map(patientsWhoStartedARTInclusionPeriod(),
		    "startDate=${startDate},endDate=${endDate},location=${location},revisionEndDate=${revisionEndDate}"));
		cd.setCompositionString("INSCRITOS OR INICIO");
		
		return cd;
	}
	
	public CohortDefinition patientsWithAddressRegistered() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES COM ENDERECO REGISTADO");
		cd.setDescription("Considera-se endereco registado, aqueles pacientes que tem pelo menos 3 destes itens registados: Distrito/Cidade; localidade/Bairro; celula/Quarteirao; Avenida/Casa");
		cd.setQuery(Category4CohortQueries.PATIENTS_WITH_ADDRESS_REGISTERED);
		
		return cd;
	}
	
	public CohortDefinition clinicalProcessFillNumerator1() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES NA AMOSTRA (TARV E PRE-TARV) COM DADOS PESSOAIS E DEMOGRAFICOS PREENCHIDOS");
		cd.setDescription("São pacientes que fazem parte da amostra e que tem todos dados pessoais e demograficos preenchidos");
		cd.addParameter(new Parameter("startDate", "Data Inicial Inclusao", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final Inclusao", Date.class));
		cd.addParameter(new Parameter("revisionEndDate", "Data Final de Avaliacao", Date.class));
		cd.addParameter(new Parameter("location", "US", Location.class));
		cd.addSearch("DADOSPESSOAIS", Mapped.map(patientsWithPersonalDetailsRegistered(), StringUtils.EMPTY));
		cd.addSearch("AMOSTRA", Mapped.map(patientsInCohortSample(),
		    "startDate=${startDate},endDate=${endDate},location=${location},revisionEndDate=${revisionEndDate}"));
		cd.addSearch("ENDERECO", Mapped.map(patientsWithAddressRegistered(), StringUtils.EMPTY));
		cd.setCompositionString("DADOSPESSOAIS AND AMOSTRA AND ENDERECO");
		
		return cd;
	}
	
	public CohortDefinition clinicalProcessFillNumerator2() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("AMOSTRA (PRE-TARV) COM HISTORIA DE PTV E RESULTADO DE DIAGNOSTICO PREENCHIDOS");
		cd.setDescription("Sao pacientes pediatricos na amostra com historia de PTV e resultado de diagnostico preenchidos");
		cd.addParameter(new Parameter("startDate", "Data Inicial Inclusao", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final Inclusao", Date.class));
		cd.addParameter(new Parameter("revisionEndDate", "Data Final de Avaliacao", Date.class));
		cd.addParameter(new Parameter("location", "US", Location.class));
		cd.addSearch("DIAGNOSTICO", Mapped.map(childrenWithDiagnoseFilledOnFirstConsultation(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("AMOSTRA", Mapped.map(patientsSubscribedInclusionPeriodPreARTSample(),
		    "startDate=${startDate},endDate=${endDate},location=${location},revisionEndDate=${revisionEndDate}"));
		cd.addSearch("HISTPTV",
		    Mapped.map(childrenWitPTVHistoryFilled(), "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.setCompositionString("DIAGNOSTICO AND AMOSTRA AND HISTPTV");
		
		return cd;
	}
	
	public CohortDefinition clinicalProcessFillDenominator() {
		
		return patientsInCohortSample();
	}
	
	public CohortDefinition convert(CohortDefinition cd, Map<String, String> renamedParameters) {
		return new MappedParametersCohortDefinition(cd, renamedParameters);
	}
	
	public CohortDefinition tBTreatmentStart() {
		DateObsCohortDefinition cd = new DateObsCohortDefinition();
		
		HibernateConceptDAO conceptDao = Context.getRegisteredComponents(HibernateConceptDAO.class).get(0);
		HibernateEncounterDAO encounterDAO = Context.getRegisteredComponents(HibernateEncounterDAO.class).get(0);
		
		List<EncounterType> encounterTypes = new ArrayList<EncounterType>();
		
		encounterTypes.add(encounterDAO.getEncounterTypeByUuid(Metadata._EncounterType.TUBERCULOSE_LIVRO));
		encounterTypes.add(encounterDAO.getEncounterTypeByUuid(Metadata._EncounterType.STARV_ADULTO_SEGUIMENTO));
		encounterTypes.add(encounterDAO.getEncounterTypeByUuid(Metadata._EncounterType.STARV_PEDIATRIA_SEGUIMENTO));
		encounterTypes.add(encounterDAO.getEncounterTypeByUuid(Metadata._EncounterType.TUBERCULOSE_RASTREIO));
		encounterTypes.add(encounterDAO.getEncounterTypeByUuid(Metadata._EncounterType.TUBERCULOSE_PROCESSO));
		
		cd.setName("INICIO DE TRATAMENTO DE TUBERCULOSE DATA NOTIFICADA NAS FICHAS DE: SEGUIMENTO, RASTREIO E LIVRO TB");
		cd.setDescription("Pacientes que iniciram TB com a data de inicio de tratamento de TB notificada nas fichas de seguimento adulto e pediatria, rastreio de tuberculose e livro de TB");
		cd.setQuestion(conceptDao.getConceptByUuid(Metadata._Concept.TB_TREATMENT_START_DATE));
		cd.setEncounterTypeList(encounterTypes);
		cd.setTimeModifier(PatientSetService.TimeModifier.ANY);
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setOperator1(RangeComparator.GREATER_EQUAL);
		cd.addParameter(new Parameter("startDate", "DE", Date.class));
		cd.setOperator2(RangeComparator.LESS_EQUAL);
		cd.addParameter(new Parameter("endDate", "ATE", Date.class));
		
		return cd;
	}
	
	public CohortDefinition patientsInARTAndPreARTSampleNotInTBTreatment() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("MQ_PACIENTES NA AMOSTRA (TARV E PRE-TARV) E QUE NAO SE ENCONTRAM EM TRATAMENTO DE TB");
		cd.setDescription("Sao pacientes na amostra (TARV e PRE-TARV), que não se encontram em tratamento da tuberculose");
		cd.addParameter(new Parameter("startDate", "Data Inicial Inclusao", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final Inclusao", Date.class));
		cd.addParameter(new Parameter("revisionEndDate", "Data Final Avaliacao", Date.class));
		cd.addParameter(new Parameter("location", "US", Location.class));
		cd.addSearch("AMOSTRA", Mapped.map(patientsInCohortSample(),
		    "startDate=${startDate},endDate=${endDate},location=${location},revisionEndDate=${revisionEndDate}"));
		cd.addSearch("TB", Mapped.map(tBTreatmentStart(), "location=${location}"));
		cd.setCompositionString("AMOSTRA NOT TB");
		
		return cd;
	}
	
	public CohortDefinition patientsWhoHadTBTrackingInEachClinicConsultation() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES QUE TIVERAM RASTREIO DE TUBERCULOSE EM CADA CONSULTA CLINICA");
		cd.setDescription("São pacientes que durante um periodo tiveram consulta clinica e que foram rastreiados para tuberculose em cada visita (Numero de visitas igual ao numero de rastreios)");
		cd.addParameter(new Parameter("startDate", "Data Inicial Inclusao", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final Avaliacao", Date.class));
		cd.addParameter(new Parameter("location", "US", Location.class));
		cd.setQuery(Category7CohortQueries.PATIENTS_WHO_HAD_TB_TRACKING_IN_EACH_CLINIC_CONSULTATION);
		
		return cd;
	}
	
	public CohortDefinition tBTrackingNumerator1() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("PACIENTES NA AMOSTRA (TARV E PRE-TARV) E QUE NAO SE ENCONTRAM EM TRATAMENTO DE TB E RASTREIADOS EM CADA CONSULTA");
		cd.setDescription("Sao pacientes na amostra (TARV e PRE-TARV), que não se encontram em tratamento da tuberculose e que foram rastreiados para tuberculose em cada consulta clinica");
		cd.addParameter(new Parameter("startDate", "Data Inicial Inclusao", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final Inclusao", Date.class));
		cd.addParameter(new Parameter("revisionEndDate", "Data Final Avaliacao", Date.class));
		cd.addParameter(new Parameter("location", "US", Location.class));
		cd.addSearch("AMOSTRA", Mapped.map(patientsInARTAndPreARTSampleNotInTBTreatment(),
		    "startDate=${startDate},endDate=${endDate},location=${location},revisionEndDate=${revisionEndDate}"));
		cd.addSearch("RASTREIO", Mapped.map(patientsWhoHadTBTrackingInEachClinicConsultation(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.setCompositionString("AMOSTRA AND RASTREIO");
		
		return cd;
	}
	
	public CohortDefinition tBTrackingDenominator1() {
		
		return patientsInARTAndPreARTSampleNotInTBTreatment();
	}
	
	public CohortDefinition pregnantsSubscribedInARTServiceWhoStartedARTInInclusionPeriod() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("GRAVIDAS INSCRITAS NO SERVICO TARV E QUE INICIARAM TARV NO PERIODO DE INCLUSAO (AMOSTRA GRAVIDA)");
		cd.setDescription("São gravidas inscritas no serviço TARV e que iniciaram tarv e que fazem parte da amostra para a avaliação de qualidade de dados de MQ");
		cd.addParameter(new Parameter("startDate", "Data Inicial Inclusao", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final Avaliacao", Date.class));
		cd.addParameter(new Parameter("location", "US", Location.class));
		cd.setQuery(Category7CohortQueries.PREGNANTS_SUBSCRIBED_ART_SERVICE_WHO_STARTED_ART_IN_INCLUSION_PERIOD);
		
		return cd;
	}
	
	public CohortDefinition tBTrackingNumerator3() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("GRAVIDAS INSCRITAS NO SERVICO TARV (AMOSTRA GRAVIDA) E QUE FORAM RASTREIADAS PARA TUBERCULOSE EM CADA VISITA");
		cd.setDescription("Amostra de gravida, MQ e que foram rastreiadas para tuberculose em cada visita");
		cd.addParameter(new Parameter("startDate", "Data Inicial Inclusao", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final Inclusao", Date.class));
		cd.addParameter(new Parameter("location", "US", Location.class));
		cd.addSearch("AMOSTRA", Mapped.map(pregnantsSubscribedInARTServiceWhoStartedARTInInclusionPeriod(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("RASTREIO", Mapped.map(patientsWhoHadTBTrackingInEachClinicConsultation(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.setCompositionString("AMOSTRA AND RASTREIO");
		
		return cd;
	}
	
	public CohortDefinition tBTrackingDenominator3() {
		
		return pregnantsSubscribedInARTServiceWhoStartedARTInInclusionPeriod();
	}
	
	public CohortDefinition patientsWhoHadClinicalConsultationAndHadITSTrackingInEachVisit() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES QUE TIVERAM CONSULTA CLINICA NUM PERIODO E QUE TIVERAM RASTREIO DE ITS EM CADA VISITA");
		cd.setDescription("São pacientes que tiveram consulta clinica num periodo e que tiveram rastreio de ITS em cada consulta");
		cd.addParameter(new Parameter("startDate", "Data Inicial Inclusao", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final Avaliacao", Date.class));
		cd.addParameter(new Parameter("location", "US", Location.class));
		cd.setQuery(Category8CohortQueries.PATIENTS_WHO_HAD_CLINICAL_CONSULTATION_AND_HAD_ITS_TRACKING_IN_EACH_VISIT);
		
		return cd;
	}
	
	public CohortDefinition iTSTrackingNumerator1() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("MQ_PACIENTES NA AMOSTRA (TARV E PRE-TARV) E QUE TIVERAM RASTREIO DE ITS EM CADA VISITA DO PERIODO EM ANALISE COORTE");
		cd.setDescription("Sao pacientes que fazem parte da amostra e que foram rastreiados para ITS em cada consulta durante o periodo em analise");
		cd.addParameter(new Parameter("startDate", "Data Inicial Inclusao", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final Inclusao", Date.class));
		cd.addParameter(new Parameter("revisionEndDate", "Data Final Avaliacao", Date.class));
		cd.addParameter(new Parameter("location", "US", Location.class));
		cd.addSearch("COORTE", Mapped.map(patientsInCohortSample(),
		    "startDate=${startDate},endDate=${endDate},location=${location},revisionEndDate=${revisionEndDate}"));
		cd.addSearch("ITS", Mapped.map(patientsWhoHadClinicalConsultationAndHadITSTrackingInEachVisit(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.setCompositionString("COORTE AND ITS");
		
		return cd;
	}
	
	public CohortDefinition iTSTrackingDenominator1() {
		
		return patientsInCohortSample();
	}
	
	public CohortDefinition adultsWhoHadClinicalConsultationDuringAPeriodAndWhereStayedInEachVisit() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES ADULTOS QUE TIVERAM CONSULTA CLINICA NUM PERIODO E QUE FORAM ESTADIADOS EM CADA VISITA");
		cd.setDescription("São pacientes adultos que tiveram consultas clinicas durante um determinado periodo e que foram estadiados em cada consulta clinica");
		cd.addParameter(new Parameter("startDate", "Data Inicial Inclusao", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final Avaliacao", Date.class));
		cd.addParameter(new Parameter("location", "US", Location.class));
		cd.setQuery(Category11CohortQueries.ADULTS_WHO_HAD_CLINICAL_CONSULTATION_DURING_A_PERIOD_AND_WERE_STAYED_IN_EACH_VISIT);
		
		return cd;
	}
	
	public CohortDefinition childrenWhoHadClinicalConsultationDuringAPeriodAndWhereStayedInEachVisit() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES CRIANCAS QUE TIVERAM CONSULTA CLINICA NUM PERIODO E QUE FORAM ESTADIADOS EM CADA VISITA");
		cd.setDescription("São pacientes criancas que tiveram consultas clinicas durante um determinado periodo e que foram estadiados em cada consulta clinica");
		cd.addParameter(new Parameter("startDate", "Data Inicial Inclusao", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final Avaliacao", Date.class));
		cd.addParameter(new Parameter("location", "US", Location.class));
		cd.setQuery(Category11CohortQueries.CHILDREN_WHO_HAD_CLINICAL_CONSULTATION_DURING_A_PERIOD_AND_WERE_STAYED_IN_EACH_VISIT);
		
		return cd;
	}
	
	public CohortDefinition wHOStateNumerator1() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("MQ_PACIENTES ADULTOS NA AMOSTRA (TARV E PRE-TARV) E QUE FORAM ESTADIADO EM CADA VISITA NO PERIODO DE ANÁLISE");
		cd.setDescription("Sao pacientes adultos da coorte de estudo que tiveram consulta clinica e estadiado em cada consulta");
		cd.addParameter(new Parameter("startDate", "Data Inicial Inclusao", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final Inclusao", Date.class));
		cd.addParameter(new Parameter("revisionEndDate", "Data Final Avaliacao", Date.class));
		cd.addParameter(new Parameter("location", "US", Location.class));
		cd.addSearch("ESTADIO", Mapped.map(adultsWhoHadClinicalConsultationDuringAPeriodAndWhereStayedInEachVisit(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("AMOSTRA", Mapped.map(patientsInCohortSample(),
		    "startDate=${startDate},endDate=${endDate},location=${location},revisionEndDate=${revisionEndDate}"));
		cd.setCompositionString("ESTADIO AND AMOSTRA");
		
		return cd;
	}
	
	public CohortDefinition wHOStateNumerator2() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("MQ_PACIENTES CRIANCAS NA AMOSTRA (TARV E PRE-TARV) E QUE FORAM ESTADIADO EM CADA VISITA NO PERIODO DE ANÁLISE");
		cd.setDescription("Sao pacientes criancas da coorte de estudo que tiveram consulta clinica e estadiado em cada consulta");
		cd.addParameter(new Parameter("startDate", "Data Inicial Inclusao", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final Inclusao", Date.class));
		cd.addParameter(new Parameter("revisionEndDate", "Data Final Avaliacao", Date.class));
		cd.addParameter(new Parameter("location", "US", Location.class));
		cd.addSearch("ESTADIO", Mapped.map(childrenWhoHadClinicalConsultationDuringAPeriodAndWhereStayedInEachVisit(),
		    "startDate=${startDate},endDate=${endDate},location=${location}"));
		cd.addSearch("COORTE", Mapped.map(patientsInCohortSample(),
		    "startDate=${startDate},endDate=${endDate},location=${location},revisionEndDate=${revisionEndDate}"));
		cd.setCompositionString("ESTADIO AND COORTE");
		
		return cd;
	}
	
	public CohortDefinition wHOStateDenominator1() {
		
		return patientsInCohortSample();
	}
}
