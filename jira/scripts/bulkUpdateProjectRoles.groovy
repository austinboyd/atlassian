// update multiple project roles with group 

def accountId = '123456:12345a67-bbb1-12c3-dd45-678ee99f99g0' // could also be turned into array list
def groupName = 'Engineering Managers' // name of group in Jira
def projectKey = ["PER", "PLFR", "ANA", "AOS", "CORE", "SREIR", "TQ", "UDI"] // an array list
def roleName = 'Administrators'

// loop through projectKey array
for (key in projectKey){
    def roles = get("/rest/api/2/project/${key}/role")
        .asObject(Map).body
    //println key // for testing to see if key is accurate
    
    String developersUrl = roles[roleName] // converts map into string 

    assert developersUrl != null
    
    def result = post(developersUrl)
        .header('Content-Type', 'application/json')
        .body([
                // use either account ID or group, account ID's can become an 
                //user: [accountId],
                group: [groupName]
        ])
        .asString()
    
    assert result.status == 200
    result.statusText
}
