import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.cineplusapp.cineplusspaapp.domain.model.MovieUi
import androidx.hilt.navigation.compose.hiltViewModel
import com.cineplusapp.cineplusspaapp.viewmodel.MovieViewModel

@Composable
fun MovieDetailScreen(
    movieId: Int,
    onBack: () -> Unit,
    vm: MovieViewModel = hiltViewModel()
) {
    val movie: MovieUi? by vm.byId(movieId).collectAsState(initial = null)

    LaunchedEffect(movie) {
        android.util.Log.d(
            "MovieDetail",
            "Movie detail: titulo=${movie?.titulo}, posterUrl=${movie?.posterUrl}"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ---------- IMAGEN DEL POSTER ----------
        AsyncImage(
            model = movie?.posterUrl,
            contentDescription = "Poster de ${movie?.titulo}",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(16.dp))

        // ---------- TÍTULO ----------
        Text(
            text = movie?.titulo ?: "Cargando…",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(8.dp))

        // ---------- GÉNERO ----------
        if (!movie?.genero.isNullOrBlank()) {
            Text("Género: ${movie!!.genero}")
            Spacer(Modifier.height(4.dp))
        }

        // ---------- SINOPSIS ----------
        if (!movie?.sinopsis.isNullOrBlank()) {
            Spacer(Modifier.height(12.dp))
            Text(text = movie!!.sinopsis!!)
        }

        Spacer(Modifier.height(24.dp))

        Button(onClick = onBack) {
            Text("Volver")
        }
    }
}
