package com.pharmvigil.app.data

import com.pharmvigil.app.data.model.AeCase

object DemoData {
    val sampleCases = listOf(
        AeCase(
            patientId = "SUBJ-1002",
            protocolId = "DEMO-2026",
            suspectDrug = "Pembrolizumab (Keytruda)",
            adverseEventTerm = "Immune-mediated Colitis",
            medDraPt = "Colitis",
            systemOrganClass = "Gastrointestinal disorders",
            grade = 3,
            gradeDescription = "Severe symptoms; IV fluids indicated; peritoneal signs absent; hospitalization indicated",
            onsetDate = "2026-09-02",
            isSerious = true,
            seriousnessCriteria = "Inpatient hospitalization or prolongation",
            causality = "Probable",
            outcome = "Recovering/Resolving",
            actionTakenWithDrug = "Permanently Discontinued",
            clinicalNotes = "Subject presented with >6 stools/day over baseline, severe cramping. Admitted for IV corticosteroids.",
            isDemo = true
        ),
        AeCase(
            patientId = "SUBJ-1045",
            protocolId = "DEMO-2026",
            suspectDrug = "Nivolumab",
            adverseEventTerm = "Fatigue",
            medDraPt = "Fatigue",
            systemOrganClass = "General disorders and administration site conditions",
            grade = 1,
            gradeDescription = "Fatigue relieved by rest",
            onsetDate = "2026-09-05",
            isSerious = false,
            seriousnessCriteria = "None",
            causality = "Possible",
            outcome = "Recovering/Resolving",
            actionTakenWithDrug = "Dose Not Changed",
            clinicalNotes = "Mild fatigue reported on Day 14 post infusion, no interference with ADLs.",
            isDemo = true
        ),
        AeCase(
            patientId = "SUBJ-1088",
            protocolId = "DEMO-2026",
            suspectDrug = "Trastuzumab deruxtecan",
            adverseEventTerm = "Interstitial Lung Disease / Pneumonitis",
            medDraPt = "Pneumonitis",
            systemOrganClass = "Respiratory, thoracic and mediastinal disorders",
            grade = 4,
            gradeDescription = "Life-threatening respiratory compromise; urgent intervention indicated (e.g. tracheostomy or intubation)",
            onsetDate = "2026-09-08",
            isSerious = true,
            seriousnessCriteria = "Life-threatening, Inpatient hospitalization",
            causality = "Probable",
            outcome = "Not Recovered",
            actionTakenWithDrug = "Permanently Discontinued",
            clinicalNotes = "Acute hypoxia (SpO2 84%), bilateral infiltrates on HRCT. Transferred to ICU, high-dose methylprednisolone.",
            isDemo = true
        ),
        AeCase(
            patientId = "SUBJ-1014",
            protocolId = "DEMO-2026",
            suspectDrug = "Cisplatin",
            adverseEventTerm = "Nausea",
            medDraPt = "Nausea",
            systemOrganClass = "Gastrointestinal disorders",
            grade = 2,
            gradeDescription = "Oral intake decreased without significant weight loss, dehydration or malnutrition",
            onsetDate = "2026-09-07",
            isSerious = false,
            seriousnessCriteria = "None",
            causality = "Definite",
            outcome = "Recovered/Resolved",
            actionTakenWithDrug = "Dose Not Changed",
            clinicalNotes = "Managed with oral Ondansetron and Dexamethasone with good resolution.",
            isDemo = true
        ),
        AeCase(
            patientId = "SUBJ-1033",
            protocolId = "DEMO-2026",
            suspectDrug = "Paclitaxel",
            adverseEventTerm = "Peripheral sensory neuropathy",
            medDraPt = "Peripheral sensory neuropathy",
            systemOrganClass = "Nervous system disorders",
            grade = 2,
            gradeDescription = "Moderate symptoms; limiting instrumental ADLs (e.g. preparing meals, managing finances, shopping)",
            onsetDate = "2026-08-30",
            isSerious = false,
            seriousnessCriteria = "None",
            causality = "Probable",
            outcome = "Not Recovered",
            actionTakenWithDrug = "Dose Reduced",
            clinicalNotes = "Bilateral 'stocking-glove' tingling affecting buttoning shirts. Dose reduced by 20% for Cycle 4.",
            isDemo = true
        )
    )
}
