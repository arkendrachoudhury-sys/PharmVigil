package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.AeCaseDao
import com.example.data.model.AeCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [AeCase::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun aeCaseDao(): AeCaseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pharmvigil_database"
                )
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.aeCaseDao())
                    }
                }
            }

            private suspend fun populateInitialData(dao: AeCaseDao) {
                val sampleCases = listOf(
                    AeCase(
                        patientId = "SUBJ-1002",
                        protocolId = "ONC-2026-01",
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
                        clinicalNotes = "Subject presented with >6 stools/day over baseline, severe cramping. Admitted for IV corticosteroids."
                    ),
                    AeCase(
                        patientId = "SUBJ-1045",
                        protocolId = "ONC-2026-01",
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
                        clinicalNotes = "Mild fatigue reported on Day 14 post infusion, no interference with ADLs."
                    ),
                    AeCase(
                        patientId = "SUBJ-1088",
                        protocolId = "CV-402-HEART",
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
                        clinicalNotes = "Acute hypoxia (SpO2 84%), bilateral infiltrates on HRCT. Transferred to ICU, high-dose methylprednisolone."
                    ),
                    AeCase(
                        patientId = "SUBJ-1014",
                        protocolId = "ONC-2026-01",
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
                        clinicalNotes = "Managed with oral Ondansetron and Dexamethasone with good resolution."
                    ),
                    AeCase(
                        patientId = "SUBJ-1033",
                        protocolId = "ONC-2026-01",
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
                        clinicalNotes = "Bilateral 'stocking-glove' tingling affecting buttoning shirts. Dose reduced by 20% for Cycle 4."
                    )
                )

                sampleCases.forEach { dao.insertCase(it) }
            }
        }
    }
}
