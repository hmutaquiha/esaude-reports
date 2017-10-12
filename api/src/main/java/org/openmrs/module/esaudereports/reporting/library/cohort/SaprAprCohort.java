package org.openmrs.module.esaudereports.reporting.library.cohort;

import org.openmrs.Concept;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.api.PatientSetService;
import org.openmrs.api.context.Context;
import org.openmrs.module.esaudereports.reporting.library.queries.sapr.apr.CohortQueries;
import org.openmrs.module.esaudereports.reporting.metadata.Dictionary;
import org.openmrs.module.esaudereports.reporting.metadata.Metadata;
import org.openmrs.module.esaudereports.reporting.utils.CoreUtils;
import org.openmrs.module.esaudereports.reporting.utils.ReportUtils;
import org.openmrs.module.reporting.cohort.definition.*;
import org.openmrs.module.reporting.common.RangeComparator;
import org.openmrs.module.reporting.common.SetComparator;
import org.openmrs.module.reporting.common.TimeQualifier;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by Hamilton Mutaquiha on 8/23/17.
 */
@Component
public class SaprAprCohort {

    @Autowired
    private CommonCohortLibrary cohortLibrary;

    public CohortDefinition aRTStartIncludingTransfers() {
        SqlCohortDefinition cd = new SqlCohortDefinition();
        cd.setName("INICIO DE TRATAMENTO ARV - NUM PERIODO: INCLUI TRANSFERIDOS DE COM DATA DE INICIO CONHECIDA (SQL)");
        cd.setDescription("Paciente que iniciou o tratamento ARV (Na unidade sanitaria seleccionada). São inclusos os transferidos de com data de inicio conhecida");
        cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
        cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
        cd.addParameter(new Parameter("location", "Location", Location.class));
        cd.setQuery(CohortQueries.ART_TREATMENT_START_IN_PERIOD_INCLUDE_TRANSFERS);

        return cd;
    }

    public CohortDefinition patientsTransferedOnARTProgram() {
        SqlCohortDefinition cd = new SqlCohortDefinition();
        cd.setName("PROGRAMA: PACIENTES TRANSFERIDOS DE NO PROGRAMA DE TRATAMENTO ARV: NUM PERIODO");
        cd.setDescription("São pacientes que entraram no programa de tratamento ARV num periodo vindos transferidos de ");
        cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
        cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
        cd.addParameter(new Parameter("location", "Location", Location.class));
        cd.setQuery(CohortQueries.PATIENTS_TRANFERED_ON_ART_TRAEATMENT_PROGRAM_IN_PERIOD);

        return cd;
    }

    public CohortDefinition aRTStartInPeriodExcludingTransfersFrom() {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("INICIO DE TRATAMENTO ARV - NUM PERIODO: EXCLUI TRANSFERIDOS DE COM DATA DE INICIO CONHECIDA (SQL)");
        cd.setDescription("Sao pacientes que iniciaram tratamento ARV num periodo excluindo os transferidos de com a data de inicio conhecida e mesmo que coincida no periodo");
        cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
        cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
        cd.addParameter(new Parameter("location", "Location", Location.class));
        cd.addSearch("INICIO", ReportUtils.map(aRTStartIncludingTransfers(), "startDate=${startDate},endDate=${endDate},location=${location}"));
        cd.addSearch("TRANSFDEPRG", ReportUtils.map(patientsTransferedOnARTProgram(),"startDate=${startDate},endDate=${endDate},location=${location}"));
        cd.setCompositionString("INICIO NOT TRANSFDEPRG");
        return cd;
    }

    public CohortDefinition patientsWithDateOfBirthUpdatedOnARTService() {
        DateObsCohortDefinition cd = new DateObsCohortDefinition();

        EncounterType ADULTO_SEGUIMENTO = CoreUtils.getEncounterType(Metadata._EncounterType.ADULTO_SEGUIMENTO_6);

        EncounterType ADULTO_INICIAL_A = CoreUtils.getEncounterType(Metadata._EncounterType.ADULTO_INICIAL_A_5);

        cd.setName("PACIENTES COM DATA DE PARTO ACTUALIZADO NO SERVICO TARV");
        cd.setDescription("Sao pacientes com data de parto actualizado no servico tarv. Repare que os parametros 'Data Inicial' e 'Data Final' refere-se a data de parto e nao data de registo (actualizacao)");
        cd.setQuestion(Dictionary.getConcept(Dictionary.DATE_OF_BIRTH));
        cd.setEncounterTypeList(Arrays.asList(ADULTO_SEGUIMENTO, ADULTO_INICIAL_A));
        cd.setTimeModifier(PatientSetService.TimeModifier.ANY);
        cd.addParameter(new Parameter("location", "Location", Location.class));
        cd.setOperator1(RangeComparator.GREATER_EQUAL);
        cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
        cd.setOperator2(RangeComparator.LESS_EQUAL);
        cd.addParameter(new Parameter("endDate", "Data Final", Date.class));

        return cd;
    }

