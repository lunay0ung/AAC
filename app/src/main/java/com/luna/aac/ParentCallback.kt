package com.luna.aac

import com.luna.aac.data.Expression

interface ParentCallback {
    fun onBackMainScreen()
    fun onSelectExpression(expression: Expression)
}