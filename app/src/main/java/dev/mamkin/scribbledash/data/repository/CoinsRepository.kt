import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class CoinsRepository(
    private val dataStore: DataStore<Preferences>
) {

    private object PreferencesKeys {
        val COINS_COUNT = intPreferencesKey("COINS_COUNT")
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
} 