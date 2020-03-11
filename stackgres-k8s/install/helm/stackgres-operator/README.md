# Helm Chart to create the StackGres Operator

## Requirements

* The Kubernetes cluster to install the operator
* [Helm](https://helm.sh/docs/using_helm/#installing-helm) version >= `3.1.1`

## Create the StackGres Operator

Run the next command (make sure you are in the root path of the [repository](https://gitlab.com/ongresinc/stackgres)).

`helm install --namespace stackgres stackgres-operator operator/install/kubernetes/chart/stackgres-operator/`

## Configurable parameters

See https://stackgres.io/doc/latest/03-production-installation/02-installation-via-helm/

## List the chart deployment

`helm list` or `helm ls --all`

## Delete the StackGres Operator

`helm delete --purge stackgres-operator`

## Integrate grafana in the StackGres Operator UI

To integrate grafana in the UI follow those steps:

1. Create grafana dashboard for postgres exporter and copy/paste share URL:

Grafana > Create > Import > Grafana.com Dashboard 9628

2. Copy/paste grafana dashboard URL for postgres exporter:

Grafana > Dashboard > Manage > Select postgres exporter dashboard > Copy URL

```
helm install ... --set-string grafana.url="http://localhost:3000/d/000000039/postgresql-database?orgId=1&refresh=10s"
```

3. Create and copy/paste grafana API token:

Grafana > Configuration > API Keys > Add API key (for viewer) > Copy key value

```
helm install ... --set-string grafana.token="eyJrIjoidXc4NXJPa1VOdmNHVkFYMGJuME9zcnJucnBYRU1FQTMiLCJuIjoic3RhY2tncmVzIiwiaWQiOjF9"
```

4. Copy and paste grafana service hostname:

```
helm install ... --set-string grafana.webHost="$(kubectl get service prometheus-operator-grafana --template $'{{ .metadata.name }}.{{ .metadata.namespace }}.svc\n')"
```

5. Set the HTTP scheme used by grafana (optional):


```
helm install ... --set-string grafana.schema="http"
```
