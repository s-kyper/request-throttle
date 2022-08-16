## Throttler

[[_TOC_]]

---


### Introduction

Write a Spring boot application that only has 1 controller with 1 method that returns HTTP 200 and empty body.

---

### Task description

Implement a functionality that limits N requests in X minutes for 1 IP. If count of requests is more than N - throw an exception and HTTP 502, while requests count per X minutes won't get less than N.

---

### Requirements

N and X must be editable from configuration file. Functionality must be multithreaded. Make this functionality can be applied quickly to new methods (not only in controllers). Need to write a test that emulate multiply requests from different IP.

#### Stack

Java 11+, Spring + Spring Boot, JUnit, Dockerfile

---

:scroll: **END** 
