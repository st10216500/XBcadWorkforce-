import android.content.Context
import com.example.iccchurch.Note
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class NotesStorage(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("notes_storage", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveNotes(notes: List<Note>) {
        val notesJson = gson.toJson(notes)
        sharedPreferences.edit().putString("notes", notesJson).apply()
    }

    fun getNotes(): List<Note> {
        val notesJson = sharedPreferences.getString("notes", null)
        return if (notesJson != null) {
            val type = object : TypeToken<List<Note>>() {}.type
            gson.fromJson(notesJson, type)
        } else {
            emptyList()
        }
    }
}
