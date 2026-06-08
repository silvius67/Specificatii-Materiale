package ro.subcontrol.decizii.model

import com.google.gson.annotations.SerializedName

// Tipurile de noduri posibile în arbore
enum class NodeType {
    @SerializedName("question") QUESTION,
    @SerializedName("choice")   CHOICE,
    @SerializedName("result")   RESULT
}

// Severitatea unui nod de tip rezultat
enum class Severity {
    @SerializedName("success") SUCCESS,
    @SerializedName("warning") WARNING,
    @SerializedName("danger")  DANGER
}

// O opțiune (buton) dintr-un nod
data class NodeOption(
    val label: String,
    val next: String
)

// Un nod din arbore
data class DecisionNode(
    val type: NodeType,
    val step: String = "",
    val text: String = "",
    val ref: String = "",
    val options: List<NodeOption> = emptyList(),

    // Câmpuri doar pentru noduri de tip RESULT
    val severity: Severity? = null,
    val title: String = "",
    val action: String = ""
)

// Arborele complet, citit din JSON
data class DecisionTree(
    val start: String,
    val nodes: Map<String, DecisionNode>
)
