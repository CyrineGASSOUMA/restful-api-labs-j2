# Formation RESTful API — Labs du jour 2

| Dossier | Contenu |
|---|---|
| `lab4/exercice1/` | Contrat à compléter + `generer.sh` : API générée, endpoints en **501** |
| `lab4/exercice2/` | Contrat complet + `generer.sh` : **mock généré**, endpoints en **201/200** avec les exemples du contrat |
| `lab7-fichiers/` | Fichiers de sécurité à copier au lab 7 |
| `correction/` | Une correction par lab et par exercice |

Les labs 5, 6 et 7 se font dans le projet de l'exercice 1 : `lab4/exercice1/banque-api`.

## Lab 4
```
sh lab4/exercice1/generer.sh
cd lab4/exercice1/banque-api && mvn spring-boot:run      # Swagger : 501

sh lab4/exercice2/generer.sh
cd lab4/exercice2/banque-api && mvn spring-boot:run      # Swagger : 201 + exemple
```
Swagger UI : onglet **Ports**, port 8080, icône globe. Un seul projet à la fois : `Ctrl+C` avant d'en lancer un autre.

## Corrections
| Dossier | Contenu |
|---|---|
| `correction/lab4/exercice1/openapi.yaml` | Contrat complété |
| `correction/lab4/exercice2/banque-api` | Mock généré |
| `correction/lab4/exercice3/banque-api` | Logique implémentée (`VirementsDelegate`) |
| `correction/lab5` à `lab7` | Projet complet en fin de lab |

Dans les corrections, le code est généré à chaque `mvn compile` (plugin Maven, même générateur, mêmes options).

## Vérification automatique
Le pipeline (`.gitlab-ci.yml` ou `.github/workflows/verifier.yml`) compile et démarre les corrections, et vérifie que les deux générations du lab 4 produisent un projet qui compile.
