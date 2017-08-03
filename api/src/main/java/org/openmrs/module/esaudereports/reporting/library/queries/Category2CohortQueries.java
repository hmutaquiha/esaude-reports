package org.openmrs.module.esaudereports.reporting.library.queries;

/**
 * Created by git on 8/3/17.
 */
public class Category2CohortQueries {

    /**
     * MQ_INSCRITOS NO PERIODO DE INCLUSAO (AMOSTRA PRE-TARV)
     */
    public static final String SUBSCRIBED_INCLUSION_PERIOD_PRE_ART_SAMPLE = " Select 	inscricao.patient_id "
            + " from 	(	Select 	p.patient_id,min(encounter_datetime) as data_inscricao "
            + "			from 	patient p "
            + "					inner join encounter e on p.patient_id=e.patient_id "
            + "			where 	p.voided=0 and e.voided=0 and e.encounter_type in (5,7) and "
            + "					e.location_id=:location and e.encounter_datetime between :startDate and :endDate "
            + "			group 	by p.patient_id "
            + "			union "
            + "			select 	pg.patient_id,min(date_enrolled) data_inscricao "
            + "			from 	patient p "
            + "					inner join patient_program pg on p.patient_id=pg.patient_id "
            + "					inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
            + "			where 	pg.voided=0 and p.voided=0 and program_id=1 and pg.date_enrolled=ps.start_date and ps.voided=0 and "
            + "					date_enrolled between :startDate and :endDate and location_id=:location and ps.state=1 "
            + "			group by pg.patient_id "
            + "		) inscricao inner join "
            + "		(	select 	distinct patient_id "
            + "			from 	encounter "
            + "			where 	encounter_type in (6,9) and voided=0 and "
            + "					encounter_datetime between :startDate and date_add(:startDate, interval 6 MONTH) and "
            + "					location_id=:location "
            + "		) consulta on consulta.patient_id=inscricao.patient_id "
            + " where 	inscricao.patient_id not in "
            + "		(	Select 	p.patient_id "
            + "			from 	patient p "
            + "					inner join encounter e on p.patient_id=e.patient_id "
            + "					inner join obs o on o.encounter_id=e.encounter_id "
            + "			where 	e.voided=0 and o.voided=0 and p.voided=0 and "
            + "					e.encounter_type in (18,6,9) and o.concept_id in (1255,1087) and "
            + "					e.encounter_datetime between :startDate and :revisionEndDate "
            + "			union "
            + "			Select 	p.patient_id "
            + "			from 	patient p "
            + "					inner join encounter e on p.patient_id=e.patient_id "
            + "					inner join obs o on e.encounter_id=o.encounter_id "
            + "			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9) and "
            + "					o.concept_id=1190 and o.value_datetime is not null and o.value_datetime between :startDate and :revisionEndDate "
            + "			union "
            + "			Select 	p.patient_id "
            + "			from 	patient p "
            + "					inner join encounter e on p.patient_id=e.patient_id "
            + "			where 	p.voided=0 and e.voided=0 and e.encounter_type=18 and e.encounter_datetime between :startDate and :revisionEndDate "
            + "			union "
            + "			select 	pg.patient_id "
            + "			from 	patient p "
            + "					inner join patient_program pg on p.patient_id=pg.patient_id "
            + "			where 	pg.voided=0 and p.voided=0 and program_id=2 and  date_enrolled between :startDate and :revisionEndDate "
            + "			union "
            + "			select 	pg.patient_id "
            + "			from 	patient p "
            + "					inner join patient_program pg on p.patient_id=pg.patient_id "
            + "					inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
            + "			where 	pg.voided=0 and ps.voided=0 and p.voided=0 and "
            + "					pg.program_id=1 and ps.state=3 and ps.end_date is null and ps.start_date between :startDate and :revisionEndDate "
            + "			union " + "			select 	pg.patient_id " + "			from 	patient p "
            + "					inner join patient_program pg on p.patient_id=pg.patient_id "
            + "					inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
            + "			where 	pg.voided=0 and ps.voided=0 and p.voided=0 and " + "					pg.program_id=1 and ps.state=28 and "
            + "					ps.start_date between :startDate and :revisionEndDate " + "			union " + "			Select 	p.patient_id "
            + "			from 	patient p " + "					inner join encounter e on p.patient_id=e.patient_id "
            + "					inner join obs o on e.encounter_id=o.encounter_id "
            + "			where 	p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982 and value_coded=44 and "
            + "					e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :revisionEndDate "
            + "			union " + "			Select 	p.patient_id "
            + " 			from 	patient p inner join encounter e on p.patient_id=e.patient_id "
            + "					inner join obs o on e.encounter_id=o.encounter_id "
            + "			where 	p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 and "
            + "					e.encounter_type in (5,6) and o.value_datetime between :startDate and :revisionEndDate " + "			union "
            + "			select 	pp.patient_id " + "			from 	patient_program pp "
            + " 			where 	pp.program_id=8 and pp.voided=0 and "
            + "					pp.date_enrolled between :startDate and :revisionEndDate " + "			union " + "			select 	pp.patient_id "
            + "			from 	patient_program pp "
            + "					inner join patient_state ps on pp.patient_program_id=ps.patient_program_id "
            + "			where 	pp.program_id=8 and pp.voided=0 and ps.voided=0 and ps.state=25 and ps.end_date is null and "
            + "					ps.start_date between :startDate and :revisionEndDate " + "			union " + "			select 	pp.patient_id "
            + "			from 	patient_program pp " + "			where 	pp.program_id=8 and pp.voided=0 and "
            + "					pp.date_enrolled<:startDate and date_completed is null " + "		) ";

    /**
     * MQ_PACIENTES COM CONSULTA CLINICA NA MESMA DATA DA INSCRIÇÃO
     */
    public static final String PATIENTS_WITH_CLINIC_CONSULTATION_ON_SUBSCRIPTION_DATE = " Select 	inscricao.patient_id "
            + " from 	encounter e "
            + "		inner join "
            + "		(	Select 	p.patient_id,min(encounter_datetime) as data_inscricao "
            + "			from 	patient p "
            + "					inner join encounter e on p.patient_id=e.patient_id "
            + "			where 	p.voided=0 and e.voided=0 and e.encounter_type in (5,7) and "
            + "					e.location_id=:location and e.encounter_datetime between :startDate and :endDate "
            + "			group 	by p.patient_id "
            + "			union "
            + "			select 	pg.patient_id,min(date_enrolled) data_inscricao "
            + "			from 	patient p "
            + "					inner join patient_program pg on p.patient_id=pg.patient_id "
            + "					inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
            + "			where 	pg.voided=0 and p.voided=0 and program_id=1 and pg.date_enrolled=ps.start_date and ps.voided=0 and "
            + "					date_enrolled between :startDate and :endDate and location_id=:location and ps.state=1 "
            + "			group by pg.patient_id "
            + "		) inscricao on inscricao.patient_id=e.patient_id "
            + " where e.encounter_type in (6,9) and e.voided=0 and e.encounter_datetime=inscricao.data_inscricao and e.location_id=:location ";
}
