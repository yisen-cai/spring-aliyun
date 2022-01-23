## Spring Aliyun Integration

> Define beans and auto configuration over aliyun-sdk



#### Modules

- aliyun-library

- spring-boot-aliyun-starter



#### Usage

##### Add dependency

Gradle

~~~kotlin
implementation("com.glancebar.aliyun:spring-boot-starter-aliyun:LATEST")
~~~

Maven

~~~xml
<dependency>
  <groupId>com.glancebar.wechat</groupId>
  <artifactId>spring-boot-starter-aliyun</artifactId>
  <version>LATEST</version>
</dependency>
~~~



##### Define configuration

~~~java
aliyun:
  sts:
    access-key-id: your-id
    access-key-secret: your-secret
    role-arn: your-role-arn
    endpoint: sts.cn-beijing.aliyuncs.com
    duration-seconds: 36000
~~~

##### Directly use

~~~java
stsService.generateStsSignature("username")
~~~

