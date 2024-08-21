package com.sample

import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Table
import org.jetbrains.compose.web.dom.Tbody
import org.jetbrains.compose.web.dom.Td
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Tr
import org.jetbrains.compose.web.renderComposable

var idCounter = 1

data class Row(val id: Int, val label: String)

fun buildData(count: Int): List<Row> {
    return List(count, init = {
        Row(
            idCounter++,
            "${adjectives.random()} ${colours.random()} ${nouns.random()}"
        )
    })
}

val adjectives = arrayOf(
    "pretty",
    "large",
    "big",
    "small",
    "tall",
    "short",
    "long",
    "handsome",
    "plain",
    "quaint",
    "clean",
    "elegant",
    "easy",
    "angry",
    "crazy",
    "helpful",
    "mushy",
    "odd",
    "unsightly",
    "adorable",
    "important",
    "inexpensive",
    "cheap",
    "expensive",
    "fancy"
)
val colours = arrayOf("red", "yellow", "blue", "green", "pink", "brown", "purple", "brown", "white", "black", "orange")
val nouns = arrayOf(
    "table",
    "chair",
    "house",
    "bbq",
    "desk",
    "car",
    "pony",
    "cookie",
    "sandwich",
    "burger",
    "pizza",
    "mouse",
    "keyboard"
)

fun main() {
    var data by mutableStateOf(emptyList<Row>())
    var selected by mutableStateOf<Row?>(null)

    renderComposable(rootElementId = "root") {
        Div({ id("main") }) {
            Div({ classes("container") }) {
                Div({ classes("jumbotron") }) {
                    Div({ classes("row") }) {
                        Div({ classes("col-md-6") }) {
                            H1 { Text("Compose HTML") }
                        }
                        Div({ classes("col-md-6") }) {
                            Div({ classes("row") }) {
                                Div({ classes("col-sm-6", "smallpad") }) {
                                    Button(
                                        {
                                            id("run")
                                            classes("btn", "btn-primary", "btn-block")
                                            onClick {
                                                data = buildData(1000)
                                            }
                                        }
                                    ) { Text("Create 1,000 rows") }
                                }
                                Div({ classes("col-sm-6", "smallpad") }) {
                                    Button(
                                        {
                                            id("runlots")
                                            classes("btn", "btn-primary", "btn-block")
                                            onClick {
                                                data = buildData(10_000)
                                            }
                                        }
                                    ) { Text("Create 10,000 rows") }
                                }
                                Div({ classes("col-sm-6", "smallpad") }) {
                                    Button(
                                        {
                                            id("add")
                                            classes("btn", "btn-primary", "btn-block")
                                            onClick {
                                                data += buildData(1000)
                                            }
                                        }
                                    ) { Text("Append 1,000 rows") }
                                }
                                Div({ classes("col-sm-6", "smallpad") }) {
                                    Button(
                                        {
                                            id("update")
                                            classes("btn", "btn-primary", "btn-block")
                                            onClick {
                                                data = data.mapIndexed { index, row ->
                                                    if (index % 10 == 0) {
                                                        row.copy(label = "${row.label} !!!")
                                                    } else {
                                                        row
                                                    }
                                                }
                                            }
                                        }
                                    ) { Text("Update every 10th row") }
                                }
                                Div({ classes("col-sm-6", "smallpad") }) {
                                    Button(
                                        {
                                            id("clear")
                                            classes("btn", "btn-primary", "btn-block")
                                            onClick {
                                                data = emptyList()
                                            }
                                        }
                                    ) { Text("Clear") }
                                }
                                Div({ classes("col-sm-6", "smallpad") }) {
                                    Button(
                                        {
                                            id("swaprows")
                                            classes("btn", "btn-primary", "btn-block")
                                            onClick {
                                                if (data.size > 998) {
                                                    val newData = data.toTypedArray()
                                                    val temp = newData[1]
                                                    newData[1] = newData[998]
                                                    newData[998] = temp
                                                    data = newData.asList()
                                                }
                                            }
                                        }
                                    ) { Text("Swap Rows") }
                                }
                            }
                        }
                    }
                }
                Table({ classes("table", "table-hover", "table-striped", "test-data") }) {
                    Tbody {
                        for (chunk in data.chunked(100)) {
                            key("${chunk.first().id}#${chunk.last().id}#${chunk.size}") {
                                for (item in chunk) {
                                    Tr({ if (selected?.id == item.id) classes("danger") }) {
                                        Td({ classes("col-md-1") }) { Text(item.id.toString()) }
                                        Td({ classes("col-md-4") }) {
                                            A(attrs = {
                                                onClick {
                                                    selected = item
                                                }
                                            }) { Text(item.label) }
                                        }
                                        Td({ classes("col-md-1") }) {
                                            A(attrs = {
                                                onClick {
                                                    val newData = data.toMutableList()
                                                    newData.remove(item)
                                                    data = newData
                                                }
                                            }) {
                                                Span({
                                                    classes(
                                                        "glyphicon",
                                                        "glyphicon-remove"
                                                    )
                                                    attr("aria-hidden", "true")
                                                })
                                            }
                                        }
                                        Td({ classes("col-md-6") })
                                    }
                                }
                            }
                        }
                    }
                }

                Span({
                    classes("preloadicon", "glyphicon", "glyphicon-remove")
                    attr("aria-hidden", "true")
                })
            }
        }
    }
}
