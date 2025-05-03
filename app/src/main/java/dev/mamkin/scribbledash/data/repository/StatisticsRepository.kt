import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class StatisticsRepository(
    private val dataStore: DataStore<Preferences>
) {

    private object PreferencesKeys {
        val HIGHEST_SPEED_DRAW_SCORE = intPreferencesKey("highest_speed_draw_score")
        val SPEED_DRAW_COUNT = intPreferencesKey("speed_draw_count")
        val HIGHEST_ENDLESS_MODE_SCORE = intPreferencesKey("highest_endless_mode_score")
        val ENDLESS_MODE_COUNT = intPreferencesKey("endless_mode_count")
    }

    val highestSpeedDrawScore: Flow<Int> = dataStore.data
        .catch { exception ->
            // Handle potential IOExceptions when reading data
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            // Return the score or 0 if it doesn't exist
            preferences[PreferencesKeys.HIGHEST_SPEED_DRAW_SCORE] ?: 0
        }

    val speedDrawCount: Flow<Int> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.SPEED_DRAW_COUNT] ?: 0
        }

    val highestEndlessModeScore: Flow<Int> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.HIGHEST_ENDLESS_MODE_SCORE] ?: 0
        }

    val endlessModeCount: Flow<Int> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.ENDLESS_MODE_COUNT] ?: 0
        }

    /**
     * Updates the highest speed draw score only if the new score is higher.
     */
    suspend fun updateHighestSpeedDrawScore(newScore: Int) {
        dataStore.edit { preferences ->
            val currentScore = preferences[PreferencesKeys.HIGHEST_SPEED_DRAW_SCORE] ?: 0
            if (newScore > currentScore) {
                preferences[PreferencesKeys.HIGHEST_SPEED_DRAW_SCORE] = newScore
            }
        }
    }

    /**
     * Updates the count of completed speed draw games if the new count is higher.
     */
    suspend fun updateSpeedDrawCount(newCount: Int) {
        dataStore.edit { preferences ->
            val currentCount = preferences[PreferencesKeys.SPEED_DRAW_COUNT] ?: 0
            if (newCount > currentCount) {
                preferences[PreferencesKeys.SPEED_DRAW_COUNT] = newCount
            }
        }
    }

    /**
     * Updates the highest endless mode score only if the new score is higher.
     */
    suspend fun updateHighestEndlessModeScore(newScore: Int) {
        dataStore.edit { preferences ->
            val currentScore = preferences[PreferencesKeys.HIGHEST_ENDLESS_MODE_SCORE] ?: 0
            if (newScore > currentScore) {
                preferences[PreferencesKeys.HIGHEST_ENDLESS_MODE_SCORE] = newScore
            }
        }
    }

    /**
     * Increments the count of completed endless mode games.
     */
    suspend fun incrementEndlessModeCount() {
        dataStore.edit { preferences ->
            val currentCount = preferences[PreferencesKeys.ENDLESS_MODE_COUNT] ?: 0
            preferences[PreferencesKeys.ENDLESS_MODE_COUNT] = currentCount + 1
        }
    }
} 