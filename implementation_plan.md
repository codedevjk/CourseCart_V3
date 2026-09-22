# Allow Deletion of Categories with Inactive Courses and Guard Inactive Courses

The goal is to allow administrators to delete categories as long as they don't contain any ACTIVE courses. The inactive courses must persist in the database for existing users, but when a user tries to access an inactive course in their learning dashboard, they should be blocked by a popup.

## Proposed Changes

### 1. Database Schema Update
To allow deleting a category without deleting its attached courses, the category_id foreign key must be nullable and configured to ON DELETE SET NULL.
* Run an ALTER TABLE on the courses table in MySQL to:
  * Drop the existing foreign key.
  * Modify category_id to allow NULL.
  * Add the foreign key back with ON DELETE SET NULL.

### 2. Backend (catalog-service)
* **Course.java entity**: Change @JoinColumn(name = "category_id", nullable = false) to 
ullable = true.
* **CatalogServiceImpl.java**: Update the deleteCategory method. Instead of blocking deletion if *any* courses are attached, we will only block it if there is at least one ACTIVE course.
  * If all attached courses are INACTIVE or DRAFT, the category will be deleted. The database ON DELETE SET NULL cascade will safely detach the courses.

### 3. Frontend (coursecart-ui)
* **course-learning.component.ts**: When a user navigates to the learning page for a course, we will check if course.status !== 'ACTIVE'.
* If it is not active, we will show a popup modal with the message "The course is not available".
* **course-learning.component.html**: Add the HTML for the unavailability popup overlay, which will block access to the lesson content.

## Verification Plan
1. Alter the DB schema and update the backend, then restart the catalog-service.
2. Delete the "Salesforce" category from the Admin dashboard (which contains only an inactive course).
3. Verify the category deletes successfully and the "Demo1 Course" still exists in the DB with a 
ull category.
4. Open the learning dashboard for "Demo1 Course" as a user and verify the popup appears blocking the course.
