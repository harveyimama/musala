## Harvey Imama's Drone Service

[[_TOC_]]

---

:scroll: **START**


### Introduction

There is a major new technology that is destined to be a disruptive force in the field of transportation: **the drone**. Just as the mobile phone allowed developing countries to leapfrog older technologies for personal communication, the drone has the potential to leapfrog traditional transportation infrastructure.

Useful drone functions include delivery of small items that are (urgently) needed in locations with difficult access.

---

### Task description

 **10 drones** are seeded upon loading. The default weight for all rones are 500. These Settinsg can be modified in teh docker-compose.yml file.

---

### To Deploy

Using **My Repository in Docker hub**:
- Copy the docker-compose.yml file to server
- In folder run docker-compose up
- Service will be avialble at http://<<<server ip>>>:9091 

Using **Your image repository**:
- Go to .github/workflows 
- Modify the dev_musala.yml file
- Add your repository secrets on line 36 and 37, these should have been first set up on git
- Change the image tag on line 45
- Push change to main branch 
- Change image tag also on docker file
- Copy the docker-compose.yml file to server
- In folder run docker-compose up
- Service will be avialble at http://<<<server ip>>>:9091 
 
---

### API Documemtatioon 

Api documentatin available in link below

https://documenter.postman.com/preview/2308456-d2f71e4c-f858-49b5-872b-0f25c2e6d6a1?environment=&versionTag=latest&apiName=CURRENT&version=latest&documentationLayout=classic-double-column&right-sidebar=303030&top-bar=FFFFFF&highlight=EF5B25

:scroll: **END** 
