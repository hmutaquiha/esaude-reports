package org.openmrs.module.esaudereports.reporting.library.cohort;

import org.openmrs.Location;
import org.openmrs.module.esaudereports.reporting.library.queries.mer.MerCohortQueries;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by Hamilton Mutaquiha on 2/16/18.
 */
@Component
public class MerCohort {
	
	public CohortDefinition artStartInPeriod() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("INICIO DE TRATAMENTO ARV - NUM PERIODO");
		cd.setDescription("Pacientes que iniciaram o tratamento ARV (Na unidade sanitaria seleccionada)");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(MerCohortQueries.ART_START_IN_PERIOD);
		
		return cd;
	}
	
	public CohortDefinition artStartInPeriodByAge() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("INICIO DE TRATAMENTO ARV - NUM PERIODO POR IDADE");
		cd.setDescription("Paciente que iniciaram o tratamento ARV (Na unidade sanitaria seleccionada) por idade");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("minAgeInclusive", "Idade Mínima", Integer.class));
		cd.addParameter(new Parameter("maxAgeExclusive", "Idade Máxima", Integer.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(MerCohortQueries.ART_START_IN_PERIOD_BY_AGE);
		
		return cd;
	}
	
	public CohortDefinition artStartInPeriodByGenderAndAge() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("INICIO DE TRATAMENTO ARV - NUM PERIODO POR SEXO E POR IDADE");
		cd.setDescription("Pacientes que iniciaram o tratamento ARV (Na unidade sanitaria seleccionada) por sexo e por idade");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("gender", "Sexo", Character.class));
		cd.addParameter(new Parameter("minAgeInclusive", "Idade Mínima", Integer.class));
		cd.addParameter(new Parameter("maxAgeExclusive", "Idade Máxima", Integer.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(MerCohortQueries.ART_START_IN_PERIOD_BY_GENDER_AGE);
		
		return cd;
	}
	
	public CohortDefinition artStartInPeriodPregnants() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES QUE INICIARAM TARV NO PERIODO ENQUANTO GRAVIDAS");
		cd.setDescription("Grávidas que iniciaram o tratamento ARV (Na unidade sanitaria seleccionada)");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(MerCohortQueries.ART_START_IN_PERIOD_PREGNANTS);
		
		return cd;
	}
	
	public CohortDefinition artStartInPeriodBreastfeedings() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES QUE INICIARAM TARV NO PERIODO ENQUANTO A AMAMENTAR");
		cd.setDescription("Lactantes que iniciaram o tratamento ARV (Na unidade sanitaria seleccionada)");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(MerCohortQueries.ART_START_IN_PERIOD_BREASTFEEDINGS);
		
		return cd;
	}
	
	public CohortDefinition artStartInPeriodWithTB() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("INICIO DE TRATAMENTO ARV - NUM PERIODO COM CONFIRMAÇÃO DE TB");
		cd.setDescription("Pacientes que iniciAram o tratamento ARV (Na unidade sanitaria seleccionada) com TB confirmado");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("minAgeInclusive", "Idade Mínima", Integer.class));
		cd.addParameter(new Parameter("maxAgeExclusive", "Idade Máxima", Integer.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(MerCohortQueries.ART_START_IN_PERIOD_WITH_TB);
		
		return cd;
	}
	
	public CohortDefinition inTreatmentInPeriod() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("EM TRATAMENTO ARV - NUM PERIODO");
		cd.setDescription("Pacientes em tratamento ARV (Na unidade sanitaria seleccionada)");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(MerCohortQueries.IN_TREATMENT_IN_PERIOD);
		
		return cd;
	}
	
	public CohortDefinition inTreatmentInPeriodByAge() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("EM TRATAMENTO ARV - NUM PERIODO");
		cd.setDescription("Pacientes em tratamento ARV (Na unidade sanitaria seleccionada)");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("minAgeInclusive", "Idade Mínima", Integer.class));
		cd.addParameter(new Parameter("maxAgeExclusive", "Idade Máxima", Integer.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(MerCohortQueries.IN_TREATMENT_IN_PERIOD_BY_AGE);
		
		return cd;
	}
	
	public CohortDefinition inTreatmentInPeriodByGenderAndAge() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("EM TRATAMENTO ARV - NUM PERIODO");
		cd.setDescription("Pacientes em tratamento ARV (Na unidade sanitaria seleccionada)");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("gender", "Sexo", Character.class));
		cd.addParameter(new Parameter("minAgeInclusive", "Idade Mínima", Integer.class));
		cd.addParameter(new Parameter("maxAgeExclusive", "Idade Máxima", Integer.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.setQuery(MerCohortQueries.IN_TREATMENT_IN_PERIOD_BY_AGE_AND_GENDER);
		
		return cd;
	}
	
	public CohortDefinition artStartInPeriodRetention() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("INICIO DE TRATAMENTO ARV - NUM PERIODO: RETENÇÃO");
		cd.setDescription("Pacientes que iniciaram o tratamento ARV (Na unidade sanitaria seleccionada)");
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addParameter(new Parameter("months", "Meses", Integer.class));
		cd.setQuery(MerCohortQueries.ART_START_IN_PERIOD_RETENTION);
		
		return cd;
	}
	
	public CohortDefinition artStartInPeriodByAgeRetention() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("INICIO DE TRATAMENTO ARV - NUM PERIODO POR IDADE: RETENÇÃO");
		cd.setDescription("Paciente que iniciaram o tratamento ARV (Na unidade sanitaria seleccionada) por idade");
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("minAgeInclusive", "Idade Mínima", Integer.class));
		cd.addParameter(new Parameter("maxAgeExclusive", "Idade Máxima", Integer.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addParameter(new Parameter("months", "Meses", Integer.class));
		cd.setQuery(MerCohortQueries.ART_START_IN_PERIOD_BY_AGE_RETENTION);
		
		return cd;
	}
	
	public CohortDefinition artStartInPeriodByGenderAndAgeRetention() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("INICIO DE TRATAMENTO ARV - NUM PERIODO POR SEXO E POR IDADE: RETENÇÃO");
		cd.setDescription("Pacientes que iniciaram o tratamento ARV (Na unidade sanitaria seleccionada) por sexo e por idade");
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("gender", "Sexo", Character.class));
		cd.addParameter(new Parameter("minAgeInclusive", "Idade Mínima", Integer.class));
		cd.addParameter(new Parameter("maxAgeExclusive", "Idade Máxima", Integer.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addParameter(new Parameter("months", "Meses", Integer.class));
		cd.setQuery(MerCohortQueries.ART_START_IN_PERIOD_BY_AGE_AND_GENDER_RETENTION);
		
		return cd;
	}
	
	public CohortDefinition artStartInPeriodPregnantsRetention() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES QUE INICIARAM TARV NO PERIODO ENQUANTO GRAVIDAS: RETENÇÃO");
		cd.setDescription("Grávidas que iniciaram o tratamento ARV (Na unidade sanitaria seleccionada)");
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addParameter(new Parameter("months", "Meses", Integer.class));
		cd.setQuery(MerCohortQueries.ART_START_IN_PERIOD_PREGNANTS_RETENTION);
		
		return cd;
	}
	
	public CohortDefinition artStartInPeriodBreastfeedingsRetention() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES QUE INICIARAM TARV NO PERIODO ENQUANTO A AMAMENTAR: RETENÇÃO");
		cd.setDescription("Lactantes que iniciaram o tratamento ARV (Na unidade sanitaria seleccionada)");
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addParameter(new Parameter("months", "Meses", Integer.class));
		cd.setQuery(MerCohortQueries.ART_START_IN_PERIOD_BREASTFEEDINGS_RETENTION);
		
		return cd;
	}
	
	public CohortDefinition inTreatmentInPeriodRetention() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("EM TRATAMENTO ARV - NUM PERIODO: RETENÇÃO");
		cd.setDescription("Pacientes em tratamento ARV (Na unidade sanitaria seleccionada)");
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addParameter(new Parameter("months", "Meses", Integer.class));
		cd.setQuery(MerCohortQueries.IN_TREATMENT_IN_PERIOD_RETENTION);
		
		return cd;
	}
	
	public CohortDefinition inTreatmentInPeriodByAgeRetention() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("EM TRATAMENTO ARV - NUM PERIODO POR IDADE: RETENÇÃO");
		cd.setDescription("Paciente em tratamento ARV (Na unidade sanitaria seleccionada) por idade");
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("minAgeInclusive", "Idade Mínima", Integer.class));
		cd.addParameter(new Parameter("maxAgeExclusive", "Idade Máxima", Integer.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addParameter(new Parameter("months", "Meses", Integer.class));
		cd.setQuery(MerCohortQueries.IN_TREATMENT_IN_PERIOD_BY_AGE_RETENTION);
		
		return cd;
	}
	
	public CohortDefinition inTreatmentInPeriodByGenderAndAgeRetention() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("EM TRATAMENTO ARV - NUM PERIODO POR SEXO E POR IDADE: RETENÇÃO");
		cd.setDescription("Pacientes em tratamento ARV (Na unidade sanitaria seleccionada) por sexo e por idade");
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("gender", "Sexo", Character.class));
		cd.addParameter(new Parameter("minAgeInclusive", "Idade Mínima", Integer.class));
		cd.addParameter(new Parameter("maxAgeExclusive", "Idade Máxima", Integer.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addParameter(new Parameter("months", "Meses", Integer.class));
		cd.setQuery(MerCohortQueries.IN_TREATMENT_IN_PERIOD_BY_AGE_AND_GENDER_RETENTION);
		
		return cd;
	}
	
	public CohortDefinition inTreatmentInPeriodPregnantsRetention() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("GRÁVIDAS EM TRATAMENTO ARV NO PERIODO: RETENÇÃO");
		cd.setDescription("Grávidas em tratamento ARV (Na unidade sanitaria seleccionada)");
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addParameter(new Parameter("months", "Meses", Integer.class));
		cd.setQuery(MerCohortQueries.IN_TREATMENT_IN_PERIOD_PREGNANTS_RETENTION);
		
		return cd;
	}
	
	public CohortDefinition inTreatmentInPeriodBreastfeedingsRetention() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("LACTANTES EM TRATAMENTO ARV NO PERIODO: RETENÇÃO");
		cd.setDescription("Lactantes em tratamento ARV (Na unidade sanitaria seleccionada)");
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addParameter(new Parameter("months", "Meses", Integer.class));
		cd.setQuery(MerCohortQueries.IN_TREATMENT_IN_PERIOD_BREASTFEEDINGS_RETENTION);
		
		return cd;
	}
	
	public CohortDefinition patientsWithViralLoadReportedLast12Months() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES COM CARGA VIRAL REPORTADA NOS ÚLTIMOS 12 MESES");
		cd.setDescription("Pacientes em tratamento ARV (Na unidade sanitaria seleccionada)");
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addParameter(new Parameter("months", "Meses", Integer.class));
		cd.setQuery(MerCohortQueries.PATIENTS_WITH_REPORTED_VIRAL_LOAD_LAST_12_MONTHS);
		
		return cd;
	}
	
	public CohortDefinition patientsWithViralLoadReportedLast12MonthsByAge() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES COM CARGA VIRAL REPORTADA NOS ÚLTIMOS 12 MESES POR IDADE");
		cd.setDescription("Paciente em tratamento ARV (Na unidade sanitaria seleccionada) por idade");
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("minAgeInclusive", "Idade Mínima", Integer.class));
		cd.addParameter(new Parameter("maxAgeExclusive", "Idade Máxima", Integer.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addParameter(new Parameter("months", "Meses", Integer.class));
		cd.setQuery(MerCohortQueries.PATIENTS_WITH_REPORTED_VIRAL_LOAD_LAST_12_MONTHS_BY_AGE);
		
		return cd;
	}
	
	public CohortDefinition patientsWithViralLoadReportedLast12MonthsByGenderAndAge() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES COM CARGA VIRAL REPORTADA NOS ÚLTIMOS 12 MESES POR SEXO E POR IDADE");
		cd.setDescription("Pacientes em tratamento ARV (Na unidade sanitaria seleccionada) por sexo e por idade");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("gender", "Sexo", Character.class));
		cd.addParameter(new Parameter("minAgeInclusive", "Idade Mínima", Integer.class));
		cd.addParameter(new Parameter("maxAgeExclusive", "Idade Máxima", Integer.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addParameter(new Parameter("months", "Meses", Integer.class));
		cd.setQuery(MerCohortQueries.PATIENTS_WITH_REPORTED_VIRAL_LOAD_LAST_12_MONTHS_BY_AGE_AND_GENDER);
		
		return cd;
	}
	
	public CohortDefinition patientsWithViralLoadReportedLast12MonthsPregnants() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES GRÁVIDAS COM CARGA VIRAL REPORTADA NOS ÚLTIMOS 12 MESES");
		cd.setDescription("Grávidas em tratamento ARV (Na unidade sanitaria seleccionada)");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addParameter(new Parameter("months", "Meses", Integer.class));
		cd.setQuery(MerCohortQueries.PATIENTS_WITH_REPORTED_VIRAL_LOAD_LAST_12_MONTHS_PREGNANTS);
		
		return cd;
	}
	
	public CohortDefinition patientsWithViralLoadReportedLast12MonthsBreastfeedings() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES LACTANTES COM CARGA VIRAL REPORTADA NOS ÚLTIMOS 12 MESES");
		cd.setDescription("Lactantes em tratamento ARV (Na unidade sanitaria seleccionada)");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addParameter(new Parameter("months", "Meses", Integer.class));
		cd.setQuery(MerCohortQueries.PATIENTS_WITH_REPORTED_VIRAL_LOAD_LAST_12_MONTHS_BREASTFEEDINGS);
		
		return cd;
	}
	
	public CohortDefinition patientsWithUndetectableViralLoadLast12Months() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES COM CARGA VIRAL INDETECTÁVEL NOS ÚLTIMOS 12 MESES");
		cd.setDescription("Pacientes em tratamento ARV (Na unidade sanitaria seleccionada)");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addParameter(new Parameter("months", "Meses", Integer.class));
		cd.setQuery(MerCohortQueries.PATIENTS_WITH_UNDETECTABLE_VIRAL_LOAD_LAST_12_MONTHS);
		
		return cd;
	}
	
	public CohortDefinition patientsWithUndetectableViralLoadLast12MonthsByAge() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES COM CARGA VIRAL INDETECTÁVEL NOS ÚLTIMOS 12 MESES POR IDADE");
		cd.setDescription("Paciente em tratamento ARV (Na unidade sanitaria seleccionada) por idade");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("minAgeInclusive", "Idade Mínima", Integer.class));
		cd.addParameter(new Parameter("maxAgeExclusive", "Idade Máxima", Integer.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addParameter(new Parameter("months", "Meses", Integer.class));
		cd.setQuery(MerCohortQueries.PATIENTS_WITH_UNDETECTABLE_VIRAL_LOAD_LAST_12_MONTHS_BY_AGE);
		
		return cd;
	}
	
	public CohortDefinition patientsWithUndetectableViralLoadLast12MonthsByGenderAndAge() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES COM CARGA VIRAL INDETECTÁVEL NOS ÚLTIMOS 12 MESES POR SEXO E POR IDADE");
		cd.setDescription("Pacientes em tratamento ARV (Na unidade sanitaria seleccionada) por sexo e por idade");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("gender", "Sexo", Character.class));
		cd.addParameter(new Parameter("minAgeInclusive", "Idade Mínima", Integer.class));
		cd.addParameter(new Parameter("maxAgeExclusive", "Idade Máxima", Integer.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addParameter(new Parameter("months", "Meses", Integer.class));
		cd.setQuery(MerCohortQueries.PATIENTS_WITH_UNDETECTABLE_VIRAL_LOAD_LAST_12_MONTHS_BY_AGE_AND_GENDER);
		
		return cd;
	}
	
	public CohortDefinition patientsWithUndetectableViralLoadLast12MonthsPregnants() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES GRÁVIDAS COM CARGA VIRAL INDETECTÁVEL NOS ÚLTIMOS 12 MESES");
		cd.setDescription("Grávidas em tratamento ARV (Na unidade sanitaria seleccionada)");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addParameter(new Parameter("months", "Meses", Integer.class));
		cd.setQuery(MerCohortQueries.PATIENTS_WITH_UNDETECTABLE_VIRAL_LOAD_LAST_12_MONTHS_PREGNANTS);
		
		return cd;
	}
	
	public CohortDefinition patientsWithUndetectableViralLoadLast12MonthsBreastfeedings() {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("PACIENTES LACTANTES COM CARGA VIRAL INDETECTÁVEL NOS ÚLTIMOS 12 MESES");
		cd.setDescription("Lactantes em tratamento ARV (Na unidade sanitaria seleccionada)");
		cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
		cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
		cd.addParameter(new Parameter("location", "Location", Location.class));
		cd.addParameter(new Parameter("months", "Meses", Integer.class));
		cd.setQuery(MerCohortQueries.PATIENTS_WITH_UNDETECTABLE_VIRAL_LOAD_LAST_12_MONTHS_BREASTFEEDINGS);
		
		return cd;
	}
}
