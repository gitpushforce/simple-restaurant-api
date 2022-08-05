Author: Masaki Nakamura

# Simple Restaurant 
* This app was developed using Java 11 under Spring boot 2.7.2 framework.
* It's already deployed on a VPS using Jenkins and docker 
* The controllers, service and Dao are tested with a coverage of 100% using Junit 5.

## Pre-requisites
* Database: We can build it locally using the docker-compose.yml file in the repository, but there is a temporal DB already builded in a VSP server with the necessary schemas. The application.properties file has the settings for that remote server by default. 
If we want to use the local DB builded by the docker-compose then we should switch the spring.datasource.url property for the local one that is currently commented out.
* The API can be builded locally, but there is a docker container of the app already deployed in a VPS server under Jenkins Pipeline. 
The Jenkinsfile used to deploy is in the repository. 

`http://153.121.71.32:8081`

(check the request endpoints)

## How to Build and Deploy

###**Local**
*  For DB build, use the docker-compose.yml placed in /dockerLocal directory.
   
   `docker-compose up --build -d`
   
*   The application.properties files contains the following property:

	`spring.datasource.url = 
`

* switch the **PROD** value for the **DOCKER** one by commenting out.

* It's easier to build the app using an IDE like IntelliJ but can be builded following the commands below.

At project root directory:

*Create jar file:  (restaurant-api.jar)*

    ./gradlew build
    
*Execute jar file:*

`java -jar /build/lib/restaurant-api.jar
`
###**PROD**
Use Jenkinsfile to build in a Jenkins pipeline.
Jenkins and Docker must be installed in PROD server.

The Jenkinsfile will apply the following stages:

1. Create build directories in Server 
2. Checkout repository from github (master branch)
3. Build the app and create the Jar file in Server.
4. Create the docker image using the Dockerfile placed in the repository.
5. Clean the created directory in server used to build the app.

Once the image is builded, create the docker container:

`docker run -p 8081:8080 --name {containerName} -it -d {imageName}:{tagName}`

# Endpoints

## queryall (GET method)
Retrieves all the orders from a specified table.
### Params:
**table** : Integer   (the table number)

### request sample
`http://localhost:8080/v1/queryall?table=2`

### response sample
`{
    "count": 2,
    "order": [
        {
            "orderId": 5,
            "tableNum": 2,
            "itemId": "0002",
            "itemName": "item2",
            "cookTime": 13
        },
        {
            "orderId": 8,
            "tableNum": 2,
            "itemId": "0009",
            "itemName": "item9",
            "cookTime": 15
        }
    ]
}`

## queryitem (GET method)
Retrieved data of a specified order

### Params:
**orderId** : Integer   (order id)

### request sample
`http://localhost:8080/v1/queryitem?orderId=7`

### response sample
`{
    "orderId": 7,
    "tableNum": 9,
    "itemId": "0006",
    "itemName": "item6",
    "cookTime": 13
}`

## items (GET method)
Retrieve the list of items available in ITEMS_TBL

### Params:
no parameter needed

### request sample
`http://localhost:8080/v1/items`

### response sample
`[
    {
        "itemId": "0001",
        "itemName": "item1"
    },
    {
        "itemId": "0002",
        "itemName": "item2"
    },
    {
        "itemId": "0003",
        "itemName": "item3"
    }
]`

## add (PUT method)
Creates an order for a specified table number.
The order can be created only with the items availables in ITEMS_TBL table in the DBm otherwise an OrderCreationException will be thrown


### Params:
**table** : Integer   (table number)

**item** : String   (item id)

### request sample
`http://localhost:8080/v1/add?table=1&item=0001`

### response sample
`{
    "success": true
}`

## del (DELETE method)
Deletes an order in DB

### Params:
**orderId** : Integer   (order id)

### request sample

`http://localhost:8080/v1/del?orderId=78`

### response sample
`{
    "success": true
}`


# DATABASE

## ORDER_TBL

| column        | PK | type      | default value   | nullable | extra|
|---------------|----|-----------|-----------------|----------|------|
| order_id      |YES | int       | -               | NO       | AUTO_INCREMENT  |
| table_num     |NO  | int       | -               | NO       | -               | 
| item_id       |NO  | var (4)   | -               | NO       | -               |
| cook_time     |NO  | int       | -               | NO       | -               |
| update_date   |NO  | timestamp | -               | YES      | CURREN_TIMESTAMP on update |
| created_date  |NO  | timestamp | curr. timestamp | NO       | - |

## ITEMS_TBL

 column      | PK  | type        | default value | nullable | extra |
|------------|-----|-------------|---------------|----------|-------|
| item_id    | YES |varchar (4)  | -             | NO       | -     |
| item_name  | NO  |varchar (20) | -             | NO       | -     |

ITEMS_TBL has data already inserted when creating DB with docker-compose.yml 