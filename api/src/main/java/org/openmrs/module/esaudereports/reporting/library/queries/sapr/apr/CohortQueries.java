package org.openmrs.module.esaudereports.reporting.library.queries.sapr.apr;

/**
 * Created by Hamilton Mutaquiha on 8/23/17.
 */
public class CohortQueries {

    /**
     * PROGRAMA: PACIENTES TRANSFERIDOS DE NO PROGRAMA DE TRATAMENTO ARV: NUM PERIODO
     */
    public static final String PATIENTS_TRANFERED_ON_ART_TRAEATMENT_PROGRAM_IN_PERIOD = "select 	pg.patient_id"
            + " from 	patient p" + " inner join patient_program pg on p.patient_id=pg.patient_id"
            + " inner join patient_state ps on pg.patient_program_id=ps.patient_program_id"
            + " where 	pg.voided=0 and ps.voided=0 and p.voided=0 and"
            + " pg.program_id=2 and ps.state=29 and ps.start_date=pg.date_enrolled and"
            + " ps.start_date between :startDate and :endDate and location_id=:location";

    /**
     * INICIO DE TRATAMENTO ARV - NUM PERIODO: INCLUI TRANSFERIDOS DE COM DATA DE INICIO CONHECIDA
     * (SQL)
     */
    public static final String ART_TREATMENT_START_IN_PERIOD_INCLUDE_TRANSFERS = "select patient_id"
            + " from"
            + " (	Select patient_id,min(data_inicio) data_inicio"
            + " 		from"
            + " 			(	Select 	p.patient_id,min(e.encounter_datetime) data_inicio"
            + " 				from 	patient p"
            + " 						inner join encounter e on p.patient_id=e.patient_id"
            + " 						inner join obs o on o.encounter_id=e.encounter_id"
            + " 				where 	e.voided=0 and o.voided=0 and p.voided=0 and"
            + " 						e.encounter_type in (18,6,9) and o.concept_id=1255 and o.value_coded=1256 and"
            + " 						e.encounter_datetime<=:endDate and e.location_id=:location"
            + " 				group by p.patient_id"
            + " 				union"
            + " 				Select 	p.patient_id,min(value_datetime) data_inicio"
            + " 				from 	patient p"
            + " 						inner join encounter e on p.patient_id=e.patient_id"
            + " 						inner join obs o on e.encounter_id=o.encounter_id"
            + " 				where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9) and"
            + " 						o.concept_id=1190 and o.value_datetime is not null and"
            + " 						o.value_datetime<=:endDate and e.location_id=:location"
            + " 				group by p.patient_id"
            + " 				union"
            + " 				select 	pg.patient_id,date_enrolled data_inicio"
            + " 				from 	patient p inner join patient_program pg on p.patient_id=pg.patient_id"
            + " 				where 	pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:endDate and location_id=:location"
            + " 				union"
            + " 			  SELECT 	e.patient_id, MIN(e.encounter_datetime) AS data_inicio"
            + " 			  FROM 		patient p"
            + " 						inner join encounter e on p.patient_id=e.patient_id"
            + " 			  WHERE		p.voided=0 and e.encounter_type=18 AND e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location"
            + " 			  GROUP BY 	p.patient_id" + " 			) inicio_real" + " 		group by patient_id" + " 	)inicio"
            + " where data_inicio between :startDate and :endDate";

    /**
     * GRAVIDAS INSCRITAS NO SERVIÇO TARV
     */
    public static final String PREGNANTS_INSCRIBED_ON_ART_SERVICE = "Select 	p.patient_id"
            + " from 	patient p"
            + " 		inner join encounter e on p.patient_id=e.patient_id"
            + " 		inner join obs o on e.encounter_id=o.encounter_id"
            + " where 	p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982 and value_coded=44 and"
            + " 		e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate and e.location_id=:location"
            + " union"
            + " Select 	p.patient_id"
            + " from 	patient p inner join encounter e on p.patient_id=e.patient_id"
            + " 		inner join obs o on e.encounter_id=o.encounter_id"
            + " where 	p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 and"
            + " 		e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate and e.location_id=:location"
            + " union"
            + " Select 	p.patient_id"
            + " from 	patient p inner join encounter e on p.patient_id=e.patient_id"
            + " 		inner join obs o on e.encounter_id=o.encounter_id"
            + " where 	p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 and"
            + " 		e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate and e.location_id=:location"
            + " union" + " select 	pp.patient_id" + " from 	patient_program pp"
            + " where 	pp.program_id=8 and pp.voided=0 and"
            + " 		pp.date_enrolled between :startDate and :endDate and pp.location_id=:location";

    /**
     * PROGRAMA: PACIENTES QUE DERAM PARTO HÁ DOIS ANOS ATRÁS DA DATA DE REFERENCIA - LACTANTES
     */
    public static final String PATIENTS_WHO_GAVE_BIRTH_TWO_YEARS_AGO = "select 	pg.patient_id"
            + " from 	patient p"
            + " 		inner join patient_program pg on p.patient_id=pg.patient_id"
            + " 		inner join patient_state ps on pg.patient_program_id=ps.patient_program_id"
            + " where 	pg.voided=0 and ps.voided=0 and p.voided=0 and"
            + " 		pg.program_id=8 and ps.state=27 and ps.end_date is null and"
            + " 		ps.start_date between date_add(:startDate, interval -2 year) and date_add(:startDate, interval -1 day) and location_id=:location";

    /**
     * PROGRAMA: PACIENTES INSCRITOS NO PROGRAMA TRATAMENTO ARV (TARV) - PERIODO FINAL
     */
    public static final String PATIENTS_INSCRIBED_ON_ART_PROGRAM = "select 	pg.patient_id"
            + " from 	patient p inner join patient_program pg on p.patient_id=pg.patient_id"
            + " where 	pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:endDate and location_id=:location";
}
