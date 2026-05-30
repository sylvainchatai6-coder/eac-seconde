📚 EAC Seconde – Citoyenneté et Droits
Application web éducative interactive destinée aux élèves de Seconde.
Objectif : réviser l’Éducation à la Citoyenneté (EAC) de manière moderne, interactive et multilingue.

🎯 Objectifs pédagogiques
Réviser les notions essentielles de l’EAC.

Comprendre les droits, devoirs et responsabilités du citoyen.

S’entraîner avec des quiz et des flashcards.

Favoriser l’apprentissage multilingue (Français, Anglais, Malagasy).

⚙️ Fonctionnalités principales
7 leçons complètes couvrant le programme officiel.

Multilingue : Français, Anglais, Malagasy.

Quiz interactifs avec feedback immédiat et score.

Flashcards pour mémoriser les mots-clés (30 cartes).

Barre de recherche pour accéder rapidement aux contenus.

API REST documentée pour l’intégration et l’extension.

🛠️ Technologies utilisées
Frontend : React 18, Tailwind CSS, Vite

Backend : Python FastAPI

📥 Installation
Prérequis
Node.js (v18+)

Python (v3.10+)

Étapes
bash
# Cloner le dépôt
git clone https://github.com/sylvainchatai6-coder/eac-seconde.git
cd eac-seconde

# Lancer le backend
cd backend
pip install -r requirements.txt
uvicorn main:app --reload

# Lancer le frontend
cd frontend
npm install
npm run dev
📂 Structure du projet
Code
eac-seconde/
├── backend/
├── frontend/
└── README.md
🌐 API Endpoints
GET /api/lecons/{lang} → liste des leçons

GET /api/lecons/{lang}/{id} → contenu d’une leçon

GET /api/quiz/{lang}/{lesson_id} → quiz par leçon

GET /api/flashcards/{lang} → flashcards par langue

GET /api/recherche?q=mot&lang=fr → recherche par mot-clé

Langues supportées : fr, en, mg

📖 Contenu pédagogique
Leçons
Introduction à la citoyenneté

Droits, devoirs et responsabilités

Déclaration universelle des droits de l’homme (DUDH)

La corruption

Genre, discrimination et violence

Le développement durable

Solidarité et participation citoyenne

Flashcards (30 cartes)
Citoyenneté, Nationalité, Droits, Devoirs, Responsabilité, DUDH, ONU, 1948, Présomption d’innocence, Corruption, Népotisme, Favoritisme, Discrimination, Genre, Violence physique, Violence psychologique, Développement durable, Pilier environnemental, Solidarité, Participation citoyenne, Pot-de-vin, Extorsion, Détournement de fonds, Égalité, Liberté, Loi, Impôt, Éducation, Environnement, Bénévolat.

📜 Licence
MIT

👤 Auteur
Sylvain SOLOFONIAINA

✅ Conclusion
Cette application est conçue pour offrir aux élèves de Seconde un outil moderne, interactif et multilingue afin de réviser efficacement l’EAC et de préparer leurs examens.
