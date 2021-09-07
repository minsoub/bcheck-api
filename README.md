### Document DB 접속
- AWS의 VPN 연결을 통해서 접속해야 함.  
- 로컬에서 테스트시 VPN 연결
- AWS DocumentDB 접속을 위해서 pem 파일이 AWS로 부터 발급받아야 함.
- resources/cert/rds-combined-ca-bundle.pem

### Client를 통한 DocumentDB 접속 (Connection Settings)
#### Connection
- Robo 3T 를 사용해서 DocumentDB 접속 
- Connection Type : Direct Connection
- Connection Name : bcheckdb
- Connection Address : bcheck-docdb.cluster-cfe0kcb9osrg.ap-northeast-2.docdb.amazonaws.com
- Connection Port : 27017
#### Authentication
- Database : admin
- User Name : bcheckuser
- Password : 문의요망
#### TLS
- Use TLS protocol : checked
- Authentication Method : Use CA Certificate
- CA Certificate: AWS 발급받은 pem 파일 등록

### Git Clone
- git clone https://git-codecommit.ap-northeast-2.amazonaws.com/v1/repos/new-bcheck-api
- branch checkout
```shell
PS C:\DevPJT\bcheck-api> git remote update
Fetching origin
From https://git-codecommit.ap-northeast-2.amazonaws.com/v1/repos/new-bcheck-api
 * [new branch]      dev        -> origin/dev
PS C:\DevPJT\bcheck-api> git branch -r
  origin/HEAD -> origin/master
  origin/dev
  origin/master
PS C:\DevPJT\bcheck-api> git checkout -t origin/dev
Switched to a new branch 'dev'
Branch 'dev' set up to track remote branch 'dev' from 'origin'.
PS C:\DevPJT\bcheck-api> git branch
* dev
  master
```
### 기타 파일 정보
- buildspec.yaml
  - AWS CodeBuild에서 사용될 buildspec 정의 파일   
  - 코드 빌드에서 빌드 및 ECR에 도커이미지를 전송하기 위한 권한이 부여되어야 함.   
  - 또한 EKS에 대한 접근 권한이 부여되어야 함.  
- Dockerfile
  - 도커이미지 정의 파일로 코드 빌드에서 해당 파일 참조해서 도커이미지를 생성한다. 
  - 도커이미지가 컨테이너로 올라가 서비스 될 때 실행될 EntryPoint도 정의 
- bcheck-api-deployment.yaml
  - EKS deployment 배포 파일 

### Prometheus와 Grafana를 이용한 Spring Boot 어플리케이션 모니터링 설정
- build.gradle
  Micrometer 메트릭 엔진을 설정한다.   
  ```yaml
    // Micrometer core and prometheus registry
    implementation group: 'io.micrometer', name: 'micrometer-core', version: '1.7.3'
    implementation group: 'io.micrometer', name: 'micrometer-registry-prometheus', version: '1.7.3'
  ```
- application.yaml
  ```yaml
  # micrometer prometheus
  management:
    endpoint:
      metrics:
        enabled: true
      prometheus:
        enabled: true
    endpoints:
      web:
        exposure:
          include: health, info, metrics, prometheus
    metrics:
      export:
        prometheus:
          enabled: true  
  ```
- Application을 실행하고 http://localhost:7777/app/actuator 확인
  ```json
  {"_links":{"self":{"href":"http://localhost:7777/app/actuator","templated":false},"health":{"href":"http://localhost:7777/app/actuator/health","templated":false},"health-path":{"href":"http://localhost:7777/app/actuator/health/{*path}","templated":true},"info":{"href":"http://localhost:7777/app/actuator/info","templated":false},"prometheus":{"href":"http://localhost:7777/app/actuator/prometheus","templated":false},"metrics-requiredMetricName":{"href":"http://localhost:7777/app/actuator/metrics/{requiredMetricName}","templated":true},"metrics":{"href":"http://localhost:7777/app/actuator/metrics","templated":false}}}
   ```
