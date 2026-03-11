# Trivia Master

<img width="761" height="87" alt="afbeelding" src="https://github.com/user-attachments/assets/a6039d2e-b219-4390-8aef-0a14d9e75f42" />

Trivia Master is a simple but fun quiz game where you can test your knowledge across multiple categories.

<br>

---

## Table of Contents 

- [Trivia Master](#trivia-master)
  * [Live Demo](#live-demo)
  * [Features](#features)
  * [Tech Stack](#tech-stack)
  * [API Endpoints](#api-endpoints)
  * [Quick Start](#quick-start)
  * [Testing](#testing)
  * [Contributing](#contributing)
  * [Screenshots](#screenshots)
  * [License](#license)

<br>

---

## Live Demo
A deployed instance can be viewed at [goldfishproductions.nl](https://goldfishproductions.nl).

<br>

---

## Features

- **Custom Trivia Quizzes** – Generate quizzes based on selected categories, difficulty, question type and number of questions.
- **Frontend** – Responsive UI built with TypeScript, React and Vite.
- **Backend** – Java + Spring Boot API responsible for fetching questions and validating answers.

<br>

---

## Tech Stack

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=springboot&logoColor=white)
![React](https://img.shields.io/badge/React-20232A?logo=react&logoColor=61DAFB)
![Vite](https://img.shields.io/badge/Vite-646CFF?logo=vite&logoColor=white)
![TypeScript](https://img.shields.io/badge/TypeScript-007ACC?logo=typescript&logoColor=white)

### Frontend
- React
- TypeScript
- Vite

### Backend
- Java
- Spring Boot
- Maven

### Infrastructure
- GitHub (Version Control)
- Koyeb (Backend Hosting)

<br>

---

## API Endpoints

Base URL:

`/api`

### Get Trivia Questions

Retrieve a list of trivia questions based on selected filters.

```http
GET /api/questions
```

#### Query parameters

| Parameter | Description |
|-----------|-------------|
| amount | Number of questions |
| category | Trivia category (integer value) |
| difficulty | easy / medium / hard |
| type | multiple / boolean |

#### Response

```json
[
   {
      "id": 1,
      "question": "The Earth is flat.",
      "category": "Science",
      "difficulty": "easy",
      "type": "boolean",
      "answers": ["True", "False"]
   }
]
```

#### Example

```http
GET /api/questions?amount=10&difficulty=easy
```
---

### Validate Answers

Validate answers submitted by the player.

```http
POST /api/answers/validate
```

#### Request body

```json
[
  {
    "questionId": 1,
    "answer": "True"
  }
]
```

#### Response

```json
[
  {
    "question": {
      "id": 1,
      "question": "The Earth is flat.",
      "category": "Science",
      "difficulty": "easy",
      "type": "boolean",
      "answers": ["True", "False"]
    },
    "submittedAnswer": "True",
    "correctAnswer": "False",
    "result": false
  }
]
```

#### Example

```http
GET /api/questions?amount=10&difficulty=easy
```

<br>

---

## Quick Start

### Prerequisites

- **Node.js** (v18 or higher)
- **Java JDK** (17 or higher)
- **Maven**
- **Git**

---

### Installation

#### 1. Clone the repository

```bash
git clone https://github.com/goldfishprojects/trivia-master.git
cd trivia-master
```

#### 2. Start the backend (Spring Boot)
```bash
cd api
mvn spring-boot:run
```

The backend will start on:
`http://localhost:9090`

#### 3. Configure frontend environment

Copy the example environment file:
```bash
cp front-end/.env.example front-end/.env
```

`.env.example`:

```env
VITE_API_BASE_URL=http://localhost:9090/api
```

#### 4. Start the frontend (React + Vite)

```bash
cd front-end
npm install
npm run dev
```

The frontend will run on:
`http://localhost:5173`

<br>

---

## Testing

The backend is covered with unit tests using **JUnit** and **Mockito** to verify the quiz logic and answer validation.

Current test coverage: **88%**

Run the tests with:

```bash
cd api
mvn test
```

<br>

---

## Contributing

Contributions are welcome. If you would like to improve the project, feel free to open an issue or submit a pull request.

### How to contribute

1. Fork the repository
2. Create a new branch

```bash
git checkout -b feature/my-feature
```

3. Make your changes and commit them

```bash
git commit -m "Add new feature"
```

4. Push your branch

```bash
git push origin feature/my-feature
```

5. Open a Pull Request

### Development Guidelines

- Follow the existing project structure
- Write clear commit messages
- Add tests when introducing new functionality

<br>

---

## Screenshots

### Quiz Configuration

Users can select the category, difficulty, type and number of questions.

<img width="1112" height="742" alt="afbeelding" src="https://github.com/user-attachments/assets/34424666-2b1a-429b-8602-4bbff3ce5326" />

### Playing the Quiz

Questions are displayed one by one.

<img width="1112" height="742" alt="afbeelding" src="https://github.com/user-attachments/assets/8de32c37-90bf-45f3-be5d-474d7300d475" />

### Results

After completing the quiz, the results screen shows the correct answers and score.

<img width="1112" height="742" alt="afbeelding" src="https://github.com/user-attachments/assets/8e2dfaad-0686-4028-a558-3ee049ab23c5" />

<br>

---

## License

This project is licensed under the MIT License.
