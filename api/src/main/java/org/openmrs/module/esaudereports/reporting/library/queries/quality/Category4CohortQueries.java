package org.openmrs.module.esaudereports.reporting.library.queries.quality;

/**
 * Classe contém as queries das cohorts de relatórios - Categoria 4: Preenchimento do processo
 * clínico
 * 
 * @author Hamilton Mutaquiha
 */
public class Category4CohortQueries {
	
	/**
	 * MQ_PACIENTES CRIANCAS COM DIAGNOSTICO PREENCHIDO NA PRIMEIRA CONSULTA
	 */
	public static final String CHILDREN_WITH_DIAGNOSE_FILLED_ON_FIRST_CONSULTATION = "select consulta.patient_id"
	        + " from"
	        + " (	select encounter.encounter_id, primeira_consulta.patient_id,primeira_consulta.data_primeira_consulta"
	        + " 	from"
	        + " 		(	select 	p.patient_id,min(encounter_datetime) data_primeira_consulta"
	        + " 			from 	patient p"
	        + " 					inner join encounter e on p.patient_id=e.patient_id"
	        + " 			where 	p.voided=0 and e.voided=0 and"
	        + " 					e.encounter_datetime between :startDate and :endDate and e.location_id=:location and"
	        + " 					e.encounter_type=9"
	        + " 			group by p.patient_id"
	        + " 		) primeira_consulta"
	        + " 		inner join encounter on encounter.patient_id=primeira_consulta.patient_id and encounter.encounter_datetime=primeira_consulta.data_primeira_consulta"
	        + " 	where encounter.encounter_type=9 and encounter.location_id=:location and encounter.voided=0"
	        + " ) consulta" + " inner join obs o on consulta.encounter_id=o.encounter_id"
	        + " where o.voided=0 and o.concept_id in (1030,6285,1040) and o.value_coded=703";
	
	/**
	 * MQ_PACIENTES CRIANCAS COM HISTORIA DE PTV PREENCHIDA
	 */
	public static final String CHILDREN_WITH_PTV_HISTORY_FILLED = "select p.patient_id"
	        + " from 	patient p"
	        + " 		inner join encounter e on p.patient_id=e.patient_id"
	        + " 		inner join obs o on e.encounter_id=o.encounter_id"
	        + " where 	p.voided=0 and e.voided=0 and o.voided=0 and"
	        + " 		e.encounter_datetime between :startDate and :endDate and e.encounter_type=7 and e.location_id=:location and"
	        + " 		o.concept_id in (1501,1502)";
	
	/**
	 * MQ_PACIENTES COM DADOS PESSOAIS REGISTADOS
	 */
	public static final String PATIENTS_WITH_PERSONAL_DETAILS_REGISTERED = "select	p.patient_id"
	        + " from 	patient p"
	        + " 	inner join person pe on p.patient_id=pe.person_id"
	        + " 	inner join person_name pn on p.patient_id=pn.person_id"
	        + " where 	p.voided=0 and pe.gender is not null and pe.birthdate is not null and"
	        + " 	pn.given_name is not null and pn.family_name is not null and trim(pn.given_name)<>'' and trim(pn.family_name)<>'' and trim(pe.gender)<>''";
	
	/**
	 * MQ_PACIENTES COM ENDERECO REGISTADO
	 */
	public static final String PATIENTS_WITH_ADDRESS_REGISTERED = "select patient_id" + " from (" + " select	p.patient_id,"
	        + " 		if(county_district is not null and trim(county_district)<>'',1,0) distrito,"
	        + " 		if(address6 is not null and trim(address6)<>'',1,0) localidade,"
	        + " 		if(address5 is not null and trim(address5)<>'',1,0) bairro,"
	        + " 		if(address1 is not null and trim(address1)<>'',1,0) referencia,"
	        + " 		if(address3 is not null and trim(address3)<>'',1,0) celula" + " from 	patient p"
	        + " 		inner join person_address pa on p.patient_id=pa.person_id"
	        + " where 	p.voided=0 and pa.voided=0) endereco" + " where (distrito+localidade+bairro+celula+referencia)>=3";
}
