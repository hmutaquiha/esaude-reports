package org.openmrs.module.esaudereports;

/**
 * Created by codehub on 7/19/17.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.reporting.ReportingConstants;
import org.openmrs.module.reporting.report.manager.ReportManagerUtil;
import org.openmrs.module.reporting.report.util.ReportUtil;

/**
 * Initializes reports
 */
public class EsaudeReportInitializer implements Initializer {
	
	protected static final Log log = LogFactory.getLog(EsaudeReportInitializer.class);
	
	/**
	 * @see Initializer#started()
	 */
	@Override
	public synchronized void started() {
		removeOldReports();
		
		ReportManagerUtil.setupAllReports(EsaudeReportManager.class);
		ReportUtil.updateGlobalProperty(ReportingConstants.GLOBAL_PROPERTY_DATA_EVALUATION_BATCH_SIZE, "-1");
	}
	
	/**
	 * @see Initializer#stopped()
	 */
	@Override
	public void stopped() {
	}
	
	private void removeOldReports() {
		AdministrationService as = Context.getAdministrationService();
		//the purpose of this snipet is to allow rapid development other than going to change the report version all the time for change
		log.warn("Removing all reports");
		//getting id of the loaded report designs
		String report_resource_quality_improvement_id = "select id from reporting_report_design where uuid='c200541e-72ce-11e7-b45c-507b9dc4c741'";
		//deleting the resource already loaded
		as.executeSQL("delete from reporting_report_design_resource where report_design_id =("
		        + report_resource_quality_improvement_id + ");", false);
		//deleting the actual designs now
		as.executeSQL("delete from reporting_report_design where uuid='c200541e-72ce-11e7-b45c-507b9dc4c741';", false);
		
		//deleting all report requests and managers
		as.executeSQL("delete from reporting_report_request;", false);
		as.executeSQL("delete from global_property WHERE property LIKE 'reporting.reportManager%';", false);
		
		//deleting the actual report definitions from the db
		as.executeSQL("delete from serialized_object WHERE uuid = 'd1275b54-72ce-11e7-8b29-507b9dc4c741';", false);
	}
}
