package ro.subcontrol.decizii.model

data class NodeOption(
    val label: String = "",
    val next: String = ""
)

data class DecisionNode(
    val type: String = "",        // "question", "choice", "result"
    val step: String = "",
    val text: String = "",
    val ref: String = "",
    val options: List<NodeOption> = emptyList(),
    val severity: String = "",    // "success", "warning", "danger"
    val title: String = "",
    val action: String = ""
) {
    val isResult get() = type == "result"
    val isChoice get() = type == "choice"
}

data class DecisionTree(
    val start: String = "",
    val nodes: Map<String, DecisionNode> = emptyMap()
)
