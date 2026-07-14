package com.koinvois.generator.core.common.state

import com.koinvois.generator.databinding.StateEmptyBinding
import com.koinvois.generator.databinding.StateErrorBinding
import com.koinvois.generator.utilities.extensions.hide
import com.koinvois.generator.utilities.extensions.visible

/** Binds text/action for the shared `state_empty.xml` component. */
fun StateEmptyBinding.bind(
    title: String,
    description: String? = null,
    actionText: String? = null,
    onAction: (() -> Unit)? = null
) {
    txtStateTitle.text = title
    if (description != null) {
        txtStateDescription.text = description
        txtStateDescription.visible()
    } else {
        txtStateDescription.hide()
    }
    if (actionText != null && onAction != null) {
        btnStateAction.text = actionText
        btnStateAction.setOnClickListener { onAction() }
        btnStateAction.visible()
    } else {
        btnStateAction.hide()
    }
}

/** Binds text/retry-action for the shared `state_error.xml` component. */
fun StateErrorBinding.bind(
    title: String,
    description: String? = null,
    onRetry: (() -> Unit)? = null
) {
    txtStateTitle.text = title
    if (description != null) {
        txtStateDescription.text = description
        txtStateDescription.visible()
    } else {
        txtStateDescription.hide()
    }
    btnStateAction.setOnClickListener { onRetry?.invoke() }
}
