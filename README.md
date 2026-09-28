# Student Management System

A terminal-based Java application for managing students, courses, enrollments,
and marks. Built as a core-Java course project demonstrating OOP, the
Collections Framework, and JDBC with full CRUD operations backed by a
SQLite database.

## Features Implemented

- Add, view, update, and delete student records (name, ID, contact info).
- Add courses and view the course list.
- Enroll and unenroll students in courses.
- Search students by ID or by (partial) name.
- List all students enrolled in a given course.
- Record and update marks for a student in a course.
- Compute each student's average and generate a class ranking report,
  sorted from highest to lowest average.
- Menu-driven console interface with input validation (rejects non-numeric
  input and empty text where a value is required).
- Data is stored in a SQLite database file, so it persists between runs.

## Technologies / Libraries Used

- Java 11 (core Java only — no frameworks).
- JDBC with the `sqlite-jdbc` driver (org.xerial:sqlite-jdbc, version 3.46.1.3).
- SQLite as the database engine (single file, no server or credentials needed).
- Maven for dependency management and builds.

## Project Structure

```
student-management-system/
├── pom.xml
├── schema.sql
├── README.md
├── .gitignore
└── src/main/java/com/sms/
    ├── Main.java                     (menu-driven console entry point)
    ├── model/
    │   ├── Reportable.java           (interface — polymorphism)
    │   ├── Student.java
    │   ├── Course.java
    │   └── Enrollment.java
    ├── exception/
    │   └── StudentNotFoundException.java   (custom checked exception)
    ├── dao/
    │   ├── DBConnection.java         (connection + schema setup)
    │   ├── StudentDAO.java           (CRUD for students)
    │   ├── CourseDAO.java            (CRUD for courses)
    │   └── EnrollmentDAO.java        (enroll/unenroll/marks)
    └── service/
        └── StudentService.java       (business logic, collections, ranking)
```

## Database Setup

No manual setup is required. The app uses SQLite, which stores the whole
database in a single local file (`student_management.db`), and it creates
the required tables automatically the first time it runs.

If you want to inspect or create the schema manually, the script is in
`schema.sql`:

```sql
CREATE TABLE IF NOT EXISTS students (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    contact TEXT
);

CREATE TABLE IF NOT EXISTS courses (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS enrollments (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id INTEGER NOT NULL,
    course_id INTEGER NOT NULL,
    marks REAL DEFAULT 0,
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (course_id) REFERENCES courses(id)
);
```

Since SQLite does not use a username/password, there are no database
credentials to keep out of source control for this project.

## Setup and Run Instructions

### Option A — IntelliJ IDEA (recommended)

1. Open IntelliJ IDEA → **File → Open** → select the `student-management-system` folder.
2. IntelliJ will detect the `pom.xml` and prompt to load it as a Maven project — click **Load**. It will download the `sqlite-jdbc` dependency automatically.
3. Open `src/main/java/com/sms/Main.java`.
4. Click the green **Run** arrow next to `public static void main`.
5. Use the console at the bottom of the screen to interact with the menu.

### Option B — Command line (Maven)

```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.sms.Main"
```

### Option C — Command line (plain javac/java)

```bash
# Compile (adjust the path to wherever your sqlite-jdbc jar is)
javac -cp sqlite-jdbc-3.46.1.3.jar -d out $(find src -name "*.java")

# Run
java -cp "out:sqlite-jdbc-3.46.1.3.jar" com.sms.Main
```

The database file `student_management.db` is created automatically in the
project's working directory on first run.

## Screenshots

_Add 2–3 screenshots of the terminal application running here before
submission, e.g.:_

- Main menu
- Adding a student / course
- Class ranking report output

## Known Limitations

- No login or role-based access (single-user, no authentication).
- No stretch goals implemented (attendance tracking, exports, admin view)
  — only the core required features are included.
- Minimal validation on IDs (e.g. enrolling a non-existent course ID is
  not explicitly checked before insertion).
