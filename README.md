# Spring Boot Crud JPA Thymeleaf demo

## Table of Contents

* [Run Locally](#run-locally)
* [Build using Tanzu Build Service](#build-using-tanzu-build-service)
* [Deploy to TAS4K8S - Docker Push](#deploy-to-tas4k8s---docker-push)
* [Deploy to TAS4K8S - Source Artifact Push](#deploy-to-tas4k8s---source-artifact-push)

## Run Locally 

Clone or Fork repository as follows.

``` bash
$ git clone https://github.com/papicella/spring-crud-thymeleaf-demo.git
$ cd spring-crud-thymeleaf-demo
```

Build/Package as shown below.

```bash
$ ./mvnw -D skipTests package
```

Run as shown below accessing in a browser using - [http://localhost:8080/]

```bash
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

```bash
pb project create papicella
pb project target papicella
```

Create a registry secret and repository secret as shown below.

```bash
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

```bash
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

```bash
$ pb image logs index.docker.io/pasapples/spring-crud-thymeleaf-demo -b 1 -f
```

View image build once complete

```bash
$ pb image builds index.docker.io/pasapples/spring-crud-thymeleaf-demo
Build    Status     Started Time           Finished Time          Reason    Digest
-----    ------     ------------           -------------          ------    ------
    1    SUCCESS    2020-05-26 10:18:57    2020-05-26 10:22:53    CONFIG    80da8d3b999707d379bb9353bccf1370f8df54daab275b43334cb45214c2709b
```

## Deploy to TAS4K8S - Docker Push

For this step you will need TAS4K8S beta installed which can be downloaded and installed using the two link below.

* https://network.pivotal.io/products/tas-for-kubernetes/
* https://docs.pivotal.io/tas-kubernetes/0-1/

Ensure your using the latest CF CLI which can be obtained using this link - https://github.com/cloudfoundry/cli

```bash
$ cf version
cf version 6.51.0+2acd15650.2020-04-07
```

Target your API endpoint as shown below

```bash
$ cf api https://api.system.run.haas-210.pez.pivotal.io --skip-ssl-validation
Setting api endpoint to https://api.system.run.haas-210.pez.pivotal.io...
OK

api endpoint:   https://api.system.run.haas-210.pez.pivotal.io
api version:    2.148.0
```

Login as follows you will need a username / password.

```bash
$ cf auth admin UAA_ADMIN_PASSWORD_REPLACE
$ cf target -o system
$ cf create-space development
$ cf target -s development
```

Enable diego docker flag to push from a docker image.

```bash
$ cf enable-feature-flag diego_docker
Setting status of diego_docker as admin...

OK

Feature diego_docker Enabled.
```

Push application as shown below.

```bash
$ cf push my-springboot-app --docker-image pasapples/spring-crud-thymeleaf-demo -i 1 -m 1g
Pushing app my-springboot-app to org system / space development as admin...
Getting app info...
Creating app with these attributes...
+ name:           my-springboot-app
+ docker image:   pasapples/spring-crud-thymeleaf-demo
+ instances:      1
+ memory:         1G
  routes:
+   my-springboot-app.system.run.haas-210.pez.pivotal.io

Creating app my-springboot-app...
Mapping routes...

Staging app and tracing logs...

Waiting for app to start...

name:                my-springboot-app
requested state:     started
isolation segment:   placeholder
routes:              my-springboot-app.system.run.haas-210.pez.pivotal.io
last uploaded:       Tue 26 May 10:53:14 AEST 2020
stack:
docker image:        pasapples/spring-crud-thymeleaf-demo

type:           web
instances:      1/1
memory usage:   1024M
     state     since                  cpu    memory    disk      details
#0   running   2020-05-26T00:53:16Z   0.0%   0 of 1G   0 of 1G
```

Access in browser as shown below.

![alt tag](https://i.ibb.co/Ms18zpW/spring-crud-thymeleaf-2.png)

## Deploy to TAS4K8S - Source Artifact Push

From the previously cloned or forked GitHub project above (https://github.com/papicella/spring-crud-thymeleaf-demo) create a manifest.yaml file as shown below.

```bash
$ cat manifest.yml
---
applications:
- name: my-springboot-app
  memory: 1024M
  instances: 1
  path: ./target/spring-crud-thymeleaf-demo-0.0.1-SNAPSHOT.jar
```

Delete application if it exists

```bash
$ cf delete -f my-springboot-app
Deleting app my-springboot-app in org system / space development as admin...
OK
```

Deploy using built artifact as shown below.

```bash
$ cf push -f manifest.yml
Pushing from manifest to org system / space development as admin...
Using manifest file /Users/papicella/piv-projects/Baeldung-DEMOS/spring-crud-thymeleaf-demo/manifest.yml
Getting app info...
Creating app with these attributes...
+ name:        my-springboot-app
  path:        /Users/papicella/pivotal/DemoProjects/spring-starter/pivotal/Baeldung-DEMOS/spring-crud-thymeleaf-demo/target/spring-crud-thymeleaf-demo-0.0.1-SNAPSHOT.jar
+ instances:   1
+ memory:      1G
  routes:
+   my-springboot-app.system.run.haas-210.pez.pivotal.io

Creating app my-springboot-app...
Mapping routes...
Comparing local files to remote cache...
Packaging files to upload...
Uploading files...
 34.24 MiB / 34.24 MiB [===============================================================================================================================================================================================================================] 100.00% 32s

Waiting for API to complete processing files...

Staging app and tracing logs...

Waiting for app to start...

name:                my-springboot-app
requested state:     started
isolation segment:   placeholder
routes:              my-springboot-app.system.run.haas-210.pez.pivotal.io
last uploaded:       Tue 26 May 11:30:06 AEST 2020
stack:
buildpacks:

type:           web
instances:      1/1
memory usage:   1024M
     state     since                  cpu    memory    disk      details
#0   running   2020-05-26T01:30:32Z   0.0%   0 of 1G   0 of 1G
```

<hr size=2 />
Pas Apicella [pasa at vmware.com] is an Advisory Application Platform Architect at VMware APJ 

[]: http://localhost:8080/