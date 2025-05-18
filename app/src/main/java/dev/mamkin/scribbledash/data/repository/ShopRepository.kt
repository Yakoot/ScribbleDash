import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class ShopRepository(
    private val dataStore: DataStore<Preferences>
) {

    private object PreferencesKeys {
        val COINS_COUNT = intPreferencesKey("COINS_COUNT")
        val SELECTED_PEN_ID = intPreferencesKey("SELECTED_PEN_ID")
        val SELECTED_CANVAS_ID = intPreferencesKey("SELECTED_CANVAS_ID")
        val PURCHASED_PEN_IDS = stringPreferencesKey("PURCHASED_PEN_IDS")
        val PURCHASED_CANVAS_IDS = stringPreferencesKey("PURCHASED_CANVAS_IDS")
    }

    val coinsCount: Flow<Int> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.COINS_COUNT] ?: 0
        }

    val selectedPenId: Flow<Int> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.SELECTED_PEN_ID] ?: 0
        }

    val selectedCanvasId: Flow<Int> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.SELECTED_CANVAS_ID] ?: 0
        }

    val purchasedPenIds: Flow<List<Int>> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.PURCHASED_PEN_IDS]
                ?.split(",")
                ?.mapNotNull { it.toIntOrNull() }
                ?: listOf(0)
        }

    val purchasedCanvasIds: Flow<List<Int>> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.PURCHASED_CANVAS_IDS]
                ?.split(",")
                ?.mapNotNull { it.toIntOrNull() }
                ?: listOf(0)
        }

    suspend fun addCoins(count: Int) {
        dataStore.edit { preferences ->
            val currentScore = preferences[PreferencesKeys.COINS_COUNT] ?: 0
            preferences[PreferencesKeys.COINS_COUNT] = currentScore + count
        }
    }

    suspend fun subtractCoins(count: Int) {
        dataStore.edit { preferences ->
            val currentScore = preferences[PreferencesKeys.COINS_COUNT] ?: 0
            preferences[PreferencesKeys.COINS_COUNT] = currentScore - count
        }
    }

    suspend fun buyPen(id: Int, cost: Int) {
        dataStore.edit { preferences ->
            val currentCoins = preferences[PreferencesKeys.COINS_COUNT] ?: 0
            if (currentCoins >= cost) {
                preferences[PreferencesKeys.COINS_COUNT] = currentCoins - cost
                val currentIds = preferences[PreferencesKeys.PURCHASED_PEN_IDS]?.split(",")?.mapNotNull { it.toIntOrNull() }?.toMutableList() ?: mutableListOf()
                if (!currentIds.contains(id)) {
                    currentIds.add(id)
                    preferences[PreferencesKeys.PURCHASED_PEN_IDS] = currentIds.joinToString(",")
                }
            }
        }
    }

    suspend fun buyCanvasBackground(id: Int, cost: Int) {
        dataStore.edit { preferences ->
            val currentCoins = preferences[PreferencesKeys.COINS_COUNT] ?: 0
            if (currentCoins >= cost) {
                preferences[PreferencesKeys.COINS_COUNT] = currentCoins - cost
                val currentIds = preferences[PreferencesKeys.PURCHASED_CANVAS_IDS]?.split(",")?.mapNotNull { it.toIntOrNull() }?.toMutableList() ?: mutableListOf()
                if (!currentIds.contains(id)) {
                    currentIds.add(id)
                    preferences[PreferencesKeys.PURCHASED_CANVAS_IDS] = currentIds.joinToString(",")
                }
            }
        }
    }

    suspend fun selectPen(id: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_PEN_ID] = id
        }
    }

    suspend fun selectCanvasBackground(id: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_CANVAS_ID] = id
        }
    }

    suspend fun testResetAll() {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_CANVAS_ID] = 0
            preferences[PreferencesKeys.SELECTED_PEN_ID] = 0
            preferences[PreferencesKeys.COINS_COUNT] = 0
            preferences[PreferencesKeys.PURCHASED_CANVAS_IDS] = "0"
            preferences[PreferencesKeys.PURCHASED_PEN_IDS] = "0"
        }
    }
} 