# Lights Out Game (Backend)

## Project Description

Quarkus backend service for the **Lights Out** puzzle game.
It provides REST APIs for creating, storing, and solving Lights Out problems. The backend handles problem validation and solution computation.

---

## 🔧 Tech Stack

| Layer      | Tech                              |
| ---------- | --------------------------------- |
| Framework  | [Quarkus](https://quarkus.io/)    |
| Database   | H2 (in-memory)     |
| Build Tool | [Maven](https://maven.apache.org/) |

---

## ⚙️ Setup & Installation

1. **Clone the repo**

   ```bash
   git clone https://github.com/TimotejSustersic/lights-out-be.git
   ```

2. **Run in dev mode / Run project in IntelliJ**

   ```bash
   ./mvnw compile quarkus:dev
   ```

   The backend will be available at: [http://localhost:8080](http://localhost:8080)

---

## 📂 Project Structure

```
src/
 ├── main/java/com/lightsout/
 │   ├── model/                # Entities (Problem, Solution, SolutionStep)
 │   ├── resource/             # REST endpoints
 │   └── service/              # Solvers (BFS, Linear Algebra)
```

---

## 🚀 Features & Functionality

* **BFSSolver:**
  A simple breadth-first search algorithm with state caching.
  Intended for testing while the frontend was under development.
  Works well for small grids (e.g., 3x3), struggles on larger boards.


* **LinearAlgebraSolver:**
  The main production solver.
  Uses Gaussian elimination over GF(2) to compute solutions in milliseconds, regardless of grid size.
  The solver generates all possible grid moves, constructs a matrix representation, and reduces it to solve the system.
  The approach was inspired by [this video](https://www.youtube.com/watch?v=1izbpSk3ays).


* **API Endpoints**

  | Endpoint                 | Method | Description                               |
    | ------------------------ | ------ |-------------------------------------------|
  | `/problems`              | GET    | List all stored problems.                 |
  | `/problems/{id}`         | GET    | Fetch a specific problem by ID.           |
  | `/problems`              | POST   | Create a new problem & check solvability. |
  | `/solutions`             | GET    | List all stored solutions.                |
  | `/solutions/{problemId}` | GET    | Fetch solution for a given problem.       |


* **Swagger Docs** available at: [http://localhost:8080/q/swagger-ui/](http://localhost:8080/q/swagger-ui/)

---

## 📚 References

* [Gaussian Elimination for Lights Out](https://www.youtube.com/watch?v=1izbpSk3ays)

---

## 👨‍💻 Author

**Timotej Šušteršič**

* [GitHub](https://github.com/TimotejSustersic/)

---

## 📄 License

This project is licensed under the MIT License. See the `LICENSE` file for details.
