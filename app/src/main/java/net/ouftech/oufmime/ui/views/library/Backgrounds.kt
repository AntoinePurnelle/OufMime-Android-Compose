/*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     https://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package net.ouftech.oufmime.ui.views.library

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp
import net.ouftech.oufmime.ui.theme.BlueTeam
import net.ouftech.oufmime.ui.theme.OrangeTeam

@Composable
fun Backgrounded(content: @Composable () -> Unit) = BackgroundSurface(color = MaterialTheme.colorScheme.background, content = content)

@Composable
fun SplitBackgrounded(content: @Composable () -> Unit) {
    BackgroundSurface {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val rect = Rect(Offset.Zero, size)

            val topSplit = rect.topRight.copy(x = rect.topRight.x * 3 / 4)
            val centerLeftSplit = Offset(x = 0.45f * size.width, y = 0.52f * size.height)
            val centerRightSplit = Offset(x = 0.55f * size.width, y = 0.48f * size.height)
            val bottomSplit = rect.bottomRight.copy(x = rect.topRight.x / 4)

            drawBackgroundPath(
                listOf(rect.topLeft, topSplit, centerLeftSplit, centerRightSplit, bottomSplit, rect.bottomLeft),
                OrangeTeam
            )
            drawBackgroundPath(
                listOf(topSplit, rect.topRight, rect.bottomRight, bottomSplit, centerRightSplit, centerLeftSplit),
                BlueTeam
            )
        }

        content()
    }
}

@Composable
private fun BackgroundSurface(color: Color = MaterialTheme.colorScheme.background, content: @Composable () -> Unit) =
    Surface(shape = RoundedCornerShape(40.dp), color = color, content = content)

private fun DrawScope.drawBackgroundPath(
    positions: List<Offset>,
    backgroundColor: Color
) {
    val path = Path().apply {
        moveTo(positions[0])
        (1 until positions.size).forEach { lineTo(positions[it]) }
    }

    drawIntoCanvas { canvas ->
        canvas.drawOutline(
            outline = Outline.Generic(path),
            paint = Paint().apply { color = backgroundColor }
        )
    }
}

fun Path.moveTo(offset: Offset) = moveTo(offset.x, offset.y)
fun Path.lineTo(offset: Offset) = lineTo(offset.x, offset.y)