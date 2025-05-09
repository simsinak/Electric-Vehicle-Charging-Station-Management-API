# REST API DEVOLON TASK
powered by bootstrap
[![](https://user-images.githubusercontent.com/33158051/103466606-760a4000-4d14-11eb-9941-2f3d00371471.png)](https://spring.io/)[![](https://avatars0.githubusercontent.com/u/5429470?s=200&v=4)](https://docker.com)

This is the result!
You can download the Docker Image or the Jar file from the following link
#### [Download Docker Image or Jar](https://github.com/simsinak/Rest_API_for_the_electric_vehicle_charging_station_management_system/releases)

## Running
- #### Jar File
```sh
$ java -jar {file_name.jar}
```
 - #### Docker Image File
```sh
$ docker load -i {image_path.tar}
$ docker run -p 8080:8080 {image_name}
```
Verify the deployment by navigating to your server address in your preferred browser.
```sh
$ localhost:8080
```
# IMPORTANT POINTS!

  - To make the api more scalable, paging added to API, the paging links are in header Link of response
  - To find the locations in a distqnce, we must find all POI with in a specific rectangle and then compute their distance to prune the corners. actually we could use geo types and specific prebuilt methods of SQL for the Geo locations. But, Because the DBMS may change and these SQL commands are not identical in all DBMS, We prefered to implement it in native SQL in Station Repository. It is even fast for millions of records.:)
  - It is for test so, we used H2 DB
  - API URI starts with /api
  - UI starts with /
  - The DB schema did not changed!
  
 | Method | path | purpose|
| ------ | ------ |------|
| GET | /api/companies?page={page}&limit={limit} | Get All Compnies|
| GET | /api/companies/{id} | Get Specific Company |
| DELETE | /api/companies/{id} | Delete Specific Company
| PUT | /api/companies/{id} | Update Specific Company
| POST | /api/companies | Create New Company
| GET | /api/companies/{id}/stations | Get All Children Stations in a Hierarchial Structure For a Specific Company
| GET | /api/stations?page={page}&limit={limit} | Get All Stations
| GET | /api/stations/{id} | Get Specific Station |
| DELETE | /api/stations/{id} | Delete Specific Station
| PUT | /api/stations/{id} | Update Specific Station
| POST | /api/stations] | Create New Station
| PATCH |  /api/stations/{id} | Patch Specific Station
| GET | /api/stations/around?latitude={lat}&longitude={lon}&distance={dist} | Get All Stations in a Distance From a Point

