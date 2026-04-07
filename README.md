# CampusConnect 🎓

Système de gestion académique pour l'**École Nationale Supérieure Polytechnique de Douala (ENSPD)**.  
Projet POO – 3ème année

---

## Description

CampusConnect permet de gérer :
- Les **acteurs** : étudiants et enseignants
- L'**offre de formation** : cours, groupes (CM/TD/TP), salles
- Les **inscriptions** et **évaluations** (notes, moyennes)
- Le **planning des séances** avec détection automatique des conflits

## Technologies

| Élément | Choix |
|---|---|
| Langage | Java (POO classique) |
| Interface | Terminal / Console (Scanner) |
| Persistance | Mémoire vive (ArrayList) |
| Framework | Aucun – Java pur |

## Structure du projet

```
src/main/java/com/enspd/campusconnect/
├── model/          → Classes entités (Personne, Etudiant, Cours…)
├── service/        → Logique métier (inscriptions, notes, planning)
├── ui/             → Menus terminal (à venir – Phase 4)
├── util/           → DataStore (stockage en mémoire)
└── Main.java       → Point d'entrée
```

## Roadmap

| Phase | Contenu | Statut |
|---|---|---|
| Phase 1 | Modèles POO (toutes les classes) | ✅ Fait |
| Phase 2 | Services métier (logique, validations) | 🔄 En cours |
| Phase 3 | DataStore & stockage mémoire | ✅ Fait |
| Phase 4 | Interface terminal (menus Scanner) | ⏳ À faire |
| Phase 5 | Tests & export CSV | ⏳ À faire |

## Lancer le projet

```bash
# Compiler
javac -d out src/main/java/com/enspd/campusconnect/**/*.java src/main/java/com/enspd/campusconnect/Main.java

# Exécuter
java -cp out com.enspd.campusconnect.Main
```

## Équipe

| Membre | GitHub |
|---|---|
| Med Amin | [@medamin6981-eng](https://github.com/medamin6981-eng) |
| ... | ... |

---
> Projet académique – ENSPD – Tous droits réservés
