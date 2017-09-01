package org.openmrs.module.esaudereports.reporting.library.queries.quality;

/**
 * Classe contém as queries das cohorts de relatórios - Categoria 3: Consultas Clínicas de
 * seguimento
 * 
 * @author Hamilton Mutaquiha
 */
public class Category3CohortQueries {
	
	/**
	 * MQ_INICIO TARV NO PERIODO DE INCLUSAO (AMOSTRA TARV)
	 */
	public static final String ART_START_INCLUSION_PERIOD_ART_SAMPLE = "select inicio.patient_id " + " from "
	        + " (Select   p.patient_id " + " from  patient p " + " inner join encounter e on p.patient_id=e.patient_id "
	        + " inner join obs o on o.encounter_id=e.encounter_id " + " where e.voided=0 and o.voided=0 and p.voided=0 and "
	        + " e.encounter_type in (18,6,9) and o.concept_id=1255 and o.value_coded=1256 and "
	        + " e.encounter_datetime between :startDate and :endDate and e.location_id=:location " + "union"
	        + " select   p.patient_id " + " from  patient p " + " inner join encounter e on p.patient_id=e.patient_id "
	        + " inner join obs o on e.encounter_id=o.encounter_id "
	        + " where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9) and "
	        + " o.concept_id=1190 and o.value_datetime is not null and "
	        + " o.value_datetime between :startDate and :endDate and e.location_id=:location " + "union"
	        + " select pg.patient_id " + " from  patient p  "
	        + " inner join patient_program pg on p.patient_id=pg.patient_id "
	        + " inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
	        + " where pg.voided=0 and p.voided=0 and program_id=2 and pg.date_enrolled=ps.start_date and ps.voided=0 and "
	        + " date_enrolled between :startDate and :endDate and " + " location_id=:location and ps.state=6 "
	        + " ) inicio " + " inner join " + " (select distinct patient_id " + " from  encounter "
	        + " where encounter_type in (6,9) and voided=0 and "
	        + " encounter_datetime between :startDate and date_add(:startDate, interval 6 MONTH) and "
	        + " location_id=:location " + " ) consulta on consulta.patient_id=inicio.patient_id "
	        + "  where inicio.patient_id " + " not in " + " ( " + " select pg.patient_id " + " from  patient p "
	        + " inner join patient_program pg on p.patient_id=pg.patient_id "
	        + " inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
	        + " where pg.voided=0 and ps.voided=0 and p.voided=0 and "
	        + " pg.program_id=2 and ps.state=7 and ps.end_date is null and " + " ps.start_date <=:revisionEndDate "
	        + "union" + " select pg.patient_id " + " from  patient p "
	        + " inner join patient_program pg on p.patient_id=pg.patient_id "
	        + " inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
	        + " where pg.voided=0 and ps.voided=0 and p.voided=0 and "
	        + " pg.program_id=2 and ps.state=29 and ps.start_date<=:revisionEndDate " + "union" + " select   p.patient_id "
	        + " from  patient p " + " inner join encounter e on p.patient_id=e.patient_id "
	        + " inner join obs o on e.encounter_id=o.encounter_id "
	        + " where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982 and value_coded=44 and "
	        + " e.encounter_type in (5,6) and e.encounter_datetime <=:revisionEndDate " + "union"
	        + " select   p.patient_id " + " from  patient p inner join encounter e on p.patient_id=e.patient_id "
	        + " inner join obs o on e.encounter_id=o.encounter_id "
	        + " where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 and "
	        + " e.encounter_type in (5,6) and o.value_datetime<=:revisionEndDate " + "union" + " select pp.patient_id "
	        + " from  patient_program pp "
	        + " where pp.program_id=8 and pp.voided=0 and pp.date_enrolled<=:revisionEndDate " + "union"
	        + " select pp.patient_id " + " from  patient_program pp "
	        + " inner join patient_state ps on pp.patient_program_id=ps.patient_program_id "
	        + " where pp.program_id=8 and pp.voided=0 and ps.voided=0 and ps.state=25 and ps.end_date is null and "
	        + " ps.start_date<=:revisionEndDate " + "union" + " Select   p.patient_id " + " from  patient p "
	        + " inner join encounter e on p.patient_id=e.patient_id "
	        + " inner join obs o on o.encounter_id=e.encounter_id " + " where e.voided=0 and o.voided=0 and p.voided=0 and "
	        + " e.encounter_type in (18,6,9) and o.concept_id=1255 and o.value_coded=1256 and "
	        + " e.encounter_datetime<:startDate " + "union" + " Select   p.patient_id " + " from  patient p "
	        + " inner join encounter e on p.patient_id=e.patient_id "
	        + " inner join obs o on e.encounter_id=o.encounter_id "
	        + " where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9) and "
	        + " o.concept_id=1190 and o.value_datetime is not null and " + " o.value_datetime < :startDate " + "union"
	        + " select pg.patient_id " + " from  patient p "
	        + " inner join patient_program pg on p.patient_id=pg.patient_id "
	        + " inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
	        + " where pg.voided=0 and p.voided=0 and program_id=2 and pg.date_enrolled=ps.start_date and ps.voided=0 and "
	        + " date_enrolled<:startDate and ps.state=6 " + " )";
	
