# Personalized Travel Itinerary Planner – Bachelor’s Thesis Project

This repository contains the source code and documentation for my **bachelor’s thesis project**, a full‑stack web application that builds **personalized travel itineraries** from a small set of user preferences (starting point, interests, season, companions, budget, optional destination, etc.).

The application combines **live web data**, **large language models** and **map services** to suggest safe, relevant destinations and detailed points of interest, then presents everything in an interactive UI.

---

## 1. What the application does

From a user’s perspective, the app helps you:

- **Create a personalized trip**  
  - Enter basic profile and trip details: starting point, age, gender, interests, companions, season, budget, and optional destination (city / country / continent).
  - Required fields are intentionally minimal: **starting point** and **interests**.

- **Generate a smart itinerary in one click**  
  - The backend builds a tailored tour with:
    - a **chosen destination city**,
    - a **tour name** and **highlights**,  
    - up to **5 points of interest (POIs)** with rich descriptions, tags and metadata.
  - Even with the same input, the itinerary can change from one generation to the next, keeping results fresh and varied.

- **View rich details for each point of interest**
  - long, story‑like descriptions (history, culture, traditions),
  - address, coordinates (lat/lon), rating, phone, website, opening hours, representative tags.
  - an automatically generated **representative image** for the tour.

- **Explore itineraries on an interactive map**
  - All POIs are shown on a map with numbered markers for easy orientation.

- **Create and manage local “accounts” and saved trips**
  - Users can create a profile and store multiple itineraries.
  - All user data and itineraries are stored **locally in the browser** (IndexedDB via Dexie), not on a remote server.

---

## 2. High‑level system architecture

The system is implemented as a **monolithic RESTful web application**:

- **Backend**: Kotlin + Spring Boot REST API
- **Frontend**: Angular + Angular Material
- **Local storage**: Dexie / IndexedDB in the browser
- **Deployment**: Docker containers for frontend and backend, orchestrated with Docker Compose 

The high‑level flow for generating an itinerary:

1. **User submits trip preferences** from the Angular UI.
2. Backend **builds a Google search prompt** from those preferences (budget, destination type, interests, etc.) and calls **Google Custom Search API**. 
3. For the top results, the backend:
   - fetches the HTML using **WebScrapingAPI**,  
   - cleans it with **Jsoup** to keep only textual content. 
4. The cleaned content is sent to **OpenAI ChatGPT (GPT‑3.5‑turbo‑1106)** to **extract candidate cities**; the system counts frequencies and chooses the best destination (breaking ties randomly).
5. A second ChatGPT call generates the **tour JSON**:
   - `tour_name`,  
   - `tour_highlights`,  
   - `points_of_interest` (name, description, tags).
6. For each POI, the backend calls **Google Places Text Search + Details APIs** to enrich it with verified data (address, coordinates, rating, phone, website, schedule, photo reference).
7. The backend calls **OpenAI DALL‑E‑3** to generate a representative image for the tour and converts it to a **base64 data URL** so it stays valid even after the original link expires.
8. The combined result is returned as a `VacationPlannerOutput` object and rendered in the Angular UI as a ticket‑like itinerary card plus map.  

---

## 3. Tech stack

### Backend (Kotlin / Spring Boot)

- **Language:** Kotlin  
- **Framework:** Spring Boot 3.x  
- **Build & deps:** Maven (`pom.xml` includes Spring Web, validation, Jackson Kotlin, Apache HttpClient 5, Jsoup, Google Maps client, Swagger annotations, etc.).  
- **Architecture:**
  - `Controller` module (`VacationPlannerController`)  
    - exposes REST endpoints under `/api/v1/relation-trip`:
      - `POST /planner` – generate an itinerary  
      - `GET /filter-cities/{text}` – autocomplete destinations  
      - `GET /city-exists/{city}` – validate city names  
      - several test endpoints for external services.  
  - `Services`:
    - `VacationPlannerService` – orchestrates the full planning algorithm (search, scraping, city extraction, POI generation, Places enrichment, image generation).  
    - `CustomSearchService` – builds search prompts and calls Google Custom Search.  
    - `ChatGptService` – crafts prompts and parses JSON responses for both city extraction and POI generation.  
    - `ImageGeneratorService` – calls DALL‑E‑3, rescales images, encodes to base64 data URLs.  
    - `FilterCitiesService` – loads and filters the city database, removes diacritics, supports autocomplete and existence checks.  
  - `Gateways`:
    - `IOpenAiGateway` + configuration for ChatGPT & DALL‑E (pooled HTTP client with timeouts, auth header interceptor).  
    - `SearchApiGateway` – Google Custom Search API.  
    - `WebScrapingApiGateway` – WebScrapingAPI.  
    - `PlacesApiGateway` – Google Places Text Search & Details APIs.  
  - `Models`:
    - DTOs such as `VacationPlannerInput`, `VacationPlannerOutput`, `Poi`, `Itinerary`, `ItineraryPoi`.  
    - Enums for `Gender`, `Attendants`, `Season`, `Transport`, `Interests`, `Budget`.  
  - `Exception handling`:
    - `VacationPlannerExceptionHandler` using `@ControllerAdvice` to normalize HTTP errors, validation errors and log levels.