- http://localhost:7777/app/actuator/prometheus 접속해서 Prometheus가 수집할 Metric 확인
  ```shell
  # HELP jvm_gc_memory_promoted_bytes_total Count of positive increases in the size of the old generation memory pool before GC to after GC
  # TYPE jvm_gc_memory_promoted_bytes_total counter
  jvm_gc_memory_promoted_bytes_total 5390992.0
  # HELP jvm_classes_unloaded_classes_total The total number of classes unloaded since the Java virtual machine has started execution
  # TYPE jvm_classes_unloaded_classes_total counter
  jvm_classes_unloaded_classes_total 0.0
  # HELP jvm_gc_memory_allocated_bytes_total Incremented for an increase in the size of the (young) heap memory pool after one GC to before the next
  # TYPE jvm_gc_memory_allocated_bytes_total counter
  jvm_gc_memory_allocated_bytes_total 4.99122176E8
  # HELP jvm_gc_live_data_size_bytes Size of long-lived heap memory pool after reclamation
  # TYPE jvm_gc_live_data_size_bytes gauge
  jvm_gc_live_data_size_bytes 0.0
  # HELP process_cpu_usage The "recent cpu usage" for the Java Virtual Machine process
  # TYPE process_cpu_usage gauge
  process_cpu_usage 0.08950092565437348
  # HELP jdbc_connections_max Maximum number of active connections that can be allocated at the same time.
  # TYPE jdbc_connections_max gauge
  jdbc_connections_max{name="dataSource",} 10.0
  # HELP system_cpu_count The number of processors available to the Java virtual machine
  ```
- Spring Boot 2는 기본적으로 다음과 같은 metric들을 제공하고 있다.
JVM, report utilization of:  
Various memory and buffer pools  
Statistics related to garbage collection  
Thread utilization  
Number of classes loaded/unloaded  
CPU usage  
Spring MVC and WebFlux request latencies  
RestTemplate latencies  
Cache utilization  
Datasource utilization, including HikariCP pool metrics  
RabbitMQ connection factories  
File descriptor usage  
Logback: record the number of events logged to Logback at each level  
Uptime: report a gauge for uptime and a fixed gauge representing the application’s absolute start time  
Tomcat usage  

### codecommit 과 codebuild 연결을 위한 람다함수 생성 
- Lambda function : bcheck-api-codecommit-to-codebuild-trigger
```python
import json
import boto3

codecommit = boto3.client('codecommit')

def lambda_handler(event, context):
    print(event['Records'])
    #branch 이름 셋팅
    dev_trigger_branch = 'refs/heads/dev'   ## dev version 
    master_trigger_branch = 'refs/heads/master'  ## master version

    #Log the updated references from the event
    references = { reference['ref'] for reference in event['Records'][0]['codecommit']['references'] }
    print("References: "  + str(references))
    print("Trigger branch: "  + dev_trigger_branch)


    #branch 이름 문자열로 비교, 동시에 두개 이상의 branch 가 push 되는 경우 references 
    if str(references).find(str(dev_trigger_branch)) >= 0 :
        cb = boto3.client('codebuild')

        #Get the repository from the event and show its git clone URL
        repository = event['Records'][0]['eventSourceARN'].split(':')[5]
        try:
            print('Starting build for codebulid {0}'.format('bcheck--api'))
            build = {
               'projectName': 'bcheck-api', # event['Records'][0]['customData'],
               'sourceVersion': event['Records'][0]['codecommit']['references'][0]['commit']
            }
            print('source version => {0}'.format(event['Records'][0]['codecommit']['references'][0]['commit']))
            response = cb.start_build(**build);
            #cb.start_build(
            #    projectName='bcheck-gateway-api'
            #)
            print('Successfully launched a new CodePipeline build!')
        except Exception as e:
            print(e)
            print('Error getting repository {}. Make sure it exists and that your repository is in the same region as this function.'.format(repository))
            raise e
    elif str(references).find(str(master_trigger_branch)) >= 0 :
        cb = boto3.client('codebuild')

        #Get the repository from the event and show its git clone URL
        repository = event['Records'][0]['eventSourceARN'].split(':')[5]
        try:
            print('Starting build for codebulid {0}'.format('bcheck--api-prod'))
            build = {
               'projectName': 'bcheck-api-prod', # event['Records'][0]['customData'],
               'sourceVersion': event['Records'][0]['codecommit']['references'][0]['commit']
            }
            print('source version => {0}'.format(event['Records'][0]['codecommit']['references'][0]['commit']))
            response = cb.start_build(**build);
            #cb.start_build(
            #    projectName='bcheck-gateway-api'
            #)
            print('Successfully launched a new CodePipeline build!')
        except Exception as e:
            print(e)
            print('Error getting repository {}. Make sure it exists and that your repository is in the same region as this function.'.format(repository))
            raise e       
    else :
        print("References: "  + str(references) + " is not trigger branch")
```
- 람다함수 트리거 생성 
  - 트리거 추가를 통해서 codecommit 과 연결 작업 
- 람다함수 권한 설정 
  - 람다함수가 codebuild를 작업할 수 있는 권한 추가한다.