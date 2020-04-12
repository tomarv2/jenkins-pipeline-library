#/bin/sh
BITBUCKET_URL="http://tomarv2.bitbucket.com"

BITBUCKET_USER=$1
BITBUCKET_PASSWORD=$2

BITBUCKET_PROYECT=$3
BITBUCKET_REPOSITORY=$4

IFS=',' read -r -a USER_ARRAY <<< "$5"

get_user () {
    local BIBUCKET_USER_URL="${BITBUCKET_URL}/rest/api/1.0/users?filter=$1"
    local USER_JSON=$(curl -u $BITBUCKET_USER:$BITBUCKET_PASSWORD -H "Content-Type: application/json" -X GET $BIBUCKET_USER_URL)
    local BIBUCKET_ADD_REV="${BITBUCKET_URL}/rest/default-reviewers/1.0/projects/$BITBUCKET_PROYECT/repos/$BITBUCKET_REPOSITORY/condition"

    if [ $(echo $USER_JSON| jq '.size') = 1 ]
    then
        USER_JSON=$(echo $USER_JSON | jq '.values[0]')
        declare -A DICT
        DICT[name]=$(echo $USER_JSON | jq '.name' )
        DICT[email_address]=$(echo $USER_JSON | jq '.emailAddress')
        DICT[id]=$(echo $USER_JSON | jq '.id')	
        DICT[display_name]=$(echo $USER_JSON | jq '.displayName')
        DICT[slug]=$(echo $USER_JSON | jq '.slug')

        curl -u $BITBUCKET_USER:$BITBUCKET_PASSWORD -H "Content-Type: application/json" -X POST $BIBUCKET_ADD_REV -d '
        {
         "reviewers": [
            {
                "name": '"${DICT[name]}"',
                "emailAddress": '"${DICT[email_address]}"',
                "id": '${DICT[id]}',
                "displayName": '"${DICT[display_name]}"',
                "active": true,
                "slug": '"${DICT[slug]}"',
                "type": "NORMAL"        
           }
        ],
        "sourceMatcher": {
            "active": true,
            "id": "refs/heads/**",
            "displayId": "refs/heads/**",
            "type": {
                "id": "PATTERN",
                "name": "Pattern"
            }
        },
        "targetMatcher": {
            "active": true,
            "id": "refs/heads/master",
            "displayId": "master",
            "type": {
                "id": "BRANCH",
                "name": "Branch"
            }
        },
        "requiredApprovals": 3
        }'

    else
        echo "EMAIL ${1} DOES NOT EXISTS ON BITBUCKET SERVER..."       
        exit
    fi
}

for i in "${USER_ARRAY[@]}"
do
    get_user $i
done
