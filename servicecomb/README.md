## 启动说明

### 1. alpha server

#### 1.1 准备postgres数据库

  ``` sql
-- 创建用户
create user saga;
create database servicecomb_saga owner saga;
alter user saga with password 'sagapwd';

-- 创建数据库
CREATE TABLE IF NOT EXISTS TxEvent (
  surrogateId BIGSERIAL PRIMARY KEY,
  serviceName varchar(36) NOT NULL,
  instanceId varchar(36) NOT NULL,
  creationTime timestamp(6) NOT NULL DEFAULT CURRENT_DATE,
  globalTxId varchar(36) NOT NULL,
  localTxId varchar(36) NOT NULL,
  parentTxId varchar(36) DEFAULT NULL,
  type varchar(50) NOT NULL,
  compensationMethod varchar(512) NOT NULL,
  expiryTime timestamp(6) NOT NULL,
  retryMethod varchar(512) NOT NULL,
  retries int NOT NULL DEFAULT 0,
  payloads bytea
);

CREATE INDEX IF NOT EXISTS saga_events_index ON TxEvent (surrogateId, globalTxId, localTxId, type, expiryTime);
CREATE INDEX IF NOT EXISTS saga_global_tx_index ON TxEvent (globalTxId);


CREATE TABLE IF NOT EXISTS Command (
  surrogateId BIGSERIAL PRIMARY KEY,
  eventId bigint NOT NULL UNIQUE,
  serviceName varchar(36) NOT NULL,
  instanceId varchar(36) NOT NULL,
  globalTxId varchar(36) NOT NULL,
  localTxId varchar(36) NOT NULL,
  parentTxId varchar(36) DEFAULT NULL,
  compensationMethod varchar(512) NOT NULL,
  payloads bytea,
  status varchar(12),
  lastModified timestamp(6) NOT NULL DEFAULT CURRENT_DATE,
  version bigint NOT NULL
);

CREATE INDEX IF NOT EXISTS saga_commands_index ON Command (surrogateId, eventId, globalTxId, localTxId, status);


CREATE TABLE IF NOT EXISTS TxTimeout (
  surrogateId BIGSERIAL PRIMARY KEY,
  eventId bigint NOT NULL UNIQUE,
  serviceName varchar(36) NOT NULL,
  instanceId varchar(36) NOT NULL,
  globalTxId varchar(36) NOT NULL,
  localTxId varchar(36) NOT NULL,
  parentTxId varchar(36) DEFAULT NULL,
  type varchar(50) NOT NULL,
  expiryTime TIMESTAMP NOT NULL,
  status varchar(12),
  version bigint NOT NULL
);

CREATE INDEX IF NOT EXISTS saga_timeouts_index ON TxTimeout (surrogateId, expiryTime, globalTxId, localTxId, status);

CREATE TABLE IF NOT EXISTS tcc_global_tx_event (
  surrogateId BIGSERIAL PRIMARY KEY,
  globalTxId varchar(36) NOT NULL,
  localTxId varchar(36) NOT NULL,
  parentTxId varchar(36) DEFAULT NULL,
  serviceName varchar(36) NOT NULL,
  instanceId varchar(36) NOT NULL,
  txType varchar(12),
  status varchar(12),
  creationTime timestamp(6) NOT NULL DEFAULT CURRENT_DATE,
  lastModified timestamp(6) NOT NULL DEFAULT CURRENT_DATE
);

CREATE UNIQUE INDEX IF NOT EXISTS tcc_global_tx_event_index ON tcc_global_tx_event (globalTxId, localTxId, parentTxId, txType);

CREATE TABLE IF NOT EXISTS tcc_participate_event (
  surrogateId BIGSERIAL PRIMARY KEY,
  serviceName varchar(36) NOT NULL,
  instanceId varchar(36) NOT NULL,
  globalTxId varchar(36) NOT NULL,
  localTxId varchar(36) NOT NULL,
  parentTxId varchar(36) DEFAULT NULL,
  confirmMethod varchar(512) NOT NULL,
  cancelMethod varchar(512) NOT NULL,
  status varchar(50) NOT NULL,
  creationTime timestamp(6) NOT NULL DEFAULT CURRENT_DATE,
  lastModified timestamp(6) NOT NULL DEFAULT CURRENT_DATE
);

CREATE UNIQUE INDEX IF NOT EXISTS tcc_participate_event_index ON tcc_participate_event (globalTxId, localTxId, parentTxId);

CREATE TABLE IF NOT EXISTS tcc_tx_event (
  surrogateId BIGSERIAL PRIMARY KEY,
  globalTxId varchar(36) NOT NULL,
  localTxId varchar(36) NOT NULL,
  parentTxId varchar(36) DEFAULT NULL,
  serviceName varchar(36) NOT NULL,
  instanceId varchar(36) NOT NULL,
  methodInfo varchar(512) NOT NULL,
  txType varchar(12),
  status varchar(12),
  creationTime timestamp(6) NOT NULL DEFAULT CURRENT_DATE,
  lastModified timestamp(6) NOT NULL DEFAULT CURRENT_DATE
);

CREATE UNIQUE INDEX IF NOT EXISTS tcc_tx_event_index ON tcc_tx_event (globalTxId, localTxId, parentTxId, txType);

CREATE TABLE IF NOT EXISTS master_lock (
  serviceName varchar(36) not NULL,
  expireTime timestamp(3) not NULL,
  lockedTime timestamp(3) not NULL,
  instanceId  varchar(255) not NULL,
  PRIMARY KEY (serviceName)
);

CREATE INDEX IF NOT EXISTS master_lock_index ON master_lock (serviceName);

-- 赋予用户 saga 表权限
GRANT ALL ON TxEvent TO saga;
GRANT ALL ON Command TO saga;
GRANT ALL ON TxTimeout TO saga;
GRANT ALL ON tcc_global_tx_event TO saga;
GRANT ALL ON tcc_participate_event TO saga;
GRANT ALL ON tcc_tx_event TO saga;
GRANT ALL ON master_lock TO saga;

GRANT ALL ON SEQUENCE public.command_surrogateid_seq TO saga;
GRANT ALL ON SEQUENCE public.tcc_global_tx_event_surrogateid_seq TO saga;
GRANT ALL ON SEQUENCE public.tcc_participate_event_surrogateid_seq TO saga;
GRANT ALL ON SEQUENCE public.tcc_tx_event_surrogateid_seq TO saga;
GRANT ALL ON SEQUENCE public.txevent_surrogateid_seq TO saga;
GRANT ALL ON SEQUENCE public.txtimeout_surrogateid_seq TO saga;
  ```

