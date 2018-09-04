1. Post to create user
Post to http://localhost:9090/buffer/createUser

which sends request to Jira server at http://localhost:8181/rest/api/2/user with basic authentication

Json content of person to be created:

{
    "name": "user3",
    "password": "password",
    "emailAddress": "user3@example.com",
    "displayName": "user3"
}


2. CreatePersonQueue tracks users need to be created; it is moved to FinishedCreationQueue once the rest call was sent to Jira successfully.