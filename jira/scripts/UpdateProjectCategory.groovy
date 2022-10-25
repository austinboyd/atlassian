def projectKey = ["ASKIT", "ITP"]//  ["AI", "ANP", "AUDI", "CAMP", "CPT", "APP", "PRO", "FE", "CO", "DOT", "DSML", "DLVR", "DEVX", "FRAM", "EX", "PLAT", "IN", "WF", "MLP", "MOB", "PER", "PLFR", "ANA", "AOS", "CORE", "SREIR", "TQ", "UDI"]] // an array list
def projectCategory =  '10001' // eng: '10002' 

// loop through projectKey array
for (key in projectKey){
    def category = put("/rest/api/3/project/${key}")
        .header('Content-Type', 'application/json')
        .body([
            "categoryId": "10001",
        ])
        .asJson()
}