    public CohortDefinition aRTStartForBeingBreastfeeding() {
        CodedObsCohortDefinition cd = new CodedObsCohortDefinition();

        cd.setName("INICIO DE TARV POR SER LACTANTE");
        cd.setDescription("São pacientes que iniciaram TARV por serem lactantes. Conceito 6334");
        cd.setQuestion(Dictionary.getConcept(Dictionary.CRITERIA_FOR_ART_START));
        cd.setEncounterTypeList(Arrays.asList(CoreUtils.getEncounterType(Metadata._EncounterType.ADULTO_SEGUIMENTO_6)));
        cd.setTimeModifier(PatientSetService.TimeModifier.LAST);
        cd.setOperator(SetComparator.IN);
        cd.setValueList(Arrays.asList(Dictionary.getConcept(Dictionary.BREASTFEEDING)));
        cd.addParameter(new Parameter("location", "Unidade Sanitaria", Location.class));
        cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
        cd.addParameter(new Parameter("endDate", "Data Final", Date.class));

        return cd;
    }

    public CohortDefinition pregnantsInscribedOnARTService() {
        SqlCohortDefinition cd = new SqlCohortDefinition();
        cd.setName("GRAVIDAS INSCRITAS NO SERVIÇO TARV");
        cd.setDescription("São pacientes que estão gravidas durante a abertura do processo ou durante o seguimento no serviço TARV e que foi notificado como nova gravidez durante o seguimemento.");
        cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
        cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
        cd.addParameter(new Parameter("location", "Location", Location.class));
        cd.setQuery(CohortQueries.PREGNANTS_INSCRIBED_ON_ART_SERVICE);

        return cd;
    }

    public CohortDefinition patientsWhoGaveBirthTwoYearsAgo() {
        SqlCohortDefinition cd = new SqlCohortDefinition();
        cd.setName("PROGRAMA: PACIENTES QUE DERAM PARTO HÁ DOIS ANOS ATRÁS DA DATA DE REFERENCIA - LACTANTES");
        cd.setDescription("São pacientes inscritos no programa de PTV e que foram actualizados como parto num periodo de 2 anos atrás da data de referencia");
        cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
        cd.addParameter(new Parameter("location", "Location", Location.class));
        cd.setQuery(CohortQueries.PATIENTS_WHO_GAVE_BIRTH_TWO_YEARS_AGO);

        return cd;
    }

    public CohortDefinition registeredBreastFeeding() {
        CodedObsCohortDefinition cd = new CodedObsCohortDefinition();

        cd.setName("LACTANTES REGISTADAS");
        cd.setDescription("São pacientes que foram actualizados como lactantes na ficha de seguimento");
        cd.setQuestion(Dictionary.getConcept(Dictionary.BREASTFEEDING));
        cd.setEncounterTypeList(Arrays.asList(CoreUtils.getEncounterType(Metadata._EncounterType.ADULTO_SEGUIMENTO_6)));
        cd.setTimeModifier(PatientSetService.TimeModifier.LAST);
        cd.setOperator(SetComparator.IN);
        cd.setValueList(Arrays.asList(Dictionary.getConcept(Dictionary.YES)));
        cd.addParameter(new Parameter("location", "Unidade Sanitaria", Location.class));
        cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
        cd.addParameter(new Parameter("endDate", "Data Final", Date.class));

        return cd;
    }

    public CohortDefinition breastFeedingOrPuerpueras() {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("LACTANTES OU PUERPUERAS (POS-PARTO) REGISTADAS: PROCESSO CLINICO E FICHA DE SEGUIMENTO");
        cd.setDescription("São pacientes puerpueras ou lactantes registadas. O registo pode ser na ficha de seguimento ou no processo clinico durante a abertura de processo ");
        cd.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
        cd.addParameter(new Parameter("endDate", "Data Final", Date.class));
        cd.addParameter(new Parameter("location", "Location", Location.class));
        cd.addSearch("DATAPARTO", ReportUtils.map(patientsWithDateOfBirthUpdatedOnARTService(),"startDate=${startDate},endDate=${endDate},location=${location}"));
        cd.addSearch("INICIOLACTANTE", ReportUtils.map(aRTStartForBeingBreastfeeding(),"startDate=${startDate},endDate=${endDate},location=${location}"));
        cd.addSearch("GRAVIDAS", ReportUtils.map(pregnantsInscribedOnARTService(), "startDate=${startDate},endDate=${endDate},location=${location}"));
        cd.addSearch("LACTANTEPROGRAMA", ReportUtils.map(patientsWhoGaveBirthTwoYearsAgo(), "startDate=${startDate},location=${location}"));
        cd.addSearch("FEMININO", ReportUtils.map(cohortLibrary.females(), ""));
        cd.addSearch("LACTANTE", ReportUtils.map(registeredBreastFeeding(), "startDate=${startDate},endDate=${endDate},location=${location}"));

        cd.setCompositionString("((DATAPARTO OR INICIOLACTANTE OR LACTANTEPROGRAMA  OR LACTANTE) AND NOT GRAVIDAS) AND FEMININO");
        return cd;
    }

