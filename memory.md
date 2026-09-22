# CourseCart - Project Memory

## What Was Completed
- Initial project baseline established (`starter.md`).
- Raw business requirements captured (`raw-requirement.md`).
- Requirements Clarification phase completed (`clarification.md`).
- Constitution phase completed (`constitution.md`).
- Specification phase completed (`spec.md`).
- Architecture Planning phase completed (`plan.md` & `plan_architecture_report.md`).
- Data Model Design phase completed (`data-model.md` & `data_model_audit_report.md`).
- API Contract phase completed (`api-contract.md`).
- Architecture Correction Audit completed (`api_contract_architecture_correction_audit.md`).
- Implementation Task Breakdown completed (`tasks.md` & `tasks_generation_audit.md`).
- Verification & Testing Strategy completed (`test.md` & `test_strategy_audit.md`).

## Important Decisions Made
- **Testing Approach:** Educational, structured verification aligning with a HomeServe-level capstone. Avoids over-engineered enterprise test frameworks (No load testing, Kafka testing, JWT testing).
- **Checkout Integrity Testing:** Mandatory test scenarios defined to guarantee duplicate purchases are blocked, and failed simulated payments produce zero DB persistence.
- **Lifecycle Integrity Testing:** Testing explicitly mandated to prove `INACTIVE` courses hide from public catalogs while guaranteeing permanent learning access for historical buyers.
- **Security Validation:** Testing focuses purely on `localStorage` state, Frontend Angular Guards, and parameterized backend identity validation.

## Constraints & Governance (Locked via Constitution)
- **Strict Originality:** Do not copy HomeServe domain concepts, logic, or data models.
- **Workflow:** Documentation-first workflow is now **COMPLETE**. No application code has been generated yet.
- **Anti-Drift:** Do not trust documentation blindly against future code. Maintain strict boundaries. Commerce depends on Catalog/Enrollment; no circular dependencies allowed.
- **Out of Scope Boundary Reinforced:** Explicit exclusion of Real payments, Shopping carts, Subscriptions, Video hosting, Instructor roles, JWT/OAuth, and Exams/Certificates.
# CourseCart - Project Memory

## What Was Completed
- Initial project baseline established (`starter.md`).
- Raw business requirements captured (`raw-requirement.md`).
- Requirements Clarification phase completed (`clarification.md`).
- Constitution phase completed (`constitution.md`).
- Specification phase completed (`spec.md`).
- Architecture Planning phase completed (`plan.md` & `plan_architecture_report.md`).
- Data Model Design phase completed (`data-model.md` & `data_model_audit_report.md`).
- API Contract phase completed (`api-contract.md`).
- Architecture Correction Audit completed (`api_contract_architecture_correction_audit.md`).
- Implementation Task Breakdown completed (`tasks.md` & `tasks_generation_audit.md`).
- Verification & Testing Strategy completed (`test.md` & `test_strategy_audit.md`).

## Important Decisions Made
- **Testing Approach:** Educational, structured verification aligning with a HomeServe-level capstone. Avoids over-engineered enterprise test frameworks (No load testing, Kafka testing, JWT testing).
- **Checkout Integrity Testing:** Mandatory test scenarios defined to guarantee duplicate purchases are blocked, and failed simulated payments produce zero DB persistence.
- **Lifecycle Integrity Testing:** Testing explicitly mandated to prove `INACTIVE` courses hide from public catalogs while guaranteeing permanent learning access for historical buyers.
- **Security Validation:** Testing focuses purely on `localStorage` state, Frontend Angular Guards, and parameterized backend identity validation.

## Constraints & Governance (Locked via Constitution)
- **Strict Originality:** Do not copy HomeServe domain concepts, logic, or data models.
- **Workflow:** Documentation-first workflow is now **COMPLETE**. No application code has been generated yet.
- **Anti-Drift:** Do not trust documentation blindly against future code. Maintain strict boundaries. Commerce depends on Catalog/Enrollment; no circular dependencies allowed.
- **Out of Scope Boundary Reinforced:** Explicit exclusion of Real payments, Shopping carts, Subscriptions, Video hosting, Instructor roles, JWT/OAuth, and Exams/Certificates.

## Facts Future Agent Sessions Must Preserve
- The detailed 14-Phase Implementation Breakdown in `tasks.md`.
- The Verification Framework in `test.md` covering all 15 User Stories.
- The 4-microservice requirement, exact ports (8081-8084), and allowed cross-service dependency graph.
- The 7-table schema ownership.
- Phase 1: Project foundation (Maven multi-module, directories).
- Phase 2: Database initialization (PostgreSQL schemas for all services).
- Phase 3: User Service (Registration, login APIs, mock authentication logic).
- Phase 3A: API Gateway & Consul (Service discovery, routing).
- Phase 4: Catalog Service (Courses, Categories, Lessons, pagination).
- Phase 5: Enrollment Service (User enrollments, lesson progress, idempotent completion).
- Phase 6: Commerce Service (Mock checkout, idempotent orders, revenue reporting).
- Phase 7: Angular Foundation + Backend Integration (Angular scaffolding, Models, HTTP Services, AuthService, Interceptor, Error handling).
- Phase 7A: Backend Startup Defect Repair (Repaired ECJ NoClassDefFoundError by running clean builds).
- Phase 8: Learner-Facing UI (Complete frontend implementation including Home, Course Discovery, Course Detail, Checkout, Dashboard, and Course Learning views. No video hosting implemented. Mock checkout integrated. Unit tests passed)..
- The 2-role restriction (`USER`, `ADMIN`).
- The 15 approved User Stories mapped to the microservices and specific parameterized REST endpoints.
- The authority hierarchy and strict anti-drift rules.

## Project Status
**The CourseCart Planning and Documentation Phase is 100% Complete.** 
- **Phase 1-6:** Backend foundations completed (external).
- **Phase 7 & 7A:** Angular Frontend Foundation & API Integration + Backend Startup Defect Repair completed. 
- **Phase 8:** Learner-Facing UI completed. Ready for next phase.
