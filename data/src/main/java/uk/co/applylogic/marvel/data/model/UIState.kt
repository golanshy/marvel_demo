package uk.co.applylogic.marvel.data.model


sealed class UIState {
    object Initialized : UIState()
    object Refreshing : UIState()
    object InProgress : UIState()
    object OnResults: UIState()
    object NoResults: UIState()
    data class Error(val errorCode: Int, val errorMessage: String?): UIState()
}
