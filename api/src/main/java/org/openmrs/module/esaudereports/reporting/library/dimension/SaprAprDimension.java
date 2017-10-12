package org.openmrs.module.esaudereports.reporting.library.dimension;

import org.openmrs.Location;
import org.openmrs.module.esaudereports.reporting.library.cohort.SaprAprCohort;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.indicator.dimension.CohortDefinitionDimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static org.openmrs.module.esaudereports.reporting.utils.ReportUtils.map;

/**
 * Created by administrator on 8/23/17.
 */
@Component
public class SaprAprDimension {

    @Autowired
    private SaprAprCohort saprAprCohort;

    /**
     * Dimensions of age for children and adults for quality improvement report
     *
     * @return CohortDefinitionDimension
     */
    public CohortDefinitionDimension dimForSaprApr() {
        CohortDefinitionDimension dim = new CohortDefinitionDimension();
        dim.setName("DIMENSAO APR");
        dim.setDescription("Dimensão para indicadores do APR: Data Inicial: grávidas, Lactante, DAM e DAG");
        dim.addParameter(new Parameter("startDate", "Data Inicial", Date.class));
        dim.addParameter(new Parameter("endDate", "Data Final", Date.class));
        dim.addParameter(new Parameter("location", "Location", Location.class));
        dim.addCohortDefinition("LACTANTE", map(saprAprCohort.breastFeedingOrPuerpueras(),"startDate=${startDate}, endDate=${endDate}, location=${location}"));
        dim.addCohortDefinition("GRAVIDA", map(saprAprCohort.pregnantsInscribedOnARTService(),"startDate=${startDate}, endDate=${endDate}, location=${location}"));
        return dim;
    }

}
