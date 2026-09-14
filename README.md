# PharmVigil

PharmVigil is a standalone, offline-first clinical pharmacovigilance application designed for healthcare professionals, clinical trial investigators, and regulatory compliance teams. It facilitates the structured grading, logging, and evaluation of Adverse Events (AE) and Serious Adverse Events (SAE) without requiring active network connectivity or external API dependencies.

## Core Capabilities

*   **CTCAE Reference Library**: An embedded, searchable offline encyclopedia containing common Common Terminology Criteria for Adverse Events (CTCAE) terms, mapped to MedDRA Preferred Terms (PT) and System Organ Classes (SOC).
*   **SAE Regulatory Wizard**: An interactive decision matrix utilizing ICH E2A and FDA 21 CFR 312.32 guidelines. It evaluates seriousness criteria, causality, and expectedness to determine Suspected Unexpected Serious Adverse Reaction (SUSAR) status and calculate expedited regulatory reporting clocks (e.g., 7-day vs 15-day).
*   **Local Data Persistence**: Secure, on-device storage of clinical event logs utilizing Android Room database architecture. Maintains Subject IDs, suspect drugs, clinical narratives, causality assessments, and drug actions.
*   **Automated Narrative Generation**: Formats logged case parameters into standardized CIOMS-I / ICH E2A narrative structures, available for immediate clipboard export.
*   **Analytics Dashboard**: Native visual analytics tracking severity distribution (Grade 1 through Grade 5), seriousness proportions, and critical sentinel event monitoring across active cases.

## System Architecture

The application is built entirely on the Android Jetpack Compose framework, prioritizing a smooth, lag-free user experience through asynchronous data handling and local caching.

```mermaid
graph TD
    UI[Jetpack Compose UI Layer]
    VM[PharmVigil ViewModel]
    Repo[AeCase Repository]
    DB[(Room SQLite Database)]
    CTCAE[Embedded CTCAE Library]

    UI -->|Observes StateFlow| VM
    UI -->|Dispatches Events| VM
    VM -->|Queries & Updates| Repo
    VM -->|Read-only Lookup| CTCAE
    Repo -->|Suspend Functions| DB
```

## User Flow

The typical clinical workflow within the application is designed for rapid, deterministic data entry at the point of care.

```mermaid
flowchart LR
    A[Identify Event] --> B{Action Route}
    
    B -->|Search Library| C[CTCAE Catalog]
    C -->|Select Term & Grade| E
    
    B -->|Evaluate Criteria| D[SAE Wizard]
    D -->|Determine Reporting Clock| E
    
    E[Log AE/SAE Case] --> F[Local Database Storage]
    F --> G[CIOMS-I Narrative Export]
    F --> H[Analytics Dashboard]
```

## Technical Implementation

*   **Language**: Kotlin
*   **UI Toolkit**: Jetpack Compose (Material Design 3)
*   **Architecture**: MVVM (Model-View-ViewModel) with Unidirectional Data Flow
*   **Local Storage**: Room Persistence Library (SQLite abstract)
*   **Concurrency**: Kotlin Coroutines & Flow
