# ClickRest


ClickRest - API to register banner clicks in DB. 

## Application

There will be two operations:

1. Add click
2. Get stat

What you have to do. Controller with two methods:
 - Click registration: *POST /banner/{id}/click*, where id is an id (string) of banner. 
Also this method accepts object as a body. 
This object contains only one field: cost (int)
 - Get statistics for the banner: *GET /banner/{id}/click*. 
The method returns an object with one field "cost", 
this field contains amount of all accumulated "costs" 
received for this banned in method POST (see item 1).

Technologies: 
 - Maven or Gradle
 - Spring Boot
 - Spring Mvc
 - Spring Data
 - MongoDB

Note: as Mongo could be setup as replica, 
so it is not good to update DB for every POST request, 
because in case of hundreds of PRS the replication process will consume a lot of resources.  
So you have to flush data to DB every 5 seconds.

It should be simple app, no logging, no configs. 

## Test

A test method which generates random banner id, 
calls two times POST method, then calls GET method 
and checks that it receives the expected answer.

docker pull mongo:3.4

docker run --name mongo4test -d -p 27017:27107 mongo:3.4
docker exec -it mongo4test bash