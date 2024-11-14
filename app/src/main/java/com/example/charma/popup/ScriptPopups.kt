package com.example.charma.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.charma.R
import com.example.charma.scripts.Article
import com.example.charma.scripts.Scraper
import com.example.charma.ui.theme.NinerGold
import com.example.charma.ui.theme.UNCCGreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ArticleListPopup() {
    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var articles by remember { mutableStateOf<List<Article>>(emptyList()) }
    val state = rememberScrollState()

    // Fetch articles in the background
    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            articles = Scraper.scrapeArticles()
        }
    }

    //TODO - Fix the Newspaper Icon
    // Newspaper Icon
    IconButton(onClick = { showDialog = true }) {
        Icon(
            painter = painterResource(id = R.drawable.greennews), // Replace with your icon resource
            contentDescription = "News",
        )
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(color = NinerGold, fontWeight = FontWeight.Bold)) {
                                append("N")
                            }
                            withStyle(style = SpanStyle(color = UNCCGreen, fontWeight = FontWeight.Bold)) {
                                append("iner ")
                            }
                            withStyle(style = SpanStyle(color = NinerGold, fontWeight = FontWeight.Bold)) {
                                append("N")
                            }
                            withStyle(style = SpanStyle(color = UNCCGreen, fontWeight = FontWeight.Bold)) {
                                append("ews")
                            }
                        }
                    )
                }
            },
            text = {
                if (articles.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight(0.7f)
                            .verticalScroll(state)
                    ) {
                        articles.forEach { article ->
                            ArticleCard(article = article)
                            HorizontalDivider(thickness = 0.5.dp, color = Color.Gray)
                        }
                    }
                } else {
                    Text("Loading articles...")
                }
            },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun ArticleCard(article: Article) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { openUrl(context, article.url) }
    ) {
        // Use Material 3 typography styles
        Text(
            text = article.title,
            style = MaterialTheme.typography.titleMedium // equivalent to h6
        )
        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = article.date,
            style = MaterialTheme.typography.labelSmall, // equivalent to body2
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = article.excerpt,
            style = MaterialTheme.typography.bodyMedium // equivalent to body1
        )
        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Read more",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelMedium // new style for emphasis
        )
    }
}


private fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}
