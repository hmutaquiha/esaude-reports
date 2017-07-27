package org.openmrs.module.esaudereports.reporting.library.dimension;

import org.openmrs.module.esaudereports.reporting.library.cohort.CohortLibrary;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.indicator.dimension.CohortDefinitionDimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static org.openmrs.module.esaudereports.reporting.utils.ReportUtils.map;

/**
 * Created by Nicholas Ingosi on 6/20/17.
 */
@Component
public class CommonDimension {
	
	@Autowired
	private CohortLibrary commonLibrary;
	
	/**
	 * Gender dimension
	 * 
	 * @return the dimension
	 */
	public CohortDefinitionDimension gender() {
		CohortDefinitionDimension dim = new CohortDefinitionDimension();
		dim.setName("gender");
		dim.addCohortDefinition("M", map(commonLibrary.males()));
		dim.addCohortDefinition("F", map(commonLibrary.females()));
		return dim;
	}
	
	/**
	 * Dimension of age using the 3 standard age groups
	 * 
	 * @return the dimension
	 */
	public CohortDefinitionDimension standardAgeGroups() {
		CohortDefinitionDimension dim = new CohortDefinitionDimension();
		dim.setName("age groups (<1, <15, 15+)");
		dim.addParameter(new Parameter("onDate", "Date", Date.class));
		dim.addCohortDefinition("<1", map(commonLibrary.agedAtMost(0), "effectiveDate=${onDate}"));
		dim.addCohortDefinition("<15", map(commonLibrary.agedAtMost(14), "effectiveDate=${onDate}"));
		dim.addCohortDefinition("15+", map(commonLibrary.agedAtLeast(15), "effectiveDate=${onDate}"));
		return dim;
	}
	
	/**
	 * Dimensions of age for children and adults for quality improvement report
	 * 
	 * @return CohortDefinitionDimension
	 */
	public CohortDefinitionDimension dimForQualityImprovement() {
		CohortDefinitionDimension dim = new CohortDefinitionDimension();
		dim.setName("age groups (0-14, 15+)");
		dim.addParameter(new Parameter("onDate", "Date", Date.class));
		dim.addCohortDefinition("0-14", map(commonLibrary.agedAtMost(14), "effectiveDate=${onDate}"));
		dim.addCohortDefinition("15+", map(commonLibrary.agedAtLeast(15), "effectiveDate=${onDate}"));
		return dim;
		
	}
	
}
