package org.openmrs.module.esaudereports.reporting.library.queries.quality;

/**
 * Classe contém as queries das cohorts de relatórios - Categoria 8: Rastreio de ITS
 * 
 * @author Hamilton Mutaquiha
 */
public class Category8CohortQueries {
	
	/**
	 * MQ_PACIENTES QUE TIVERAM CONSULTA CLINICA NUM PERIODO E QUE TIVERAM RASTREIO DE ITS EM CADA
	 * VISITA
	 */
	public static final String PATIENTS_WHO_HAD_CLINICAL_CONSULTATION_AND_HAD_ITS_TRACKING_IN_EACH_VISIT = "select consulta.patient_id"
	        + " from"
	        + " (Select 	p.patient_id,count(*) consultas"
	        + " from 	patient p inner join encounter e on e.patient_id=p.patient_id"
	        + " where 	p.voided=0 and e.voided=0 and e.encounter_datetime between :startDate and :endDate and"
	        + " 		e.location_id=:location and e.encounter_type in (6,9)"
	        + " group by patient_id) consulta"
	        + " inner join"
	        + " (Select 	p.patient_id,count(*) itss"
	        + " from 	patient p"
	        + " 		inner join encounter e on e.patient_id=p.patient_id"
	        + " 		inner join obs o on o.encounter_id=e.encounter_id"
	        + " where 	p.voided=0 and e.voided=0 and e.encounter_datetime between :startDate and :endDate and"
	        + " 		e.location_id=:location and e.encounter_type in (6,9) and o.voided=0 and o.concept_id=6258"
	        + " group by patient_id) its on consulta.patient_id=its.patient_id" + " where its.itss>=consulta.consultas";
}
