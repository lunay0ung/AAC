package com.luna.aac.data

import androidx.annotation.DrawableRes


sealed class Item {
    abstract fun getType(): Type
    abstract fun getDrawableResId(): Int
    abstract fun getItemTitle(): String
}

data class Category(
    @DrawableRes val image: Int,
    val title: String,
    val expressions: List<Expression>
) : Item() {
    override fun getType(): Type {
        return Type.TOPIC
    }

    override fun getDrawableResId(): Int {
        return image
    }

    override fun getItemTitle(): String {
        return title
    }
}

data class Expression(
    @DrawableRes val image: Int,
    val title: String
) : Item() {
    override fun getType(): Type {
        return Type.DETAIL
    }

    override fun getDrawableResId(): Int {
        return image
    }

    override fun getItemTitle(): String {
        return title
    }
}

enum class Type {
    TOPIC, DETAIL
}