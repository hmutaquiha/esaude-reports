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

import org.openmrs.Location;
import org.openmrs.module.esaudereports.reporting.library.queries.Category2CohortQueries;
import org.openmrs.module.esaudereports.reporting.library.queries.CohortQueries;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.evaluation.parameter.Parameterizable;
import org.springframework.stereotype.Component;

import java.util.Date;

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
        cd.addSearch("INSCRITOS", Mapped.map(patientsSubscribedInclusionPeriodPreARTSample(), "startDate=${startDate},endDate=${endDate},location=${location},revisionEndDate=${revisionEndDate}"));
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

    public CohortDefinition followUpClinicalConsulationsNumerator() {
        SqlCohortDefinition cd = new SqlCohortDefinition();

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
        cd.setQuery(CohortQueries.ART_START_INCLUSION_PERIOD_ART_SAMPLE);
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
}
