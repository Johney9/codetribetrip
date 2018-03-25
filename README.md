# codetribetrip

place `google-api.properties` with the property `key=yourGoogleApiKey` into `src/main/resources` to enable google authentication of locations

the REST api works by providing an endpoint to which users can connect and access data stored on the server.
running the server locally, some tests would be as follows:
1. curl -X POST -d destination='beograd' -d startDate=2018-3-13 -d endDate=2018-3-26 -d comment='he' localhost:8080/trip/create-trip
2. curl -X POST -d destination='hawaii' -d startDate=2018-7-13 -d endDate=2018-8-26 -d comment='long vacation' localhost:8080/trip/create-trip
3. curl -X POST -d destination='taiwan' -d startDate=2018-5-5 -d endDate=2018-5-6 -d comment='short stay' localhost:8080/trip/create-trip
4. curl -X GET localhost:8080/trip/show-future-trips-starting-before?startDate=2018-9-5
5. curl -X GET "http://localhost:8080/trip/search?destination=taiwan" -H  "accept: */*"

1-3. use Http.POST to create new Trips on the server

4. shows the sorted future trips

5. demonstrates search
