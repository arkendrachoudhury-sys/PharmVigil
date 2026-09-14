package com.example.data.ctcae

data class CtcaeTerm(
    val term: String,
    val medDraPt: String,
    val soc: String,
    val definition: String,
    val grade1: String,
    val grade2: String,
    val grade3: String,
    val grade4: String,
    val grade5: String
)

object CtcaeDatabase {
    val terms = listOf(
        CtcaeTerm(
            term = "Diarrhea",
            medDraPt = "Diarrhea",
            soc = "Gastrointestinal disorders",
            definition = "A disorder characterized by frequent and watery bowel movements.",
            grade1 = "Increase of <4 stools per day over baseline; mild increase in ostomy output compared to baseline.",
            grade2 = "Increase of 4 - 6 stools per day over baseline; moderate increase in ostomy output compared to baseline; limiting instrumental ADL.",
            grade3 = "Increase of >=7 stools per day over baseline; hospitalization indicated; severe increase in ostomy output; limiting self care ADL.",
            grade4 = "Life-threatening consequences; urgent intervention indicated (e.g., hemodynamic collapse).",
            grade5 = "Death"
        ),
        CtcaeTerm(
            term = "Colitis",
            medDraPt = "Colitis",
            soc = "Gastrointestinal disorders",
            definition = "A disorder characterized by inflammation of the colon.",
            grade1 = "Asymptomatic; clinical or diagnostic observations only; intervention not indicated.",
            grade2 = "Abdominal pain; mucus or blood in stool; limiting instrumental ADL.",
            grade3 = "Severe abdominal pain; peritoneal signs absent; hospitalization indicated; limiting self care ADL.",
            grade4 = "Life-threatening consequences (e.g., perforation, ischemia, necrosis, toxic megacolon); urgent intervention indicated.",
            grade5 = "Death"
        ),
        CtcaeTerm(
            term = "Nausea",
            medDraPt = "Nausea",
            soc = "Gastrointestinal disorders",
            definition = "A disorder characterized by a queasy sensation and an urge to vomit.",
            grade1 = "Loss of appetite without alteration in eating habits.",
            grade2 = "Oral intake decreased without significant weight loss, dehydration or malnutrition; minimal medical intervention indicated.",
            grade3 = "Inadequate oral caloric or fluid intake; tube feeding, TPN, or hospitalization indicated.",
            grade4 = "Not applicable for Nausea in CTCAE framework.",
            grade5 = "Death (secondary to aspiration or severe complications)"
        ),
        CtcaeTerm(
            term = "Vomiting",
            medDraPt = "Vomiting",
            soc = "Gastrointestinal disorders",
            definition = "A disorder characterized by the reflexive act of ejecting the contents of the stomach through the mouth.",
            grade1 = "1 - 2 episodes (separated by 5 minutes) in 24 hrs.",
            grade2 = "3 - 5 episodes in 24 hrs; outpatient IV hydration indicated.",
            grade3 = ">=6 episodes in 24 hrs; tube feeding, TPN or hospitalization indicated.",
            grade4 = "Life-threatening consequences; urgent hemodynamic or surgical intervention indicated.",
            grade5 = "Death"
        ),
        CtcaeTerm(
            term = "Fatigue",
            medDraPt = "Fatigue",
            soc = "General disorders and administration site conditions",
            definition = "A disorder characterized by a state of generalized weakness with a pronounced inability to summon sufficient energy to accomplish daily tasks.",
            grade1 = "Fatigue relieved by rest.",
            grade2 = "Fatigue not relieved by rest; limiting instrumental ADL.",
            grade3 = "Fatigue not relieved by rest, limiting self care ADL.",
            grade4 = "Not applicable for Fatigue.",
            grade5 = "Death"
        ),
        CtcaeTerm(
            term = "Pneumonitis",
            medDraPt = "Pneumonitis",
            soc = "Respiratory, thoracic and mediastinal disorders",
            definition = "A disorder characterized by focal or diffuse non-infectious inflammation of the lung parenchyma.",
            grade1 = "Asymptomatic; clinical or diagnostic observations only (radiographic infiltrates); intervention not indicated.",
            grade2 = "Symptomatic; medical intervention indicated (e.g., systemic corticosteroids); limiting instrumental ADL.",
            grade3 = "Severe symptoms; limiting self care ADL; oxygen indicated; hospitalization indicated.",
            grade4 = "Life-threatening respiratory compromise; urgent intervention indicated (e.g., intubation/ventilator).",
            grade5 = "Death"
        ),
        CtcaeTerm(
            term = "Neutropenia",
            medDraPt = "Neutrophil count decreased",
            soc = "Blood and lymphatic system disorders",
            definition = "A disorder characterized by a decrease in the absolute number of neutrophils in the blood.",
            grade1 = "<LLN - 1500/mm3; <LLN - 1.5 x 10^9 /L",
            grade2 = "<1500 - 1000/mm3; <1.5 - 1.0 x 10^9 /L",
            grade3 = "<1000 - 500/mm3; <1.0 - 0.5 x 10^9 /L",
            grade4 = "<500/mm3; <0.5 x 10^9 /L (urgent G-CSF and protective precautions indicated)",
            grade5 = "Death (secondary to neutropenic sepsis)"
        ),
        CtcaeTerm(
            term = "Febrile neutropenia",
            medDraPt = "Febrile neutropenia",
            soc = "Blood and lymphatic system disorders",
            definition = "A disorder characterized by an ANC <1000/mm3 and a single temperature of >38.3 degrees C (101 degrees F) or a sustained temperature of >=38 degrees C (100.4 degrees F) for more than one hour.",
            grade1 = "Not applicable (by definition requires fever + neutropenia).",
            grade2 = "Not applicable.",
            grade3 = "Present; absolute neutrophil count <1000/mm3 with single temp >38.3 C or sustained >=38 C for >1 hr; IV antibiotics indicated.",
            grade4 = "Life-threatening consequences; urgent intervention indicated (e.g., septic shock, intensive care).",
            grade5 = "Death"
        ),
        CtcaeTerm(
            term = "Thrombocytopenia",
            medDraPt = "Platelet count decreased",
            soc = "Blood and lymphatic system disorders",
            definition = "A disorder characterized by a decrease in the number of platelets in the blood.",
            grade1 = "<LLN - 75,000/mm3; <LLN - 75.0 x 10^9 /L",
            grade2 = "<75,000 - 50,000/mm3; <75.0 - 50.0 x 10^9 /L",
            grade3 = "<50,000 - 25,000/mm3; <50.0 - 25.0 x 10^9 /L; transfusion indicated for bleeding.",
            grade4 = "<25,000/mm3; <25.0 x 10^9 /L; life-threatening hemorrhage risk; urgent platelet transfusion indicated.",
            grade5 = "Death"
        ),
        CtcaeTerm(
            term = "Anemia",
            medDraPt = "Anemia",
            soc = "Blood and lymphatic system disorders",
            definition = "A disorder characterized by a decrease in hemoglobin concentration.",
            grade1 = "Hemoglobin <LLN - 10.0 g/dL; <LLN - 6.2 mmol/L",
            grade2 = "Hemoglobin <10.0 - 8.0 g/dL; <6.2 - 4.9 mmol/L",
            grade3 = "Hemoglobin <8.0 g/dL; <4.9 mmol/L; transfusion indicated",
            grade4 = "Life-threatening consequences; urgent intervention indicated (e.g., acute hemodynamic instability).",
            grade5 = "Death"
        ),
        CtcaeTerm(
            term = "Peripheral sensory neuropathy",
            medDraPt = "Peripheral sensory neuropathy",
            soc = "Nervous system disorders",
            definition = "A disorder characterized by damage to the nerves of the peripheral nervous system associated with sensory changes (paresthesias, dysesthesias).",
            grade1 = "Asymptomatic; loss of deep tendon reflexes or paresthesia not interfering with function.",
            grade2 = "Moderate symptoms; limiting instrumental ADL (e.g., buttoning clothes, using utensils).",
            grade3 = "Severe symptoms; limiting self care ADL (e.g., feeding oneself, dressing); assistive device indicated.",
            grade4 = "Life-threatening consequences; urgent intervention indicated (e.g., respiratory paralysis).",
            grade5 = "Death"
        ),
        CtcaeTerm(
            term = "Hypertension",
            medDraPt = "Hypertension",
            soc = "Vascular disorders",
            definition = "A disorder characterized by a persistently high arterial blood pressure.",
            grade1 = "Prehypertension (systolic BP 120 - 139 mmHg or diastolic BP 80 - 89 mmHg).",
            grade2 = "Stage 1 hypertension (systolic BP 140 - 159 mmHg or diastolic BP 90 - 99 mmHg); medical intervention indicated; recurrent or persistent (>=24 hrs).",
            grade3 = "Stage 2 hypertension (systolic BP >=160 mmHg or diastolic BP >=100 mmHg); medical intervention indicated; more than one drug or more intensive therapy than previously.",
            grade4 = "Life-threatening consequences (e.g., malignant hypertension, transient or permanent neurologic deficit, hypertensive crisis); urgent intervention indicated.",
            grade5 = "Death"
        ),
        CtcaeTerm(
            term = "Rash maculo-papular",
            medDraPt = "Rash maculo-papular",
            soc = "Skin and subcutaneous tissue disorders",
            definition = "A disorder characterized by the presence of macules (flat) and papules (raised) which may be pruritic.",
            grade1 = "Macules/papules covering <10% BSA with or without symptoms (e.g., pruritus, burning, tightness).",
            grade2 = "Macules/papules covering 10 - 30% BSA with or without symptoms; limiting instrumental ADL.",
            grade3 = "Macules/papules covering >30% BSA with or without associated symptoms; limiting self care ADL; systemic corticosteroids indicated.",
            grade4 = "Not applicable (Bullous/ulcerative skin reaction or SJS/TEN graded separately).",
            grade5 = "Death"
        ),
        CtcaeTerm(
            term = "Alanine aminotransferase increased (ALT)",
            medDraPt = "Alanine aminotransferase increased",
            soc = "Investigations",
            definition = "A finding based on laboratory test results indicating an increase in the level of ALT in the blood.",
            grade1 = ">ULN - 3.0 x ULN",
            grade2 = ">3.0 - 5.0 x ULN",
            grade3 = ">5.0 - 20.0 x ULN",
            grade4 = ">20.0 x ULN (Hy's Law evaluation triggered if accompanied by total bilirubin >2x ULN).",
            grade5 = "Death (secondary to acute hepatic failure)"
        ),
        CtcaeTerm(
            term = "Acute kidney injury",
            medDraPt = "Acute kidney injury",
            soc = "Renal and urinary disorders",
            definition = "A disorder characterized by a rapid decline in renal function with azotemia and electrolyte imbalances.",
            grade1 = "Serum creatinine >0.3 mg/dL increase from baseline or creatinine 1.5 - 1.9 times baseline.",
            grade2 = "Serum creatinine 2.0 - 2.9 times baseline.",
            grade3 = "Serum creatinine >=3.0 times baseline or >4.0 mg/dL; hospitalization indicated.",
            grade4 = "Life-threatening consequences; dialysis indicated.",
            grade5 = "Death"
        ),
        CtcaeTerm(
            term = "Electrocardiogram QT corrected interval prolonged",
            medDraPt = "Electrocardiogram QT prolonged",
            soc = "Investigations",
            definition = "A finding based on ECG results indicating an increase in the corrected QT interval.",
            grade1 = "Average QTc 450 - 480 ms.",
            grade2 = "Average QTc 481 - 500 ms.",
            grade3 = "Average QTc >=501 ms on at least two separate ECGs.",
            grade4 = "QTc >=501 ms or >60 ms change from baseline AND Torsade de pointes or polymorphic ventricular tachycardia or signs/symptoms of serious arrhythmia.",
            grade5 = "Death"
        )
    )

    fun search(query: String): List<CtcaeTerm> {
        if (query.isBlank()) return terms
        val lower = query.lowercase().trim()
        return terms.filter {
            it.term.lowercase().contains(lower) ||
            it.medDraPt.lowercase().contains(lower) ||
            it.soc.lowercase().contains(lower) ||
            it.definition.lowercase().contains(lower)
        }
    }

    fun getAllSocs(): List<String> {
        return terms.map { it.soc }.distinct().sorted()
    }
}
