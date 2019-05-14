// This script is set in a Script Custom field and calculates how many days a ticket took to be resolved. 
// It compared Created Date and Resolution Date to find this number]
// The template must be set to Number field and the Searcher should be set to Number Searcher 

def createDate = issue.getCreated();
def resolutionDate = issue.getResolutionDate();
if (resolutionDate == null) {
   return null;
}
return resolutionDate - createDate;