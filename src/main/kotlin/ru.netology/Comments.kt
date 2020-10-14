package ru.netology

data class Comments(
    val canPost: Boolean = true,
    val groupsCanPost: Boolean = true,
    val canClose: Boolean = true,
    val canOpen: Boolean = true,
    val comments: Array<Comment> = emptyArray()
){
    val count: Int = comments.size
}