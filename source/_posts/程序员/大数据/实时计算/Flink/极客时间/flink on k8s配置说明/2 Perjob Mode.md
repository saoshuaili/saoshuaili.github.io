---
title: 2 Perjob Mode  
date: 2022-12-04 02:42:26  
tags: []  
categories:
  - 大数据
  - 实时计算
  - Flink
  - 极客时间
  - flink on k8s配置说明
---

# 配置文件

## ConfigMap

和 `Session Mode` 的 `configmap` 完全相同。这里就不赘述了

## Service

### rest service

```yaml
# jobmanager-rest-service.yaml
apiVersion: v1
kind: Service
metadata:
  name: flink-jobmanager-rest-per-job
spec:
  type: NodePort
  ports:
  - name: rest
    port: 8081
    targetPort: 8081
    nodePort: 30086
  selector:
    app: flink
    component: jobmanager
```

这里可以看到，和 `Session Mode` 的 `rest-service` 完全相同，目的是为了将 `rest` 服务的端口向外暴露，让外部可以通过访问 `30086` 端口来访问 `flink` 的 `rest` 服务。

### jobmanager service

```yaml
# jobmanager-service.yaml
apiVersion: v1
kind: Service
metadata:
  name: flink-jobmanager-per-job
spec:
  type: ClusterIP
  ports:
  - name: rpc
    port: 6123
  - name: blob-server
    port: 6124
  - name: webui
    port: 8081
  selector:
    app: flink
    component: flink-jobmanager-per-job
```

可以看到，和 `Session Mode` 的也完全相同。

## Deployment

### job manager deployment

```yaml
# jobmanager-job-deployment.yaml
apiVersion: batch/v1
kind: Job
metadata:
  name: flink-jobmanager-per-job
spec:
  template:
    metadata:
      labels:
        app: flink
        component: jobmanager
    spec:
      restartPolicy: OnFailure
      containers:
        - name: jobmanager
          image: flink:1.11.1-scala_2.11
          env:
          args: ["standalone-job", "--job-classname", "org.apache.flink.streaming.examples.windowing.TopSpeedWindowing", --allowNonRestoredState, --output hdfs://node02:8020/flink-training/wordcount-output] 
          # optional arguments: ["--job-id", "<job id>", "--fromSavepoint", "/path/to/savepoint", "--allowNonRestoredState"]
          ports:
            - containerPort: 6123
              name: rpc
            - containerPort: 6124
              name: blob-server
            - containerPort: 8081
              name: webui
          livenessProbe:
            tcpSocket:
              port: 6123
            initialDelaySeconds: 30
            periodSeconds: 60
          volumeMounts:
            - name: flink-config-volume
              mountPath: /opt/flink/conf
            - name: job-artifacts-volume
              mountPath: /opt/flink/usrlib/
          securityContext:
            runAsUser: 9999  # refers to user _flink_ from official flink image, change if necessary
      volumes:
        - name: flink-config-volume
          configMap:
            name: flink-config-per-job
            items:
              - key: flink-conf.yaml
                path: flink-conf.yaml
              - key: log4j-console.properties
                path: log4j-console.properties
        - name: job-artifacts-volume
          hostPath:
            path: /home/flink-training/flink-1.11.1/examples/streaming/
```

在这里附上和 `Session Mode` 的比较结果：

![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20221120235431.png)

可以看到，最主要的区别首先是 `Kind` ，`Session Mode` 的类型是 `Deployment` ，这是一个常驻进程，会一直保证有一个 `Pod` 的存在，而 `Perjob Mode` 的类型是 `Job` ，这是一个只生成一次的任务，每次调用生成一个镜像。

### task manager deployment

```yaml
# taskmanager-job-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: flink-taskmanager-per-job
spec:
  replicas: 2
  selector:
    matchLabels:
      app: flink
      component: taskmanager
  template:
    metadata:
      labels:
        app: flink
        component: taskmanager
    spec:
      containers:
      - name: taskmanager
        image: flink:1.11.1-scala_2.11 #镜像可以自定义镜像
        env:
        args: ["taskmanager"] # 指定TaskManager类型实例
        ports:
        - containerPort: 6122
          name: rpc
        - containerPort: 6125
          name: query-state
        livenessProbe:
          tcpSocket:
            port: 6122
          initialDelaySeconds: 30
          periodSeconds: 60
        volumeMounts:
        - name: flink-config-volume #flink configuration 路径挂载
          mountPath: /opt/flink/conf/
        - name: job-artifacts-volume
          mountPath: /opt/flink/usrlib
        securityContext:
          runAsUser: 9999  # refers to user _flink_ from official flink image, change if necessary
      volumes:
      - name: flink-config-volume # 从configmap中获取
        configMap:
          name: flink-config-per-job
          items:
          - key: flink-conf.yaml
            path: flink-conf.yaml
          - key: log4j-console.properties
            path: log4j-console.properties
      - name: job-artifacts-volume # 指定宿主机路径
        hostPath:
          path: /home/flink-training/flink-1.11.1/examples/streaming/
```

这里附带上和 `Session Mode` 的比较：

![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20221121004245.png)

可以看到，这里和 `Session Mode` 几乎是完全相同的。




