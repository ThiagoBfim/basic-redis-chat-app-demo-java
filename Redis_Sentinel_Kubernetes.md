# Redis Sentinel on Kubernetes

Esse documento faz referência ao uso da arquitetura Redis Sentinel com Master Slave com Kubernetes.

##  Master-Slave com Sentinel

Essa arquitetura garante uma alta disponibilidade, boa consistnência dos dados, e baixa complexidade na implementação do client.

Utilizando essa arquitetura com Master-Slave, será criado o master que é responsável pela escrita, e os demais apenas para leitura.

Além do Master e dos Slaves, também  é criado uma instância do Sentinel para cada instância do Redis.

O Sentinel é responsável para identificar quem é o master, e caso haja algum problema com o master, ele elegerá um novo master.

<img src='https://rtfm.co.ua/wp-content/uploads/2019/03/screen-shot-2017-08-11-at-14-34-42.png' width='300px' height='420px'></img>


### Configuração da aplicação

A aplicação precisa conter as configurações para se conectar com os Sentinels.

[Exemplo da configuração com Spring](https://github.com/spring-projects/spring-data-examples/blob/main/redis/sentinel/src/main/java/example/springdata/redis/sentinel/RedisSentinelApplication.java)


### Integração da aplicação com Redis.

O Sentinel será responsável por descobrir quem é o master e retornar para aplicação essa informação.

A aplicação fará a conexão com o master para realizar a escrita.

Para maiores informações: https://redis.io/topics/sentinel-clients

#### Observação

* Caso o master fique indisponível, o Sentinel elegerá um novo master.
* Caso o Sentinel caia, a aplicação fará a comunicação com outro Sentinel.

## Importante

Para integração com Kubernetes, foi utilizado o helm do bitnami:
https://github.com/bitnami/charts/tree/master/bitnami/redis

Entretanto, a configuração para uso do Sentinel com Kubernetes ainda não está maduro no momento da escrita desse documento.

Foi identificado o seguinte problema na configuração:

A aplicação obtém do Sentinel o IP do Master, e esse IP não está acessível.  

Há um issue aberto para tratar dessa problemática: https://github.com/bitnami/charts/issues/4082


## Quick Guide

1. Instalar Radis with Kubernetes utilizando Helm
https://github.com/bitnami/charts/tree/master/bitnami/redis

```
helm repo add bitnami https://charts.bitnami.com/bitnami

helm install my-release \
  --set auth.password=secretpassword \
  --set sentinel.enabled=true \
    bitnami/redis
```

2. Subir a aplicação

```
git clone https://github.com/ThiagoBfim/basic-redis-chat-app-demo-java
cd basic-redis-chat-app-demo-java
docker build -t chat-app .
kubectl apply -f app-kubernetes.yaml
```

3. Acessar a aplicação

Acessar localhost:30001

4. Bonus - Testar Failover

Siga os passos: `helm status my-release`

    1. $ kubectl exec --tty -i redis-client    --namespace default -- bash
    2. $ redis-cli -h my-release-redis -p 26379 -a $REDIS_PASSWORD
    3. $ SENTINEL get-master-addr-by-name mymaster
    4. $ SENTINEL failover mymaster
    5. $ SENTINEL get-master-addr-by-name mymaster

Após realizar o failover você vai perceber que o IP do master muda.


## Importante

Após o Failover, o master ficará indisponível até um novo master ser liberado. Durante esse momento, a escrita deixa de funcionar, pois os Sentinels estarão estabelecendo um novo Master.

Pelos testes é um momento curto, e que pode ser acompanhado pelo log.


A aplicação do Quick Guide funciona pois está tudo dentro da mesma rede do Kubernetes.

Caso a aplicação não esteja na mesma rede que o Redis, será necessário configurar um NetworkPolicy para possibilitar o acesso da aplicação ao Redis Master, por meio do retorno do Sentinel pelo comando ` SENTINEL get-master-addr-by-name mymaster` .

Lembrando que isso é só uma POC, essas configurações não devem ser utilizadas em produção.

## Referência

* [Redis on Kubernetes](https://medium.com/swlh/production-checklist-for-redis-on-kubernetes-60173d5a5325)
* [Bitnami Redis Cluster - master-slave cluster with Redis Sentinel](https://engineering.bitnami.com/articles/deploy-and-scale-a-redis-cluster-on-kubernetes-with-bitnami-and-helm.html)
* [Redis Documentação](https://docs.redislabs.com/latest/platforms/kubernetes/getting-started/quick-start/)
* [Redis Cluster Specification](https://redis.io/topics/cluster-spec)
* [Redis Sentinel Documentation](https://redis.io/topics/sentinel)
* [Exemplo Projeto Java](https://developpaper.com/understanding-springboot-integration-in-redis-cluster-environment/)
