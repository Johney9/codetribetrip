#!/usr/bin/env bash
curl -X POST -d destination='novi sad' -d startDate=2018-4-13 -d endDate=2018-4-26 -d comment='eh' localhost:8080/trip/create-trip
curl -X POST -d destination='beograd' -d startDate=2018-3-13 -d endDate=2018-3-26 -d comment='he' localhost:8080/trip/create-trip
curl -X POST -d destination='hawaii' -d startDate=2018-7-13 -d endDate=2018-8-26 -d comment='long vacation' localhost:8080/trip/create-trip
curl -X POST -d destination='taiwan' -d startDate=2018-5-5 -d endDate=2018-5-6 -d comment='short stay' localhost:8080/trip/create-trip
curl -X POST -d destination='taiwan' -d startDate=2018-5-5 -d endDate=2018-5-6 -d comment='short stay' localhost:8080/trip/create-trip
curl -X GET localhost:8080/trip/show-future-trips-starting-before?startDate=2018-5-5
curl -X GET "http://localhost:8080/trip/search?destination=taiwan" -H  "accept: */*"