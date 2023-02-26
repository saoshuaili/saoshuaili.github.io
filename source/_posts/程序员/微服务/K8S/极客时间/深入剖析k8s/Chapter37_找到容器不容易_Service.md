---
title: Chapter37_找到容器不容易:Service、DNS与服务发现  
date: 2022-12-04 02:34:14  
tags: []  
categories:
  - 微服务
  - K8S
  - 极客时间
  - 深入剖析k8s
---
# ClusterIP 模式的 Service

现在我有一个Service：

```yaml
apiVersion: v1
kind: Service
metadata:
  name: hostnames
spec:
  selector:
    app: hostnames
  ports:
  - name: default
    protocol: TCP
    port: 80
    targetPort: 9376
```

解释：

- `metadata`是元数据，代表想要创建的service的信息，这里只指定了一个name，代表想创建的service叫做hostnames。
- `selector`是声明，声明这个 Service 只代理携带了 app=hostnames 标签的 Pod。
- `ports`是端口声明，说明这个 Service 的 80 端口，代理的是 Pod 的 9376 端口，协议是TCP。



此时我可以直接`kubectl apply -f service.yaml`

这样会创建一个服务：

![image-20221109001633876](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20221109001633876.png)

可以看见，服务名为`hostnames`，类型是`ClusterIP`，集群IP是`10.98.37.204`。

被service选中的pod，就被称为该service的**endpoints**。

值得注意的是，在这里我们选择是携带app=hostnames标签的pod，但是因为我们此时还没有创建对应的deployment，所以我们获取该service对应的endpoints时，获取得到的结果会为空。

![image-20221109002408294](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20221109002408294.png)



此时我们来创建对应的deployment，编写了以下yaml：

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: hostnames
spec:
  selector:
    matchLabels:
      app: hostnames
  replicas: 3
  template:
    metadata:
      labels:
        app: hostnames
    spec:
      containers:
      - name: hostnames
        image: k8s.gcr.io/serve_hostname
        ports:
        - containerPort: 9376
          protocol: TCP
```

解释：

- `metadata`是元数据，代表想要创建的deploy的信息，这里只指定了一个name，代表想创建的deploy叫做hostnames。
- `app`代表标签，标签名为`hostname`

然后我们执行`kubectl apply -f deploy.yaml`：

这个应用的作用，就是每次访问 9376 端口时，返回它自己的 hostname。

此时我们再获取上面service的endpoints，就可以看到多出了三个endpoints

![image-20221109003101025](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20221109003101025.png)

这三个endpoints就对应三个pods的ip地址对应的9376端口：

![image-20221109003147330](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20221109003147330.png)

需要注意的是，只有处于 Running 状态，且 readinessProbe 检查通过的 Pod，才会出现在 Service 的 Endpoints 列表里。并且，当某一个 Pod 出现问题时，Kubernetes 会自动把它从 Service 里摘除掉。

此时我们就可以通过访问该Service的url来访问对应的pods了：

![image-20221109003413489](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20221109003413489.png)

可以看到，我首先获取了该service的IP地址，然后连续访问了三次该IP地址对应的80端口，得到了三个不同的hostname，证明实现了负载均衡以及代理Pod的功能。

## ClusterIP模式的功能

ClusterIP 模式的 Service 为你提供的，就是一个 Pod 的稳定的 IP 地址，即 VIP。并且，这里 Pod 和 Service 的关系是可以通过 Label 确定的。













