# CourseCart - Project Context & Starter Guide

## 1. Project Identity
**Project Name:** CourseCart

## 2. Project Vision
CourseCart is a production-style online course marketplace and learning platform designed to provide a comprehensive, educational, and realistic capstone project experience.

## 3. Business Domain
The core domain focuses on an end-to-end course marketplace and learning workflow. The system facilitates interactions between users seeking educational content and platform administrators managing the catalog.

## 4. Core Project Objective
To build an original, functional course platform that supports user registration/login, course discovery and browsing, enrollment/purchasing, learning progress tracking, and administrative catalog management. The project serves as an intern/trainee-friendly capstone, demonstrating clear frontend/backend separation and clean RESTful microservice architecture.

## 5. Complexity Target
CourseCart must achieve a complexity level comparable to the HomeServe capstone project. 
Target characteristics include:
- Production-style architecture with clear separation of concerns.
- Angular-based frontend and Java Spring Boot backend.
- Relational data model using MySQL.
- Meaningful validation, robust error handling, and logical REST APIs.
- Clean documentation and traceability.
- Avoidance of unnecessary enterprise infrastructure (e.g., Kubernetes, Kafka, Redis, Docker orchestration, JWT/Spring Security) unless genuinely necessary and explicitly selected later.

## 6. Relationship to Reference HomeServe
The `Reference HomeServe` project serves **ONLY** as a read-only benchmark for determining approximate difficulty, architectural depth, coding complexity, and documentation standard. CourseCart must be built at the same overall level of quality but must remain functionally distinct.

## 7. Strict Originality Rule
CourseCart must be an **ORIGINAL** project. It must feature its own domain model, business workflows, user stories, microservices, APIs, database design, and business rules. Do NOT simply rename HomeServe concepts or blindly copy its data structures and API patterns.

## 8. Technology Stack
The project will use the following technology stack:
**Backend:**
- Java
- Spring Boot
- Maven
- Spring Data JPA
- REST APIs

**Frontend:**
- Angular
- TypeScript
- HTML
- CSS/SCSS
- Angular Routing
- Angular Guards (AuthGuard, AdminGuard)

**Database:**
- MySQL

## 9. Exact Technology Versions (Discovered from Reference HomeServe)
To maintain environment consistency and intended difficulty, the following confirmed versions discovered in the Reference HomeServe project will be used:
- **Java:** 21
- **Spring Boot:** 3.2.5
- **Angular:** ^16.2.0 (16.2.x)
- **TypeScript:** ~5.1.3
- **Node:** >=18.10.0 <19
- **MySQL:** 8.x (with InnoDB engine)
*(Note: These are CONFIRMED facts from inspection).*

## 10. Frontend Architecture Direction
- **Identity & Session:** Frontend-managed identity/session state utilizing browser `localStorage`.
- **Security:** Use Angular `AuthGuard` and `AdminGuard` for UI routing protection.
*(Status: PLANNED, subject to further architectural definition).*

## 11. Backend Architecture Direction
- **Communication:** Clean RESTful communication between services where needed.
- **Data Persistence:** Domain-driven responsibility over MySQL database tables.
- **Security Constraint:** Do NOT automatically implement robust backend authorization middleware, AuthInterceptor, or `X-User-*` headers unless explicitly deliberate.

## 12. Four-Microservice Requirement
CourseCart must use exactly **FOUR** backend microservices unless a future architectural analysis proves a compelling reason otherwise.

## 13. Preliminary Microservice Domain Direction
The following domains represent the preliminary architectural separation:
1. **User / Identity Service:** Manages user profiles, authentication state, and platform access.
2. **Course Catalog Service:** Manages course details, categories, search, and browsing functionality.
3. **Enrollment / Learning Service:** Manages user enrollments, progress tracking, and access to learning content/modules.
4. **Commerce / Payment Service:** Manages the shopping cart, checkout process, and simulated payment state.
*(Status: PLANNED, exact boundaries to be finalized during the planning workflow).*

## 14. Two-Role Model
The system must use exactly **TWO** application authentication roles: `USER` and `ADMIN`. Do not introduce additional authentication roles (like instructor, moderator, etc.) unless they are represented through application data logic rather than core authentication roles.

## 15. USER Responsibilities
- Register and log in.
- Browse and search courses.
- View detailed course information.
- Purchase or enroll in courses.
- Access enrolled learning content and track personal learning activity.
- Manage relevant personal information.

## 16. ADMIN Responsibilities
- Manage platform-controlled operations.
- Create, update, and manage courses and related catalog data.
- View and manage relevant platform records.
- Perform administrative workflows defined in the final specification.

## 17. Authentication / Session Model
The project adopts a simplified, educational security model. Authentication and session state will be managed primarily on the frontend using `localStorage`. The backend will validate ownership and state where necessary for business logic, but will avoid overly complex security frameworks.

## 18. Security Constraints and Non-Goals
Do **NOT** automatically claim or implement the following enterprise security models unless explicitly approved in future requirements:
- JWT (JSON Web Tokens)
- Spring Security
- 
- OAuth
- Backend role-based authorization middleware
- AuthInterceptor
- `X-User-*` headers

