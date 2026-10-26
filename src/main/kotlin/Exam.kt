import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.gson.gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.poi.ss.formula.functions.Column
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.FileInputStream
import java.io.FileOutputStream

@Composable
fun Exam() {
    // Пишите здесь основной код для выполнения вашего задания
    var data by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.padding(12.dp).fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedButton(
                onClick = {
                    scope.launch(Dispatchers.IO) {
                        data = runCatching {
                            data()
                        }.getOrElse {
                            it.localizedMessage
                        }
                    }
                },
                modifier = Modifier.weight(1f).padding(12.dp)
            ) {
                Text("Получить данные")
            }
            Text(text = data, Modifier.weight(1f).padding(start = 8.dp))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedButton(
                onClick = {
                    result = check(data)
                    write(data, result)
                },
                modifier = Modifier.weight(1f).padding(12.dp)
            ) {
                Text("Отправить результат теста")
            }
            Text(text = result, Modifier.weight(1f).padding(start = 8.dp))
        }
    }
}


//** Средства получения данных **//
// Клиент сетевого сервиса
val client = HttpClient {
    install(ContentNegotiation) {
        gson()
    }
}
// Адрес страницы для получения данных
const val URL = "http://localhost:4444/TransferSimulator"
// Класс данных для получения информации
class Data(
    val value: String
)
// Функция для получения искомого значения
suspend fun data() = client.get("$URL/email").body<Data>().value

//** Средства проверки строки на правильность **//
val regex = "[-.\\w]+@[-.\\w]+\\.[a-zA-Z]{2,6}".toRegex()
fun check(s: String): String = if (regex.matches(s)) "Успешно" else "Не успешно"


//** Средства записи результатов в файл **//
fun write(data: String, result: String) {
    val file = "ТестКейс.docx"

}