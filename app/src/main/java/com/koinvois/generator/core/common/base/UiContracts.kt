package com.koinvois.generator.core.common.base

/** Marker for an immutable screen state rendered by a Fragment/Activity. */
interface UiState

/** Marker for a user action dispatched from the UI into a [BaseViewModel]. */
interface UiEvent

/** Marker for a one-shot side effect (navigation, snackbar, share intent, ...). */
interface UiEffect