    public CohortDefinition haveBeenInART() {
        CodedObsCohortDefinition cd = new CodedObsCohortDefinition();

        EncounterType ADULTO_SEGUIMENTO = CoreUtils.getEncounterType(Metadata._EncounterType.ADULTO_SEGUIMENTO_6);

        EncounterType PEDIATRIA_SEGUIMENTO = CoreUtils.getEncounterType(Metadata._EncounterType.PEDIATRIA_SEGUIMENTO_9);

        EncounterType FARMACIA = CoreUtils.getEncounterType(Metadata._EncounterType.FARMACIA_18);

        Concept START = Dictionary.getConcept(Dictionary.START);

        Concept TRANSFERED_FROM = Dictionary.getConcept(Dictionary.TRANSFERED_FROM);

        cd.setName("ALGUMA VEZ ESTEVE EM TRATAMENTO ARV - PERIODO FINAL");
        cd.setDescription("Pacientes que alguma vez esteve em tratamento ARV (Iniciou ou veio transferido de outra us em TARV) até um determinado periodo final");
        cd.setQuestion(Context.getConceptService().getConceptByUuid(Metadata._Concept.ART_MANAGEMENT));
        cd.setEncounterTypeList(Arrays.asList(ADULTO_SEGUIMENTO, PEDIATRIA_SEGUIMENTO, FARMACIA));
        cd.setTimeModifier(PatientSetService.TimeModifier.ANY);
        cd.setOperator(SetComparator.IN);
        cd.setValueList(Arrays.asList(START, TRANSFERED_FROM));
        cd.addParameter(new Parameter("location", "Unidade Sanitaria", Location.class));
        cd.addParameter(new Parameter("endDate", "ATE", Date.class));

        return cd;
    }

    public CohortDefinition patientsInscribedOnARTProgram() {
        SqlCohortDefinition cd = new SqlCohortDefinition();
        cd.setName("PROGRAMA: PACIENTES INSCRITOS NO PROGRAMA TRATAMENTO ARV (TARV) - PERIODO FINAL");
        cd.setDescription("Sao pacientes inscritos no programa de tratamento ARV até um determinado periodo final");
        cd.addParameter(new Parameter("endDate", "Data Inicial", Date.class));
        cd.addParameter(new Parameter("location", "US", Location.class));
        cd.setQuery(CohortQueries.PATIENTS_INSCRIBED_ON_ART_PROGRAM);

        return cd;
    }

    public CohortDefinition aRTStartUsingDateConcept() {
        DateObsCohortDefinition cd = new DateObsCohortDefinition();

        EncounterType ADULTO_SEGUIMENTO = CoreUtils.getEncounterType(Metadata._EncounterType.ADULTO_SEGUIMENTO_6);

        EncounterType PEDIATRIA_SEGUIMENTO = CoreUtils.getEncounterType(Metadata._EncounterType.PEDIATRIA_SEGUIMENTO_9);

        EncounterType FARMACIA = CoreUtils.getEncounterType(Metadata._EncounterType.FARMACIA_18);

        cd.setName("INICIO DE TARV USANDO O CONCEITO DE DATA - PERIODO FINAL");
        cd.setDescription("São pacientes que iniciaram TARV registado no conceito 'Data de Inicio de TARV' na ficha de seguimento");
        cd.setQuestion(Dictionary.getConcept(Dictionary.ART_START_DATE));
        cd.setEncounterTypeList(Arrays.asList(ADULTO_SEGUIMENTO, PEDIATRIA_SEGUIMENTO, FARMACIA));
        cd.setTimeModifier(PatientSetService.TimeModifier.ANY);
        cd.addParameter(new Parameter("location", "Unidade Sanitaria", Location.class));
        cd.setOperator1(RangeComparator.LESS_EQUAL);
        cd.addParameter(new Parameter("endDate", "Data Final", Date.class));

        return cd;
    }

    public CohortDefinition haveBeenInART_Pharmacy() {
        EncounterCohortDefinition cd = new EncounterCohortDefinition();
        cd.setName("ALGUMA VEZ ESTEVE EM TRATAMENTO ARV - PERIODO FINAL - FARMACIA");
        cd.setDescription("Pacientes que alguma vez esteve em tratamento ARV, levantou pelo menos uma vez ARV na Famacia (tem pelo menos um FRIDA/FILA preenchido) até um determinado periodo final");
        cd.setEncounterTypeList(Arrays.asList(CoreUtils.getEncounterType(Metadata._EncounterType.FARMACIA_18)));
        cd.setTimeQualifier(TimeQualifier.ANY);
        cd.addParameter(new Parameter("location", "Unidade Sanitaria", Location.class));
        cd.addParameter(new Parameter("endDate", "Data Final", Date.class));

        return cd;
    }
}
