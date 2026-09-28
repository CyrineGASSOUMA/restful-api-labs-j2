#!/bin/sh
# Exercice 2 : génère un mock Spring Boot depuis openapi.yaml (réponses 2xx avec les exemples du contrat)
# À lancer depuis n'importe où : le script se place dans son propre dossier.
cd "$(dirname "$0")"
java -jar ../../outils/openapi-generator-cli.jar generate -i openapi.yaml -g spring -o banque-api \
  --additional-properties=useSpringBoot3=true,delegatePattern=true,useTags=true,openApiNullable=false,basePackage=fr.formation.banque,apiPackage=fr.formation.banque.api,modelPackage=fr.formation.banque.api.model,configPackage=fr.formation.banque.config,groupId=fr.formation,artifactId=banque-api-mock,returnSuccessCode=true

# URL correcte dans Swagger derrière le proxy Codespaces (sans effet en local)
PROPS=banque-api/src/main/resources/application.properties
grep -q "forward-headers-strategy" "$PROPS" || echo "server.forward-headers-strategy=framework" >> "$PROPS"
echo "Projet généré dans $(pwd)/banque-api"