### Frontend (Angular)

- **Framework:** Angular (TypeScript)  
- **UI library:** Angular Material
- **Core components:**
  - `HomeComponent` – main page with profile info, saved trips and usage guide.  
  - `SuggestComponent` – form for entering trip details and preferences.  
  - `ItineraryViewerComponent` – displays the generated trip as an “airline ticket” plus map and POI list.  
  - `RegisterLoginComponent` – browser‑local user “accounts”.  
  - `LoadingComponent` – full‑screen loading dialog while the itinerary is being generated (planning can take ~1–2 minutes).  
  - `DialogComponent` – reusable dialog for success/error messages.

- **Core services:**
  - `PlannerService` – calls the backend `/planner` endpoint.  
  - `CitiesFilterService` – calls `/filter-cities` for destination autocomplete.  
  - `SendItineraryService` – shares itinerary data between components.  
  - `EncrDecrService` – simple encryption/decryption for user passwords before storing them locally.

- **Maps integration:** Google Maps JavaScript API for dynamic map rendering and POI markers. 

### Data & caching

- **City database generation**  
  - Source: public GitHub dataset of countries and cities. 
  - A dedicated **Python script** preprocesses the raw file to:
    - remove high‑risk countries/regions based on travel warnings,  
    - remove very small settlements (< 1000 inhabitants),  
    - filter out strings like “village”, “commune” (and equivalents in other languages).
  - Result: ~111,800 cleaned city entries with associated country and continent. 

- **Browser‑local database (Dexie / IndexedDB)**  
  - Tables:
    - `users`: username, encrypted password, email, gender, location, yearOfBirth, avatar src, etc.  
    - `itineraries`: tour name, photo (base64), destination, starting point, season, attendant, transport, budget, highlights, number of POIs, and serialized list of POIs.  
    - `Poi` (conceptual) for the POI attributes stored inside itineraries.  
  - All user‑specific data stays in the browser cache; changing device or browser implies a fresh start.

### Infrastructure

- **Dockerfiles** for both backend and frontend:
  - Backend: OpenJDK base image, copies Spring Boot JAR, exposes port 8080 and runs `java -jar`.  
  - Frontend: Node (alpine), installs Angular CLI and dependencies, runs `ng serve` on port 4200.  
- **Docker Compose**: starts both services and maps ports `8080:8080` (API) and `4200:4200` (SPA).

---

## 4. Data privacy & security

The project focuses on **client‑side data ownership** and safe content:  

- No server‑side user database: profiles and itineraries are stored in IndexedDB on the user’s device.
- Users can clear their browser cache at any time to erase all local data.
- Passwords are stored **encrypted** using the `EncrDecrService`, never in plain text.
- The curated city database excludes destinations that are:
  - too small,  
  - or flagged as dangerous based on news sources and advisories.  

---

## 5. Testing & reliability

To validate the external integrations and overall flow, the backend exposes **dedicated test endpoints** for:  

- Google Custom Search (prompt correctness & result quality),
- WebScrapingAPI + Jsoup (HTML cleanup),
- ChatGPT (city extraction and itinerary JSON generation),
- Google Places (Text Search + Details).  

The thesis document includes example requests and responses for each of these services, plus full end‑to‑end itinerary examples.

---

## 6. Limitations & future work

Known limitations:  

- Dependence on multiple external APIs (Google, OpenAI, WebScrapingAPI) means downtime or quota limits can break itinerary generation.
- Personalization is prompt‑ and rule‑based; very specific or niche preferences might not always be perfectly captured.
- The app is optimized for desktop/laptop browsers; mobile UX is limited because the local cache is device‑specific.

Planned / suggested future improvements:  

- Dedicated **mobile application** for on‑the‑go access to saved itineraries.
- More advanced security (e.g. stronger encryption, optional cloud sync with user consent).
- Better handling of API failures and retry strategies.
- Additional filters (e.g. accessibility, kids‑friendly, pet‑friendly, eco‑friendly trips).
