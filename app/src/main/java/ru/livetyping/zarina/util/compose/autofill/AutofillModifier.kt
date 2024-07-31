package ru.livetyping.zarina.util.compose.autofill

import android.annotation.SuppressLint
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.FocusEventModifierNode
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.GlobalPositionAwareModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.autofill.AutofillNode as ComposeAutofillNode

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.autofill(
    autofillType: AutofillType,
    onFilled: (String) -> Unit,
): Modifier = this.autofill(listOf(autofillType), onFilled)

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.autofill(
    autofillTypes: List<AutofillType>,
    onFilled: (String) -> Unit,
): Modifier = this then AutofillElement(autofillTypes, onFilled)

@OptIn(ExperimentalComposeUiApi::class)
private class AutofillElement(
    val autofillTypes: List<AutofillType>,
    val onFilled: (String) -> Unit,
) : ModifierNodeElement<AutofillNode>() {

    override fun create(): AutofillNode = AutofillNode(autofillTypes, onFilled)

    override fun update(node: AutofillNode) {
        node.update(autofillTypes, onFilled)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AutofillElement

        if (autofillTypes != other.autofillTypes) return false
        if (onFilled != other.onFilled) return false

        return true
    }

    override fun hashCode(): Int {
        var result = autofillTypes.hashCode()
        result = 31 * result + onFilled.hashCode()
        return result
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "autofill"
        properties["autofillTypes"] = autofillTypes
        properties["onFilled"] = onFilled
    }
}

@OptIn(ExperimentalComposeUiApi::class)
private class AutofillNode(
    autofillTypes: List<AutofillType>,
    onFilled: (String) -> Unit,
) : Modifier.Node(), GlobalPositionAwareModifierNode, FocusEventModifierNode,
    CompositionLocalConsumerModifierNode {

    private var composeAutofillNode = ComposeAutofillNode(
        autofillTypes = autofillTypes,
        onFill = onFilled,
    )

    private var coordinates: LayoutCoordinates? = null

    override fun onAttach() {
        addAutofillNodeToAutofillTree(composeAutofillNode)
    }

    @SuppressLint("SuspiciousCompositionLocalModifierRead")
    override fun onDetach() {
        val autofill = currentValueOf(LocalAutofill)
        autofill?.cancelAutofillForNode(composeAutofillNode)
    }

    override fun onGloballyPositioned(coordinates: LayoutCoordinates) {
        this.coordinates = coordinates
        updateAutofillNodeBoundingBox()
    }

    override fun onFocusEvent(focusState: FocusState) {
        val autofill = currentValueOf(LocalAutofill)
        autofill?.run {
            if (focusState.isFocused) {
                requestAutofillForNode(composeAutofillNode)
            } else {
                cancelAutofillForNode(composeAutofillNode)
            }
        }
    }

    fun update(
        autofillTypes: List<AutofillType>,
        onFilled: (String) -> Unit,
    ) {
        composeAutofillNode = ComposeAutofillNode(
            autofillTypes = autofillTypes,
            onFill = onFilled,
        )
        updateAutofillNodeBoundingBox()
        addAutofillNodeToAutofillTree(composeAutofillNode)
    }

    private fun addAutofillNodeToAutofillTree(node: ComposeAutofillNode) {
        val autofillTree = currentValueOf(LocalAutofillTree)
        autofillTree += node
    }

    private fun updateAutofillNodeBoundingBox() {
        coordinates?.let {
            composeAutofillNode.boundingBox = it.boundsInWindow()
        }
    }
}
