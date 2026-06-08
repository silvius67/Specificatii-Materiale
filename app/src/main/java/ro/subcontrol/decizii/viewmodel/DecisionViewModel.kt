package ro.subcontrol.decizii.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ro.subcontrol.decizii.model.DecisionNode
import ro.subcontrol.decizii.model.DecisionTree

class DecisionViewModel(app: Application) : AndroidViewModel(app) {

    private lateinit var tree: DecisionTree

    // Stiva de navigare — reține istoricul nodurilor parcurse
    private val historyStack = ArrayDeque<String>()

    // Nodul curent expus către UI
    private val _currentNode = MutableStateFlow<DecisionNode?>(null)
    val currentNode: StateFlow<DecisionNode?> = _currentNode.asStateFlow()

    // Poate fi apăsat Înapoi?
    private val _canGoBack = MutableStateFlow(false)
    val canGoBack: StateFlow<Boolean> = _canGoBack.asStateFlow()

    // Progresul (0..1) pentru bara de progres
    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()

    // Breadcrumb — lista de etichete parcurse
    private val _breadcrumb = MutableStateFlow<List<String>>(emptyList())
    val breadcrumb: StateFlow<List<String>> = _breadcrumb.asStateFlow()

    init {
        loadTree()
    }

    private fun loadTree() {
        val json = getApplication<Application>()
            .assets
            .open("decision_tree.json")
            .bufferedReader()
            .use { it.readText() }

        tree = Gson().fromJson(json, DecisionTree::class.java)
        restart()
    }

    /** Navighează la un nod după ID (apăsare buton opțiune) */
    fun navigate(nodeId: String) {
        historyStack.addLast(nodeId)
        updateState()
    }

    /** Înapoi la nodul anterior */
    fun goBack() {
        if (historyStack.size > 1) {
            historyStack.removeLast()
            updateState()
        }
    }

    /** Resetează la început */
    fun restart() {
        historyStack.clear()
        historyStack.addLast(tree.start)
        updateState()
    }

    private fun updateState() {
        val currentId = historyStack.last()
        val node = tree.nodes[currentId]
        _currentNode.value = node

        _canGoBack.value = historyStack.size > 1

        // Progres estimat: maxim 5 pași
        _progress.value = ((historyStack.size - 1) / 5f).coerceIn(0f, 1f)

        // Breadcrumb: etichetele pașilor parcurși
        _breadcrumb.value = historyStack.mapNotNull { id ->
            tree.nodes[id]?.step?.takeIf { it.isNotBlank() }
        }
    }
}
