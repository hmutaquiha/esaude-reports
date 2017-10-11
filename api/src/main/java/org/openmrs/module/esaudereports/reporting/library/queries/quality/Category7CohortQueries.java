package org.openmrs.module.esaudereports.reporting.library.queries.quality;

/**
 * Classe contém as queries das cohorts de relatórios - Categoria 7: Rastreio de TB
 * 
 * @author Hamilton Mutaquiha
 */
public class Category7CohortQueries {
	
	/**
	 * PACIENTES QUE TIVERAM RASTREIO DE TUBERCULOSE EM CADA CONSULTA CLINICA
	 */
	public static final String PATIENTS_WHO_HAD_TB_TRACKING_IN_EACH_CLINIC_CONSULTATION = "select consulta.patient_id"
	        + " from" + " (Select 	p.patient_id,count(*) consultas"
	        + " from 	patient p inner join encounter e on e.patient_id=p.patient_id"
	        + " where 	p.voided=0 and e.voided=0 and e.encounter_datetime between :startDate and :endDate and"
	        + " 		e.location_id=:location and e.encounter_type in (6,9)" + " group by patient_id) consulta" + " inner join"
	        + " (Select 	p.patient_id,count(*) rastreios" + " from 	patient p"
	        + " 		inner join encounter e on e.patient_id=p.patient_id"
	        + " 		inner join obs o on o.encounter_id=e.encounter_id"
	        + " where 	p.voided=0 and e.voided=0 and e.encounter_datetime between :startDate and :endDate and"
	        + " 		e.location_id=:location and e.encounter_type in (6,9) and o.voided=0 and o.concept_id=6257"
	        + " group by patient_id) rastreio on consulta.patient_id=rastreio.patient_id"
	        + " where rastreio.rastreios>=consulta.consultas";
	
	/**
	 * MQ_GRAVIDAS INSCRITAS NO SERVICO TARV E QUE INICIARAM TARV NO PERIODO DE INCLUSAO (AMOSTRA
	 * GRAVIDA)
	 */
	public static final String PREGNANTS_SUBSCRIBED_ART_SERVICE_WHO_STARTED_ART_IN_INCLUSION_PERIOD = "select gravida.patient_id"
	        + " from"
	        + " ("
	        + " 	Select 	p.patient_id"
	        + " 	from 	patient p"
	        + " 			inner join encounter e on p.patient_id=e.patient_id"
	        + " 			inner join obs o on e.encounter_id=o.encounter_id"
	        + " 	where 	p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982 and value_coded=44 and"
	        + " 			e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate and e.location_id=:location"
	        + " 	union"
	        + "	Select 	p.patient_id"
	        + " 	from 	patient p inner join encounter e on p.patient_id=e.patient_id"
	        + " 			inner join obs o on e.encounter_id=o.encounter_id"
	        + " 	where 	p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 and"
	        + " 			e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate and e.location_id=:location"
	        + " 	union"
	        + " 	Select 	p.patient_id"
	        + " 	from 	patient p inner join encounter e on p.patient_id=e.patient_id"
	        + " 			inner join obs o on e.encounter_id=o.encounter_id"
	        + " 	where 	p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 and"
	        + " 			e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate and e.location_id=:location"
	        + " 	union"
	        + " 	select 	pp.patient_id"
	        + " 	from 	patient_program pp"
	        + " 	where 	pp.program_id=8 and pp.voided=0 and"
	        + " 			pp.date_enrolled between :startDate and :endDate and pp.location_id=:location"
	        + " ) gravida"
	        + " inner join"
	        + " (  select   distinct patient_id"
	        + "    from     encounter"
	        + "    where 	encounter_type in (6,9) and voided=0 and"
	        + " 			encounter_datetime between :startDate and date_add(:endDate, interval 6 MONTH) and"
	        + " 			location_id=:location"
	        + " ) consulta on consulta.patient_id=gravida.patient_id"
	        + " inner join"
	        + " (	select patient_id,data_inicio"
	        + " 	from"
	        + " 	(	Select patient_id,min(data_inicio) data_inicio"
	        + " 		from"
	        + " 				(	Select 	p.patient_id,min(e.encounter_datetime) data_inicio"
	        + " 					from 	patient p"
	        + " 							inner join encounter e on p.patient_id=e.patient_id"
	        + " 							inner join obs o on o.encounter_id=e.encounter_id"
	        + "					where 	e.voided=0 and o.voided=0 and p.voided=0 and"
	        + " 							e.encounter_type in (18,6,9) and o.concept_id=1255 and o.value_coded=1256 and"
	        + " 							e.encounter_datetime<=:endDate and e.location_id=:location"
	        + " 					group by p.patient_id"
	        + " 					union"
	        + "					Select 	p.patient_id,min(value_datetime) data_inicio"
	        + " 					from 	patient p"
	        + " 							inner join encounter e on p.patient_id=e.patient_id"
	        + " 							inner join obs o on e.encounter_id=o.encounter_id"
	        + " 					where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9) and"
	        + " 							o.concept_id=1190 and o.value_datetime is not null and"
	        + " 							o.value_datetime<=:endDate and e.location_id=:location"
	        + " 					group by p.patient_id"
	        + " 					union"
	        + " 					select 	pg.patient_id,date_enrolled data_inicio"
	        + " 					from 	patient p inner join patient_program pg on p.patient_id=pg.patient_id"
	        + " 					where 	pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:endDate and location_id=:location"
	        + " 					union"
	        + " 				  SELECT 	e.patient_id, MIN(e.encounter_datetime) AS data_inicio"
	        + " 				  FROM 		patient p"
	        + " 							inner join encounter e on p.patient_id=e.patient_id"
	        + " 				  WHERE		p.voided=0 and e.encounter_type=18 AND e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location"
	        + " 				  GROUP BY 	p.patient_id"
	        + " 				) inicio"
	        + " 			group by patient_id"
	        + " 	)inicio1"
	        + " 	where data_inicio between :startDate and  :endDate"
	        + " ) inicio_real on inicio_real.patient_id=gravida.patient_id";
}
