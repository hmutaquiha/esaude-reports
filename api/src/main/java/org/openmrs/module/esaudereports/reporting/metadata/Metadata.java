package org.openmrs.module.esaudereports.reporting.metadata;

/**
 * Created by Nicholas Ingosi on 8/1/17.
 */
public class Metadata {

    public static class _Concept{
        public final static String ANTIRETROVIRAL_PLAN = "e1d9ee10-1d5f-11e0-b929-000c29ad1d07";//1255
        public final static String PREVIOUS_ANTIRETROVIRAL_DRUGS_USED_FOR_TREATMENT = "e1d83d4a-1d5f-11e0-b929-000c29ad1d07";//1087
        public final static String HISTORICAL_DRUG_START_DATE = "e1d8f690-1d5f-11e0-b929-000c29ad1d07";//1190
        public final static String PREGNANT = "e1e056a6-1d5f-11e0-b929-000c29ad1d07";//1982
        public final static String GESTATION = "e1cdd58a-1d5f-11e0-b929-000c29ad1d07";//44
        public final static String NUMBER_OF_WEEKS_PREGNANT = "e1da0788-1d5f-11e0-b929-000c29ad1d07"; //1279
    }

    public static class _EncounterType{
        public final static String ADULTO_INICIAL_A = "e278f820-1d5f-11e0-b929-000c29ad1d07";//5
        public final static String  PEDIATRIA_INICIAL_A = "e278fa8c-1d5f-11e0-b929-000c29ad1d07";//7
        public final static String  ADULTO_SEGUIMENTO = "e278f956-1d5f-11e0-b929-000c29ad1d07"; //6
        public final static String  PEDIATRIA_SEGUIMENTO = "e278fce4-1d5f-11e0-b929-000c29ad1d07"; //9
        public final static String   FARMACIA = "e279133c-1d5f-11e0-b929-000c29ad1d07"; //18
    }

    public static class _Program{
        public final static String SERVICO_TARV_CUIDADO = "7b2e4a0a-d4eb-4df7-be30-78ca4b28ca99";
        public final static String PTV_ETV = "06057245-ca21-43ab-a02f-e861d7e54593";
        public final static String SERVICO_TARV_TRATAMENTO = "efe2481f-9e75-4515-8d5a-86bfde2b5ad3";
    }

    public static class _PattientStates{
        public final static String state1 = "";
        public final static String state25 = "";
        public final static String state28 = "";
    }
}
