# 📦 Logistics Shipment Tracking System - Manual & Automation Testing

## Project Overview

This project covers end-to-end **functional, API, database, and automation testing** of a Logistics Shipment Tracking System. The system handles the complete shipment lifecycle — from creation to final delivery — and this test suite validates every stage of that workflow.

---

## 🛠️ Tools & Technologies Used

| Category | Tool/Technology |
|---|---|
| Automation | Selenium WebDriver (Java) |
| API Testing | Postman |
| Database Validation | MySQL |
| Test Management | Jira |
| Test Documentation | Excel |
| Build Tool | Maven |
| Test Framework | TestNG |
| Language | Java |

---

## 📁 Project Folder Structure

```
LogisticsShipmentTrackingSystem/
│
├── 1_LoginTest/                            → Login page basic test
├── 2_Open_Shipment_Portal/                → Open tracking portal test
├── 3_1_Login_with_Valid_Credentials/      → Valid login credentials test
├── 4_Search_for_a_Shipment_java_Copy_Edit/→ Search shipment by tracking ID
├── 5_Add_Shipment_to_Track/               → Add new shipment tracking test
├── 7_Invalid_Login_Negative_Test/         → Negative test for invalid login
├── Basic_Setup_and_Teardown_TestNG/       → Base setup & teardown config
├── Project_Test_Cases/                    → All test case Excel files & SQL scripts
│   ├── ShipmentTracking_TestCases.xlsx
│   ├── API_TestCases_Postman_Collection.json
│   └── DB_Validation_Queries.sql
├── 6_Checkout_with_Valid_Address          → Delivery address validation test
├── pom.xml                                → Maven dependencies
├── testng.xml                             → TestNG suite config
└── README.md
```

---

## 🔄 Shipment Status Lifecycle Tested

```
Created → Picked Up → In Transit → Out for Delivery → Delivered
```

Each transition was validated to ensure correct business logic is applied at every step.

---

## ✅ What Was Tested

### Functional Testing
- Shipment creation with valid and invalid inputs
- Shipment search by tracking ID
- Shipment status transition validation
- Login with valid and invalid credentials
- Delivery address entry and validation

### Negative / Boundary Testing
- Invalid tracking IDs (null, empty, special characters)
- Duplicate shipment creation
- Incorrect delivery status updates
- Login with wrong username/password combinations

### API Testing (Postman)
- Validated shipment tracking endpoints (GET /track/{id})
- Verified HTTP status codes (200, 400, 404, 500)
- Validated JSON response structures and field values

### Database Testing (MySQL)
- Confirmed shipment records created in DB after submission
- Verified status field updates at each lifecycle stage
- Checked delivery timestamps stored correctly

### Automation Testing (Selenium + Java + TestNG)
- Automated login flow (valid credentials)
- Automated shipment search by tracking ID
- Automated regression suite using TestNG

---

## 🐛 Defect Tracking

All defects were logged and tracked in **Jira** with:
- Clear bug title and description
- Steps to reproduce
- Expected vs. Actual result
- Screenshots attached
- Severity & Priority assigned

---

## 📊 Test Execution Reports

Test execution results and reports are maintained in **Excel** with:
- Test Case ID
- Test Description
- Preconditions
- Test Steps
- Expected Result
- Actual Result
- Status (Pass/Fail)
- Remarks

---

## ⚙️ How to Run the Automation Tests

### Prerequisites
- Java 11+
- Maven
- Chrome Browser
- ChromeDriver (matching version)
- TestNG plugin (if using Eclipse/IntelliJ)

### Steps

```bash
# Clone the repository
git clone https://github.com/YOUR_USERNAME/LogisticsShipmentTrackingSystem.git

# Navigate to project
cd LogisticsShipmentTrackingSystem

# Run all tests via Maven
mvn test

# OR run specific TestNG suite
mvn test -DsuiteXmlFile=testng.xml
```

---

## 📌 Key Highlights

- Designed **50+ test cases** covering positive, negative, and boundary scenarios
- Achieved **95%+ test coverage** across all shipment lifecycle stages
- Identified and reported **12+ critical defects** during testing
- Validated **5 API endpoints** using Postman
- Automated **regression scenarios** saving ~40% manual effort

---

## 📂 Test Data

Test data used for execution includes:
- Valid tracking IDs: `TRK001`, `TRK002`, `TRK003`
- Invalid tracking IDs: `INVALID`, `000000`, `@#$%^`
- Valid login: `admin@logistics.com` / `Admin@123`
- Invalid login: `wronguser@test.com` / `wrongpass`

---
