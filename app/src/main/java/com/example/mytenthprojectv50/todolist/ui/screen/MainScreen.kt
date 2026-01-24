package com.example.mytenthprojectv50.todolist.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SwipeToDismissBoxValue.EndToStart
import androidx.compose.material3.SwipeToDismissBoxValue.Settled
import androidx.compose.material3.SwipeToDismissBoxValue.StartToEnd
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Add
import com.example.mytenthprojectv50.todolist.data.local.entity.TodoEntity
import androidx.compose.foundation.layout.size
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.FocusRequester
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FloatingActionButton
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.border
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.text.TextStyle
import androidx.compose.material3.lightColorScheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.animation.core.RepeatMode
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material3.SnackbarHost
import androidx.compose.animation.core.tween
import androidx.compose.material3.SnackbarResult
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Brush
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.ui.geometry.Offset
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.ui.graphics.TileMode

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val todos by viewModel.todos.collectAsStateWithLifecycle()
    var isAdding by remember { mutableStateOf(false) }
    var newTaskDescription by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { Header()},
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                        onClick = { isAdding = true },
                modifier = Modifier
                    .border(1.dp, color = MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(16.dp))

            ) {


                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Добавить задачу"
                )

            }
        },

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            TodoLazyColumn(
                todos = todos,
                isAdding = isAdding,
                newTaskDescription = newTaskDescription,
                onTextChange = { newTaskDescription = it },
                onComplete = { viewModel.toggleCompleted(it.id) },
                onDelete = { todo ->
                     scope.launch {

                         val itemToDelete = todo
                         viewModel.deleteTodo(todo.id)

                         val result = snackbarHostState.showSnackbar(
                             message = "Задача удалена",
                             actionLabel = "Отменить",
                             duration = SnackbarDuration.Short
                         )

                         if (result == SnackbarResult.ActionPerformed) {


                             viewModel.restoreTodo(itemToDelete)
                         }
                     }
                           },
                onEditDone = { entity, newText ->
                    viewModel.editTodo(entity.copy(title = newText))
                },
                onAddDone = {
                    if (newTaskDescription.isNotBlank()) {
                        viewModel.addTodo(newTaskDescription)
                        newTaskDescription = ""
                        isAdding = false
                    }
                },
                modifier = Modifier
                    .fillMaxSize(),

                contentPadding = PaddingValues(0.dp)

            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header() {
    Surface(
        shape =  (RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
    ){
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "Ваши заметки",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 26.sp
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
                .border( 1.dp, Color(0xFF000000), shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))

        )
    }
}

