package com.coderbdk.worddetector.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coderbdk.worddetector.R

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    Box(
        Modifier
            .fillMaxSize()
            .background(
                color = Color(0xFF02152C)
            )
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(24.dp)
                .background(
                    color = Color(0xFF021C3A),
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 2.dp,
                    color = Color.Cyan,
                    shape = RoundedCornerShape(16.dp)
                )

        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                Icon(
                    modifier = Modifier.size(350.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.outline_search_24),
                    contentDescription = null,
                    tint = Color.Cyan
                )
                Icon(
                    modifier = Modifier
                        .size(350.dp)
                        .padding(start = 4.dp, top = 16.dp, end = 4.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.outline_search_24),
                    contentDescription = null,
                    tint = Color(0xFF021C3A)
                )

                Text(
                    "W",
                    fontSize = 100.sp,
                    color = Color.Cyan,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier.padding(bottom = 64.dp, end = 64.dp)
                )
            }

            Text(
                "WORD\nDETECTOR",
                fontSize = 56.sp,
                color = Color.Cyan,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Cyan.copy(0.8f), fontSize = 16.sp)) {
                        append("Detect")
                    }
                    withStyle(style = SpanStyle(color = Color(0xFF0A6DDE), fontSize = 24.sp)) {
                        append(" / ")
                    }
                    withStyle(style = SpanStyle(color = Color.Cyan.copy(0.8f), fontSize = 16.sp)) {
                        append("Highlight")
                    }
                    withStyle(style = SpanStyle(color = Color(0xFF0A6DDE), fontSize = 24.sp)) {
                        append(" \\ ")
                    }
                    withStyle(style = SpanStyle(color = Color.Cyan.copy(0.8f), fontSize = 16.sp)) {
                        append("Censor")
                    }

                }, textAlign = TextAlign.Center,
                fontWeight = FontWeight.Thin,
                fontFamily = FontFamily.Serif, modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashPreview() {
    SplashScreen()
}