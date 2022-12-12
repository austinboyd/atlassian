// update multiple project roles with group 


def accountId = '123456:12345a67-bbb1-12c3-dd45-678ee99f99g0' // the ID for the user adding to roles
def groupName = 'Engineering Managers' // name of group for adding to roles
def projectKey = ["AI", "ANP", "AUDI", "CAMP", "CPT", "APP"] // The projects that each groupName or accountId will be added to // as an array list
def roleName = 'Administrators' // name of Role the accountId or groupName should be assigned 

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
                // use either accountId or groupName, I don't think this script can do both simultaneously
                //user: [accountId],
                group: [groupName]
        ])
        .asString()
    
    assert result.status == 200
    result.statusText
}
