package app.model

enum class Format(val label: String) {
    ECOSPOLD_1("EcoSpold 1"),
    ILCD("ILCD"),
    JSON_LD("JSON LD");

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