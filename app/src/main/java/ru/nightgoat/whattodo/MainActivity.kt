package ru.nightgoat.whattodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.nightgoat.whattodo.ui.theme.WhatToDoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    WhatToDoTheme {
        val todos: MutableList<TodoItem> = remember {
            mutableStateListOf()
        }
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    ToDoList(todos = todos) { item ->
                        todos.toList().find { item.index == it.index }?.let {
                            todos.removeAt(it.index)
                            todos.add(it.index, it)
                        }
                    }
                }
                AddTodoItem(todos) {
                    todos.add(it)
                }
            }
        }
    }
}

@Composable
fun ToDoList(todos: List<TodoItem>, onItemChange: (TodoItem) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.Top,
    ) {
        items(todos) {
            TodoItemUi(
                item = it,
                onItemChange = onItemChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(1f)
                    .padding(4.dp)
            )
        }
    }
}

@Composable
fun TodoItemUi(item: TodoItem, onItemChange: (TodoItem) -> Unit, modifier: Modifier = Modifier) {
    val inputText = remember { mutableStateOf(item.description) }
    Column {
        Text(text = item.title, modifier = modifier)
        OutlinedTextField(
            value = inputText.value,
            onValueChange = {
                inputText.value = it
                onItemChange(item.changeDescription(it))
            },
            modifier = modifier
        )
        Divider()
    }
}

@Composable
fun AddTodoItem(todos: List<TodoItem>, addNewItem: (TodoItem) -> Unit) {
    val inputText = remember { mutableStateOf(TextFieldValue()) }
    Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.padding(8.dp)) {
        OutlinedTextField(
            value = inputText.value,
            onValueChange = {
                inputText.value = it
            },
            label = {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = "todo's title",
                        style = MaterialTheme.typography.body2
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth(),
            textStyle = MaterialTheme.typography.body2,
        )
        Button(
            onClick = {
                val newItem = TodoItem(
                    index = todos.size,
                    title = inputText.value.text,
                    description = "Hello Android!"
                )
                addNewItem(newItem)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(top = 8.dp)
        ) {
            Text(text = "Add new todo")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApp()
}

data class TodoItem(
    val index: Int,
    val title: String,
    val description: String
) {
    fun changeDescription(newDescription: String) =
        copy(description = newDescription)
}