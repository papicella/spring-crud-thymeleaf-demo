# Spring Boot Crud JPA Thymeleaf demo

## Table of Contents

* [Run Locally](#run-locally)
* [Build using Tanzu Build Service](#tanzu-build-service)
* [Deploy to TAS4K8S - Docker Push](#tas4k8s-docker-push)
* [Deploy to TAS4K8S - Source Push](#tas4k8s-source-push)

## Run Locally 

Clone or Fork repository as follows

```
$ git clone https://github.com/papicella/spring-crud-thymeleaf-demo.git
$ cd spring-crud-thymeleaf-demo
```

Build/Package as shown below

```
$ ./mvnw -D skipTests package
```

Run as shown below accessing in a browser using - [http://localhost:8080/]

```
$ ./mvnw spring-boot:run
....
2020-05-26 09:39:32.849  INFO 66462 --- [           main] p.s.d.SpringCrudThymeleafDemoApplication : Started SpringCrudThymeleafDemoApplication in 2.241 seconds (JVM running for 2.522)
2020-05-26 09:39:32.893  INFO 66462 --- [           main] pas.spring.demos.LoadDatabase            : Pre loading User{id=1, name='Pas', email='pasa@vmware.com'}
2020-05-26 09:39:32.894  INFO 66462 --- [           main] pas.spring.demos.LoadDatabase            : Pre loading User{id=2, name='Lucia', email='lucia78@rocks.com'}
2020-05-26 09:39:32.895  INFO 66462 --- [           main] pas.spring.demos.LoadDatabase            : Pre loading User{id=3, name='Lucas', email='lucas@rocks.com'}
2020-05-26 09:39:32.896  INFO 66462 --- [           main] pas.spring.demos.LoadDatabase            : Pre loading User{id=4, name='Siena', email='siena@rocks.com'}
```

![alt tag](https://i.ibb.co/WKB1Cpz/spring-crud-thymeleaf-1.png)

## Build using Tanzu Build Service

The following assumes you have Tanzu Build Service installed if you don't you can skip this step and move to #tas4k8s-source-push instead or you can use the already created image as "**pasapples/spring-crud-thymeleaf-demo**" 
For more information on how to install / use Tanzu Build service use the link as follows - https://docs.pivotal.io/build-service/0-1-0/index.html

Create a project and target that project 

```
pb project create papicella
pb project target papicella
```

Create a registry secret and repository secret as shown below.

```
$ cat repository.yaml
repository: github.com
username: GIT_USER_REPLACE
password: GIT_TOKEN_REPLACE

$ pb secrets git apply -f repository.yam

$ cat registry-dockerhub.yaml registry: https://index.docker.io/v1/
username: DOCKERHUB_USER_REPLACE
password: DOCKERHUB_PASSWORD_REPLACE

$ pb secrets registry apply -f registry-dockerhub.yaml
```

Apply image configuration as shown below.

```
$ cat spring-crud-thymeleaf-dockerhub.yaml
source:
  git:
    url: https://github.com/papicella/spring-crud-thymeleaf-demo.git
    revision: master
build:
  env:
  - name: BP_JAVA_VERSION
    value: 11.*
image:
  tag: pasapples/spring-crud-thymeleaf-demo

$ pb image apply -f spring-crud-thymeleaf-dockerhub.yam
```

View logs of build as follows.

```
$ pb image logs index.docker.io/pasapples/spring-crud-thymeleaf-demo -b 1 -f
```

View image build once complete

```
$ pb image builds index.docker.io/pasapples/spring-crud-thymeleaf-demo
Build    Status     Started Time           Finished Time          Reason    Digest
-----    ------     ------------           -------------          ------    ------
    1    SUCCESS    2020-05-26 10:18:57    2020-05-26 10:22:53    CONFIG    80da8d3b999707d379bb9353bccf1370f8df54daab275b43334cb45214c2709b
```

## Deploy to TAS4K8S - Docker Push

For this step you will need TAS4K8S beta installed which can be downloaded and installed using the two link below.


Target your API endpoint as shown below

```

```


## Deploy to TAS4K8S - Source Push

<hr size=2 />
Pas Apicella [pasa at vmware.com] is an Advisory Application Platform Architect at VMware APJ 

[]: http://localhost:8080/