package org.openmrs.module.esaudereports.reporting.library.queries;

/**
 * Classe contém as queries das cohorts de relatórios - Categoria 11:Estadiamento OMS
 * 
 * @author Hamilton Mutaquiha
 */
public class Category11CohortQueries {
	
	/**
	 * MQ_PACIENTES ADULTOS QUE TIVERAM CONSULTA CLINICA NUM PERIODO E QUE FORAM ESTADIADOS EM CADA
	 * VISITA
	 */
	public static final String ADULTS_WHO_HAD_CLINICAL_CONSULTATION_DURING_A_PERIOD_AND_WERE_STAYED_IN_EACH_VISIT = "select consulta.patient_id"
	        + " from"
	        + " (Select 	p.patient_id,count(*) consultas"
	        + " from 	patient p inner join encounter e on e.patient_id=p.patient_id"
	        + " where 	p.voided=0 and e.voided=0 and e.encounter_datetime between :startDate and :endDate and"
	        + " 		e.location_id=:location and e.encounter_type=6"
	        + " group by patient_id) consulta"
	        + " inner join"
	        + " (Select 	p.patient_id,count(*) estadios"
	        + " from 	patient p"
	        + " 		inner join encounter e on e.patient_id=p.patient_id"
	        + " 		inner join obs o on o.encounter_id=e.encounter_id"
	        + " where 	p.voided=0 and e.voided=0 and e.encounter_datetime between :startDate and :endDate and"
	        + " 		e.location_id=:location and e.encounter_type=6 and o.voided=0 and o.concept_id=5356"
	        + " group by patient_id) estadio on consulta.patient_id=estadio.patient_id"
	        + " where estadio.estadios>=consulta.consultas";
	
	/**
	 * MQ_PACIENTES CRIANCAS QUE TIVERAM CONSULTA CLINICA NUM PERIODO E QUE FORAM ESTADIADOS EM CADA
	 * VISITA
	 */
	public static final String CHILDREN_WHO_HAD_CLINICAL_CONSULTATION_DURING_A_PERIOD_AND_WERE_STAYED_IN_EACH_VISIT = "select consulta.patient_id"
	        + " from"
	        + " (Select 	p.patient_id,count(*) consultas"
	        + " from 	patient p inner join encounter e on e.patient_id=p.patient_id"
	        + " where 	p.voided=0 and e.voided=0 and e.encounter_datetime between :startDate and :endDate and "
	        + " 		e.location_id=:location and e.encounter_type=9"
	        + " group by patient_id) consulta"
	        + " inner join"
	        + " (Select 	p.patient_id,count(*) estadios"
	        + " from 	patient p"
	        + " 		inner join encounter e on e.patient_id=p.patient_id"
	        + " 		inner join obs o on o.encounter_id=e.encounter_id"
	        + " where 	p.voided=0 and e.voided=0 and e.encounter_datetime between :startDate and :endDate and"
	        + " 		e.location_id=:location and e.encounter_type=9 and o.voided=0 and o.concept_id=5356"
	        + " group by patient_id) estadio on consulta.patient_id=estadio.patient_id"
	        + " where estadio.estadios>=consulta.consultas";
}
