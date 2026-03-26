## About
This application allows sellers to manage marketing campaigns for their products. It allows for full CRUD operations (Create, Read, Update, Delete) with an integrated financial system for budget management.

---

## Live Demo

* **Frontend:** [https://campaign-manager-five-steel.vercel.app/](https://campaign-manager-five-steel.vercel.app/)
* **Backend:** [https://campaign-manager-9w8p.onrender.com](https://campaign-manager-9w8p.onrender.com)

**Note:** The application is hosted on a free tier. Due to "cold starts," the **first request may take 30-60 seconds** to load while the server wakes up.

---

## Overview

Sellers can manage separate campaigns for each product with the following mandatory data:
* **Campaign Name**
* **Keywords** (with typeahead support)
* **Bid Amount** (with minimum amount validation)
* **Campaign Fund** (automatically deducted from the user's **Emerald Account**, with real-time balance updates)
* **Status** (ON/OFF toggle)
* **Town** (selected from a pre-populated dropdown)
* **Radius** (defined in kilometers)

---

The project includes a comprehensive suite of unit tests for the core logic, which can be executed using ./mvn test