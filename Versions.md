# CourseCart Trainee Versions Plan

The Master Project contains 15 User Stories. To create effective training modules, the project is divided into three distinct Trainee Versions. 
**Note:** US 01 (User Registration) and US 02 (User Login) are pre-implemented across ALL versions to provide a working authentication foundation. Trainees focus on implementing the core business logic (Controllers, ServiceImpls, HTML, and TS files) for 7 specific User Stories per version.

---

## 📘 Version 1 (V1) - "The E-Commerce Pipeline"
**Focus:** The complete storefront flow (Categories -> Courses -> Publish -> Search -> View -> Buy -> Receipt).
**Trainee Implements (8 Stories):**
- **US 03** – View Profile (User Service)
- **US 04** – Category Management (Catalog Service)
- **US 05** – Course Management (Catalog Service)
- **US 07** – Course Lifecycle (Catalog Service)
- **US 08** – Browse and Search Catalog (Catalog Service)
- **US 09** – View Course Details (Catalog Service)
- **US 10** – Single Course Purchase (Commerce Service)
- **US 11** – View Order History (Commerce Service)

**Backend Files to Hollow Out:**
- `UserController.java` & `UserServiceImpl.java` (US 03)
- `CategoryController.java`, `CourseController.java`, `CatalogServiceImpl.java` (US 04, 05, 07, 08, 09)
- `CommerceController.java`, `CommerceServiceImpl.java` (US 10, 11)

**Frontend Files to Hollow Out:**
- Services: `user.service.ts`, `catalog.service.ts`, `commerce.service.ts`
- Components: `profile`, `course-management`, `course-discovery`, `course-card`, `course-detail`, `checkout`, `order-history`

---

## 📗 Version 2 (V2) - "The LMS & Administration Pipeline"
**Focus:** Content delivery, metrics, and dashboards.
**Trainee Implements (7 Stories):**
- **US 05** – Course Management (Catalog Service)
- **US 06** – Admin Lesson Management (Catalog Service)
- **US 07** – Course Lifecycle (Catalog Service)
- **US 12** – My Learning Dashboard (Enrollment Service)
- **US 13** – Access Course Content (Enrollment Service)
- **US 14** – Track Lesson Progress (Enrollment Service)
- **US 15** – Admin Dashboard (All Services)

**Backend Files to Hollow Out:**
- `CourseController.java`, `CatalogServiceImpl.java` (US 05, 06, 07)
- `EnrollmentController.java`, `EnrollmentServiceImpl.java` (US 12, 13, 14)
- `UserController.java`, `CatalogController.java`, `CommerceController.java` (Count endpoints for US 15)

**Frontend Files to Hollow Out:**
- Services: `catalog.service.ts`, `enrollment.service.ts`
- Components: `course-management`, `lesson-management`, `admin-dashboard`, `learning-dashboard`, `course-learning`

---

## 📙 Version 3 (V3) - "The Full-Stack Sampler"
**Focus:** Tracing a user journey horizontally across the entire ecosystem.
**Trainee Implements (8 Stories):**
- **US 03** – View Profile (User Service)
- **US 05** – Course Management (Catalog Service)
- **US 08** – Browse and Search Catalog (Catalog Service)
- **US 09** – View Course Details (Catalog Service)
- **US 10** – Single Course Purchase (Commerce Service)
- **US 11** – View Order History (Commerce Service)
- **US 12** – My Learning Dashboard (Enrollment Service)
- **US 14** – Track Lesson Progress (Enrollment Service)

**Backend Files to Hollow Out:**
- `UserController.java`, `UserServiceImpl.java` (US 03)
- `CourseController.java`, `CatalogServiceImpl.java` (US 05, 08, 09)
- `CommerceController.java`, `CommerceServiceImpl.java` (US 10, 11)
- `EnrollmentController.java`, `EnrollmentServiceImpl.java` (US 12, 14)

**Frontend Files to Hollow Out:**
- Services: `user.service.ts`, `catalog.service.ts`, `commerce.service.ts`, `enrollment.service.ts`
- Components: `profile`, `course-management`, `course-discovery`, `course-detail`, `checkout`, `order-history`, `learning-dashboard`, `course-learning`