*Documentation must always describe the ACTUAL implementation. Never describe frontend guards as equivalent to robust backend authorization.*

## 19. Payment Model
- **Approach:** UI/application-level payment demonstration.
- **Methods:** Example mock methods (e.g., UPI, CARD, COD).
- **Constraint:** No real external payment gateway integration (e.g., Stripe, PayPal, Razorpay) unless explicitly approved later. Payment state must reflect actual mock project logic. Documentation must clearly state when payment is simulated/demo-only. Do not falsely describe a mock flow as real processing.

## 20. Database Direction
- **RDBMS:** MySQL is mandatory (Do not claim H2 unless explicitly generated later).
- **Design:** Clear relational schema with appropriate primary and foreign keys.
- **Initialization:** Include `schema.sql` and `data.sql` strategies if consistent with the chosen architecture.
- **Ownership:** Domain ownership must be clearly documented.

## 21. User Story Planning Philosophy
There is NO fixed requirement for exactly 10 user stories. The final number will be determined based on:
- Complete business coverage.
- Comparable project depth to HomeServe.
- Manageable implementation scope and clear microservice distribution.
Each user story must include: Story ID, Priority, Description, Acceptance Criteria, Relevant Role, Relevant Microservice(s), and Dependencies.

## 22. Documentation-First Workflow
CourseCart follows a strict documentation-first workflow. Implementation code is not to be generated until the planning and specification documents are complete and approved.

## 23. Required Future Document Sequence
After this `starter.md` file, documents must be created **ONE BY ONE** in separate tasks:
1. `raw-requirement.md`
2. `clarification.md`
3. `constitution.md`
4. `spec.md`
5. `plan.md`
6. `data-model.md`
7. `api-contract.md`
8. `tasks.md`
9. `test.md`

## 24. `memory.md` Maintenance Rule
The project must maintain a persistent operational context file at `J:\Capstone\CourseCart\memory.md`.
After EVERY major agent task or planning session, this file MUST be updated with:
- What was completed.
- Important decisions made.
- Confirmed architecture facts and technology decisions.
- Constraints and user story decisions.
- Rejected alternatives and common mistakes to avoid.
- Unresolved questions.
*Rule: Do not invent future decisions.*

## 25. Major Task Reporting Rule
After every major task, the agent must create or update a clear audit/report summarizing:
- Task objective
- Files inspected, created, or modified
- Key decisions and validation performed
- Important findings and remaining issues
- Final status
*(Note: Factual reporting only. Do not claim build success or API correctness without verification).*

## 26. Source-of-Truth Hierarchy
When inconsistencies arise, resolve them according to the following authority hierarchy (1 being the highest):
1. **Actual generated CourseCart code and configuration** (once implementation exists).
2. **Approved CourseCart specification/constitution.**
3. **Approved architecture and API/data contracts.**
4. **`starter.md` and `memory.md`** (for persistent operational context).
5. **Reference HomeServe** (for complexity/technical reference only).
6. **Assumptions** (lowest authority, must be validated).

## 27. Reference Inspection Rules
The `Reference HomeServe` directory is **READ-ONLY**. It must never be modified, and its concepts must not be blindly copied.

## 28. Anti-Hallucination Rules
- Distinguish strictly between: Confirmed facts (from inspected files), Planned decisions, Assumptions requiring clarification, and Future implementation decisions.
- **NEVER** present assumptions as confirmed facts.
- **NEVER** copy documentation claims from the reference project without verifying applicability to CourseCart.

## 29. Code Generation Rules for Future Phases
- Prioritize clean architecture, readable code, and realistic business workflows.
- Ensure consistent documentation and frontend/backend traceability.
- Maintain appropriate validation and proper error handling.

## 30. Documentation Accuracy Rules
Documentation must accurately reflect the state of the system. Do not invent idealized features (like enterprise security) if they are not implemented in code.

## 31. Validation Philosophy
Validation must be factual. Never claim a test passes or a feature works without empirical verification (e.g., checking code, contracts, configuration, or running the build).

## 32. Common Mistakes to Avoid
- Blindly copying domain concepts, APIs, or database models from HomeServe.
- Introducing unnecessary enterprise complexity (e.g., Kubernetes, JWT).
- Falsely claiming enterprise features or real payment processing in documentation.
- Generating code before completing the documentation sequence.

## 33. Explicit "Do Not Do Yet"
At this stage in the workflow:
- **Do NOT** generate any application code.
- **Do NOT** create backend or frontend projects.
- **Do NOT** create any of the subsequent planning documents (`raw-requirement.md`, `spec.md`, etc.) yet.
- **Do NOT** modify `Reference HomeServe`.

## 34. Initial Project Folder Expectations
Currently, the `CourseCart` directory should only contain foundational context files, specifically `starter.md` and `memory.md`.

## 35. Definition of Success for the Planning Phase
The planning phase will be considered successful when the entire documentation sequence (from `raw-requirement.md` to `test.md`) has been generated systematically, resulting in a cohesive, original, and unambiguous specification ready for implementation.
