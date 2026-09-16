package com.pharmvigil.app.data.model

enum class Causality(val label: String) {
    DEFINITE("Definite"),
    PROBABLE("Probable"),
    POSSIBLE("Possible"),
    UNLIKELY("Unlikely"),
    UNRELATED("Unrelated");

    companion object {
        fun fromLabel(label: String): Causality =
            entries.find { it.label.equals(label, ignoreCase = true) } ?: POSSIBLE
    }
}

enum class Outcome(val label: String) {
    RECOVERED("Recovered/Resolved"),
    RECOVERING("Recovering/Resolving"),
    NOT_RECOVERED("Not Recovered"),
    FATAL("Fatal"),
    UNKNOWN("Unknown");

    companion object {
        fun fromLabel(label: String): Outcome =
            entries.find { it.label.equals(label, ignoreCase = true) } ?: RECOVERING
    }
}

enum class ActionTaken(val label: String) {
    DOSE_REDUCED("Dose Reduced"),
    DOSE_INTERRUPTED("Dose Interrupted"),
    PERMANENTLY_DISCONTINUED("Permanently Discontinued"),
    DOSE_NOT_CHANGED("Dose Not Changed"),
    NOT_APPLICABLE("Not Applicable");

    companion object {
        fun fromLabel(label: String): ActionTaken =
            entries.find { it.label.equals(label, ignoreCase = true) } ?: DOSE_INTERRUPTED
    }
}
