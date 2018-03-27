package app.model

enum class Format(val label: String) {
    ECOSPOLD_1("EcoSpold 1"),
    ECOSPOLD_2("EcoSpold 2"),
    ILCD("ILCD"),
    JSON_LD("JSON LD"),
    SIMAPRO_CSV("SimaPro CSV");

    companion object {
        fun get(label: String): Format? {
            Format.values().forEach { f ->
                if (f.label == label)
                    return f
            }
            return null
        }
    }
}