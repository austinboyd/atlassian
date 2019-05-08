import com.atlassian.jira.bc.issue.IssueService
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.IssueInputParameters
import com.atlassian.jira.project.Project
import groovy.transform.Field
import com.atlassian.jira.issue.customfields.option.LazyLoadedOption
import com.atlassian.jira.config.SubTaskManager
import com.atlassian.jira.user.ApplicationUser

def customFieldManager = ComponentAccessor.getCustomFieldManager()
@Field
SubTaskManager subTaskManager = ComponentAccessor.getSubTaskManager()
@Field
ApplicationUser user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()

//name of the custom field
def customFieldName = 'Elements'
//get the custom field object
def customFieldObject = customFieldManager.getCustomFieldObjectByName(customFieldName)

//uncomment the next line for testing in the Script Console 
// def issue = ComponentAccessor.getIssueManager().getIssueObject("MARCOM-3097")

// Comment out this line when testing in the script console
Issue issue = issue
//get all the values of the custom field
def customFieldValues = customFieldObject.getValue(issue)


//loop through the values and create sub tasks 
//change the case value as per requirements such as "[build] Collateral" or "[in-market] Email" 

customFieldValues.each { LazyLoadedOption it -> 
    def optionValue = it.getValue() 
    switch (optionValue){ 
        case "[build] Collateral": println("Creating new issue, based on Collateral..") 
            def newIssue = createIssue(issue, optionValue) createTask(issue, newIssue) 
            break 
        case "[build] Event footprint": println("Creating new issue, based on Event Footprint..") 
            def newIssue = createIssue(issue, optionValue) createTask(issue, newIssue) 
            break 
        case "[in-market] Email": println("Creating new issue, based on Email..") 
            def newIssue = createIssue(issue, optionValue) createTask(issue, newIssue) 
            break 
    } 
}

/**
 * @param issue
 * @param newIssue
 * @return
 */
private createTask(Issue issue, Issue newIssue){
    if(newIssue) {
        issueManager.createIssueObject(user, newIssue)
    }
}


/**
 * This function creates a new issue. issueInputParameter should be set according to
 * the requirement
 * @param issue
 * @param optionValue
 * @return
 */
private Issue createIssue(Issue issue, String optionValue) {
    Project project = issue.getProjectObject()
    IssueService issueService = ComponentAccessor.issueService
    IssueInputParameters issueInputParameters = issueService.newIssueInputParameters()
    def constantsManager = ComponentAccessor.getConstantsManager()

    //get issue type such as 'Bug'
    def bugIssueType =  constantsManager.getAllIssueTypeObjects().findByName("Task")

    issueInputParameters.setProjectId(project.id)
        .setIssueTypeId(bugIssueType.id)
        .setReporterId(user.name)
        .setSummary("Test issue - " + optionValue)



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