@Composable
fun TodoListItemWithAnimation(
    entity: TodoEntity,
    onComplete: (TodoEntity) -> Unit,
    onDelete: (TodoEntity) -> Unit,
    onEditDone: (TodoEntity, String) -> Unit,
    modifier: Modifier = Modifier,
) {

    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2000f, // Величина смещения (подберите под размер)
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerOffset"
    )

    // 2. Анимация появления при выполнении (как была раньше)
    val animationProgress by animateFloatAsState(
        targetValue = if (entity.isCompleted) 1f else 0f,
        animationSpec = tween(500),
        label = "CompletedAnimation"
    )

    var isEditing by remember { mutableStateOf(false) }
    var editText by remember { mutableStateOf(entity.title) }

    val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
        confirmValueChange = { direction ->
            when (direction) {
                StartToEnd -> {
                    onComplete(entity)
                    false
                }
                EndToStart -> {
                    onDelete(entity)
                    true
                }
                else -> false
            }
        }
    )

    SwipeToDismissBox(
        state = swipeToDismissBoxState,

        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        isEditing = true
                    }
                )
            } ,
        backgroundContent = {

            val startComplete = Color(0xFF0DFF00)
            val endComplete = Color(0xFF226B03)
            val startDelete = Color(0xFFFF002A)
            val endDelete = Color(0xFF860606)
            when (swipeToDismissBoxState.dismissDirection) {
                StartToEnd -> {
                    Icon(
                        if (entity.isCompleted) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                        contentDescription = if (entity.isCompleted) "Done" else "Not done",
                        modifier = Modifier
                            .padding(vertical = 8.dp, horizontal = 6.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .fillMaxSize()
                            .drawBehind {
                                drawRect(
                                    lerp(
                                        startComplete,
                                        endComplete,
                                        swipeToDismissBoxState.progress
                                    )
                                )
                            }
                            .wrapContentSize(Alignment.CenterStart)
                            .padding(start = 18.dp  )
                            .size(36.dp),
                        tint = Color.White
                    )
                }
                EndToStart -> {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove item",
                        modifier = Modifier
                            .padding(vertical = 8.dp, horizontal = 6.dp)

                            .clip(RoundedCornerShape(12.dp))
                            .fillMaxSize()
                            .background(
                                lerp(
                                    startDelete,
                                    endDelete,
                                    swipeToDismissBoxState.progress
                                )
                            )
                            .wrapContentSize(Alignment.CenterEnd)
                            .padding(end = 18.dp  )
                            .size(36.dp),
                        tint = Color.White
                    )
                }
                Settled -> {}
            }
        }
    ) {
        ElevatedCard(
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation( defaultElevation = 6.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .padding(vertical = 8.dp, horizontal = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawBehind {
                        if (animationProgress > 0f) {
                            // Цвета градиента
                            val colors = listOf(
                                Color(0xFF0DFF00).copy(alpha = 0.2f * animationProgress),
                                Color(0xFFFFFFFF).copy(alpha = 0.5f * animationProgress), // "Блик" посередине
                                Color(0xFF226B03).copy(alpha = 0.3f * animationProgress)
                            )

                            // Настройка 45 градусов + смещение анимации
                            val brush = Brush.linearGradient(
                                colors = colors,
                                // Смещение shimmerOffset заставляет градиент "ехать"
                                start = Offset(shimmerOffset, shimmerOffset),
                                end = Offset(shimmerOffset + size.width, shimmerOffset + size.height),
                                tileMode = TileMode.Mirror // Повторяет градиент бесконечно
                            )

                            drawRect(brush = brush)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {

                if (isEditing) {
                    OutlinedTextField(
                        value = editText,
                        onValueChange = { editText = it },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                onEditDone(entity, editText)
                                isEditing = false
                            }
                        ),

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = entity.title,
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TodoLazyColumn(
    todos: List<TodoEntity>,
    isAdding: Boolean,
    newTaskDescription: String,
    onTextChange: (String) -> Unit,
    onComplete: (TodoEntity) -> Unit,
    onDelete: (TodoEntity) -> Unit,
    onEditDone: (TodoEntity, String) -> Unit,
    onAddDone: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val listState = rememberLazyListState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(isAdding) {
        if (isAdding) {

            listState.animateScrollToItem(0)
            try {
                focusRequester.requestFocus()
            } catch (e: Exception) {
            }
        }
    }

    LazyColumn (
        state = listState,
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        if (isAdding) {
            item(key = "new_task") {
                ElevatedCard(
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation( defaultElevation = 6.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .padding(vertical = 8.dp, horizontal = 6.dp)
                ) {
                    OutlinedTextField(
                        value = newTaskDescription,
                        onValueChange = onTextChange,
                        label = { Text("Новая задача") },
                        textStyle = TextStyle(
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        ),

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)

                            .focusRequester(focusRequester),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { onAddDone() }),
                        singleLine = true,

                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary

                        )
                    )
                }

            }
        }

    items(
            items = todos,
            key = { it.id }
        ) { entity ->
            TodoListItemWithAnimation(
                entity =  entity,
                onComplete = onComplete,
                onDelete = onDelete,
                onEditDone = onEditDone,
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItem()
            )
        }
    }
}
private val LightMinimalColors = lightColorScheme(
    primary = Color(0xFFFFFFFF),
    onPrimary = Color(0xFF000000),
)

private val DarkMinimalColors = darkColorScheme(
    primary = Color(0xFF0B1838),
    onPrimary = Color(0xFFFFFFFF),
)

@Composable
fun MinimalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkMinimalColors else LightMinimalColors

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}

@Composable
fun AppContent() {
    MinimalTheme {
        MainScreen()  // тут весь твой UI
    }
}