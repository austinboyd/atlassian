import com.atlassian.jira.bc.issue.IssueService
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.IssueInputParameters
import com.atlassian.jira.project.Project
import groovy.transform.Field
import com.atlassian.jira.issue.customfields.option.LazyLoadedOption
import com.atlassian.jira.config.SubTaskManager
import com.atlassian.jira.user.ApplicationUser
import org.apache.log4j.Logger
import org.apache.log4j.Level

//enable logging so that you can use something like log.debug("Creating new issue, based on Collateral..")
Logger log = Logger.getLogger("com.example")
log.setLevel(Level.DEBUG)

def customFieldManager = ComponentAccessor.getCustomFieldManager()
@Field
SubTaskManager subTaskManager = ComponentAccessor.getSubTaskManager()
@Field
ApplicationUser user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()

//define the name of the custom field you're wanting to target for creating issues against
def customFieldName = 'Elements'
//get the custom field object
def customFieldObject = customFieldManager.getCustomFieldObjectByName(customFieldName)
// To test in the script console, uncomment the next line and enter an existing ticket number
// def issue = ComponentAccessor.getIssueManager().getIssueObject("MARCOM-3096")
// Comment out the next line when testing in the script console, otherwise it is necessary when placing in a post fuction
Issue issue = issue

//get all the values of the custom field
def customFieldValues = customFieldObject.getValue(issue)

// For each checkbox in the custom field selected, the next two sections will create a ticket for it.
customFieldValues.each { LazyLoadedOption it -> 
    def optionValue = it.getValue() 
    log.debug("Creating new issue, based on ${optionValue}..")
    def newIssue = createIssue(issue, optionValue)
             
}

private Issue createIssue(Issue issue, String optionValue) {
    Project project = issue.getProjectObject()
    IssueService issueService = ComponentAccessor.issueService
    IssueInputParameters issueInputParameters = issueService.newIssueInputParameters()
    def constantsManager = ComponentAccessor.getConstantsManager()

// This is where to set issue parameters like issue type, custom field values, reporter, etc.
    issueInputParameters.setProjectId(project.id)
        .setIssueTypeId("3")
        .setReporterId(user.name)
        .setSummary("Test issue - " + optionValue)

//This part validates the issue creation?
    IssueService.CreateValidationResult createValidationResult = issueService.validateCreate(user, issueInputParameters);
    Issue newIssue
    if (createValidationResult.isValid()) {
        IssueService.IssueResult createResult = issueService.create(user, createValidationResult);
        if (createResult.isValid()) {
            newIssue = createResult.issue
        }

    }
    return newIssue
}