	/**
	 * MQ_PACIENTES QUE TIVERAM 3 CONSULTAS DENTRO DE 6 MESES APOS INSCRICAO
	 */
	public static final String PATIENTS_WITH_3_CONSULTATIONS_WITHIN_6_MONTHS_AFTER_ENROLLMENT = "select inscricao.patient_id "
	        + " from "
	        + " 	(	select patient_id,min(data_abertura) data_abertura "
	        + " 		from ( "
	        + " 				Select 	e.patient_id,min(encounter_datetime) data_abertura "
	        + " 				from 	patient p "
	        + " 						inner join encounter e on e.patient_id=p.patient_id "
	        + " 				where 	p.voided=0 and e.encounter_type in (5,7) and e.voided=0 and "
	        + " 						e.location_id=:location and e.encounter_datetime between :startDate and :endDate "
	        + " 				group by p.patient_id "
	        + " 				union "
	        + " 				select 	pg.patient_id,min(date_enrolled) data_inscricao "
	        + " 				from 	patient p "
	        + " 						inner join patient_program pg on p.patient_id=pg.patient_id "
	        + " 						inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
	        + " 				where 	pg.voided=0 and p.voided=0 and program_id=1 and pg.date_enrolled=ps.start_date and ps.voided=0 and "
	        + " 						date_enrolled between :startDate and :endDate and location_id=:location and ps.state=1 "
	        + " 				group by pg.patient_id "
	        + " 			)abertura1 "
	        + " 		group by patient_id "
	        + " 	) inscricao "
	        + " 	inner join encounter e on e.patient_id=inscricao.patient_id "
	        + " where 	e.voided=0 and e.encounter_type in (6,9) and e.location_id=:location and "
	        + " 		e.encounter_datetime between data_abertura and date_add(data_abertura, interval 6 month) "
	        + " group by inscricao.patient_id " + " having count(*)>=3";
	
	/**
	 * MQ_PACIENTES QUE INICIARAM TARV E QUE TIVERAM NO MINIMO 3 CONSULTAS ANTES DOS 6 MESES DE TARV
	 */
	public static final String PATIENTS_WHO_STARTED_ART_WITH_AT_LEAST_3_CONSULTATIONS_BEFORE_6_MONTHS_OF_ART = "select inicio_real.patient_id"
	        + " from"
	        + " (	select patient_id,min(data_inicio) data_inicio,date_add(min(data_inicio),interval 6 Month) data_6meses"
	        + " 	from"
	        + " 	(	Select 	p.patient_id,min(e.encounter_datetime) data_inicio"
	        + " 		from 	patient p"
	        + " 				inner join encounter e on p.patient_id=e.patient_id"
	        + " 				inner join obs o on o.encounter_id=e.encounter_id"
	        + " 		where 	e.voided=0 and o.voided=0 and p.voided=0 and"
	        + " 				e.encounter_type in (18,6,9) and o.concept_id=1255 and o.value_coded=1256 and"
	        + " 				e.encounter_datetime between :startDate and :endDate and e.location_id=:location"
	        + " 		group by p.patient_id"
	        + " 		union"
	        + " 		Select 	p.patient_id,min(value_datetime) data_inicio"
	        + " 		from 	patient p"
	        + " 				inner join encounter e on p.patient_id=e.patient_id"
	        + " 				inner join obs o on e.encounter_id=o.encounter_id"
	        + " 		where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9) and"
	        + " 				o.concept_id=1190 and o.value_datetime is not null and"
	        + " 				o.value_datetime between :startDate and :endDate and e.location_id=:location"
	        + " 		group by p.patient_id"
	        + " 		union"
	        + " 		select 	pg.patient_id,date_enrolled data_inicio"
	        + " 		from 	patient p"
	        + " 				inner join patient_program pg on p.patient_id=pg.patient_id"
	        + " 				inner join patient_state ps on pg.patient_program_id=ps.patient_program_id"
	        + " 		where 	pg.voided=0 and p.voided=0 and program_id=2 and pg.date_enrolled=ps.start_date and"
	        + " 				date_enrolled between :startDate and :endDate and pg.location_id=:location and ps.state=6 and ps.voided=0"
	        + " 	) inicio"
	        + " 	group by patient_id"
	        + " ) inicio_real"
	        + " inner join"
	        + " encounter e on e.patient_id=inicio_real.patient_id"
	        + " where e.voided=0 and e.encounter_type in (6,9) and e.encounter_datetime between data_inicio and data_6meses and"
	        + " e.location_id=:location" + " group by inicio_real.patient_id" + " having count(*)>=3";
	
	/**
	 * MQ_PACIENTES COM CONSULTAS MENSAIS
	 */
	public static final String PATIENTS_WITH_MONTHLY_CONSULTATIONS = "select p.patient_id" + " from 	patient p"
	        + " 		inner join encounter e on p.patient_id=e.patient_id"
	        + " where 	p.voided=0 and e.voided=0 and e.encounter_datetime between :endDate and :revisionEndDate and"
	        + " 		e.location_id=:location and e.encounter_type in (6,9)" + " group by p.patient_id"
	        + " having count(*)>=round(datediff(:revisionEndDate,:endDate)/30)-1";
}
