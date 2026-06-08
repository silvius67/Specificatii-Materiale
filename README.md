# Subcontrol Decizii — Aplicație Android

Arbore decizional pentru verificarea specificației tehnice a produselor de construcții.
Conf. HG 668/2017 · RPC (UE) 305/2011 / 2024/3110 · HG 766/1997 · Legea 10/1995

---

## Structura proiectului

```
SubcontrolApp/
├── app/src/main/
│   ├── assets/
│   │   └── decision_tree.json          ← ARBORELE DECIZIONAL (editabil fără cod)
│   ├── java/ro/subcontrol/decizii/
│   │   ├── model/
│   │   │   └── DecisionTree.kt         ← Modele de date (NodeType, Severity, etc.)
│   │   ├── viewmodel/
│   │   │   └── DecisionViewModel.kt    ← Logica de navigare + stivă înapoi
│   │   ├── ui/
│   │   │   ├── theme/
│   │   │   │   ├── Color.kt            ← Paleta de culori
│   │   │   │   └── Theme.kt            ← Tema Material3
│   │   │   ├── Components.kt           ← Componente reutilizabile
│   │   │   └── DecisionScreen.kt       ← Ecranul principal (UI)
│   │   └── MainActivity.kt             ← Entry point
│   ├── res/values/themes.xml
│   └── AndroidManifest.xml
├── gradle/libs.versions.toml           ← Versiuni dependențe
├── app/build.gradle.kts
├── build.gradle.kts
└── settings.gradle.kts
```

---

## Instalare în Android Studio

1. Deschide **Android Studio** (Hedgehog 2023.1 sau mai recent)
2. **File → Open** → selectează folderul `SubcontrolApp`
3. Așteaptă sincronizarea Gradle (câteva minute la prima rulare)
4. Conectează un telefon Android (API 26+) sau pornește un emulator
5. Apasă **Run ▶**

### Cerințe minime
- Android Studio Hedgehog 2023.1+
- JDK 11+
- Android SDK 35
- Telefon/emulator cu Android 8.0+ (API 26)

---

## Cum modific arborele decizional

Deschide `app/src/main/assets/decision_tree.json`.

Structura unui nod de întrebare:
```json
"id_nod": {
  "type": "question",
  "step": "Pasul X",
  "text": "Întrebarea afișată?",
  "ref": "Referința legală",
  "options": [
    { "label": "DA", "next": "id_nod_urmator_da" },
    { "label": "NU", "next": "id_nod_urmator_nu" }
  ]
}
```

Structura unui nod de rezultat:
```json
"id_rezultat": {
  "type": "result",
  "severity": "success",
  "step": "Concluzie",
  "title": "Titlul concluziei",
  "text": "Descriere detaliată...",
  "ref": "Temei legal",
  "action": "Ce trebuie să faci...",
  "options": []
}
```

Valori posibile pentru `severity`: `success` (verde), `warning` (galben), `danger` (roșu)
Valori posibile pentru `type`: `question`, `choice`, `result`

**Nu este nevoie de recompilare** dacă modifici doar JSON-ul, atâta timp cât fișierul rămâne în `assets/`.

---

## Dependențe principale

| Librărie | Scop |
|---|---|
| Jetpack Compose + Material3 | UI declarativ modern |
| Lifecycle ViewModel + StateFlow | Gestionarea stării |
| Gson | Parsarea JSON-ului |
| Activity Compose | Integrare Compose în Activity |

---

## Actualizări legislative viitoare

Când RPC (UE) 2024/3110 devine integral aplicabil (8 ian. 2026), actualizează
referințele din `decision_tree.json` fără a modifica niciun fișier Kotlin.
