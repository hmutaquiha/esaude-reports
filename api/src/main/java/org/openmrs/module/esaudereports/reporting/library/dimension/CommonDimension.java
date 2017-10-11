package org.openmrs.module.esaudereports.reporting.library.dimension;

import org.openmrs.module.esaudereports.reporting.library.cohort.CommonCohortLibrary;
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
	private CommonCohortLibrary commonLibrary;
	
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
	
	/**
	 * Dimension of age for patients on ARV of different age group
	 * 
	 * @return CohortDefinitionDimension
	 */
	public CohortDefinitionDimension arvAgeBands() {
		CohortDefinitionDimension dim = new CohortDefinitionDimension();
		dim.setName("age group (<1, 1-4, 5-9, 5-14, 10-14, 15-19, 20-24, 25-49, 0-14, 15+, 20+, 50+ )");
		dim.addParameter(new Parameter("onDate", "Date", Date.class));
		dim.addCohortDefinition("<1", map(commonLibrary.agedAtMost(1), "effectiveDate=${onDate}"));
		dim.addCohortDefinition("0-14", map(commonLibrary.agedAtLeastAgedAtMost(0, 14), "effectiveDate=${onDate}"));
		dim.addCohortDefinition("1-4", map(commonLibrary.agedAtLeastAgedAtMost(1, 4), "effectiveDate=${onDate}"));
		dim.addCohortDefinition("5-14", map(commonLibrary.agedAtLeastAgedAtMost(5, 14), "effectiveDate=${onDate}"));
		dim.addCohortDefinition("15+", map(commonLibrary.agedAtLeast(15), "effectiveDate=${onDate}"));
		dim.addCohortDefinition("5-9", map(commonLibrary.agedAtLeastAgedAtMost(5, 9), "effectiveDate=${onDate}"));
		dim.addCohortDefinition("10-14", map(commonLibrary.agedAtLeastAgedAtMost(10, 14), "effectiveDate=${onDate}"));
		dim.addCohortDefinition("15-19", map(commonLibrary.agedAtLeastAgedAtMost(15, 19), "effectiveDate=${onDate}"));
		dim.addCohortDefinition("20-24", map(commonLibrary.agedAtLeastAgedAtMost(20, 24), "effectiveDate=${onDate}"));
		dim.addCohortDefinition("25-49", map(commonLibrary.agedAtLeastAgedAtMost(25, 49), "effectiveDate=${onDate}"));
		dim.addCohortDefinition("50+", map(commonLibrary.agedAtLeast(50), "effectiveDate=${onDate}"));
		dim.addCohortDefinition("20+", map(commonLibrary.agedAtLeast(20), "effectiveDate=${onDate}"));
		return dim;
	}
	
}
