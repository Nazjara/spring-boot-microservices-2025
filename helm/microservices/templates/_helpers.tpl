{{/*
Create a default fully qualified app name.
*/}}
{{- define "microservices.fullname" -}}
{{- .Release.Name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Common labels
*/}}
{{- define "microservices.labels" -}}
helm.sh/chart: {{ .Chart.Name }}-{{ .Chart.Version | replace "+" "_" }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end -}}

{{/*
Create a service name
*/}}
{{- define "microservices.serviceName" -}}
{{- .name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create image reference
*/}}
{{- define "microservices.image" -}}
{{- printf "%s:%s" .repository .tag -}}
{{- end -}}