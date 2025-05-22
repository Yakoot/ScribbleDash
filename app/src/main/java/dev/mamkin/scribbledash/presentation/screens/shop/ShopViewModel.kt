package dev.mamkin.scribbledash.presentation.screens.shop

import ShopRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mamkin.scribbledash.ui.theme.BackgroundTier
import dev.mamkin.scribbledash.ui.theme.PenTier
import dev.mamkin.scribbledash.ui.theme.canvasBackgroundAssets
import dev.mamkin.scribbledash.ui.theme.penAssets
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ShopViewModel(
    val shopRepository: ShopRepository
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _noMoneyEvent = Channel<Unit>()
    val noMoneyEvent = _noMoneyEvent.receiveAsFlow()

    private val _state = MutableStateFlow(ShopState())
    val state: StateFlow<ShopState> = _state
        .onStart {
            if (!hasLoadedInitialData) {
                subscribeOnRepository()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ShopState()
        )

    private fun subscribeOnRepository() {
        viewModelScope.launch {
            shopRepository.purchasedCanvasIds.collectLatest { purchasedCanvasIds ->
                val current = _state.value
                val updatedCanvasItems = canvasBackgroundAssets.map {
                    ShopItemState(
                        id = it.id,
                        penColor = null,
                        isSelected = current.canvasItems.any { item -> item.isSelected && item.id == it.id },
                        isLocked = !purchasedCanvasIds.contains(it.id),
                        canvasBackground = it.background,
                        grade = when (it.tier) {
                            BackgroundTier.BASIC -> ShopItemGrade.BASIC
                            BackgroundTier.PREMIUM -> ShopItemGrade.PREMIUM
                            BackgroundTier.LEGENDARY -> ShopItemGrade.LEGENDARY
                        },
                        price = it.tier.price,
                    )
                }
                _state.update { it.copy(canvasItems = updatedCanvasItems) }
            }
        }
        viewModelScope.launch {
            shopRepository.purchasedPenIds.collectLatest { purchasedPenIds ->
                val current = _state.value
                val updatedPenItems = penAssets.map {
                    ShopItemState(
                        id = it.id,
                        penColor = it.color,
                        isSelected = current.penItems.any { item -> item.isSelected && item.id == it.id },
                        isLocked = !purchasedPenIds.contains(it.id),
                        canvasBackground = null,
                        grade = when (it.tier) {
                            PenTier.BASIC -> ShopItemGrade.BASIC
                            PenTier.PREMIUM -> ShopItemGrade.PREMIUM
                            PenTier.LEGENDARY -> ShopItemGrade.LEGENDARY
                        },
                        price = it.tier.price,
                    )
                }
                _state.update { it.copy(penItems = updatedPenItems) }
            }
        }
        viewModelScope.launch {
            shopRepository.selectedPenId.collectLatest { selectedPenId ->
                _state.update { current ->
                    val updatedPenItems = current.penItems.map { it.copy(isSelected = it.id == selectedPenId) }
                    current.copy(penItems = updatedPenItems)
                }
            }
        }
        viewModelScope.launch {
            shopRepository.selectedCanvasId.collectLatest { selectedCanvasId ->
                _state.update { current ->
                    val updatedCanvasItems = current.canvasItems.map { it.copy(isSelected = it.id == selectedCanvasId) }
                    current.copy(canvasItems = updatedCanvasItems)
                }
            }
        }
        viewModelScope.launch {
            shopRepository.coinsCount.collectLatest { coinsCount ->
                _state.update { it.copy(coins = coinsCount) }
            }
        }
    }


    fun onAction(action: ShopAction) {
        when (action) {
            is ShopAction.SelectTab -> {
                _state.value = _state.value.copy(
                    tab = action.tab
                )
            }

            is ShopAction.TestAddCoins -> {
                viewModelScope.launch {
                    shopRepository.addCoins(100)
                }
            }

            is ShopAction.TestResetAll -> {
                viewModelScope.launch {
                    shopRepository.testResetAll()
                }
            }

            is ShopAction.CanvasClick -> onCanvasClick(action.id)
            is ShopAction.PenClick -> onPenClick(action.id)
        }
    }

    private fun onCanvasClick(id: Int) {
        val canvas = canvasBackgroundAssets.find { it.id == id } ?: return
        viewModelScope.launch {
            val purchasedCanvasIds = shopRepository.purchasedCanvasIds.first()
            val coins = shopRepository.coinsCount.first()
            val price = canvas.tier.price

            if (purchasedCanvasIds.contains(id)) {
                shopRepository.selectCanvasBackground(id)
                return@launch
            }

            if (coins >= price) {
                shopRepository.buyCanvasBackground(id, price)
                shopRepository.selectCanvasBackground(id)
            } else {
                _noMoneyEvent.send(Unit)
            }
        }
    }

    private fun onPenClick(id: Int) {
        val pen = penAssets.find { it.id == id } ?: return
        viewModelScope.launch {
            val purchasedPenIds = shopRepository.purchasedPenIds.first()
            val coins = shopRepository.coinsCount.first()
            val price = pen.tier.price

            if (purchasedPenIds.contains(id)) {
                shopRepository.selectPen(id)
                return@launch
            }

            if (coins >= price) {
                shopRepository.buyPen(id, price)
                shopRepository.selectPen(id)
            } else {
                _noMoneyEvent.send(Unit)
            }
        }
    }
}