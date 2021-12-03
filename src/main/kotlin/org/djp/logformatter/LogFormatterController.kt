package org.djp.logformatter

import com.github.vertical_blank.sqlformatter.SqlFormatter
import com.github.vertical_blank.sqlformatter.core.FormatConfig
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.TextArea
import javafx.scene.control.TextField

class LogFormatterController {
    @FXML
    protected lateinit var textFieldStatement: TextField

    @FXML
    protected lateinit var textFieldParameters: TextField

    @FXML
    protected lateinit var textFieldTypes: TextField

    @FXML
    protected lateinit var textAreaSql: TextArea

    protected var statement: String
        get() = textFieldStatement.text
        set(value) {
            textFieldStatement.text = value
        }

    protected var parameters: String
        get() = textFieldParameters.text
        set(value) {
            textFieldParameters.text = value
        }

    protected var types: String
        get() = textFieldTypes.text
        set(value) {
            textFieldTypes.text = value
        }

    protected var sql: String
        get() = textAreaSql.text
        set(value) {
            textAreaSql.text = value
        }

    @FXML
    fun onGenerate(actionEvent: ActionEvent) {
        val sqlBuilder = StringBuilder(statement.substring(statement.indexOf("SELECT")).trim())
        val parameterList = parameters
            .substring(parameters.indexOfFirst { it == '[' } + 1, parameters.indexOfLast { it == ']' })
            .split(',')
            .map(String::trim)
        val typeList = types
            .substring(types.indexOfFirst { it == '[' } + 1, types.indexOfLast { it == ']' })
            .split(',')
            .map(String::trim)
        val varCount = sqlBuilder.count { it == '?' }
        assert(varCount == parameterList.size && varCount == typeList.size)
        var next: Int = -1
        for (i in 0 until varCount) {
            next = sqlBuilder.indexOf('?', next + 1)
            when (typeList[i]) {
                "java.lang.Integer" -> sqlBuilder.replace(next, next + 1, parameterList[i])
                "java.lang.Float" -> sqlBuilder.replace(next, next + 1, parameterList[i])
                "java.lang.Double" -> sqlBuilder.replace(next, next + 1, parameterList[i])
                else -> sqlBuilder.replace(next, next + 1, "'${parameterList[i]}'")
            }
        }
        sql = SqlFormatter.format(
            sqlBuilder.toString(),
            FormatConfig.builder()
                .indent("  ")
                .uppercase(true)
                .linesBetweenQueries(1)
                .maxColumnLength(Int.MAX_VALUE)
                .build()
        )
    }
}