#### 1.2 下载 alpha server

- 下载页面 http://servicecomb.apache.org/cn/release/

- 启动命令

  alpha server 源码

  https://github.com/apache/servicecomb-pack/blob/master/alpha/alpha-server/src/main/resources/application.yaml

```bash
#!/bin/bash

java -Dspring.profiles.active=prd 
-D"spring.datasource.url=jdbc:postgresql://localhost:5432/servicecomb_saga?useSSL=false" -Dspring.datasource.username=saga -Dspring.datasource.password=sagapwd -jar alpha-server-0.3.0-exec.jar
```

- 端口

  通过源码可知， alpha server 共默认暴漏两个端口 8080、8090，8080 端口是 Omega 需要连接的端口。

  ```yaml
  server:
    port: 8090
  alpha:
    server:
      host: 0.0.0.0
      port: 8080
  ```

### 2. Omega

#### 2.1 依赖

```xml
<dependency>
    <groupId>org.apache.servicecomb.pack</groupId>
    <artifactId>omega-spring-starter</artifactId>
    <version>${servicecomb.pack.version}</version>
</dependency>
<!-- 应用间的调用通过 RestTemplate，Omega 支持 -->
<dependency>
    <groupId>org.apache.servicecomb.pack</groupId>
    <artifactId>omega-transport-resttemplate</artifactId>
    <version>${servicecomb.pack.version}</version>
</dependency>
<!-- 应用间的调用通过 Feign，Omega 支持 -->
<dependency>
    <groupId>org.apache.servicecomb.pack</groupId>
    <artifactId>omega-transport-feign</artifactId>
    <version>${servicecomb.pack.version}</version>
</dependency>
```

#### 2.2 注解

**启动类，添加注解 @EnableOmega **

```java
@EnableOmega
@SpringBootApplication
public class SpiritStorageApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpiritStorageApplication.class, args);
	}
}
```

- 事务启动方

```java
/** Service **/
@SagaStart
public void userPay(Order order) {
}
```

- 事务参与方

```java
@Compensable(compensationMethod = "cancelCreate")
public void create(Order order) {
}

public void cancelCreate(Order order) {
}
```

#### 2.3 配置

Omega 使用 grpc client 与Alpha Server 建立连接，不要配成 localhost 或 127.0.0.1。

```yaml
## 配置 alpha server 的地址，io.grpc.internal.ManagedChannelImpl 的方法 getNameResolver
alpha:
  cluster: 
    address: peer1:8080
```

### 3. 使用问题

1. 超时问题

   alpha — omega 超时异常时，在补偿操作后，会再次回调正常事务，导致数据异常

   ```java
   /**
   	情况1，有问题
   	执行顺序 decreaseStock --> cancelDecreaseStock -> dao.save(commodityCode, stock);
   	dao.save(commodityCode, totalCount - count) 会执行两次
   **/
   @Compensable(compensationMethod = "cancelDecreaseStock", timeout = 2)
   public void decreaseStock(String commodityCode, int count) {
       int stock = totalCount - count;
       dao.save(commodityCode, stock);
       Thread.sleep(10000L);
   }
   
   /**
   	情况2，没有问题
   	因为 dao.save(commodityCode, stock) 因超时未执行
   	执行顺序 sleep(10000L) --> cancelDecreaseStock -> dao.save(commodityCode, stock);
   **/
   @Compensable(compensationMethod = "cancelDecreaseStock", timeout = 2)
   public void decreaseStock(String commodityCode, int count) {
       Thread.sleep(10000L);
       int stock = totalCount - count;
       dao.save(commodityCode, stock);
   }
   
   public void cancelDecreaseStock(String commodityCode, int count) {
       int stock = totalCount + count;
       dao.save(commodityCode, stock);
   }
   ```

2. alpha server

   - 不稳定，omega 和 alpha 之间的 grpc 连接时断时续